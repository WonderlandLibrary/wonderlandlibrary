package io.netty.handler.codec.http.cors;

import java.util.concurrent.Callable;

final class CorsConfig$ConstantValueGenerator implements Callable {
   private final Object value;

   private CorsConfig$ConstantValueGenerator(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("value must not be null");
      } else {
         this.value = var1;
      }
   }

   public Object call() {
      return this.value;
   }

   CorsConfig$ConstantValueGenerator(Object var1, Object var2) {
      this(var1);
   }
}
