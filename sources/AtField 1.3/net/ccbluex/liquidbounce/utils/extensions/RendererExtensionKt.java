/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 */
package net.ccbluex.liquidbounce.utils.extensions;

import net.minecraft.client.gui.FontRenderer;

public final class RendererExtensionKt {
    public static final int drawCenteredString(FontRenderer fontRenderer, String string, float f, float f2, int n, boolean bl) {
        return fontRenderer.func_175065_a(string, f - (float)fontRenderer.func_78256_a(string) / 2.0f, f2, n, bl);
    }

    public static final int drawCenteredString(FontRenderer fontRenderer, String string, float f, float f2, int n) {
        return fontRenderer.func_175065_a(string, f - (float)fontRenderer.func_78256_a(string) / 2.0f, f2, n, false);
    }
}

