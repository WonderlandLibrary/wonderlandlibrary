/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.internal.AppendableCharSequence;
import java.util.List;

public abstract class HttpObjectDecoder
extends ReplayingDecoder<State> {
    private final int maxHeaderSize;
    private HttpMessage message;
    private final AppendableCharSequence seq = new AppendableCharSequence(128);
    private int headerSize;
    private final int maxChunkSize;
    private final LineParser lineParser;
    private final int maxInitialLineLength;
    private final boolean chunkedSupported;
    private final HeaderParser headerParser = new HeaderParser(this.seq);
    protected final boolean validateHeaders;
    private long contentLength;
    private long chunkSize;

    @Override
    protected void decodeLast(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        this.decode(channelHandlerContext, byteBuf, list);
        if (this.message != null) {
            boolean bl = this.isDecodingRequest() ? true : this.contentLength() > 0L;
            this.reset();
            if (!bl) {
                list.add(LastHttpContent.EMPTY_LAST_CONTENT);
            }
        }
    }

    private HttpContent invalidChunk(Exception exception) {
        this.checkpoint(State.BAD_MESSAGE);
        DefaultLastHttpContent defaultLastHttpContent = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        defaultLastHttpContent.setDecoderResult(DecoderResult.failure(exception));
        this.message = null;
        return defaultLastHttpContent;
    }

    protected abstract HttpMessage createInvalidMessage();

    protected abstract HttpMessage createMessage(String[] var1) throws Exception;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        switch ((State)((Object)this.state())) {
            case SKIP_CONTROL_CHARS: {
                HttpObjectDecoder.skipControlCharacters(byteBuf);
                this.checkpoint(State.READ_INITIAL);
                this.checkpoint();
            }
            case READ_INITIAL: {
                String[] stringArray;
                try {
                    stringArray = HttpObjectDecoder.splitInitialLine(this.lineParser.parse(byteBuf));
                    if (stringArray.length < 3) {
                        this.checkpoint(State.SKIP_CONTROL_CHARS);
                        return;
                    }
                    this.message = this.createMessage(stringArray);
                    this.checkpoint(State.READ_HEADER);
                }
                catch (Exception exception) {
                    list.add(this.invalidMessage(exception));
                    return;
                }
            }
            case READ_HEADER: {
                String[] stringArray;
                try {
                    stringArray = this.readHeaders(byteBuf);
                    this.checkpoint(stringArray);
                    if (stringArray == State.READ_CHUNK_SIZE) {
                        if (!this.chunkedSupported) {
                            throw new IllegalArgumentException("Chunked messages not supported");
                        }
                        list.add(this.message);
                        return;
                    }
                    if (stringArray == State.SKIP_CONTROL_CHARS) {
                        list.add(this.message);
                        list.add(LastHttpContent.EMPTY_LAST_CONTENT);
                        this.reset();
                        return;
                    }
                    long l = this.contentLength();
                    if (l == 0L || l == -1L && this.isDecodingRequest()) {
                        list.add(this.message);
                        list.add(LastHttpContent.EMPTY_LAST_CONTENT);
                        this.reset();
                        return;
                    }
                    assert (stringArray == State.READ_FIXED_LENGTH_CONTENT || stringArray == State.READ_VARIABLE_LENGTH_CONTENT);
                    list.add(this.message);
                    if (stringArray == State.READ_FIXED_LENGTH_CONTENT) {
                        this.chunkSize = l;
                    }
                    return;
                }
                catch (Exception exception) {
                    list.add(this.invalidMessage(exception));
                    return;
                }
            }
            case READ_VARIABLE_LENGTH_CONTENT: {
                int n = Math.min(this.actualReadableBytes(), this.maxChunkSize);
                if (n > 0) {
                    ByteBuf byteBuf2 = ByteBufUtil.readBytes(channelHandlerContext.alloc(), byteBuf, n);
                    if (byteBuf.isReadable()) {
                        list.add(new DefaultHttpContent(byteBuf2));
                    } else {
                        list.add(new DefaultLastHttpContent(byteBuf2, this.validateHeaders));
                        this.reset();
                    }
                } else if (!byteBuf.isReadable()) {
                    list.add(LastHttpContent.EMPTY_LAST_CONTENT);
                    this.reset();
                }
                return;
            }
            case READ_FIXED_LENGTH_CONTENT: {
                int n = this.actualReadableBytes();
                if (n == 0) {
                    return;
                }
                int n2 = Math.min(n, this.maxChunkSize);
                if ((long)n2 > this.chunkSize) {
                    n2 = (int)this.chunkSize;
                }
                ByteBuf byteBuf3 = ByteBufUtil.readBytes(channelHandlerContext.alloc(), byteBuf, n2);
                this.chunkSize -= (long)n2;
                if (this.chunkSize == 0L) {
                    list.add(new DefaultLastHttpContent(byteBuf3, this.validateHeaders));
                    this.reset();
                } else {
                    list.add(new DefaultHttpContent(byteBuf3));
                }
                return;
            }
            case READ_CHUNK_SIZE: {
                try {
                    AppendableCharSequence appendableCharSequence = this.lineParser.parse(byteBuf);
                    int n = HttpObjectDecoder.getChunkSize(appendableCharSequence.toString());
                    this.chunkSize = n;
                    if (n == 0) {
                        this.checkpoint(State.READ_CHUNK_FOOTER);
                        return;
                    }
                    this.checkpoint(State.READ_CHUNKED_CONTENT);
                }
                catch (Exception exception) {
                    list.add(this.invalidChunk(exception));
                    return;
                }
            }
            case READ_CHUNKED_CONTENT: {
                assert (this.chunkSize <= Integer.MAX_VALUE);
                int n = Math.min((int)this.chunkSize, this.maxChunkSize);
                DefaultHttpContent defaultHttpContent = new DefaultHttpContent(ByteBufUtil.readBytes(channelHandlerContext.alloc(), byteBuf, n));
                this.chunkSize -= (long)n;
                list.add(defaultHttpContent);
                if (this.chunkSize == 0L) {
                    this.checkpoint(State.READ_CHUNK_DELIMITER);
                } else {
                    return;
                }
            }
            case READ_CHUNK_DELIMITER: {
                int n;
                while (true) {
                    if ((n = byteBuf.readByte()) == 13) {
                        if (byteBuf.readByte() != 10) continue;
                        this.checkpoint(State.READ_CHUNK_SIZE);
                        return;
                    }
                    if (n == 10) {
                        this.checkpoint(State.READ_CHUNK_SIZE);
                        return;
                    }
                    this.checkpoint();
                }
            }
            case READ_CHUNK_FOOTER: {
                try {
                    LastHttpContent lastHttpContent = this.readTrailingHeaders(byteBuf);
                    list.add(lastHttpContent);
                    this.reset();
                    return;
                }
                catch (Exception exception) {
                    list.add(this.invalidChunk(exception));
                    return;
                }
            }
            case BAD_MESSAGE: {
                byteBuf.skipBytes(this.actualReadableBytes());
                break;
            }
            case UPGRADED: {
                int n = this.actualReadableBytes();
                if (n <= 0) break;
                list.add(byteBuf.readBytes(this.actualReadableBytes()));
                break;
            }
        }
    }

    private static String[] splitInitialLine(AppendableCharSequence appendableCharSequence) {
        int n = HttpObjectDecoder.findNonWhitespace(appendableCharSequence, 0);
        int n2 = HttpObjectDecoder.findWhitespace(appendableCharSequence, n);
        int n3 = HttpObjectDecoder.findNonWhitespace(appendableCharSequence, n2);
        int n4 = HttpObjectDecoder.findWhitespace(appendableCharSequence, n3);
        int n5 = HttpObjectDecoder.findNonWhitespace(appendableCharSequence, n4);
        int n6 = HttpObjectDecoder.findEndOfString(appendableCharSequence);
        return new String[]{appendableCharSequence.substring(n, n2), appendableCharSequence.substring(n3, n4), n5 < n6 ? appendableCharSequence.substring(n5, n6) : ""};
    }

    private State readHeaders(ByteBuf byteBuf) {
        State state;
        this.headerSize = 0;
        HttpMessage httpMessage = this.message;
        HttpHeaders httpHeaders = httpMessage.headers();
        AppendableCharSequence appendableCharSequence = this.headerParser.parse(byteBuf);
        String string = null;
        String string2 = null;
        if (appendableCharSequence.length() > 0) {
            httpHeaders.clear();
            do {
                char c = appendableCharSequence.charAt(0);
                if (string != null && (c == ' ' || c == '\t')) {
                    string2 = string2 + ' ' + appendableCharSequence.toString().trim();
                    continue;
                }
                if (string != null) {
                    httpHeaders.add(string, string2);
                }
                String[] stringArray = HttpObjectDecoder.splitHeader(appendableCharSequence);
                string = stringArray[0];
                string2 = stringArray[1];
            } while ((appendableCharSequence = this.headerParser.parse(byteBuf)).length() > 0);
            if (string != null) {
                httpHeaders.add(string, (Object)string2);
            }
        }
        if (this.isContentAlwaysEmpty(httpMessage)) {
            HttpHeaders.removeTransferEncodingChunked(httpMessage);
            state = State.SKIP_CONTROL_CHARS;
        } else {
            state = HttpHeaders.isTransferEncodingChunked(httpMessage) ? State.READ_CHUNK_SIZE : (this.contentLength() >= 0L ? State.READ_FIXED_LENGTH_CONTENT : State.READ_VARIABLE_LENGTH_CONTENT);
        }
        return state;
    }

    private static void skipControlCharacters(ByteBuf byteBuf) {
        char c;
        while (Character.isISOControl(c = (char)byteBuf.readUnsignedByte()) || Character.isWhitespace(c)) {
        }
        byteBuf.readerIndex(byteBuf.readerIndex() - 1);
    }

    private static String[] splitHeader(AppendableCharSequence appendableCharSequence) {
        int n;
        int n2;
        int n3;
        char c;
        int n4;
        int n5 = appendableCharSequence.length();
        for (n4 = n3 = HttpObjectDecoder.findNonWhitespace(appendableCharSequence, 0); n4 < n5 && (c = appendableCharSequence.charAt(n4)) != ':' && !Character.isWhitespace(c); ++n4) {
        }
        for (n2 = n4; n2 < n5; ++n2) {
            if (appendableCharSequence.charAt(n2) != ':') continue;
            ++n2;
            break;
        }
        if ((n = HttpObjectDecoder.findNonWhitespace(appendableCharSequence, n2)) == n5) {
            return new String[]{appendableCharSequence.substring(n3, n4), ""};
        }
        int n6 = HttpObjectDecoder.findEndOfString(appendableCharSequence);
        return new String[]{appendableCharSequence.substring(n3, n4), appendableCharSequence.substring(n, n6)};
    }

    private void reset() {
        HttpResponse httpResponse;
        HttpMessage httpMessage = this.message;
        this.message = null;
        this.contentLength = Long.MIN_VALUE;
        if (!this.isDecodingRequest() && (httpResponse = (HttpResponse)httpMessage) != null && httpResponse.getStatus().code() == 101) {
            this.checkpoint(State.UPGRADED);
            return;
        }
        this.checkpoint(State.SKIP_CONTROL_CHARS);
    }

    protected abstract boolean isDecodingRequest();

    private static int findWhitespace(CharSequence charSequence, int n) {
        int n2;
        for (n2 = n; n2 < charSequence.length() && !Character.isWhitespace(charSequence.charAt(n2)); ++n2) {
        }
        return n2;
    }

    private static int getChunkSize(String string) {
        string = string.trim();
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c != ';' && !Character.isWhitespace(c) && !Character.isISOControl(c)) continue;
            string = string.substring(0, i);
            break;
        }
        return Integer.parseInt(string, 16);
    }

    private static int findNonWhitespace(CharSequence charSequence, int n) {
        int n2;
        for (n2 = n; n2 < charSequence.length() && Character.isWhitespace(charSequence.charAt(n2)); ++n2) {
        }
        return n2;
    }

    protected HttpObjectDecoder(int n, int n2, int n3, boolean bl) {
        this(n, n2, n3, bl, true);
    }

    protected boolean isContentAlwaysEmpty(HttpMessage httpMessage) {
        if (httpMessage instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse)httpMessage;
            int n = httpResponse.getStatus().code();
            if (n >= 100 && n < 200) {
                return n != 101 || httpResponse.headers().contains("Sec-WebSocket-Accept");
            }
            switch (n) {
                case 204: 
                case 205: 
                case 304: {
                    return true;
                }
            }
        }
        return false;
    }

    protected HttpObjectDecoder(int n, int n2, int n3, boolean bl, boolean bl2) {
        super(State.SKIP_CONTROL_CHARS);
        this.lineParser = new LineParser(this.seq);
        this.contentLength = Long.MIN_VALUE;
        if (n <= 0) {
            throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + n);
        }
        if (n2 <= 0) {
            throw new IllegalArgumentException("maxHeaderSize must be a positive integer: " + n2);
        }
        if (n3 <= 0) {
            throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + n3);
        }
        this.maxInitialLineLength = n;
        this.maxHeaderSize = n2;
        this.maxChunkSize = n3;
        this.chunkedSupported = bl;
        this.validateHeaders = bl2;
    }

    private long contentLength() {
        if (this.contentLength == Long.MIN_VALUE) {
            this.contentLength = HttpHeaders.getContentLength(this.message, -1L);
        }
        return this.contentLength;
    }

    private LastHttpContent readTrailingHeaders(ByteBuf byteBuf) {
        this.headerSize = 0;
        AppendableCharSequence appendableCharSequence = this.headerParser.parse(byteBuf);
        String string = null;
        if (appendableCharSequence.length() > 0) {
            DefaultLastHttpContent defaultLastHttpContent = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
            do {
                Object object;
                char c = appendableCharSequence.charAt(0);
                if (string != null && (c == ' ' || c == '\t')) {
                    object = defaultLastHttpContent.trailingHeaders().getAll(string);
                    if (object.isEmpty()) continue;
                    int n = object.size() - 1;
                    String string2 = (String)object.get(n) + appendableCharSequence.toString().trim();
                    object.set(n, string2);
                    continue;
                }
                object = HttpObjectDecoder.splitHeader(appendableCharSequence);
                String string3 = object[0];
                if (!(HttpHeaders.equalsIgnoreCase(string3, "Content-Length") || HttpHeaders.equalsIgnoreCase(string3, "Transfer-Encoding") || HttpHeaders.equalsIgnoreCase(string3, "Trailer"))) {
                    defaultLastHttpContent.trailingHeaders().add(string3, object[1]);
                }
                string = string3;
            } while ((appendableCharSequence = this.headerParser.parse(byteBuf)).length() > 0);
            return defaultLastHttpContent;
        }
        return LastHttpContent.EMPTY_LAST_CONTENT;
    }

    private HttpMessage invalidMessage(Exception exception) {
        this.checkpoint(State.BAD_MESSAGE);
        if (this.message != null) {
            this.message.setDecoderResult(DecoderResult.failure(exception));
        } else {
            this.message = this.createInvalidMessage();
            this.message.setDecoderResult(DecoderResult.failure(exception));
        }
        HttpMessage httpMessage = this.message;
        this.message = null;
        return httpMessage;
    }

    protected HttpObjectDecoder() {
        this(4096, 8192, 8192, true);
    }

    private static int findEndOfString(CharSequence charSequence) {
        int n;
        for (n = charSequence.length(); n > 0 && Character.isWhitespace(charSequence.charAt(n - 1)); --n) {
        }
        return n;
    }

    private final class LineParser
    implements ByteBufProcessor {
        private final AppendableCharSequence seq;
        private int size;

        LineParser(AppendableCharSequence appendableCharSequence) {
            this.seq = appendableCharSequence;
        }

        public AppendableCharSequence parse(ByteBuf byteBuf) {
            this.seq.reset();
            this.size = 0;
            int n = byteBuf.forEachByte(this);
            byteBuf.readerIndex(n + 1);
            return this.seq;
        }

        @Override
        public boolean process(byte by) throws Exception {
            char c = (char)by;
            if (c == '\r') {
                return true;
            }
            if (c == '\n') {
                return false;
            }
            if (this.size >= HttpObjectDecoder.this.maxInitialLineLength) {
                throw new TooLongFrameException("An HTTP line is larger than " + HttpObjectDecoder.this.maxInitialLineLength + " bytes.");
            }
            ++this.size;
            this.seq.append(c);
            return true;
        }
    }

    static enum State {
        SKIP_CONTROL_CHARS,
        READ_INITIAL,
        READ_HEADER,
        READ_VARIABLE_LENGTH_CONTENT,
        READ_FIXED_LENGTH_CONTENT,
        READ_CHUNK_SIZE,
        READ_CHUNKED_CONTENT,
        READ_CHUNK_DELIMITER,
        READ_CHUNK_FOOTER,
        BAD_MESSAGE,
        UPGRADED;

    }

    private final class HeaderParser
    implements ByteBufProcessor {
        private final AppendableCharSequence seq;

        public AppendableCharSequence parse(ByteBuf byteBuf) {
            this.seq.reset();
            HttpObjectDecoder.this.headerSize = 0;
            int n = byteBuf.forEachByte(this);
            byteBuf.readerIndex(n + 1);
            return this.seq;
        }

        @Override
        public boolean process(byte by) throws Exception {
            char c = (char)by;
            HttpObjectDecoder.this.headerSize++;
            if (c == '\r') {
                return true;
            }
            if (c == '\n') {
                return false;
            }
            if (HttpObjectDecoder.this.headerSize >= HttpObjectDecoder.this.maxHeaderSize) {
                throw new TooLongFrameException("HTTP header is larger than " + HttpObjectDecoder.this.maxHeaderSize + " bytes.");
            }
            this.seq.append(c);
            return true;
        }

        HeaderParser(AppendableCharSequence appendableCharSequence) {
            this.seq = appendableCharSequence;
        }
    }
}

