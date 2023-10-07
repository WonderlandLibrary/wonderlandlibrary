// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

public class ItemSlab extends ItemBlock
{
    private final BlockSlab field_150949_c;
    private final BlockSlab field_179226_c;
    private static final String __OBFID = "CL_00000071";
    
    public ItemSlab(final Block p_i45782_1_, final BlockSlab p_i45782_2_, final BlockSlab p_i45782_3_) {
        super(p_i45782_1_);
        this.field_150949_c = p_i45782_2_;
        this.field_179226_c = p_i45782_3_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return this.field_150949_c.getFullSlabName(stack.getMetadata());
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (stack.stackSize == 0) {
            return false;
        }
        if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
            return false;
        }
        final Object var9 = this.field_150949_c.func_176553_a(stack);
        final IBlockState var10 = worldIn.getBlockState(pos);
        if (var10.getBlock() == this.field_150949_c) {
            final IProperty var11 = this.field_150949_c.func_176551_l();
            final Comparable var12 = var10.getValue(var11);
            final BlockSlab.EnumBlockHalf var13 = (BlockSlab.EnumBlockHalf)var10.getValue(BlockSlab.HALF_PROP);
            if (((side == EnumFacing.UP && var13 == BlockSlab.EnumBlockHalf.BOTTOM) || (side == EnumFacing.DOWN && var13 == BlockSlab.EnumBlockHalf.TOP)) && var12 == var9) {
                final IBlockState var14 = this.field_179226_c.getDefaultState().withProperty(var11, var12);
                if (worldIn.checkNoEntityCollision(this.field_179226_c.getCollisionBoundingBox(worldIn, pos, var14)) && worldIn.setBlockState(pos, var14, 3)) {
                    worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, this.field_179226_c.stepSound.getPlaceSound(), (this.field_179226_c.stepSound.getVolume() + 1.0f) / 2.0f, this.field_179226_c.stepSound.getFrequency() * 0.8f);
                    --stack.stackSize;
                }
                return true;
            }
        }
        return this.func_180615_a(stack, worldIn, pos.offset(side), var9) || super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, BlockPos p_179222_2_, final EnumFacing p_179222_3_, final EntityPlayer p_179222_4_, final ItemStack p_179222_5_) {
        final BlockPos var6 = p_179222_2_;
        final IProperty var7 = this.field_150949_c.func_176551_l();
        final Object var8 = this.field_150949_c.func_176553_a(p_179222_5_);
        final IBlockState var9 = worldIn.getBlockState(p_179222_2_);
        if (var9.getBlock() == this.field_150949_c) {
            final boolean var10 = var9.getValue(BlockSlab.HALF_PROP) == BlockSlab.EnumBlockHalf.TOP;
            if (((p_179222_3_ == EnumFacing.UP && !var10) || (p_179222_3_ == EnumFacing.DOWN && var10)) && var8 == var9.getValue(var7)) {
                return true;
            }
        }
        p_179222_2_ = p_179222_2_.offset(p_179222_3_);
        final IBlockState var11 = worldIn.getBlockState(p_179222_2_);
        return (var11.getBlock() == this.field_150949_c && var8 == var11.getValue(var7)) || super.canPlaceBlockOnSide(worldIn, var6, p_179222_3_, p_179222_4_, p_179222_5_);
    }
    
    private boolean func_180615_a(final ItemStack p_180615_1_, final World worldIn, final BlockPos p_180615_3_, final Object p_180615_4_) {
        final IBlockState var5 = worldIn.getBlockState(p_180615_3_);
        if (var5.getBlock() == this.field_150949_c) {
            final Comparable var6 = var5.getValue(this.field_150949_c.func_176551_l());
            if (var6 == p_180615_4_) {
                final IBlockState var7 = this.field_179226_c.getDefaultState().withProperty(this.field_150949_c.func_176551_l(), var6);
                if (worldIn.checkNoEntityCollision(this.field_179226_c.getCollisionBoundingBox(worldIn, p_180615_3_, var7)) && worldIn.setBlockState(p_180615_3_, var7, 3)) {
                    worldIn.playSoundEffect(p_180615_3_.getX() + 0.5f, p_180615_3_.getY() + 0.5f, p_180615_3_.getZ() + 0.5f, this.field_179226_c.stepSound.getPlaceSound(), (this.field_179226_c.stepSound.getVolume() + 1.0f) / 2.0f, this.field_179226_c.stepSound.getFrequency() * 0.8f);
                    --p_180615_1_.stackSize;
                }
                return true;
            }
        }
        return false;
    }
}
