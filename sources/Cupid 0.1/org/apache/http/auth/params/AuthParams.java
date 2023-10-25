package org.apache.http.auth.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public final class AuthParams {
  public static String getCredentialCharset(HttpParams params) {
    Args.notNull(params, "HTTP parameters");
    String charset = (String)params.getParameter("http.auth.credential-charset");
    if (charset == null)
      charset = HTTP.DEF_PROTOCOL_CHARSET.name(); 
    return charset;
  }
  
  public static void setCredentialCharset(HttpParams params, String charset) {
    Args.notNull(params, "HTTP parameters");
    params.setParameter("http.auth.credential-charset", charset);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\auth\params\AuthParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */