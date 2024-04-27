package net.ccbluex.liquidbounce.api.minecraft.inventory;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\b\n\b\n\n\b\bf\u000020J02\b0H&R0X¦¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "", "windowId", "", "getWindowId", "()I", "getSlot", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "id", "Pride"})
public interface IContainer {
    public int getWindowId();

    @NotNull
    public ISlot getSlot(int var1);
}
