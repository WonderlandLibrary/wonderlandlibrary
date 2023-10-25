package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class NetscapeDraftSpecFactory implements CookieSpecFactory, CookieSpecProvider {
  private final String[] datepatterns;
  
  public NetscapeDraftSpecFactory(String[] datepatterns) {
    this.datepatterns = datepatterns;
  }
  
  public NetscapeDraftSpecFactory() {
    this(null);
  }
  
  public CookieSpec newInstance(HttpParams params) {
    if (params != null) {
      String[] patterns = null;
      Collection<?> param = (Collection)params.getParameter("http.protocol.cookie-datepatterns");
      if (param != null) {
        patterns = new String[param.size()];
        patterns = param.<String>toArray(patterns);
      } 
      return new NetscapeDraftSpec(patterns);
    } 
    return new NetscapeDraftSpec();
  }
  
  public CookieSpec create(HttpContext context) {
    return new NetscapeDraftSpec(this.datepatterns);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\NetscapeDraftSpecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */