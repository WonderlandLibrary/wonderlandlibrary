/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.IntegerProperty
 *  javafx.scene.Node
 *  javafx.scene.effect.BlendMode
 *  javafx.scene.effect.Effect
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;

public class EffectHelper {
    private static EffectAccessor effectAccessor;

    private EffectHelper() {
    }

    public static Effect getPeer(javafx.scene.effect.Effect effect) {
        return effectAccessor.getPeer(effect);
    }

    public static void sync(javafx.scene.effect.Effect effect) {
        effectAccessor.sync(effect);
    }

    public static IntegerProperty effectDirtyProperty(javafx.scene.effect.Effect effect) {
        return effectAccessor.effectDirtyProperty(effect);
    }

    public static boolean isEffectDirty(javafx.scene.effect.Effect effect) {
        return effectAccessor.isEffectDirty(effect);
    }

    public static BaseBounds getBounds(javafx.scene.effect.Effect effect, BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        return effectAccessor.getBounds(effect, baseBounds, baseTransform, node, boundsAccessor);
    }

    public static javafx.scene.effect.Effect copy(javafx.scene.effect.Effect effect) {
        return effectAccessor.copy(effect);
    }

    public static Blend.Mode getToolkitBlendMode(BlendMode blendMode) {
        return effectAccessor.getToolkitBlendMode(blendMode);
    }

    public static void setEffectAccessor(EffectAccessor effectAccessor) {
        if (EffectHelper.effectAccessor != null) {
            throw new IllegalStateException();
        }
        EffectHelper.effectAccessor = effectAccessor;
    }

    static {
        Utils.forceInit(javafx.scene.effect.Effect.class);
    }

    public static interface EffectAccessor {
        public Effect getPeer(javafx.scene.effect.Effect var1);

        public void sync(javafx.scene.effect.Effect var1);

        public IntegerProperty effectDirtyProperty(javafx.scene.effect.Effect var1);

        public boolean isEffectDirty(javafx.scene.effect.Effect var1);

        public BaseBounds getBounds(javafx.scene.effect.Effect var1, BaseBounds var2, BaseTransform var3, Node var4, BoundsAccessor var5);

        public javafx.scene.effect.Effect copy(javafx.scene.effect.Effect var1);

        public Blend.Mode getToolkitBlendMode(BlendMode var1);
    }
}

