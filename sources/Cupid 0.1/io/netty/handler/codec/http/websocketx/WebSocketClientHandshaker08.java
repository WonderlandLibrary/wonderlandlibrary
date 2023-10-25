package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.URI;

public class WebSocketClientHandshaker08 extends WebSocketClientHandshaker {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketClientHandshaker08.class);
  
  public static final String MAGIC_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
  
  private String expectedChallengeResponseString;
  
  private final boolean allowExtensions;
  
  public WebSocketClientHandshaker08(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength) {
    super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
    this.allowExtensions = allowExtensions;
  }
  
  protected FullHttpRequest newHandshakeRequest() {
    URI wsURL = uri();
    String path = wsURL.getPath();
    if (wsURL.getQuery() != null && !wsURL.getQuery().isEmpty())
      path = wsURL.getPath() + '?' + wsURL.getQuery(); 
    if (path == null || path.isEmpty())
      path = "/"; 
    byte[] nonce = WebSocketUtil.randomBytes(16);
    String key = WebSocketUtil.base64(nonce);
    String acceptSeed = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
    this.expectedChallengeResponseString = WebSocketUtil.base64(sha1);
    if (logger.isDebugEnabled())
      logger.debug("WebSocket version 08 client handshake key: {}, expected response: {}", key, this.expectedChallengeResponseString); 
    DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
    HttpHeaders headers = defaultFullHttpRequest.headers();
    headers.add("Upgrade", "WebSocket".toLowerCase()).add("Connection", "Upgrade").add("Sec-WebSocket-Key", key).add("Host", wsURL.getHost());
    int wsPort = wsURL.getPort();
    String originValue = "http://" + wsURL.getHost();
    if (wsPort != 80 && wsPort != 443)
      originValue = originValue + ':' + wsPort; 
    headers.add("Sec-WebSocket-Origin", originValue);
    String expectedSubprotocol = expectedSubprotocol();
    if (expectedSubprotocol != null && !expectedSubprotocol.isEmpty())
      headers.add("Sec-WebSocket-Protocol", expectedSubprotocol); 
    headers.add("Sec-WebSocket-Version", "8");
    if (this.customHeaders != null)
      headers.add(this.customHeaders); 
    return (FullHttpRequest)defaultFullHttpRequest;
  }
  
  protected void verify(FullHttpResponse response) {
    HttpResponseStatus status = HttpResponseStatus.SWITCHING_PROTOCOLS;
    HttpHeaders headers = response.headers();
    if (!response.getStatus().equals(status))
      throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + response.getStatus()); 
    String upgrade = headers.get("Upgrade");
    if (!"WebSocket".equalsIgnoreCase(upgrade))
      throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + upgrade); 
    String connection = headers.get("Connection");
    if (!"Upgrade".equalsIgnoreCase(connection))
      throw new WebSocketHandshakeException("Invalid handshake response connection: " + connection); 
    String accept = headers.get("Sec-WebSocket-Accept");
    if (accept == null || !accept.equals(this.expectedChallengeResponseString))
      throw new WebSocketHandshakeException(String.format("Invalid challenge. Actual: %s. Expected: %s", new Object[] { accept, this.expectedChallengeResponseString })); 
  }
  
  protected WebSocketFrameDecoder newWebsocketDecoder() {
    return new WebSocket08FrameDecoder(false, this.allowExtensions, maxFramePayloadLength());
  }
  
  protected WebSocketFrameEncoder newWebSocketEncoder() {
    return new WebSocket08FrameEncoder(true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker08.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */