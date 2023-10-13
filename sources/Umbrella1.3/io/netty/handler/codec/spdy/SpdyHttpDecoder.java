/*
 * Decompiled with CFR 0.150.
 */
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
import io.netty.handler.codec.spdy.DefaultSpdyRstStreamFrame;
import io.netty.handler.codec.spdy.DefaultSpdySynReplyFrame;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyDataFrame;
import io.netty.handler.codec.spdy.SpdyFrame;
import io.netty.handler.codec.spdy.SpdyHeaders;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamStatus;
import io.netty.handler.codec.spdy.SpdySynReplyFrame;
import io.netty.handler.codec.spdy.SpdySynStreamFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpdyHttpDecoder
extends MessageToMessageDecoder<SpdyFrame> {
    private final int spdyVersion;
    private final int maxContentLength;
    private final Map<Integer, FullHttpMessage> messageMap;

    public SpdyHttpDecoder(SpdyVersion version, int maxContentLength) {
        this(version, maxContentLength, new HashMap<Integer, FullHttpMessage>());
    }

    protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        if (maxContentLength <= 0) {
            throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
        }
        this.spdyVersion = version.getVersion();
        this.maxContentLength = maxContentLength;
        this.messageMap = messageMap;
    }

    protected FullHttpMessage putMessage(int streamId, FullHttpMessage message) {
        return this.messageMap.put(streamId, message);
    }

    protected FullHttpMessage getMessage(int streamId) {
        return this.messageMap.get(streamId);
    }

    protected FullHttpMessage removeMessage(int streamId) {
        return this.messageMap.remove(streamId);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, SpdyFrame msg, List<Object> out) throws Exception {
        block32: {
            if (msg instanceof SpdySynStreamFrame) {
                SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
                int streamId = spdySynStreamFrame.getStreamId();
                if (SpdyCodecUtil.isServerId(streamId)) {
                    int associatedToStreamId = spdySynStreamFrame.getAssociatedToStreamId();
                    if (associatedToStreamId == 0) {
                        DefaultSpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INVALID_STREAM);
                        ctx.writeAndFlush(spdyRstStreamFrame);
                        return;
                    }
                    String URL2 = SpdyHeaders.getUrl(this.spdyVersion, spdySynStreamFrame);
                    if (URL2 == null) {
                        DefaultSpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                        ctx.writeAndFlush(spdyRstStreamFrame);
                        return;
                    }
                    if (spdySynStreamFrame.isTruncated()) {
                        DefaultSpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
                        ctx.writeAndFlush(spdyRstStreamFrame);
                        return;
                    }
                    try {
                        FullHttpResponse httpResponseWithEntity = SpdyHttpDecoder.createHttpResponse(this.spdyVersion, spdySynStreamFrame);
                        SpdyHttpHeaders.setStreamId(httpResponseWithEntity, streamId);
                        SpdyHttpHeaders.setAssociatedToStreamId(httpResponseWithEntity, associatedToStreamId);
                        SpdyHttpHeaders.setPriority(httpResponseWithEntity, spdySynStreamFrame.getPriority());
                        SpdyHttpHeaders.setUrl(httpResponseWithEntity, URL2);
                        if (spdySynStreamFrame.isLast()) {
                            HttpHeaders.setContentLength(httpResponseWithEntity, 0L);
                            out.add(httpResponseWithEntity);
                            break block32;
                        }
                        this.putMessage(streamId, httpResponseWithEntity);
                    }
                    catch (Exception e) {
                        DefaultSpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                        ctx.writeAndFlush(spdyRstStreamFrame);
                    }
                } else {
                    if (spdySynStreamFrame.isTruncated()) {
                        DefaultSpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
                        spdySynReplyFrame.setLast(true);
                        SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
                        SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, HttpVersion.HTTP_1_0);
                        ctx.writeAndFlush(spdySynReplyFrame);
                        return;
                    }
                    try {
                        FullHttpRequest httpRequestWithEntity = SpdyHttpDecoder.createHttpRequest(this.spdyVersion, spdySynStreamFrame);
                        SpdyHttpHeaders.setStreamId(httpRequestWithEntity, streamId);
                        if (spdySynStreamFrame.isLast()) {
                            out.add(httpRequestWithEntity);
                            break block32;
                        }
                        this.putMessage(streamId, httpRequestWithEntity);
                    }
                    catch (Exception e) {
                        DefaultSpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
                        spdySynReplyFrame.setLast(true);
                        SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, HttpResponseStatus.BAD_REQUEST);
                        SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, HttpVersion.HTTP_1_0);
                        ctx.writeAndFlush(spdySynReplyFrame);
                    }
                }
            } else if (msg instanceof SpdySynReplyFrame) {
                SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
                int streamId = spdySynReplyFrame.getStreamId();
                if (spdySynReplyFrame.isTruncated()) {
                    DefaultSpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame);
                    return;
                }
                try {
                    FullHttpResponse httpResponseWithEntity = SpdyHttpDecoder.createHttpResponse(this.spdyVersion, spdySynReplyFrame);
                    SpdyHttpHeaders.setStreamId(httpResponseWithEntity, streamId);
                    if (spdySynReplyFrame.isLast()) {
                        HttpHeaders.setContentLength(httpResponseWithEntity, 0L);
                        out.add(httpResponseWithEntity);
                        break block32;
                    }
                    this.putMessage(streamId, httpResponseWithEntity);
                }
                catch (Exception e) {
                    DefaultSpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                    ctx.writeAndFlush(spdyRstStreamFrame);
                }
            } else if (msg instanceof SpdyHeadersFrame) {
                SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
                int streamId = spdyHeadersFrame.getStreamId();
                FullHttpMessage fullHttpMessage = this.getMessage(streamId);
                if (fullHttpMessage == null) {
                    return;
                }
                if (!spdyHeadersFrame.isTruncated()) {
                    for (Map.Entry<String, String> e : spdyHeadersFrame.headers()) {
                        fullHttpMessage.headers().add(e.getKey(), (Object)e.getValue());
                    }
                }
                if (spdyHeadersFrame.isLast()) {
                    HttpHeaders.setContentLength(fullHttpMessage, fullHttpMessage.content().readableBytes());
                    this.removeMessage(streamId);
                    out.add(fullHttpMessage);
                }
            } else if (msg instanceof SpdyDataFrame) {
                SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
                int streamId = spdyDataFrame.getStreamId();
                FullHttpMessage fullHttpMessage = this.getMessage(streamId);
                if (fullHttpMessage == null) {
                    return;
                }
                ByteBuf content = fullHttpMessage.content();
                if (content.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes()) {
                    this.removeMessage(streamId);
                    throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
                }
                ByteBuf spdyDataFrameData = spdyDataFrame.content();
                int spdyDataFrameDataLen = spdyDataFrameData.readableBytes();
                content.writeBytes(spdyDataFrameData, spdyDataFrameData.readerIndex(), spdyDataFrameDataLen);
                if (spdyDataFrame.isLast()) {
                    HttpHeaders.setContentLength(fullHttpMessage, content.readableBytes());
                    this.removeMessage(streamId);
                    out.add(fullHttpMessage);
                }
            } else if (msg instanceof SpdyRstStreamFrame) {
                SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
                int streamId = spdyRstStreamFrame.getStreamId();
                this.removeMessage(streamId);
            }
        }
    }

    private static FullHttpRequest createHttpRequest(int spdyVersion, SpdyHeadersFrame requestFrame) throws Exception {
        HttpMethod method = SpdyHeaders.getMethod(spdyVersion, requestFrame);
        String url = SpdyHeaders.getUrl(spdyVersion, requestFrame);
        HttpVersion httpVersion = SpdyHeaders.getVersion(spdyVersion, requestFrame);
        SpdyHeaders.removeMethod(spdyVersion, requestFrame);
        SpdyHeaders.removeUrl(spdyVersion, requestFrame);
        SpdyHeaders.removeVersion(spdyVersion, requestFrame);
        DefaultFullHttpRequest req = new DefaultFullHttpRequest(httpVersion, method, url);
        SpdyHeaders.removeScheme(spdyVersion, requestFrame);
        if (spdyVersion >= 3) {
            String host = SpdyHeaders.getHost(requestFrame);
            SpdyHeaders.removeHost(requestFrame);
            HttpHeaders.setHost((HttpMessage)req, host);
        }
        for (Map.Entry<String, String> e : requestFrame.headers()) {
            req.headers().add(e.getKey(), (Object)e.getValue());
        }
        HttpHeaders.setKeepAlive(req, true);
        req.headers().remove("Transfer-Encoding");
        return req;
    }

    private static FullHttpResponse createHttpResponse(int spdyVersion, SpdyHeadersFrame responseFrame) throws Exception {
        HttpResponseStatus status = SpdyHeaders.getStatus(spdyVersion, responseFrame);
        HttpVersion version = SpdyHeaders.getVersion(spdyVersion, responseFrame);
        SpdyHeaders.removeStatus(spdyVersion, responseFrame);
        SpdyHeaders.removeVersion(spdyVersion, responseFrame);
        DefaultFullHttpResponse res = new DefaultFullHttpResponse(version, status);
        for (Map.Entry<String, String> e : responseFrame.headers()) {
            res.headers().add(e.getKey(), (Object)e.getValue());
        }
        HttpHeaders.setKeepAlive(res, true);
        res.headers().remove("Transfer-Encoding");
        res.headers().remove("Trailer");
        return res;
    }
}

