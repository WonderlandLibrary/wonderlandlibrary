package javafx.scene.effect;

import javafx.scene.paint.Color;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class InnerShadowBuilder implements Builder {
   private int __set;
   private BlurType blurType;
   private double choke;
   private Color color;
   private double height;
   private Effect input;
   private double offsetX;
   private double offsetY;
   private double radius;
   private double width;

   protected InnerShadowBuilder() {
   }

   public static InnerShadowBuilder create() {
      return new InnerShadowBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(InnerShadow var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setBlurType(this.blurType);
               break;
            case 1:
               var1.setChoke(this.choke);
               break;
            case 2:
               var1.setColor(this.color);
               break;
            case 3:
               var1.setHeight(this.height);
               break;
            case 4:
               var1.setInput(this.input);
               break;
            case 5:
               var1.setOffsetX(this.offsetX);
               break;
            case 6:
               var1.setOffsetY(this.offsetY);
               break;
            case 7:
               var1.setRadius(this.radius);
               break;
            case 8:
               var1.setWidth(this.width);
         }
      }

   }

   public InnerShadowBuilder blurType(BlurType var1) {
      this.blurType = var1;
      this.__set(0);
      return this;
   }

   public InnerShadowBuilder choke(double var1) {
      this.choke = var1;
      this.__set(1);
      return this;
   }

   public InnerShadowBuilder color(Color var1) {
      this.color = var1;
      this.__set(2);
      return this;
   }

   public InnerShadowBuilder height(double var1) {
      this.height = var1;
      this.__set(3);
      return this;
   }

   public InnerShadowBuilder input(Effect var1) {
      this.input = var1;
      this.__set(4);
      return this;
   }

   public InnerShadowBuilder offsetX(double var1) {
      this.offsetX = var1;
      this.__set(5);
      return this;
   }

   public InnerShadowBuilder offsetY(double var1) {
      this.offsetY = var1;
      this.__set(6);
      return this;
   }

   public InnerShadowBuilder radius(double var1) {
      this.radius = var1;
      this.__set(7);
      return this;
   }

   public InnerShadowBuilder width(double var1) {
      this.width = var1;
      this.__set(8);
      return this;
   }

   public InnerShadow build() {
      InnerShadow var1 = new InnerShadow();
      this.applyTo(var1);
      return var1;
   }
}
