package org.apache.http.impl.conn;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;

@Deprecated
@ThreadSafe
public final class SchemeRegistryFactory {
  public static SchemeRegistry createDefault() {
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", 80, (SchemeSocketFactory)PlainSocketFactory.getSocketFactory()));
    registry.register(new Scheme("https", 443, (SchemeSocketFactory)SSLSocketFactory.getSocketFactory()));
    return registry;
  }
  
  public static SchemeRegistry createSystemDefault() {
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", 80, (SchemeSocketFactory)PlainSocketFactory.getSocketFactory()));
    registry.register(new Scheme("https", 443, (SchemeSocketFactory)SSLSocketFactory.getSystemSocketFactory()));
    return registry;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\SchemeRegistryFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */