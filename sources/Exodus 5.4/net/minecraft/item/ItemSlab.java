/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSlab
extends ItemBlock {
    private final BlockSlab doubleSlab;
    private final BlockSlab singleSlab;

    @Override
    public int getMetadata(int n) {
        return n;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return this.singleSlab.getUnlocalizedName(itemStack.getMetadata());
    }

    private boolean tryPlace(ItemStack itemStack, World world, BlockPos blockPos, Object object) {
        Object obj;
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (iBlockState.getBlock() == this.singleSlab && (obj = iBlockState.getValue(this.singleSlab.getVariantProperty())) == object) {
            IBlockState iBlockState2 = this.doubleSlab.getDefaultState().withProperty(this.singleSlab.getVariantProperty(), obj);
            if (world.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(world, blockPos, iBlockState2)) && world.setBlockState(blockPos, iBlockState2, 3)) {
                world.playSoundEffect((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f, this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0f) / 2.0f, this.doubleSlab.stepSound.getFrequency() * 0.8f);
                --itemStack.stackSize;
            }
            return true;
        }
        return false;
    }

    public ItemSlab(Block block, BlockSlab blockSlab, BlockSlab blockSlab2) {
        super(block);
        this.singleSlab = blockSlab;
        this.doubleSlab = blockSlab2;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer, ItemStack itemStack) {
        IBlockState iBlockState;
        BlockPos blockPos2 = blockPos;
        IProperty<?> iProperty = this.singleSlab.getVariantProperty();
        Object object = this.singleSlab.getVariant(itemStack);
        IBlockState iBlockState2 = world.getBlockState(blockPos);
        if (iBlockState2.getBlock() == this.singleSlab) {
            boolean bl;
            boolean bl2 = bl = iBlockState2.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
            if ((enumFacing == EnumFacing.UP && !bl || enumFacing == EnumFacing.DOWN && bl) && object == iBlockState2.getValue(iProperty)) {
                return true;
            }
        }
        return (iBlockState = world.getBlockState(blockPos = blockPos.offset(enumFacing))).getBlock() == this.singleSlab && object == iBlockState.getValue(iProperty) ? true : super.canPlaceBlockOnSide(world, blockPos2, enumFacing, entityPlayer, itemStack);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (itemStack.stackSize == 0) {
            return false;
        }
        if (!entityPlayer.canPlayerEdit(blockPos.offset(enumFacing), enumFacing, itemStack)) {
            return false;
        }
        Object object = this.singleSlab.getVariant(itemStack);
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (iBlockState.getBlock() == this.singleSlab) {
            IProperty<?> iProperty = this.singleSlab.getVariantProperty();
            Object obj = iBlockState.getValue(iProperty);
            BlockSlab.EnumBlockHalf enumBlockHalf = iBlockState.getValue(BlockSlab.HALF);
            if ((enumFacing == EnumFacing.UP && enumBlockHalf == BlockSlab.EnumBlockHalf.BOTTOM || enumFacing == EnumFacing.DOWN && enumBlockHalf == BlockSlab.EnumBlockHalf.TOP) && obj == object) {
                IBlockState iBlockState2 = this.doubleSlab.getDefaultState().withProperty(iProperty, obj);
                if (world.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(world, blockPos, iBlockState2)) && world.setBlockState(blockPos, iBlockState2, 3)) {
                    world.playSoundEffect((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f, this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0f) / 2.0f, this.doubleSlab.stepSound.getFrequency() * 0.8f);
                    --itemStack.stackSize;
                }
                return true;
            }
        }
        return this.tryPlace(itemStack, world, blockPos.offset(enumFacing), object) ? true : super.onItemUse(itemStack, entityPlayer, world, blockPos, enumFacing, f, f2, f3);
    }
}

