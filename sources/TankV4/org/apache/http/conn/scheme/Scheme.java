package org.apache.http.conn.scheme;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

/** @deprecated */
@Deprecated
@Immutable
public final class Scheme {
   private final String name;
   private final SchemeSocketFactory socketFactory;
   private final int defaultPort;
   private final boolean layered;
   private String stringRep;

   public Scheme(String var1, int var2, SchemeSocketFactory var3) {
      Args.notNull(var1, "Scheme name");
      Args.check(var2 > 0 && var2 <= 65535, "Port is invalid");
      Args.notNull(var3, "Socket factory");
      this.name = var1.toLowerCase(Locale.ENGLISH);
      this.defaultPort = var2;
      if (var3 instanceof SchemeLayeredSocketFactory) {
         this.layered = true;
         this.socketFactory = var3;
      } else if (var3 instanceof LayeredSchemeSocketFactory) {
         this.layered = true;
         this.socketFactory = new SchemeLayeredSocketFactoryAdaptor2((LayeredSchemeSocketFactory)var3);
      } else {
         this.layered = false;
         this.socketFactory = var3;
      }

   }

   /** @deprecated */
   @Deprecated
   public Scheme(String var1, SocketFactory var2, int var3) {
      Args.notNull(var1, "Scheme name");
      Args.notNull(var2, "Socket factory");
      Args.check(var3 > 0 && var3 <= 65535, "Port is invalid");
      this.name = var1.toLowerCase(Locale.ENGLISH);
      if (var2 instanceof LayeredSocketFactory) {
         this.socketFactory = new SchemeLayeredSocketFactoryAdaptor((LayeredSocketFactory)var2);
         this.layered = true;
      } else {
         this.socketFactory = new SchemeSocketFactoryAdaptor(var2);
         this.layered = false;
      }

      this.defaultPort = var3;
   }

   public final int getDefaultPort() {
      return this.defaultPort;
   }

   /** @deprecated */
   @Deprecated
   public final SocketFactory getSocketFactory() {
      if (this.socketFactory instanceof SchemeSocketFactoryAdaptor) {
         return ((SchemeSocketFactoryAdaptor)this.socketFactory).getFactory();
      } else {
         return (SocketFactory)(this.layered ? new LayeredSocketFactoryAdaptor((LayeredSchemeSocketFactory)this.socketFactory) : new SocketFactoryAdaptor(this.socketFactory));
      }
   }

   public final SchemeSocketFactory getSchemeSocketFactory() {
      return this.socketFactory;
   }

   public final String getName() {
      return this.name;
   }

   public final boolean isLayered() {
      return this.layered;
   }

   public final int resolvePort(int var1) {
      return var1 <= 0 ? this.defaultPort : var1;
   }

   public final String toString() {
      if (this.stringRep == null) {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.name);
         var1.append(':');
         var1.append(Integer.toString(this.defaultPort));
         this.stringRep = var1.toString();
      }

      return this.stringRep;
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Scheme)) {
         return false;
      } else {
         Scheme var2 = (Scheme)var1;
         return this.name.equals(var2.name) && this.defaultPort == var2.defaultPort && this.layered == var2.layered;
      }
   }

   public int hashCode() {
      byte var1 = 17;
      int var2 = LangUtils.hashCode(var1, this.defaultPort);
      var2 = LangUtils.hashCode(var2, this.name);
      var2 = LangUtils.hashCode(var2, this.layered);
      return var2;
   }
}
