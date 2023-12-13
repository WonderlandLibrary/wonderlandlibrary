package com.sun.webkit.graphics;

import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGDefaultCamera;

public class WCCamera extends NGDefaultCamera {
   public static final NGCamera INSTANCE = new WCCamera();

   public void validate(int var1, int var2) {
      if ((double)var1 != this.viewWidth || (double)var2 != this.viewHeight) {
         this.setViewWidth((double)var1);
         this.setViewHeight((double)var2);
         this.projViewTx.ortho(0.0, (double)var1, (double)var2, 0.0, -9999999.0, 99999.0);
      }

   }
}
