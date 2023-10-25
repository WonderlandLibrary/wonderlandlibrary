package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.regex.Pattern;

public class WebSocketServerHandshaker00 extends WebSocketServerHandshaker {
  private static final Pattern BEGINNING_DIGIT = Pattern.compile("[^0-9]");
  
  private static final Pattern BEGINNING_SPACE = Pattern.compile("[^ ]");
  
  public WebSocketServerHandshaker00(String webSocketURL, String subprotocols, int maxFramePayloadLength) {
    super(WebSocketVersion.V00, webSocketURL, subprotocols, maxFramePayloadLength);
  }
  
  protected FullHttpResponse newHandshakeResponse(FullHttpRequest req, HttpHeaders headers) {
    if (!"Upgrade".equalsIgnoreCase(req.headers().get("Connection")) || !"WebSocket".equalsIgnoreCase(req.headers().get("Upgrade")))
      throw new WebSocketHandshakeException("not a WebSocket handshake request: missing upgrade"); 
    boolean isHixie76 = (req.headers().contains("Sec-WebSocket-Key1") && req.headers().contains("Sec-WebSocket-Key2"));
    DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(101, isHixie76 ? "WebSocket Protocol Handshake" : "Web Socket Protocol Handshake"));
    if (headers != null)
      defaultFullHttpResponse.headers().add(headers); 
    defaultFullHttpResponse.headers().add("Upgrade", "WebSocket");
    defaultFullHttpResponse.headers().add("Connection", "Upgrade");
    if (isHixie76) {
      defaultFullHttpResponse.headers().add("Sec-WebSocket-Origin", req.headers().get("Origin"));
      defaultFullHttpResponse.headers().add("Sec-WebSocket-Location", uri());
      String subprotocols = req.headers().get("Sec-WebSocket-Protocol");
      if (subprotocols != null) {
        String selectedSubprotocol = selectSubprotocol(subprotocols);
        if (selectedSubprotocol == null) {
          if (logger.isDebugEnabled())
            logger.debug("Requested subprotocol(s) not supported: {}", subprotocols); 
        } else {
          defaultFullHttpResponse.headers().add("Sec-WebSocket-Protocol", selectedSubprotocol);
        } 
      } 
      String key1 = req.headers().get("Sec-WebSocket-Key1");
      String key2 = req.headers().get("Sec-WebSocket-Key2");
      int a = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(key1).replaceAll("")) / BEGINNING_SPACE.matcher(key1).replaceAll("").length());
      int b = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(key2).replaceAll("")) / BEGINNING_SPACE.matcher(key2).replaceAll("").length());
      long c = req.content().readLong();
      ByteBuf input = Unpooled.buffer(16);
      input.writeInt(a);
      input.writeInt(b);
      input.writeLong(c);
      defaultFullHttpResponse.content().writeBytes(WebSocketUtil.md5(input.array()));
    } else {
      defaultFullHttpResponse.headers().add("WebSocket-Origin", req.headers().get("Origin"));
      defaultFullHttpResponse.headers().add("WebSocket-Location", uri());
      String protocol = req.headers().get("WebSocket-Protocol");
      if (protocol != null)
        defaultFullHttpResponse.headers().add("WebSocket-Protocol", selectSubprotocol(protocol)); 
    } 
    return (FullHttpResponse)defaultFullHttpResponse;
  }
  
  public ChannelFuture close(Channel channel, CloseWebSocketFrame frame, ChannelPromise promise) {
    return channel.writeAndFlush(frame, promise);
  }
  
  protected WebSocketFrameDecoder newWebsocketDecoder() {
    return new WebSocket00FrameDecoder(maxFramePayloadLength());
  }
  
  protected WebSocketFrameEncoder newWebSocketEncoder() {
    return new WebSocket00FrameEncoder();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshaker00.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */