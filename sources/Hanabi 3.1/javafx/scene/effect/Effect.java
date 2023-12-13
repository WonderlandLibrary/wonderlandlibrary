package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;

public abstract class Effect {
   private com.sun.scenario.effect.Effect peer;
   private IntegerProperty effectDirty = new SimpleIntegerProperty(this, "effectDirty");

   protected Effect() {
      this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
   }

   void effectBoundsChanged() {
      this.toggleDirty(EffectDirtyBits.BOUNDS_CHANGED);
   }

   abstract com.sun.scenario.effect.Effect impl_createImpl();

   /** @deprecated */
   @Deprecated
   public com.sun.scenario.effect.Effect impl_getImpl() {
      if (this.peer == null) {
         this.peer = this.impl_createImpl();
      }

      return this.peer;
   }

   private void setEffectDirty(int var1) {
      this.impl_effectDirtyProperty().set(var1);
   }

   /** @deprecated */
   @Deprecated
   public final IntegerProperty impl_effectDirtyProperty() {
      return this.effectDirty;
   }

   /** @deprecated */
   @Deprecated
   public final boolean impl_isEffectDirty() {
      return this.isEffectDirty(EffectDirtyBits.EFFECT_DIRTY);
   }

   final void markDirty(EffectDirtyBits var1) {
      this.setEffectDirty(this.effectDirty.get() | var1.getMask());
   }

   private void toggleDirty(EffectDirtyBits var1) {
      this.setEffectDirty(this.effectDirty.get() ^ var1.getMask());
   }

   private boolean isEffectDirty(EffectDirtyBits var1) {
      return (this.effectDirty.get() & var1.getMask()) != 0;
   }

   private void clearEffectDirty(EffectDirtyBits var1) {
      this.setEffectDirty(this.effectDirty.get() & ~var1.getMask());
   }

   /** @deprecated */
   @Deprecated
   public final void impl_sync() {
      if (this.isEffectDirty(EffectDirtyBits.EFFECT_DIRTY)) {
         this.impl_update();
         this.clearEffectDirty(EffectDirtyBits.EFFECT_DIRTY);
      }

   }

   abstract void impl_update();

   abstract boolean impl_checkChainContains(Effect var1);

   boolean impl_containsCycles(Effect var1) {
      return var1 != null && (var1 == this || var1.impl_checkChainContains(this));
   }

   /** @deprecated */
   @Deprecated
   public abstract BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4);

   /** @deprecated */
   @Deprecated
   public abstract Effect impl_copy();

   static BaseBounds transformBounds(BaseTransform var0, BaseBounds var1) {
      if (var0 != null && !var0.isIdentity()) {
         RectBounds var2 = new RectBounds();
         BaseBounds var3 = var0.transform((BaseBounds)var1, (BaseBounds)var2);
         return var3;
      } else {
         return var1;
      }
   }

   static int getKernelSize(float var0, int var1) {
      int var2 = (int)Math.ceil((double)var0);
      if (var2 < 1) {
         var2 = 1;
      }

      var2 = (var2 - 1) * var1 + 1;
      var2 |= 1;
      return var2 / 2;
   }

   static BaseBounds getShadowBounds(BaseBounds var0, BaseTransform var1, float var2, float var3, BlurType var4) {
      int var5 = 0;
      int var6 = 0;
      switch (var4) {
         case GAUSSIAN:
            float var7 = var2 < 1.0F ? 0.0F : (var2 - 1.0F) / 2.0F;
            float var8 = var3 < 1.0F ? 0.0F : (var3 - 1.0F) / 2.0F;
            var5 = (int)Math.ceil((double)var7);
            var6 = (int)Math.ceil((double)var8);
            break;
         case ONE_PASS_BOX:
            var5 = getKernelSize((float)Math.round(var2 / 3.0F), 1);
            var6 = getKernelSize((float)Math.round(var3 / 3.0F), 1);
            break;
         case TWO_PASS_BOX:
            var5 = getKernelSize((float)Math.round(var2 / 3.0F), 2);
            var6 = getKernelSize((float)Math.round(var3 / 3.0F), 2);
            break;
         case THREE_PASS_BOX:
            var5 = getKernelSize((float)Math.round(var2 / 3.0F), 3);
            var6 = getKernelSize((float)Math.round(var3 / 3.0F), 3);
      }

      var0 = var0.deriveWithPadding((float)var5, (float)var6, 0.0F);
      return transformBounds(var1, var0);
   }

   static BaseBounds getInputBounds(BaseBounds var0, BaseTransform var1, Node var2, BoundsAccessor var3, Effect var4) {
      if (var4 != null) {
         var0 = var4.impl_getBounds(var0, var1, var2, var3);
      } else {
         var0 = var3.getGeomBounds(var0, var1, var2);
      }

      return var0;
   }

   class EffectInputProperty extends ObjectPropertyBase {
      private final String propertyName;
      private Effect validInput = null;
      private final EffectInputChangeListener effectChangeListener = Effect.this.new EffectInputChangeListener();

      public EffectInputProperty(String var2) {
         this.propertyName = var2;
      }

      public void invalidated() {
         Effect var1 = (Effect)super.get();
         if (Effect.this.impl_containsCycles(var1)) {
            if (this.isBound()) {
               this.unbind();
               this.set(this.validInput);
               throw new IllegalArgumentException("Cycle in effect chain detected, binding was set to incorrect value, unbinding the input property");
            } else {
               this.set(this.validInput);
               throw new IllegalArgumentException("Cycle in effect chain detected");
            }
         } else {
            this.validInput = var1;
            this.effectChangeListener.register(var1);
            Effect.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            Effect.this.effectBoundsChanged();
         }
      }

      public Object getBean() {
         return Effect.this;
      }

      public String getName() {
         return this.propertyName;
      }
   }

   class EffectInputChangeListener extends EffectChangeListener {
      private int oldBits;

      public void register(Effect var1) {
         super.register(var1 == null ? null : var1.impl_effectDirtyProperty());
         if (var1 != null) {
            this.oldBits = var1.impl_effectDirtyProperty().get();
         }

      }

      public void invalidated(Observable var1) {
         int var2 = ((IntegerProperty)var1).get();
         int var3 = var2 ^ this.oldBits;
         this.oldBits = var2;
         if (EffectDirtyBits.isSet(var3, EffectDirtyBits.EFFECT_DIRTY) && EffectDirtyBits.isSet(var2, EffectDirtyBits.EFFECT_DIRTY)) {
            Effect.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
         }

         if (EffectDirtyBits.isSet(var3, EffectDirtyBits.BOUNDS_CHANGED)) {
            Effect.this.toggleDirty(EffectDirtyBits.BOUNDS_CHANGED);
         }

      }
   }
}
