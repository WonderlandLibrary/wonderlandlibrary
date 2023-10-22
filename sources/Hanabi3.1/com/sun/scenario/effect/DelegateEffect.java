package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

public abstract class DelegateEffect extends Effect {
   protected DelegateEffect(Effect var1) {
      super(var1);
   }

   protected DelegateEffect(Effect var1, Effect var2) {
      super(var1, var2);
   }

   protected abstract Effect getDelegate();

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      return this.getDelegate().getBounds(var1, var2);
   }

   public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return this.getDelegate().filter(var1, var2, var3, var4, var5);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getDelegate().untransform(var1, var2);
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getDelegate().transform(var1, var2);
   }

   public Effect.AccelType getAccelType(FilterContext var1) {
      return this.getDelegate().getAccelType(var1);
   }

   public boolean reducesOpaquePixels() {
      return this.getDelegate().reducesOpaquePixels();
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      return this.getDelegate().getDirtyRegions(var1, var2);
   }
}
