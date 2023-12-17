// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public interface IItemPropertyGetter
{
    float apply(final ItemStack p0, final World p1, final EntityLivingBase p2);
}
