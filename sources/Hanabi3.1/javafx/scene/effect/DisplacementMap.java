package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;

public class DisplacementMap extends Effect {
   private ObjectProperty input;
   private final FloatMap defaultMap = new FloatMap(1, 1);
   private ObjectProperty mapData;
   private final MapDataChangeListener mapDataChangeListener = new MapDataChangeListener();
   private DoubleProperty scaleX;
   private DoubleProperty scaleY;
   private DoubleProperty offsetX;
   private DoubleProperty offsetY;
   private BooleanProperty wrap;

   com.sun.scenario.effect.DisplacementMap impl_createImpl() {
      return new com.sun.scenario.effect.DisplacementMap(new com.sun.scenario.effect.FloatMap(1, 1), com.sun.scenario.effect.Effect.DefaultInput);
   }

   public DisplacementMap() {
      this.setMapData(new FloatMap(1, 1));
   }

   public DisplacementMap(FloatMap var1) {
      this.setMapData(var1);
   }

   public DisplacementMap(FloatMap var1, double var2, double var4, double var6, double var8) {
      this.setMapData(var1);
      this.setOffsetX(var2);
      this.setOffsetY(var4);
      this.setScaleX(var6);
      this.setScaleY(var8);
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

   public final void setMapData(FloatMap var1) {
      this.mapDataProperty().set(var1);
   }

   public final FloatMap getMapData() {
      return this.mapData == null ? null : (FloatMap)this.mapData.get();
   }

   public final ObjectProperty mapDataProperty() {
      if (this.mapData == null) {
         this.mapData = new ObjectPropertyBase() {
            public void invalidated() {
               DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
               DisplacementMap.this.effectBoundsChanged();
            }

            public Object getBean() {
               return DisplacementMap.this;
            }

            public String getName() {
               return "mapData";
            }
         };
      }

      return this.mapData;
   }

   public final void setScaleX(double var1) {
      this.scaleXProperty().set(var1);
   }

   public final double getScaleX() {
      return this.scaleX == null ? 1.0 : this.scaleX.get();
   }

   public final DoubleProperty scaleXProperty() {
      if (this.scaleX == null) {
         this.scaleX = new DoublePropertyBase(1.0) {
            public void invalidated() {
               DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return DisplacementMap.this;
            }

            public String getName() {
               return "scaleX";
            }
         };
      }

      return this.scaleX;
   }

   public final void setScaleY(double var1) {
      this.scaleYProperty().set(var1);
   }

   public final double getScaleY() {
      return this.scaleY == null ? 1.0 : this.scaleY.get();
   }

   public final DoubleProperty scaleYProperty() {
      if (this.scaleY == null) {
         this.scaleY = new DoublePropertyBase(1.0) {
            public void invalidated() {
               DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return DisplacementMap.this;
            }

            public String getName() {
               return "scaleY";
            }
         };
      }

      return this.scaleY;
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
               DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return DisplacementMap.this;
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
               DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return DisplacementMap.this;
            }

            public String getName() {
               return "offsetY";
            }
         };
      }

      return this.offsetY;
   }

   public final void setWrap(boolean var1) {
      this.wrapProperty().set(var1);
   }

   public final boolean isWrap() {
      return this.wrap == null ? false : this.wrap.get();
   }

   public final BooleanProperty wrapProperty() {
      if (this.wrap == null) {
         this.wrap = new BooleanPropertyBase() {
            public void invalidated() {
               DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }

            public Object getBean() {
               return DisplacementMap.this;
            }

            public String getName() {
               return "wrap";
            }
         };
      }

      return this.wrap;
   }

   void impl_update() {
      Effect var1 = this.getInput();
      if (var1 != null) {
         var1.impl_sync();
      }

      com.sun.scenario.effect.DisplacementMap var2 = (com.sun.scenario.effect.DisplacementMap)this.impl_getImpl();
      var2.setContentInput(var1 == null ? null : var1.impl_getImpl());
      FloatMap var3 = this.getMapData();
      this.mapDataChangeListener.register(var3);
      if (var3 != null) {
         var3.impl_sync();
         var2.setMapData(var3.getImpl());
      } else {
         this.defaultMap.impl_sync();
         var2.setMapData(this.defaultMap.getImpl());
      }

      var2.setScaleX((float)this.getScaleX());
      var2.setScaleY((float)this.getScaleY());
      var2.setOffsetX((float)this.getOffsetX());
      var2.setOffsetY((float)this.getOffsetY());
      var2.setWrap(this.isWrap());
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_getBounds(BaseBounds var1, BaseTransform var2, Node var3, BoundsAccessor var4) {
      var1 = getInputBounds(var1, BaseTransform.IDENTITY_TRANSFORM, var3, var4, this.getInput());
      return transformBounds(var2, var1);
   }

   /** @deprecated */
   @Deprecated
   public Effect impl_copy() {
      DisplacementMap var1 = new DisplacementMap(this.getMapData().impl_copy(), this.getOffsetX(), this.getOffsetY(), this.getScaleX(), this.getScaleY());
      var1.setInput(this.getInput());
      return var1;
   }

   private class MapDataChangeListener extends EffectChangeListener {
      FloatMap mapData;

      private MapDataChangeListener() {
      }

      public void register(FloatMap var1) {
         this.mapData = var1;
         super.register(this.mapData == null ? null : this.mapData.effectDirtyProperty());
      }

      public void invalidated(Observable var1) {
         if (this.mapData.impl_isEffectDirty()) {
            DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            DisplacementMap.this.effectBoundsChanged();
         }

      }

      // $FF: synthetic method
      MapDataChangeListener(Object var2) {
         this();
      }
   }
}
