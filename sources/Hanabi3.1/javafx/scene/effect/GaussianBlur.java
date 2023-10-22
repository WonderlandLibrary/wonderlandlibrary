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

public class GaussianBlur extends Effect {
   private ObjectProperty input;
   private DoubleProperty radius;

   public GaussianBlur() {
   }

   public GaussianBlur(double var1) {
      this.setRadius(var1);
   }

   com.sun.scenario.effect.GaussianBlur impl_createImpl() {
      return new com.sun.scenario.effect.GaussianBlur();
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
               GaussianBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               GaussianBlur.this.effectBoundsChanged();
            }

            public Object getBean() {
               return GaussianBlur.this;
            }

            public String getName() {
               return "radius";
            }
         };
      }

      return this.radius;
   }

   private float getClampedRadius() {
      return (float)Utils.clamp(0.0, this.getRadius(), 63.0);
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      com.sun.scenario.effect.GaussianBlur var2 = (com.sun.scenario.effect.GaussianBlur)this.impl_getImpl();
      var2.setRadius(this.getClampedRadius());
      var2.setInput(var1 == null ? null : var1.impl_getImpl());
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      var1 = getInputBounds(var1, BaseTransform.IDENTITY_TRANSFORM, var3, var4, this.getInput());
      float var5 = this.getClampedRadius();
      var1 = var1.deriveWithPadding(var5, var5, 0.0F);
      return transformBounds(var2, var1);
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      return new GaussianBlur(this.getRadius());
   }
}
