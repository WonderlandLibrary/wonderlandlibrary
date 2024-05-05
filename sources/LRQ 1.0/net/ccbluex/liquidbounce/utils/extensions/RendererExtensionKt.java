/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 */
package net.ccbluex.liquidbounce.utils.extensions;

import net.minecraft.client.gui.FontRenderer;

public final class RendererExtensionKt {
    public static final int drawCenteredString(FontRenderer $this$drawCenteredString, String s, float x, float y, int color, boolean shadow) {
        return $this$drawCenteredString.func_175065_a(s, x - (float)$this$drawCenteredString.func_78256_a(s) / 2.0f, y, color, shadow);
    }

    public static final int drawCenteredString(FontRenderer $this$drawCenteredString, String s, float x, float y, int color) {
        return $this$drawCenteredString.func_175065_a(s, x - (float)$this$drawCenteredString.func_78256_a(s) / 2.0f, y, color, false);
    }
}

