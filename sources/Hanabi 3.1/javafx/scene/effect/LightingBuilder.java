package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class LightingBuilder implements Builder {
   private int __set;
   private Effect bumpInput;
   private Effect contentInput;
   private double diffuseConstant;
   private Light light;
   private double specularConstant;
   private double specularExponent;
   private double surfaceScale;

   protected LightingBuilder() {
   }

   public static LightingBuilder create() {
      return new LightingBuilder();
   }

   public void applyTo(Lighting var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBumpInput(this.bumpInput);
      }

      if ((var2 & 2) != 0) {
         var1.setContentInput(this.contentInput);
      }

      if ((var2 & 4) != 0) {
         var1.setDiffuseConstant(this.diffuseConstant);
      }

      if ((var2 & 8) != 0) {
         var1.setLight(this.light);
      }

      if ((var2 & 16) != 0) {
         var1.setSpecularConstant(this.specularConstant);
      }

      if ((var2 & 32) != 0) {
         var1.setSpecularExponent(this.specularExponent);
      }

      if ((var2 & 64) != 0) {
         var1.setSurfaceScale(this.surfaceScale);
      }

   }

   public LightingBuilder bumpInput(Effect var1) {
      this.bumpInput = var1;
      this.__set |= 1;
      return this;
   }

   public LightingBuilder contentInput(Effect var1) {
      this.contentInput = var1;
      this.__set |= 2;
      return this;
   }

   public LightingBuilder diffuseConstant(double var1) {
      this.diffuseConstant = var1;
      this.__set |= 4;
      return this;
   }

   public LightingBuilder light(Light var1) {
      this.light = var1;
      this.__set |= 8;
      return this;
   }

   public LightingBuilder specularConstant(double var1) {
      this.specularConstant = var1;
      this.__set |= 16;
      return this;
   }

   public LightingBuilder specularExponent(double var1) {
      this.specularExponent = var1;
      this.__set |= 32;
      return this;
   }

   public LightingBuilder surfaceScale(double var1) {
      this.surfaceScale = var1;
      this.__set |= 64;
      return this;
   }

   public Lighting build() {
      Lighting var1 = new Lighting();
      this.applyTo(var1);
      return var1;
   }
}
