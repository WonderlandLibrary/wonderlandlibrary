package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ColorAdjustBuilder implements Builder {
   private int __set;
   private double brightness;
   private double contrast;
   private double hue;
   private Effect input;
   private double saturation;

   protected ColorAdjustBuilder() {
   }

   public static ColorAdjustBuilder create() {
      return new ColorAdjustBuilder();
   }

   public void applyTo(ColorAdjust var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBrightness(this.brightness);
      }

      if ((var2 & 2) != 0) {
         var1.setContrast(this.contrast);
      }

      if ((var2 & 4) != 0) {
         var1.setHue(this.hue);
      }

      if ((var2 & 8) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 16) != 0) {
         var1.setSaturation(this.saturation);
      }

   }

   public ColorAdjustBuilder brightness(double var1) {
      this.brightness = var1;
      this.__set |= 1;
      return this;
   }

   public ColorAdjustBuilder contrast(double var1) {
      this.contrast = var1;
      this.__set |= 2;
      return this;
   }

   public ColorAdjustBuilder hue(double var1) {
      this.hue = var1;
      this.__set |= 4;
      return this;
   }

   public ColorAdjustBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 8;
      return this;
   }

   public ColorAdjustBuilder saturation(double var1) {
      this.saturation = var1;
      this.__set |= 16;
      return this;
   }

   public ColorAdjust build() {
      ColorAdjust var1 = new ColorAdjust();
      this.applyTo(var1);
      return var1;
   }
}
