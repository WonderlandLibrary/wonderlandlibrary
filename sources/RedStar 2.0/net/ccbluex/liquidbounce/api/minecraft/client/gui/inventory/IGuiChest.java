package net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiContainer;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IIInventory;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\b\n\b\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\b\b\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IGuiChest;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IGuiContainer;", "inventoryRows", "", "getInventoryRows", "()I", "lowerChestInventory", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IIInventory;", "getLowerChestInventory", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IIInventory;", "Pride"})
public interface IGuiChest
extends IGuiContainer {
    public int getInventoryRows();

    @Nullable
    public IIInventory getLowerChestInventory();
}
