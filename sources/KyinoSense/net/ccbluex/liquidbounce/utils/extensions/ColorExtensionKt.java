/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils.extensions;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, xi=2, d1={"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u0012\u0010\u0004\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u0012\u0010\u0004\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0005\u00a8\u0006\u0006"}, d2={"darker", "Ljava/awt/Color;", "factor", "", "setAlpha", "", "KyinoClient"})
public final class ColorExtensionKt {
    @NotNull
    public static final Color darker(@NotNull Color $this$darker, float factor) {
        Intrinsics.checkParameterIsNotNull($this$darker, "$this$darker");
        return new Color((float)$this$darker.getRed() / 255.0f * RangesKt.coerceIn(factor, 0.0f, 1.0f), (float)$this$darker.getGreen() / 255.0f * RangesKt.coerceIn(factor, 0.0f, 1.0f), (float)$this$darker.getBlue() / 255.0f * RangesKt.coerceIn(factor, 0.0f, 1.0f), (float)$this$darker.getAlpha() / 255.0f);
    }

    @NotNull
    public static final Color setAlpha(@NotNull Color $this$setAlpha, float factor) {
        Intrinsics.checkParameterIsNotNull($this$setAlpha, "$this$setAlpha");
        return new Color((float)$this$setAlpha.getRed() / 255.0f, (float)$this$setAlpha.getGreen() / 255.0f, (float)$this$setAlpha.getBlue() / 255.0f, RangesKt.coerceIn(factor, 0.0f, 1.0f));
    }

    @NotNull
    public static final Color setAlpha(@NotNull Color $this$setAlpha, int factor) {
        Intrinsics.checkParameterIsNotNull($this$setAlpha, "$this$setAlpha");
        return new Color($this$setAlpha.getRed(), $this$setAlpha.getGreen(), $this$setAlpha.getBlue(), RangesKt.coerceIn(factor, 0, 255));
    }
}

