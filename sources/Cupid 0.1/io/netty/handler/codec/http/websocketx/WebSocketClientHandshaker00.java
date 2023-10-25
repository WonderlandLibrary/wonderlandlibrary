package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketClientHandshaker00 extends WebSocketClientHandshaker {
  private ByteBuf expectedChallengeResponseBytes;
  
  public WebSocketClientHandshaker00(URI webSocketURL, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength) {
    super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
  }
  
  protected FullHttpRequest newHandshakeRequest() {
    int spaces1 = WebSocketUtil.randomNumber(1, 12);
    int spaces2 = WebSocketUtil.randomNumber(1, 12);
    int max1 = Integer.MAX_VALUE / spaces1;
    int max2 = Integer.MAX_VALUE / spaces2;
    int number1 = WebSocketUtil.randomNumber(0, max1);
    int number2 = WebSocketUtil.randomNumber(0, max2);
    int product1 = number1 * spaces1;
    int product2 = number2 * spaces2;
    String key1 = Integer.toString(product1);
    String key2 = Integer.toString(product2);
    key1 = insertRandomCharacters(key1);
    key2 = insertRandomCharacters(key2);
    key1 = insertSpaces(key1, spaces1);
    key2 = insertSpaces(key2, spaces2);
    byte[] key3 = WebSocketUtil.randomBytes(8);
    ByteBuffer buffer = ByteBuffer.allocate(4);
    buffer.putInt(number1);
    byte[] number1Array = buffer.array();
    buffer = ByteBuffer.allocate(4);
    buffer.putInt(number2);
    byte[] number2Array = buffer.array();
    byte[] challenge = new byte[16];
    System.arraycopy(number1Array, 0, challenge, 0, 4);
    System.arraycopy(number2Array, 0, challenge, 4, 4);
    System.arraycopy(key3, 0, challenge, 8, 8);
    this.expectedChallengeResponseBytes = Unpooled.wrappedBuffer(WebSocketUtil.md5(challenge));
    URI wsURL = uri();
    String path = wsURL.getPath();
    if (wsURL.getQuery() != null && !wsURL.getQuery().isEmpty())
      path = wsURL.getPath() + '?' + wsURL.getQuery(); 
    if (path == null || path.isEmpty())
      path = "/"; 
    DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
    HttpHeaders headers = defaultFullHttpRequest.headers();
    headers.add("Upgrade", "WebSocket").add("Connection", "Upgrade").add("Host", wsURL.getHost());
    int wsPort = wsURL.getPort();
    String originValue = "http://" + wsURL.getHost();
    if (wsPort != 80 && wsPort != 443)
      originValue = originValue + ':' + wsPort; 
    headers.add("Origin", originValue).add("Sec-WebSocket-Key1", key1).add("Sec-WebSocket-Key2", key2);
    String expectedSubprotocol = expectedSubprotocol();
    if (expectedSubprotocol != null && !expectedSubprotocol.isEmpty())
      headers.add("Sec-WebSocket-Protocol", expectedSubprotocol); 
    if (this.customHeaders != null)
      headers.add(this.customHeaders); 
    headers.set("Content-Length", Integer.valueOf(key3.length));
    defaultFullHttpRequest.content().writeBytes(key3);
    return (FullHttpRequest)defaultFullHttpRequest;
  }
  
  protected void verify(FullHttpResponse response) {
    HttpResponseStatus status = new HttpResponseStatus(101, "WebSocket Protocol Handshake");
    if (!response.getStatus().equals(status))
      throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + response.getStatus()); 
    HttpHeaders headers = response.headers();
    String upgrade = headers.get("Upgrade");
    if (!"WebSocket".equalsIgnoreCase(upgrade))
      throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + upgrade); 
    String connection = headers.get("Connection");
    if (!"Upgrade".equalsIgnoreCase(connection))
      throw new WebSocketHandshakeException("Invalid handshake response connection: " + connection); 
    ByteBuf challenge = response.content();
    if (!challenge.equals(this.expectedChallengeResponseBytes))
      throw new WebSocketHandshakeException("Invalid challenge"); 
  }
  
  private static String insertRandomCharacters(String key) {
    int count = WebSocketUtil.randomNumber(1, 12);
    char[] randomChars = new char[count];
    int randCount = 0;
    while (randCount < count) {
      int rand = (int)(Math.random() * 126.0D + 33.0D);
      if ((33 < rand && rand < 47) || (58 < rand && rand < 126)) {
        randomChars[randCount] = (char)rand;
        randCount++;
      } 
    } 
    for (int i = 0; i < count; i++) {
      int split = WebSocketUtil.randomNumber(0, key.length());
      String part1 = key.substring(0, split);
      String part2 = key.substring(split);
      key = part1 + randomChars[i] + part2;
    } 
    return key;
  }
  
  private static String insertSpaces(String key, int spaces) {
    for (int i = 0; i < spaces; i++) {
      int split = WebSocketUtil.randomNumber(1, key.length() - 1);
      String part1 = key.substring(0, split);
      String part2 = key.substring(split);
      key = part1 + ' ' + part2;
    } 
    return key;
  }
  
  protected WebSocketFrameDecoder newWebsocketDecoder() {
    return new WebSocket00FrameDecoder(maxFramePayloadLength());
  }
  
  protected WebSocketFrameEncoder newWebSocketEncoder() {
    return new WebSocket00FrameEncoder();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker00.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */