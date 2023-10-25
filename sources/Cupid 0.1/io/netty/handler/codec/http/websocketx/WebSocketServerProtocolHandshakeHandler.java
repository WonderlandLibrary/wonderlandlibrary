package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

class WebSocketServerProtocolHandshakeHandler extends ChannelInboundHandlerAdapter {
  private final String websocketPath;
  
  private final String subprotocols;
  
  private final boolean allowExtensions;
  
  private final int maxFramePayloadSize;
  
  WebSocketServerProtocolHandshakeHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize) {
    this.websocketPath = websocketPath;
    this.subprotocols = subprotocols;
    this.allowExtensions = allowExtensions;
    this.maxFramePayloadSize = maxFrameSize;
  }
  
  public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
    FullHttpRequest req = (FullHttpRequest)msg;
    try {
      if (req.getMethod() != HttpMethod.GET) {
        sendHttpResponse(ctx, (HttpRequest)req, (HttpResponse)new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
        return;
      } 
      WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(ctx.pipeline(), (HttpRequest)req, this.websocketPath), this.subprotocols, this.allowExtensions, this.maxFramePayloadSize);
      WebSocketServerHandshaker handshaker = wsFactory.newHandshaker((HttpRequest)req);
      if (handshaker == null) {
        WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
      } else {
        ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req);
        handshakeFuture.addListener((GenericFutureListener)new ChannelFutureListener() {
              public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                  ctx.fireExceptionCaught(future.cause());
                } else {
                  ctx.fireUserEventTriggered(WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                } 
              }
            });
        WebSocketServerProtocolHandler.setHandshaker(ctx, handshaker);
        ctx.pipeline().replace((ChannelHandler)this, "WS403Responder", WebSocketServerProtocolHandler.forbiddenHttpRequestResponder());
      } 
    } finally {
      req.release();
    } 
  }
  
  private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
    ChannelFuture f = ctx.channel().writeAndFlush(res);
    if (!HttpHeaders.isKeepAlive((HttpMessage)req) || res.getStatus().code() != 200)
      f.addListener((GenericFutureListener)ChannelFutureListener.CLOSE); 
  }
  
  private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
    String protocol = "ws";
    if (cp.get(SslHandler.class) != null)
      protocol = "wss"; 
    return protocol + "://" + req.headers().get("Host") + path;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerProtocolHandshakeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */