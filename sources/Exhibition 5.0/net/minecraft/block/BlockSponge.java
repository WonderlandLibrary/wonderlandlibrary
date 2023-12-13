// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockSponge extends Block
{
    public static final PropertyBool WET_PROP;
    private static final String __OBFID = "CL_00000311";
    
    protected BlockSponge() {
        super(Material.sponge);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSponge.WET_PROP, false));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((boolean)state.getValue(BlockSponge.WET_PROP)) ? 1 : 0;
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setWet(worldIn, pos, state);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.setWet(worldIn, pos, state);
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }
    
    protected void setWet(final World worldIn, final BlockPos p_176311_2_, final IBlockState p_176311_3_) {
        if (!(boolean)p_176311_3_.getValue(BlockSponge.WET_PROP) && this.absorbWater(worldIn, p_176311_2_)) {
            worldIn.setBlockState(p_176311_2_, p_176311_3_.withProperty(BlockSponge.WET_PROP, true), 2);
            worldIn.playAuxSFX(2001, p_176311_2_, Block.getIdFromBlock(Blocks.water));
        }
    }
    
    private boolean absorbWater(final World worldIn, final BlockPos p_176312_2_) {
        final LinkedList var3 = Lists.newLinkedList();
        final ArrayList var4 = Lists.newArrayList();
        var3.add(new Tuple(p_176312_2_, 0));
        int var5 = 0;
        while (!var3.isEmpty()) {
            final Tuple var6 = var3.poll();
            final BlockPos var7 = (BlockPos)var6.getFirst();
            final int var8 = (int)var6.getSecond();
            for (final EnumFacing var12 : EnumFacing.values()) {
                final BlockPos var13 = var7.offset(var12);
                if (worldIn.getBlockState(var13).getBlock().getMaterial() == Material.water) {
                    worldIn.setBlockState(var13, Blocks.air.getDefaultState(), 2);
                    var4.add(var13);
                    ++var5;
                    if (var8 < 6) {
                        var3.add(new Tuple(var13, var8 + 1));
                    }
                }
            }
            if (var5 > 64) {
                break;
            }
        }
        final Iterator var14 = var4.iterator();
        while (var14.hasNext()) {
            final BlockPos var7 = var14.next();
            worldIn.notifyNeighborsOfStateChange(var7, Blocks.air);
        }
        return var5 > 0;
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSponge.WET_PROP, (meta & 0x1) == 0x1);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue(BlockSponge.WET_PROP)) ? 1 : 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSponge.WET_PROP });
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (state.getValue(BlockSponge.WET_PROP)) {
            final EnumFacing var5 = EnumFacing.random(rand);
            if (var5 != EnumFacing.UP && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offset(var5))) {
                double var6 = pos.getX();
                double var7 = pos.getY();
                double var8 = pos.getZ();
                if (var5 == EnumFacing.DOWN) {
                    var7 -= 0.05;
                    var6 += rand.nextDouble();
                    var8 += rand.nextDouble();
                }
                else {
                    var7 += rand.nextDouble() * 0.8;
                    if (var5.getAxis() == EnumFacing.Axis.X) {
                        var8 += rand.nextDouble();
                        if (var5 == EnumFacing.EAST) {
                            ++var6;
                        }
                        else {
                            var6 += 0.05;
                        }
                    }
                    else {
                        var6 += rand.nextDouble();
                        if (var5 == EnumFacing.SOUTH) {
                            ++var8;
                        }
                        else {
                            var8 += 0.05;
                        }
                    }
                }
                worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    static {
        WET_PROP = PropertyBool.create("wet");
    }
}
