package com.mojang.realmsclient.util;

public abstract class Option {
   public abstract Object get();

   public static Option.Some some(Object var0) {
      return new Option.Some(var0);
   }

   public static Option.None none() {
      return new Option.None();
   }

   public static final class None extends Option {
      public Object get() {
         throw new RuntimeException("None has no value");
      }
   }

   public static final class Some extends Option {
      private final Object a;

      public Some(Object var1) {
         this.a = var1;
      }

      public Object get() {
         return this.a;
      }

      public static Option of(Object var0) {
         return new Option.Some(var0);
      }
   }
}
