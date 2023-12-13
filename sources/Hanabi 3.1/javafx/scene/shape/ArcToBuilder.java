package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ArcToBuilder extends PathElementBuilder implements Builder {
   private int __set;
   private boolean largeArcFlag;
   private double radiusX;
   private double radiusY;
   private boolean sweepFlag;
   private double x;
   private double XAxisRotation;
   private double y;

   protected ArcToBuilder() {
   }

   public static ArcToBuilder create() {
      return new ArcToBuilder();
   }

   public void applyTo(ArcTo var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setLargeArcFlag(this.largeArcFlag);
      }

      if ((var2 & 2) != 0) {
         var1.setRadiusX(this.radiusX);
      }

      if ((var2 & 4) != 0) {
         var1.setRadiusY(this.radiusY);
      }

      if ((var2 & 8) != 0) {
         var1.setSweepFlag(this.sweepFlag);
      }

      if ((var2 & 16) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 32) != 0) {
         var1.setXAxisRotation(this.XAxisRotation);
      }

      if ((var2 & 64) != 0) {
         var1.setY(this.y);
      }

   }

   public ArcToBuilder largeArcFlag(boolean var1) {
      this.largeArcFlag = var1;
      this.__set |= 1;
      return this;
   }

   public ArcToBuilder radiusX(double var1) {
      this.radiusX = var1;
      this.__set |= 2;
      return this;
   }

   public ArcToBuilder radiusY(double var1) {
      this.radiusY = var1;
      this.__set |= 4;
      return this;
   }

   public ArcToBuilder sweepFlag(boolean var1) {
      this.sweepFlag = var1;
      this.__set |= 8;
      return this;
   }

   public ArcToBuilder x(double var1) {
      this.x = var1;
      this.__set |= 16;
      return this;
   }

   public ArcToBuilder XAxisRotation(double var1) {
      this.XAxisRotation = var1;
      this.__set |= 32;
      return this;
   }

   public ArcToBuilder y(double var1) {
      this.y = var1;
      this.__set |= 64;
      return this;
   }

   public ArcTo build() {
      ArcTo var1 = new ArcTo();
      this.applyTo(var1);
      return var1;
   }
}
