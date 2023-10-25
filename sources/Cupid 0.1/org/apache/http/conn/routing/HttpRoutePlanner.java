package org.apache.http.conn.routing;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

public interface HttpRoutePlanner {
  HttpRoute determineRoute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext) throws HttpException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\routing\HttpRoutePlanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */