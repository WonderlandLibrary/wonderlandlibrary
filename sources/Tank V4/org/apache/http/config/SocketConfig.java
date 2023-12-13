package org.apache.http.config;

import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class SocketConfig implements Cloneable {
   public static final SocketConfig DEFAULT = (new SocketConfig.Builder()).build();
   private final int soTimeout;
   private final boolean soReuseAddress;
   private final int soLinger;
   private final boolean soKeepAlive;
   private final boolean tcpNoDelay;

   SocketConfig(int var1, boolean var2, int var3, boolean var4, boolean var5) {
      this.soTimeout = var1;
      this.soReuseAddress = var2;
      this.soLinger = var3;
      this.soKeepAlive = var4;
      this.tcpNoDelay = var5;
   }

   public int getSoTimeout() {
      return this.soTimeout;
   }

   public boolean isSoReuseAddress() {
      return this.soReuseAddress;
   }

   public int getSoLinger() {
      return this.soLinger;
   }

   public boolean isSoKeepAlive() {
      return this.soKeepAlive;
   }

   public boolean isTcpNoDelay() {
      return this.tcpNoDelay;
   }

   protected SocketConfig clone() throws CloneNotSupportedException {
      return (SocketConfig)super.clone();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[soTimeout=").append(this.soTimeout).append(", soReuseAddress=").append(this.soReuseAddress).append(", soLinger=").append(this.soLinger).append(", soKeepAlive=").append(this.soKeepAlive).append(", tcpNoDelay=").append(this.tcpNoDelay).append("]");
      return var1.toString();
   }

   public static SocketConfig.Builder custom() {
      return new SocketConfig.Builder();
   }

   public static SocketConfig.Builder copy(SocketConfig var0) {
      Args.notNull(var0, "Socket config");
      return (new SocketConfig.Builder()).setSoTimeout(var0.getSoTimeout()).setSoReuseAddress(var0.isSoReuseAddress()).setSoLinger(var0.getSoLinger()).setSoKeepAlive(var0.isSoKeepAlive()).setTcpNoDelay(var0.isTcpNoDelay());
   }

   protected Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   public static class Builder {
      private int soTimeout;
      private boolean soReuseAddress;
      private int soLinger = -1;
      private boolean soKeepAlive;
      private boolean tcpNoDelay = true;

      Builder() {
      }

      public SocketConfig.Builder setSoTimeout(int var1) {
         this.soTimeout = var1;
         return this;
      }

      public SocketConfig.Builder setSoReuseAddress(boolean var1) {
         this.soReuseAddress = var1;
         return this;
      }

      public SocketConfig.Builder setSoLinger(int var1) {
         this.soLinger = var1;
         return this;
      }

      public SocketConfig.Builder setSoKeepAlive(boolean var1) {
         this.soKeepAlive = var1;
         return this;
      }

      public SocketConfig.Builder setTcpNoDelay(boolean var1) {
         this.tcpNoDelay = var1;
         return this;
      }

      public SocketConfig build() {
         return new SocketConfig(this.soTimeout, this.soReuseAddress, this.soLinger, this.soKeepAlive, this.tcpNoDelay);
      }
   }
}
