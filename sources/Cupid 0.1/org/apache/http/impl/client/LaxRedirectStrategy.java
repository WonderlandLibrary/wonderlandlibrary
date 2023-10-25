package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class LaxRedirectStrategy extends DefaultRedirectStrategy {
  private static final String[] REDIRECT_METHODS = new String[] { "GET", "POST", "HEAD" };
  
  protected boolean isRedirectable(String method) {
    for (String m : REDIRECT_METHODS) {
      if (m.equalsIgnoreCase(method))
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\LaxRedirectStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */