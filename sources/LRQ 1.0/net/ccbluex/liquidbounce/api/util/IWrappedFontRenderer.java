/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.util;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\f\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0003H&J0\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH&J*\u0010\f\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0003H&J2\u0010\f\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH&J*\u0010\u000e\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0003H&J\u0010\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0011H&J\u0010\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0011H&J\u0012\u0010\u0014\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/api/util/IWrappedFontRenderer;", "", "drawCenteredString", "", "s", "", "x", "", "y", "color", "shadow", "", "drawString", "text", "drawStringWithShadow", "getCharWidth", "character", "", "getColorCode", "charCode", "getStringWidth", "LiquidSense"})
public interface IWrappedFontRenderer {
    public int drawString(@Nullable String var1, float var2, float var3, int var4);

    public int drawStringWithShadow(@Nullable String var1, float var2, float var3, int var4);

    public int drawCenteredString(@NotNull String var1, float var2, float var3, int var4, boolean var5);

    public int drawCenteredString(@NotNull String var1, float var2, float var3, int var4);

    public int drawString(@Nullable String var1, float var2, float var3, int var4, boolean var5);

    public int getColorCode(char var1);

    public int getStringWidth(@Nullable String var1);

    public int getCharWidth(char var1);
}

