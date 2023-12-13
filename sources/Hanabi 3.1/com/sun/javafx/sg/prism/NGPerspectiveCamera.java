package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;

public class NGPerspectiveCamera extends NGCamera {
   private final boolean fixedEyeAtCameraZero;
   private double fovrad;
   private boolean verticalFieldOfView;

   public NGPerspectiveCamera(boolean var1) {
      this.fixedEyeAtCameraZero = var1;
   }

   public void setFieldOfView(float var1) {
      this.fovrad = Math.toRadians((double)var1);
   }

   public void setVerticalFieldOfView(boolean var1) {
      this.verticalFieldOfView = var1;
   }

   public PickRay computePickRay(float var1, float var2, PickRay var3) {
      return PickRay.computePerspectivePickRay((double)var1, (double)var2, this.fixedEyeAtCameraZero, this.viewWidth, this.viewHeight, this.fovrad, this.verticalFieldOfView, this.worldTransform, this.zNear, this.zFar, var3);
   }
}
