/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSoup
extends ItemFood {
    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        super.onItemUseFinish(itemStack, world, entityPlayer);
        return new ItemStack(Items.bowl);
    }

    public ItemSoup(int n) {
        super(n, false);
        this.setMaxStackSize(1);
    }
}

