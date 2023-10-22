package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class InnerShadow extends Effect {
   private boolean changeIsLocal;
   private ObjectProperty input;
   private DoubleProperty radius;
   private DoubleProperty width;
   private DoubleProperty height;
   private ObjectProperty blurType;
   private DoubleProperty choke;
   private ObjectProperty color;
   private DoubleProperty offsetX;
   private DoubleProperty offsetY;

   public InnerShadow() {
   }

   public InnerShadow(double var1, Color var3) {
      this.setRadius(var1);
      this.setColor(var3);
   }

   public InnerShadow(double var1, double var3, double var5, Color var7) {
      this.setRadius(var1);
      this.setOffsetX(var3);
      this.setOffsetY(var5);
      this.setColor(var7);
   }

   public InnerShadow(BlurType var1, Color var2, double var3, double var5, double var7, double var9) {
      this.setBlurType(var1);
      this.setColor(var2);
      this.setRadius(var3);
      this.setChoke(var5);
      this.setOffsetX(var7);
      this.setOffsetY(var9);
   }

   com.sun.scenario.effect.InnerShadow impl_createImpl() {
      return new com.sun.scenario.effect.InnerShadow();
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
               double var1 = InnerShadow.this.getRadius();
               if (!InnerShadow.this.changeIsLocal) {
                  InnerShadow.this.changeIsLocal = true;
                  InnerShadow.this.updateRadius(var1);
                  InnerShadow.this.changeIsLocal = false;
                  InnerShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                  InnerShadow.this.effectBoundsChanged();
               }

            }

            public Object getBean() {
               return InnerShadow.this;
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
               double var1 = InnerShadow.this.getWidth();
               if (!InnerShadow.this.changeIsLocal) {
                  InnerShadow.this.changeIsLocal = true;
                  InnerShadow.this.updateWidth(var1);
                  InnerShadow.this.changeIsLocal = false;
                  InnerShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                  InnerShadow.this.effectBoundsChanged();
               }

            }

            public Object getBean() {
               return InnerShadow.this;
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
               double var1 = InnerShadow.this.getHeight();
               if (!InnerShadow.this.changeIsLocal) {
                  InnerShadow.this.changeIsLocal = true;
                  InnerShadow.this.updateHeight(var1);
                  InnerShadow.this.changeIsLocal = false;
                  InnerShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                  InnerShadow.this.effectBoundsChanged();
               }

            }

            public Object getBean() {
               return InnerShadow.this;
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
               InnerShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return InnerShadow.this;
            }

            public String getName() {
               return "blurType";
            }
         };
      }

      return this.blurType;
   }

   public final void setChoke(double var1) {
      this.chokeProperty().set(var1);
   }

   public final double getChoke() {
      return this.choke == null ? 0.0 : this.choke.get();
   }

   public final DoubleProperty chokeProperty() {
      if (this.choke == null) {
         this.choke = new DoublePropertyBase() {
            public void invalidated() {
               InnerShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return InnerShadow.this;
            }

            public String getName() {
               return "choke";
            }
         };
      }

      return this.choke;
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
               InnerShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return InnerShadow.this;
            }

            public String getName() {
               return "color";
            }
         };
      }

      return this.color;
   }

   public final void setOffsetX(double var1) {
      this.offsetXProperty().set(var1);
   }

   public final double getOffsetX() {
      return this.offsetX == null ? 0.0 : this.offsetX.get();
   }

   public final DoubleProperty offsetXProperty() {
      if (this.offsetX == null) {
         this.offsetX = new DoublePropertyBase() {
            public void invalidated() {
               InnerShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               InnerShadow.this.effectBoundsChanged();
            }

            public Object getBean() {
               return InnerShadow.this;
            }

            public String getName() {
               return "offsetX";
            }
         };
      }

      return this.offsetX;
   }

   public final void setOffsetY(double var1) {
      this.offsetYProperty().set(var1);
   }

   public final double getOffsetY() {
      return this.offsetY == null ? 0.0 : this.offsetY.get();
   }

   public final DoubleProperty offsetYProperty() {
      if (this.offsetY == null) {
         this.offsetY = new DoublePropertyBase() {
            public void invalidated() {
               InnerShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               InnerShadow.this.effectBoundsChanged();
            }

            public Object getBean() {
               return InnerShadow.this;
            }

            public String getName() {
               return "offsetY";
            }
         };
      }

      return this.offsetY;
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

      com.sun.scenario.effect.InnerShadow var2 = (com.sun.scenario.effect.InnerShadow)this.impl_getImpl();
      var2.setShadowSourceInput(var1 == null ? null : var1.impl_getImpl());
      var2.setContentInput(var1 == null ? null : var1.impl_getImpl());
      var2.setGaussianWidth((float)Utils.clamp(0.0, this.getWidth(), 255.0));
      var2.setGaussianHeight((float)Utils.clamp(0.0, this.getHeight(), 255.0));
      var2.setShadowMode(Toolkit.getToolkit().toShadowMode(this.getBlurTypeInternal()));
      var2.setColor(Toolkit.getToolkit().toColor4f(this.getColorInternal()));
      var2.setChoke((float)Utils.clamp(0.0, this.getChoke(), 1.0));
      var2.setOffsetX((int)this.getOffsetX());
      var2.setOffsetY((int)this.getOffsetY());
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      return getInputBounds(var1, var2, var3, var4, this.getInput());
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      InnerShadow var1 = new InnerShadow(this.getBlurType(), this.getColor(), this.getRadius(), this.getChoke(), this.getOffsetX(), this.getOffsetY());
      var1.setInput(this.getInput());
      var1.setWidth(this.getWidth());
      var1.setHeight(this.getHeight());
      return var1;
   }
}
