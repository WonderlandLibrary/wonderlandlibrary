package com.sun.javafx.sg.prism;

public class NGDefaultCamera extends NGParallelCamera {
   public void validate(int var1, int var2) {
      if ((double)var1 != this.viewWidth || (double)var2 != this.viewHeight) {
         this.setViewWidth((double)var1);
         this.setViewHeight((double)var2);
         double var3 = var1 > var2 ? (double)var1 / 2.0 : (double)var2 / 2.0;
         this.projViewTx.ortho(0.0, (double)var1, (double)var2, 0.0, -var3, var3);
      }

   }
}
