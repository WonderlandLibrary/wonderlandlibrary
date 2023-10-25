/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDropper
extends BlockDispenser {
    private final IBehaviorDispenseItem dropBehavior = new BehaviorDefaultDispenseItem();

    @Override
    protected IBehaviorDispenseItem getBehavior(ItemStack itemStack) {
        return this.dropBehavior;
    }

    @Override
    protected void dispense(World world, BlockPos blockPos) {
        BlockSourceImpl blockSourceImpl = new BlockSourceImpl(world, blockPos);
        TileEntityDispenser tileEntityDispenser = (TileEntityDispenser)blockSourceImpl.getBlockTileEntity();
        if (tileEntityDispenser != null) {
            int n = tileEntityDispenser.getDispenseSlot();
            if (n < 0) {
                world.playAuxSFX(1001, blockPos, 0);
            } else {
                ItemStack itemStack = tileEntityDispenser.getStackInSlot(n);
                if (itemStack != null) {
                    ItemStack itemStack2;
                    EnumFacing enumFacing = world.getBlockState(blockPos).getValue(FACING);
                    BlockPos blockPos2 = blockPos.offset(enumFacing);
                    IInventory iInventory = TileEntityHopper.getInventoryAtPosition(world, blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
                    if (iInventory == null) {
                        itemStack2 = this.dropBehavior.dispense(blockSourceImpl, itemStack);
                        if (itemStack2 != null && itemStack2.stackSize <= 0) {
                            itemStack2 = null;
                        }
                    } else {
                        itemStack2 = TileEntityHopper.putStackInInventoryAllSlots(iInventory, itemStack.copy().splitStack(1), enumFacing.getOpposite());
                        if (itemStack2 == null) {
                            itemStack2 = itemStack.copy();
                            if (--itemStack2.stackSize <= 0) {
                                itemStack2 = null;
                            }
                        } else {
                            itemStack2 = itemStack.copy();
                        }
                    }
                    tileEntityDispenser.setInventorySlotContents(n, itemStack2);
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityDropper();
    }
}

