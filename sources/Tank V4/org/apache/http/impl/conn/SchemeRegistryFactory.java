package org.apache.http.impl.conn;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

/** @deprecated */
@Deprecated
@ThreadSafe
public final class SchemeRegistryFactory {
   public static SchemeRegistry createDefault() {
      SchemeRegistry var0 = new SchemeRegistry();
      var0.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
      var0.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
      return var0;
   }

   public static SchemeRegistry createSystemDefault() {
      SchemeRegistry var0 = new SchemeRegistry();
      var0.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
      var0.register(new Scheme("https", 443, SSLSocketFactory.getSystemSocketFactory()));
      return var0;
   }
}
