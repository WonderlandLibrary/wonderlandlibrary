/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemPropertyGetter {
    public float apply(ItemStack var1, World var2, EntityLivingBase var3);
}

