// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockGrass extends Block implements IGrowable
{
    public static final PropertyBool SNOWY;
    
    protected BlockGrass() {
        super(Material.GRASS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockGrass.SNOWY, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos.up()).getBlock();
        return state.withProperty((IProperty<Comparable>)BlockGrass.SNOWY, block == Blocks.SNOW || block == Blocks.SNOW_LAYER);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getLightOpacity() > 2) {
                worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
            }
            else if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                for (int i = 0; i < 4; ++i) {
                    final BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
                        return;
                    }
                    final IBlockState iblockstate = worldIn.getBlockState(blockpos.up());
                    final IBlockState iblockstate2 = worldIn.getBlockState(blockpos);
                    if (iblockstate2.getBlock() == Blocks.DIRT && iblockstate2.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && iblockstate.getLightOpacity() <= 2) {
                        worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState());
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        final BlockPos blockpos = pos.up();
        int i = 0;
    Label_0257_Outer:
        while (i < 128) {
            BlockPos blockpos2 = blockpos;
            int j = 0;
            while (true) {
                while (j < i / 16) {
                    blockpos2 = blockpos2.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                    if (worldIn.getBlockState(blockpos2.down()).getBlock() == Blocks.GRASS) {
                        if (!worldIn.getBlockState(blockpos2).isNormalCube()) {
                            ++j;
                            continue Label_0257_Outer;
                        }
                    }
                    ++i;
                    continue Label_0257_Outer;
                }
                if (worldIn.getBlockState(blockpos2).getBlock().material != Material.AIR) {
                    continue;
                }
                if (rand.nextInt(8) == 0) {
                    final BlockFlower.EnumFlowerType blockflower$enumflowertype = worldIn.getBiome(blockpos2).pickRandomFlower(rand, blockpos2);
                    final BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
                    final IBlockState iblockstate = blockflower.getDefaultState().withProperty(blockflower.getTypeProperty(), blockflower$enumflowertype);
                    if (blockflower.canBlockStay(worldIn, blockpos2, iblockstate)) {
                        worldIn.setBlockState(blockpos2, iblockstate, 3);
                    }
                    continue;
                }
                final IBlockState iblockstate2 = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
                if (Blocks.TALLGRASS.canBlockStay(worldIn, blockpos2, iblockstate2)) {
                    worldIn.setBlockState(blockpos2, iblockstate2, 3);
                }
                continue;
            }
        }
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockGrass.SNOWY });
    }
    
    static {
        SNOWY = PropertyBool.create("snowy");
    }
}
