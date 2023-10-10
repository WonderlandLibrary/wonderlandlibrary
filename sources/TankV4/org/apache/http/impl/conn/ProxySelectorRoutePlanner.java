package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.Proxy.Type;
import java.util.Collection;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
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
@NotThreadSafe
public class ProxySelectorRoutePlanner implements HttpRoutePlanner {
   protected final SchemeRegistry schemeRegistry;
   protected ProxySelector proxySelector;

   public ProxySelectorRoutePlanner(SchemeRegistry var1, ProxySelector var2) {
      Args.notNull(var1, "SchemeRegistry");
      this.schemeRegistry = var1;
      this.proxySelector = var2;
   }

   public ProxySelector getProxySelector() {
      return this.proxySelector;
   }

   public void setProxySelector(ProxySelector var1) {
      this.proxySelector = var1;
   }

   public HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      Args.notNull(var2, "HTTP request");
      HttpRoute var4 = ConnRouteParams.getForcedRoute(var2.getParams());
      if (var4 != null) {
         return var4;
      } else {
         Asserts.notNull(var1, "Target host");
         InetAddress var5 = ConnRouteParams.getLocalAddress(var2.getParams());
         HttpHost var6 = this.determineProxy(var1, var2, var3);
         Scheme var7 = this.schemeRegistry.getScheme(var1.getSchemeName());
         boolean var8 = var7.isLayered();
         if (var6 == null) {
            var4 = new HttpRoute(var1, var5, var8);
         } else {
            var4 = new HttpRoute(var1, var5, var6, var8);
         }

         return var4;
      }
   }

   protected HttpHost determineProxy(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      ProxySelector var4 = this.proxySelector;
      if (var4 == null) {
         var4 = ProxySelector.getDefault();
      }

      if (var4 == null) {
         return null;
      } else {
         URI var5 = null;

         try {
            var5 = new URI(var1.toURI());
         } catch (URISyntaxException var10) {
            throw new HttpException("Cannot convert host to URI: " + var1, var10);
         }

         List var6 = var4.select(var5);
         Proxy var7 = this.chooseProxy(var6, var1, var2, var3);
         HttpHost var8 = null;
         if (var7.type() == Type.HTTP) {
            if (!(var7.address() instanceof InetSocketAddress)) {
               throw new HttpException("Unable to handle non-Inet proxy address: " + var7.address());
            }

            InetSocketAddress var9 = (InetSocketAddress)var7.address();
            var8 = new HttpHost(this.getHost(var9), var9.getPort());
         }

         return var8;
      }
   }

   protected String getHost(InetSocketAddress var1) {
      return var1.isUnresolved() ? var1.getHostName() : var1.getAddress().getHostAddress();
   }

   protected Proxy chooseProxy(List var1, HttpHost var2, HttpRequest var3, HttpContext var4) {
      Args.notEmpty((Collection)var1, "List of proxies");
      Proxy var5 = null;
      int var6 = 0;

      while(var5 == null && var6 < var1.size()) {
         Proxy var7 = (Proxy)var1.get(var6);
         switch(var7.type()) {
         case DIRECT:
         case HTTP:
            var5 = var7;
         case SOCKS:
         default:
            ++var6;
         }
      }

      if (var5 == null) {
         var5 = Proxy.NO_PROXY;
      }

      return var5;
   }
}
