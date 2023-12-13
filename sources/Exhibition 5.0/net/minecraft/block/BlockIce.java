// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.world.EnumSkyBlock;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockIce extends BlockBreakable
{
    private static final String __OBFID = "CL_00000259";
    
    public BlockIce() {
        super(Material.ice, false);
        this.slipperiness = 0.98f;
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te) {
        playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        playerIn.addExhaustion(0.025f);
        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(playerIn)) {
            final ItemStack var8 = this.createStackedBlock(state);
            if (var8 != null) {
                Block.spawnAsEntity(worldIn, pos, var8);
            }
        }
        else {
            if (worldIn.provider.func_177500_n()) {
                worldIn.setBlockToAir(pos);
                return;
            }
            final int var9 = EnchantmentHelper.getFortuneModifier(playerIn);
            this.dropBlockAsItem(worldIn, pos, state, var9);
            final Material var10 = worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial();
            if (var10.blocksMovement() || var10.isLiquid()) {
                worldIn.setBlockState(pos, Blocks.flowing_water.getDefaultState());
            }
        }
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - this.getLightOpacity()) {
            if (worldIn.provider.func_177500_n()) {
                worldIn.setBlockToAir(pos);
            }
            else {
                this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
                worldIn.setBlockState(pos, Blocks.water.getDefaultState());
            }
        }
    }
    
    @Override
    public int getMobilityFlag() {
        return 0;
    }
}
