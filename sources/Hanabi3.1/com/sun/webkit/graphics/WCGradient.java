package com.sun.webkit.graphics;

import com.sun.prism.paint.Color;

public abstract class WCGradient {
   public static final int PAD = 1;
   public static final int REFLECT = 2;
   public static final int REPEAT = 3;
   private int spreadMethod = 1;
   private boolean proportional;

   void setSpreadMethod(int var1) {
      if (var1 != 2 && var1 != 3) {
         var1 = 1;
      }

      this.spreadMethod = var1;
   }

   public int getSpreadMethod() {
      return this.spreadMethod;
   }

   void setProportional(boolean var1) {
      this.proportional = var1;
   }

   public boolean isProportional() {
      return this.proportional;
   }

   protected abstract void addStop(Color var1, float var2);

   public abstract Object getPlatformGradient();
}
