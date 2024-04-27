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

    public int func_78276_b(@Nullable String text, int x, int y, int color) {
        return this.wrapped.drawString(text, x, y, color);
    }

    public int func_175065_a(@Nullable String text, float x, float y, int color, boolean dropShadow) {
        return this.wrapped.drawString(text, x, y, color, dropShadow);
    }

    public int func_175063_a(@Nullable String text, float x, float y, int color) {
        return this.wrapped.drawStringWithShadow(text, x, y, color);
    }

    public int func_175064_b(char character) {
        return this.wrapped.getColorCode(character);
    }

    public int func_78256_a(@Nullable String text) {
        return this.wrapped.getStringWidth(text);
    }

    public int func_78263_a(char character) {
        return this.wrapped.getCharWidth(character);
    }

    public final IWrappedFontRenderer getWrapped() {
        return this.wrapped;
    }

    public FontRendererWrapper(IWrappedFontRenderer wrapped) {
        super(Minecraft.func_71410_x().field_71474_y, new ResourceLocation("textures/font/ascii.png"), Minecraft.func_71410_x().func_110434_K(), false);
        this.wrapped = wrapped;
    }
}

