package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;

@Beta
@GwtCompatible
public abstract class Escaper {
   private final Function asFunction = new Function(this) {
      final Escaper this$0;

      {
         this.this$0 = var1;
      }

      public String apply(String var1) {
         return this.this$0.escape(var1);
      }

      public Object apply(Object var1) {
         return this.apply((String)var1);
      }
   };

   protected Escaper() {
   }

   public abstract String escape(String var1);

   public final Function asFunction() {
      return this.asFunction;
   }
}
