package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import java.util.Collection;
import java.util.Iterator;
import javafx.scene.shape.PathElement;

public final class PathUtils {
   private PathUtils() {
   }

   public static Path2D configShape(Collection var0, boolean var1) {
      Path2D var2 = new Path2D(var1 ? 0 : 1, var0.size());
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         PathElement var4 = (PathElement)var3.next();
         var4.impl_addTo(var2);
      }

      return var2;
   }
}
