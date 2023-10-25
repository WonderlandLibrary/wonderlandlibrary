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
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.URI;

public abstract class WebSocketClientHandshaker {
  private final URI uri;
  
  private final WebSocketVersion version;
  
  private volatile boolean handshakeComplete;
  
  private final String expectedSubprotocol;
  
  private volatile String actualSubprotocol;
  
  protected final HttpHeaders customHeaders;
  
  private final int maxFramePayloadLength;
  
  protected WebSocketClientHandshaker(URI uri, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength) {
    this.uri = uri;
    this.version = version;
    this.expectedSubprotocol = subprotocol;
    this.customHeaders = customHeaders;
    this.maxFramePayloadLength = maxFramePayloadLength;
  }
  
  public URI uri() {
    return this.uri;
  }
  
  public WebSocketVersion version() {
    return this.version;
  }
  
  public int maxFramePayloadLength() {
    return this.maxFramePayloadLength;
  }
  
  public boolean isHandshakeComplete() {
    return this.handshakeComplete;
  }
  
  private void setHandshakeComplete() {
    this.handshakeComplete = true;
  }
  
  public String expectedSubprotocol() {
    return this.expectedSubprotocol;
  }
  
  public String actualSubprotocol() {
    return this.actualSubprotocol;
  }
  
  private void setActualSubprotocol(String actualSubprotocol) {
    this.actualSubprotocol = actualSubprotocol;
  }
  
  public ChannelFuture handshake(Channel channel) {
    if (channel == null)
      throw new NullPointerException("channel"); 
    return handshake(channel, channel.newPromise());
  }
  
  public final ChannelFuture handshake(Channel channel, final ChannelPromise promise) {
    FullHttpRequest request = newHandshakeRequest();
    HttpResponseDecoder decoder = (HttpResponseDecoder)channel.pipeline().get(HttpResponseDecoder.class);
    if (decoder == null) {
      HttpClientCodec codec = (HttpClientCodec)channel.pipeline().get(HttpClientCodec.class);
      if (codec == null) {
        promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
        return (ChannelFuture)promise;
      } 
    } 
    channel.writeAndFlush(request).addListener((GenericFutureListener)new ChannelFutureListener() {
          public void operationComplete(ChannelFuture future) {
            if (future.isSuccess()) {
              ChannelPipeline p = future.channel().pipeline();
              ChannelHandlerContext ctx = p.context(HttpRequestEncoder.class);
              if (ctx == null)
                ctx = p.context(HttpClientCodec.class); 
              if (ctx == null) {
                promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec"));
                return;
              } 
              p.addAfter(ctx.name(), "ws-encoder", (ChannelHandler)WebSocketClientHandshaker.this.newWebSocketEncoder());
              promise.setSuccess();
            } else {
              promise.setFailure(future.cause());
            } 
          }
        });
    return (ChannelFuture)promise;
  }
  
  protected abstract FullHttpRequest newHandshakeRequest();
  
  public final void finishHandshake(Channel channel, FullHttpResponse response) {
    verify(response);
    setActualSubprotocol(response.headers().get("Sec-WebSocket-Protocol"));
    setHandshakeComplete();
    ChannelPipeline p = channel.pipeline();
    HttpContentDecompressor decompressor = (HttpContentDecompressor)p.get(HttpContentDecompressor.class);
    if (decompressor != null)
      p.remove((ChannelHandler)decompressor); 
    ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
    if (ctx == null) {
      ctx = p.context(HttpClientCodec.class);
      if (ctx == null)
        throw new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec"); 
      p.replace(ctx.name(), "ws-decoder", (ChannelHandler)newWebsocketDecoder());
    } else {
      if (p.get(HttpRequestEncoder.class) != null)
        p.remove(HttpRequestEncoder.class); 
      p.replace(ctx.name(), "ws-decoder", (ChannelHandler)newWebsocketDecoder());
    } 
  }
  
  protected abstract void verify(FullHttpResponse paramFullHttpResponse);
  
  protected abstract WebSocketFrameDecoder newWebsocketDecoder();
  
  protected abstract WebSocketFrameEncoder newWebSocketEncoder();
  
  public ChannelFuture close(Channel channel, CloseWebSocketFrame frame) {
    if (channel == null)
      throw new NullPointerException("channel"); 
    return close(channel, frame, channel.newPromise());
  }
  
  public ChannelFuture close(Channel channel, CloseWebSocketFrame frame, ChannelPromise promise) {
    if (channel == null)
      throw new NullPointerException("channel"); 
    return channel.writeAndFlush(frame, promise);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */