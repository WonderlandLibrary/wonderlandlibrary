package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.NodeBuilder;
import javafx.scene.paint.Paint;

/** @deprecated */
@Deprecated
public abstract class ShapeBuilder extends NodeBuilder {
   private int __set;
   private Paint fill;
   private boolean smooth;
   private Paint stroke;
   private Collection strokeDashArray;
   private double strokeDashOffset;
   private StrokeLineCap strokeLineCap;
   private StrokeLineJoin strokeLineJoin;
   private double strokeMiterLimit;
   private StrokeType strokeType;
   private double strokeWidth;

   protected ShapeBuilder() {
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Shape var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setFill(this.fill);
               break;
            case 1:
               var1.setSmooth(this.smooth);
               break;
            case 2:
               var1.setStroke(this.stroke);
               break;
            case 3:
               var1.getStrokeDashArray().addAll(this.strokeDashArray);
               break;
            case 4:
               var1.setStrokeDashOffset(this.strokeDashOffset);
               break;
            case 5:
               var1.setStrokeLineCap(this.strokeLineCap);
               break;
            case 6:
               var1.setStrokeLineJoin(this.strokeLineJoin);
               break;
            case 7:
               var1.setStrokeMiterLimit(this.strokeMiterLimit);
               break;
            case 8:
               var1.setStrokeType(this.strokeType);
               break;
            case 9:
               var1.setStrokeWidth(this.strokeWidth);
         }
      }

   }

   public ShapeBuilder fill(Paint var1) {
      this.fill = var1;
      this.__set(0);
      return this;
   }

   public ShapeBuilder smooth(boolean var1) {
      this.smooth = var1;
      this.__set(1);
      return this;
   }

   public ShapeBuilder stroke(Paint var1) {
      this.stroke = var1;
      this.__set(2);
      return this;
   }

   public ShapeBuilder strokeDashArray(Collection var1) {
      this.strokeDashArray = var1;
      this.__set(3);
      return this;
   }

   public ShapeBuilder strokeDashArray(Double... var1) {
      return this.strokeDashArray((Collection)Arrays.asList(var1));
   }

   public ShapeBuilder strokeDashOffset(double var1) {
      this.strokeDashOffset = var1;
      this.__set(4);
      return this;
   }

   public ShapeBuilder strokeLineCap(StrokeLineCap var1) {
      this.strokeLineCap = var1;
      this.__set(5);
      return this;
   }

   public ShapeBuilder strokeLineJoin(StrokeLineJoin var1) {
      this.strokeLineJoin = var1;
      this.__set(6);
      return this;
   }

   public ShapeBuilder strokeMiterLimit(double var1) {
      this.strokeMiterLimit = var1;
      this.__set(7);
      return this;
   }

   public ShapeBuilder strokeType(StrokeType var1) {
      this.strokeType = var1;
      this.__set(8);
      return this;
   }

   public ShapeBuilder strokeWidth(double var1) {
      this.strokeWidth = var1;
      this.__set(9);
      return this;
   }
}
