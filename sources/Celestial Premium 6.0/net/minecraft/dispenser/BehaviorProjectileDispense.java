/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BehaviorProjectileDispense
extends BehaviorDefaultDispenseItem {
    @Override
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        IPosition iposition = BlockDispenser.getDispensePosition(source);
        EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
        IProjectile iprojectile = this.getProjectileEntity(world, iposition, stack);
        iprojectile.setThrowableHeading(enumfacing.getXOffset(), (float)enumfacing.getYOffset() + 0.1f, enumfacing.getZOffset(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
        world.spawnEntityInWorld((Entity)((Object)iprojectile));
        stack.func_190918_g(1);
        return stack;
    }

    @Override
    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playEvent(1002, source.getBlockPos(), 0);
    }

    protected abstract IProjectile getProjectileEntity(World var1, IPosition var2, ItemStack var3);

    protected float getProjectileInaccuracy() {
        return 6.0f;
    }

    protected float getProjectileVelocity() {
        return 1.1f;
    }
}

