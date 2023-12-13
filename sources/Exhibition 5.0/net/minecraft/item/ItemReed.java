// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemReed extends Item
{
    private Block field_150935_a;
    private static final String __OBFID = "CL_00001773";
    
    public ItemReed(final Block p_i45329_1_) {
        this.field_150935_a = p_i45329_1_;
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState var9 = worldIn.getBlockState(pos);
        final Block var10 = var9.getBlock();
        if (var10 == Blocks.snow_layer && (int)var9.getValue(BlockSnow.LAYERS_PROP) < 1) {
            side = EnumFacing.UP;
        }
        else if (!var10.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        if (!playerIn.func_175151_a(pos, side, stack)) {
            return false;
        }
        if (stack.stackSize == 0) {
            return false;
        }
        if (worldIn.canBlockBePlaced(this.field_150935_a, pos, false, side, null, stack)) {
            IBlockState var11 = this.field_150935_a.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);
            if (worldIn.setBlockState(pos, var11, 3)) {
                var11 = worldIn.getBlockState(pos);
                if (var11.getBlock() == this.field_150935_a) {
                    ItemBlock.setTileEntityNBT(worldIn, pos, stack);
                    var11.getBlock().onBlockPlacedBy(worldIn, pos, var11, playerIn, stack);
                }
                worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, this.field_150935_a.stepSound.getPlaceSound(), (this.field_150935_a.stepSound.getVolume() + 1.0f) / 2.0f, this.field_150935_a.stepSound.getFrequency() * 0.8f);
                --stack.stackSize;
                return true;
            }
        }
        return false;
    }
}
