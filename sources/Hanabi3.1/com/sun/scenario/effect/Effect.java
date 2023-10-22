package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Effect {
   public static final Effect DefaultInput = null;
   private final List inputs;
   private final List unmodifiableInputs;
   private final int maxInputs;

   protected Effect() {
      this.inputs = Collections.emptyList();
      this.unmodifiableInputs = this.inputs;
      this.maxInputs = 0;
   }

   protected Effect(Effect var1) {
      this.inputs = new ArrayList(1);
      this.unmodifiableInputs = Collections.unmodifiableList(this.inputs);
      this.maxInputs = 1;
      this.setInput(0, var1);
   }

   protected Effect(Effect var1, Effect var2) {
      this.inputs = new ArrayList(2);
      this.unmodifiableInputs = Collections.unmodifiableList(this.inputs);
      this.maxInputs = 2;
      this.setInput(0, var1);
      this.setInput(1, var2);
   }

   Object getState() {
      return null;
   }

   public int getNumInputs() {
      return this.inputs.size();
   }

   public final List getInputs() {
      return this.unmodifiableInputs;
   }

   protected void setInput(int var1, Effect var2) {
      if (var1 >= 0 && var1 < this.maxInputs) {
         if (var1 < this.inputs.size()) {
            this.inputs.set(var1, var2);
         } else {
            this.inputs.add(var2);
         }

      } else {
         throw new IllegalArgumentException("Index must be within allowable range");
      }
   }

   public static BaseBounds combineBounds(BaseBounds... var0) {
      Object var1 = null;
      if (var0.length == 1) {
         var1 = var0[0];
      } else {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            BaseBounds var3 = var0[var2];
            if (var3 != null && !var3.isEmpty()) {
               if (var1 == null) {
                  RectBounds var4 = new RectBounds();
                  var1 = var4.deriveWithNewBounds(var3);
               } else {
                  var1 = ((BaseBounds)var1).deriveWithUnion(var3);
               }
            }
         }
      }

      if (var1 == null) {
         var1 = new RectBounds();
      }

      return (BaseBounds)var1;
   }

   public static Rectangle combineBounds(Rectangle... var0) {
      Rectangle var1 = null;
      if (var0.length == 1) {
         var1 = var0[0];
      } else {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            Rectangle var3 = var0[var2];
            if (var3 != null && !var3.isEmpty()) {
               if (var1 == null) {
                  var1 = new Rectangle(var3);
               } else {
                  var1.add(var3);
               }
            }
         }
      }

      if (var1 == null) {
         var1 = new Rectangle();
      }

      return var1;
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      int var4 = var3.length;
      Rectangle[] var5 = new Rectangle[var4];

      for(int var6 = 0; var6 < var4; ++var6) {
         var5[var6] = var3[var6].getTransformedBounds(var2);
      }

      Rectangle var7 = combineBounds(var5);
      return var7;
   }

   public abstract ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5);

   public static BaseBounds transformBounds(BaseTransform var0, BaseBounds var1) {
      if (var0 != null && !var0.isIdentity()) {
         RectBounds var2 = new RectBounds();
         BaseBounds var3 = var0.transform((BaseBounds)var1, (BaseBounds)var2);
         return var3;
      } else {
         return var1;
      }
   }

   protected ImageData ensureTransform(FilterContext var1, ImageData var2, BaseTransform var3, Rectangle var4) {
      if (var3 != null && !var3.isIdentity()) {
         if (!var2.validate(var1)) {
            var2.unref();
            return new ImageData(var1, (Filterable)null, new Rectangle());
         } else {
            return var2.transform(var3);
         }
      } else {
         return var2;
      }
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      DirtyRegionContainer var3 = null;

      for(int var4 = 0; var4 < this.inputs.size(); ++var4) {
         DirtyRegionContainer var5 = this.getDefaultedInput(var4, var1).getDirtyRegions(var1, var2);
         if (var3 == null) {
            var3 = var5;
         } else {
            var3.merge(var5);
            var2.checkIn(var5);
         }
      }

      if (var3 == null) {
         var3 = var2.checkOut();
      }

      return var3;
   }

   Effect getDefaultedInput(int var1, Effect var2) {
      return getDefaultedInput((Effect)this.inputs.get(var1), var2);
   }

   static Effect getDefaultedInput(Effect var0, Effect var1) {
      return var0 == null ? var1 : var0;
   }

   public abstract BaseBounds getBounds(BaseTransform var1, Effect var2);

   public Point2D transform(Point2D var1, Effect var2) {
      return var1;
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return var1;
   }

   public static Filterable createCompatibleImage(FilterContext var0, int var1, int var2) {
      return Renderer.getRenderer(var0).createCompatibleImage(var1, var2);
   }

   public static Filterable getCompatibleImage(FilterContext var0, int var1, int var2) {
      return Renderer.getRenderer(var0).getCompatibleImage(var1, var2);
   }

   public static void releaseCompatibleImage(FilterContext var0, Filterable var1) {
      Renderer.getRenderer(var0).releaseCompatibleImage(var1);
   }

   public abstract boolean reducesOpaquePixels();

   public abstract AccelType getAccelType(FilterContext var1);

   static {
      AccessHelper.setStateAccessor((var0) -> {
         return var0.getState();
      });
   }

   public static enum AccelType {
      INTRINSIC("Intrinsic"),
      NONE("CPU/Java"),
      SIMD("CPU/SIMD"),
      FIXED("CPU/Fixed"),
      OPENGL("OpenGL"),
      DIRECT3D("Direct3D");

      private String text;

      private AccelType(String var3) {
         this.text = var3;
      }

      public String toString() {
         return this.text;
      }
   }
}
