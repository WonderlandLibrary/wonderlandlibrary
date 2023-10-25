package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.AppendableCharSequence;
import java.util.List;

public abstract class HttpObjectDecoder extends ReplayingDecoder<HttpObjectDecoder.State> {
  private final int maxInitialLineLength;
  
  private final int maxHeaderSize;
  
  private final int maxChunkSize;
  
  private final boolean chunkedSupported;
  
  protected final boolean validateHeaders;
  
  private final AppendableCharSequence seq = new AppendableCharSequence(128);
  
  private final HeaderParser headerParser = new HeaderParser(this.seq);
  
  private final LineParser lineParser = new LineParser(this.seq);
  
  private HttpMessage message;
  
  private long chunkSize;
  
  private int headerSize;
  
  private long contentLength = Long.MIN_VALUE;
  
  enum State {
    SKIP_CONTROL_CHARS, READ_INITIAL, READ_HEADER, READ_VARIABLE_LENGTH_CONTENT, READ_FIXED_LENGTH_CONTENT, READ_CHUNK_SIZE, READ_CHUNKED_CONTENT, READ_CHUNK_DELIMITER, READ_CHUNK_FOOTER, BAD_MESSAGE, UPGRADED;
  }
  
  protected HttpObjectDecoder() {
    this(4096, 8192, 8192, true);
  }
  
  protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported) {
    this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, true);
  }
  
  protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders) {
    super(State.SKIP_CONTROL_CHARS);
    if (maxInitialLineLength <= 0)
      throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + maxInitialLineLength); 
    if (maxHeaderSize <= 0)
      throw new IllegalArgumentException("maxHeaderSize must be a positive integer: " + maxHeaderSize); 
    if (maxChunkSize <= 0)
      throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize); 
    this.maxInitialLineLength = maxInitialLineLength;
    this.maxHeaderSize = maxHeaderSize;
    this.maxChunkSize = maxChunkSize;
    this.chunkedSupported = chunkedSupported;
    this.validateHeaders = validateHeaders;
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
    int i;
    int readLimit;
    int toRead;
    int readableBytes;
    int j;
    HttpContent chunk;
    ByteBuf content;
    switch ((State)state()) {
      case SKIP_CONTROL_CHARS:
        try {
          skipControlCharacters(buffer);
          checkpoint(State.READ_INITIAL);
        } finally {
          checkpoint();
        } 
      case READ_INITIAL:
        try {
          String[] initialLine = splitInitialLine(this.lineParser.parse(buffer));
          if (initialLine.length < 3) {
            checkpoint(State.SKIP_CONTROL_CHARS);
            return;
          } 
          this.message = createMessage(initialLine);
          checkpoint(State.READ_HEADER);
        } catch (Exception e) {
          out.add(invalidMessage(e));
          return;
        } 
      case READ_HEADER:
        try {
          State nextState = readHeaders(buffer);
          checkpoint(nextState);
          if (nextState == State.READ_CHUNK_SIZE) {
            if (!this.chunkedSupported)
              throw new IllegalArgumentException("Chunked messages not supported"); 
            out.add(this.message);
            return;
          } 
          if (nextState == State.SKIP_CONTROL_CHARS) {
            out.add(this.message);
            out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            reset();
            return;
          } 
          long contentLength = contentLength();
          if (contentLength == 0L || (contentLength == -1L && isDecodingRequest())) {
            out.add(this.message);
            out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            reset();
            return;
          } 
          assert nextState == State.READ_FIXED_LENGTH_CONTENT || nextState == State.READ_VARIABLE_LENGTH_CONTENT;
          out.add(this.message);
          if (nextState == State.READ_FIXED_LENGTH_CONTENT)
            this.chunkSize = contentLength; 
          return;
        } catch (Exception e) {
          out.add(invalidMessage(e));
          return;
        } 
      case READ_VARIABLE_LENGTH_CONTENT:
        i = Math.min(actualReadableBytes(), this.maxChunkSize);
        if (i > 0) {
          ByteBuf byteBuf = ByteBufUtil.readBytes(ctx.alloc(), buffer, i);
          if (buffer.isReadable()) {
            out.add(new DefaultHttpContent(byteBuf));
          } else {
            out.add(new DefaultLastHttpContent(byteBuf, this.validateHeaders));
            reset();
          } 
        } else if (!buffer.isReadable()) {
          out.add(LastHttpContent.EMPTY_LAST_CONTENT);
          reset();
        } 
        return;
      case READ_FIXED_LENGTH_CONTENT:
        readLimit = actualReadableBytes();
        if (readLimit == 0)
          return; 
        j = Math.min(readLimit, this.maxChunkSize);
        if (j > this.chunkSize)
          j = (int)this.chunkSize; 
        content = ByteBufUtil.readBytes(ctx.alloc(), buffer, j);
        this.chunkSize -= j;
        if (this.chunkSize == 0L) {
          out.add(new DefaultLastHttpContent(content, this.validateHeaders));
          reset();
        } else {
          out.add(new DefaultHttpContent(content));
        } 
        return;
      case READ_CHUNK_SIZE:
        try {
          AppendableCharSequence line = this.lineParser.parse(buffer);
          int chunkSize = getChunkSize(line.toString());
          this.chunkSize = chunkSize;
          if (chunkSize == 0) {
            checkpoint(State.READ_CHUNK_FOOTER);
            return;
          } 
          checkpoint(State.READ_CHUNKED_CONTENT);
        } catch (Exception e) {
          out.add(invalidChunk(e));
          return;
        } 
      case READ_CHUNKED_CONTENT:
        assert this.chunkSize <= 2147483647L;
        toRead = Math.min((int)this.chunkSize, this.maxChunkSize);
        chunk = new DefaultHttpContent(ByteBufUtil.readBytes(ctx.alloc(), buffer, toRead));
        this.chunkSize -= toRead;
        out.add(chunk);
        if (this.chunkSize == 0L) {
          checkpoint(State.READ_CHUNK_DELIMITER);
        } else {
          return;
        } 
      case READ_CHUNK_DELIMITER:
        while (true) {
          byte next = buffer.readByte();
          if (next == 13) {
            if (buffer.readByte() == 10) {
              checkpoint(State.READ_CHUNK_SIZE);
              return;
            } 
            continue;
          } 
          if (next == 10) {
            checkpoint(State.READ_CHUNK_SIZE);
            return;
          } 
          checkpoint();
        } 
      case READ_CHUNK_FOOTER:
        try {
          LastHttpContent trailer = readTrailingHeaders(buffer);
          out.add(trailer);
          reset();
          return;
        } catch (Exception e) {
          out.add(invalidChunk(e));
          return;
        } 
      case BAD_MESSAGE:
        buffer.skipBytes(actualReadableBytes());
        break;
      case UPGRADED:
        readableBytes = actualReadableBytes();
        if (readableBytes > 0)
          out.add(buffer.readBytes(actualReadableBytes())); 
        break;
    } 
  }
  
  protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    decode(ctx, in, out);
    if (this.message != null) {
      boolean prematureClosure;
      if (isDecodingRequest()) {
        prematureClosure = true;
      } else {
        prematureClosure = (contentLength() > 0L);
      } 
      reset();
      if (!prematureClosure)
        out.add(LastHttpContent.EMPTY_LAST_CONTENT); 
    } 
  }
  
  protected boolean isContentAlwaysEmpty(HttpMessage msg) {
    if (msg instanceof HttpResponse) {
      HttpResponse res = (HttpResponse)msg;
      int code = res.getStatus().code();
      if (code >= 100 && code < 200)
        return (code != 101 || res.headers().contains("Sec-WebSocket-Accept")); 
      switch (code) {
        case 204:
        case 205:
        case 304:
          return true;
      } 
    } 
    return false;
  }
  
  private void reset() {
    HttpMessage message = this.message;
    this.message = null;
    this.contentLength = Long.MIN_VALUE;
    if (!isDecodingRequest()) {
      HttpResponse res = (HttpResponse)message;
      if (res != null && res.getStatus().code() == 101) {
        checkpoint(State.UPGRADED);
        return;
      } 
    } 
    checkpoint(State.SKIP_CONTROL_CHARS);
  }
  
  private HttpMessage invalidMessage(Exception cause) {
    checkpoint(State.BAD_MESSAGE);
    if (this.message != null) {
      this.message.setDecoderResult(DecoderResult.failure(cause));
    } else {
      this.message = createInvalidMessage();
      this.message.setDecoderResult(DecoderResult.failure(cause));
    } 
    HttpMessage ret = this.message;
    this.message = null;
    return ret;
  }
  
  private HttpContent invalidChunk(Exception cause) {
    checkpoint(State.BAD_MESSAGE);
    HttpContent chunk = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
    chunk.setDecoderResult(DecoderResult.failure(cause));
    this.message = null;
    return chunk;
  }
  
  private static void skipControlCharacters(ByteBuf buffer) {
    char c;
    do {
      c = (char)buffer.readUnsignedByte();
    } while (Character.isISOControl(c) || Character.isWhitespace(c));
    buffer.readerIndex(buffer.readerIndex() - 1);
  }
  
  private State readHeaders(ByteBuf buffer) {
    State nextState;
    this.headerSize = 0;
    HttpMessage message = this.message;
    HttpHeaders headers = message.headers();
    AppendableCharSequence line = this.headerParser.parse(buffer);
    String name = null;
    String value = null;
    if (line.length() > 0) {
      headers.clear();
      do {
        char firstChar = line.charAt(0);
        if (name != null && (firstChar == ' ' || firstChar == '\t')) {
          value = value + ' ' + line.toString().trim();
        } else {
          if (name != null)
            headers.add(name, value); 
          String[] header = splitHeader(line);
          name = header[0];
          value = header[1];
        } 
        line = this.headerParser.parse(buffer);
      } while (line.length() > 0);
      if (name != null)
        headers.add(name, value); 
    } 
    if (isContentAlwaysEmpty(message)) {
      HttpHeaders.removeTransferEncodingChunked(message);
      nextState = State.SKIP_CONTROL_CHARS;
    } else if (HttpHeaders.isTransferEncodingChunked(message)) {
      nextState = State.READ_CHUNK_SIZE;
    } else if (contentLength() >= 0L) {
      nextState = State.READ_FIXED_LENGTH_CONTENT;
    } else {
      nextState = State.READ_VARIABLE_LENGTH_CONTENT;
    } 
    return nextState;
  }
  
  private long contentLength() {
    if (this.contentLength == Long.MIN_VALUE)
      this.contentLength = HttpHeaders.getContentLength(this.message, -1L); 
    return this.contentLength;
  }
  
  private LastHttpContent readTrailingHeaders(ByteBuf buffer) {
    this.headerSize = 0;
    AppendableCharSequence line = this.headerParser.parse(buffer);
    String lastHeader = null;
    if (line.length() > 0) {
      LastHttpContent trailer = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
      do {
        char firstChar = line.charAt(0);
        if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
          List<String> current = trailer.trailingHeaders().getAll(lastHeader);
          if (!current.isEmpty()) {
            int lastPos = current.size() - 1;
            String newString = (String)current.get(lastPos) + line.toString().trim();
            current.set(lastPos, newString);
          } 
        } else {
          String[] header = splitHeader(line);
          String name = header[0];
          if (!HttpHeaders.equalsIgnoreCase(name, "Content-Length") && !HttpHeaders.equalsIgnoreCase(name, "Transfer-Encoding") && !HttpHeaders.equalsIgnoreCase(name, "Trailer"))
            trailer.trailingHeaders().add(name, header[1]); 
          lastHeader = name;
        } 
        line = this.headerParser.parse(buffer);
      } while (line.length() > 0);
      return trailer;
    } 
    return LastHttpContent.EMPTY_LAST_CONTENT;
  }
  
  private static int getChunkSize(String hex) {
    hex = hex.trim();
    for (int i = 0; i < hex.length(); i++) {
      char c = hex.charAt(i);
      if (c == ';' || Character.isWhitespace(c) || Character.isISOControl(c)) {
        hex = hex.substring(0, i);
        break;
      } 
    } 
    return Integer.parseInt(hex, 16);
  }
  
  private static String[] splitInitialLine(AppendableCharSequence sb) {
    int aStart = findNonWhitespace((CharSequence)sb, 0);
    int aEnd = findWhitespace((CharSequence)sb, aStart);
    int bStart = findNonWhitespace((CharSequence)sb, aEnd);
    int bEnd = findWhitespace((CharSequence)sb, bStart);
    int cStart = findNonWhitespace((CharSequence)sb, bEnd);
    int cEnd = findEndOfString((CharSequence)sb);
    return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), (cStart < cEnd) ? sb.substring(cStart, cEnd) : "" };
  }
  
  private static String[] splitHeader(AppendableCharSequence sb) {
    int length = sb.length();
    int nameStart = findNonWhitespace((CharSequence)sb, 0);
    int nameEnd;
    for (nameEnd = nameStart; nameEnd < length; nameEnd++) {
      char ch = sb.charAt(nameEnd);
      if (ch == ':' || Character.isWhitespace(ch))
        break; 
    } 
    int colonEnd;
    for (colonEnd = nameEnd; colonEnd < length; colonEnd++) {
      if (sb.charAt(colonEnd) == ':') {
        colonEnd++;
        break;
      } 
    } 
    int valueStart = findNonWhitespace((CharSequence)sb, colonEnd);
    if (valueStart == length)
      return new String[] { sb.substring(nameStart, nameEnd), "" }; 
    int valueEnd = findEndOfString((CharSequence)sb);
    return new String[] { sb.substring(nameStart, nameEnd), sb.substring(valueStart, valueEnd) };
  }
  
  private static int findNonWhitespace(CharSequence sb, int offset) {
    int result;
    for (result = offset; result < sb.length() && 
      Character.isWhitespace(sb.charAt(result)); result++);
    return result;
  }
  
  private static int findWhitespace(CharSequence sb, int offset) {
    int result;
    for (result = offset; result < sb.length() && 
      !Character.isWhitespace(sb.charAt(result)); result++);
    return result;
  }
  
  private static int findEndOfString(CharSequence sb) {
    int result;
    for (result = sb.length(); result > 0 && 
      Character.isWhitespace(sb.charAt(result - 1)); result--);
    return result;
  }
  
  protected abstract boolean isDecodingRequest();
  
  protected abstract HttpMessage createMessage(String[] paramArrayOfString) throws Exception;
  
  protected abstract HttpMessage createInvalidMessage();
  
  private final class HeaderParser implements ByteBufProcessor {
    private final AppendableCharSequence seq;
    
    HeaderParser(AppendableCharSequence seq) {
      this.seq = seq;
    }
    
    public AppendableCharSequence parse(ByteBuf buffer) {
      this.seq.reset();
      HttpObjectDecoder.this.headerSize = 0;
      int i = buffer.forEachByte(this);
      buffer.readerIndex(i + 1);
      return this.seq;
    }
    
    public boolean process(byte value) throws Exception {
      char nextByte = (char)value;
      HttpObjectDecoder.this.headerSize++;
      if (nextByte == '\r')
        return true; 
      if (nextByte == '\n')
        return false; 
      if (HttpObjectDecoder.this.headerSize >= HttpObjectDecoder.this.maxHeaderSize)
        throw new TooLongFrameException("HTTP header is larger than " + HttpObjectDecoder.this.maxHeaderSize + " bytes."); 
      this.seq.append(nextByte);
      return true;
    }
  }
  
  private final class LineParser implements ByteBufProcessor {
    private final AppendableCharSequence seq;
    
    private int size;
    
    LineParser(AppendableCharSequence seq) {
      this.seq = seq;
    }
    
    public AppendableCharSequence parse(ByteBuf buffer) {
      this.seq.reset();
      this.size = 0;
      int i = buffer.forEachByte(this);
      buffer.readerIndex(i + 1);
      return this.seq;
    }
    
    public boolean process(byte value) throws Exception {
      char nextByte = (char)value;
      if (nextByte == '\r')
        return true; 
      if (nextByte == '\n')
        return false; 
      if (this.size >= HttpObjectDecoder.this.maxInitialLineLength)
        throw new TooLongFrameException("An HTTP line is larger than " + HttpObjectDecoder.this.maxInitialLineLength + " bytes."); 
      this.size++;
      this.seq.append(nextByte);
      return true;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\HttpObjectDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */