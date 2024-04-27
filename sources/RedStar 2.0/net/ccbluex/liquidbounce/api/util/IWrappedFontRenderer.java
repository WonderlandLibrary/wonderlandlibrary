package net.ccbluex.liquidbounce.api.util;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\n\u0000\n\u0000\n\b\n\u0000\n\n\u0000\n\n\b\n\n\b\n\f\n\b\bf\u000020J(020202\b02\t0H&J0020202\b02\t02\n0H&J*\f02\b0202\b02\t0H&J2\f02\b\r0202\b02\t02\n0H&J*02\b\r0202\b02\t0H&J020H&J020H&J02\b\r0H&¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/IWrappedFontRenderer;", "", "drawCenteredString", "", "s", "", "x", "", "y", "color", "shadow", "", "drawString", "text", "drawStringWithShadow", "getCharWidth", "character", "", "getColorCode", "charCode", "getStringWidth", "Pride"})
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
