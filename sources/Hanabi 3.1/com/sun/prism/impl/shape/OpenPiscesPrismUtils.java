package com.sun.prism.impl.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.Dasher;
import com.sun.openpisces.Renderer;
import com.sun.openpisces.Stroker;
import com.sun.openpisces.TransformingPathConsumer2D;
import com.sun.prism.BasicStroke;

public class OpenPiscesPrismUtils {
   private static final Renderer savedAARenderer = new Renderer(3, 3);
   private static final Renderer savedRenderer = new Renderer(0, 0);
   private static final Stroker savedStroker;
   private static final Dasher savedDasher;
   private static TransformingPathConsumer2D.FilterSet transformer;

   private static PathConsumer2D initRenderer(BasicStroke var0, BaseTransform var1, Rectangle var2, int var3, Renderer var4) {
      int var5 = var0 == null && var3 == 0 ? 0 : 1;
      var4.reset(var2.x, var2.y, var2.width, var2.height, var5);
      Object var6 = transformer.getConsumer(var4, var1);
      if (var0 != null) {
         savedStroker.reset(var0.getLineWidth(), var0.getEndCap(), var0.getLineJoin(), var0.getMiterLimit());
         savedStroker.setConsumer((PathConsumer2D)var6);
         var6 = savedStroker;
         float[] var7 = var0.getDashArray();
         if (var7 != null) {
            savedDasher.reset(var7, var0.getDashPhase());
            var6 = savedDasher;
         }
      }

      return (PathConsumer2D)var6;
   }

   public static void feedConsumer(PathIterator var0, PathConsumer2D var1) {
      for(float[] var2 = new float[6]; !var0.isDone(); var0.next()) {
         int var3 = var0.currentSegment(var2);
         switch (var3) {
            case 0:
               var1.moveTo(var2[0], var2[1]);
               break;
            case 1:
               var1.lineTo(var2[0], var2[1]);
               break;
            case 2:
               var1.quadTo(var2[0], var2[1], var2[2], var2[3]);
               break;
            case 3:
               var1.curveTo(var2[0], var2[1], var2[2], var2[3], var2[4], var2[5]);
               break;
            case 4:
               var1.closePath();
         }
      }

      var1.pathDone();
   }

   public static Renderer setupRenderer(Shape var0, BasicStroke var1, BaseTransform var2, Rectangle var3, boolean var4) {
      PathIterator var5 = var0.getPathIterator((BaseTransform)null);
      Renderer var6 = var4 ? savedAARenderer : savedRenderer;
      feedConsumer(var5, initRenderer(var1, var2, var3, var5.getWindingRule(), var6));
      return var6;
   }

   public static Renderer setupRenderer(Path2D var0, BasicStroke var1, BaseTransform var2, Rectangle var3, boolean var4) {
      Renderer var5 = var4 ? savedAARenderer : savedRenderer;
      PathConsumer2D var6 = initRenderer(var1, var2, var3, var0.getWindingRule(), var5);
      float[] var7 = var0.getFloatCoordsNoClone();
      byte[] var8 = var0.getCommandsNoClone();
      int var9 = var0.getNumCommands();
      int var10 = 0;

      for(int var11 = 0; var11 < var9; ++var11) {
         switch (var8[var11]) {
            case 0:
               var6.moveTo(var7[var10 + 0], var7[var10 + 1]);
               var10 += 2;
               break;
            case 1:
               var6.lineTo(var7[var10 + 0], var7[var10 + 1]);
               var10 += 2;
               break;
            case 2:
               var6.quadTo(var7[var10 + 0], var7[var10 + 1], var7[var10 + 2], var7[var10 + 3]);
               var10 += 4;
               break;
            case 3:
               var6.curveTo(var7[var10 + 0], var7[var10 + 1], var7[var10 + 2], var7[var10 + 3], var7[var10 + 4], var7[var10 + 5]);
               var10 += 6;
               break;
            case 4:
               var6.closePath();
         }
      }

      var6.pathDone();
      return var5;
   }

   static {
      savedStroker = new Stroker(savedRenderer);
      savedDasher = new Dasher(savedStroker);
      transformer = new TransformingPathConsumer2D.FilterSet();
   }
}
