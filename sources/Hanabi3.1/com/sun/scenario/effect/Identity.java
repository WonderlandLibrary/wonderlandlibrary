package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;
import java.util.Map;

public class Identity extends Effect {
   private Filterable src;
   private Point2D loc = new Point2D();
   private final Map datacache = new HashMap();

   public Identity(Filterable var1) {
      this.src = var1;
   }

   public final Filterable getSource() {
      return this.src;
   }

   public void setSource(Filterable var1) {
      Filterable var2 = this.src;
      this.src = var1;
      this.clearCache();
   }

   public final Point2D getLocation() {
      return this.loc;
   }

   public void setLocation(Point2D var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Location must be non-null");
      } else {
         Point2D var2 = this.loc;
         this.loc.setLocation(var1);
      }
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      if (this.src == null) {
         return new RectBounds();
      } else {
         float var3 = (float)this.src.getPhysicalWidth() / this.src.getPixelScale();
         float var4 = (float)this.src.getPhysicalHeight() / this.src.getPixelScale();
         Object var5 = new RectBounds(this.loc.x, this.loc.y, this.loc.x + var3, this.loc.y + var4);
         if (var1 != null && !var1.isIdentity()) {
            var5 = transformBounds(var1, (BaseBounds)var5);
         }

         return (BaseBounds)var5;
      }
   }

   public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      ImageData var6 = (ImageData)this.datacache.get(var1);
      if (var6 != null && !var6.addref()) {
         var6.setReusable(false);
         this.datacache.remove(var1);
         var6.unref();
         var6 = null;
      }

      if (var6 == null) {
         Renderer var7 = Renderer.getRenderer(var1);
         Filterable var8 = this.src;
         if (var8 == null) {
            var8 = getCompatibleImage(var1, 1, 1);
            var6 = new ImageData(var1, var8, new Rectangle(1, 1));
         } else {
            var6 = var7.createImageData(var1, var8);
         }

         if (var6 == null) {
            return new ImageData(var1, (Filterable)null, (Rectangle)null);
         }

         var6.setReusable(true);
         this.datacache.put(var1, var6);
      }

      var2 = Offset.getOffsetTransform(var2, (double)this.loc.x, (double)this.loc.y);
      var6 = var6.transform(var2);
      return var6;
   }

   public Effect.AccelType getAccelType(FilterContext var1) {
      return Effect.AccelType.INTRINSIC;
   }

   private void clearCache() {
      this.datacache.clear();
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
