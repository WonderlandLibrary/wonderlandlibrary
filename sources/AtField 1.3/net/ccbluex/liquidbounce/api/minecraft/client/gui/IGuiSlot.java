/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.client.gui;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGui;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\bf\u0018\u00002\u00020\u0001J \u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH&J(\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003H&J\b\u0010\u0014\u001a\u00020\tH&J\u0018\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0003H&J\u0010\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\u0003H&J\u0010\u0010\u001a\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\u0011H&J\u0010\u0010\u001c\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0003H&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005\u00a8\u0006\u001d"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiSlot;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGui;", "slotHeight", "", "getSlotHeight", "()I", "width", "getWidth", "drawScreen", "", "mouseX", "mouseY", "partialTicks", "", "elementClicked", "index", "doubleClick", "", "var3", "var4", "handleMouseInput", "registerScrollButtons", "down", "up", "scrollBy", "value", "setEnableScissor", "flag", "setListWidth", "AtField"})
public interface IGuiSlot
extends IGui {
    public void registerScrollButtons(int var1, int var2);

    public void setListWidth(int var1);

    public void drawScreen(int var1, int var2, float var3);

    public void setEnableScissor(boolean var1);

    public int getSlotHeight();

    public void elementClicked(int var1, boolean var2, int var3, int var4);

    public void scrollBy(int var1);

    public void handleMouseInput();

    public int getWidth();
}

