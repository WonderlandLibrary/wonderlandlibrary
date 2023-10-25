package io.netty.handler.codec.http;

public interface HttpRequest extends HttpMessage {
  HttpMethod getMethod();
  
  HttpRequest setMethod(HttpMethod paramHttpMethod);
  
  String getUri();
  
  HttpRequest setUri(String paramString);
  
  HttpRequest setProtocolVersion(HttpVersion paramHttpVersion);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\HttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */