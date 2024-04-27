package net.ccbluex.liquidbounce.api.minecraft.client.gui;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\n\u0000\n\u0000\n\b\n\b\n\n\u0000\n\n\b\n\n\b\n\n\b\bf\u000020J(020\b2\t0\n20\n2\f0H&J0020\b2\t0\n20\n2\f02\r0H&J(020\b2\t0\n20\n2\f0H&J0020\b2\t0\n20\n2\f02\r0H&J(020\b2\t0202\f0H&J(020\b2\t0202\f0H&J\b0H&J020\bH&J\b0H&R0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "", "fontHeight", "", "getFontHeight", "()I", "drawCenteredString", "text", "", "x", "", "y", "color", "shadow", "", "drawString", "str", "drawStringWithShadow", "getGameFontRenderer", "Lnet/ccbluex/liquidbounce/ui/font/GameFontRenderer;", "getStringWidth", "isGameFontRenderer", "Pride"})
public interface IFontRenderer {
    public int getFontHeight();

    public int getStringWidth(@NotNull String var1);

    public int drawString(@NotNull String var1, int var2, int var3, int var4);

    public int drawString(@NotNull String var1, float var2, float var3, int var4, boolean var5);

    public int drawCenteredString(@NotNull String var1, float var2, float var3, int var4);

    public int drawCenteredString(@NotNull String var1, float var2, float var3, int var4, boolean var5);

    public int drawStringWithShadow(@NotNull String var1, int var2, int var3, int var4);

    public boolean isGameFontRenderer();

    @NotNull
    public GameFontRenderer getGameFontRenderer();

    public int drawString(@NotNull String var1, float var2, float var3, int var4);
}
