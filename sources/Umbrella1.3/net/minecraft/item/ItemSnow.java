/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSnow
extends ItemBlock {
    private static final String __OBFID = "CL_00000068";

    public ItemSnow(Block p_i45781_1_) {
        super(p_i45781_1_);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState var12;
        int var11;
        if (stack.stackSize == 0) {
            return false;
        }
        if (!playerIn.func_175151_a(pos, side, stack)) {
            return false;
        }
        IBlockState var9 = worldIn.getBlockState(pos);
        Block var10 = var9.getBlock();
        if (var10 != this.block && side != EnumFacing.UP) {
            pos = pos.offset(side);
            var9 = worldIn.getBlockState(pos);
            var10 = var9.getBlock();
        }
        if (var10 == this.block && (var11 = ((Integer)var9.getValue(BlockSnow.LAYERS_PROP)).intValue()) <= 7 && worldIn.checkNoEntityCollision(this.block.getCollisionBoundingBox(worldIn, pos, var12 = var9.withProperty(BlockSnow.LAYERS_PROP, Integer.valueOf(var11 + 1)))) && worldIn.setBlockState(pos, var12, 2)) {
            worldIn.playSoundEffect((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
            --stack.stackSize;
            return true;
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}

