/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.Rectangle;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import java.nio.FloatBuffer;
import java.security.AccessController;

public abstract class LinearConvolveRenderState
implements RenderState {
    public static final int MAX_COMPILED_KERNEL_SIZE = 128;
    public static final int MAX_KERNEL_SIZE;
    static final float MIN_EFFECT_RADIUS = 0.00390625f;
    static final float[] BLACK_COMPONENTS;

    public static int getPeerSize(int n) {
        if (n < 32) {
            return n + 3 & 0xFFFFFFFC;
        }
        if (n <= MAX_KERNEL_SIZE) {
            return n + 31 & 0xFFFFFFE0;
        }
        throw new RuntimeException("No peer available for kernel size: " + n);
    }

    static boolean nearZero(float f, int n) {
        return (double)Math.abs(f * (float)n) < 0.001953125;
    }

    static boolean nearOne(float f, int n) {
        return (double)Math.abs(f * (float)n - (float)n) < 0.001953125;
    }

    public abstract boolean isShadow();

    public abstract Color4f getShadowColor();

    public abstract int getInputKernelSize(int var1);

    public abstract boolean isNop();

    public abstract ImageData validatePassInput(ImageData var1, int var2);

    public abstract boolean isPassNop();

    public EffectPeer<? extends LinearConvolveRenderState> getPassPeer(Renderer renderer, FilterContext filterContext) {
        if (this.isPassNop()) {
            return null;
        }
        int n = this.getPassKernelSize();
        int n2 = LinearConvolveRenderState.getPeerSize(n);
        String string = this.isShadow() ? "LinearConvolveShadow" : "LinearConvolve";
        return renderer.getPeerInstance(filterContext, string, n2);
    }

    public abstract Rectangle getPassResultBounds(Rectangle var1, Rectangle var2);

    public PassType getPassType() {
        return PassType.GENERAL_VECTOR;
    }

    public abstract FloatBuffer getPassWeights();

    public abstract int getPassWeightsArrayLength();

    public abstract float[] getPassVector();

    public abstract float[] getPassShadowColorComponents();

    public abstract int getPassKernelSize();

    static {
        BLACK_COMPONENTS = Color4f.BLACK.getPremultipliedRGBComponents();
        int n = PlatformUtil.isEmbedded() ? 64 : 128;
        int n2 = AccessController.doPrivileged(() -> Integer.getInteger("decora.maxLinearConvolveKernelSize", n));
        if (n2 > 128) {
            System.out.println("Clamping maxLinearConvolveKernelSize to 128");
            n2 = 128;
        }
        MAX_KERNEL_SIZE = n2;
    }

    public static final class PassType
    extends Enum<PassType> {
        public static final /* enum */ PassType HORIZONTAL_CENTERED = new PassType();
        public static final /* enum */ PassType VERTICAL_CENTERED = new PassType();
        public static final /* enum */ PassType GENERAL_VECTOR = new PassType();
        private static final /* synthetic */ PassType[] $VALUES;

        public static PassType[] values() {
            return (PassType[])$VALUES.clone();
        }

        public static PassType valueOf(String string) {
            return Enum.valueOf(PassType.class, string);
        }

        private static /* synthetic */ PassType[] $values() {
            return new PassType[]{HORIZONTAL_CENTERED, VERTICAL_CENTERED, GENERAL_VECTOR};
        }

        static {
            $VALUES = PassType.$values();
        }
    }
}

