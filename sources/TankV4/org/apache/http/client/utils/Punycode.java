package org.apache.http.client.utils;

import org.apache.http.annotation.Immutable;

@Immutable
public class Punycode {
   private static final Idn impl;

   public static String toUnicode(String var0) {
      return impl.toUnicode(var0);
   }

   static {
      Object var0;
      try {
         var0 = new JdkIdn();
      } catch (Exception var2) {
         var0 = new Rfc3492Idn();
      }

      impl = (Idn)var0;
   }
}
