package net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\b\n\b\bf\u000020J(02\b0\t2\n02\f02\r0H&R0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IGuiContainer;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "inventorySlots", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "getInventorySlots", "()Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "handleMouseClick", "", "slot", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "slotNumber", "", "clickedButton", "clickType", "Pride"})
public interface IGuiContainer
extends IGuiScreen {
    public void handleMouseClick(@NotNull ISlot var1, int var2, int var3, int var4);

    @Nullable
    public IContainer getInventorySlots();
}
