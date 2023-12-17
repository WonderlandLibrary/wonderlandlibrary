// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.world.end.DragonFightManager;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEndCrystal extends Item
{
    public ItemEndCrystal() {
        this.setTranslationKey("end_crystal");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != Blocks.OBSIDIAN && iblockstate.getBlock() != Blocks.BEDROCK) {
            return EnumActionResult.FAIL;
        }
        final BlockPos blockpos = pos.up();
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(blockpos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final BlockPos blockpos2 = blockpos.up();
        boolean flag = !worldIn.isAirBlock(blockpos) && !worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
        flag |= (!worldIn.isAirBlock(blockpos2) && !worldIn.getBlockState(blockpos2).getBlock().isReplaceable(worldIn, blockpos2));
        if (flag) {
            return EnumActionResult.FAIL;
        }
        final double d0 = blockpos.getX();
        final double d2 = blockpos.getY();
        final double d3 = blockpos.getZ();
        final List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d2, d3, d0 + 1.0, d2 + 2.0, d3 + 1.0));
        if (!list.isEmpty()) {
            return EnumActionResult.FAIL;
        }
        if (!worldIn.isRemote) {
            final EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(worldIn, pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f);
            entityendercrystal.setShowBottom(false);
            worldIn.spawnEntity(entityendercrystal);
            if (worldIn.provider instanceof WorldProviderEnd) {
                final DragonFightManager dragonfightmanager = ((WorldProviderEnd)worldIn.provider).getDragonFightManager();
                dragonfightmanager.respawnDragon();
            }
        }
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }
}
