package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.net.InetAddress;
import java.text.ParseException;
import javax.annotation.Nullable;

@Beta
public final class HostSpecifier {
   private final String canonicalForm;

   private HostSpecifier(String var1) {
      this.canonicalForm = var1;
   }

   public static HostSpecifier fromValid(String var0) {
      HostAndPort var1 = HostAndPort.fromString(var0);
      Preconditions.checkArgument(!var1.hasPort());
      String var2 = var1.getHostText();
      InetAddress var3 = null;

      try {
         var3 = InetAddresses.forString(var2);
      } catch (IllegalArgumentException var5) {
      }

      if (var3 != null) {
         return new HostSpecifier(InetAddresses.toUriString(var3));
      } else {
         InternetDomainName var4 = InternetDomainName.from(var2);
         if (var4.hasPublicSuffix()) {
            return new HostSpecifier(var4.toString());
         } else {
            throw new IllegalArgumentException("Domain name does not have a recognized public suffix: " + var2);
         }
      }
   }

   public static HostSpecifier from(String var0) throws ParseException {
      try {
         return fromValid(var0);
      } catch (IllegalArgumentException var3) {
         ParseException var2 = new ParseException("Invalid host specifier: " + var0, 0);
         var2.initCause(var3);
         throw var2;
      }
   }

   public static boolean isValid(String var0) {
      try {
         fromValid(var0);
         return true;
      } catch (IllegalArgumentException var2) {
         return false;
      }
   }

   public boolean equals(@Nullable Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof HostSpecifier) {
         HostSpecifier var2 = (HostSpecifier)var1;
         return this.canonicalForm.equals(var2.canonicalForm);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.canonicalForm.hashCode();
   }

   public String toString() {
      return this.canonicalForm;
   }
}
