/*
 * Decompiled with CFR 0.152.
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
    public ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
        World world = iBlockSource.getWorld();
        IPosition iPosition = BlockDispenser.getDispensePosition(iBlockSource);
        EnumFacing enumFacing = BlockDispenser.getFacing(iBlockSource.getBlockMetadata());
        IProjectile iProjectile = this.getProjectileEntity(world, iPosition);
        iProjectile.setThrowableHeading(enumFacing.getFrontOffsetX(), (float)enumFacing.getFrontOffsetY() + 0.1f, enumFacing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
        world.spawnEntityInWorld((Entity)((Object)iProjectile));
        itemStack.splitStack(1);
        return itemStack;
    }

    @Override
    protected void playDispenseSound(IBlockSource iBlockSource) {
        iBlockSource.getWorld().playAuxSFX(1002, iBlockSource.getBlockPos(), 0);
    }

    protected float func_82498_a() {
        return 6.0f;
    }

    protected float func_82500_b() {
        return 1.1f;
    }

    protected abstract IProjectile getProjectileEntity(World var1, IPosition var2);
}

