// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.color;

import net.minecraft.block.material.MapColor;
import net.minecraft.world.World;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ObjectIntIdentityMap;

public class BlockColors
{
    private final ObjectIntIdentityMap<IBlockColor> mapBlockColors;
    
    public BlockColors() {
        this.mapBlockColors = new ObjectIntIdentityMap<IBlockColor>(32);
    }
    
    public static BlockColors init() {
        final BlockColors blockcolors = new BlockColors();
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                final BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = state.getValue(BlockDoublePlant.VARIANT);
                return (worldIn != null && pos != null && (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS || blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.FERN)) ? BiomeColorHelper.getGrassColorAtPos(worldIn, (state.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) ? pos.down() : pos) : -1;
            }
        }, Blocks.DOUBLE_PLANT);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                if (worldIn == null || pos == null) {
                    return -1;
                }
                final TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityFlowerPot) {
                    final Item item = ((TileEntityFlowerPot)tileentity).getFlowerPotItem();
                    final IBlockState iblockstate = Block.getBlockFromItem(item).getDefaultState();
                    return blockcolors.colorMultiplier(iblockstate, worldIn, pos, tintIndex);
                }
                return -1;
            }
        }, Blocks.FLOWER_POT);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                return (worldIn != null && pos != null) ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5, 1.0);
            }
        }, Blocks.GRASS);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                final BlockPlanks.EnumType blockplanks$enumtype = state.getValue(BlockOldLeaf.VARIANT);
                if (blockplanks$enumtype == BlockPlanks.EnumType.SPRUCE) {
                    return ColorizerFoliage.getFoliageColorPine();
                }
                if (blockplanks$enumtype == BlockPlanks.EnumType.BIRCH) {
                    return ColorizerFoliage.getFoliageColorBirch();
                }
                return (worldIn != null && pos != null) ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
            }
        }, Blocks.LEAVES);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                return (worldIn != null && pos != null) ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
            }
        }, Blocks.LEAVES2);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                return (worldIn != null && pos != null) ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : -1;
            }
        }, Blocks.WATER, Blocks.FLOWING_WATER);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                return BlockRedstoneWire.colorMultiplier(state.getValue((IProperty<Integer>)BlockRedstoneWire.POWER));
            }
        }, Blocks.REDSTONE_WIRE);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                return (worldIn != null && pos != null) ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : -1;
            }
        }, Blocks.REEDS);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                final int i = state.getValue((IProperty<Integer>)BlockStem.AGE);
                final int j = i * 32;
                final int k = 255 - i * 8;
                final int l = i * 4;
                return j << 16 | k << 8 | l;
            }
        }, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                if (worldIn != null && pos != null) {
                    return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
                }
                return (state.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.DEAD_BUSH) ? 16777215 : ColorizerGrass.getGrassColor(0.5, 1.0);
            }
        }, Blocks.TALLGRASS);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                return (worldIn != null && pos != null) ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
            }
        }, Blocks.VINE);
        blockcolors.registerBlockColorHandler(new IBlockColor() {
            @Override
            public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess worldIn, @Nullable final BlockPos pos, final int tintIndex) {
                return (worldIn != null && pos != null) ? 2129968 : 7455580;
            }
        }, Blocks.WATERLILY);
        return blockcolors;
    }
    
    public int getColor(final IBlockState state, final World p_189991_2_, final BlockPos p_189991_3_) {
        final IBlockColor iblockcolor = this.mapBlockColors.getByValue(Block.getIdFromBlock(state.getBlock()));
        if (iblockcolor != null) {
            return iblockcolor.colorMultiplier(state, null, null, 0);
        }
        final MapColor mapcolor = state.getMapColor(p_189991_2_, p_189991_3_);
        return (mapcolor != null) ? mapcolor.colorValue : -1;
    }
    
    public int colorMultiplier(final IBlockState state, @Nullable final IBlockAccess blockAccess, @Nullable final BlockPos pos, final int tintIndex) {
        final IBlockColor iblockcolor = this.mapBlockColors.getByValue(Block.getIdFromBlock(state.getBlock()));
        return (iblockcolor == null) ? -1 : iblockcolor.colorMultiplier(state, blockAccess, pos, tintIndex);
    }
    
    public void registerBlockColorHandler(final IBlockColor blockColor, final Block... blocksIn) {
        for (final Block block : blocksIn) {
            this.mapBlockColors.put(blockColor, Block.getIdFromBlock(block));
        }
    }
}
