/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.websocketx;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class WebSocketServerHandshaker {
    private final String[] subprotocols;
    private final String uri;
    private String selectedSubprotocol;
    private final WebSocketVersion version;
    public static final String SUB_PROTOCOL_WILDCARD = "*";
    protected static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketServerHandshaker.class);
    private final int maxFramePayloadLength;

    public int maxFramePayloadLength() {
        return this.maxFramePayloadLength;
    }

    public String uri() {
        return this.uri;
    }

    protected abstract WebSocketFrameEncoder newWebSocketEncoder();

    public Set<String> subprotocols() {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        Collections.addAll(linkedHashSet, this.subprotocols);
        return linkedHashSet;
    }

    public WebSocketVersion version() {
        return this.version;
    }

    public final ChannelFuture handshake(Channel channel, FullHttpRequest fullHttpRequest, HttpHeaders httpHeaders, final ChannelPromise channelPromise) {
        String string;
        ChannelHandlerContext channelHandlerContext;
        if (logger.isDebugEnabled()) {
            logger.debug("{} WebSocket version {} server handshake", (Object)channel, (Object)this.version());
        }
        FullHttpResponse fullHttpResponse = this.newHandshakeResponse(fullHttpRequest, httpHeaders);
        ChannelPipeline channelPipeline = channel.pipeline();
        if (channelPipeline.get(HttpObjectAggregator.class) != null) {
            channelPipeline.remove(HttpObjectAggregator.class);
        }
        if (channelPipeline.get(HttpContentCompressor.class) != null) {
            channelPipeline.remove(HttpContentCompressor.class);
        }
        if ((channelHandlerContext = channelPipeline.context(HttpRequestDecoder.class)) == null) {
            channelHandlerContext = channelPipeline.context(HttpServerCodec.class);
            if (channelHandlerContext == null) {
                channelPromise.setFailure(new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
                return channelPromise;
            }
            channelPipeline.addBefore(channelHandlerContext.name(), "wsdecoder", this.newWebsocketDecoder());
            channelPipeline.addBefore(channelHandlerContext.name(), "wsencoder", this.newWebSocketEncoder());
            string = channelHandlerContext.name();
        } else {
            channelPipeline.replace(channelHandlerContext.name(), "wsdecoder", (ChannelHandler)this.newWebsocketDecoder());
            string = channelPipeline.context(HttpResponseEncoder.class).name();
            channelPipeline.addBefore(string, "wsencoder", this.newWebSocketEncoder());
        }
        channel.writeAndFlush(fullHttpResponse).addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    ChannelPipeline channelPipeline = channelFuture.channel().pipeline();
                    channelPipeline.remove(string);
                    channelPromise.setSuccess();
                } else {
                    channelPromise.setFailure(channelFuture.cause());
                }
            }
        });
        return channelPromise;
    }

    protected WebSocketServerHandshaker(WebSocketVersion webSocketVersion, String string, String string2, int n) {
        this.version = webSocketVersion;
        this.uri = string;
        if (string2 != null) {
            String[] stringArray = StringUtil.split(string2, ',');
            for (int i = 0; i < stringArray.length; ++i) {
                stringArray[i] = stringArray[i].trim();
            }
            this.subprotocols = stringArray;
        } else {
            this.subprotocols = EmptyArrays.EMPTY_STRINGS;
        }
        this.maxFramePayloadLength = n;
    }

    public ChannelFuture close(Channel channel, CloseWebSocketFrame closeWebSocketFrame) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return this.close(channel, closeWebSocketFrame, channel.newPromise());
    }

    public ChannelFuture close(Channel channel, CloseWebSocketFrame closeWebSocketFrame, ChannelPromise channelPromise) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        return channel.writeAndFlush(closeWebSocketFrame, channelPromise).addListener(ChannelFutureListener.CLOSE);
    }

    public String selectedSubprotocol() {
        return this.selectedSubprotocol;
    }

    public ChannelFuture handshake(Channel channel, FullHttpRequest fullHttpRequest) {
        return this.handshake(channel, fullHttpRequest, null, channel.newPromise());
    }

    protected abstract WebSocketFrameDecoder newWebsocketDecoder();

    protected abstract FullHttpResponse newHandshakeResponse(FullHttpRequest var1, HttpHeaders var2);

    protected String selectSubprotocol(String string) {
        String[] stringArray;
        if (string == null || this.subprotocols.length == 0) {
            return null;
        }
        for (String string2 : stringArray = StringUtil.split(string, ',')) {
            String string3 = string2.trim();
            for (String string4 : this.subprotocols) {
                if (!SUB_PROTOCOL_WILDCARD.equals(string4) && !string3.equals(string4)) continue;
                this.selectedSubprotocol = string3;
                return string3;
            }
        }
        return null;
    }
}

