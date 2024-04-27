/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.ranges.RangesKt
 */
package cc.paimon.skid.lbp.newVer.extensions;

import cc.paimon.modules.render.NewGUI;
import cc.paimon.utils.AnimationUtils2;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public final class AnimHelperKt {
    public static final float animLinear(float f, float f2, float f3, float f4) {
        return ((Boolean)NewGUI.fastRenderValue.get()).booleanValue() ? (f2 < 0.0f ? f3 : f4) : RangesKt.coerceIn((float)(f + f2), (float)f3, (float)f4);
    }

    public static final float animSmooth(float f, float f2, float f3) {
        return (Boolean)NewGUI.fastRenderValue.get() != false ? f2 : AnimationUtils2.animate(f2, f, f3 * (float)RenderUtils.deltaTime * 0.025f);
    }
}

