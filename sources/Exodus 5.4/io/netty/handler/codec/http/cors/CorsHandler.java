/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.cors;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
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
    private HttpRequest request;
    private final CorsConfig config;

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        logger.error("Caught error in CorsHandler", throwable);
        channelHandlerContext.fireExceptionCaught(throwable);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        if (this.config.isCorsSupportEnabled() && object instanceof HttpRequest) {
            this.request = (HttpRequest)object;
            if (CorsHandler.isPreflightRequest(this.request)) {
                this.handlePreflight(channelHandlerContext, this.request);
                return;
            }
            if (this.config.isShortCurcuit() && !this.validateOrigin()) {
                CorsHandler.forbidden(channelHandlerContext, this.request);
                return;
            }
        }
        channelHandlerContext.fireChannelRead(object);
    }

    private static void setVaryHeader(HttpResponse httpResponse) {
        httpResponse.headers().set("Vary", (Object)"Origin");
    }

    private static void setOrigin(HttpResponse httpResponse, String string) {
        httpResponse.headers().set("Access-Control-Allow-Origin", (Object)string);
    }

    private boolean setOrigin(HttpResponse httpResponse) {
        String string = this.request.headers().get("Origin");
        if (string != null) {
            if ("null".equals(string) && this.config.isNullOriginAllowed()) {
                CorsHandler.setAnyOrigin(httpResponse);
                return true;
            }
            if (this.config.isAnyOriginSupported()) {
                if (this.config.isCredentialsAllowed()) {
                    this.echoRequestOrigin(httpResponse);
                    CorsHandler.setVaryHeader(httpResponse);
                } else {
                    CorsHandler.setAnyOrigin(httpResponse);
                }
                return true;
            }
            if (this.config.origins().contains(string)) {
                CorsHandler.setOrigin(httpResponse, string);
                CorsHandler.setVaryHeader(httpResponse);
                return true;
            }
            logger.debug("Request origin [" + string + "] was not among the configured origins " + this.config.origins());
        }
        return false;
    }

    private boolean validateOrigin() {
        if (this.config.isAnyOriginSupported()) {
            return true;
        }
        String string = this.request.headers().get("Origin");
        if (string == null) {
            return true;
        }
        if ("null".equals(string) && this.config.isNullOriginAllowed()) {
            return true;
        }
        return this.config.origins().contains(string);
    }

    private void setAllowHeaders(HttpResponse httpResponse) {
        httpResponse.headers().set("Access-Control-Allow-Headers", (Iterable<?>)this.config.allowedRequestHeaders());
    }

    private void echoRequestOrigin(HttpResponse httpResponse) {
        CorsHandler.setOrigin(httpResponse, this.request.headers().get("Origin"));
    }

    private static boolean isPreflightRequest(HttpRequest httpRequest) {
        HttpHeaders httpHeaders = httpRequest.headers();
        return httpRequest.getMethod().equals(HttpMethod.OPTIONS) && httpHeaders.contains("Origin") && httpHeaders.contains("Access-Control-Request-Method");
    }

    public CorsHandler(CorsConfig corsConfig) {
        this.config = corsConfig;
    }

    private void handlePreflight(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(httpRequest.getProtocolVersion(), HttpResponseStatus.OK);
        if (this.setOrigin(defaultFullHttpResponse)) {
            this.setAllowMethods(defaultFullHttpResponse);
            this.setAllowHeaders(defaultFullHttpResponse);
            this.setAllowCredentials(defaultFullHttpResponse);
            this.setMaxAge(defaultFullHttpResponse);
            this.setPreflightHeaders(defaultFullHttpResponse);
        }
        channelHandlerContext.writeAndFlush(defaultFullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        HttpResponse httpResponse;
        if (this.config.isCorsSupportEnabled() && object instanceof HttpResponse && this.setOrigin(httpResponse = (HttpResponse)object)) {
            this.setAllowCredentials(httpResponse);
            this.setAllowHeaders(httpResponse);
            this.setExposeHeaders(httpResponse);
        }
        channelHandlerContext.writeAndFlush(object, channelPromise);
    }

    private void setPreflightHeaders(HttpResponse httpResponse) {
        httpResponse.headers().add(this.config.preflightResponseHeaders());
    }

    private void setMaxAge(HttpResponse httpResponse) {
        httpResponse.headers().set("Access-Control-Max-Age", (Object)this.config.maxAge());
    }

    private static void setAnyOrigin(HttpResponse httpResponse) {
        CorsHandler.setOrigin(httpResponse, "*");
    }

    private void setAllowMethods(HttpResponse httpResponse) {
        httpResponse.headers().set("Access-Control-Allow-Methods", (Iterable<?>)this.config.allowedRequestMethods());
    }

    private void setExposeHeaders(HttpResponse httpResponse) {
        if (!this.config.exposedHeaders().isEmpty()) {
            httpResponse.headers().set("Access-Control-Expose-Headers", (Iterable<?>)this.config.exposedHeaders());
        }
    }

    private static void forbidden(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) {
        channelHandlerContext.writeAndFlush(new DefaultFullHttpResponse(httpRequest.getProtocolVersion(), HttpResponseStatus.FORBIDDEN)).addListener(ChannelFutureListener.CLOSE);
    }

    private void setAllowCredentials(HttpResponse httpResponse) {
        if (this.config.isCredentialsAllowed()) {
            httpResponse.headers().set("Access-Control-Allow-Credentials", (Object)"true");
        }
    }
}

