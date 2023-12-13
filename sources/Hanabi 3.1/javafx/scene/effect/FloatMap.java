package javafx.scene.effect;

import com.sun.javafx.util.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;

public class FloatMap {
   private com.sun.scenario.effect.FloatMap map;
   private float[] buf;
   private boolean mapBufferDirty = true;
   private BooleanProperty effectDirty;
   private IntegerProperty width;
   private IntegerProperty height;

   com.sun.scenario.effect.FloatMap getImpl() {
      return this.map;
   }

   private void updateBuffer() {
      if (this.getWidth() > 0 && this.getHeight() > 0) {
         int var1 = Utils.clampMax(this.getWidth(), 4096);
         int var2 = Utils.clampMax(this.getHeight(), 4096);
         int var3 = var1 * var2 * 4;
         this.buf = new float[var3];
         this.mapBufferDirty = true;
      }

   }

   private void impl_update() {
      if (this.mapBufferDirty) {
         this.map = new com.sun.scenario.effect.FloatMap(Utils.clamp(1, this.getWidth(), 4096), Utils.clamp(1, this.getHeight(), 4096));
         this.mapBufferDirty = false;
      }

      this.map.put(this.buf);
   }

   void impl_sync() {
      if (this.impl_isEffectDirty()) {
         this.impl_update();
         this.impl_clearDirty();
      }

   }

   private void setEffectDirty(boolean var1) {
      this.effectDirtyProperty().set(var1);
   }

   final BooleanProperty effectDirtyProperty() {
      if (this.effectDirty == null) {
         this.effectDirty = new SimpleBooleanProperty(this, "effectDirty");
      }

      return this.effectDirty;
   }

   /** @deprecated */
   @Deprecated
   boolean impl_isEffectDirty() {
      return this.effectDirty == null ? false : this.effectDirty.get();
   }

   private void impl_markDirty() {
      this.setEffectDirty(true);
   }

   private void impl_clearDirty() {
      this.setEffectDirty(false);
   }

   public FloatMap() {
      this.updateBuffer();
      this.impl_markDirty();
   }

   public FloatMap(int var1, int var2) {
      this.setWidth(var1);
      this.setHeight(var2);
      this.updateBuffer();
      this.impl_markDirty();
   }

   public final void setWidth(int var1) {
      this.widthProperty().set(var1);
   }

   public final int getWidth() {
      return this.width == null ? 1 : this.width.get();
   }

   public final IntegerProperty widthProperty() {
      if (this.width == null) {
         this.width = new IntegerPropertyBase(1) {
            public void invalidated() {
               FloatMap.this.updateBuffer();
               FloatMap.this.impl_markDirty();
            }

            public Object getBean() {
               return FloatMap.this;
            }

            public String getName() {
               return "width";
            }
         };
      }

      return this.width;
   }

   public final void setHeight(int var1) {
      this.heightProperty().set(var1);
   }

   public final int getHeight() {
      return this.height == null ? 1 : this.height.get();
   }

   public final IntegerProperty heightProperty() {
      if (this.height == null) {
         this.height = new IntegerPropertyBase(1) {
            public void invalidated() {
               FloatMap.this.updateBuffer();
               FloatMap.this.impl_markDirty();
            }

            public Object getBean() {
               return FloatMap.this;
            }

            public String getName() {
               return "height";
            }
         };
      }

      return this.height;
   }

   public void setSample(int var1, int var2, int var3, float var4) {
      this.buf[(var1 + var2 * this.getWidth()) * 4 + var3] = var4;
      this.impl_markDirty();
   }

   public void setSamples(int var1, int var2, float var3) {
      int var4 = (var1 + var2 * this.getWidth()) * 4;
      this.buf[var4 + 0] = var3;
      this.impl_markDirty();
   }

   public void setSamples(int var1, int var2, float var3, float var4) {
      int var5 = (var1 + var2 * this.getWidth()) * 4;
      this.buf[var5 + 0] = var3;
      this.buf[var5 + 1] = var4;
      this.impl_markDirty();
   }

   public void setSamples(int var1, int var2, float var3, float var4, float var5) {
      int var6 = (var1 + var2 * this.getWidth()) * 4;
      this.buf[var6 + 0] = var3;
      this.buf[var6 + 1] = var4;
      this.buf[var6 + 2] = var5;
      this.impl_markDirty();
   }

   public void setSamples(int var1, int var2, float var3, float var4, float var5, float var6) {
      int var7 = (var1 + var2 * this.getWidth()) * 4;
      this.buf[var7 + 0] = var3;
      this.buf[var7 + 1] = var4;
      this.buf[var7 + 2] = var5;
      this.buf[var7 + 3] = var6;
      this.impl_markDirty();
   }

   /** @deprecated */
   @Deprecated
   public FloatMap impl_copy() {
      FloatMap var1 = new FloatMap(this.getWidth(), this.getHeight());
      System.arraycopy(this.buf, 0, var1.buf, 0, this.buf.length);
      return var1;
   }
}
