package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Beta
@Immutable
@GwtCompatible
public final class HostAndPort implements Serializable {
   private static final int NO_PORT = -1;
   private final String host;
   private final int port;
   private final boolean hasBracketlessColons;
   private static final long serialVersionUID = 0L;

   private HostAndPort(String var1, int var2, boolean var3) {
      this.host = var1;
      this.port = var2;
      this.hasBracketlessColons = var3;
   }

   public String getHostText() {
      return this.host;
   }

   public int getPort() {
      Preconditions.checkState(this.hasPort());
      return this.port;
   }

   public int getPortOrDefault(int var1) {
      return this >= 0 ? this.port : var1;
   }

   public static HostAndPort fromParts(String var0, int var1) {
      Preconditions.checkArgument(isValidPort(var1), "Port out of range: %s", var1);
      HostAndPort var2 = fromString(var0);
      Preconditions.checkArgument(var2 >= 0, "Host has a port: %s", var0);
      return new HostAndPort(var2.host, var1, var2.hasBracketlessColons);
   }

   public static HostAndPort fromHost(String var0) {
      HostAndPort var1 = fromString(var0);
      Preconditions.checkArgument(var1 >= 0, "Host has a port: %s", var0);
      return var1;
   }

   public static HostAndPort fromString(String var0) {
      Preconditions.checkNotNull(var0);
      String var2 = null;
      boolean var3 = false;
      String var1;
      int var7;
      if (var0.startsWith("[")) {
         String[] var4 = getHostAndPortFromBracketedHost(var0);
         var1 = var4[0];
         var2 = var4[1];
      } else {
         var7 = var0.indexOf(58);
         if (var7 >= 0 && var0.indexOf(58, var7 + 1) == -1) {
            var1 = var0.substring(0, var7);
            var2 = var0.substring(var7 + 1);
         } else {
            var1 = var0;
            var3 = var7 >= 0;
         }
      }

      var7 = -1;
      if (!Strings.isNullOrEmpty(var2)) {
         Preconditions.checkArgument(!var2.startsWith("+"), "Unparseable port number: %s", var0);

         try {
            var7 = Integer.parseInt(var2);
         } catch (NumberFormatException var6) {
            throw new IllegalArgumentException("Unparseable port number: " + var0);
         }

         Preconditions.checkArgument(isValidPort(var7), "Port number out of range: %s", var0);
      }

      return new HostAndPort(var1, var7, var3);
   }

   private static String[] getHostAndPortFromBracketedHost(String var0) {
      boolean var1 = false;
      boolean var2 = false;
      boolean var3 = false;
      Preconditions.checkArgument(var0.charAt(0) == '[', "Bracketed host-port string must start with a bracket: %s", var0);
      int var6 = var0.indexOf(58);
      int var7 = var0.lastIndexOf(93);
      Preconditions.checkArgument(var6 > -1 && var7 > var6, "Invalid bracketed host/port: %s", var0);
      String var4 = var0.substring(1, var7);
      if (var7 + 1 == var0.length()) {
         return new String[]{var4, ""};
      } else {
         Preconditions.checkArgument(var0.charAt(var7 + 1) == ':', "Only a colon may follow a close bracket: %s", var0);

         for(int var5 = var7 + 2; var5 < var0.length(); ++var5) {
            Preconditions.checkArgument(Character.isDigit(var0.charAt(var5)), "Port must be numeric: %s", var0);
         }

         return new String[]{var4, var0.substring(var7 + 2)};
      }
   }

   public HostAndPort withDefaultPort(int var1) {
      Preconditions.checkArgument(isValidPort(var1));
      return this >= 0 && this.port != var1 ? new HostAndPort(this.host, var1, this.hasBracketlessColons) : this;
   }

   public HostAndPort requireBracketsForIPv6() {
      Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host);
      return this;
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof HostAndPort)) {
         return false;
      } else {
         HostAndPort var2 = (HostAndPort)var1;
         return Objects.equal(this.host, var2.host) && this.port == var2.port && this.hasBracketlessColons == var2.hasBracketlessColons;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.host, this.port, this.hasBracketlessColons);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this.host.length() + 8);
      if (this.host.indexOf(58) >= 0) {
         var1.append('[').append(this.host).append(']');
      } else {
         var1.append(this.host);
      }

      if (this >= 0) {
         var1.append(':').append(this.port);
      }

      return var1.toString();
   }

   private static boolean isValidPort(int var0) {
      return var0 >= 0 && var0 <= 65535;
   }
}
