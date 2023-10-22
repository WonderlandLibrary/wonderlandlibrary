package javafx.scene.shape;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NGShape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.paint.Paint;

public class Rectangle extends Shape {
   private final RoundRectangle2D shape;
   private static final int NON_RECTILINEAR_TYPE_MASK = -80;
   private DoubleProperty x;
   private DoubleProperty y;
   private final DoubleProperty width;
   private final DoubleProperty height;
   private DoubleProperty arcWidth;
   private DoubleProperty arcHeight;

   public Rectangle() {
      this.shape = new RoundRectangle2D();
      this.width = new DoublePropertyBase() {
         public void invalidated() {
            Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Rectangle.this.impl_geomChanged();
         }

         public Object getBean() {
            return Rectangle.this;
         }

         public String getName() {
            return "width";
         }
      };
      this.height = new DoublePropertyBase() {
         public void invalidated() {
            Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Rectangle.this.impl_geomChanged();
         }

         public Object getBean() {
            return Rectangle.this;
         }

         public String getName() {
            return "height";
         }
      };
   }

   public Rectangle(double var1, double var3) {
      this.shape = new RoundRectangle2D();
      this.width = new DoublePropertyBase() {
         public void invalidated() {
            Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Rectangle.this.impl_geomChanged();
         }

         public Object getBean() {
            return Rectangle.this;
         }

         public String getName() {
            return "width";
         }
      };
      this.height = new DoublePropertyBase() {
         public void invalidated() {
            Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Rectangle.this.impl_geomChanged();
         }

         public Object getBean() {
            return Rectangle.this;
         }

         public String getName() {
            return "height";
         }
      };
      this.setWidth(var1);
      this.setHeight(var3);
   }

   public Rectangle(double var1, double var3, Paint var5) {
      this.shape = new RoundRectangle2D();
      this.width = new DoublePropertyBase() {
         public void invalidated() {
            Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Rectangle.this.impl_geomChanged();
         }

         public Object getBean() {
            return Rectangle.this;
         }

         public String getName() {
            return "width";
         }
      };
      this.height = new DoublePropertyBase() {
         public void invalidated() {
            Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Rectangle.this.impl_geomChanged();
         }

         public Object getBean() {
            return Rectangle.this;
         }

         public String getName() {
            return "height";
         }
      };
      this.setWidth(var1);
      this.setHeight(var3);
      this.setFill(var5);
   }

   public Rectangle(double var1, double var3, double var5, double var7) {
      this(var5, var7);
      this.setX(var1);
      this.setY(var3);
   }

   public final void setX(double var1) {
      if (this.x != null || var1 != 0.0) {
         this.xProperty().set(var1);
      }

   }

   public final double getX() {
      return this.x == null ? 0.0 : this.x.get();
   }

   public final DoubleProperty xProperty() {
      if (this.x == null) {
         this.x = new DoublePropertyBase() {
            public void invalidated() {
               Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Rectangle.this.impl_geomChanged();
            }

            public Object getBean() {
               return Rectangle.this;
            }

            public String getName() {
               return "x";
            }
         };
      }

      return this.x;
   }

   public final void setY(double var1) {
      if (this.y != null || var1 != 0.0) {
         this.yProperty().set(var1);
      }

   }

   public final double getY() {
      return this.y == null ? 0.0 : this.y.get();
   }

   public final DoubleProperty yProperty() {
      if (this.y == null) {
         this.y = new DoublePropertyBase() {
            public void invalidated() {
               Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Rectangle.this.impl_geomChanged();
            }

            public Object getBean() {
               return Rectangle.this;
            }

            public String getName() {
               return "y";
            }
         };
      }

      return this.y;
   }

   public final void setWidth(double var1) {
      this.width.set(var1);
   }

   public final double getWidth() {
      return this.width.get();
   }

   public final DoubleProperty widthProperty() {
      return this.width;
   }

   public final void setHeight(double var1) {
      this.height.set(var1);
   }

   public final double getHeight() {
      return this.height.get();
   }

   public final DoubleProperty heightProperty() {
      return this.height;
   }

   public final void setArcWidth(double var1) {
      if (this.arcWidth != null || var1 != 0.0) {
         this.arcWidthProperty().set(var1);
      }

   }

   public final double getArcWidth() {
      return this.arcWidth == null ? 0.0 : this.arcWidth.get();
   }

