package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class Flood extends CoreEffect {
   private Object paint;
   private RectBounds bounds;

   public Flood(Object var1) {
      this.bounds = new RectBounds();
      if (var1 == null) {
         throw new IllegalArgumentException("Paint must be non-null");
      } else {
         this.paint = var1;
         this.updatePeerKey("Flood");
      }
   }

   public Flood(Object var1, RectBounds var2) {
      this(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Bounds must be non-null");
      } else {
         this.bounds.setBounds(var2);
      }
   }

   public Object getPaint() {
      return this.paint;
   }

   public void setPaint(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Paint must be non-null");
      } else {
         Object var2 = this.paint;
         this.paint = var1;
      }
   }

   public RectBounds getFloodBounds() {
      return new RectBounds(this.bounds);
   }

   public void setFloodBounds(RectBounds var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Bounds must be non-null");
      } else {
         new RectBounds(this.bounds);
         this.bounds.setBounds(var1);
      }
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      return transformBounds(var1, this.bounds);
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return new Point2D(Float.NaN, Float.NaN);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return new Point2D(Float.NaN, Float.NaN);
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.RenderSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      return true;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      DirtyRegionContainer var3 = var2.checkOut();
      var3.reset();
      return var3;
   }
}
