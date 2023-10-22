package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

public class PerspectiveTransform extends Effect {
   private ObjectProperty input;
   private DoubleProperty ulx;
   private DoubleProperty uly;
   private DoubleProperty urx;
   private DoubleProperty ury;
   private DoubleProperty lrx;
   private DoubleProperty lry;
   private DoubleProperty llx;
   private DoubleProperty lly;
   private float[] devcoords = new float[8];

   public PerspectiveTransform() {
   }

   public PerspectiveTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.setUlx(var1);
      this.setUly(var3);
      this.setUrx(var5);
      this.setUry(var7);
      this.setLlx(var13);
      this.setLly(var15);
      this.setLrx(var9);
      this.setLry(var11);
   }

   private void updateXform() {
      ((com.sun.scenario.effect.PerspectiveTransform)this.impl_getImpl()).setQuadMapping((float)this.getUlx(), (float)this.getUly(), (float)this.getUrx(), (float)this.getUry(), (float)this.getLrx(), (float)this.getLry(), (float)this.getLlx(), (float)this.getLly());
   }

   com.sun.scenario.effect.PerspectiveTransform impl_createImpl() {
      return new com.sun.scenario.effect.PerspectiveTransform();
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

   public final void setUlx(double var1) {
      this.ulxProperty().set(var1);
   }

   public final double getUlx() {
      return this.ulx == null ? 0.0 : this.ulx.get();
   }

   public final DoubleProperty ulxProperty() {
      if (this.ulx == null) {
         this.ulx = new DoublePropertyBase() {
            public void invalidated() {
               PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               PerspectiveTransform.this.effectBoundsChanged();
            }

            public Object getBean() {
               return PerspectiveTransform.this;
            }

            public String getName() {
               return "ulx";
            }
         };
      }

      return this.ulx;
   }

   public final void setUly(double var1) {
      this.ulyProperty().set(var1);
   }

   public final double getUly() {
      return this.uly == null ? 0.0 : this.uly.get();
   }

   public final DoubleProperty ulyProperty() {
      if (this.uly == null) {
         this.uly = new DoublePropertyBase() {
            public void invalidated() {
               PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               PerspectiveTransform.this.effectBoundsChanged();
            }

            public Object getBean() {
               return PerspectiveTransform.this;
            }

            public String getName() {
               return "uly";
            }
         };
      }

      return this.uly;
   }

   public final void setUrx(double var1) {
      this.urxProperty().set(var1);
   }

   public final double getUrx() {
      return this.urx == null ? 0.0 : this.urx.get();
   }

   public final DoubleProperty urxProperty() {
      if (this.urx == null) {
         this.urx = new DoublePropertyBase() {
            public void invalidated() {
               PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               PerspectiveTransform.this.effectBoundsChanged();
            }

            public Object getBean() {
               return PerspectiveTransform.this;
            }

            public String getName() {
               return "urx";
            }
         };
      }

      return this.urx;
   }

   public final void setUry(double var1) {
      this.uryProperty().set(var1);
   }

   public final double getUry() {
      return this.ury == null ? 0.0 : this.ury.get();
   }

   public final DoubleProperty uryProperty() {
      if (this.ury == null) {
         this.ury = new DoublePropertyBase() {
            public void invalidated() {
               PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               PerspectiveTransform.this.effectBoundsChanged();
            }

            public Object getBean() {
               return PerspectiveTransform.this;
            }

            public String getName() {
               return "ury";
            }
         };
      }

      return this.ury;
   }

   public final void setLrx(double var1) {
      this.lrxProperty().set(var1);
   }

   public final double getLrx() {
      return this.lrx == null ? 0.0 : this.lrx.get();
   }

   public final DoubleProperty lrxProperty() {
      if (this.lrx == null) {
         this.lrx = new DoublePropertyBase() {
            public void invalidated() {
               PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               PerspectiveTransform.this.effectBoundsChanged();
            }

            public Object getBean() {
               return PerspectiveTransform.this;
            }

            public String getName() {
               return "lrx";
            }
         };
      }

      return this.lrx;
   }

   public final void setLry(double var1) {
      this.lryProperty().set(var1);
   }

   public final double getLry() {
      return this.lry == null ? 0.0 : this.lry.get();
   }

   public final DoubleProperty lryProperty() {
      if (this.lry == null) {
         this.lry = new DoublePropertyBase() {
            public void invalidated() {
               PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               PerspectiveTransform.this.effectBoundsChanged();
            }

            public Object getBean() {
               return PerspectiveTransform.this;
            }

            public String getName() {
               return "lry";
            }
         };
      }

      return this.lry;
   }

   public final void setLlx(double var1) {
      this.llxProperty().set(var1);
   }

   public final double getLlx() {
      return this.llx == null ? 0.0 : this.llx.get();
   }

   public final DoubleProperty llxProperty() {
      if (this.llx == null) {
         this.llx = new DoublePropertyBase() {
            public void invalidated() {
               PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               PerspectiveTransform.this.effectBoundsChanged();
            }

            public Object getBean() {
               return PerspectiveTransform.this;
            }

            public String getName() {
               return "llx";
            }
         };
      }

      return this.llx;
   }

   public final void setLly(double var1) {
      this.llyProperty().set(var1);
   }

   public final double getLly() {
      return this.lly == null ? 0.0 : this.lly.get();
   }

   public final DoubleProperty llyProperty() {
      if (this.lly == null) {
         this.lly = new DoublePropertyBase() {
            public void invalidated() {
               PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               PerspectiveTransform.this.effectBoundsChanged();
            }

            public Object getBean() {
               return PerspectiveTransform.this;
            }

            public String getName() {
               return "lly";
            }
         };
      }

      return this.lly;
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      ((com.sun.scenario.effect.PerspectiveTransform)this.impl_getImpl()).setInput(var1 == null ? null : var1.impl_getImpl());
      this.updateXform();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      this.setupDevCoords(var2);
      float var7;
      float var5 = var7 = this.devcoords[0];
      float var8;
      float var6 = var8 = this.devcoords[1];

      for(int var9 = 2; var9 < this.devcoords.length; var9 += 2) {
         if (var5 > this.devcoords[var9]) {
            var5 = this.devcoords[var9];
         } else if (var7 < this.devcoords[var9]) {
            var7 = this.devcoords[var9];
         }

         if (var6 > this.devcoords[var9 + 1]) {
            var6 = this.devcoords[var9 + 1];
         } else if (var8 < this.devcoords[var9 + 1]) {
            var8 = this.devcoords[var9 + 1];
         }
      }

      return new RectBounds(var5, var6, var7, var8);
   }

   private void setupDevCoords(BaseTransform var1) {
      this.devcoords[0] = (float)this.getUlx();
      this.devcoords[1] = (float)this.getUly();
      this.devcoords[2] = (float)this.getUrx();
      this.devcoords[3] = (float)this.getUry();
      this.devcoords[4] = (float)this.getLrx();
      this.devcoords[5] = (float)this.getLry();
      this.devcoords[6] = (float)this.getLlx();
      this.devcoords[7] = (float)this.getLly();
      var1.transform((float[])this.devcoords, 0, (float[])this.devcoords, 0, 4);
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      return new PerspectiveTransform(this.getUlx(), this.getUly(), this.getUrx(), this.getUry(), this.getLrx(), this.getLry(), this.getLlx(), this.getLly());
   }
}
