// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class ItemSpectralArrow extends ItemArrow
{
    @Override
    public EntityArrow createArrow(final World worldIn, final ItemStack stack, final EntityLivingBase shooter) {
        return new EntitySpectralArrow(worldIn, shooter);
    }
}
