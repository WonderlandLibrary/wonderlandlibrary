package org.apache.http.impl.conn;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.Proxy.Type;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.protocol.HttpContext;

@Immutable
public class SystemDefaultRoutePlanner extends DefaultRoutePlanner {
   private final ProxySelector proxySelector;

   public SystemDefaultRoutePlanner(SchemePortResolver var1, ProxySelector var2) {
      super(var1);
      this.proxySelector = var2 != null ? var2 : ProxySelector.getDefault();
   }

   public SystemDefaultRoutePlanner(ProxySelector var1) {
      this((SchemePortResolver)null, var1);
   }

   protected HttpHost determineProxy(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      URI var4;
      try {
         var4 = new URI(var1.toURI());
      } catch (URISyntaxException var9) {
         throw new HttpException("Cannot convert host to URI: " + var1, var9);
      }

      List var5 = this.proxySelector.select(var4);
      Proxy var6 = this.chooseProxy(var5);
      HttpHost var7 = null;
      if (var6.type() == Type.HTTP) {
         if (!(var6.address() instanceof InetSocketAddress)) {
            throw new HttpException("Unable to handle non-Inet proxy address: " + var6.address());
         }

         InetSocketAddress var8 = (InetSocketAddress)var6.address();
         var7 = new HttpHost(this.getHost(var8), var8.getPort());
      }

      return var7;
   }

   private String getHost(InetSocketAddress var1) {
      return var1.isUnresolved() ? var1.getHostName() : var1.getAddress().getHostAddress();
   }

   private Proxy chooseProxy(List var1) {
      Proxy var2 = null;
      int var3 = 0;

      while(var2 == null && var3 < var1.size()) {
         Proxy var4 = (Proxy)var1.get(var3);
         switch(var4.type()) {
         case DIRECT:
         case HTTP:
            var2 = var4;
         case SOCKS:
         default:
            ++var3;
         }
      }

      if (var2 == null) {
         var2 = Proxy.NO_PROXY;
      }

      return var2;
   }
}
