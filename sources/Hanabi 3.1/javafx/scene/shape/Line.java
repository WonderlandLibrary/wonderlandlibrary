package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGLine;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Line extends Shape {
   private final Line2D shape = new Line2D();
   private final DoubleProperty startX;
   private final DoubleProperty startY;
   private final DoubleProperty endX;
   private final DoubleProperty endY;

   public Line() {
      ((StyleableProperty)this.fillProperty()).applyStyle((StyleOrigin)null, (Object)null);
      ((StyleableProperty)this.strokeProperty()).applyStyle((StyleOrigin)null, Color.BLACK);
      this.startX = new DoublePropertyBase() {
         public void invalidated() {
            Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Line.this.impl_geomChanged();
         }

         public Object getBean() {
            return Line.this;
         }

         public String getName() {
            return "startX";
         }
      };
      this.startY = new DoublePropertyBase() {
         public void invalidated() {
            Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Line.this.impl_geomChanged();
         }

         public Object getBean() {
            return Line.this;
         }

         public String getName() {
            return "startY";
         }
      };
      this.endX = new DoublePropertyBase() {
         public void invalidated() {
            Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Line.this.impl_geomChanged();
         }

         public Object getBean() {
            return Line.this;
         }

         public String getName() {
            return "endX";
         }
      };
      this.endY = new DoublePropertyBase() {
         public void invalidated() {
            Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Line.this.impl_geomChanged();
         }

         public Object getBean() {
            return Line.this;
         }

         public String getName() {
            return "endY";
         }
      };
   }

   public Line(double var1, double var3, double var5, double var7) {
      ((StyleableProperty)this.fillProperty()).applyStyle((StyleOrigin)null, (Object)null);
      ((StyleableProperty)this.strokeProperty()).applyStyle((StyleOrigin)null, Color.BLACK);
      this.startX = new DoublePropertyBase() {
         public void invalidated() {
            Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Line.this.impl_geomChanged();
         }

         public Object getBean() {
            return Line.this;
         }

         public String getName() {
            return "startX";
         }
      };
      this.startY = new DoublePropertyBase() {
         public void invalidated() {
            Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Line.this.impl_geomChanged();
         }

         public Object getBean() {
            return Line.this;
         }

         public String getName() {
            return "startY";
         }
      };
      this.endX = new DoublePropertyBase() {
         public void invalidated() {
            Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Line.this.impl_geomChanged();
         }

         public Object getBean() {
            return Line.this;
         }

         public String getName() {
            return "endX";
         }
      };
      this.endY = new DoublePropertyBase() {
         public void invalidated() {
            Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Line.this.impl_geomChanged();
         }

         public Object getBean() {
            return Line.this;
         }

         public String getName() {
            return "endY";
         }
      };
      this.setStartX(var1);
      this.setStartY(var3);
      this.setEndX(var5);
      this.setEndY(var7);
   }

   public final void setStartX(double var1) {
      this.startX.set(var1);
   }

   public final double getStartX() {
      return this.startX.get();
   }

   public final DoubleProperty startXProperty() {
      return this.startX;
   }

   public final void setStartY(double var1) {
      this.startY.set(var1);
   }

   public final double getStartY() {
      return this.startY.get();
   }

   public final DoubleProperty startYProperty() {
      return this.startY;
   }

   public final void setEndX(double var1) {
      this.endX.set(var1);
   }

   public final double getEndX() {
      return this.endX.get();
   }

   public final DoubleProperty endXProperty() {
      return this.endX;
   }

   public final void setEndY(double var1) {
      this.endY.set(var1);
   }

   public final double getEndY() {
      return this.endY.get();
   }

   public final DoubleProperty endYProperty() {
      return this.endY;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGLine();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      if (this.impl_mode != NGShape.Mode.FILL && this.impl_mode != NGShape.Mode.EMPTY && this.getStrokeType() != StrokeType.INSIDE) {
         double var3 = this.getStartX();
         double var5 = this.getEndX();
         double var7 = this.getStartY();
         double var9 = this.getEndY();
         double var11 = this.getStrokeWidth();
         if (this.getStrokeType() == StrokeType.CENTERED) {
            var11 /= 2.0;
         }

         double var13;
         double var15;
         double var17;
         double var19;
         if (var2.isTranslateOrIdentity()) {
            var11 = Math.max(var11, 0.5);
            if (var2.getType() == 1) {
               var17 = var2.getMxt();
               var19 = var2.getMyt();
               var3 += var17;
               var7 += var19;
               var5 += var17;
               var9 += var19;
            }

            if (var7 == var9 && var3 != var5) {
               var15 = var11;
               var13 = this.getStrokeLineCap() == StrokeLineCap.BUTT ? 0.0 : var11;
            } else if (var3 == var5 && var7 != var9) {
               var13 = var11;
               var15 = this.getStrokeLineCap() == StrokeLineCap.BUTT ? 0.0 : var11;
            } else {
               if (this.getStrokeLineCap() == StrokeLineCap.SQUARE) {
                  var11 *= Math.sqrt(2.0);
               }

               var15 = var11;
               var13 = var11;
            }

            if (var3 > var5) {
               var17 = var3;
               var3 = var5;
               var5 = var17;
            }

            if (var7 > var9) {
               var17 = var7;
               var7 = var9;
               var9 = var17;
            }

            var3 -= var13;
            var7 -= var15;
            var5 += var13;
            var9 += var15;
            var1 = var1.deriveWithNewBounds((float)var3, (float)var7, 0.0F, (float)var5, (float)var9, 0.0F);
            return var1;
         } else {
            var13 = var5 - var3;
            var15 = var9 - var7;
            var17 = Math.sqrt(var13 * var13 + var15 * var15);
            if (var17 == 0.0) {
               var13 = var11;
               var15 = 0.0;
            } else {
               var13 = var11 * var13 / var17;
               var15 = var11 * var15 / var17;
            }

            double var21;
            if (this.getStrokeLineCap() != StrokeLineCap.BUTT) {
               var19 = var13;
               var21 = var15;
            } else {
               var21 = 0.0;
               var19 = 0.0;
            }

            double[] var23 = new double[]{var3 - var15 - var19, var7 + var13 - var21, var3 + var15 - var19, var7 - var13 - var21, var5 + var15 + var19, var9 - var13 + var21, var5 - var15 + var19, var9 + var13 + var21};
            var2.transform((double[])var23, 0, (double[])var23, 0, 4);
            var3 = Math.min(Math.min(var23[0], var23[2]), Math.min(var23[4], var23[6]));
            var7 = Math.min(Math.min(var23[1], var23[3]), Math.min(var23[5], var23[7]));
            var5 = Math.max(Math.max(var23[0], var23[2]), Math.max(var23[4], var23[6]));
            var9 = Math.max(Math.max(var23[1], var23[3]), Math.max(var23[5], var23[7]));
            var3 -= 0.5;
            var7 -= 0.5;
            var5 += 0.5;
            var9 += 0.5;
            var1 = var1.deriveWithNewBounds((float)var3, (float)var7, 0.0F, (float)var5, (float)var9, 0.0F);
            return var1;
         }
      } else {
         return var1.makeEmpty();
      }
   }

   /** @deprecated */
   @Deprecated
   public Line2D impl_configShape() {
      this.shape.setLine((float)this.getStartX(), (float)this.getStartY(), (float)this.getEndX(), (float)this.getEndY());
      return this.shape;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         NGLine var1 = (NGLine)this.impl_getPeer();
         var1.updateLine((float)this.getStartX(), (float)this.getStartY(), (float)this.getEndX(), (float)this.getEndY());
      }

   }

   /** @deprecated */
   @Deprecated
   protected Paint impl_cssGetFillInitialValue() {
      return null;
   }

   /** @deprecated */
   @Deprecated
   protected Paint impl_cssGetStrokeInitialValue() {
      return Color.BLACK;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Line[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("startX=").append(this.getStartX());
      var1.append(", startY=").append(this.getStartY());
      var1.append(", endX=").append(this.getEndX());
      var1.append(", endY=").append(this.getEndY());
      Paint var3 = this.getStroke();
      if (var3 != null) {
         var1.append(", stroke=").append(var3);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }
}
