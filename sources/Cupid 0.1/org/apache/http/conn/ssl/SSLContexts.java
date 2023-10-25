package org.apache.http.conn.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.annotation.Immutable;

@Immutable
public class SSLContexts {
  public static SSLContext createDefault() throws SSLInitializationException {
    try {
      SSLContext sslcontext = SSLContext.getInstance("TLS");
      sslcontext.init(null, null, null);
      return sslcontext;
    } catch (NoSuchAlgorithmException ex) {
      throw new SSLInitializationException(ex.getMessage(), ex);
    } catch (KeyManagementException ex) {
      throw new SSLInitializationException(ex.getMessage(), ex);
    } 
  }
  
  public static SSLContext createSystemDefault() throws SSLInitializationException {
    try {
      return SSLContext.getInstance("Default");
    } catch (NoSuchAlgorithmException ex) {
      return createDefault();
    } 
  }
  
  public static SSLContextBuilder custom() {
    return new SSLContextBuilder();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ssl\SSLContexts.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */