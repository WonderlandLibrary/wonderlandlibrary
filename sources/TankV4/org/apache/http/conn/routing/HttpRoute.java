package org.apache.http.conn.routing;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public final class HttpRoute implements RouteInfo, Cloneable {
   private final HttpHost targetHost;
   private final InetAddress localAddress;
   private final List proxyChain;
   private final RouteInfo.TunnelType tunnelled;
   private final RouteInfo.LayerType layered;
   private final boolean secure;

   private HttpRoute(HttpHost var1, InetAddress var2, List var3, boolean var4, RouteInfo.TunnelType var5, RouteInfo.LayerType var6) {
      Args.notNull(var1, "Target host");
      this.targetHost = var1;
      this.localAddress = var2;
      if (var3 != null && !var3.isEmpty()) {
         this.proxyChain = new ArrayList(var3);
      } else {
         this.proxyChain = null;
      }

      if (var5 == RouteInfo.TunnelType.TUNNELLED) {
         Args.check(this.proxyChain != null, "Proxy required if tunnelled");
      }

      this.secure = var4;
      this.tunnelled = var5 != null ? var5 : RouteInfo.TunnelType.PLAIN;
      this.layered = var6 != null ? var6 : RouteInfo.LayerType.PLAIN;
   }

   public HttpRoute(HttpHost var1, InetAddress var2, HttpHost[] var3, boolean var4, RouteInfo.TunnelType var5, RouteInfo.LayerType var6) {
      this(var1, var2, var3 != null ? Arrays.asList(var3) : null, var4, var5, var6);
   }

   public HttpRoute(HttpHost var1, InetAddress var2, HttpHost var3, boolean var4, RouteInfo.TunnelType var5, RouteInfo.LayerType var6) {
      this(var1, var2, var3 != null ? Collections.singletonList(var3) : null, var4, var5, var6);
   }

   public HttpRoute(HttpHost var1, InetAddress var2, boolean var3) {
      this(var1, var2, Collections.emptyList(), var3, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
   }

   public HttpRoute(HttpHost var1) {
      this(var1, (InetAddress)null, (List)Collections.emptyList(), false, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
   }

   public HttpRoute(HttpHost var1, InetAddress var2, HttpHost var3, boolean var4) {
      this(var1, var2, Collections.singletonList(Args.notNull(var3, "Proxy host")), var4, var4 ? RouteInfo.TunnelType.TUNNELLED : RouteInfo.TunnelType.PLAIN, var4 ? RouteInfo.LayerType.LAYERED : RouteInfo.LayerType.PLAIN);
   }

   public HttpRoute(HttpHost var1, HttpHost var2) {
      this(var1, (InetAddress)null, var2, false);
   }

   public final HttpHost getTargetHost() {
      return this.targetHost;
   }

   public final InetAddress getLocalAddress() {
      return this.localAddress;
   }

   public final InetSocketAddress getLocalSocketAddress() {
      return this.localAddress != null ? new InetSocketAddress(this.localAddress, 0) : null;
   }

   public final int getHopCount() {
      return this.proxyChain != null ? this.proxyChain.size() + 1 : 1;
   }

   public final HttpHost getHopTarget(int var1) {
      Args.notNegative(var1, "Hop index");
      int var2 = this.getHopCount();
      Args.check(var1 < var2, "Hop index exceeds tracked route length");
      return var1 < var2 - 1 ? (HttpHost)this.proxyChain.get(var1) : this.targetHost;
   }

   public final HttpHost getProxyHost() {
      return this.proxyChain != null && !this.proxyChain.isEmpty() ? (HttpHost)this.proxyChain.get(0) : null;
   }

   public final RouteInfo.TunnelType getTunnelType() {
      return this.tunnelled;
   }

   public final boolean isTunnelled() {
      return this.tunnelled == RouteInfo.TunnelType.TUNNELLED;
   }

   public final RouteInfo.LayerType getLayerType() {
      return this.layered;
   }

   public final boolean isLayered() {
      return this.layered == RouteInfo.LayerType.LAYERED;
   }

   public final boolean isSecure() {
      return this.secure;
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof HttpRoute)) {
         return false;
      } else {
         HttpRoute var2 = (HttpRoute)var1;
         return this.secure == var2.secure && this.tunnelled == var2.tunnelled && this.layered == var2.layered && LangUtils.equals(this.targetHost, var2.targetHost) && LangUtils.equals(this.localAddress, var2.localAddress) && LangUtils.equals(this.proxyChain, var2.proxyChain);
      }
   }

   public final int hashCode() {
      byte var1 = 17;
      int var4 = LangUtils.hashCode(var1, this.targetHost);
      var4 = LangUtils.hashCode(var4, this.localAddress);
      HttpHost var3;
      if (this.proxyChain != null) {
         for(Iterator var2 = this.proxyChain.iterator(); var2.hasNext(); var4 = LangUtils.hashCode(var4, var3)) {
            var3 = (HttpHost)var2.next();
         }
      }

      var4 = LangUtils.hashCode(var4, this.secure);
      var4 = LangUtils.hashCode(var4, this.tunnelled);
      var4 = LangUtils.hashCode(var4, this.layered);
      return var4;
   }

   public final String toString() {
      StringBuilder var1 = new StringBuilder(50 + this.getHopCount() * 30);
      if (this.localAddress != null) {
         var1.append(this.localAddress);
         var1.append("->");
      }

      var1.append('{');
      if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED) {
         var1.append('t');
      }

      if (this.layered == RouteInfo.LayerType.LAYERED) {
         var1.append('l');
      }

      if (this.secure) {
         var1.append('s');
      }

      var1.append("}->");
      if (this.proxyChain != null) {
         Iterator var2 = this.proxyChain.iterator();

         while(var2.hasNext()) {
            HttpHost var3 = (HttpHost)var2.next();
            var1.append(var3);
            var1.append("->");
         }
      }

      var1.append(this.targetHost);
      return var1.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
