/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine
extends Block {
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool[] ALL_FACES = new PropertyBool[]{UP, NORTH, SOUTH, WEST, EAST};
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0625, 1.0, 1.0);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375, 0.0, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.0625);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.9375, 1.0, 1.0, 1.0);

    public BlockVine() {
        super(Material.VINE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = state.getActualState(source, pos);
        int i = 0;
        AxisAlignedBB axisalignedbb = FULL_BLOCK_AABB;
        if (state.getValue(UP).booleanValue()) {
            axisalignedbb = UP_AABB;
            ++i;
        }
        if (state.getValue(NORTH).booleanValue()) {
            axisalignedbb = NORTH_AABB;
            ++i;
        }
        if (state.getValue(EAST).booleanValue()) {
            axisalignedbb = EAST_AABB;
            ++i;
        }
        if (state.getValue(SOUTH).booleanValue()) {
            axisalignedbb = SOUTH_AABB;
            ++i;
        }
        if (state.getValue(WEST).booleanValue()) {
            axisalignedbb = WEST_AABB;
            ++i;
        }
        return i == 1 ? axisalignedbb : FULL_BLOCK_AABB;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        BlockPos blockpos = pos.up();
        return state.withProperty(UP, worldIn.getBlockState(blockpos).func_193401_d(worldIn, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side != EnumFacing.DOWN && side != EnumFacing.UP && this.func_193395_a(worldIn, pos, side);
    }

    public boolean func_193395_a(World p_193395_1_, BlockPos p_193395_2_, EnumFacing p_193395_3_) {
        Block block = p_193395_1_.getBlockState(p_193395_2_.up()).getBlock();
        return this.func_193396_c(p_193395_1_, p_193395_2_.offset(p_193395_3_.getOpposite()), p_193395_3_) && (block == Blocks.AIR || block == Blocks.VINE || this.func_193396_c(p_193395_1_, p_193395_2_.up(), EnumFacing.UP));
    }

    private boolean func_193396_c(World p_193396_1_, BlockPos p_193396_2_, EnumFacing p_193396_3_) {
        IBlockState iblockstate = p_193396_1_.getBlockState(p_193396_2_);
        return iblockstate.func_193401_d(p_193396_1_, p_193396_2_, p_193396_3_) == BlockFaceShape.SOLID && !BlockVine.func_193397_e(iblockstate.getBlock());
    }

    protected static boolean func_193397_e(Block p_193397_0_) {
        return p_193397_0_ instanceof BlockShulkerBox || p_193397_0_ == Blocks.BEACON || p_193397_0_ == Blocks.CAULDRON || p_193397_0_ == Blocks.GLASS || p_193397_0_ == Blocks.STAINED_GLASS || p_193397_0_ == Blocks.PISTON || p_193397_0_ == Blocks.STICKY_PISTON || p_193397_0_ == Blocks.PISTON_HEAD || p_193397_0_ == Blocks.TRAPDOOR;
    }

    private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
        IBlockState iblockstate = state;
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            IBlockState iblockstate1;
            PropertyBool propertybool = BlockVine.getPropertyFor(enumfacing);
            if (!state.getValue(propertybool).booleanValue() || this.func_193395_a(worldIn, pos, enumfacing.getOpposite()) || (iblockstate1 = worldIn.getBlockState(pos.up())).getBlock() == this && iblockstate1.getValue(propertybool).booleanValue()) continue;
            state = state.withProperty(propertybool, false);
        }
        if (BlockVine.getNumGrownFaces(state) == 0) {
            return false;
        }
        if (iblockstate != state) {
            worldIn.setBlockState(pos, state, 2);
        }
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        if (!worldIn.isRemote && !this.recheckGrownSides(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0) {
            int i = 4;
            int j = 5;
            boolean flag = false;
            block0: for (int k = -4; k <= 4; ++k) {
                for (int l = -4; l <= 4; ++l) {
                    for (int i1 = -1; i1 <= 1; ++i1) {
                        if (worldIn.getBlockState(pos.add(k, i1, l)).getBlock() != this || --j > 0) continue;
                        flag = true;
                        break block0;
                    }
                }
            }
            EnumFacing enumfacing1 = EnumFacing.random(rand);
            BlockPos blockpos2 = pos.up();
            if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos2)) {
                IBlockState iblockstate2 = state;
                for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                    if (rand.nextBoolean() && this.func_193395_a(worldIn, blockpos2, enumfacing2.getOpposite())) {
                        iblockstate2 = iblockstate2.withProperty(BlockVine.getPropertyFor(enumfacing2), true);
                        continue;
                    }
                    iblockstate2 = iblockstate2.withProperty(BlockVine.getPropertyFor(enumfacing2), false);
                }
                if (iblockstate2.getValue(NORTH).booleanValue() || iblockstate2.getValue(EAST).booleanValue() || iblockstate2.getValue(SOUTH).booleanValue() || iblockstate2.getValue(WEST).booleanValue()) {
                    worldIn.setBlockState(blockpos2, iblockstate2, 2);
                }
            } else if (enumfacing1.getAxis().isHorizontal() && !state.getValue(BlockVine.getPropertyFor(enumfacing1)).booleanValue()) {
                if (!flag) {
                    BlockPos blockpos4 = pos.offset(enumfacing1);
                    IBlockState iblockstate3 = worldIn.getBlockState(blockpos4);
                    Block block1 = iblockstate3.getBlock();
                    if (block1.blockMaterial == Material.AIR) {
                        EnumFacing enumfacing3 = enumfacing1.rotateY();
                        EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
                        boolean flag1 = state.getValue(BlockVine.getPropertyFor(enumfacing3));
                        boolean flag2 = state.getValue(BlockVine.getPropertyFor(enumfacing4));
                        BlockPos blockpos = blockpos4.offset(enumfacing3);
                        BlockPos blockpos1 = blockpos4.offset(enumfacing4);
                        if (flag1 && this.func_193395_a(worldIn, blockpos.offset(enumfacing3), enumfacing3)) {
                            worldIn.setBlockState(blockpos4, this.getDefaultState().withProperty(BlockVine.getPropertyFor(enumfacing3), true), 2);
                        } else if (flag2 && this.func_193395_a(worldIn, blockpos1.offset(enumfacing4), enumfacing4)) {
                            worldIn.setBlockState(blockpos4, this.getDefaultState().withProperty(BlockVine.getPropertyFor(enumfacing4), true), 2);
                        } else if (flag1 && worldIn.isAirBlock(blockpos) && this.func_193395_a(worldIn, blockpos, enumfacing1)) {
                            worldIn.setBlockState(blockpos, this.getDefaultState().withProperty(BlockVine.getPropertyFor(enumfacing1.getOpposite()), true), 2);
                        } else if (flag2 && worldIn.isAirBlock(blockpos1) && this.func_193395_a(worldIn, blockpos1, enumfacing1)) {
                            worldIn.setBlockState(blockpos1, this.getDefaultState().withProperty(BlockVine.getPropertyFor(enumfacing1.getOpposite()), true), 2);
                        }
                    } else if (iblockstate3.func_193401_d(worldIn, blockpos4, enumfacing1) == BlockFaceShape.SOLID) {
                        worldIn.setBlockState(pos, state.withProperty(BlockVine.getPropertyFor(enumfacing1), true), 2);
                    }
                }
            } else if (pos.getY() > 1) {
                BlockPos blockpos3 = pos.down();
                IBlockState iblockstate = worldIn.getBlockState(blockpos3);
                Block block = iblockstate.getBlock();
                if (block.blockMaterial == Material.AIR) {
                    IBlockState iblockstate1 = state;
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                        if (!rand.nextBoolean()) continue;
                        iblockstate1 = iblockstate1.withProperty(BlockVine.getPropertyFor(enumfacing), false);
                    }
                    if (iblockstate1.getValue(NORTH).booleanValue() || iblockstate1.getValue(EAST).booleanValue() || iblockstate1.getValue(SOUTH).booleanValue() || iblockstate1.getValue(WEST).booleanValue()) {
                        worldIn.setBlockState(blockpos3, iblockstate1, 2);
                    }
                } else if (block == this) {
                    IBlockState iblockstate4 = iblockstate;
                    for (EnumFacing enumfacing5 : EnumFacing.Plane.HORIZONTAL) {
                        PropertyBool propertybool = BlockVine.getPropertyFor(enumfacing5);
                        if (!rand.nextBoolean() || !state.getValue(propertybool).booleanValue()) continue;
                        iblockstate4 = iblockstate4.withProperty(propertybool, true);
                    }
                    if (iblockstate4.getValue(NORTH).booleanValue() || iblockstate4.getValue(EAST).booleanValue() || iblockstate4.getValue(SOUTH).booleanValue() || iblockstate4.getValue(WEST).booleanValue()) {
                        worldIn.setBlockState(blockpos3, iblockstate4, 2);
                    }
                }
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false);
        return facing.getAxis().isHorizontal() ? iblockstate.withProperty(BlockVine.getPropertyFor(facing.getOpposite()), true) : iblockstate;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.field_190931_a;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(StatList.getBlockStats(this));
            BlockVine.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.VINE, 1, 0));
        } else {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(SOUTH, (meta & 1) > 0).withProperty(WEST, (meta & 2) > 0).withProperty(NORTH, (meta & 4) > 0).withProperty(EAST, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(SOUTH).booleanValue()) {
            i |= 1;
        }
        if (state.getValue(WEST).booleanValue()) {
            i |= 2;
        }
        if (state.getValue(NORTH).booleanValue()) {
            i |= 4;
        }
        if (state.getValue(EAST).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, UP, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180: {
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            }
            case CLOCKWISE_90: {
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            }
        }
        return state;
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT: {
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            }
            case FRONT_BACK: {
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            }
        }
        return super.withMirror(state, mirrorIn);
    }

    public static PropertyBool getPropertyFor(EnumFacing side) {
        switch (side) {
            case UP: {
                return UP;
            }
            case NORTH: {
                return NORTH;
            }
            case SOUTH: {
                return SOUTH;
            }
            case WEST: {
                return WEST;
            }
            case EAST: {
                return EAST;
            }
        }
        throw new IllegalArgumentException(side + " is an invalid choice");
    }

    public static int getNumGrownFaces(IBlockState state) {
        int i = 0;
        for (PropertyBool propertybool : ALL_FACES) {
            if (!state.getValue(propertybool).booleanValue()) continue;
            ++i;
        }
        return i;
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}

