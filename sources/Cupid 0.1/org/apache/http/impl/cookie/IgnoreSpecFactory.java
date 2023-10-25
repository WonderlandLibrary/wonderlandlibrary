package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider {
  public CookieSpec newInstance(HttpParams params) {
    return new IgnoreSpec();
  }
  
  public CookieSpec create(HttpContext context) {
    return new IgnoreSpec();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\IgnoreSpecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */