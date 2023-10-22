package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class PerspectiveTransformBuilder implements Builder {
   private int __set;
   private Effect input;
   private double llx;
   private double lly;
   private double lrx;
   private double lry;
   private double ulx;
   private double uly;
   private double urx;
   private double ury;

   protected PerspectiveTransformBuilder() {
   }

   public static PerspectiveTransformBuilder create() {
      return new PerspectiveTransformBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(PerspectiveTransform var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setInput(this.input);
               break;
            case 1:
               var1.setLlx(this.llx);
               break;
            case 2:
               var1.setLly(this.lly);
               break;
            case 3:
               var1.setLrx(this.lrx);
               break;
            case 4:
               var1.setLry(this.lry);
               break;
            case 5:
               var1.setUlx(this.ulx);
               break;
            case 6:
               var1.setUly(this.uly);
               break;
            case 7:
               var1.setUrx(this.urx);
               break;
            case 8:
               var1.setUry(this.ury);
         }
      }

   }

   public PerspectiveTransformBuilder input(Effect var1) {
      this.input = var1;
      this.__set(0);
      return this;
   }

   public PerspectiveTransformBuilder llx(double var1) {
      this.llx = var1;
      this.__set(1);
      return this;
   }

   public PerspectiveTransformBuilder lly(double var1) {
      this.lly = var1;
      this.__set(2);
      return this;
   }

   public PerspectiveTransformBuilder lrx(double var1) {
      this.lrx = var1;
      this.__set(3);
      return this;
   }

   public PerspectiveTransformBuilder lry(double var1) {
      this.lry = var1;
      this.__set(4);
      return this;
   }

   public PerspectiveTransformBuilder ulx(double var1) {
      this.ulx = var1;
      this.__set(5);
      return this;
   }

   public PerspectiveTransformBuilder uly(double var1) {
      this.uly = var1;
      this.__set(6);
      return this;
   }

   public PerspectiveTransformBuilder urx(double var1) {
      this.urx = var1;
      this.__set(7);
      return this;
   }

   public PerspectiveTransformBuilder ury(double var1) {
      this.ury = var1;
      this.__set(8);
      return this;
   }

   public PerspectiveTransform build() {
      PerspectiveTransform var1 = new PerspectiveTransform();
      this.applyTo(var1);
      return var1;
   }
}
