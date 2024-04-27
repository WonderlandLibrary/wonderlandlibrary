package net.ccbluex.liquidbounce.api.minecraft.item;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.minecraft.IArmorMaterial;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\n\u0000\n\n\b\n\b\n\b\n\n\u0000\bf\u000020J\n020\fH&R0X¦¢\bR0X¦¢\b\b\t¨\r"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemArmor;", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "armorMaterial", "Lnet/ccbluex/liquidbounce/api/minecraft/minecraft/IArmorMaterial;", "getArmorMaterial", "()Lnet/ccbluex/liquidbounce/api/minecraft/minecraft/IArmorMaterial;", "armorType", "", "getArmorType", "()I", "getColor", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "Pride"})
public interface IItemArmor
extends IItem {
    @NotNull
    public IArmorMaterial getArmorMaterial();

    public int getArmorType();

    public int getColor(@NotNull IItemStack var1);
}
