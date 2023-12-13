package com.ibm.icu.impl;

public class Assert {
   public static void fail(Exception var0) {
      fail(var0.toString());
   }

   public static void fail(String var0) {
      throw new IllegalStateException("failure '" + var0 + "'");
   }

   public static void assrt(boolean var0) {
      if (!var0) {
         throw new IllegalStateException("assert failed");
      }
   }

   public static void assrt(String var0, boolean var1) {
      if (!var1) {
         throw new IllegalStateException("assert '" + var0 + "' failed");
      }
   }
}