   public final DoubleProperty arcWidthProperty() {
      if (this.arcWidth == null) {
         this.arcWidth = new StyleableDoubleProperty() {
            public void invalidated() {
               Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            }

            public CssMetaData getCssMetaData() {
               return Rectangle.StyleableProperties.ARC_WIDTH;
            }

            public Object getBean() {
               return Rectangle.this;
            }

            public String getName() {
               return "arcWidth";
            }
         };
      }

      return this.arcWidth;
   }

   public final void setArcHeight(double var1) {
      if (this.arcHeight != null || var1 != 0.0) {
         this.arcHeightProperty().set(var1);
      }

   }

   public final double getArcHeight() {
      return this.arcHeight == null ? 0.0 : this.arcHeight.get();
   }

   public final DoubleProperty arcHeightProperty() {
      if (this.arcHeight == null) {
         this.arcHeight = new StyleableDoubleProperty() {
            public void invalidated() {
               Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            }

            public CssMetaData getCssMetaData() {
               return Rectangle.StyleableProperties.ARC_HEIGHT;
            }

            public Object getBean() {
               return Rectangle.this;
            }

            public String getName() {
               return "arcHeight";
            }
         };
      }

      return this.arcHeight;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGRectangle();
   }

   public static List getClassCssMetaData() {
      return Rectangle.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   StrokeLineJoin convertLineJoin(StrokeLineJoin var1) {
      return this.getArcWidth() > 0.0 && this.getArcHeight() > 0.0 ? StrokeLineJoin.BEVEL : var1;
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      if (this.impl_mode == NGShape.Mode.EMPTY) {
         return var1.makeEmpty();
      } else if (this.getArcWidth() > 0.0 && this.getArcHeight() > 0.0 && (var2.getType() & -80) != 0) {
         return this.computeShapeBounds(var1, var2, this.impl_configShape());
      } else {
         double var3;
         double var5;
         if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
            var3 = this.getStrokeWidth();
            if (this.getStrokeType() == StrokeType.CENTERED) {
               var3 /= 2.0;
            }

            var5 = 0.0;
         } else {
            var5 = 0.0;
            var3 = 0.0;
         }

         return this.computeBounds(var1, var2, var3, var5, this.getX(), this.getY(), this.getWidth(), this.getHeight());
      }
   }

   /** @deprecated */
   @Deprecated
   public RoundRectangle2D impl_configShape() {
      if (this.getArcWidth() > 0.0 && this.getArcHeight() > 0.0) {
         this.shape.setRoundRect((float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight(), (float)this.getArcWidth(), (float)this.getArcHeight());
      } else {
         this.shape.setRoundRect((float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight(), 0.0F, 0.0F);
      }

      return this.shape;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         NGRectangle var1 = (NGRectangle)this.impl_getPeer();
         var1.updateRectangle((float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight(), (float)this.getArcWidth(), (float)this.getArcHeight());
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Rectangle[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("x=").append(this.getX());
      var1.append(", y=").append(this.getY());
      var1.append(", width=").append(this.getWidth());
      var1.append(", height=").append(this.getHeight());
      var1.append(", fill=").append(this.getFill());
      Paint var3 = this.getStroke();
      if (var3 != null) {
         var1.append(", stroke=").append(var3);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }

   private static class StyleableProperties {
      private static final CssMetaData ARC_HEIGHT = new CssMetaData("-fx-arc-height", SizeConverter.getInstance(), 0.0) {
         public boolean isSettable(Rectangle var1) {
            return var1.arcHeight == null || !var1.arcHeight.isBound();
         }

         public StyleableProperty getStyleableProperty(Rectangle var1) {
            return (StyleableProperty)var1.arcHeightProperty();
         }
      };
      private static final CssMetaData ARC_WIDTH = new CssMetaData("-fx-arc-width", SizeConverter.getInstance(), 0.0) {
         public boolean isSettable(Rectangle var1) {
            return var1.arcWidth == null || !var1.arcWidth.isBound();
         }

         public StyleableProperty getStyleableProperty(Rectangle var1) {
            return (StyleableProperty)var1.arcWidthProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Shape.getClassCssMetaData());
         var0.add(ARC_HEIGHT);
         var0.add(ARC_WIDTH);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
