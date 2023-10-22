package javafx.scene.effect;

import javafx.scene.paint.Color;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ShadowBuilder implements Builder {
   private int __set;
   private BlurType blurType;
   private Color color;
   private double height;
   private Effect input;
   private double radius;
   private double width;

   protected ShadowBuilder() {
   }

   public static ShadowBuilder create() {
      return new ShadowBuilder();
   }

   public void applyTo(Shadow var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBlurType(this.blurType);
      }

      if ((var2 & 2) != 0) {
         var1.setColor(this.color);
      }

      if ((var2 & 4) != 0) {
         var1.setHeight(this.height);
      }

      if ((var2 & 8) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 16) != 0) {
         var1.setRadius(this.radius);
      }

      if ((var2 & 32) != 0) {
         var1.setWidth(this.width);
      }

   }

   public ShadowBuilder blurType(BlurType var1) {
      this.blurType = var1;
      this.__set |= 1;
      return this;
   }

   public ShadowBuilder color(Color var1) {
      this.color = var1;
      this.__set |= 2;
      return this;
   }

   public ShadowBuilder height(double var1) {
      this.height = var1;
      this.__set |= 4;
      return this;
   }

   public ShadowBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 8;
      return this;
   }

   public ShadowBuilder radius(double var1) {
      this.radius = var1;
      this.__set |= 16;
      return this;
   }

   public ShadowBuilder width(double var1) {
      this.width = var1;
      this.__set |= 32;
      return this;
   }

   public Shadow build() {
      Shadow var1 = new Shadow();
      this.applyTo(var1);
      return var1;
   }
}
