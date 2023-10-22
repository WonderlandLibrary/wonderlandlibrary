package com.sun.prism.impl.shape;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;

public class ShapeUtil {
   private static final ShapeRasterizer shapeRasterizer;

   public static MaskData rasterizeShape(Shape var0, BasicStroke var1, RectBounds var2, BaseTransform var3, boolean var4, boolean var5) {
      return shapeRasterizer.getMaskData(var0, var1, var2, var3, var4, var5);
   }

   private ShapeUtil() {
   }

   static {
      if (PrismSettings.doNativePisces) {
         shapeRasterizer = new NativePiscesRasterizer();
      } else {
         shapeRasterizer = new OpenPiscesRasterizer();
      }

   }
}
