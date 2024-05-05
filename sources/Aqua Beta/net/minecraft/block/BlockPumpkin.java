package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPumpkin
extends BlockDirectional {
    private BlockPattern snowmanBasePattern;
    private BlockPattern snowmanPattern;
    private BlockPattern golemBasePattern;
    private BlockPattern golemPattern;
    private static final Predicate<IBlockState> field_181085_Q = new /* Unavailable Anonymous Inner Class!! */;

    protected BlockPumpkin() {
        super(Material.gourd, MapColor.adobeColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
    }

    public boolean canDispenserPlace(World worldIn, BlockPos pos) {
        return this.getSnowmanBasePattern().match(worldIn, pos) != null || this.getGolemBasePattern().match(worldIn, pos) != null;
    }

    private void trySpawnGolem(World worldIn, BlockPos pos) {
        block9: {
            BlockPattern.PatternHelper blockpattern$patternhelper;
            block8: {
                blockpattern$patternhelper = this.getSnowmanPattern().match(worldIn, pos);
                if (blockpattern$patternhelper == null) break block8;
                for (int i = 0; i < this.getSnowmanPattern().getThumbLength(); ++i) {
                    BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
                    worldIn.setBlockState(blockworldstate.getPos(), Blocks.air.getDefaultState(), 2);
                }
                EntitySnowman entitysnowman = new EntitySnowman(worldIn);
                BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
                entitysnowman.setLocationAndAngles((double)blockpos1.getX() + 0.5, (double)blockpos1.getY() + 0.05, (double)blockpos1.getZ() + 0.5, 0.0f, 0.0f);
                worldIn.spawnEntityInWorld((Entity)entitysnowman);
                for (int j = 0; j < 120; ++j) {
                    worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                }
                for (int i1 = 0; i1 < this.getSnowmanPattern().getThumbLength(); ++i1) {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(0, i1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.air);
                }
                break block9;
            }
            blockpattern$patternhelper = this.getGolemPattern().match(worldIn, pos);
            if (blockpattern$patternhelper == null) break block9;
            for (int k = 0; k < this.getGolemPattern().getPalmLength(); ++k) {
                for (int l = 0; l < this.getGolemPattern().getThumbLength(); ++l) {
                    worldIn.setBlockState(blockpattern$patternhelper.translateOffset(k, l, 0).getPos(), Blocks.air.getDefaultState(), 2);
                }
            }
            BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
            EntityIronGolem entityirongolem = new EntityIronGolem(worldIn);
            entityirongolem.setPlayerCreated(true);
            entityirongolem.setLocationAndAngles((double)blockpos.getX() + 0.5, (double)blockpos.getY() + 0.05, (double)blockpos.getZ() + 0.5, 0.0f, 0.0f);
            worldIn.spawnEntityInWorld((Entity)entityirongolem);
            for (int j1 = 0; j1 < 120; ++j1) {
                worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double)blockpos.getX() + worldIn.rand.nextDouble(), (double)blockpos.getY() + worldIn.rand.nextDouble() * 3.9, (double)blockpos.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
            }
            for (int k1 = 0; k1 < this.getGolemPattern().getPalmLength(); ++k1) {
                for (int l1 = 0; l1 < this.getGolemPattern().getThumbLength(); ++l1) {
                    BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
                }
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState((BlockPos)pos).getBlock().blockMaterial.isReplaceable() && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down());
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING});
    }

    protected BlockPattern getSnowmanBasePattern() {
        if (this.snowmanBasePattern == null) {
            this.snowmanBasePattern = FactoryBlockPattern.start().aisle(new String[]{" ", "#", "#"}).where('#', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock((Block)Blocks.snow))).build();
        }
        return this.snowmanBasePattern;
    }

    protected BlockPattern getSnowmanPattern() {
        if (this.snowmanPattern == null) {
            this.snowmanPattern = FactoryBlockPattern.start().aisle(new String[]{"^", "#", "#"}).where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock((Block)Blocks.snow))).build();
        }
        return this.snowmanPattern;
    }

    protected BlockPattern getGolemBasePattern() {
        if (this.golemBasePattern == null) {
            this.golemBasePattern = FactoryBlockPattern.start().aisle(new String[]{"~ ~", "###", "~#~"}).where('#', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock((Block)Blocks.iron_block))).where('~', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock((Block)Blocks.air))).build();
        }
        return this.golemBasePattern;
    }

    protected BlockPattern getGolemPattern() {
        if (this.golemPattern == null) {
            this.golemPattern = FactoryBlockPattern.start().aisle(new String[]{"~^~", "###", "~#~"}).where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock((Block)Blocks.iron_block))).where('~', BlockWorldState.hasState((Predicate)BlockStateHelper.forBlock((Block)Blocks.air))).build();
        }
        return this.golemPattern;
    }
}
