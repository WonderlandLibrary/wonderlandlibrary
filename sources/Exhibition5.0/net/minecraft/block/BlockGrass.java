// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import exhibition.module.Module;
import exhibition.module.impl.render.Xray;
import exhibition.Client;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockGrass extends Block implements IGrowable
{
    public static final PropertyBool SNOWY;
    private static final String __OBFID = "CL_00000251";
    
    protected BlockGrass() {
        super(Material.grass);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockGrass.SNOWY, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final Block var4 = worldIn.getBlockState(pos.offsetUp()).getBlock();
        return state.withProperty(BlockGrass.SNOWY, var4 == Blocks.snow || var4 == Blocks.snow_layer);
    }
    
    @Override
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        return this.getBlockColor();
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return BiomeColorHelper.func_180286_a(worldIn, pos);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.getLightFromNeighbors(pos.offsetUp()) < 4 && worldIn.getBlockState(pos.offsetUp()).getBlock().getLightOpacity() > 2) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
            else if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9) {
                for (int var5 = 0; var5 < 4; ++var5) {
                    final BlockPos var6 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    final Block var7 = worldIn.getBlockState(var6.offsetUp()).getBlock();
                    final IBlockState var8 = worldIn.getBlockState(var6);
                    if (var8.getBlock() == Blocks.dirt && var8.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(var6.offsetUp()) >= 4 && var7.getLightOpacity() <= 2) {
                        worldIn.setBlockState(var6, Blocks.grass.getDefaultState());
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }
    
    @Override
    public boolean isStillGrowing(final World worldIn, final BlockPos p_176473_2_, final IBlockState p_176473_3_, final boolean p_176473_4_) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random p_180670_2_, final BlockPos p_180670_3_, final IBlockState p_180670_4_) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random p_176474_2_, final BlockPos p_176474_3_, final IBlockState p_176474_4_) {
        final BlockPos var5 = p_176474_3_.offsetUp();
        int var6 = 0;
    Label_0009:
        while (var6 < 128) {
            BlockPos var7 = var5;
            while (true) {
                for (int var8 = 0; var8 < var6 / 16; ++var8) {
                    var7 = var7.add(p_176474_2_.nextInt(3) - 1, (p_176474_2_.nextInt(3) - 1) * p_176474_2_.nextInt(3) / 2, p_176474_2_.nextInt(3) - 1);
                    if (worldIn.getBlockState(var7.offsetDown()).getBlock() != Blocks.grass || worldIn.getBlockState(var7).getBlock().isNormalCube()) {
                        ++var6;
                        continue Label_0009;
                    }
                }
                if (worldIn.getBlockState(var7).getBlock().blockMaterial != Material.air) {
                    continue;
                }
                if (p_176474_2_.nextInt(8) == 0) {
                    final BlockFlower.EnumFlowerType var9 = worldIn.getBiomeGenForCoords(var7).pickRandomFlower(p_176474_2_, var7);
                    final BlockFlower var10 = var9.func_176964_a().func_180346_a();
                    final IBlockState var11 = var10.getDefaultState().withProperty(var10.func_176494_l(), var9);
                    if (var10.canBlockStay(worldIn, var7, var11)) {
                        worldIn.setBlockState(var7, var11, 3);
                    }
                    continue;
                }
                final IBlockState var12 = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.field_176497_a, BlockTallGrass.EnumType.GRASS);
                if (Blocks.tallgrass.canBlockStay(worldIn, var7, var12)) {
                    worldIn.setBlockState(var7, var12, 3);
                }
                continue;
            }
        }
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        if (Client.getModuleManager().isEnabled(Xray.class)) {
            return super.getBlockLayer();
        }
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockGrass.SNOWY });
    }
    
    static {
        SNOWY = PropertyBool.create("snowy");
    }
}
