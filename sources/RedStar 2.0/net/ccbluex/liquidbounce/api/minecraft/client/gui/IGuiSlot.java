package net.ccbluex.liquidbounce.api.minecraft.client.gui;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\n\u0000\n\b\n\b\n\n\b\n\n\b\n\n\b\f\bf\u000020J \b0\t2\n0202\f0\rH&J(0\t20202020H&J\b0\tH&J0\t2020H&J0\t20H&J0\t20H&J0\t20H&R0X¦¢\bR0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiSlot;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGui;", "slotHeight", "", "getSlotHeight", "()I", "width", "getWidth", "drawScreen", "", "mouseX", "mouseY", "partialTicks", "", "elementClicked", "index", "doubleClick", "", "var3", "var4", "handleMouseInput", "registerScrollButtons", "down", "up", "scrollBy", "value", "setEnableScissor", "flag", "setListWidth", "Pride"})
public interface IGuiSlot
extends IGui {
    public int getWidth();

    public int getSlotHeight();

    public void scrollBy(int var1);

    public void registerScrollButtons(int var1, int var2);

    public void drawScreen(int var1, int var2, float var3);

    public void elementClicked(int var1, boolean var2, int var3, int var4);

    public void handleMouseInput();

    public void setListWidth(int var1);

    public void setEnableScissor(boolean var1);
}
