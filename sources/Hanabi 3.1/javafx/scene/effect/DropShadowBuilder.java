package javafx.scene.effect;

import javafx.scene.paint.Color;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class DropShadowBuilder implements Builder {
   private int __set;
   private BlurType blurType;
   private Color color;
   private double height;
   private Effect input;
   private double offsetX;
   private double offsetY;
   private double radius;
   private double spread;
   private double width;

   protected DropShadowBuilder() {
   }

   public static DropShadowBuilder create() {
      return new DropShadowBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(DropShadow var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setBlurType(this.blurType);
               break;
            case 1:
               var1.setColor(this.color);
               break;
            case 2:
               var1.setHeight(this.height);
               break;
            case 3:
               var1.setInput(this.input);
               break;
            case 4:
               var1.setOffsetX(this.offsetX);
               break;
            case 5:
               var1.setOffsetY(this.offsetY);
               break;
            case 6:
               var1.setRadius(this.radius);
               break;
            case 7:
               var1.setSpread(this.spread);
               break;
            case 8:
               var1.setWidth(this.width);
         }
      }

   }

   public DropShadowBuilder blurType(BlurType var1) {
      this.blurType = var1;
      this.__set(0);
      return this;
   }

   public DropShadowBuilder color(Color var1) {
      this.color = var1;
      this.__set(1);
      return this;
   }

   public DropShadowBuilder height(double var1) {
      this.height = var1;
      this.__set(2);
      return this;
   }

   public DropShadowBuilder input(Effect var1) {
      this.input = var1;
      this.__set(3);
      return this;
   }

   public DropShadowBuilder offsetX(double var1) {
      this.offsetX = var1;
      this.__set(4);
      return this;
   }

   public DropShadowBuilder offsetY(double var1) {
      this.offsetY = var1;
      this.__set(5);
      return this;
   }

   public DropShadowBuilder radius(double var1) {
      this.radius = var1;
      this.__set(6);
      return this;
   }

   public DropShadowBuilder spread(double var1) {
      this.spread = var1;
      this.__set(7);
      return this;
   }

   public DropShadowBuilder width(double var1) {
      this.width = var1;
      this.__set(8);
      return this;
   }

   public DropShadow build() {
      DropShadow var1 = new DropShadow();
      this.applyTo(var1);
      return var1;
   }
}
