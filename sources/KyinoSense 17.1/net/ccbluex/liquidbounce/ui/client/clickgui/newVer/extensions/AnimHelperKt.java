/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer.extensions;

import kotlin.Metadata;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.utils.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, xi=2, d1={"\u0000\n\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0006\u001a\"\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001\u001a\u001a\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001\u00a8\u0006\u0007"}, d2={"animLinear", "", "speed", "min", "max", "animSmooth", "target", "KyinoClient"})
public final class AnimHelperKt {
    public static final float animSmooth(float $this$animSmooth, float target, float speed) {
        return (Boolean)ClickGUI.fastRenderValue.get() != false ? target : AnimationUtils.animate(target, $this$animSmooth, speed * (float)RenderUtils.deltaTime * 0.025f);
    }

    public static final float animLinear(float $this$animLinear, float speed, float min, float max) {
        return ((Boolean)ClickGUI.fastRenderValue.get()).booleanValue() ? (speed < 0.0f ? min : max) : RangesKt.coerceIn($this$animLinear + speed, min, max);
    }
}

