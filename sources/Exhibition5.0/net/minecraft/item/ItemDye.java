// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.block.IGrowable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemDye extends Item
{
    public static final int[] dyeColors;
    private static final String __OBFID = "CL_00000022";
    
    public ItemDye() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        final int var2 = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumDyeColor.func_176766_a(var2).func_176762_d();
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
            return false;
        }
        final EnumDyeColor var9 = EnumDyeColor.func_176766_a(stack.getMetadata());
        if (var9 == EnumDyeColor.WHITE) {
            if (func_179234_a(stack, worldIn, pos)) {
                if (!worldIn.isRemote) {
                    worldIn.playAuxSFX(2005, pos, 0);
                }
                return true;
            }
        }
        else if (var9 == EnumDyeColor.BROWN) {
            final IBlockState var10 = worldIn.getBlockState(pos);
            final Block var11 = var10.getBlock();
            if (var11 == Blocks.log && var10.getValue(BlockPlanks.VARIANT_PROP) == BlockPlanks.EnumType.JUNGLE) {
                if (side == EnumFacing.DOWN) {
                    return false;
                }
                if (side == EnumFacing.UP) {
                    return false;
                }
                pos = pos.offset(side);
                if (worldIn.isAirBlock(pos)) {
                    final IBlockState var12 = Blocks.cocoa.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);
                    worldIn.setBlockState(pos, var12, 2);
                    if (!playerIn.capabilities.isCreativeMode) {
                        --stack.stackSize;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean func_179234_a(final ItemStack p_179234_0_, final World worldIn, final BlockPos pos) {
        final IBlockState var3 = worldIn.getBlockState(pos);
        if (var3.getBlock() instanceof IGrowable) {
            final IGrowable var4 = (IGrowable)var3.getBlock();
            if (var4.isStillGrowing(worldIn, pos, var3, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (var4.canUseBonemeal(worldIn, worldIn.rand, pos, var3)) {
                        var4.grow(worldIn, worldIn.rand, pos, var3);
                    }
                    --p_179234_0_.stackSize;
                }
                return true;
            }
        }
        return false;
    }
    
    public static void func_180617_a(final World worldIn, final BlockPos p_180617_1_, int p_180617_2_) {
        if (p_180617_2_ == 0) {
            p_180617_2_ = 15;
        }
        final Block var3 = worldIn.getBlockState(p_180617_1_).getBlock();
        if (var3.getMaterial() != Material.air) {
            var3.setBlockBoundsBasedOnState(worldIn, p_180617_1_);
            for (int var4 = 0; var4 < p_180617_2_; ++var4) {
                final double var5 = Item.itemRand.nextGaussian() * 0.02;
                final double var6 = Item.itemRand.nextGaussian() * 0.02;
                final double var7 = Item.itemRand.nextGaussian() * 0.02;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, p_180617_1_.getX() + Item.itemRand.nextFloat(), p_180617_1_.getY() + Item.itemRand.nextFloat() * var3.getBlockBoundsMaxY(), p_180617_1_.getZ() + Item.itemRand.nextFloat(), var5, var6, var7, new int[0]);
            }
        }
    }
    
    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target) {
        if (target instanceof EntitySheep) {
            final EntitySheep var4 = (EntitySheep)target;
            final EnumDyeColor var5 = EnumDyeColor.func_176766_a(stack.getMetadata());
            if (!var4.getSheared() && var4.func_175509_cj() != var5) {
                var4.func_175512_b(var5);
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List subItems) {
        for (int var4 = 0; var4 < 16; ++var4) {
            subItems.add(new ItemStack(itemIn, 1, var4));
        }
    }
    
    static {
        dyeColors = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320 };
    }
}
