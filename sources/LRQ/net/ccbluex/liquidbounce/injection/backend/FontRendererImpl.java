/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.client.gui.FontRenderer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.util.IWrappedFontRenderer;
import net.ccbluex.liquidbounce.injection.backend.utils.FontRendererWrapper;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.Nullable;

public final class FontRendererImpl
implements IFontRenderer {
    private final FontRenderer wrapped;

    @Override
    public int getFontHeight() {
        return this.wrapped.field_78288_b;
    }

    @Override
    public int getStringWidth(String str) {
        return this.wrapped.func_78256_a(str);
    }

    @Override
    public int drawString(String str, int x, int y, int color) {
        return this.wrapped.func_78276_b(str, x, y, color);
    }

    @Override
    public int drawString(String str, float x, float y, int color) {
        return this.wrapped.func_78276_b(str, (int)x, (int)y, color);
    }

    @Override
    public int drawString(String str, float x, float y, int color, boolean shadow) {
        return this.wrapped.func_175065_a(str, x, y, color, shadow);
    }

    @Override
    public int drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0f, y, color);
    }

    @Override
    public int drawCenteredString(String text, float x, float y, int color, boolean shadow) {
        return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0f, y, color, shadow);
    }

    @Override
    public int drawStringWithShadow(String text, int x, int y, int color) {
        return this.wrapped.func_175063_a(text, (float)x, (float)y, color);
    }

    @Override
    public boolean isGameFontRenderer() {
        return this.wrapped instanceof FontRendererWrapper;
    }

    @Override
    public GameFontRenderer getGameFontRenderer() {
        FontRenderer fontRenderer = this.wrapped;
        if (fontRenderer == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.FontRendererWrapper");
        }
        IWrappedFontRenderer iWrappedFontRenderer = ((FontRendererWrapper)fontRenderer).getWrapped();
        if (iWrappedFontRenderer == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.ui.font.GameFontRenderer");
        }
        return (GameFontRenderer)iWrappedFontRenderer;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof FontRendererImpl && ((FontRendererImpl)other).wrapped.equals(this.wrapped);
    }

    public final FontRenderer getWrapped() {
        return this.wrapped;
    }

    public FontRendererImpl(FontRenderer wrapped) {
        this.wrapped = wrapped;
    }
}

