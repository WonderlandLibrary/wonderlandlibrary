package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.protocol.HttpContext;

@Immutable
public class NoopUserTokenHandler implements UserTokenHandler {
  public static final NoopUserTokenHandler INSTANCE = new NoopUserTokenHandler();
  
  public Object getUserToken(HttpContext context) {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\NoopUserTokenHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */