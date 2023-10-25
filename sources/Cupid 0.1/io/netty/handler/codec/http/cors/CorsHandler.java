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
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class CorsHandler extends ChannelDuplexHandler {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(CorsHandler.class);
  
  private final CorsConfig config;
  
  private HttpRequest request;
  
  public CorsHandler(CorsConfig config) {
    this.config = config;
  }
  
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (this.config.isCorsSupportEnabled() && msg instanceof HttpRequest) {
      this.request = (HttpRequest)msg;
      if (isPreflightRequest(this.request)) {
        handlePreflight(ctx, this.request);
        return;
      } 
      if (this.config.isShortCurcuit() && !validateOrigin()) {
        forbidden(ctx, this.request);
        return;
      } 
    } 
    ctx.fireChannelRead(msg);
  }
  
  private void handlePreflight(ChannelHandlerContext ctx, HttpRequest request) {
    DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
    if (setOrigin((HttpResponse)defaultFullHttpResponse)) {
      setAllowMethods((HttpResponse)defaultFullHttpResponse);
      setAllowHeaders((HttpResponse)defaultFullHttpResponse);
      setAllowCredentials((HttpResponse)defaultFullHttpResponse);
      setMaxAge((HttpResponse)defaultFullHttpResponse);
      setPreflightHeaders((HttpResponse)defaultFullHttpResponse);
    } 
    ctx.writeAndFlush(defaultFullHttpResponse).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
  }
  
  private void setPreflightHeaders(HttpResponse response) {
    response.headers().add(this.config.preflightResponseHeaders());
  }
  
  private boolean setOrigin(HttpResponse response) {
    String origin = this.request.headers().get("Origin");
    if (origin != null) {
      if ("null".equals(origin) && this.config.isNullOriginAllowed()) {
        setAnyOrigin(response);
        return true;
      } 
      if (this.config.isAnyOriginSupported()) {
        if (this.config.isCredentialsAllowed()) {
          echoRequestOrigin(response);
          setVaryHeader(response);
        } else {
          setAnyOrigin(response);
        } 
        return true;
      } 
      if (this.config.origins().contains(origin)) {
        setOrigin(response, origin);
        setVaryHeader(response);
        return true;
      } 
      logger.debug("Request origin [" + origin + "] was not among the configured origins " + this.config.origins());
    } 
    return false;
  }
  
  private boolean validateOrigin() {
    if (this.config.isAnyOriginSupported())
      return true; 
    String origin = this.request.headers().get("Origin");
    if (origin == null)
      return true; 
    if ("null".equals(origin) && this.config.isNullOriginAllowed())
      return true; 
    return this.config.origins().contains(origin);
  }
  
  private void echoRequestOrigin(HttpResponse response) {
    setOrigin(response, this.request.headers().get("Origin"));
  }
  
  private static void setVaryHeader(HttpResponse response) {
    response.headers().set("Vary", "Origin");
  }
  
  private static void setAnyOrigin(HttpResponse response) {
    setOrigin(response, "*");
  }
  
  private static void setOrigin(HttpResponse response, String origin) {
    response.headers().set("Access-Control-Allow-Origin", origin);
  }
  
  private void setAllowCredentials(HttpResponse response) {
    if (this.config.isCredentialsAllowed())
      response.headers().set("Access-Control-Allow-Credentials", "true"); 
  }
  
  private static boolean isPreflightRequest(HttpRequest request) {
    HttpHeaders headers = request.headers();
    return (request.getMethod().equals(HttpMethod.OPTIONS) && headers.contains("Origin") && headers.contains("Access-Control-Request-Method"));
  }
  
  private void setExposeHeaders(HttpResponse response) {
    if (!this.config.exposedHeaders().isEmpty())
      response.headers().set("Access-Control-Expose-Headers", this.config.exposedHeaders()); 
  }
  
  private void setAllowMethods(HttpResponse response) {
    response.headers().set("Access-Control-Allow-Methods", this.config.allowedRequestMethods());
  }
  
  private void setAllowHeaders(HttpResponse response) {
    response.headers().set("Access-Control-Allow-Headers", this.config.allowedRequestHeaders());
  }
  
  private void setMaxAge(HttpResponse response) {
    response.headers().set("Access-Control-Max-Age", Long.valueOf(this.config.maxAge()));
  }
  
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    if (this.config.isCorsSupportEnabled() && msg instanceof HttpResponse) {
      HttpResponse response = (HttpResponse)msg;
      if (setOrigin(response)) {
        setAllowCredentials(response);
        setAllowHeaders(response);
        setExposeHeaders(response);
      } 
    } 
    ctx.writeAndFlush(msg, promise);
  }
  
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    logger.error("Caught error in CorsHandler", cause);
    ctx.fireExceptionCaught(cause);
  }
  
  private static void forbidden(ChannelHandlerContext ctx, HttpRequest request) {
    ctx.writeAndFlush(new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.FORBIDDEN)).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\cors\CorsHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */