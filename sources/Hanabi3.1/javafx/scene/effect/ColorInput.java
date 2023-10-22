package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.effect.Flood;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ColorInput extends Effect {
   private ObjectProperty paint;
   private DoubleProperty x;
   private DoubleProperty y;
   private DoubleProperty width;
   private DoubleProperty height;

   public ColorInput() {
   }

   public ColorInput(double var1, double var3, double var5, double var7, Paint var9) {
      this.setX(var1);
      this.setY(var3);
      this.setWidth(var5);
      this.setHeight(var7);
      this.setPaint(var9);
   }

   Flood impl_createImpl() {
      return new Flood(Toolkit.getPaintAccessor().getPlatformPaint(Color.RED));
   }

   public final void setPaint(Paint var1) {
      this.paintProperty().set(var1);
   }

   public final Paint getPaint() {
      return (Paint)(this.paint == null ? Color.RED : (Paint)this.paint.get());
   }

   public final ObjectProperty paintProperty() {
      if (this.paint == null) {
         this.paint = new ObjectPropertyBase(Color.RED) {
            public void invalidated() {
               ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return ColorInput.this;
            }

            public String getName() {
               return "paint";
            }
         };
      }

      return this.paint;
   }

   public final void setX(double var1) {
      this.xProperty().set(var1);
   }

   public final double getX() {
      return this.x == null ? 0.0 : this.x.get();
   }

   public final DoubleProperty xProperty() {
      if (this.x == null) {
         this.x = new DoublePropertyBase() {
            public void invalidated() {
               ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ColorInput.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ColorInput.this;
            }

            public String getName() {
               return "x";
            }
         };
      }

      return this.x;
   }

   public final void setY(double var1) {
      this.yProperty().set(var1);
   }

   public final double getY() {
      return this.y == null ? 0.0 : this.y.get();
   }

   public final DoubleProperty yProperty() {
      if (this.y == null) {
         this.y = new DoublePropertyBase() {
            public void invalidated() {
               ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ColorInput.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ColorInput.this;
            }

            public String getName() {
               return "y";
            }
         };
      }

      return this.y;
   }

   public final void setWidth(double var1) {
      this.widthProperty().set(var1);
   }

   public final double getWidth() {
      return this.width == null ? 0.0 : this.width.get();
   }

   public final DoubleProperty widthProperty() {
      if (this.width == null) {
         this.width = new DoublePropertyBase() {
            public void invalidated() {
               ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ColorInput.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ColorInput.this;
            }

            public String getName() {
               return "width";
            }
         };
      }

      return this.width;
   }

   public final void setHeight(double var1) {
      this.heightProperty().set(var1);
   }

   public final double getHeight() {
      return this.height == null ? 0.0 : this.height.get();
   }

   public final DoubleProperty heightProperty() {
      if (this.height == null) {
         this.height = new DoublePropertyBase() {
            public void invalidated() {
               ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ColorInput.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ColorInput.this;
            }

            public String getName() {
               return "height";
            }
         };
      }

      return this.height;
   }

   private Paint getPaintInternal() {
      Paint var1 = this.getPaint();
      return (Paint)(var1 == null ? Color.RED : var1);
   }

   void impl_update() {
      Flood var1 = (Flood)this.impl_getImpl();
      var1.setPaint(Toolkit.getPaintAccessor().getPlatformPaint(this.getPaintInternal()));
      var1.setFloodBounds(new RectBounds((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.getWidth()), (float)(this.getY() + this.getHeight())));
   }

   boolean impl_checkChainContains(Effect var1) {
      return false;
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      RectBounds var5 = new RectBounds((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.getWidth()), (float)(this.getY() + this.getHeight()));
      return transformBounds(var2, var5);
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      return new ColorInput(this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getPaint());
   }
}
