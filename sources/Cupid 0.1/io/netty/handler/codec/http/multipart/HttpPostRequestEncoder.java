package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedInput;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpPostRequestEncoder implements ChunkedInput<HttpContent> {
  public enum EncoderMode {
    RFC1738, RFC3986;
  }
  
  private static final Map<Pattern, String> percentEncodings = new HashMap<Pattern, String>();
  
  private final HttpDataFactory factory;
  
  private final HttpRequest request;
  
  private final Charset charset;
  
  private boolean isChunked;
  
  private final List<InterfaceHttpData> bodyListDatas;
  
  final List<InterfaceHttpData> multipartHttpDatas;
  
  private final boolean isMultipart;
  
  String multipartDataBoundary;
  
  String multipartMixedBoundary;
  
  private boolean headerFinalized;
  
  private final EncoderMode encoderMode;
  
  private boolean isLastChunk;
  
  private boolean isLastChunkSent;
  
  private FileUpload currentFileUpload;
  
  private boolean duringMixedMode;
  
  private long globalBodySize;
  
  private ListIterator<InterfaceHttpData> iterator;
  
  private ByteBuf currentBuffer;
  
  private InterfaceHttpData currentData;
  
  private boolean isKey;
  
  static {
    percentEncodings.put(Pattern.compile("\\*"), "%2A");
    percentEncodings.put(Pattern.compile("\\+"), "%20");
    percentEncodings.put(Pattern.compile("%7E"), "~");
  }
  
  public HttpPostRequestEncoder(HttpRequest request, boolean multipart) throws ErrorDataEncoderException {
    this(new DefaultHttpDataFactory(16384L), request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
  }
  
  public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart) throws ErrorDataEncoderException {
    this(factory, request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
  }
  
  public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart, Charset charset, EncoderMode encoderMode) throws ErrorDataEncoderException {
    this.isKey = true;
    if (factory == null)
      throw new NullPointerException("factory"); 
    if (request == null)
      throw new NullPointerException("request"); 
    if (charset == null)
      throw new NullPointerException("charset"); 
    if (request.getMethod() != HttpMethod.POST)
      throw new ErrorDataEncoderException("Cannot create a Encoder if not a POST"); 
    this.request = request;
    this.charset = charset;
    this.factory = factory;
    this.bodyListDatas = new ArrayList<InterfaceHttpData>();
    this.isLastChunk = false;
    this.isLastChunkSent = false;
    this.isMultipart = multipart;
    this.multipartHttpDatas = new ArrayList<InterfaceHttpData>();
    this.encoderMode = encoderMode;
    if (this.isMultipart)
      initDataMultipart(); 
  }
  
  public void cleanFiles() {
    this.factory.cleanRequestHttpDatas(this.request);
  }
  
  public boolean isMultipart() {
    return this.isMultipart;
  }
  
  private void initDataMultipart() {
    this.multipartDataBoundary = getNewMultipartDelimiter();
  }
  
  private void initMixedMultipart() {
    this.multipartMixedBoundary = getNewMultipartDelimiter();
  }
  
  private static String getNewMultipartDelimiter() {
    return Long.toHexString(ThreadLocalRandom.current().nextLong()).toLowerCase();
  }
  
  public List<InterfaceHttpData> getBodyListAttributes() {
    return this.bodyListDatas;
  }
  
  public void setBodyHttpDatas(List<InterfaceHttpData> datas) throws ErrorDataEncoderException {
    if (datas == null)
      throw new NullPointerException("datas"); 
    this.globalBodySize = 0L;
    this.bodyListDatas.clear();
    this.currentFileUpload = null;
    this.duringMixedMode = false;
    this.multipartHttpDatas.clear();
    for (InterfaceHttpData data : datas)
      addBodyHttpData(data); 
  }
  
  public void addBodyAttribute(String name, String value) throws ErrorDataEncoderException {
    if (name == null)
      throw new NullPointerException("name"); 
    String svalue = value;
    if (value == null)
      svalue = ""; 
    Attribute data = this.factory.createAttribute(this.request, name, svalue);
    addBodyHttpData(data);
  }
  
  public void addBodyFileUpload(String name, File file, String contentType, boolean isText) throws ErrorDataEncoderException {
    if (name == null)
      throw new NullPointerException("name"); 
    if (file == null)
      throw new NullPointerException("file"); 
    String scontentType = contentType;
    String contentTransferEncoding = null;
    if (contentType == null)
      if (isText) {
        scontentType = "text/plain";
      } else {
        scontentType = "application/octet-stream";
      }  
    if (!isText)
      contentTransferEncoding = HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value(); 
    FileUpload fileUpload = this.factory.createFileUpload(this.request, name, file.getName(), scontentType, contentTransferEncoding, null, file.length());
    try {
      fileUpload.setContent(file);
    } catch (IOException e) {
      throw new ErrorDataEncoderException(e);
    } 
    addBodyHttpData(fileUpload);
  }
  
  public void addBodyFileUploads(String name, File[] file, String[] contentType, boolean[] isText) throws ErrorDataEncoderException {
    if (file.length != contentType.length && file.length != isText.length)
      throw new NullPointerException("Different array length"); 
    for (int i = 0; i < file.length; i++)
      addBodyFileUpload(name, file[i], contentType[i], isText[i]); 
  }
  
  public void addBodyHttpData(InterfaceHttpData data) throws ErrorDataEncoderException {
    if (this.headerFinalized)
      throw new ErrorDataEncoderException("Cannot add value once finalized"); 
    if (data == null)
      throw new NullPointerException("data"); 
    this.bodyListDatas.add(data);
    if (!this.isMultipart) {
      if (data instanceof Attribute) {
        Attribute attribute = (Attribute)data;
        try {
          String key = encodeAttribute(attribute.getName(), this.charset);
          String value = encodeAttribute(attribute.getValue(), this.charset);
          Attribute newattribute = this.factory.createAttribute(this.request, key, value);
          this.multipartHttpDatas.add(newattribute);
          this.globalBodySize += (newattribute.getName().length() + 1) + newattribute.length() + 1L;
        } catch (IOException e) {
          throw new ErrorDataEncoderException(e);
        } 
      } else if (data instanceof FileUpload) {
        FileUpload fileUpload = (FileUpload)data;
        String key = encodeAttribute(fileUpload.getName(), this.charset);
        String value = encodeAttribute(fileUpload.getFilename(), this.charset);
        Attribute newattribute = this.factory.createAttribute(this.request, key, value);
        this.multipartHttpDatas.add(newattribute);
        this.globalBodySize += (newattribute.getName().length() + 1) + newattribute.length() + 1L;
      } 
      return;
    } 
    if (data instanceof Attribute) {
      if (this.duringMixedMode) {
        InternalAttribute internalAttribute = new InternalAttribute(this.charset);
        internalAttribute.addValue("\r\n--" + this.multipartMixedBoundary + "--");
        this.multipartHttpDatas.add(internalAttribute);
        this.multipartMixedBoundary = null;
        this.currentFileUpload = null;
        this.duringMixedMode = false;
      } 
      InternalAttribute internal = new InternalAttribute(this.charset);
      if (!this.multipartHttpDatas.isEmpty())
        internal.addValue("\r\n"); 
      internal.addValue("--" + this.multipartDataBoundary + "\r\n");
      Attribute attribute = (Attribute)data;
      internal.addValue("Content-Disposition: form-data; name=\"" + attribute.getName() + "\"\r\n");
      Charset localcharset = attribute.getCharset();
      if (localcharset != null)
        internal.addValue("Content-Type: text/plain; charset=" + localcharset + "\r\n"); 
      internal.addValue("\r\n");
      this.multipartHttpDatas.add(internal);
      this.multipartHttpDatas.add(data);
      this.globalBodySize += attribute.length() + internal.size();
    } else if (data instanceof FileUpload) {
      boolean localMixed;
      FileUpload fileUpload = (FileUpload)data;
      InternalAttribute internal = new InternalAttribute(this.charset);
      if (!this.multipartHttpDatas.isEmpty())
        internal.addValue("\r\n"); 
      if (this.duringMixedMode) {
        if (this.currentFileUpload != null && this.currentFileUpload.getName().equals(fileUpload.getName())) {
          localMixed = true;
        } else {
          internal.addValue("--" + this.multipartMixedBoundary + "--");
          this.multipartHttpDatas.add(internal);
          this.multipartMixedBoundary = null;
          internal = new InternalAttribute(this.charset);
          internal.addValue("\r\n");
          localMixed = false;
          this.currentFileUpload = fileUpload;
          this.duringMixedMode = false;
        } 
      } else if (this.currentFileUpload != null && this.currentFileUpload.getName().equals(fileUpload.getName())) {
        initMixedMultipart();
        InternalAttribute pastAttribute = (InternalAttribute)this.multipartHttpDatas.get(this.multipartHttpDatas.size() - 2);
        this.globalBodySize -= pastAttribute.size();
        StringBuilder replacement = new StringBuilder(139 + this.multipartDataBoundary.length() + this.multipartMixedBoundary.length() * 2 + fileUpload.getFilename().length() + fileUpload.getName().length());
        replacement.append("--");
        replacement.append(this.multipartDataBoundary);
        replacement.append("\r\n");
        replacement.append("Content-Disposition");
        replacement.append(": ");
        replacement.append("form-data");
        replacement.append("; ");
        replacement.append("name");
        replacement.append("=\"");
        replacement.append(fileUpload.getName());
        replacement.append("\"\r\n");
        replacement.append("Content-Type");
        replacement.append(": ");
        replacement.append("multipart/mixed");
        replacement.append("; ");
        replacement.append("boundary");
        replacement.append('=');
        replacement.append(this.multipartMixedBoundary);
        replacement.append("\r\n\r\n");
        replacement.append("--");
        replacement.append(this.multipartMixedBoundary);
        replacement.append("\r\n");
        replacement.append("Content-Disposition");
        replacement.append(": ");
        replacement.append("attachment");
        replacement.append("; ");
        replacement.append("filename");
        replacement.append("=\"");
        replacement.append(fileUpload.getFilename());
        replacement.append("\"\r\n");
        pastAttribute.setValue(replacement.toString(), 1);
        pastAttribute.setValue("", 2);
        this.globalBodySize += pastAttribute.size();
        localMixed = true;
        this.duringMixedMode = true;
      } else {
        localMixed = false;
        this.currentFileUpload = fileUpload;
        this.duringMixedMode = false;
      } 
      if (localMixed) {
        internal.addValue("--" + this.multipartMixedBoundary + "\r\n");
        internal.addValue("Content-Disposition: attachment; filename=\"" + fileUpload.getFilename() + "\"\r\n");
      } else {
        internal.addValue("--" + this.multipartDataBoundary + "\r\n");
        internal.addValue("Content-Disposition: form-data; name=\"" + fileUpload.getName() + "\"; " + "filename" + "=\"" + fileUpload.getFilename() + "\"\r\n");
      } 
      internal.addValue("Content-Type: " + fileUpload.getContentType());
      String contentTransferEncoding = fileUpload.getContentTransferEncoding();
      if (contentTransferEncoding != null && contentTransferEncoding.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
        internal.addValue("\r\nContent-Transfer-Encoding: " + HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value() + "\r\n\r\n");
      } else if (fileUpload.getCharset() != null) {
        internal.addValue("; charset=" + fileUpload.getCharset() + "\r\n\r\n");
      } else {
        internal.addValue("\r\n\r\n");
      } 
      this.multipartHttpDatas.add(internal);
      this.multipartHttpDatas.add(data);
      this.globalBodySize += fileUpload.length() + internal.size();
    } 
  }
  
  public HttpRequest finalizeRequest() throws ErrorDataEncoderException {
    if (!this.headerFinalized) {
      if (this.isMultipart) {
        InternalAttribute internal = new InternalAttribute(this.charset);
        if (this.duringMixedMode)
          internal.addValue("\r\n--" + this.multipartMixedBoundary + "--"); 
        internal.addValue("\r\n--" + this.multipartDataBoundary + "--\r\n");
        this.multipartHttpDatas.add(internal);
        this.multipartMixedBoundary = null;
        this.currentFileUpload = null;
        this.duringMixedMode = false;
        this.globalBodySize += internal.size();
      } 
      this.headerFinalized = true;
    } else {
      throw new ErrorDataEncoderException("Header already encoded");
    } 
    HttpHeaders headers = this.request.headers();
    List<String> contentTypes = headers.getAll("Content-Type");
    List<String> transferEncoding = headers.getAll("Transfer-Encoding");
    if (contentTypes != null) {
      headers.remove("Content-Type");
      for (String contentType : contentTypes) {
        String lowercased = contentType.toLowerCase();
        if (lowercased.startsWith("multipart/form-data") || lowercased.startsWith("application/x-www-form-urlencoded"))
          continue; 
        headers.add("Content-Type", contentType);
      } 
    } 
    if (this.isMultipart) {
      String value = "multipart/form-data; boundary=" + this.multipartDataBoundary;
      headers.add("Content-Type", value);
    } else {
      headers.add("Content-Type", "application/x-www-form-urlencoded");
    } 
    long realSize = this.globalBodySize;
    if (this.isMultipart) {
      this.iterator = this.multipartHttpDatas.listIterator();
    } else {
      realSize--;
      this.iterator = this.multipartHttpDatas.listIterator();
    } 
    headers.set("Content-Length", String.valueOf(realSize));
    if (realSize > 8096L || this.isMultipart) {
      this.isChunked = true;
      if (transferEncoding != null) {
        headers.remove("Transfer-Encoding");
        for (String v : transferEncoding) {
          if (v.equalsIgnoreCase("chunked"))
            continue; 
          headers.add("Transfer-Encoding", v);
        } 
      } 
      HttpHeaders.setTransferEncodingChunked((HttpMessage)this.request);
      return new WrappedHttpRequest(this.request);
    } 
    HttpContent chunk = nextChunk();
    if (this.request instanceof FullHttpRequest) {
      FullHttpRequest fullRequest = (FullHttpRequest)this.request;
      ByteBuf chunkContent = chunk.content();
      if (fullRequest.content() != chunkContent) {
        fullRequest.content().clear().writeBytes(chunkContent);
        chunkContent.release();
      } 
      return (HttpRequest)fullRequest;
    } 
    return new WrappedFullHttpRequest(this.request, chunk);
  }
  
  public boolean isChunked() {
    return this.isChunked;
  }
  
  private String encodeAttribute(String s, Charset charset) throws ErrorDataEncoderException {
    if (s == null)
      return ""; 
    try {
      String encoded = URLEncoder.encode(s, charset.name());
      if (this.encoderMode == EncoderMode.RFC3986)
        for (Map.Entry<Pattern, String> entry : percentEncodings.entrySet()) {
          String replacement = entry.getValue();
          encoded = ((Pattern)entry.getKey()).matcher(encoded).replaceAll(replacement);
        }  
      return encoded;
    } catch (UnsupportedEncodingException e) {
      throw new ErrorDataEncoderException(charset.name(), e);
    } 
  }
  
  private ByteBuf fillByteBuf() {
    int length = this.currentBuffer.readableBytes();
    if (length > 8096) {
      ByteBuf byteBuf = this.currentBuffer.slice(this.currentBuffer.readerIndex(), 8096);
      this.currentBuffer.skipBytes(8096);
      return byteBuf;
    } 
    ByteBuf slice = this.currentBuffer;
    this.currentBuffer = null;
    return slice;
  }
  
  private HttpContent encodeNextChunkMultipart(int sizeleft) throws ErrorDataEncoderException {
    if (this.currentData == null)
      return null; 
    if (this.currentData instanceof InternalAttribute) {
      byteBuf = ((InternalAttribute)this.currentData).toByteBuf();
      this.currentData = null;
    } else {
      if (this.currentData instanceof Attribute) {
        try {
          byteBuf = ((Attribute)this.currentData).getChunk(sizeleft);
        } catch (IOException e) {
          throw new ErrorDataEncoderException(e);
        } 
      } else {
        try {
          byteBuf = ((HttpData)this.currentData).getChunk(sizeleft);
        } catch (IOException e) {
          throw new ErrorDataEncoderException(e);
        } 
      } 
      if (byteBuf.capacity() == 0) {
        this.currentData = null;
        return null;
      } 
    } 
    if (this.currentBuffer == null) {
      this.currentBuffer = byteBuf;
    } else {
      this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, byteBuf });
    } 
    if (this.currentBuffer.readableBytes() < 8096) {
      this.currentData = null;
      return null;
    } 
    ByteBuf byteBuf = fillByteBuf();
    return (HttpContent)new DefaultHttpContent(byteBuf);
  }
  
  private HttpContent encodeNextChunkUrlEncoded(int sizeleft) throws ErrorDataEncoderException {
    if (this.currentData == null)
      return null; 
    int size = sizeleft;
    if (this.isKey) {
      String key = this.currentData.getName();
      buffer = Unpooled.wrappedBuffer(key.getBytes());
      this.isKey = false;
      if (this.currentBuffer == null) {
        this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { buffer, Unpooled.wrappedBuffer("=".getBytes()) });
        size -= buffer.readableBytes() + 1;
      } else {
        this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer, Unpooled.wrappedBuffer("=".getBytes()) });
        size -= buffer.readableBytes() + 1;
      } 
      if (this.currentBuffer.readableBytes() >= 8096) {
        buffer = fillByteBuf();
        return (HttpContent)new DefaultHttpContent(buffer);
      } 
    } 
    try {
      buffer = ((HttpData)this.currentData).getChunk(size);
    } catch (IOException e) {
      throw new ErrorDataEncoderException(e);
    } 
    ByteBuf delimiter = null;
    if (buffer.readableBytes() < size) {
      this.isKey = true;
      delimiter = this.iterator.hasNext() ? Unpooled.wrappedBuffer("&".getBytes()) : null;
    } 
    if (buffer.capacity() == 0) {
      this.currentData = null;
      if (this.currentBuffer == null) {
        this.currentBuffer = delimiter;
      } else if (delimiter != null) {
        this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, delimiter });
      } 
      if (this.currentBuffer.readableBytes() >= 8096) {
        buffer = fillByteBuf();
        return (HttpContent)new DefaultHttpContent(buffer);
      } 
      return null;
    } 
    if (this.currentBuffer == null) {
      if (delimiter != null) {
        this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { buffer, delimiter });
      } else {
        this.currentBuffer = buffer;
      } 
    } else if (delimiter != null) {
      this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer, delimiter });
    } else {
      this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer });
    } 
    if (this.currentBuffer.readableBytes() < 8096) {
      this.currentData = null;
      this.isKey = true;
      return null;
    } 
    ByteBuf buffer = fillByteBuf();
    return (HttpContent)new DefaultHttpContent(buffer);
  }
  
  public void close() throws Exception {}
  
  public HttpContent readChunk(ChannelHandlerContext ctx) throws Exception {
    if (this.isLastChunkSent)
      return null; 
    return nextChunk();
  }
  
  private HttpContent nextChunk() throws ErrorDataEncoderException {
    if (this.isLastChunk) {
      this.isLastChunkSent = true;
      return (HttpContent)LastHttpContent.EMPTY_LAST_CONTENT;
    } 
    int size = 8096;
    if (this.currentBuffer != null)
      size -= this.currentBuffer.readableBytes(); 
    if (size <= 0) {
      ByteBuf byteBuf = fillByteBuf();
      return (HttpContent)new DefaultHttpContent(byteBuf);
    } 
    if (this.currentData != null) {
      if (this.isMultipart) {
        HttpContent chunk = encodeNextChunkMultipart(size);
        if (chunk != null)
          return chunk; 
      } else {
        HttpContent chunk = encodeNextChunkUrlEncoded(size);
        if (chunk != null)
          return chunk; 
      } 
      size = 8096 - this.currentBuffer.readableBytes();
    } 
    if (!this.iterator.hasNext()) {
      this.isLastChunk = true;
      ByteBuf byteBuf = this.currentBuffer;
      this.currentBuffer = null;
      return (HttpContent)new DefaultHttpContent(byteBuf);
    } 
    while (size > 0 && this.iterator.hasNext()) {
      HttpContent chunk;
      this.currentData = this.iterator.next();
      if (this.isMultipart) {
        chunk = encodeNextChunkMultipart(size);
      } else {
        chunk = encodeNextChunkUrlEncoded(size);
      } 
      if (chunk == null) {
        size = 8096 - this.currentBuffer.readableBytes();
        continue;
      } 
      return chunk;
    } 
    this.isLastChunk = true;
    if (this.currentBuffer == null) {
      this.isLastChunkSent = true;
      return (HttpContent)LastHttpContent.EMPTY_LAST_CONTENT;
    } 
    ByteBuf buffer = this.currentBuffer;
    this.currentBuffer = null;
    return (HttpContent)new DefaultHttpContent(buffer);
  }
  
  public boolean isEndOfInput() throws Exception {
    return this.isLastChunkSent;
  }
  
  public static class ErrorDataEncoderException extends Exception {
    private static final long serialVersionUID = 5020247425493164465L;
    
    public ErrorDataEncoderException() {}
    
    public ErrorDataEncoderException(String msg) {
      super(msg);
    }
    
    public ErrorDataEncoderException(Throwable cause) {
      super(cause);
    }
    
    public ErrorDataEncoderException(String msg, Throwable cause) {
      super(msg, cause);
    }
  }
  
  private static class WrappedHttpRequest implements HttpRequest {
    private final HttpRequest request;
    
    WrappedHttpRequest(HttpRequest request) {
      this.request = request;
    }
    
    public HttpRequest setProtocolVersion(HttpVersion version) {
      this.request.setProtocolVersion(version);
      return this;
    }
    
    public HttpRequest setMethod(HttpMethod method) {
      this.request.setMethod(method);
      return this;
    }
    
    public HttpRequest setUri(String uri) {
      this.request.setUri(uri);
      return this;
    }
    
    public HttpMethod getMethod() {
      return this.request.getMethod();
    }
    
    public String getUri() {
      return this.request.getUri();
    }
    
    public HttpVersion getProtocolVersion() {
      return this.request.getProtocolVersion();
    }
    
    public HttpHeaders headers() {
      return this.request.headers();
    }
    
    public DecoderResult getDecoderResult() {
      return this.request.getDecoderResult();
    }
    
    public void setDecoderResult(DecoderResult result) {
      this.request.setDecoderResult(result);
    }
  }
  
  private static final class WrappedFullHttpRequest extends WrappedHttpRequest implements FullHttpRequest {
    private final HttpContent content;
    
    private WrappedFullHttpRequest(HttpRequest request, HttpContent content) {
      super(request);
      this.content = content;
    }
    
    public FullHttpRequest setProtocolVersion(HttpVersion version) {
      super.setProtocolVersion(version);
      return this;
    }
    
    public FullHttpRequest setMethod(HttpMethod method) {
      super.setMethod(method);
      return this;
    }
    
    public FullHttpRequest setUri(String uri) {
      super.setUri(uri);
      return this;
    }
    
    public FullHttpRequest copy() {
      DefaultFullHttpRequest copy = new DefaultFullHttpRequest(getProtocolVersion(), getMethod(), getUri(), content().copy());
      copy.headers().set(headers());
      copy.trailingHeaders().set(trailingHeaders());
      return (FullHttpRequest)copy;
    }
    
    public FullHttpRequest duplicate() {
      DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(getProtocolVersion(), getMethod(), getUri(), content().duplicate());
      duplicate.headers().set(headers());
      duplicate.trailingHeaders().set(trailingHeaders());
      return (FullHttpRequest)duplicate;
    }
    
    public FullHttpRequest retain(int increment) {
      this.content.retain(increment);
      return this;
    }
    
    public FullHttpRequest retain() {
      this.content.retain();
      return this;
    }
    
    public ByteBuf content() {
      return this.content.content();
    }
    
    public HttpHeaders trailingHeaders() {
      if (this.content instanceof LastHttpContent)
        return ((LastHttpContent)this.content).trailingHeaders(); 
      return HttpHeaders.EMPTY_HEADERS;
    }
    
    public int refCnt() {
      return this.content.refCnt();
    }
    
    public boolean release() {
      return this.content.release();
    }
    
    public boolean release(int decrement) {
      return this.content.release(decrement);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\HttpPostRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */