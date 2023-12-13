package org.newdawn.slick.font.effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.Glyph;

public class ShadowEffect implements ConfigurableEffect {
   public static final int NUM_KERNELS = 16;
   public static final float[][] GAUSSIAN_BLUR_KERNELS = generateGaussianBlurKernels(16);
   private Color color;
   private float opacity;
   private float xDistance;
   private float yDistance;
   private int blurKernelSize;
   private int blurPasses;

   public ShadowEffect() {
      this.color = Color.black;
      this.opacity = 0.6F;
      this.xDistance = 2.0F;
      this.yDistance = 2.0F;
      this.blurKernelSize = 0;
      this.blurPasses = 1;
   }

   public ShadowEffect(Color var1, int var2, int var3, float var4) {
      this.color = Color.black;
      this.opacity = 0.6F;
      this.xDistance = 2.0F;
      this.yDistance = 2.0F;
      this.blurKernelSize = 0;
      this.blurPasses = 1;
      this.color = var1;
      this.xDistance = (float)var2;
      this.yDistance = (float)var3;
      this.opacity = var4;
   }

   public void draw(BufferedImage var1, Graphics2D var2, UnicodeFont var3, Glyph var4) {
      var2 = (Graphics2D)var2.create();
      var2.translate((double)this.xDistance, (double)this.yDistance);
      var2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.round(this.opacity * 255.0F)));
      var2.fill(var4.getShape());
      Iterator var5 = var3.getEffects().iterator();

      while(var5.hasNext()) {
         Effect var6 = (Effect)var5.next();
         if (var6 instanceof OutlineEffect) {
            Composite var7 = var2.getComposite();
            var2.setComposite(AlphaComposite.Src);
            var2.setStroke(((OutlineEffect)var6).getStroke());
            var2.draw(var4.getShape());
            var2.setComposite(var7);
            break;
         }
      }

      var2.dispose();
      if (this.blurKernelSize > 1 && this.blurKernelSize < 16 && this.blurPasses > 0) {
         this.blur(var1);
      }

   }

   private void blur(BufferedImage var1) {
      float[] var2 = GAUSSIAN_BLUR_KERNELS[this.blurKernelSize - 1];
      Kernel var3 = new Kernel(var2.length, 1, var2);
      Kernel var4 = new Kernel(1, var2.length, var2);
      RenderingHints var5 = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
      ConvolveOp var6 = new ConvolveOp(var3, 1, var5);
      ConvolveOp var7 = new ConvolveOp(var4, 1, var5);
      BufferedImage var8 = EffectUtil.getScratchImage();

      for(int var9 = 0; var9 < this.blurPasses; ++var9) {
         var6.filter(var1, var8);
         var7.filter(var8, var1);
      }

   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color var1) {
      this.color = var1;
   }

   public float getXDistance() {
      return this.xDistance;
   }

   public void setXDistance(float var1) {
      this.xDistance = var1;
   }

   public float getYDistance() {
      return this.yDistance;
   }

   public void setYDistance(float var1) {
      this.yDistance = var1;
   }

   public int getBlurKernelSize() {
      return this.blurKernelSize;
   }

   public void setBlurKernelSize(int var1) {
      this.blurKernelSize = var1;
   }

   public int getBlurPasses() {
      return this.blurPasses;
   }

   public void setBlurPasses(int var1) {
      this.blurPasses = var1;
   }

   public float getOpacity() {
      return this.opacity;
   }

   public void setOpacity(float var1) {
      this.opacity = var1;
   }

   public String toString() {
      return "Shadow";
   }

   public List getValues() {
      ArrayList var1 = new ArrayList();
      var1.add(EffectUtil.colorValue("Color", this.color));
      var1.add(EffectUtil.floatValue("Opacity", this.opacity, 0.0F, 1.0F, "This setting sets the translucency of the shadow."));
      var1.add(EffectUtil.floatValue("X distance", this.xDistance, Float.MIN_VALUE, Float.MAX_VALUE, "This setting is the amount of pixels to offset the shadow on the x axis. The glyphs will need padding so the shadow doesn't get clipped."));
      var1.add(EffectUtil.floatValue("Y distance", this.yDistance, Float.MIN_VALUE, Float.MAX_VALUE, "This setting is the amount of pixels to offset the shadow on the y axis. The glyphs will need padding so the shadow doesn't get clipped."));
      ArrayList var2 = new ArrayList();
      var2.add(new String[]{"None", "0"});

      for(int var3 = 2; var3 < 16; ++var3) {
         var2.add(new String[]{String.valueOf(var3)});
      }

      String[][] var4 = (String[][])((String[][])var2.toArray(new String[var2.size()][]));
      var1.add(EffectUtil.optionValue("Blur kernel size", String.valueOf(this.blurKernelSize), var4, "This setting controls how many neighboring pixels are used to blur the shadow. Set to \"None\" for no blur."));
      var1.add(EffectUtil.intValue("Blur passes", this.blurPasses, "The setting is the number of times to apply a blur to the shadow. Set to \"0\" for no blur."));
      return var1;
   }

   public void setValues(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ConfigurableEffect.Value var3 = (ConfigurableEffect.Value)var2.next();
         if (var3.getName().equals("Color")) {
            this.color = (Color)var3.getObject();
         } else if (var3.getName().equals("Opacity")) {
            this.opacity = (Float)var3.getObject();
         } else if (var3.getName().equals("X distance")) {
            this.xDistance = (Float)var3.getObject();
         } else if (var3.getName().equals("Y distance")) {
            this.yDistance = (Float)var3.getObject();
         } else if (var3.getName().equals("Blur kernel size")) {
            this.blurKernelSize = Integer.parseInt((String)var3.getObject());
         } else if (var3.getName().equals("Blur passes")) {
            this.blurPasses = (Integer)var3.getObject();
         }
      }

   }

   private static float[][] generateGaussianBlurKernels(int var0) {
      float[][] var1 = generatePascalsTriangle(var0);
      float[][] var2 = new float[var1.length][];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         float var4 = 0.0F;
         var2[var3] = new float[var1[var3].length];

         for(int var5 = 0; var5 < var1[var3].length; ++var5) {
            var4 += var1[var3][var5];
         }

         float var7 = 1.0F / var4;

         for(int var6 = 0; var6 < var1[var3].length; ++var6) {
            var2[var3][var6] = var7 * var1[var3][var6];
         }
      }

      return var2;
   }

   private static float[][] generatePascalsTriangle(int var0) {
      if (var0 < 2) {
         var0 = 2;
      }

      float[][] var1 = new float[var0][];
      var1[0] = new float[1];
      var1[1] = new float[2];
      var1[0][0] = 1.0F;
      var1[1][0] = 1.0F;
      var1[1][1] = 1.0F;

      for(int var2 = 2; var2 < var0; ++var2) {
         var1[var2] = new float[var2 + 1];
         var1[var2][0] = 1.0F;
         var1[var2][var2] = 1.0F;

         for(int var3 = 1; var3 < var1[var2].length - 1; ++var3) {
            var1[var2][var3] = var1[var2 - 1][var3 - 1] + var1[var2 - 1][var3];
         }
      }

      return var1;
   }
}
