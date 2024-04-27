package net.ccbluex.liquidbounce.api.minecraft.entity.player;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.util.IWrappedArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\n\b\n\b\n\b\bf\u000020J020\bH&J\n0H&J020\bH&R\n00X¦¢\bR0\bX¦¢\f\b\t\n\"\b\fR\r\n00X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IInventoryPlayer;", "", "armorInventory", "Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getArmorInventory", "()Lnet/ccbluex/liquidbounce/api/util/IWrappedArray;", "currentItem", "", "getCurrentItem", "()I", "setCurrentItem", "(I)V", "mainInventory", "getMainInventory", "armorItemInSlot", "slot", "getCurrentItemInHand", "getStackInSlot", "Pride"})
public interface IInventoryPlayer {
    @NotNull
    public IWrappedArray<IItemStack> getMainInventory();

    @NotNull
    public IWrappedArray<IItemStack> getArmorInventory();

    public int getCurrentItem();

    public void setCurrentItem(int var1);

    @Nullable
    public IItemStack getStackInSlot(int var1);

    @Nullable
    public IItemStack armorItemInSlot(int var1);

    @Nullable
    public IItemStack getCurrentItemInHand();
}
