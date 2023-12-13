package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;

public class NGParallelCamera extends NGCamera {
   public PickRay computePickRay(float var1, float var2, PickRay var3) {
      return PickRay.computeParallelPickRay((double)var1, (double)var2, this.viewHeight, this.worldTransform, this.zNear, this.zFar, var3);
   }
}
