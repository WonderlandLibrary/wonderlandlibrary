/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.utils.extensions;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, xi=2, d1={"\u0000 \n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a*\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0001\u001a2\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n\u00a8\u0006\u000b"}, d2={"drawCenteredString", "", "Lnet/minecraft/client/gui/FontRenderer;", "s", "", "x", "", "y", "color", "shadow", "", "KyinoClient"})
public final class RendererExtensionKt {
    public static final int drawCenteredString(@NotNull FontRenderer $this$drawCenteredString, @NotNull String s, float x, float y, int color, boolean shadow) {
        Intrinsics.checkParameterIsNotNull($this$drawCenteredString, "$this$drawCenteredString");
        Intrinsics.checkParameterIsNotNull(s, "s");
        return $this$drawCenteredString.func_175065_a(s, x - (float)$this$drawCenteredString.func_78256_a(s) / 2.0f, y, color, shadow);
    }

    public static final int drawCenteredString(@NotNull FontRenderer $this$drawCenteredString, @NotNull String s, float x, float y, int color) {
        Intrinsics.checkParameterIsNotNull($this$drawCenteredString, "$this$drawCenteredString");
        Intrinsics.checkParameterIsNotNull(s, "s");
        return $this$drawCenteredString.func_175063_a(s, x - (float)$this$drawCenteredString.func_78256_a(s) / 2.0f, y, color);
    }
}

