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

    public final FontRenderer getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof FontRendererImpl && ((FontRendererImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public int drawCenteredString(String string, float f, float f2, int n) {
        return this.drawString(string, f - (float)this.getStringWidth(string) / 2.0f, f2, n);
    }

    @Override
    public int getFontHeight() {
        return this.wrapped.field_78288_b;
    }

    @Override
    public int drawString(String string, float f, float f2, int n, boolean bl) {
        return this.wrapped.func_175065_a(string, f, f2, n, bl);
    }

    @Override
    public boolean isGameFontRenderer() {
        return this.wrapped instanceof FontRendererWrapper;
    }

    public FontRendererImpl(FontRenderer fontRenderer) {
        this.wrapped = fontRenderer;
    }

    @Override
    public int drawCenteredStringWithShadow(String string, float f, float f2, int n) {
        return this.drawCenteredString(string, f, f2, n, true);
    }

    @Override
    public int getStringWidth(String string) {
        return this.wrapped.func_78256_a(string);
    }

    @Override
    public int getCharWidth(char c) {
        return IFontRenderer.DefaultImpls.getCharWidth(this, c);
    }

    @Override
    public int drawString(String string, float f, float f2, int n) {
        return this.wrapped.func_78276_b(string, (int)f, (int)f2, n);
    }

    @Override
    public int drawStringWithShadow(String string, int n, int n2, int n3) {
        return this.wrapped.func_175063_a(string, (float)n, (float)n2, n3);
    }

    @Override
    public int drawString(String string, int n, int n2, int n3) {
        return this.wrapped.func_78276_b(string, n, n2, n3);
    }

    @Override
    public int drawCenteredString(String string, float f, float f2, int n, boolean bl) {
        return this.drawString(string, f - (float)this.getStringWidth(string) / 2.0f, f2, n, bl);
    }
}

