package org.apache.http.protocol;

import org.apache.http.HttpRequest;

public interface HttpRequestHandlerMapper {
  HttpRequestHandler lookup(HttpRequest paramHttpRequest);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\HttpRequestHandlerMapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */