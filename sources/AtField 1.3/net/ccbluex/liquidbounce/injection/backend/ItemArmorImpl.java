/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemArmor$ArmorMaterial
 *  net.minecraft.item.ItemStack
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.item.IItemArmor;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.minecraft.IArmorMaterial;
import net.ccbluex.liquidbounce.injection.backend.ArmorMaterialImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public final class ItemArmorImpl
extends ItemImpl
implements IItemArmor {
    public ItemArmorImpl(ItemArmor itemArmor) {
        super((Item)itemArmor);
    }

    @Override
    public String getUnlocalizedName() {
        return ((ItemArmor)this.getWrapped()).func_77658_a();
    }

    @Override
    public IArmorMaterial getArmorMaterial() {
        ItemArmor.ArmorMaterial armorMaterial = ((ItemArmor)this.getWrapped()).func_82812_d();
        boolean bl = false;
        return new ArmorMaterialImpl(armorMaterial);
    }

    @Override
    public int getArmorType() {
        return ((ItemArmor)this.getWrapped()).field_77881_a.func_188454_b();
    }

    @Override
    public int getColor(IItemStack iItemStack) {
        IItemStack iItemStack2 = iItemStack;
        ItemArmor itemArmor = (ItemArmor)this.getWrapped();
        boolean bl = false;
        ItemStack itemStack = ((ItemStackImpl)iItemStack2).getWrapped();
        return itemArmor.func_82814_b(itemStack);
    }
}

