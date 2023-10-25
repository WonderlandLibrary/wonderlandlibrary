/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.spdy.SpdyFrameCodec;
import io.netty.handler.codec.spdy.SpdyHttpDecoder;
import io.netty.handler.codec.spdy.SpdyHttpEncoder;
import io.netty.handler.codec.spdy.SpdyHttpResponseStreamIdHandler;
import io.netty.handler.codec.spdy.SpdySessionHandler;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.internal.StringUtil;
import java.util.List;
import javax.net.ssl.SSLEngine;

public abstract class SpdyOrHttpChooser
extends ByteToMessageDecoder {
    private final int maxSpdyContentLength;
    private final int maxHttpContentLength;

    private boolean initPipeline(ChannelHandlerContext channelHandlerContext) {
        SslHandler sslHandler = channelHandlerContext.pipeline().get(SslHandler.class);
        if (sslHandler == null) {
            throw new IllegalStateException("SslHandler is needed for SPDY");
        }
        SelectedProtocol selectedProtocol = this.getProtocol(sslHandler.engine());
        switch (selectedProtocol) {
            case UNKNOWN: {
                return false;
            }
            case SPDY_3_1: {
                this.addSpdyHandlers(channelHandlerContext, SpdyVersion.SPDY_3_1);
                break;
            }
            case HTTP_1_0: 
            case HTTP_1_1: {
                this.addHttpHandlers(channelHandlerContext);
                break;
            }
            default: {
                throw new IllegalStateException("Unknown SelectedProtocol");
            }
        }
        return true;
    }

    protected void addSpdyHandlers(ChannelHandlerContext channelHandlerContext, SpdyVersion spdyVersion) {
        ChannelPipeline channelPipeline = channelHandlerContext.pipeline();
        channelPipeline.addLast("spdyFrameCodec", (ChannelHandler)new SpdyFrameCodec(spdyVersion));
        channelPipeline.addLast("spdySessionHandler", (ChannelHandler)new SpdySessionHandler(spdyVersion, true));
        channelPipeline.addLast("spdyHttpEncoder", (ChannelHandler)new SpdyHttpEncoder(spdyVersion));
        channelPipeline.addLast("spdyHttpDecoder", (ChannelHandler)new SpdyHttpDecoder(spdyVersion, this.maxSpdyContentLength));
        channelPipeline.addLast("spdyStreamIdHandler", (ChannelHandler)new SpdyHttpResponseStreamIdHandler());
        channelPipeline.addLast("httpRequestHandler", (ChannelHandler)this.createHttpRequestHandlerForSpdy());
    }

    protected SpdyOrHttpChooser(int n, int n2) {
        this.maxSpdyContentLength = n;
        this.maxHttpContentLength = n2;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (this.initPipeline(channelHandlerContext)) {
            channelHandlerContext.pipeline().remove(this);
        }
    }

    protected void addHttpHandlers(ChannelHandlerContext channelHandlerContext) {
        ChannelPipeline channelPipeline = channelHandlerContext.pipeline();
        channelPipeline.addLast("httpRequestDecoder", (ChannelHandler)new HttpRequestDecoder());
        channelPipeline.addLast("httpResponseEncoder", (ChannelHandler)new HttpResponseEncoder());
        channelPipeline.addLast("httpChunkAggregator", (ChannelHandler)new HttpObjectAggregator(this.maxHttpContentLength));
        channelPipeline.addLast("httpRequestHandler", (ChannelHandler)this.createHttpRequestHandlerForHttp());
    }

    protected abstract ChannelInboundHandler createHttpRequestHandlerForHttp();

    protected ChannelInboundHandler createHttpRequestHandlerForSpdy() {
        return this.createHttpRequestHandlerForHttp();
    }

    protected SelectedProtocol getProtocol(SSLEngine sSLEngine) {
        String[] stringArray = StringUtil.split(sSLEngine.getSession().getProtocol(), ':');
        if (stringArray.length < 2) {
            return SelectedProtocol.HTTP_1_1;
        }
        SelectedProtocol selectedProtocol = SelectedProtocol.protocol(stringArray[1]);
        return selectedProtocol;
    }

    public static enum SelectedProtocol {
        SPDY_3_1("spdy/3.1"),
        HTTP_1_1("http/1.1"),
        HTTP_1_0("http/1.0"),
        UNKNOWN("Unknown");

        private final String name;

        public static SelectedProtocol protocol(String string) {
            for (SelectedProtocol selectedProtocol : SelectedProtocol.values()) {
                if (!selectedProtocol.protocolName().equals(string)) continue;
                return selectedProtocol;
            }
            return UNKNOWN;
        }

        public String protocolName() {
            return this.name;
        }

        private SelectedProtocol(String string2) {
            this.name = string2;
        }
    }
}

