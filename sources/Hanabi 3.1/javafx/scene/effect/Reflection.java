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

public class Reflection extends Effect {
   private ObjectProperty input;
   private DoubleProperty topOffset;
   private DoubleProperty topOpacity;
   private DoubleProperty bottomOpacity;
   private DoubleProperty fraction;

   public Reflection() {
   }

   public Reflection(double var1, double var3, double var5, double var7) {
      this.setBottomOpacity(var7);
      this.setTopOffset(var1);
      this.setTopOpacity(var5);
      this.setFraction(var3);
   }

   com.sun.scenario.effect.Reflection impl_createImpl() {
      return new com.sun.scenario.effect.Reflection();
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

   public final void setTopOffset(double var1) {
      this.topOffsetProperty().set(var1);
   }

   public final double getTopOffset() {
      return this.topOffset == null ? 0.0 : this.topOffset.get();
   }

   public final DoubleProperty topOffsetProperty() {
      if (this.topOffset == null) {
         this.topOffset = new DoublePropertyBase() {
            public void invalidated() {
               Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               Reflection.this.effectBoundsChanged();
            }

            public Object getBean() {
               return Reflection.this;
            }

            public String getName() {
               return "topOffset";
            }
         };
      }

      return this.topOffset;
   }

   public final void setTopOpacity(double var1) {
      this.topOpacityProperty().set(var1);
   }

   public final double getTopOpacity() {
      return this.topOpacity == null ? 0.5 : this.topOpacity.get();
   }

   public final DoubleProperty topOpacityProperty() {
      if (this.topOpacity == null) {
         this.topOpacity = new DoublePropertyBase(0.5) {
            public void invalidated() {
               Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return Reflection.this;
            }

            public String getName() {
               return "topOpacity";
            }
         };
      }

      return this.topOpacity;
   }

   public final void setBottomOpacity(double var1) {
      this.bottomOpacityProperty().set(var1);
   }

   public final double getBottomOpacity() {
      return this.bottomOpacity == null ? 0.0 : this.bottomOpacity.get();
   }

   public final DoubleProperty bottomOpacityProperty() {
      if (this.bottomOpacity == null) {
         this.bottomOpacity = new DoublePropertyBase() {
            public void invalidated() {
               Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return Reflection.this;
            }

            public String getName() {
               return "bottomOpacity";
            }
         };
      }

      return this.bottomOpacity;
   }

   public final void setFraction(double var1) {
      this.fractionProperty().set(var1);
   }

   public final double getFraction() {
      return this.fraction == null ? 0.75 : this.fraction.get();
   }

   public final DoubleProperty fractionProperty() {
      if (this.fraction == null) {
         this.fraction = new DoublePropertyBase(0.75) {
            public void invalidated() {
               Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               Reflection.this.effectBoundsChanged();
            }

            public Object getBean() {
               return Reflection.this;
            }

            public String getName() {
               return "fraction";
            }
         };
      }

      return this.fraction;
   }

   private float getClampedFraction() {
      return (float)Utils.clamp(0.0, this.getFraction(), 1.0);
   }

   private float getClampedBottomOpacity() {
      return (float)Utils.clamp(0.0, this.getBottomOpacity(), 1.0);
   }

   private float getClampedTopOpacity() {
      return (float)Utils.clamp(0.0, this.getTopOpacity(), 1.0);
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      com.sun.scenario.effect.Reflection var2 = (com.sun.scenario.effect.Reflection)this.impl_getImpl();
      var2.setInput(var1 == null ? null : var1.impl_getImpl());
      var2.setFraction(this.getClampedFraction());
      var2.setTopOffset((float)this.getTopOffset());
      var2.setBottomOpacity(this.getClampedBottomOpacity());
      var2.setTopOpacity(this.getClampedTopOpacity());
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      var1 = getInputBounds(var1, BaseTransform.IDENTITY_TRANSFORM, var3, var4, this.getInput());
      var1.roundOut();
      float var5 = var1.getMinX();
      float var6 = var1.getMaxY() + (float)this.getTopOffset();
      float var7 = var1.getMinZ();
      float var8 = var1.getMaxX();
      float var9 = var6 + this.getClampedFraction() * var1.getHeight();
      float var10 = var1.getMaxZ();
      BaseBounds var11 = BaseBounds.getInstance(var5, var6, var7, var8, var9, var10);
      var11 = var11.deriveWithUnion(var1);
      return transformBounds(var2, var11);
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      Reflection var1 = new Reflection(this.getTopOffset(), this.getFraction(), this.getTopOpacity(), this.getBottomOpacity());
      var1.setInput(var1.getInput());
      return var1;
   }
}
