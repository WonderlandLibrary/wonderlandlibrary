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

public class Bloom extends Effect {
   private ObjectProperty input;
   private DoubleProperty threshold;

   public Bloom() {
   }

   public Bloom(double var1) {
      this.setThreshold(var1);
   }

   com.sun.scenario.effect.Bloom impl_createImpl() {
      return new com.sun.scenario.effect.Bloom();
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

   public final void setThreshold(double var1) {
      this.thresholdProperty().set(var1);
   }

   public final double getThreshold() {
      return this.threshold == null ? 0.3 : this.threshold.get();
   }

   public final DoubleProperty thresholdProperty() {
      if (this.threshold == null) {
         this.threshold = new DoublePropertyBase(0.3) {
            public void invalidated() {
               Bloom.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return Bloom.this;
            }

            public String getName() {
               return "threshold";
            }
         };
      }

      return this.threshold;
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      com.sun.scenario.effect.Bloom var2 = (com.sun.scenario.effect.Bloom)this.impl_getImpl();
      var2.setInput(var1 == null ? null : var1.impl_getImpl());
      var2.setThreshold((float)Utils.clamp(0.0, this.getThreshold(), 1.0));
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      return getInputBounds(var1, var2, var3, var4, this.getInput());
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      Bloom var1 = new Bloom(this.getThreshold());
      var1.setInput(this.getInput());
      return var1;
   }
}
