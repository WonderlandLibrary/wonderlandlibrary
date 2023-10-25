package net.minecraft.inventory;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
class ContainerRepair.2
extends Slot {
    final /* synthetic */ World val$worldIn;
    final /* synthetic */ BlockPos val$blockPosIn;

    ContainerRepair.2(IInventory inventoryIn, int index, int xPosition, int yPosition, World world, BlockPos blockPos) {
        this.val$worldIn = world;
        this.val$blockPosIn = blockPos;
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    public boolean canTakeStack(EntityPlayer playerIn) {
        return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
    }

    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        if (!playerIn.capabilities.isCreativeMode) {
            playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
        }
        ContainerRepair.access$000((ContainerRepair)ContainerRepair.this).setInventorySlotContents(0, (ItemStack)null);
        if (ContainerRepair.access$100((ContainerRepair)ContainerRepair.this) > 0) {
            ItemStack itemstack = ContainerRepair.access$000((ContainerRepair)ContainerRepair.this).getStackInSlot(1);
            if (itemstack != null && itemstack.stackSize > ContainerRepair.access$100((ContainerRepair)ContainerRepair.this)) {
                itemstack.stackSize -= ContainerRepair.access$100((ContainerRepair)ContainerRepair.this);
                ContainerRepair.access$000((ContainerRepair)ContainerRepair.this).setInventorySlotContents(1, itemstack);
            } else {
                ContainerRepair.access$000((ContainerRepair)ContainerRepair.this).setInventorySlotContents(1, (ItemStack)null);
            }
        } else {
            ContainerRepair.access$000((ContainerRepair)ContainerRepair.this).setInventorySlotContents(1, (ItemStack)null);
        }
        ContainerRepair.this.maximumCost = 0;
        IBlockState iblockstate = this.val$worldIn.getBlockState(this.val$blockPosIn);
        if (!playerIn.capabilities.isCreativeMode && !this.val$worldIn.isRemote && iblockstate.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12f) {
            int l = (Integer)iblockstate.getValue((IProperty)BlockAnvil.DAMAGE);
            if (++l > 2) {
                this.val$worldIn.setBlockToAir(this.val$blockPosIn);
                this.val$worldIn.playAuxSFX(1020, this.val$blockPosIn, 0);
            } else {
                this.val$worldIn.setBlockState(this.val$blockPosIn, iblockstate.withProperty((IProperty)BlockAnvil.DAMAGE, (Comparable)Integer.valueOf((int)l)), 2);
                this.val$worldIn.playAuxSFX(1021, this.val$blockPosIn, 0);
            }
        } else if (!this.val$worldIn.isRemote) {
            this.val$worldIn.playAuxSFX(1021, this.val$blockPosIn, 0);
        }
    }
}
