// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.dispenser;

import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem
{
    public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
        final World world = source.getWorld();
        final IPosition iposition = BlockDispenser.getDispensePosition(source);
        final EnumFacing enumfacing = source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
        final IProjectile iprojectile = this.getProjectileEntity(world, iposition, stack);
        iprojectile.shoot(enumfacing.getXOffset(), enumfacing.getYOffset() + 0.1f, enumfacing.getZOffset(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
        world.spawnEntity((Entity)iprojectile);
        stack.shrink(1);
        return stack;
    }
    
    @Override
    protected void playDispenseSound(final IBlockSource source) {
        source.getWorld().playEvent(1002, source.getBlockPos(), 0);
    }
    
    protected abstract IProjectile getProjectileEntity(final World p0, final IPosition p1, final ItemStack p2);
    
    protected float getProjectileInaccuracy() {
        return 6.0f;
    }
    
    protected float getProjectileVelocity() {
        return 1.1f;
    }
}
