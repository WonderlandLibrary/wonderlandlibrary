// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemSeedFood extends ItemFood
{
    private Block field_150908_b;
    private Block soilId;
    private static final String __OBFID = "CL_00000060";
    
    public ItemSeedFood(final int p_i45351_1_, final float p_i45351_2_, final Block p_i45351_3_, final Block p_i45351_4_) {
        super(p_i45351_1_, p_i45351_2_, false);
        this.field_150908_b = p_i45351_3_;
        this.soilId = p_i45351_4_;
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side != EnumFacing.UP) {
            return false;
        }
        if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
            return false;
        }
        if (worldIn.getBlockState(pos).getBlock() == this.soilId && worldIn.isAirBlock(pos.offsetUp())) {
            worldIn.setBlockState(pos.offsetUp(), this.field_150908_b.getDefaultState());
            --stack.stackSize;
            return true;
        }
        return false;
    }
}
