package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.List;

public final class RadialGradient extends Gradient {
   private final float centerX;
   private final float centerY;
   private final float focusAngle;
   private final float focusDistance;
   private final float radius;

   public RadialGradient(float var1, float var2, float var3, float var4, float var5, BaseTransform var6, boolean var7, int var8, List var9) {
      super(Paint.Type.RADIAL_GRADIENT, var6, var7, var8, var9);
      this.centerX = var1;
      this.centerY = var2;
      this.focusAngle = var3;
      this.focusDistance = var4;
      this.radius = var5;
   }

   public float getCenterX() {
      return this.centerX;
   }

   public float getCenterY() {
      return this.centerY;
   }

   public float getFocusAngle() {
      return this.focusAngle;
   }

   public float getFocusDistance() {
      return this.focusDistance;
   }

   public float getRadius() {
      return this.radius;
   }

   public String toString() {
      return "RadialGradient: FocusAngle: " + this.focusAngle + " FocusDistance: " + this.focusDistance + " CenterX: " + this.centerX + " CenterY " + this.centerY + " Radius: " + this.radius + "stops:" + this.getStops();
   }
}
