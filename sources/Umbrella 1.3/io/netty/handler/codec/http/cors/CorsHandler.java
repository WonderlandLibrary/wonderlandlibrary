/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.http.cors;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class CorsHandler
extends ChannelDuplexHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(CorsHandler.class);
    private final CorsConfig config;
    private HttpRequest request;

    public CorsHandler(CorsConfig config) {
        this.config = config;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.config.isCorsSupportEnabled() && msg instanceof HttpRequest) {
            this.request = (HttpRequest)msg;
            if (CorsHandler.isPreflightRequest(this.request)) {
                this.handlePreflight(ctx, this.request);
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }

    private void handlePreflight(ChannelHandlerContext ctx, HttpRequest request) {
        DefaultHttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        if (this.setOrigin(response)) {
            this.setAllowMethods(response);
            this.setAllowHeaders(response);
            this.setAllowCredentials(response);
            this.setMaxAge(response);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private boolean setOrigin(HttpResponse response) {
        String origin = this.request.headers().get("Origin");
        if (origin != null) {
            if ("null".equals(origin) && this.config.isNullOriginAllowed()) {
                response.headers().set("Access-Control-Allow-Origin", (Object)"*");
            } else {
                response.headers().set("Access-Control-Allow-Origin", (Object)this.config.origin());
            }
            return true;
        }
        return false;
    }

    private void setAllowCredentials(HttpResponse response) {
        if (this.config.isCredentialsAllowed()) {
            response.headers().set("Access-Control-Allow-Credentials", (Object)"true");
        }
    }

    private static boolean isPreflightRequest(HttpRequest request) {
        HttpHeaders headers = request.headers();
        return request.getMethod().equals(HttpMethod.OPTIONS) && headers.contains("Origin") && headers.contains("Access-Control-Request-Method");
    }

    private void setExposeHeaders(HttpResponse response) {
        if (!this.config.exposedHeaders().isEmpty()) {
            response.headers().set("Access-Control-Expose-Headers", (Iterable<?>)this.config.exposedHeaders());
        }
    }

    private void setAllowMethods(HttpResponse response) {
        response.headers().set("Access-Control-Allow-Methods", (Iterable<?>)this.config.allowedRequestMethods());
    }

    private void setAllowHeaders(HttpResponse response) {
        response.headers().set("Access-Control-Allow-Headers", (Iterable<?>)this.config.allowedRequestHeaders());
    }

    private void setMaxAge(HttpResponse response) {
        response.headers().set("Access-Control-Max-Age", (Object)this.config.maxAge());
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        HttpResponse response;
        if (this.config.isCorsSupportEnabled() && msg instanceof HttpResponse && this.setOrigin(response = (HttpResponse)msg)) {
            this.setAllowCredentials(response);
            this.setAllowHeaders(response);
            this.setExposeHeaders(response);
        }
        ctx.writeAndFlush(msg, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Caught error in CorsHandler", cause);
        ctx.fireExceptionCaught(cause);
    }
}

