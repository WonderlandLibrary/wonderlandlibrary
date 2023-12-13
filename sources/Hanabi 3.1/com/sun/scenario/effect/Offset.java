package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;

public class Offset extends Effect {
   private int xoff;
   private int yoff;

   public Offset(int var1, int var2, Effect var3) {
      super(var3);
      this.xoff = var1;
      this.yoff = var2;
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public int getX() {
      return this.xoff;
   }

   public void setX(int var1) {
      int var2 = this.xoff;
      this.xoff = var1;
   }

   public int getY() {
      return this.yoff;
   }

   public void setY(int var1) {
      float var2 = (float)this.yoff;
      this.yoff = var1;
   }

   static BaseTransform getOffsetTransform(BaseTransform var0, double var1, double var3) {
      return var0 != null && !var0.isIdentity() ? var0.copy().deriveWithTranslation(var1, var3) : Translate2D.getInstance(var1, var3);
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      BaseTransform var3 = getOffsetTransform(var1, (double)this.xoff, (double)this.yoff);
      Effect var4 = this.getDefaultedInput(0, var2);
      return var4.getBounds(var3, var2);
   }

   public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      BaseTransform var6 = getOffsetTransform(var2, (double)this.xoff, (double)this.yoff);
      Effect var7 = this.getDefaultedInput(0, var5);
      return var7.filter(var1, var6, var3, var4, var5);
   }

   public Point2D transform(Point2D var1, Effect var2) {
      var1 = this.getDefaultedInput(0, var2).transform(var1, var2);
      float var3 = var1.x + (float)this.xoff;
      float var4 = var1.y + (float)this.yoff;
      var1 = new Point2D(var3, var4);
      return var1;
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      float var3 = var1.x - (float)this.xoff;
      float var4 = var1.y - (float)this.yoff;
      var1 = new Point2D(var3, var4);
      var1 = this.getDefaultedInput(0, var2).untransform(var1, var2);
      return var1;
   }

   public Effect.AccelType getAccelType(FilterContext var1) {
      return ((Effect)this.getInputs().get(0)).getAccelType(var1);
   }

   public boolean reducesOpaquePixels() {
      return this.getX() != 0 || this.getY() != 0 || this.getInput() != null && this.getInput().reducesOpaquePixels();
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      if (this.xoff != 0 || this.yoff != 0) {
         for(int var5 = 0; var5 < var4.size(); ++var5) {
            var4.getDirtyRegion(var5).translate((float)this.xoff, (float)this.yoff, 0.0F);
         }
      }

      return var4;
   }
}
