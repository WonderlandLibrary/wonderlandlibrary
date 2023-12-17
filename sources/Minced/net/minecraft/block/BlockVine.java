// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLivingBase;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;

public class BlockVine extends Block
{
    public static final PropertyBool UP;
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    public static final PropertyBool[] ALL_FACES;
    protected static final AxisAlignedBB UP_AABB;
    protected static final AxisAlignedBB WEST_AABB;
    protected static final AxisAlignedBB EAST_AABB;
    protected static final AxisAlignedBB NORTH_AABB;
    protected static final AxisAlignedBB SOUTH_AABB;
    
    public BlockVine() {
        super(Material.VINE);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockVine.UP, false).withProperty((IProperty<Comparable>)BlockVine.NORTH, false).withProperty((IProperty<Comparable>)BlockVine.EAST, false).withProperty((IProperty<Comparable>)BlockVine.SOUTH, false).withProperty((IProperty<Comparable>)BlockVine.WEST, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockVine.NULL_AABB;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source, final BlockPos pos) {
        state = state.getActualState(source, pos);
        int i = 0;
        AxisAlignedBB axisalignedbb = BlockVine.FULL_BLOCK_AABB;
        if (state.getValue((IProperty<Boolean>)BlockVine.UP)) {
            axisalignedbb = BlockVine.UP_AABB;
            ++i;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.NORTH)) {
            axisalignedbb = BlockVine.NORTH_AABB;
            ++i;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.EAST)) {
            axisalignedbb = BlockVine.EAST_AABB;
            ++i;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.SOUTH)) {
            axisalignedbb = BlockVine.SOUTH_AABB;
            ++i;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.WEST)) {
            axisalignedbb = BlockVine.WEST_AABB;
            ++i;
        }
        return (i == 1) ? axisalignedbb : BlockVine.FULL_BLOCK_AABB;
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final BlockPos blockpos = pos.up();
        return state.withProperty((IProperty<Comparable>)BlockVine.UP, worldIn.getBlockState(blockpos).getBlockFaceShape(worldIn, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID);
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
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return side != EnumFacing.DOWN && side != EnumFacing.UP && this.canAttachTo(worldIn, pos, side);
    }
    
    public boolean canAttachTo(final World p_193395_1_, final BlockPos p_193395_2_, final EnumFacing p_193395_3_) {
        final Block block = p_193395_1_.getBlockState(p_193395_2_.up()).getBlock();
        return this.isAcceptableNeighbor(p_193395_1_, p_193395_2_.offset(p_193395_3_.getOpposite()), p_193395_3_) && (block == Blocks.AIR || block == Blocks.VINE || this.isAcceptableNeighbor(p_193395_1_, p_193395_2_.up(), EnumFacing.UP));
    }
    
    private boolean isAcceptableNeighbor(final World p_193396_1_, final BlockPos p_193396_2_, final EnumFacing p_193396_3_) {
        final IBlockState iblockstate = p_193396_1_.getBlockState(p_193396_2_);
        return iblockstate.getBlockFaceShape(p_193396_1_, p_193396_2_, p_193396_3_) == BlockFaceShape.SOLID && !isExceptBlockForAttaching(iblockstate.getBlock());
    }
    
    protected static boolean isExceptBlockForAttaching(final Block p_193397_0_) {
        return p_193397_0_ instanceof BlockShulkerBox || p_193397_0_ == Blocks.BEACON || p_193397_0_ == Blocks.CAULDRON || p_193397_0_ == Blocks.GLASS || p_193397_0_ == Blocks.STAINED_GLASS || p_193397_0_ == Blocks.PISTON || p_193397_0_ == Blocks.STICKY_PISTON || p_193397_0_ == Blocks.PISTON_HEAD || p_193397_0_ == Blocks.TRAPDOOR;
    }
    
    private boolean recheckGrownSides(final World worldIn, final BlockPos pos, IBlockState state) {
        final IBlockState iblockstate = state;
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final PropertyBool propertybool = getPropertyFor(enumfacing);
            if (state.getValue((IProperty<Boolean>)propertybool) && !this.canAttachTo(worldIn, pos, enumfacing.getOpposite())) {
                final IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
                if (iblockstate2.getBlock() == this && iblockstate2.getValue((IProperty<Boolean>)propertybool)) {
                    continue;
                }
                state = state.withProperty((IProperty<Comparable>)propertybool, false);
            }
        }
        if (getNumGrownFaces(state) == 0) {
            return false;
        }
        if (iblockstate != state) {
            worldIn.setBlockState(pos, state, 2);
        }
        return true;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote && !this.recheckGrownSides(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0) {
            final int i = 4;
            int j = 5;
            boolean flag = false;
        Label_0111:
            for (int k = -4; k <= 4; ++k) {
                for (int l = -4; l <= 4; ++l) {
                    for (int i2 = -1; i2 <= 1; ++i2) {
                        if (worldIn.getBlockState(pos.add(k, i2, l)).getBlock() == this && --j <= 0) {
                            flag = true;
                            break Label_0111;
                        }
                    }
                }
            }
            final EnumFacing enumfacing1 = EnumFacing.random(rand);
            final BlockPos blockpos2 = pos.up();
            if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos2)) {
                IBlockState iblockstate2 = state;
                for (final EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                    if (rand.nextBoolean() && this.canAttachTo(worldIn, blockpos2, enumfacing2.getOpposite())) {
                        iblockstate2 = iblockstate2.withProperty((IProperty<Comparable>)getPropertyFor(enumfacing2), true);
                    }
                    else {
                        iblockstate2 = iblockstate2.withProperty((IProperty<Comparable>)getPropertyFor(enumfacing2), false);
                    }
                }
                if (iblockstate2.getValue((IProperty<Boolean>)BlockVine.NORTH) || iblockstate2.getValue((IProperty<Boolean>)BlockVine.EAST) || iblockstate2.getValue((IProperty<Boolean>)BlockVine.SOUTH) || iblockstate2.getValue((IProperty<Boolean>)BlockVine.WEST)) {
                    worldIn.setBlockState(blockpos2, iblockstate2, 2);
                }
            }
            else if (enumfacing1.getAxis().isHorizontal() && !state.getValue((IProperty<Boolean>)getPropertyFor(enumfacing1))) {
                if (!flag) {
                    final BlockPos blockpos3 = pos.offset(enumfacing1);
                    final IBlockState iblockstate3 = worldIn.getBlockState(blockpos3);
                    final Block block1 = iblockstate3.getBlock();
                    if (block1.material == Material.AIR) {
                        final EnumFacing enumfacing3 = enumfacing1.rotateY();
                        final EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
                        final boolean flag2 = state.getValue((IProperty<Boolean>)getPropertyFor(enumfacing3));
                        final boolean flag3 = state.getValue((IProperty<Boolean>)getPropertyFor(enumfacing4));
                        final BlockPos blockpos4 = blockpos3.offset(enumfacing3);
                        final BlockPos blockpos5 = blockpos3.offset(enumfacing4);
                        if (flag2 && this.canAttachTo(worldIn, blockpos4.offset(enumfacing3), enumfacing3)) {
                            worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty((IProperty<Comparable>)getPropertyFor(enumfacing3), true), 2);
                        }
                        else if (flag3 && this.canAttachTo(worldIn, blockpos5.offset(enumfacing4), enumfacing4)) {
                            worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty((IProperty<Comparable>)getPropertyFor(enumfacing4), true), 2);
                        }
                        else if (flag2 && worldIn.isAirBlock(blockpos4) && this.canAttachTo(worldIn, blockpos4, enumfacing1)) {
                            worldIn.setBlockState(blockpos4, this.getDefaultState().withProperty((IProperty<Comparable>)getPropertyFor(enumfacing1.getOpposite()), true), 2);
                        }
                        else if (flag3 && worldIn.isAirBlock(blockpos5) && this.canAttachTo(worldIn, blockpos5, enumfacing1)) {
                            worldIn.setBlockState(blockpos5, this.getDefaultState().withProperty((IProperty<Comparable>)getPropertyFor(enumfacing1.getOpposite()), true), 2);
                        }
                    }
                    else if (iblockstate3.getBlockFaceShape(worldIn, blockpos3, enumfacing1) == BlockFaceShape.SOLID) {
                        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)getPropertyFor(enumfacing1), true), 2);
                    }
                }
            }
            else if (pos.getY() > 1) {
                final BlockPos blockpos6 = pos.down();
                final IBlockState iblockstate4 = worldIn.getBlockState(blockpos6);
                final Block block2 = iblockstate4.getBlock();
                if (block2.material == Material.AIR) {
                    IBlockState iblockstate5 = state;
                    for (final EnumFacing enumfacing5 : EnumFacing.Plane.HORIZONTAL) {
                        if (rand.nextBoolean()) {
                            iblockstate5 = iblockstate5.withProperty((IProperty<Comparable>)getPropertyFor(enumfacing5), false);
                        }
                    }
                    if (iblockstate5.getValue((IProperty<Boolean>)BlockVine.NORTH) || iblockstate5.getValue((IProperty<Boolean>)BlockVine.EAST) || iblockstate5.getValue((IProperty<Boolean>)BlockVine.SOUTH) || iblockstate5.getValue((IProperty<Boolean>)BlockVine.WEST)) {
                        worldIn.setBlockState(blockpos6, iblockstate5, 2);
                    }
                }
                else if (block2 == this) {
                    IBlockState iblockstate6 = iblockstate4;
                    for (final EnumFacing enumfacing6 : EnumFacing.Plane.HORIZONTAL) {
                        final PropertyBool propertybool = getPropertyFor(enumfacing6);
                        if (rand.nextBoolean() && state.getValue((IProperty<Boolean>)propertybool)) {
                            iblockstate6 = iblockstate6.withProperty((IProperty<Comparable>)propertybool, true);
                        }
                    }
                    if (iblockstate6.getValue((IProperty<Boolean>)BlockVine.NORTH) || iblockstate6.getValue((IProperty<Boolean>)BlockVine.EAST) || iblockstate6.getValue((IProperty<Boolean>)BlockVine.SOUTH) || iblockstate6.getValue((IProperty<Boolean>)BlockVine.WEST)) {
                        worldIn.setBlockState(blockpos6, iblockstate6, 2);
                    }
                }
            }
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState iblockstate = this.getDefaultState().withProperty((IProperty<Comparable>)BlockVine.UP, false).withProperty((IProperty<Comparable>)BlockVine.NORTH, false).withProperty((IProperty<Comparable>)BlockVine.EAST, false).withProperty((IProperty<Comparable>)BlockVine.SOUTH, false).withProperty((IProperty<Comparable>)BlockVine.WEST, false);
        return facing.getAxis().isHorizontal() ? iblockstate.withProperty((IProperty<Comparable>)getPropertyFor(facing.getOpposite()), true) : iblockstate;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.AIR;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(StatList.getBlockStats(this));
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.VINE, 1, 0));
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockVine.SOUTH, (meta & 0x1) > 0).withProperty((IProperty<Comparable>)BlockVine.WEST, (meta & 0x2) > 0).withProperty((IProperty<Comparable>)BlockVine.NORTH, (meta & 0x4) > 0).withProperty((IProperty<Comparable>)BlockVine.EAST, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue((IProperty<Boolean>)BlockVine.SOUTH)) {
            i |= 0x1;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.WEST)) {
            i |= 0x2;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.NORTH)) {
            i |= 0x4;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.EAST)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockVine.UP, BlockVine.NORTH, BlockVine.EAST, BlockVine.SOUTH, BlockVine.WEST });
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180: {
                return state.withProperty((IProperty<Comparable>)BlockVine.NORTH, (Comparable)state.getValue((IProperty<V>)BlockVine.SOUTH)).withProperty((IProperty<Comparable>)BlockVine.EAST, (Comparable)state.getValue((IProperty<V>)BlockVine.WEST)).withProperty((IProperty<Comparable>)BlockVine.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockVine.NORTH)).withProperty((IProperty<Comparable>)BlockVine.WEST, (Comparable)state.getValue((IProperty<V>)BlockVine.EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return state.withProperty((IProperty<Comparable>)BlockVine.NORTH, (Comparable)state.getValue((IProperty<V>)BlockVine.EAST)).withProperty((IProperty<Comparable>)BlockVine.EAST, (Comparable)state.getValue((IProperty<V>)BlockVine.SOUTH)).withProperty((IProperty<Comparable>)BlockVine.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockVine.WEST)).withProperty((IProperty<Comparable>)BlockVine.WEST, (Comparable)state.getValue((IProperty<V>)BlockVine.NORTH));
            }
            case CLOCKWISE_90: {
                return state.withProperty((IProperty<Comparable>)BlockVine.NORTH, (Comparable)state.getValue((IProperty<V>)BlockVine.WEST)).withProperty((IProperty<Comparable>)BlockVine.EAST, (Comparable)state.getValue((IProperty<V>)BlockVine.NORTH)).withProperty((IProperty<Comparable>)BlockVine.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockVine.EAST)).withProperty((IProperty<Comparable>)BlockVine.WEST, (Comparable)state.getValue((IProperty<V>)BlockVine.SOUTH));
            }
            default: {
                return state;
            }
        }
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                return state.withProperty((IProperty<Comparable>)BlockVine.NORTH, (Comparable)state.getValue((IProperty<V>)BlockVine.SOUTH)).withProperty((IProperty<Comparable>)BlockVine.SOUTH, (Comparable)state.getValue((IProperty<V>)BlockVine.NORTH));
            }
            case FRONT_BACK: {
                return state.withProperty((IProperty<Comparable>)BlockVine.EAST, (Comparable)state.getValue((IProperty<V>)BlockVine.WEST)).withProperty((IProperty<Comparable>)BlockVine.WEST, (Comparable)state.getValue((IProperty<V>)BlockVine.EAST));
            }
            default: {
                return super.withMirror(state, mirrorIn);
            }
        }
    }
    
    public static PropertyBool getPropertyFor(final EnumFacing side) {
        switch (side) {
            case UP: {
                return BlockVine.UP;
            }
            case NORTH: {
                return BlockVine.NORTH;
            }
            case SOUTH: {
                return BlockVine.SOUTH;
            }
            case WEST: {
                return BlockVine.WEST;
            }
            case EAST: {
                return BlockVine.EAST;
            }
            default: {
                throw new IllegalArgumentException(side + " is an invalid choice");
            }
        }
    }
    
    public static int getNumGrownFaces(final IBlockState state) {
        int i = 0;
        for (final PropertyBool propertybool : BlockVine.ALL_FACES) {
            if (state.getValue((IProperty<Boolean>)propertybool)) {
                ++i;
            }
        }
        return i;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        UP = PropertyBool.create("up");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        ALL_FACES = new PropertyBool[] { BlockVine.UP, BlockVine.NORTH, BlockVine.SOUTH, BlockVine.WEST, BlockVine.EAST };
        UP_AABB = new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
        WEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0625, 1.0, 1.0);
        EAST_AABB = new AxisAlignedBB(0.9375, 0.0, 0.0, 1.0, 1.0, 1.0);
        NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.0625);
        SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.9375, 1.0, 1.0, 1.0);
    }
}
