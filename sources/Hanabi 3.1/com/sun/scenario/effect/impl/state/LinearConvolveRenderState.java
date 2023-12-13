package com.sun.scenario.effect.impl.state;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.Rectangle;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import java.nio.FloatBuffer;
import java.security.AccessController;

public abstract class LinearConvolveRenderState implements RenderState {
   public static final int MAX_COMPILED_KERNEL_SIZE = 128;
   public static final int MAX_KERNEL_SIZE;
   static final float MIN_EFFECT_RADIUS = 0.00390625F;
   static final float[] BLACK_COMPONENTS;

   public static int getPeerSize(int var0) {
      if (var0 < 32) {
         return var0 + 3 & -4;
      } else if (var0 <= MAX_KERNEL_SIZE) {
         return var0 + 31 & -32;
      } else {
         throw new RuntimeException("No peer available for kernel size: " + var0);
      }
   }

   static boolean nearZero(float var0, int var1) {
      return (double)Math.abs(var0 * (float)var1) < 0.001953125;
   }

   static boolean nearOne(float var0, int var1) {
      return (double)Math.abs(var0 * (float)var1 - (float)var1) < 0.001953125;
   }

   public abstract boolean isShadow();

   public abstract Color4f getShadowColor();

   public abstract int getInputKernelSize(int var1);

   public abstract boolean isNop();

   public abstract ImageData validatePassInput(ImageData var1, int var2);

   public abstract boolean isPassNop();

   public EffectPeer getPassPeer(Renderer var1, FilterContext var2) {
      if (this.isPassNop()) {
         return null;
      } else {
         int var3 = this.getPassKernelSize();
         int var4 = getPeerSize(var3);
         String var5 = this.isShadow() ? "LinearConvolveShadow" : "LinearConvolve";
         return var1.getPeerInstance(var2, var5, var4);
      }
   }

   public abstract Rectangle getPassResultBounds(Rectangle var1, Rectangle var2);

   public PassType getPassType() {
      return LinearConvolveRenderState.PassType.GENERAL_VECTOR;
   }

   public abstract FloatBuffer getPassWeights();

   public abstract int getPassWeightsArrayLength();

   public abstract float[] getPassVector();

   public abstract float[] getPassShadowColorComponents();

   public abstract int getPassKernelSize();

   static {
      BLACK_COMPONENTS = Color4f.BLACK.getPremultipliedRGBComponents();
      int var0 = PlatformUtil.isEmbedded() ? 64 : 128;
      int var1 = (Integer)AccessController.doPrivileged(() -> {
         return Integer.getInteger("decora.maxLinearConvolveKernelSize", var0);
      });
      if (var1 > 128) {
         System.out.println("Clamping maxLinearConvolveKernelSize to 128");
         var1 = 128;
      }

      MAX_KERNEL_SIZE = var1;
   }

   public static enum PassType {
      HORIZONTAL_CENTERED,
      VERTICAL_CENTERED,
      GENERAL_VECTOR;
   }
}
