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

public class MotionBlur extends Effect {
   private ObjectProperty input;
   private DoubleProperty radius;
   private DoubleProperty angle;

   public MotionBlur() {
   }

   public MotionBlur(double var1, double var3) {
      this.setAngle(var1);
      this.setRadius(var3);
   }

   com.sun.scenario.effect.MotionBlur impl_createImpl() {
      return new com.sun.scenario.effect.MotionBlur();
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
               MotionBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               MotionBlur.this.effectBoundsChanged();
            }

            public Object getBean() {
               return MotionBlur.this;
            }

            public String getName() {
               return "radius";
            }
         };
      }

      return this.radius;
   }

   public final void setAngle(double var1) {
      this.angleProperty().set(var1);
   }

   public final double getAngle() {
      return this.angle == null ? 0.0 : this.angle.get();
   }

   public final DoubleProperty angleProperty() {
      if (this.angle == null) {
         this.angle = new DoublePropertyBase() {
            public void invalidated() {
               MotionBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               MotionBlur.this.effectBoundsChanged();
            }

            public Object getBean() {
               return MotionBlur.this;
            }

            public String getName() {
               return "angle";
            }
         };
      }

      return this.angle;
   }

   private float getClampedRadius() {
      return (float)Utils.clamp(0.0, this.getRadius(), 63.0);
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      com.sun.scenario.effect.MotionBlur var2 = (com.sun.scenario.effect.MotionBlur)this.impl_getImpl();
      var2.setInput(var1 == null ? null : var1.impl_getImpl());
      var2.setRadius(this.getClampedRadius());
      var2.setAngle((float)Math.toRadians(this.getAngle()));
   }

   private int getHPad() {
      return (int)Math.ceil(Math.abs(Math.cos(Math.toRadians(this.getAngle()))) * (double)this.getClampedRadius());
   }

   private int getVPad() {
      return (int)Math.ceil(Math.abs(Math.sin(Math.toRadians(this.getAngle()))) * (double)this.getClampedRadius());
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      var1 = getInputBounds(var1, BaseTransform.IDENTITY_TRANSFORM, var3, var4, this.getInput());
      int var5 = this.getHPad();
      int var6 = this.getVPad();
      var1 = var1.deriveWithPadding((float)var5, (float)var6, 0.0F);
      return transformBounds(var2, var1);
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      MotionBlur var1 = new MotionBlur(this.getAngle(), this.getRadius());
      var1.setInput(var1.getInput());
      return var1;
   }
}
