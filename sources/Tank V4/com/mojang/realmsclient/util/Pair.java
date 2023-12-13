package com.mojang.realmsclient.util;

public class Pair {
   private final Object first;
   private final Object second;

   protected Pair(Object var1, Object var2) {
      this.first = var1;
      this.second = var2;
   }

   public static Pair of(Object var0, Object var1) {
      return new Pair(var0, var1);
   }

   public Object first() {
      return this.first;
   }

   public Object second() {
      return this.second;
   }

   public String mkString(String var1) {
      return String.format("%s%s%s", this.first, var1, this.second);
   }
}
