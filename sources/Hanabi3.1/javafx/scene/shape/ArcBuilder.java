package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ArcBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private double centerX;
   private double centerY;
   private double length;
   private double radiusX;
   private double radiusY;
   private double startAngle;
   private ArcType type;

   protected ArcBuilder() {
   }

   public static ArcBuilder create() {
      return new ArcBuilder();
   }

   public void applyTo(Arc var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setCenterX(this.centerX);
      }

      if ((var2 & 2) != 0) {
         var1.setCenterY(this.centerY);
      }

      if ((var2 & 4) != 0) {
         var1.setLength(this.length);
      }

      if ((var2 & 8) != 0) {
         var1.setRadiusX(this.radiusX);
      }

      if ((var2 & 16) != 0) {
         var1.setRadiusY(this.radiusY);
      }

      if ((var2 & 32) != 0) {
         var1.setStartAngle(this.startAngle);
      }

      if ((var2 & 64) != 0) {
         var1.setType(this.type);
      }

   }

   public ArcBuilder centerX(double var1) {
      this.centerX = var1;
      this.__set |= 1;
      return this;
   }

   public ArcBuilder centerY(double var1) {
      this.centerY = var1;
      this.__set |= 2;
      return this;
   }

   public ArcBuilder length(double var1) {
      this.length = var1;
      this.__set |= 4;
      return this;
   }

   public ArcBuilder radiusX(double var1) {
      this.radiusX = var1;
      this.__set |= 8;
      return this;
   }

   public ArcBuilder radiusY(double var1) {
      this.radiusY = var1;
      this.__set |= 16;
      return this;
   }

   public ArcBuilder startAngle(double var1) {
      this.startAngle = var1;
      this.__set |= 32;
      return this;
   }

   public ArcBuilder type(ArcType var1) {
      this.type = var1;
      this.__set |= 64;
      return this;
   }

   public Arc build() {
      Arc var1 = new Arc();
      this.applyTo(var1);
      return var1;
   }
}
