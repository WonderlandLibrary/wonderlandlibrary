package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import com.sun.scenario.effect.GeneralShadow;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Shadow extends Effect {
   private boolean changeIsLocal;
   private ObjectProperty input;
   private DoubleProperty radius;
   private DoubleProperty width;
   private DoubleProperty height;
   private ObjectProperty blurType;
   private ObjectProperty color;

   public Shadow() {
   }

   public Shadow(double var1, Color var3) {
      this.setRadius(var1);
      this.setColor(var3);
   }

   public Shadow(BlurType var1, Color var2, double var3) {
      this.setBlurType(var1);
      this.setColor(var2);
      this.setRadius(var3);
   }

   GeneralShadow impl_createImpl() {
      return new GeneralShadow();
   }

   public final void setInput(Effect var1) {
      this.inputProperty().set(var1);
   }

   public final Effect getInput() {
      return this.input == null ? null : (Effect)this.input.get();
   }

   public final ObjectProperty inputProperty() {
      if (this.input == null) {
         this.input = new Effect.EffectInputProperty("input");
      }

      return this.input;
   }

   boolean impl_checkChainContains(Effect var1) {
      Effect var2 = this.getInput();
      if (var2 == null) {
         return false;
      } else {
         return var2 == var1 ? true : var2.impl_checkChainContains(var1);
      }
   }

   public final void setRadius(double var1) {
      this.radiusProperty().set(var1);
   }

   public final double getRadius() {
      return this.radius == null ? 10.0 : this.radius.get();
   }

   public final DoubleProperty radiusProperty() {
      if (this.radius == null) {
         this.radius = new DoublePropertyBase(10.0) {
            public void invalidated() {
               double var1 = Shadow.this.getRadius();
               if (!Shadow.this.changeIsLocal) {
                  Shadow.this.changeIsLocal = true;
                  Shadow.this.updateRadius(var1);
                  Shadow.this.changeIsLocal = false;
                  Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                  Shadow.this.effectBoundsChanged();
               }

            }

            public Object getBean() {
               return Shadow.this;
            }

            public String getName() {
               return "radius";
            }
         };
      }

      return this.radius;
   }

   private void updateRadius(double var1) {
      double var3 = var1 * 2.0 + 1.0;
      if (this.width != null && this.width.isBound()) {
         if (this.height == null || !this.height.isBound()) {
            this.setHeight(var3 * 2.0 - this.getWidth());
         }
      } else if (this.height != null && this.height.isBound()) {
         this.setWidth(var3 * 2.0 - this.getHeight());
      } else {
         this.setWidth(var3);
         this.setHeight(var3);
      }

   }

   public final void setWidth(double var1) {
      this.widthProperty().set(var1);
   }

   public final double getWidth() {
      return this.width == null ? 21.0 : this.width.get();
   }

   public final DoubleProperty widthProperty() {
      if (this.width == null) {
         this.width = new DoublePropertyBase(21.0) {
            public void invalidated() {
               double var1 = Shadow.this.getWidth();
               if (!Shadow.this.changeIsLocal) {
                  Shadow.this.changeIsLocal = true;
                  Shadow.this.updateWidth(var1);
                  Shadow.this.changeIsLocal = false;
                  Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                  Shadow.this.effectBoundsChanged();
               }

            }

            public Object getBean() {
               return Shadow.this;
            }

            public String getName() {
               return "width";
            }
         };
      }

      return this.width;
   }

   private void updateWidth(double var1) {
      double var3;
      if (this.radius != null && this.radius.isBound()) {
         if (this.height == null || !this.height.isBound()) {
            var3 = this.getRadius() * 2.0 + 1.0;
            this.setHeight(var3 * 2.0 - var1);
         }
      } else {
         var3 = (var1 + this.getHeight()) / 2.0;
         var3 = (var3 - 1.0) / 2.0;
         if (var3 < 0.0) {
            var3 = 0.0;
         }

         this.setRadius(var3);
      }

   }

   public final void setHeight(double var1) {
      this.heightProperty().set(var1);
   }

   public final double getHeight() {
      return this.height == null ? 21.0 : this.height.get();
   }

   public final DoubleProperty heightProperty() {
      if (this.height == null) {
         this.height = new DoublePropertyBase(21.0) {
            public void invalidated() {
               double var1 = Shadow.this.getHeight();
               if (!Shadow.this.changeIsLocal) {
                  Shadow.this.changeIsLocal = true;
                  Shadow.this.updateHeight(var1);
                  Shadow.this.changeIsLocal = false;
                  Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                  Shadow.this.effectBoundsChanged();
               }

            }

            public Object getBean() {
               return Shadow.this;
            }

            public String getName() {
               return "height";
            }
         };
      }

      return this.height;
   }

   private void updateHeight(double var1) {
      double var3;
      if (this.radius != null && this.radius.isBound()) {
         if (this.width == null || !this.width.isBound()) {
            var3 = this.getRadius() * 2.0 + 1.0;
            this.setWidth(var3 * 2.0 - var1);
         }
      } else {
         var3 = (this.getWidth() + var1) / 2.0;
         var3 = (var3 - 1.0) / 2.0;
         if (var3 < 0.0) {
            var3 = 0.0;
         }

         this.setRadius(var3);
      }

   }

   public final void setBlurType(BlurType var1) {
      this.blurTypeProperty().set(var1);
   }

   public final BlurType getBlurType() {
      return this.blurType == null ? BlurType.THREE_PASS_BOX : (BlurType)this.blurType.get();
   }

   public final ObjectProperty blurTypeProperty() {
      if (this.blurType == null) {
         this.blurType = new ObjectPropertyBase(BlurType.THREE_PASS_BOX) {
            public void invalidated() {
               Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               Shadow.this.effectBoundsChanged();
            }

            public Object getBean() {
               return Shadow.this;
            }

            public String getName() {
               return "blurType";
            }
         };
      }

      return this.blurType;
   }

   public final void setColor(Color var1) {
      this.colorProperty().set(var1);
   }

   public final Color getColor() {
      return this.color == null ? Color.BLACK : (Color)this.color.get();
   }

   public final ObjectProperty colorProperty() {
      if (this.color == null) {
         this.color = new ObjectPropertyBase(Color.BLACK) {
            public void invalidated() {
               Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return Shadow.this;
            }

            public String getName() {
               return "color";
            }
         };
      }

      return this.color;
   }

   private float getClampedWidth() {
      return (float)Utils.clamp(0.0, this.getWidth(), 255.0);
   }

   private float getClampedHeight() {
      return (float)Utils.clamp(0.0, this.getHeight(), 255.0);
   }

   private Color getColorInternal() {
      Color var1 = this.getColor();
      return var1 == null ? Color.BLACK : var1;
   }

   private BlurType getBlurTypeInternal() {
      BlurType var1 = this.getBlurType();
      return var1 == null ? BlurType.THREE_PASS_BOX : var1;
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      GeneralShadow var2 = (GeneralShadow)this.impl_getImpl();
      var2.setInput(var1 == null ? null : var1.impl_getImpl());
      var2.setGaussianWidth(this.getClampedWidth());
      var2.setGaussianHeight(this.getClampedHeight());
      var2.setShadowMode(Toolkit.getToolkit().toShadowMode(this.getBlurTypeInternal()));
      var2.setColor(Toolkit.getToolkit().toColor4f(this.getColorInternal()));
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      var1 = getInputBounds(var1, BaseTransform.IDENTITY_TRANSFORM, var3, var4, this.getInput());
      return getShadowBounds(var1, var2, this.getClampedWidth(), this.getClampedHeight(), this.getBlurTypeInternal());
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      Shadow var1 = new Shadow(this.getBlurType(), this.getColor(), this.getRadius());
      var1.setInput(this.getInput());
      var1.setHeight(this.getHeight());
      var1.setWidth(this.getWidth());
      return var1;
   }
}
