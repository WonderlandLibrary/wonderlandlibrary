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

public class Glow extends Effect {
   private ObjectProperty input;
   private DoubleProperty level;

   public Glow() {
   }

   public Glow(double var1) {
      this.setLevel(var1);
   }

   com.sun.scenario.effect.Glow impl_createImpl() {
      return new com.sun.scenario.effect.Glow();
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

   public final void setLevel(double var1) {
      this.levelProperty().set(var1);
   }

   public final double getLevel() {
      return this.level == null ? 0.3 : this.level.get();
   }

   public final DoubleProperty levelProperty() {
      if (this.level == null) {
         this.level = new DoublePropertyBase(0.3) {
            public void invalidated() {
               Glow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return Glow.this;
            }

            public String getName() {
               return "level";
            }
         };
      }

      return this.level;
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      com.sun.scenario.effect.Glow var2 = (com.sun.scenario.effect.Glow)this.impl_getImpl();
      var2.setInput(var1 == null ? null : var1.impl_getImpl());
      var2.setLevel((float)Utils.clamp(0.0, this.getLevel(), 1.0));
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      return getInputBounds(var1, var2, var3, var4, this.getInput());
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      return new Glow(this.getLevel());
   }
}
