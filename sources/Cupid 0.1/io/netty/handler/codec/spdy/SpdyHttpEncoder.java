package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import java.util.List;
import java.util.Map;

public class SpdyHttpEncoder extends MessageToMessageEncoder<HttpObject> {
  private final int spdyVersion;
  
  private int currentStreamId;
  
  public SpdyHttpEncoder(SpdyVersion version) {
    if (version == null)
      throw new NullPointerException("version"); 
    this.spdyVersion = version.getVersion();
  }
  
  protected void encode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
    boolean valid = false;
    boolean last = false;
    if (msg instanceof HttpRequest) {
      HttpRequest httpRequest = (HttpRequest)msg;
      SpdySynStreamFrame spdySynStreamFrame = createSynStreamFrame((HttpMessage)httpRequest);
      out.add(spdySynStreamFrame);
      last = spdySynStreamFrame.isLast();
      valid = true;
    } 
    if (msg instanceof HttpResponse) {
      HttpResponse httpResponse = (HttpResponse)msg;
      if (httpResponse.headers().contains("X-SPDY-Associated-To-Stream-ID")) {
        SpdySynStreamFrame spdySynStreamFrame = createSynStreamFrame((HttpMessage)httpResponse);
        last = spdySynStreamFrame.isLast();
        out.add(spdySynStreamFrame);
      } else {
        SpdySynReplyFrame spdySynReplyFrame = createSynReplyFrame(httpResponse);
        last = spdySynReplyFrame.isLast();
        out.add(spdySynReplyFrame);
      } 
      valid = true;
    } 
    if (msg instanceof HttpContent && !last) {
      HttpContent chunk = (HttpContent)msg;
      chunk.content().retain();
      SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(this.currentStreamId, chunk.content());
      spdyDataFrame.setLast(chunk instanceof LastHttpContent);
      if (chunk instanceof LastHttpContent) {
        LastHttpContent trailer = (LastHttpContent)chunk;
        HttpHeaders trailers = trailer.trailingHeaders();
        if (trailers.isEmpty()) {
          out.add(spdyDataFrame);
        } else {
          SpdyHeadersFrame spdyHeadersFrame = new DefaultSpdyHeadersFrame(this.currentStreamId);
          for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)trailers)
            spdyHeadersFrame.headers().add(entry.getKey(), entry.getValue()); 
          out.add(spdyHeadersFrame);
          out.add(spdyDataFrame);
        } 
      } else {
        out.add(spdyDataFrame);
      } 
      valid = true;
    } 
    if (!valid)
      throw new UnsupportedMessageTypeException(msg, new Class[0]); 
  }
  
  private SpdySynStreamFrame createSynStreamFrame(HttpMessage httpMessage) throws Exception {
    int streamID = SpdyHttpHeaders.getStreamId(httpMessage);
    int associatedToStreamId = SpdyHttpHeaders.getAssociatedToStreamId(httpMessage);
    byte priority = SpdyHttpHeaders.getPriority(httpMessage);
    String URL = SpdyHttpHeaders.getUrl(httpMessage);
    String scheme = SpdyHttpHeaders.getScheme(httpMessage);
    SpdyHttpHeaders.removeStreamId(httpMessage);
    SpdyHttpHeaders.removeAssociatedToStreamId(httpMessage);
    SpdyHttpHeaders.removePriority(httpMessage);
    SpdyHttpHeaders.removeUrl(httpMessage);
    SpdyHttpHeaders.removeScheme(httpMessage);
    httpMessage.headers().remove("Connection");
    httpMessage.headers().remove("Keep-Alive");
    httpMessage.headers().remove("Proxy-Connection");
    httpMessage.headers().remove("Transfer-Encoding");
    SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamID, associatedToStreamId, priority);
    if (httpMessage instanceof io.netty.handler.codec.http.FullHttpRequest) {
      HttpRequest httpRequest = (HttpRequest)httpMessage;
      SpdyHeaders.setMethod(this.spdyVersion, spdySynStreamFrame, httpRequest.getMethod());
      SpdyHeaders.setUrl(this.spdyVersion, spdySynStreamFrame, httpRequest.getUri());
      SpdyHeaders.setVersion(this.spdyVersion, spdySynStreamFrame, httpMessage.getProtocolVersion());
    } 
    if (httpMessage instanceof HttpResponse) {
      HttpResponse httpResponse = (HttpResponse)httpMessage;
      SpdyHeaders.setStatus(this.spdyVersion, spdySynStreamFrame, httpResponse.getStatus());
      SpdyHeaders.setUrl(this.spdyVersion, spdySynStreamFrame, URL);
      SpdyHeaders.setVersion(this.spdyVersion, spdySynStreamFrame, httpMessage.getProtocolVersion());
      spdySynStreamFrame.setUnidirectional(true);
    } 
    if (this.spdyVersion >= 3) {
      String host = HttpHeaders.getHost(httpMessage);
      httpMessage.headers().remove("Host");
      SpdyHeaders.setHost(spdySynStreamFrame, host);
    } 
    if (scheme == null)
      scheme = "https"; 
    SpdyHeaders.setScheme(this.spdyVersion, spdySynStreamFrame, scheme);
    for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)httpMessage.headers())
      spdySynStreamFrame.headers().add(entry.getKey(), entry.getValue()); 
    this.currentStreamId = spdySynStreamFrame.streamId();
    spdySynStreamFrame.setLast(isLast(httpMessage));
    return spdySynStreamFrame;
  }
  
  private SpdySynReplyFrame createSynReplyFrame(HttpResponse httpResponse) throws Exception {
    int streamID = SpdyHttpHeaders.getStreamId((HttpMessage)httpResponse);
    SpdyHttpHeaders.removeStreamId((HttpMessage)httpResponse);
    httpResponse.headers().remove("Connection");
    httpResponse.headers().remove("Keep-Alive");
    httpResponse.headers().remove("Proxy-Connection");
    httpResponse.headers().remove("Transfer-Encoding");
    SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamID);
    SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, httpResponse.getStatus());
    SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, httpResponse.getProtocolVersion());
    for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)httpResponse.headers())
      spdySynReplyFrame.headers().add(entry.getKey(), entry.getValue()); 
    this.currentStreamId = streamID;
    spdySynReplyFrame.setLast(isLast((HttpMessage)httpResponse));
    return spdySynReplyFrame;
  }
  
  private static boolean isLast(HttpMessage httpMessage) {
    if (httpMessage instanceof FullHttpMessage) {
      FullHttpMessage fullMessage = (FullHttpMessage)httpMessage;
      if (fullMessage.trailingHeaders().isEmpty() && !fullMessage.content().isReadable())
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\SpdyHttpEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */