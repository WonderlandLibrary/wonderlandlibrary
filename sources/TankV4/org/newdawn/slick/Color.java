package org.newdawn.slick;

import java.io.Serializable;
import java.nio.FloatBuffer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class Color implements Serializable {
   private static final long serialVersionUID = 1393939L;
   protected static SGL GL = Renderer.get();
   public static final Color white = new Color(1.0F, 1.0F, 1.0F, 1.0F);
   public static final Color yellow = new Color(1.0F, 1.0F, 0.0F, 1.0F);
   public static final Color red = new Color(1.0F, 0.0F, 0.0F, 1.0F);
   public static final Color blue = new Color(0.0F, 0.0F, 1.0F, 1.0F);
   public static final Color green = new Color(0.0F, 1.0F, 0.0F, 1.0F);
   public static final Color black = new Color(0.0F, 0.0F, 0.0F, 1.0F);
   public static final Color gray = new Color(0.5F, 0.5F, 0.5F, 1.0F);
   public static final Color cyan = new Color(0.0F, 1.0F, 1.0F, 1.0F);
   public static final Color darkGray = new Color(0.3F, 0.3F, 0.3F, 1.0F);
   public static final Color lightGray = new Color(0.7F, 0.7F, 0.7F, 1.0F);
   public static final Color pink = new Color(255, 175, 175, 255);
   public static final Color orange = new Color(255, 200, 0, 255);
   public static final Color magenta = new Color(255, 0, 255, 255);
   public float r;
   public float g;
   public float b;
   public float a = 1.0F;

   public Color(Color var1) {
      this.r = var1.r;
      this.g = var1.g;
      this.b = var1.b;
      this.a = var1.a;
   }

   public Color(FloatBuffer var1) {
      this.r = var1.get();
      this.g = var1.get();
      this.b = var1.get();
      this.a = var1.get();
   }

   public Color(float var1, float var2, float var3) {
      this.r = var1;
      this.g = var2;
      this.b = var3;
      this.a = 1.0F;
   }

   public Color(float var1, float var2, float var3, float var4) {
      this.r = Math.min(var1, 1.0F);
      this.g = Math.min(var2, 1.0F);
      this.b = Math.min(var3, 1.0F);
      this.a = Math.min(var4, 1.0F);
   }

   public Color(int var1, int var2, int var3) {
      this.r = (float)var1 / 255.0F;
      this.g = (float)var2 / 255.0F;
      this.b = (float)var3 / 255.0F;
      this.a = 1.0F;
   }

   public Color(int var1, int var2, int var3, int var4) {
      this.r = (float)var1 / 255.0F;
      this.g = (float)var2 / 255.0F;
      this.b = (float)var3 / 255.0F;
      this.a = (float)var4 / 255.0F;
   }

   public Color(int var1) {
      int var2 = (var1 & 16711680) >> 16;
      int var3 = (var1 & '\uff00') >> 8;
      int var4 = var1 & 255;
      int var5 = (var1 & -16777216) >> 24;
      if (var5 < 0) {
         var5 += 256;
      }

      if (var5 == 0) {
         var5 = 255;
      }

      this.r = (float)var2 / 255.0F;
      this.g = (float)var3 / 255.0F;
      this.b = (float)var4 / 255.0F;
      this.a = (float)var5 / 255.0F;
   }

   public static Color decode(String var0) {
      return new Color(Integer.decode(var0));
   }

   public void bind() {
      GL.glColor4f(this.r, this.g, this.b, this.a);
   }

   public int hashCode() {
      return (int)(this.r + this.g + this.b + this.a) * 255;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Color)) {
         return false;
      } else {
         Color var2 = (Color)var1;
         return var2.r == this.r && var2.g == this.g && var2.b == this.b && var2.a == this.a;
      }
   }

   public String toString() {
      return "Color (" + this.r + "," + this.g + "," + this.b + "," + this.a + ")";
   }

   public Color darker() {
      return this.darker(0.5F);
   }

   public Color darker(float var1) {
      var1 = 1.0F - var1;
      Color var2 = new Color(this.r * var1, this.g * var1, this.b * var1, this.a);
      return var2;
   }

   public Color brighter() {
      return this.brighter(0.2F);
   }

   public int getRed() {
      return (int)(this.r * 255.0F);
   }

   public int getGreen() {
      return (int)(this.g * 255.0F);
   }

   public int getBlue() {
      return (int)(this.b * 255.0F);
   }

   public int getAlpha() {
      return (int)(this.a * 255.0F);
   }

   public int getRedByte() {
      return (int)(this.r * 255.0F);
   }

   public int getGreenByte() {
      return (int)(this.g * 255.0F);
   }

   public int getBlueByte() {
      return (int)(this.b * 255.0F);
   }

   public int getAlphaByte() {
      return (int)(this.a * 255.0F);
   }

   public Color brighter(float var1) {
      ++var1;
      Color var2 = new Color(this.r * var1, this.g * var1, this.b * var1, this.a);
      return var2;
   }

   public Color multiply(Color var1) {
      return new Color(this.r * var1.r, this.g * var1.g, this.b * var1.b, this.a * var1.a);
   }

   public void add(Color var1) {
      this.r += var1.r;
      this.g += var1.g;
      this.b += var1.b;
      this.a += var1.a;
   }

   public void scale(float var1) {
      this.r *= var1;
      this.g *= var1;
      this.b *= var1;
      this.a *= var1;
   }

   public Color addToCopy(Color var1) {
      Color var2 = new Color(this.r, this.g, this.b, this.a);
      var2.r += var1.r;
      var2.g += var1.g;
      var2.b += var1.b;
      var2.a += var1.a;
      return var2;
   }

   public Color scaleCopy(float var1) {
      Color var2 = new Color(this.r, this.g, this.b, this.a);
      var2.r *= var1;
      var2.g *= var1;
      var2.b *= var1;
      var2.a *= var1;
      return var2;
   }
}
