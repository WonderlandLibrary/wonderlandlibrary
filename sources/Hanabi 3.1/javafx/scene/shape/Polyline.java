package javafx.scene.shape;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPolyline;
import com.sun.javafx.sg.prism.NGShape;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Polyline extends Shape {
   private final Path2D shape = new Path2D();
   private final ObservableList points;

   public Polyline() {
      ((StyleableProperty)this.fillProperty()).applyStyle((StyleOrigin)null, (Object)null);
      ((StyleableProperty)this.strokeProperty()).applyStyle((StyleOrigin)null, Color.BLACK);
      this.points = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            Polyline.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Polyline.this.impl_geomChanged();
         }
      };
   }

   public Polyline(double... var1) {
      ((StyleableProperty)this.fillProperty()).applyStyle((StyleOrigin)null, (Object)null);
      ((StyleableProperty)this.strokeProperty()).applyStyle((StyleOrigin)null, Color.BLACK);
      this.points = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            Polyline.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Polyline.this.impl_geomChanged();
         }
      };
      if (var1 != null) {
         double[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            double var5 = var2[var4];
            this.getPoints().add(var5);
         }
      }

   }

   public final ObservableList getPoints() {
      return this.points;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGPolyline();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      if (this.impl_mode != NGShape.Mode.EMPTY && this.getPoints().size() > 1) {
         if (this.getPoints().size() == 2) {
            if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
               double var3 = this.getStrokeWidth();
               if (this.getStrokeType() == StrokeType.CENTERED) {
                  var3 /= 2.0;
               }

               return this.computeBounds(var1, var2, var3, 0.5, (Double)this.getPoints().get(0), (Double)this.getPoints().get(1), 0.0, 0.0);
            } else {
               return var1.makeEmpty();
            }
         } else {
            return this.computeShapeBounds(var1, var2, this.impl_configShape());
         }
      } else {
         return var1.makeEmpty();
      }
   }

   /** @deprecated */
   @Deprecated
   public Path2D impl_configShape() {
      double var1 = (Double)this.getPoints().get(0);
      double var3 = (Double)this.getPoints().get(1);
      this.shape.reset();
      this.shape.moveTo((float)var1, (float)var3);
      int var5 = this.getPoints().size() & -2;

      for(int var6 = 2; var6 < var5; var6 += 2) {
         var1 = (Double)this.getPoints().get(var6);
         var3 = (Double)this.getPoints().get(var6 + 1);
         this.shape.lineTo((float)var1, (float)var3);
      }

      return this.shape;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         int var1 = this.getPoints().size() & -2;
         float[] var2 = new float[var1];

         for(int var3 = 0; var3 < var1; ++var3) {
            var2[var3] = (float)(Double)this.getPoints().get(var3);
         }

         NGPolyline var4 = (NGPolyline)this.impl_getPeer();
         var4.updatePolyline(var2);
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
      StringBuilder var1 = new StringBuilder("Polyline[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("points=").append(this.getPoints());
      var1.append(", fill=").append(this.getFill());
      Paint var3 = this.getStroke();
      if (var3 != null) {
         var1.append(", stroke=").append(var3);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }
}
