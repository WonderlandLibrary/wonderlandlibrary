package org.apache.http.impl.auth;

import java.nio.charset.Charset;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class BasicSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
  private final Charset charset;
  
  public BasicSchemeFactory(Charset charset) {
    this.charset = charset;
  }
  
  public BasicSchemeFactory() {
    this(null);
  }
  
  public AuthScheme newInstance(HttpParams params) {
    return (AuthScheme)new BasicScheme();
  }
  
  public AuthScheme create(HttpContext context) {
    return (AuthScheme)new BasicScheme(this.charset);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\auth\BasicSchemeFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */