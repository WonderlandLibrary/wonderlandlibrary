// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEnderEye extends Item
{
    private static final String __OBFID = "CL_00000026";
    
    public ItemEnderEye() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState var9 = worldIn.getBlockState(pos);
        if (!playerIn.func_175151_a(pos.offset(side), side, stack) || var9.getBlock() != Blocks.end_portal_frame || (boolean)var9.getValue(BlockEndPortalFrame.field_176507_b)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        worldIn.setBlockState(pos, var9.withProperty(BlockEndPortalFrame.field_176507_b, true), 2);
        worldIn.updateComparatorOutputLevel(pos, Blocks.end_portal_frame);
        --stack.stackSize;
        for (int var10 = 0; var10 < 16; ++var10) {
            final double var11 = pos.getX() + (5.0f + Item.itemRand.nextFloat() * 6.0f) / 16.0f;
            final double var12 = pos.getY() + 0.8125f;
            final double var13 = pos.getZ() + (5.0f + Item.itemRand.nextFloat() * 6.0f) / 16.0f;
            final double var14 = 0.0;
            final double var15 = 0.0;
            final double var16 = 0.0;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var11, var12, var13, var14, var15, var16, new int[0]);
        }
        final EnumFacing var17 = (EnumFacing)var9.getValue(BlockEndPortalFrame.field_176508_a);
        int var18 = 0;
        int var19 = 0;
        boolean var20 = false;
        boolean var21 = true;
        final EnumFacing var22 = var17.rotateY();
        for (int var23 = -2; var23 <= 2; ++var23) {
            final BlockPos var24 = pos.offset(var22, var23);
            final IBlockState var25 = worldIn.getBlockState(var24);
            if (var25.getBlock() == Blocks.end_portal_frame) {
                if (!(boolean)var25.getValue(BlockEndPortalFrame.field_176507_b)) {
                    var21 = false;
                    break;
                }
                var19 = var23;
                if (!var20) {
                    var18 = var23;
                    var20 = true;
                }
            }
        }
        if (var21 && var19 == var18 + 2) {
            BlockPos var26 = pos.offset(var17, 4);
            for (int var27 = var18; var27 <= var19; ++var27) {
                final BlockPos var28 = var26.offset(var22, var27);
                final IBlockState var29 = worldIn.getBlockState(var28);
                if (var29.getBlock() != Blocks.end_portal_frame || !(boolean)var29.getValue(BlockEndPortalFrame.field_176507_b)) {
                    var21 = false;
                    break;
                }
            }
            for (int var27 = var18 - 1; var27 <= var19 + 1; var27 += 4) {
                var26 = pos.offset(var22, var27);
                for (int var30 = 1; var30 <= 3; ++var30) {
                    final BlockPos var31 = var26.offset(var17, var30);
                    final IBlockState var32 = worldIn.getBlockState(var31);
                    if (var32.getBlock() != Blocks.end_portal_frame || !(boolean)var32.getValue(BlockEndPortalFrame.field_176507_b)) {
                        var21 = false;
                        break;
                    }
                }
            }
            if (var21) {
                for (int var27 = var18; var27 <= var19; ++var27) {
                    var26 = pos.offset(var22, var27);
                    for (int var30 = 1; var30 <= 3; ++var30) {
                        final BlockPos var31 = var26.offset(var17, var30);
                        worldIn.setBlockState(var31, Blocks.end_portal.getDefaultState(), 2);
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, false);
        if (var4 != null && var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && worldIn.getBlockState(var4.getBlockPos()).getBlock() == Blocks.end_portal_frame) {
            return itemStackIn;
        }
        if (!worldIn.isRemote) {
            final BlockPos var5 = worldIn.func_180499_a("Stronghold", new BlockPos(playerIn));
            if (var5 != null) {
                final EntityEnderEye var6 = new EntityEnderEye(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
                var6.func_180465_a(var5);
                worldIn.spawnEntityInWorld(var6);
                worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (Item.itemRand.nextFloat() * 0.4f + 0.8f));
                worldIn.playAuxSFXAtEntity(null, 1002, new BlockPos(playerIn), 0);
                if (!playerIn.capabilities.isCreativeMode) {
                    --itemStackIn.stackSize;
                }
                playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            }
        }
        return itemStackIn;
    }
}
