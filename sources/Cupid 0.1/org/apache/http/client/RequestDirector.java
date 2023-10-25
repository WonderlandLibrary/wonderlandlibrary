package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface RequestDirector {
  HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext) throws HttpException, IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\RequestDirector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */