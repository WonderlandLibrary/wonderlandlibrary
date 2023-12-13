package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

public class ColorAdjust extends Effect {
   private ObjectProperty input;
   private DoubleProperty hue;
   private DoubleProperty saturation;
   private DoubleProperty brightness;
   private DoubleProperty contrast;

   public ColorAdjust() {
   }

   public ColorAdjust(double var1, double var3, double var5, double var7) {
      this.setBrightness(var5);
      this.setContrast(var7);
      this.setHue(var1);
      this.setSaturation(var3);
   }

   com.sun.scenario.effect.ColorAdjust impl_createImpl() {
      return new com.sun.scenario.effect.ColorAdjust();
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

   public final void setHue(double var1) {
      this.hueProperty().set(var1);
   }

   public final double getHue() {
      return this.hue == null ? 0.0 : this.hue.get();
   }

   public final DoubleProperty hueProperty() {
      if (this.hue == null) {
         this.hue = new DoublePropertyBase() {
            public void invalidated() {
               ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ColorAdjust.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ColorAdjust.this;
            }

            public String getName() {
               return "hue";
            }
         };
      }

      return this.hue;
   }

   public final void setSaturation(double var1) {
      this.saturationProperty().set(var1);
   }

   public final double getSaturation() {
      return this.saturation == null ? 0.0 : this.saturation.get();
   }

   public final DoubleProperty saturationProperty() {
      if (this.saturation == null) {
         this.saturation = new DoublePropertyBase() {
            public void invalidated() {
               ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ColorAdjust.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ColorAdjust.this;
            }

            public String getName() {
               return "saturation";
            }
         };
      }

      return this.saturation;
   }

   public final void setBrightness(double var1) {
      this.brightnessProperty().set(var1);
   }

   public final double getBrightness() {
      return this.brightness == null ? 0.0 : this.brightness.get();
   }

   public final DoubleProperty brightnessProperty() {
      if (this.brightness == null) {
         this.brightness = new DoublePropertyBase() {
            public void invalidated() {
               ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ColorAdjust.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ColorAdjust.this;
            }

            public String getName() {
               return "brightness";
            }
         };
      }

      return this.brightness;
   }

   public final void setContrast(double var1) {
      this.contrastProperty().set(var1);
   }

   public final double getContrast() {
      return this.contrast == null ? 0.0 : this.contrast.get();
   }

   public final DoubleProperty contrastProperty() {
      if (this.contrast == null) {
         this.contrast = new DoublePropertyBase() {
            public void invalidated() {
               ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               ColorAdjust.this.effectBoundsChanged();
            }

            public Object getBean() {
               return ColorAdjust.this;
            }

            public String getName() {
               return "contrast";
            }
         };
      }

      return this.contrast;
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      com.sun.scenario.effect.ColorAdjust var2 = (com.sun.scenario.effect.ColorAdjust)this.impl_getImpl();
      var2.setInput(var1 == null ? null : var1.impl_getImpl());
      var2.setHue((float)Utils.clamp(-1.0, this.getHue(), 1.0));
      var2.setSaturation((float)Utils.clamp(-1.0, this.getSaturation(), 1.0));
      var2.setBrightness((float)Utils.clamp(-1.0, this.getBrightness(), 1.0));
      var2.setContrast((float)Utils.clamp(-1.0, this.getContrast(), 1.0));
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      return getInputBounds(var1, var2, var3, var4, this.getInput());
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      ColorAdjust var1 = new ColorAdjust(this.getHue(), this.getSaturation(), this.getBrightness(), this.getContrast());
      var1.setInput(var1.getInput());
      return var1;
   }
}
