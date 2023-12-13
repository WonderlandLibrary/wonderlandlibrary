package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
@ThreadSafe
public class DefaultHttpRoutePlanner implements HttpRoutePlanner {
   protected final SchemeRegistry schemeRegistry;

   public DefaultHttpRoutePlanner(SchemeRegistry var1) {
      Args.notNull(var1, "Scheme registry");
      this.schemeRegistry = var1;
   }

   public HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      Args.notNull(var2, "HTTP request");
      HttpRoute var4 = ConnRouteParams.getForcedRoute(var2.getParams());
      if (var4 != null) {
         return var4;
      } else {
         Asserts.notNull(var1, "Target host");
         InetAddress var5 = ConnRouteParams.getLocalAddress(var2.getParams());
         HttpHost var6 = ConnRouteParams.getDefaultProxy(var2.getParams());

         Scheme var7;
         try {
            var7 = this.schemeRegistry.getScheme(var1.getSchemeName());
         } catch (IllegalStateException var9) {
            throw new HttpException(var9.getMessage());
         }

         boolean var8 = var7.isLayered();
         if (var6 == null) {
            var4 = new HttpRoute(var1, var5, var8);
         } else {
            var4 = new HttpRoute(var1, var5, var6, var8);
         }

         return var4;
      }
   }
}
