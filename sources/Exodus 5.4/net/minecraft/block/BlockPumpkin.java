/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class BlockPumpkin
extends BlockDirectional {
    private BlockPattern snowmanPattern;
    private BlockPattern golemPattern;
    private BlockPattern golemBasePattern;
    private static final Predicate<IBlockState> field_181085_Q = new Predicate<IBlockState>(){

        public boolean apply(IBlockState iBlockState) {
            return iBlockState != null && (iBlockState.getBlock() == Blocks.pumpkin || iBlockState.getBlock() == Blocks.lit_pumpkin);
        }
    };
    private BlockPattern snowmanBasePattern;

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return world.getBlockState((BlockPos)blockPos).getBlock().blockMaterial.isReplaceable() && World.doesBlockHaveSolidTopSurface(world, blockPos.down());
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(FACING).getHorizontalIndex();
    }

    public boolean canDispenserPlace(World world, BlockPos blockPos) {
        return this.getSnowmanBasePattern().match(world, blockPos) != null || this.getGolemBasePattern().match(world, blockPos) != null;
    }

    protected BlockPattern getSnowmanPattern() {
        if (this.snowmanPattern == null) {
            this.snowmanPattern = FactoryBlockPattern.start().aisle("^", "#", "#").where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }
        return this.snowmanPattern;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    protected BlockPattern getSnowmanBasePattern() {
        if (this.snowmanBasePattern == null) {
            this.snowmanBasePattern = FactoryBlockPattern.start().aisle(" ", "#", "#").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }
        return this.snowmanBasePattern;
    }

    protected BlockPumpkin() {
        super(Material.gourd, MapColor.adobeColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    protected BlockPattern getGolemBasePattern() {
        if (this.golemBasePattern == null) {
            this.golemBasePattern = FactoryBlockPattern.start().aisle("~ ~", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.golemBasePattern;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(n));
    }

    private void trySpawnGolem(World world, BlockPos blockPos) {
        block9: {
            BlockPattern.PatternHelper patternHelper;
            block8: {
                Object object;
                patternHelper = this.getSnowmanPattern().match(world, blockPos);
                if (patternHelper == null) break block8;
                int n = 0;
                while (n < this.getSnowmanPattern().getThumbLength()) {
                    object = patternHelper.translateOffset(0, n, 0);
                    world.setBlockState(((BlockWorldState)object).getPos(), Blocks.air.getDefaultState(), 2);
                    ++n;
                }
                EntitySnowman entitySnowman = new EntitySnowman(world);
                object = patternHelper.translateOffset(0, 2, 0).getPos();
                entitySnowman.setLocationAndAngles((double)((Vec3i)object).getX() + 0.5, (double)((Vec3i)object).getY() + 0.05, (double)((Vec3i)object).getZ() + 0.5, 0.0f, 0.0f);
                world.spawnEntityInWorld(entitySnowman);
                int n2 = 0;
                while (n2 < 120) {
                    world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)((Vec3i)object).getX() + world.rand.nextDouble(), (double)((Vec3i)object).getY() + world.rand.nextDouble() * 2.5, (double)((Vec3i)object).getZ() + world.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                    ++n2;
                }
                n2 = 0;
                while (n2 < this.getSnowmanPattern().getThumbLength()) {
                    BlockWorldState blockWorldState = patternHelper.translateOffset(0, n2, 0);
                    world.notifyNeighborsRespectDebug(blockWorldState.getPos(), Blocks.air);
                    ++n2;
                }
                break block9;
            }
            patternHelper = this.getGolemPattern().match(world, blockPos);
            if (patternHelper == null) break block9;
            int n = 0;
            while (n < this.getGolemPattern().getPalmLength()) {
                int n3 = 0;
                while (n3 < this.getGolemPattern().getThumbLength()) {
                    world.setBlockState(patternHelper.translateOffset(n, n3, 0).getPos(), Blocks.air.getDefaultState(), 2);
                    ++n3;
                }
                ++n;
            }
            BlockPos blockPos2 = patternHelper.translateOffset(1, 2, 0).getPos();
            EntityIronGolem entityIronGolem = new EntityIronGolem(world);
            entityIronGolem.setPlayerCreated(true);
            entityIronGolem.setLocationAndAngles((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.05, (double)blockPos2.getZ() + 0.5, 0.0f, 0.0f);
            world.spawnEntityInWorld(entityIronGolem);
            int n4 = 0;
            while (n4 < 120) {
                world.spawnParticle(EnumParticleTypes.SNOWBALL, (double)blockPos2.getX() + world.rand.nextDouble(), (double)blockPos2.getY() + world.rand.nextDouble() * 3.9, (double)blockPos2.getZ() + world.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                ++n4;
            }
            n4 = 0;
            while (n4 < this.getGolemPattern().getPalmLength()) {
                int n5 = 0;
                while (n5 < this.getGolemPattern().getThumbLength()) {
                    BlockWorldState blockWorldState = patternHelper.translateOffset(n4, n5, 0);
                    world.notifyNeighborsRespectDebug(blockWorldState.getPos(), Blocks.air);
                    ++n5;
                }
                ++n4;
            }
        }
    }

    protected BlockPattern getGolemPattern() {
        if (this.golemPattern == null) {
            this.golemPattern = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.golemPattern;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, entityLivingBase.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        super.onBlockAdded(world, blockPos, iBlockState);
        this.trySpawnGolem(world, blockPos);
    }
}

