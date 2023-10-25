package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpResponse;

public interface ResponseHandler<T> {
  T handleResponse(HttpResponse paramHttpResponse) throws ClientProtocolException, IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\ResponseHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */