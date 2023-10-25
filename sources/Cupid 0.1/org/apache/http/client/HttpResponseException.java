package org.apache.http.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class HttpResponseException extends ClientProtocolException {
  private static final long serialVersionUID = -7186627969477257933L;
  
  private final int statusCode;
  
  public HttpResponseException(int statusCode, String s) {
    super(s);
    this.statusCode = statusCode;
  }
  
  public int getStatusCode() {
    return this.statusCode;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\HttpResponseException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */