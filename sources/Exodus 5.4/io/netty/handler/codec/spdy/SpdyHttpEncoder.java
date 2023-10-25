/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.spdy.DefaultSpdyDataFrame;
import io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame;
import io.netty.handler.codec.spdy.DefaultSpdySynReplyFrame;
import io.netty.handler.codec.spdy.DefaultSpdySynStreamFrame;
import io.netty.handler.codec.spdy.SpdyHeaders;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import io.netty.handler.codec.spdy.SpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdySynReplyFrame;
import io.netty.handler.codec.spdy.SpdySynStreamFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpdyHttpEncoder
extends MessageToMessageEncoder<HttpObject> {
    private final int spdyVersion;
    private int currentStreamId;

    private SpdySynReplyFrame createSynReplyFrame(HttpResponse httpResponse) throws Exception {
        int n = SpdyHttpHeaders.getStreamId(httpResponse);
        SpdyHttpHeaders.removeStreamId(httpResponse);
        httpResponse.headers().remove("Connection");
        httpResponse.headers().remove("Keep-Alive");
        httpResponse.headers().remove("Proxy-Connection");
        httpResponse.headers().remove("Transfer-Encoding");
        DefaultSpdySynReplyFrame defaultSpdySynReplyFrame = new DefaultSpdySynReplyFrame(n);
        SpdyHeaders.setStatus(this.spdyVersion, defaultSpdySynReplyFrame, httpResponse.getStatus());
        SpdyHeaders.setVersion(this.spdyVersion, defaultSpdySynReplyFrame, httpResponse.getProtocolVersion());
        for (Map.Entry entry : httpResponse.headers()) {
            defaultSpdySynReplyFrame.headers().add((String)entry.getKey(), entry.getValue());
        }
        this.currentStreamId = n;
        defaultSpdySynReplyFrame.setLast(SpdyHttpEncoder.isLast(httpResponse));
        return defaultSpdySynReplyFrame;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, HttpObject httpObject, List<Object> list) throws Exception {
        SpdyStreamFrame spdyStreamFrame;
        HttpObject httpObject2;
        boolean bl = false;
        boolean bl2 = false;
        if (httpObject instanceof HttpRequest) {
            httpObject2 = (HttpRequest)httpObject;
            spdyStreamFrame = this.createSynStreamFrame((HttpMessage)httpObject2);
            list.add(spdyStreamFrame);
            bl2 = spdyStreamFrame.isLast();
            bl = true;
        }
        if (httpObject instanceof HttpResponse) {
            httpObject2 = (HttpResponse)httpObject;
            if (httpObject2.headers().contains("X-SPDY-Associated-To-Stream-ID")) {
                spdyStreamFrame = this.createSynStreamFrame((HttpMessage)httpObject2);
                bl2 = spdyStreamFrame.isLast();
                list.add(spdyStreamFrame);
            } else {
                spdyStreamFrame = this.createSynReplyFrame((HttpResponse)httpObject2);
                bl2 = spdyStreamFrame.isLast();
                list.add(spdyStreamFrame);
            }
            bl = true;
        }
        if (httpObject instanceof HttpContent && !bl2) {
            httpObject2 = (HttpContent)httpObject;
            httpObject2.content().retain();
            spdyStreamFrame = new DefaultSpdyDataFrame(this.currentStreamId, httpObject2.content());
            spdyStreamFrame.setLast(httpObject2 instanceof LastHttpContent);
            if (httpObject2 instanceof LastHttpContent) {
                LastHttpContent lastHttpContent = (LastHttpContent)httpObject2;
                HttpHeaders httpHeaders = lastHttpContent.trailingHeaders();
                if (httpHeaders.isEmpty()) {
                    list.add(spdyStreamFrame);
                } else {
                    DefaultSpdyHeadersFrame defaultSpdyHeadersFrame = new DefaultSpdyHeadersFrame(this.currentStreamId);
                    for (Map.Entry entry : httpHeaders) {
                        defaultSpdyHeadersFrame.headers().add((String)entry.getKey(), entry.getValue());
                    }
                    list.add(defaultSpdyHeadersFrame);
                    list.add(spdyStreamFrame);
                }
            } else {
                list.add(spdyStreamFrame);
            }
            bl = true;
        }
        if (!bl) {
            throw new UnsupportedMessageTypeException(httpObject, new Class[0]);
        }
    }

    private static boolean isLast(HttpMessage httpMessage) {
        FullHttpMessage fullHttpMessage;
        return httpMessage instanceof FullHttpMessage && (fullHttpMessage = (FullHttpMessage)httpMessage).trailingHeaders().isEmpty() && !fullHttpMessage.content().isReadable();
    }

    private SpdySynStreamFrame createSynStreamFrame(HttpMessage httpMessage) throws Exception {
        Iterator iterator;
        int n = SpdyHttpHeaders.getStreamId(httpMessage);
        int n2 = SpdyHttpHeaders.getAssociatedToStreamId(httpMessage);
        byte by = SpdyHttpHeaders.getPriority(httpMessage);
        String string = SpdyHttpHeaders.getUrl(httpMessage);
        String string2 = SpdyHttpHeaders.getScheme(httpMessage);
        SpdyHttpHeaders.removeStreamId(httpMessage);
        SpdyHttpHeaders.removeAssociatedToStreamId(httpMessage);
        SpdyHttpHeaders.removePriority(httpMessage);
        SpdyHttpHeaders.removeUrl(httpMessage);
        SpdyHttpHeaders.removeScheme(httpMessage);
        httpMessage.headers().remove("Connection");
        httpMessage.headers().remove("Keep-Alive");
        httpMessage.headers().remove("Proxy-Connection");
        httpMessage.headers().remove("Transfer-Encoding");
        DefaultSpdySynStreamFrame defaultSpdySynStreamFrame = new DefaultSpdySynStreamFrame(n, n2, by);
        if (httpMessage instanceof FullHttpRequest) {
            iterator = (HttpRequest)httpMessage;
            SpdyHeaders.setMethod(this.spdyVersion, defaultSpdySynStreamFrame, iterator.getMethod());
            SpdyHeaders.setUrl(this.spdyVersion, defaultSpdySynStreamFrame, iterator.getUri());
            SpdyHeaders.setVersion(this.spdyVersion, defaultSpdySynStreamFrame, httpMessage.getProtocolVersion());
        }
        if (httpMessage instanceof HttpResponse) {
            iterator = (HttpResponse)httpMessage;
            SpdyHeaders.setStatus(this.spdyVersion, defaultSpdySynStreamFrame, iterator.getStatus());
            SpdyHeaders.setUrl(this.spdyVersion, defaultSpdySynStreamFrame, string);
            SpdyHeaders.setVersion(this.spdyVersion, defaultSpdySynStreamFrame, httpMessage.getProtocolVersion());
            defaultSpdySynStreamFrame.setUnidirectional(true);
        }
        if (this.spdyVersion >= 3) {
            iterator = HttpHeaders.getHost(httpMessage);
            httpMessage.headers().remove("Host");
            SpdyHeaders.setHost(defaultSpdySynStreamFrame, (String)((Object)iterator));
        }
        if (string2 == null) {
            string2 = "https";
        }
        SpdyHeaders.setScheme(this.spdyVersion, defaultSpdySynStreamFrame, string2);
        for (Map.Entry entry : httpMessage.headers()) {
            defaultSpdySynStreamFrame.headers().add((String)entry.getKey(), entry.getValue());
        }
        this.currentStreamId = defaultSpdySynStreamFrame.streamId();
        defaultSpdySynStreamFrame.setLast(SpdyHttpEncoder.isLast(httpMessage));
        return defaultSpdySynStreamFrame;
    }

    public SpdyHttpEncoder(SpdyVersion spdyVersion) {
        if (spdyVersion == null) {
            throw new NullPointerException("version");
        }
        this.spdyVersion = spdyVersion.getVersion();
    }
}

