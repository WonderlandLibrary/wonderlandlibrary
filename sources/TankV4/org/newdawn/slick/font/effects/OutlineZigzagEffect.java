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

public class OutlineZigzagEffect extends OutlineEffect {
   private float amplitude = 1.0F;
   private float wavelength = 3.0F;

   public OutlineZigzagEffect() {
      this.setStroke(new OutlineZigzagEffect.ZigzagStroke(this));
   }

   public float getWavelength() {
      return this.wavelength;
   }

   public void setWavelength(float var1) {
      this.wavelength = var1;
   }

   public float getAmplitude() {
      return this.amplitude;
   }

   public void setAmplitude(float var1) {
      this.amplitude = var1;
   }

   public OutlineZigzagEffect(int var1, Color var2) {
      super(var1, var2);
   }

   public String toString() {
      return "Outline (Zigzag)";
   }

   public List getValues() {
      List var1 = super.getValues();
      var1.add(EffectUtil.floatValue("Wavelength", this.wavelength, 1.0F, 100.0F, "This setting controls the wavelength of the outline. The smaller the value, the more segments will be used to draw the outline."));
      var1.add(EffectUtil.floatValue("Amplitude", this.amplitude, 0.5F, 50.0F, "This setting controls the amplitude of the outline. The bigger the value, the more the zigzags will vary."));
      return var1;
   }

   public void setValues(List var1) {
      super.setValues(var1);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ConfigurableEffect.Value var3 = (ConfigurableEffect.Value)var2.next();
         if (var3.getName().equals("Wavelength")) {
            this.wavelength = (Float)var3.getObject();
         } else if (var3.getName().equals("Amplitude")) {
            this.amplitude = (Float)var3.getObject();
         }
      }

   }

   static float access$100(OutlineZigzagEffect var0) {
      return var0.wavelength;
   }

   static float access$200(OutlineZigzagEffect var0) {
      return var0.amplitude;
   }

   private class ZigzagStroke implements Stroke {
      private static final float FLATNESS = 1.0F;
      private final OutlineZigzagEffect this$0;

      private ZigzagStroke(OutlineZigzagEffect var1) {
         this.this$0 = var1;
      }

      public Shape createStrokedShape(Shape var1) {
         GeneralPath var2 = new GeneralPath();
         FlatteningPathIterator var3 = new FlatteningPathIterator(var1.getPathIterator((AffineTransform)null), 1.0D);
         float[] var4 = new float[6];
         float var5 = 0.0F;
         float var6 = 0.0F;
         float var7 = 0.0F;
         float var8 = 0.0F;
         float var9 = 0.0F;
         float var10 = 0.0F;
         boolean var11 = false;
         float var12 = 0.0F;

         for(int var13 = 0; !var3.isDone(); var3.next()) {
            int var20 = var3.currentSegment(var4);
            switch(var20) {
            case 0:
               var5 = var7 = var4[0];
               var6 = var8 = var4[1];
               var2.moveTo(var5, var6);
               var12 = OutlineZigzagEffect.access$100(this.this$0) / 2.0F;
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

            var9 = var4[0];
            var10 = var4[1];
            float var14 = var9 - var7;
            float var15 = var10 - var8;
            float var16 = (float)Math.sqrt((double)(var14 * var14 + var15 * var15));
            if (var16 >= var12) {
               for(float var17 = 1.0F / var16; var16 >= var12; ++var13) {
                  float var18 = var7 + var12 * var14 * var17;
                  float var19 = var8 + var12 * var15 * var17;
                  if ((var13 & 1) == 0) {
                     var2.lineTo(var18 + OutlineZigzagEffect.access$200(this.this$0) * var15 * var17, var19 - OutlineZigzagEffect.access$200(this.this$0) * var14 * var17);
                  } else {
                     var2.lineTo(var18 - OutlineZigzagEffect.access$200(this.this$0) * var15 * var17, var19 + OutlineZigzagEffect.access$200(this.this$0) * var14 * var17);
                  }

                  var12 += OutlineZigzagEffect.access$100(this.this$0);
               }
            }

            var12 -= var16;
            var7 = var9;
            var8 = var10;
            if (var20 == 4) {
               var2.closePath();
            }
         }

         return (new BasicStroke(this.this$0.getWidth(), 2, this.this$0.getJoin())).createStrokedShape(var2);
      }

      ZigzagStroke(OutlineZigzagEffect var1, Object var2) {
         this(var1);
      }
   }
}
