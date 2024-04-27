/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.gui.FontRenderer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.util.IWrappedFontRenderer;
import net.ccbluex.liquidbounce.injection.backend.utils.FontRendererWrapper;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J(\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0006H\u0016J0\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J(\u0010\u0014\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0006H\u0016J(\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0006H\u0016J0\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J(\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0006H\u0016J(\u0010\u0017\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0006H\u0016J\u0013\u0010\u0018\u001a\u00020\u00132\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0096\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\rH\u0016J\b\u0010\u001e\u001a\u00020\u0013H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u001f"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/FontRendererImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "wrapped", "Lnet/minecraft/client/gui/FontRenderer;", "(Lnet/minecraft/client/gui/FontRenderer;)V", "fontHeight", "", "getFontHeight", "()I", "getWrapped", "()Lnet/minecraft/client/gui/FontRenderer;", "drawCenteredString", "text", "", "x", "", "y", "color", "shadow", "", "drawCenteredStringWithShadow", "drawString", "str", "drawStringWithShadow", "equals", "other", "", "getGameFontRenderer", "Lnet/ccbluex/liquidbounce/ui/font/GameFontRenderer;", "getStringWidth", "isGameFontRenderer", "LiKingSense"})
public final class FontRendererImpl
implements IFontRenderer {
    @NotNull
    private final FontRenderer wrapped;

    @Override
    public int getFontHeight() {
        return this.wrapped.field_78288_b;
    }

    @Override
    public int getStringWidth(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull((Object)str, (String)"str");
        return this.wrapped.func_78256_a(str);
    }

    @Override
    public int drawString(@NotNull String str, int x, int y, int color) {
        Intrinsics.checkParameterIsNotNull((Object)str, (String)"str");
        return this.wrapped.func_78276_b(str, x, y, color);
    }

    @Override
    public int drawString(@NotNull String str, float x, float y, int color) {
        Intrinsics.checkParameterIsNotNull((Object)str, (String)"str");
        return this.wrapped.func_78276_b(str, (int)x, (int)y, color);
    }

    @Override
    public int drawString(@NotNull String str, float x, float y, int color, boolean shadow2) {
        Intrinsics.checkParameterIsNotNull((Object)str, (String)"str");
        return this.wrapped.func_175065_a(str, x, y, color, shadow2);
    }

    @Override
    public int drawCenteredString(@NotNull String text, float x, float y, int color) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0f, y, color);
    }

    @Override
    public int drawCenteredString(@NotNull String text, float x, float y, int color, boolean shadow2) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        return this.drawString(text, x - (float)this.getStringWidth(text) / 2.0f, y, color, shadow2);
    }

    @Override
    public int drawCenteredStringWithShadow(@NotNull String text, float x, float y, int color) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        return this.drawCenteredString(text, x, y, color, true);
    }

    @Override
    public int drawStringWithShadow(@NotNull String text, int x, int y, int color) {
        Intrinsics.checkParameterIsNotNull((Object)text, (String)"text");
        return this.wrapped.func_175063_a(text, (float)x, (float)y, color);
    }

    @Override
    public boolean isGameFontRenderer() {
        return this.wrapped instanceof FontRendererWrapper;
    }

    @Override
    @NotNull
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
        return other instanceof FontRendererImpl && Intrinsics.areEqual((Object)((FontRendererImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final FontRenderer getWrapped() {
        return this.wrapped;
    }

    public FontRendererImpl(@NotNull FontRenderer wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

