package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.List;

public final class LinearGradient extends Gradient {
   private float x1;
   private float y1;
   private float x2;
   private float y2;

   public LinearGradient(float var1, float var2, float var3, float var4, BaseTransform var5, boolean var6, int var7, List var8) {
      super(Paint.Type.LINEAR_GRADIENT, var5, var6, var7, var8);
      this.x1 = var1;
      this.y1 = var2;
      this.x2 = var3;
      this.y2 = var4;
   }

   public float getX1() {
      return this.x1;
   }

   public float getY1() {
      return this.y1;
   }

   public float getX2() {
      return this.x2;
   }

   public float getY2() {
      return this.y2;
   }
}
