/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import net.ccbluex.liquidbounce.api.util.IWrappedFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class FontRendererWrapper
extends FontRenderer {
    private final IWrappedFontRenderer wrapped;

    public int func_78263_a(char c) {
        return this.wrapped.getCharWidth(c);
    }

    public FontRendererWrapper(IWrappedFontRenderer iWrappedFontRenderer) {
        super(Minecraft.func_71410_x().field_71474_y, new ResourceLocation("textures/font/ascii.png"), Minecraft.func_71410_x().func_110434_K(), false);
        this.wrapped = iWrappedFontRenderer;
    }

    public final IWrappedFontRenderer getWrapped() {
        return this.wrapped;
    }

    public int func_175064_b(char c) {
        return this.wrapped.getColorCode(c);
    }

    public int func_175063_a(@Nullable String string, float f, float f2, int n) {
        return this.wrapped.drawStringWithShadow(string, f, f2, n);
    }

    public int func_78276_b(@Nullable String string, int n, int n2, int n3) {
        return this.wrapped.drawString(string, n, n2, n3);
    }

    public int func_175065_a(@Nullable String string, float f, float f2, int n, boolean bl) {
        return this.wrapped.drawString(string, f, f2, n, bl);
    }

    public int func_78256_a(@Nullable String string) {
        return this.wrapped.getStringWidth(string);
    }
}

