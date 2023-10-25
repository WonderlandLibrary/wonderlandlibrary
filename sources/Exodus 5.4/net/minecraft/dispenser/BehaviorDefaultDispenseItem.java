/*
 * Decompiled with CFR 0.152.
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
    public final ItemStack dispense(IBlockSource iBlockSource, ItemStack itemStack) {
        ItemStack itemStack2 = this.dispenseStack(iBlockSource, itemStack);
        this.playDispenseSound(iBlockSource);
        this.spawnDispenseParticles(iBlockSource, BlockDispenser.getFacing(iBlockSource.getBlockMetadata()));
        return itemStack2;
    }

    protected void playDispenseSound(IBlockSource iBlockSource) {
        iBlockSource.getWorld().playAuxSFX(1000, iBlockSource.getBlockPos(), 0);
    }

    private int func_82488_a(EnumFacing enumFacing) {
        return enumFacing.getFrontOffsetX() + 1 + (enumFacing.getFrontOffsetZ() + 1) * 3;
    }

    public static void doDispense(World world, ItemStack itemStack, int n, EnumFacing enumFacing, IPosition iPosition) {
        double d = iPosition.getX();
        double d2 = iPosition.getY();
        double d3 = iPosition.getZ();
        d2 = enumFacing.getAxis() == EnumFacing.Axis.Y ? (d2 -= 0.125) : (d2 -= 0.15625);
        EntityItem entityItem = new EntityItem(world, d, d2, d3, itemStack);
        double d4 = world.rand.nextDouble() * 0.1 + 0.2;
        entityItem.motionX = (double)enumFacing.getFrontOffsetX() * d4;
        entityItem.motionY = 0.2f;
        entityItem.motionZ = (double)enumFacing.getFrontOffsetZ() * d4;
        entityItem.motionX += world.rand.nextGaussian() * (double)0.0075f * (double)n;
        entityItem.motionY += world.rand.nextGaussian() * (double)0.0075f * (double)n;
        entityItem.motionZ += world.rand.nextGaussian() * (double)0.0075f * (double)n;
        world.spawnEntityInWorld(entityItem);
    }

    protected ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
        EnumFacing enumFacing = BlockDispenser.getFacing(iBlockSource.getBlockMetadata());
        IPosition iPosition = BlockDispenser.getDispensePosition(iBlockSource);
        ItemStack itemStack2 = itemStack.splitStack(1);
        BehaviorDefaultDispenseItem.doDispense(iBlockSource.getWorld(), itemStack2, 6, enumFacing, iPosition);
        return itemStack;
    }

    protected void spawnDispenseParticles(IBlockSource iBlockSource, EnumFacing enumFacing) {
        iBlockSource.getWorld().playAuxSFX(2000, iBlockSource.getBlockPos(), this.func_82488_a(enumFacing));
    }
}

