/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class ItemEndCrystal
extends Item {
    public ItemEndCrystal() {
        this.setUnlocalizedName("end_crystal");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        double d2;
        double d1;
        ItemStack itemstack;
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        if (iblockstate.getBlock() != Blocks.OBSIDIAN && iblockstate.getBlock() != Blocks.BEDROCK) {
            return EnumActionResult.FAIL;
        }
        BlockPos blockpos = worldIn.up();
        if (!stack.canPlayerEdit(blockpos, hand, itemstack = stack.getHeldItem(pos))) {
            return EnumActionResult.FAIL;
        }
        BlockPos blockpos1 = blockpos.up();
        boolean flag = !playerIn.isAirBlock(blockpos) && !playerIn.getBlockState(blockpos).getBlock().isReplaceable(playerIn, blockpos);
        if (flag |= !playerIn.isAirBlock(blockpos1) && !playerIn.getBlockState(blockpos1).getBlock().isReplaceable(playerIn, blockpos1)) {
            return EnumActionResult.FAIL;
        }
        double d0 = blockpos.getX();
        List<Entity> list = playerIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d1 = (double)blockpos.getY(), d2 = (double)blockpos.getZ(), d0 + 1.0, d1 + 2.0, d2 + 1.0));
        if (!list.isEmpty()) {
            return EnumActionResult.FAIL;
        }
        if (!playerIn.isRemote) {
            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(playerIn, (float)worldIn.getX() + 0.5f, worldIn.getY() + 1, (float)worldIn.getZ() + 0.5f);
            entityendercrystal.setShowBottom(false);
            playerIn.spawnEntityInWorld(entityendercrystal);
            if (playerIn.provider instanceof WorldProviderEnd) {
                DragonFightManager dragonfightmanager = ((WorldProviderEnd)playerIn.provider).getDragonFightManager();
                dragonfightmanager.respawnDragon();
            }
        }
        itemstack.func_190918_g(1);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}

