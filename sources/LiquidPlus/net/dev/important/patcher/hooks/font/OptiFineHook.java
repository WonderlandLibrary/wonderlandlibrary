/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 */
package net.dev.important.patcher.hooks.font;

import net.minecraft.client.gui.FontRenderer;

public class OptiFineHook {
    public float getCharWidth(FontRenderer renderer, char c) {
        return renderer.func_78263_a(c);
    }

    public float getOptifineBoldOffset(FontRenderer renderer) {
        return 1.0f;
    }
}

