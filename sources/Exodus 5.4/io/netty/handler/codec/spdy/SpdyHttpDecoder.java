/*
 * Decompiled with CFR 0.152.
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
    private final int maxContentLength;
    private final boolean validateHeaders;
    private final Map<Integer, FullHttpMessage> messageMap;
    private final int spdyVersion;

    private static FullHttpResponse createHttpResponse(ChannelHandlerContext channelHandlerContext, int n, SpdyHeadersFrame spdyHeadersFrame, boolean bl) throws Exception {
        HttpResponseStatus httpResponseStatus = SpdyHeaders.getStatus(n, spdyHeadersFrame);
        HttpVersion httpVersion = SpdyHeaders.getVersion(n, spdyHeadersFrame);
        SpdyHeaders.removeStatus(n, spdyHeadersFrame);
        SpdyHeaders.removeVersion(n, spdyHeadersFrame);
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(httpVersion, httpResponseStatus, channelHandlerContext.alloc().buffer(), bl);
        for (Map.Entry<String, String> entry : spdyHeadersFrame.headers()) {
            defaultFullHttpResponse.headers().add(entry.getKey(), (Object)entry.getValue());
        }
        HttpHeaders.setKeepAlive(defaultFullHttpResponse, true);
        defaultFullHttpResponse.headers().remove("Transfer-Encoding");
        defaultFullHttpResponse.headers().remove("Trailer");
        return defaultFullHttpResponse;
    }

    protected FullHttpMessage removeMessage(int n) {
        return this.messageMap.remove(n);
    }

    private static FullHttpRequest createHttpRequest(int n, SpdyHeadersFrame spdyHeadersFrame) throws Exception {
        SpdyHeaders spdyHeaders = spdyHeadersFrame.headers();
        HttpMethod httpMethod = SpdyHeaders.getMethod(n, spdyHeadersFrame);
        String string = SpdyHeaders.getUrl(n, spdyHeadersFrame);
        HttpVersion httpVersion = SpdyHeaders.getVersion(n, spdyHeadersFrame);
        SpdyHeaders.removeMethod(n, spdyHeadersFrame);
        SpdyHeaders.removeUrl(n, spdyHeadersFrame);
        SpdyHeaders.removeVersion(n, spdyHeadersFrame);
        DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(httpVersion, httpMethod, string);
        SpdyHeaders.removeScheme(n, spdyHeadersFrame);
        String string2 = spdyHeaders.get(":host");
        spdyHeaders.remove(":host");
        defaultFullHttpRequest.headers().set("Host", (Object)string2);
        for (Map.Entry<String, String> entry : spdyHeadersFrame.headers()) {
            defaultFullHttpRequest.headers().add(entry.getKey(), (Object)entry.getValue());
        }
        HttpHeaders.setKeepAlive(defaultFullHttpRequest, true);
        defaultFullHttpRequest.headers().remove("Transfer-Encoding");
        return defaultFullHttpRequest;
    }

    protected FullHttpMessage putMessage(int n, FullHttpMessage fullHttpMessage) {
        return this.messageMap.put(n, fullHttpMessage);
    }

    public SpdyHttpDecoder(SpdyVersion spdyVersion, int n, boolean bl) {
        this(spdyVersion, n, new HashMap<Integer, FullHttpMessage>(), bl);
    }

    protected SpdyHttpDecoder(SpdyVersion spdyVersion, int n, Map<Integer, FullHttpMessage> map, boolean bl) {
        if (spdyVersion == null) {
            throw new NullPointerException("version");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("maxContentLength must be a positive integer: " + n);
        }
        this.spdyVersion = spdyVersion.getVersion();
        this.maxContentLength = n;
        this.messageMap = map;
        this.validateHeaders = bl;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, SpdyFrame spdyFrame, List<Object> list) throws Exception {
        block32: {
            if (spdyFrame instanceof SpdySynStreamFrame) {
                SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)spdyFrame;
                int n = spdySynStreamFrame.streamId();
                if (SpdyCodecUtil.isServerId(n)) {
                    int n2 = spdySynStreamFrame.associatedStreamId();
                    if (n2 == 0) {
                        DefaultSpdyRstStreamFrame defaultSpdyRstStreamFrame = new DefaultSpdyRstStreamFrame(n, SpdyStreamStatus.INVALID_STREAM);
                        channelHandlerContext.writeAndFlush(defaultSpdyRstStreamFrame);
                        return;
                    }
                    String string = SpdyHeaders.getUrl(this.spdyVersion, spdySynStreamFrame);
                    if (string == null) {
                        DefaultSpdyRstStreamFrame defaultSpdyRstStreamFrame = new DefaultSpdyRstStreamFrame(n, SpdyStreamStatus.PROTOCOL_ERROR);
                        channelHandlerContext.writeAndFlush(defaultSpdyRstStreamFrame);
                        return;
                    }
                    if (spdySynStreamFrame.isTruncated()) {
                        DefaultSpdyRstStreamFrame defaultSpdyRstStreamFrame = new DefaultSpdyRstStreamFrame(n, SpdyStreamStatus.INTERNAL_ERROR);
                        channelHandlerContext.writeAndFlush(defaultSpdyRstStreamFrame);
                        return;
                    }
                    try {
                        FullHttpResponse fullHttpResponse = SpdyHttpDecoder.createHttpResponse(channelHandlerContext, this.spdyVersion, spdySynStreamFrame, this.validateHeaders);
                        SpdyHttpHeaders.setStreamId(fullHttpResponse, n);
                        SpdyHttpHeaders.setAssociatedToStreamId(fullHttpResponse, n2);
                        SpdyHttpHeaders.setPriority(fullHttpResponse, spdySynStreamFrame.priority());
                        SpdyHttpHeaders.setUrl(fullHttpResponse, string);
                        if (spdySynStreamFrame.isLast()) {
                            HttpHeaders.setContentLength(fullHttpResponse, 0L);
                            list.add(fullHttpResponse);
                            break block32;
                        }
                        this.putMessage(n, fullHttpResponse);
                    }
                    catch (Exception exception) {
                        DefaultSpdyRstStreamFrame defaultSpdyRstStreamFrame = new DefaultSpdyRstStreamFrame(n, SpdyStreamStatus.PROTOCOL_ERROR);
                        channelHandlerContext.writeAndFlush(defaultSpdyRstStreamFrame);
                    }
                } else {
                    if (spdySynStreamFrame.isTruncated()) {
                        DefaultSpdySynReplyFrame defaultSpdySynReplyFrame = new DefaultSpdySynReplyFrame(n);
                        defaultSpdySynReplyFrame.setLast(true);
                        SpdyHeaders.setStatus(this.spdyVersion, defaultSpdySynReplyFrame, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
                        SpdyHeaders.setVersion(this.spdyVersion, defaultSpdySynReplyFrame, HttpVersion.HTTP_1_0);
                        channelHandlerContext.writeAndFlush(defaultSpdySynReplyFrame);
                        return;
                    }
                    try {
                        FullHttpRequest fullHttpRequest = SpdyHttpDecoder.createHttpRequest(this.spdyVersion, spdySynStreamFrame);
                        SpdyHttpHeaders.setStreamId(fullHttpRequest, n);
                        if (spdySynStreamFrame.isLast()) {
                            list.add(fullHttpRequest);
                            break block32;
                        }
                        this.putMessage(n, fullHttpRequest);
                    }
                    catch (Exception exception) {
                        DefaultSpdySynReplyFrame defaultSpdySynReplyFrame = new DefaultSpdySynReplyFrame(n);
                        defaultSpdySynReplyFrame.setLast(true);
                        SpdyHeaders.setStatus(this.spdyVersion, defaultSpdySynReplyFrame, HttpResponseStatus.BAD_REQUEST);
                        SpdyHeaders.setVersion(this.spdyVersion, defaultSpdySynReplyFrame, HttpVersion.HTTP_1_0);
                        channelHandlerContext.writeAndFlush(defaultSpdySynReplyFrame);
                    }
                }
            } else if (spdyFrame instanceof SpdySynReplyFrame) {
                SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)spdyFrame;
                int n = spdySynReplyFrame.streamId();
                if (spdySynReplyFrame.isTruncated()) {
                    DefaultSpdyRstStreamFrame defaultSpdyRstStreamFrame = new DefaultSpdyRstStreamFrame(n, SpdyStreamStatus.INTERNAL_ERROR);
                    channelHandlerContext.writeAndFlush(defaultSpdyRstStreamFrame);
                    return;
                }
                try {
                    FullHttpResponse fullHttpResponse = SpdyHttpDecoder.createHttpResponse(channelHandlerContext, this.spdyVersion, spdySynReplyFrame, this.validateHeaders);
                    SpdyHttpHeaders.setStreamId(fullHttpResponse, n);
                    if (spdySynReplyFrame.isLast()) {
                        HttpHeaders.setContentLength(fullHttpResponse, 0L);
                        list.add(fullHttpResponse);
                        break block32;
                    }
                    this.putMessage(n, fullHttpResponse);
                }
                catch (Exception exception) {
                    DefaultSpdyRstStreamFrame defaultSpdyRstStreamFrame = new DefaultSpdyRstStreamFrame(n, SpdyStreamStatus.PROTOCOL_ERROR);
                    channelHandlerContext.writeAndFlush(defaultSpdyRstStreamFrame);
                }
            } else if (spdyFrame instanceof SpdyHeadersFrame) {
                SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)spdyFrame;
                int n = spdyHeadersFrame.streamId();
                FullHttpMessage fullHttpMessage = this.getMessage(n);
                if (fullHttpMessage == null) {
                    return;
                }
                if (!spdyHeadersFrame.isTruncated()) {
                    for (Map.Entry<String, String> entry : spdyHeadersFrame.headers()) {
                        fullHttpMessage.headers().add(entry.getKey(), (Object)entry.getValue());
                    }
                }
                if (spdyHeadersFrame.isLast()) {
                    HttpHeaders.setContentLength(fullHttpMessage, fullHttpMessage.content().readableBytes());
                    this.removeMessage(n);
                    list.add(fullHttpMessage);
                }
            } else if (spdyFrame instanceof SpdyDataFrame) {
                SpdyDataFrame spdyDataFrame = (SpdyDataFrame)spdyFrame;
                int n = spdyDataFrame.streamId();
                FullHttpMessage fullHttpMessage = this.getMessage(n);
                if (fullHttpMessage == null) {
                    return;
                }
                ByteBuf byteBuf = fullHttpMessage.content();
                if (byteBuf.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes()) {
                    this.removeMessage(n);
                    throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
                }
                ByteBuf byteBuf2 = spdyDataFrame.content();
                int n3 = byteBuf2.readableBytes();
                byteBuf.writeBytes(byteBuf2, byteBuf2.readerIndex(), n3);
                if (spdyDataFrame.isLast()) {
                    HttpHeaders.setContentLength(fullHttpMessage, byteBuf.readableBytes());
                    this.removeMessage(n);
                    list.add(fullHttpMessage);
                }
            } else if (spdyFrame instanceof SpdyRstStreamFrame) {
                SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)spdyFrame;
                int n = spdyRstStreamFrame.streamId();
                this.removeMessage(n);
            }
        }
    }

    protected FullHttpMessage getMessage(int n) {
        return this.messageMap.get(n);
    }

    public SpdyHttpDecoder(SpdyVersion spdyVersion, int n) {
        this(spdyVersion, n, new HashMap<Integer, FullHttpMessage>(), true);
    }

    protected SpdyHttpDecoder(SpdyVersion spdyVersion, int n, Map<Integer, FullHttpMessage> map) {
        this(spdyVersion, n, map, true);
    }
}

