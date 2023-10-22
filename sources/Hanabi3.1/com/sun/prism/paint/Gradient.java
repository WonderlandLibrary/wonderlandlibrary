package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.List;

public abstract class Gradient extends Paint {
   public static final int PAD = 0;
   public static final int REFLECT = 1;
   public static final int REPEAT = 2;
   private final int numStops;
   private final List stops;
   private final BaseTransform gradientTransform;
   private final int spreadMethod;
   private long cacheOffset = -1L;

   protected Gradient(Paint.Type var1, BaseTransform var2, boolean var3, int var4, List var5) {
      super(var1, var3, false);
      if (var2 != null) {
         this.gradientTransform = var2.copy();
      } else {
         this.gradientTransform = BaseTransform.IDENTITY_TRANSFORM;
      }

      this.spreadMethod = var4;
      this.numStops = var5.size();
      this.stops = var5;
   }

   public int getSpreadMethod() {
      return this.spreadMethod;
   }

   public BaseTransform getGradientTransformNoClone() {
      return this.gradientTransform;
   }

   public int getNumStops() {
      return this.numStops;
   }

   public List getStops() {
      return this.stops;
   }

   public void setGradientOffset(long var1) {
      this.cacheOffset = var1;
   }

   public long getGradientOffset() {
      return this.cacheOffset;
   }

   public boolean isOpaque() {
      for(int var1 = 0; var1 < this.numStops; ++var1) {
         if (!((Stop)this.stops.get(var1)).getColor().isOpaque()) {
            return false;
         }
      }

      return true;
   }
}
