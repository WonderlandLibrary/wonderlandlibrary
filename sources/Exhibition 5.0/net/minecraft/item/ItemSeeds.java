// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;

public class ItemSeeds extends Item
{
    private Block cropBlock;
    private Block soilBlockID;
    private static final String __OBFID = "CL_00000061";
    
    public ItemSeeds(final Block cropBlock, final Block soilBlock) {
        this.cropBlock = cropBlock;
        this.soilBlockID = soilBlock;
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }
    
    public Block getCropBlock() {
        return this.cropBlock;
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side != EnumFacing.UP) {
            return false;
        }
        if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
            return false;
        }
        if (worldIn.getBlockState(pos).getBlock() == this.soilBlockID && worldIn.isAirBlock(pos.offsetUp())) {
            worldIn.setBlockState(pos.offsetUp(), this.cropBlock.getDefaultState());
            --stack.stackSize;
            return true;
        }
        return false;
    }
}
