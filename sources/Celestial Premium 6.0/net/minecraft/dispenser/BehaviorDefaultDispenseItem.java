/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorDefaultDispenseItem
implements IBehaviorDispenseItem {
    @Override
    public final ItemStack dispense(IBlockSource source, ItemStack stack) {
        ItemStack itemstack = this.dispenseStack(source, stack);
        this.playDispenseSound(source);
        this.spawnDispenseParticles(source, source.getBlockState().getValue(BlockDispenser.FACING));
        return itemstack;
    }

    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
        IPosition iposition = BlockDispenser.getDispensePosition(source);
        ItemStack itemstack = stack.splitStack(1);
        BehaviorDefaultDispenseItem.doDispense(source.getWorld(), itemstack, 6, enumfacing, iposition);
        return stack;
    }

    public static void doDispense(World worldIn, ItemStack stack, int speed, EnumFacing facing, IPosition position) {
        double d0 = position.getX();
        double d1 = position.getY();
        double d2 = position.getZ();
        d1 = facing.getAxis() == EnumFacing.Axis.Y ? (d1 -= 0.125) : (d1 -= 0.15625);
        EntityItem entityitem = new EntityItem(worldIn, d0, d1, d2, stack);
        double d3 = worldIn.rand.nextDouble() * 0.1 + 0.2;
        entityitem.motionX = (double)facing.getXOffset() * d3;
        entityitem.motionY = 0.2f;
        entityitem.motionZ = (double)facing.getZOffset() * d3;
        entityitem.motionX += worldIn.rand.nextGaussian() * (double)0.0075f * (double)speed;
        entityitem.motionY += worldIn.rand.nextGaussian() * (double)0.0075f * (double)speed;
        entityitem.motionZ += worldIn.rand.nextGaussian() * (double)0.0075f * (double)speed;
        worldIn.spawnEntityInWorld(entityitem);
    }

    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playEvent(1000, source.getBlockPos(), 0);
    }

    protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn) {
        source.getWorld().playEvent(2000, source.getBlockPos(), this.getWorldEventDataFrom(facingIn));
    }

    private int getWorldEventDataFrom(EnumFacing facingIn) {
        return facingIn.getXOffset() + 1 + (facingIn.getZOffset() + 1) * 3;
    }
}

