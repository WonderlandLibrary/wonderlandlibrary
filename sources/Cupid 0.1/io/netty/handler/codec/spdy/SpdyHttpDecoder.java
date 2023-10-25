package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpdyHttpDecoder extends MessageToMessageDecoder<SpdyFrame> {
  private final boolean validateHeaders;
  
  private final int spdyVersion;
  
  private final int maxContentLength;
  
  private final Map<Integer, FullHttpMessage> messageMap;
  
  public SpdyHttpDecoder(SpdyVersion version, int maxContentLength) {
    this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>(), true);
  }
  
  public SpdyHttpDecoder(SpdyVersion version, int maxContentLength, boolean validateHeaders) {
    this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>(), validateHeaders);
  }
  
  protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap) {
    this(version, maxContentLength, messageMap, true);
  }
  
  protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap, boolean validateHeaders) {
    if (version == null)
      throw new NullPointerException("version"); 
    if (maxContentLength <= 0)
      throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength); 
    this.spdyVersion = version.getVersion();
    this.maxContentLength = maxContentLength;
    this.messageMap = messageMap;
    this.validateHeaders = validateHeaders;
  }
  
  protected FullHttpMessage putMessage(int streamId, FullHttpMessage message) {
    return this.messageMap.put(Integer.valueOf(streamId), message);
  }
  
  protected FullHttpMessage getMessage(int streamId) {
    return this.messageMap.get(Integer.valueOf(streamId));
  }
  
  protected FullHttpMessage removeMessage(int streamId) {
    return this.messageMap.remove(Integer.valueOf(streamId));
  }
  
  protected void decode(ChannelHandlerContext ctx, SpdyFrame msg, List<Object> out) throws Exception {
    if (msg instanceof SpdySynStreamFrame) {
      SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
      int streamId = spdySynStreamFrame.streamId();
      if (SpdyCodecUtil.isServerId(streamId)) {
        int associatedToStreamId = spdySynStreamFrame.associatedStreamId();
        if (associatedToStreamId == 0) {
          SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INVALID_STREAM);
          ctx.writeAndFlush(spdyRstStreamFrame);
          return;
        } 
        String URL = SpdyHeaders.getUrl(this.spdyVersion, spdySynStreamFrame);
        if (URL == null) {
          SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
          ctx.writeAndFlush(spdyRstStreamFrame);
          return;
        } 
        if (spdySynStreamFrame.isTruncated()) {
          SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
          ctx.writeAndFlush(spdyRstStreamFrame);
          return;
        } 
        try {
          FullHttpResponse httpResponseWithEntity = createHttpResponse(ctx, this.spdyVersion, spdySynStreamFrame, this.validateHeaders);
          SpdyHttpHeaders.setStreamId((HttpMessage)httpResponseWithEntity, streamId);
          SpdyHttpHeaders.setAssociatedToStreamId((HttpMessage)httpResponseWithEntity, associatedToStreamId);
          SpdyHttpHeaders.setPriority((HttpMessage)httpResponseWithEntity, spdySynStreamFrame.priority());
          SpdyHttpHeaders.setUrl((HttpMessage)httpResponseWithEntity, URL);
          if (spdySynStreamFrame.isLast()) {
            HttpHeaders.setContentLength((HttpMessage)httpResponseWithEntity, 0L);
            out.add(httpResponseWithEntity);
          } else {
            putMessage(streamId, (FullHttpMessage)httpResponseWithEntity);
          } 
        } catch (Exception e) {
          SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
          ctx.writeAndFlush(spdyRstStreamFrame);
        } 
      } else {
        if (spdySynStreamFrame.isTruncated()) {
          SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
          spdySynReplyFrame.setLast(true);
          SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
          SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, HttpVersion.HTTP_1_0);
          ctx.writeAndFlush(spdySynReplyFrame);
          return;
        } 
        try {
          FullHttpRequest httpRequestWithEntity = createHttpRequest(this.spdyVersion, spdySynStreamFrame);
          SpdyHttpHeaders.setStreamId((HttpMessage)httpRequestWithEntity, streamId);
          if (spdySynStreamFrame.isLast()) {
            out.add(httpRequestWithEntity);
          } else {
            putMessage(streamId, (FullHttpMessage)httpRequestWithEntity);
          } 
        } catch (Exception e) {
          SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
          spdySynReplyFrame.setLast(true);
          SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, HttpResponseStatus.BAD_REQUEST);
          SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, HttpVersion.HTTP_1_0);
          ctx.writeAndFlush(spdySynReplyFrame);
        } 
      } 
    } else if (msg instanceof SpdySynReplyFrame) {
      SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
      int streamId = spdySynReplyFrame.streamId();
      if (spdySynReplyFrame.isTruncated()) {
        SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
        ctx.writeAndFlush(spdyRstStreamFrame);
        return;
      } 
      try {
        FullHttpResponse httpResponseWithEntity = createHttpResponse(ctx, this.spdyVersion, spdySynReplyFrame, this.validateHeaders);
        SpdyHttpHeaders.setStreamId((HttpMessage)httpResponseWithEntity, streamId);
        if (spdySynReplyFrame.isLast()) {
          HttpHeaders.setContentLength((HttpMessage)httpResponseWithEntity, 0L);
          out.add(httpResponseWithEntity);
        } else {
          putMessage(streamId, (FullHttpMessage)httpResponseWithEntity);
        } 
      } catch (Exception e) {
        SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
        ctx.writeAndFlush(spdyRstStreamFrame);
      } 
    } else if (msg instanceof SpdyHeadersFrame) {
      SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
      int streamId = spdyHeadersFrame.streamId();
      FullHttpMessage fullHttpMessage = getMessage(streamId);
      if (fullHttpMessage == null)
        return; 
      if (!spdyHeadersFrame.isTruncated())
        for (Map.Entry<String, String> e : (Iterable<Map.Entry<String, String>>)spdyHeadersFrame.headers())
          fullHttpMessage.headers().add(e.getKey(), e.getValue());  
      if (spdyHeadersFrame.isLast()) {
        HttpHeaders.setContentLength((HttpMessage)fullHttpMessage, fullHttpMessage.content().readableBytes());
        removeMessage(streamId);
        out.add(fullHttpMessage);
      } 
    } else if (msg instanceof SpdyDataFrame) {
      SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
      int streamId = spdyDataFrame.streamId();
      FullHttpMessage fullHttpMessage = getMessage(streamId);
      if (fullHttpMessage == null)
        return; 
      ByteBuf content = fullHttpMessage.content();
      if (content.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes()) {
        removeMessage(streamId);
        throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
      } 
      ByteBuf spdyDataFrameData = spdyDataFrame.content();
      int spdyDataFrameDataLen = spdyDataFrameData.readableBytes();
      content.writeBytes(spdyDataFrameData, spdyDataFrameData.readerIndex(), spdyDataFrameDataLen);
      if (spdyDataFrame.isLast()) {
        HttpHeaders.setContentLength((HttpMessage)fullHttpMessage, content.readableBytes());
        removeMessage(streamId);
        out.add(fullHttpMessage);
      } 
    } else if (msg instanceof SpdyRstStreamFrame) {
      SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
      int streamId = spdyRstStreamFrame.streamId();
      removeMessage(streamId);
    } 
  }
  
  private static FullHttpRequest createHttpRequest(int spdyVersion, SpdyHeadersFrame requestFrame) throws Exception {
    SpdyHeaders headers = requestFrame.headers();
    HttpMethod method = SpdyHeaders.getMethod(spdyVersion, requestFrame);
    String url = SpdyHeaders.getUrl(spdyVersion, requestFrame);
    HttpVersion httpVersion = SpdyHeaders.getVersion(spdyVersion, requestFrame);
    SpdyHeaders.removeMethod(spdyVersion, requestFrame);
    SpdyHeaders.removeUrl(spdyVersion, requestFrame);
    SpdyHeaders.removeVersion(spdyVersion, requestFrame);
    DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(httpVersion, method, url);
    SpdyHeaders.removeScheme(spdyVersion, requestFrame);
    String host = headers.get(":host");
    headers.remove(":host");
    defaultFullHttpRequest.headers().set("Host", host);
    for (Map.Entry<String, String> e : (Iterable<Map.Entry<String, String>>)requestFrame.headers())
      defaultFullHttpRequest.headers().add(e.getKey(), e.getValue()); 
    HttpHeaders.setKeepAlive((HttpMessage)defaultFullHttpRequest, true);
    defaultFullHttpRequest.headers().remove("Transfer-Encoding");
    return (FullHttpRequest)defaultFullHttpRequest;
  }
  
  private static FullHttpResponse createHttpResponse(ChannelHandlerContext ctx, int spdyVersion, SpdyHeadersFrame responseFrame, boolean validateHeaders) throws Exception {
    HttpResponseStatus status = SpdyHeaders.getStatus(spdyVersion, responseFrame);
    HttpVersion version = SpdyHeaders.getVersion(spdyVersion, responseFrame);
    SpdyHeaders.removeStatus(spdyVersion, responseFrame);
    SpdyHeaders.removeVersion(spdyVersion, responseFrame);
    DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(version, status, ctx.alloc().buffer(), validateHeaders);
    for (Map.Entry<String, String> e : (Iterable<Map.Entry<String, String>>)responseFrame.headers())
      defaultFullHttpResponse.headers().add(e.getKey(), e.getValue()); 
    HttpHeaders.setKeepAlive((HttpMessage)defaultFullHttpResponse, true);
    defaultFullHttpResponse.headers().remove("Transfer-Encoding");
    defaultFullHttpResponse.headers().remove("Trailer");
    return (FullHttpResponse)defaultFullHttpResponse;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\SpdyHttpDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */