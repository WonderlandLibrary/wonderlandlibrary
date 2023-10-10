package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;

@NotThreadSafe
public final class RouteTracker implements RouteInfo, Cloneable {
   private final HttpHost targetHost;
   private final InetAddress localAddress;
   private boolean connected;
   private HttpHost[] proxyChain;
   private RouteInfo.TunnelType tunnelled;
   private RouteInfo.LayerType layered;
   private boolean secure;

   public RouteTracker(HttpHost var1, InetAddress var2) {
      Args.notNull(var1, "Target host");
      this.targetHost = var1;
      this.localAddress = var2;
      this.tunnelled = RouteInfo.TunnelType.PLAIN;
      this.layered = RouteInfo.LayerType.PLAIN;
   }

   public void reset() {
      this.connected = false;
      this.proxyChain = null;
      this.tunnelled = RouteInfo.TunnelType.PLAIN;
      this.layered = RouteInfo.LayerType.PLAIN;
      this.secure = false;
   }

   public RouteTracker(HttpRoute var1) {
      this(var1.getTargetHost(), var1.getLocalAddress());
   }

   public final void connectTarget(boolean var1) {
      Asserts.check(!this.connected, "Already connected");
      this.connected = true;
      this.secure = var1;
   }

   public final void connectProxy(HttpHost var1, boolean var2) {
      Args.notNull(var1, "Proxy host");
      Asserts.check(!this.connected, "Already connected");
      this.connected = true;
      this.proxyChain = new HttpHost[]{var1};
      this.secure = var2;
   }

   public final void tunnelTarget(boolean var1) {
      Asserts.check(this.connected, "No tunnel unless connected");
      Asserts.notNull(this.proxyChain, "No tunnel without proxy");
      this.tunnelled = RouteInfo.TunnelType.TUNNELLED;
      this.secure = var1;
   }

   public final void tunnelProxy(HttpHost var1, boolean var2) {
      Args.notNull(var1, "Proxy host");
      Asserts.check(this.connected, "No tunnel unless connected");
      Asserts.notNull(this.proxyChain, "No tunnel without proxy");
      HttpHost[] var3 = new HttpHost[this.proxyChain.length + 1];
      System.arraycopy(this.proxyChain, 0, var3, 0, this.proxyChain.length);
      var3[var3.length - 1] = var1;
      this.proxyChain = var3;
      this.secure = var2;
   }

   public final void layerProtocol(boolean var1) {
      Asserts.check(this.connected, "No layered protocol unless connected");
      this.layered = RouteInfo.LayerType.LAYERED;
      this.secure = var1;
   }

   public final HttpHost getTargetHost() {
      return this.targetHost;
   }

   public final InetAddress getLocalAddress() {
      return this.localAddress;
   }

   public final int getHopCount() {
      int var1 = 0;
      if (this.connected) {
         if (this.proxyChain == null) {
            var1 = 1;
         } else {
            var1 = this.proxyChain.length + 1;
         }
      }

      return var1;
   }

   public final HttpHost getHopTarget(int var1) {
      Args.notNegative(var1, "Hop index");
      int var2 = this.getHopCount();
      Args.check(var1 < var2, "Hop index exceeds tracked route length");
      HttpHost var3 = null;
      if (var1 < var2 - 1) {
         var3 = this.proxyChain[var1];
      } else {
         var3 = this.targetHost;
      }

      return var3;
   }

   public final HttpHost getProxyHost() {
      return this.proxyChain == null ? null : this.proxyChain[0];
   }

   public final boolean isConnected() {
      return this.connected;
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

   public final HttpRoute toRoute() {
      return !this.connected ? null : new HttpRoute(this.targetHost, this.localAddress, this.proxyChain, this.secure, this.tunnelled, this.layered);
   }

   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof RouteTracker)) {
         return false;
      } else {
         RouteTracker var2 = (RouteTracker)var1;
         return this.connected == var2.connected && this.secure == var2.secure && this.tunnelled == var2.tunnelled && this.layered == var2.layered && LangUtils.equals(this.targetHost, var2.targetHost) && LangUtils.equals(this.localAddress, var2.localAddress) && LangUtils.equals(this.proxyChain, var2.proxyChain);
      }
   }

   public final int hashCode() {
      byte var1 = 17;
      int var6 = LangUtils.hashCode(var1, this.targetHost);
      var6 = LangUtils.hashCode(var6, this.localAddress);
      if (this.proxyChain != null) {
         HttpHost[] var2 = this.proxyChain;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            HttpHost var5 = var2[var4];
            var6 = LangUtils.hashCode(var6, var5);
         }
      }

      var6 = LangUtils.hashCode(var6, this.connected);
      var6 = LangUtils.hashCode(var6, this.secure);
      var6 = LangUtils.hashCode(var6, this.tunnelled);
      var6 = LangUtils.hashCode(var6, this.layered);
      return var6;
   }

   public final String toString() {
      StringBuilder var1 = new StringBuilder(50 + this.getHopCount() * 30);
      var1.append("RouteTracker[");
      if (this.localAddress != null) {
         var1.append(this.localAddress);
         var1.append("->");
      }

      var1.append('{');
      if (this.connected) {
         var1.append('c');
      }

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
         HttpHost[] var2 = this.proxyChain;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            HttpHost var5 = var2[var4];
            var1.append(var5);
            var1.append("->");
         }
      }

      var1.append(this.targetHost);
      var1.append(']');
      return var1.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
