// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import java.util.Iterator;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.BlockWorldState;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockSkull extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyBool NODROP;
    private static final Predicate<BlockWorldState> IS_WITHER_SKELETON;
    protected static final AxisAlignedBB DEFAULT_AABB;
    protected static final AxisAlignedBB NORTH_AABB;
    protected static final AxisAlignedBB SOUTH_AABB;
    protected static final AxisAlignedBB WEST_AABB;
    protected static final AxisAlignedBB EAST_AABB;
    private BlockPattern witherBasePattern;
    private BlockPattern witherPattern;
    
    protected BlockSkull() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockSkull.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockSkull.NODROP, false));
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal("tile.skull.skeleton.name");
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean hasCustomBreakingProgress(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue((IProperty<EnumFacing>)BlockSkull.FACING)) {
            default: {
                return BlockSkull.DEFAULT_AABB;
            }
            case NORTH: {
                return BlockSkull.NORTH_AABB;
            }
            case SOUTH: {
                return BlockSkull.SOUTH_AABB;
            }
            case WEST: {
                return BlockSkull.WEST_AABB;
            }
            case EAST: {
                return BlockSkull.EAST_AABB;
            }
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, placer.getHorizontalFacing()).withProperty((IProperty<Comparable>)BlockSkull.NODROP, false);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntitySkull();
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        int i = 0;
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntitySkull) {
            i = ((TileEntitySkull)tileentity).getSkullType();
        }
        return new ItemStack(Items.SKULL, 1, i);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            state = state.withProperty((IProperty<Comparable>)BlockSkull.NODROP, true);
            worldIn.setBlockState(pos, state, 4);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            if (!state.getValue((IProperty<Boolean>)BlockSkull.NODROP)) {
                final TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntitySkull) {
                    final TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
                    final ItemStack itemstack = this.getItem(worldIn, pos, state);
                    if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null) {
                        itemstack.setTagCompound(new NBTTagCompound());
                        final NBTTagCompound nbttagcompound = new NBTTagCompound();
                        NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
                        itemstack.getTagCompound().setTag("SkullOwner", nbttagcompound);
                    }
                    Block.spawnAsEntity(worldIn, pos, itemstack);
                }
            }
            super.breakBlock(worldIn, pos, state);
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.SKULL;
    }
    
    public boolean canDispenserPlace(final World worldIn, final BlockPos pos, final ItemStack stack) {
        return stack.getMetadata() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote && this.getWitherBasePattern().match(worldIn, pos) != null;
    }
    
    public void checkWitherSpawn(final World worldIn, final BlockPos pos, final TileEntitySkull te) {
        if (te.getSkullType() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote) {
            final BlockPattern blockpattern = this.getWitherPattern();
            final BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);
            if (blockpattern$patternhelper != null) {
                for (int i = 0; i < 3; ++i) {
                    final BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, 0, 0);
                    worldIn.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty((IProperty<Comparable>)BlockSkull.NODROP, true), 2);
                }
                for (int j = 0; j < blockpattern.getPalmLength(); ++j) {
                    for (int k = 0; k < blockpattern.getThumbLength(); ++k) {
                        final BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(j, k, 0);
                        worldIn.setBlockState(blockworldstate2.getPos(), Blocks.AIR.getDefaultState(), 2);
                    }
                }
                final BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
                final EntityWither entitywither = new EntityWither(worldIn);
                final BlockPos blockpos2 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                entitywither.setLocationAndAngles(blockpos2.getX() + 0.5, blockpos2.getY() + 0.55, blockpos2.getZ() + 0.5, (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) ? 0.0f : 90.0f, 0.0f);
                entitywither.renderYawOffset = ((blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) ? 0.0f : 90.0f);
                entitywither.ignite();
                for (final EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB((Class<? extends EntityPlayerMP>)EntityPlayerMP.class, entitywither.getEntityBoundingBox().grow(50.0))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitywither);
                }
                worldIn.spawnEntity(entitywither);
                for (int l = 0; l < 120; ++l) {
                    worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos.getX() + worldIn.rand.nextDouble(), blockpos.getY() - 2 + worldIn.rand.nextDouble() * 3.9, blockpos.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                }
                for (int i2 = 0; i2 < blockpattern.getPalmLength(); ++i2) {
                    for (int j2 = 0; j2 < blockpattern.getThumbLength(); ++j2) {
                        final BlockWorldState blockworldstate3 = blockpattern$patternhelper.translateOffset(i2, j2, 0);
                        worldIn.notifyNeighborsRespectDebug(blockworldstate3.getPos(), Blocks.AIR, false);
                    }
                }
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, EnumFacing.byIndex(meta & 0x7)).withProperty((IProperty<Comparable>)BlockSkull.NODROP, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockSkull.FACING).getIndex();
        if (state.getValue((IProperty<Boolean>)BlockSkull.NODROP)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockSkull.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockSkull.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockSkull.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockSkull.FACING, BlockSkull.NODROP });
    }
    
    protected BlockPattern getWitherBasePattern() {
        if (this.witherBasePattern == null) {
            this.witherBasePattern = FactoryBlockPattern.start().aisle("   ", "###", "~#~").where('#', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.SOUL_SAND))).where('~', BlockWorldState.hasState((Predicate<IBlockState>)BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.witherBasePattern;
    }
    
    protected BlockPattern getWitherPattern() {
        if (this.witherPattern == null) {
            this.witherPattern = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState((Predicate<IBlockState>)BlockStateMatcher.forBlock(Blocks.SOUL_SAND))).where('^', BlockSkull.IS_WITHER_SKELETON).where('~', BlockWorldState.hasState((Predicate<IBlockState>)BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }
        return this.witherPattern;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        FACING = BlockDirectional.FACING;
        NODROP = PropertyBool.create("nodrop");
        IS_WITHER_SKELETON = (Predicate)new Predicate<BlockWorldState>() {
            public boolean apply(@Nullable final BlockWorldState p_apply_1_) {
                return p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == Blocks.SKULL && p_apply_1_.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)p_apply_1_.getTileEntity()).getSkullType() == 1;
            }
        };
        DEFAULT_AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.5, 0.75);
        NORTH_AABB = new AxisAlignedBB(0.25, 0.25, 0.5, 0.75, 0.75, 1.0);
        SOUTH_AABB = new AxisAlignedBB(0.25, 0.25, 0.0, 0.75, 0.75, 0.5);
        WEST_AABB = new AxisAlignedBB(0.5, 0.25, 0.25, 1.0, 0.75, 0.75);
        EAST_AABB = new AxisAlignedBB(0.0, 0.25, 0.25, 0.5, 0.75, 0.75);
    }
}
