// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import java.util.Iterator;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;
import net.minecraft.block.state.pattern.BlockPattern;

public class BlockPumpkin extends BlockHorizontal
{
    private BlockPattern snowmanBasePattern;
    private BlockPattern snowmanPattern;
    private BlockPattern golemBasePattern;
    private BlockPattern golemPattern;
    private static final Predicate<IBlockState> IS_PUMPKIN;
    
    protected BlockPumpkin() {
        super(Material.GOURD, MapColor.ADOBE);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPumpkin.FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
    }
    
    public boolean canDispenserPlace(final World worldIn, final BlockPos pos) {
        return this.getSnowmanBasePattern().match(worldIn, pos) != null || this.getGolemBasePattern().match(worldIn, pos) != null;
    }
    
    private void trySpawnGolem(final World worldIn, final BlockPos pos) {
        BlockPattern.PatternHelper blockpattern$patternhelper = this.getSnowmanPattern().match(worldIn, pos);
        if (blockpattern$patternhelper != null) {
            for (int i = 0; i < this.getSnowmanPattern().getThumbLength(); ++i) {
                final BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
                worldIn.setBlockState(blockworldstate.getPos(), Blocks.AIR.getDefaultState(), 2);
            }
            final EntitySnowman entitysnowman = new EntitySnowman(worldIn);
            final BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
            entitysnowman.setLocationAndAngles(blockpos1.getX() + 0.5, blockpos1.getY() + 0.05, blockpos1.getZ() + 0.5, 0.0f, 0.0f);
            worldIn.spawnEntity(entitysnowman);
            for (final EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB((Class<? extends EntityPlayerMP>)EntityPlayerMP.class, entitysnowman.getEntityBoundingBox().grow(5.0))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitysnowman);
            }
            for (int l = 0; l < 120; ++l) {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, blockpos1.getX() + worldIn.rand.nextDouble(), blockpos1.getY() + worldIn.rand.nextDouble() * 2.5, blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
            }
            for (int i2 = 0; i2 < this.getSnowmanPattern().getThumbLength(); ++i2) {
                final BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(0, i2, 0);
                worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
            }
        }
        else {
            blockpattern$patternhelper = this.getGolemPattern().match(worldIn, pos);
            if (blockpattern$patternhelper != null) {
                for (int j = 0; j < this.getGolemPattern().getPalmLength(); ++j) {
                    for (int k = 0; k < this.getGolemPattern().getThumbLength(); ++k) {
                        worldIn.setBlockState(blockpattern$patternhelper.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                    }
                }
                final BlockPos blockpos2 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                final EntityIronGolem entityirongolem = new EntityIronGolem(worldIn);
                entityirongolem.setPlayerCreated(true);
                entityirongolem.setLocationAndAngles(blockpos2.getX() + 0.5, blockpos2.getY() + 0.05, blockpos2.getZ() + 0.5, 0.0f, 0.0f);
                worldIn.spawnEntity(entityirongolem);
                for (final EntityPlayerMP entityplayermp2 : worldIn.getEntitiesWithinAABB((Class<? extends EntityPlayerMP>)EntityPlayerMP.class, entityirongolem.getEntityBoundingBox().grow(5.0))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp2, entityirongolem);
                }
                for (int j2 = 0; j2 < 120; ++j2) {
                    worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos2.getX() + worldIn.rand.nextDouble(), blockpos2.getY() + worldIn.rand.nextDouble() * 3.9, blockpos2.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                }
                for (int k2 = 0; k2 < this.getGolemPattern().getPalmLength(); ++k2) {
                    for (int l2 = 0; l2 < this.getGolemPattern().getThumbLength(); ++l2) {
                        final BlockWorldState blockworldstate3 = blockpattern$patternhelper.translateOffset(k2, l2, 0);
                        worldIn.notifyNeighborsRespectDebug(blockworldstate3.getPos(), Blocks.AIR, false);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().material.isReplaceable() && worldIn.getBlockState(pos.down()).isTopSolid();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockPumpkin.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockPumpkin.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockPumpkin.FACING)));
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPumpkin.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPumpkin.FACING, EnumFacing.byHorizontalIndex(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockPumpkin.FACING).getHorizontalIndex();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPumpkin.FACING });
    }
    
    protected BlockPattern getSnowmanBasePattern() {
        if (this.snowmanBasePattern == null) {
            this.snowmanBasePattern = FactoryBlockPattern.start().aisle(" ", "#", "#").where('#', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        }
        return this.snowmanBasePattern;
    }
    
    protected BlockPattern getSnowmanPattern() {
        if (this.snowmanPattern == null) {
            this.snowmanPattern = FactoryBlockPattern.start().aisle("^", "#", "#").where('^', BlockWorldState.hasState(BlockPumpkin.IS_PUMPKIN)).where('#', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        }
        return this.snowmanPattern;
    }
    
    protected BlockPattern getGolemBasePattern() {
        if (this.golemBasePattern == null) {
            this.golemBasePattern = FactoryBlockPattern.start().aisle("~ ~", "###", "~#~").where('#', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState((Predicate<IBlockState>)BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.golemBasePattern;
    }
    
    protected BlockPattern getGolemPattern() {
        if (this.golemPattern == null) {
            this.golemPattern = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(BlockPumpkin.IS_PUMPKIN)).where('#', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState((Predicate<IBlockState>)BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.golemPattern;
    }
    
    static {
        IS_PUMPKIN = (Predicate)new Predicate<IBlockState>() {
            public boolean apply(@Nullable final IBlockState p_apply_1_) {
                return p_apply_1_ != null && (p_apply_1_.getBlock() == Blocks.PUMPKIN || p_apply_1_.getBlock() == Blocks.LIT_PUMPKIN);
            }
        };
    }
}
