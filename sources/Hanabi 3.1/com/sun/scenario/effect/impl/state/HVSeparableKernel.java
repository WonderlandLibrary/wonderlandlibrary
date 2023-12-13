package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;

public abstract class HVSeparableKernel extends LinearConvolveKernel {
   public final Rectangle getResultBounds(Rectangle var1, int var2) {
      int var3 = this.getKernelSize(var2);
      Rectangle var4 = new Rectangle(var1);
      if (var2 == 0) {
         var4.grow(var3 / 2, 0);
      } else {
         var4.grow(0, var3 / 2);
      }

      return var4;
   }
}
