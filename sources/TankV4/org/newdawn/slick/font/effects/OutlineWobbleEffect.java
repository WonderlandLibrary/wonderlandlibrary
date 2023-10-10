package org.newdawn.slick.font.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.util.Iterator;
import java.util.List;

public class OutlineWobbleEffect extends OutlineEffect {
   private float detail = 1.0F;
   private float amplitude = 1.0F;

   public OutlineWobbleEffect() {
      this.setStroke(new OutlineWobbleEffect.WobbleStroke(this));
   }

   public float getDetail() {
      return this.detail;
   }

   public void setDetail(float var1) {
      this.detail = var1;
   }

   public float getAmplitude() {
      return this.amplitude;
   }

   public void setAmplitude(float var1) {
      this.amplitude = var1;
   }

   public OutlineWobbleEffect(int var1, Color var2) {
      super(var1, var2);
   }

   public String toString() {
      return "Outline (Wobble)";
   }

   public List getValues() {
      List var1 = super.getValues();
      var1.remove(2);
      var1.add(EffectUtil.floatValue("Detail", this.detail, 1.0F, 50.0F, "This setting controls how detailed the outline will be. Smaller numbers cause the outline to have more detail."));
      var1.add(EffectUtil.floatValue("Amplitude", this.amplitude, 0.5F, 50.0F, "This setting controls the amplitude of the outline."));
      return var1;
   }

   public void setValues(List var1) {
      super.setValues(var1);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ConfigurableEffect.Value var3 = (ConfigurableEffect.Value)var2.next();
         if (var3.getName().equals("Detail")) {
            this.detail = (Float)var3.getObject();
         } else if (var3.getName().equals("Amplitude")) {
            this.amplitude = (Float)var3.getObject();
         }
      }

   }

   static float access$100(OutlineWobbleEffect var0) {
      return var0.detail;
   }

   static float access$200(OutlineWobbleEffect var0) {
      return var0.amplitude;
   }

   private class WobbleStroke implements Stroke {
      private static final float FLATNESS = 1.0F;
      private final OutlineWobbleEffect this$0;

      private WobbleStroke(OutlineWobbleEffect var1) {
         this.this$0 = var1;
      }

      public Shape createStrokedShape(Shape var1) {
         GeneralPath var2 = new GeneralPath();
         var1 = (new BasicStroke(this.this$0.getWidth(), 2, this.this$0.getJoin())).createStrokedShape(var1);
         FlatteningPathIterator var3 = new FlatteningPathIterator(var1.getPathIterator((AffineTransform)null), 1.0D);
         float[] var4 = new float[6];
         float var5 = 0.0F;
         float var6 = 0.0F;
         float var7 = 0.0F;
         float var8 = 0.0F;
         float var9 = 0.0F;
         float var10 = 0.0F;
         boolean var11 = false;

         for(float var12 = 0.0F; !var3.isDone(); var3.next()) {
            int var19 = var3.currentSegment(var4);
            switch(var19) {
            case 0:
               var5 = var7 = this.randomize(var4[0]);
               var6 = var8 = this.randomize(var4[1]);
               var2.moveTo(var5, var6);
               var12 = 0.0F;
               continue;
            case 1:
               break;
            case 2:
            case 3:
            default:
               continue;
            case 4:
               var4[0] = var5;
               var4[1] = var6;
            }

            var9 = this.randomize(var4[0]);
            var10 = this.randomize(var4[1]);
            float var13 = var9 - var7;
            float var14 = var10 - var8;
            float var15 = (float)Math.sqrt((double)(var13 * var13 + var14 * var14));
            if (var15 >= var12) {
               for(float var16 = 1.0F / var15; var15 >= var12; var12 += OutlineWobbleEffect.access$100(this.this$0)) {
                  float var17 = var7 + var12 * var13 * var16;
                  float var18 = var8 + var12 * var14 * var16;
                  var2.lineTo(this.randomize(var17), this.randomize(var18));
               }
            }

            var12 -= var15;
            var7 = var9;
            var8 = var10;
         }

         return var2;
      }

      private float randomize(float var1) {
         return var1 + (float)Math.random() * OutlineWobbleEffect.access$200(this.this$0) * 2.0F - 1.0F;
      }

      WobbleStroke(OutlineWobbleEffect var1, Object var2) {
         this(var1);
      }
   }
}
