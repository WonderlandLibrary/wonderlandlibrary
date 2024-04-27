package net.ccbluex.liquidbounce.api.minecraft.inventory;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\u0000\n\n\b\n\b\n\b\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\f\r¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "", "hasStack", "", "getHasStack", "()Z", "slotNumber", "", "getSlotNumber", "()I", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getStack", "()Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "Pride"})
public interface ISlot {
    public int getSlotNumber();

    @Nullable
    public IItemStack getStack();

    public boolean getHasStack();
}
