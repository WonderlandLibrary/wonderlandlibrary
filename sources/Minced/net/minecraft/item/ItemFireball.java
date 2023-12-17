// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemFireball extends Item
{
    public ItemFireball() {
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        pos = pos.offset(facing);
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        }
        if (worldIn.getBlockState(pos).getMaterial() == Material.AIR) {
            worldIn.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, (ItemFireball.itemRand.nextFloat() - ItemFireball.itemRand.nextFloat()) * 0.2f + 1.0f);
            worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState());
        }
        if (!player.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }
}
