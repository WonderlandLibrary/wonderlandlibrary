package org.apache.http.impl.conn;

import java.net.InetAddress;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultRoutePlanner implements HttpRoutePlanner {
   private final SchemePortResolver schemePortResolver;

   public DefaultRoutePlanner(SchemePortResolver var1) {
      this.schemePortResolver = (SchemePortResolver)(var1 != null ? var1 : DefaultSchemePortResolver.INSTANCE);
   }

   public HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      Args.notNull(var1, "Target host");
      Args.notNull(var2, "Request");
      HttpClientContext var4 = HttpClientContext.adapt(var3);
      RequestConfig var5 = var4.getRequestConfig();
      InetAddress var6 = var5.getLocalAddress();
      HttpHost var7 = var5.getProxy();
      if (var7 == null) {
         var7 = this.determineProxy(var1, var2, var3);
      }

      HttpHost var8;
      if (var1.getPort() <= 0) {
         try {
            var8 = new HttpHost(var1.getHostName(), this.schemePortResolver.resolve(var1), var1.getSchemeName());
         } catch (UnsupportedSchemeException var10) {
            throw new HttpException(var10.getMessage());
         }
      } else {
         var8 = var1;
      }

      boolean var9 = var8.getSchemeName().equalsIgnoreCase("https");
      return var7 == null ? new HttpRoute(var8, var6, var9) : new HttpRoute(var8, var6, var7, var9);
   }

   protected HttpHost determineProxy(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      return null;
   }
}
