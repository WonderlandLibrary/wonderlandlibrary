package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

public class BoxBlur extends Effect {
   private ObjectProperty input;
   private DoubleProperty width;
   private DoubleProperty height;
   private IntegerProperty iterations;

   public BoxBlur() {
   }

   public BoxBlur(double var1, double var3, int var5) {
      this.setWidth(var1);
      this.setHeight(var3);
      this.setIterations(var5);
   }

   com.sun.scenario.effect.BoxBlur impl_createImpl() {
      return new com.sun.scenario.effect.BoxBlur();
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

   public final void setWidth(double var1) {
      this.widthProperty().set(var1);
   }

   public final double getWidth() {
      return this.width == null ? 5.0 : this.width.get();
   }

   public final DoubleProperty widthProperty() {
      if (this.width == null) {
         this.width = new DoublePropertyBase(5.0) {
            public void invalidated() {
               BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               BoxBlur.this.effectBoundsChanged();
            }

            public Object getBean() {
               return BoxBlur.this;
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
      return this.height == null ? 5.0 : this.height.get();
   }

   public final DoubleProperty heightProperty() {
      if (this.height == null) {
         this.height = new DoublePropertyBase(5.0) {
            public void invalidated() {
               BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               BoxBlur.this.effectBoundsChanged();
            }

            public Object getBean() {
               return BoxBlur.this;
            }

            public String getName() {
               return "height";
            }
         };
      }

      return this.height;
   }

   public final void setIterations(int var1) {
      this.iterationsProperty().set(var1);
   }

   public final int getIterations() {
      return this.iterations == null ? 1 : this.iterations.get();
   }

   public final IntegerProperty iterationsProperty() {
      if (this.iterations == null) {
         this.iterations = new IntegerPropertyBase(1) {
            public void invalidated() {
               BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               BoxBlur.this.effectBoundsChanged();
            }

            public Object getBean() {
               return BoxBlur.this;
            }

            public String getName() {
               return "iterations";
            }
         };
      }

      return this.iterations;
   }

   private int getClampedWidth() {
      return Utils.clamp(0, (int)this.getWidth(), 255);
   }

   private int getClampedHeight() {
      return Utils.clamp(0, (int)this.getHeight(), 255);
   }

   private int getClampedIterations() {
      return Utils.clamp(0, this.getIterations(), 3);
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      com.sun.scenario.effect.BoxBlur var2 = (com.sun.scenario.effect.BoxBlur)this.impl_getImpl();
      var2.setInput(var1 == null ? null : var1.impl_getImpl());
      var2.setHorizontalSize(this.getClampedWidth());
      var2.setVerticalSize(this.getClampedHeight());
      var2.setPasses(this.getClampedIterations());
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      var1 = getInputBounds(var1, BaseTransform.IDENTITY_TRANSFORM, var3, var4, this.getInput());
      int var5 = this.getClampedIterations();
      int var6 = getKernelSize((float)this.getClampedWidth(), var5);
      int var7 = getKernelSize((float)this.getClampedHeight(), var5);
      var1 = var1.deriveWithPadding((float)var6, (float)var7, 0.0F);
      return transformBounds(var2, var1);
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      BoxBlur var1 = new BoxBlur(this.getWidth(), this.getHeight(), this.getIterations());
      var1.setInput(this.getInput());
      return var1;
   }
}
