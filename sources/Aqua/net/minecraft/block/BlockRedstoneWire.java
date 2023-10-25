package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire
extends Block {
    public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.create((String)"north", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.create((String)"east", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.create((String)"south", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.create((String)"west", EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create((String)"power", (int)0, (int)15);
    private boolean canProvidePower = true;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();

    public BlockRedstoneWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, (Comparable)EnumAttachPosition.NONE).withProperty(EAST, (Comparable)EnumAttachPosition.NONE).withProperty(SOUTH, (Comparable)EnumAttachPosition.NONE).withProperty(WEST, (Comparable)EnumAttachPosition.NONE).withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)0)));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        state = state.withProperty(WEST, (Comparable)this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, (Comparable)this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, (Comparable)this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, (Comparable)this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }

    private EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
        BlockPos blockpos = pos.offset(direction);
        Block block = worldIn.getBlockState(pos.offset(direction)).getBlock();
        if (!(BlockRedstoneWire.canConnectTo(worldIn.getBlockState(blockpos), direction) || !block.isBlockNormalCube() && BlockRedstoneWire.canConnectUpwardsTo(worldIn.getBlockState(blockpos.down())))) {
            Block block1 = worldIn.getBlockState(pos.up()).getBlock();
            return !block1.isBlockNormalCube() && block.isBlockNormalCube() && BlockRedstoneWire.canConnectUpwardsTo(worldIn.getBlockState(blockpos.up())) ? EnumAttachPosition.UP : EnumAttachPosition.NONE;
        }
        return EnumAttachPosition.SIDE;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : this.colorMultiplier((Integer)iblockstate.getValue((IProperty)POWER));
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state) {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        ArrayList list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        for (BlockPos blockpos : list) {
            worldIn.notifyNeighborsOfStateChange(blockpos, (Block)this);
        }
        return state;
    }

    private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state) {
        IBlockState iblockstate = state;
        int i = (Integer)state.getValue((IProperty)POWER);
        int j = 0;
        j = this.getMaxCurrentStrength(worldIn, pos2, j);
        this.canProvidePower = false;
        int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
        this.canProvidePower = true;
        if (k > 0 && k > j - 1) {
            j = k;
        }
        int l = 0;
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            boolean flag;
            BlockPos blockpos = pos1.offset(enumfacing);
            boolean bl = flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();
            if (flag) {
                l = this.getMaxCurrentStrength(worldIn, blockpos, l);
            }
            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube() && !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube()) {
                if (!flag || pos1.getY() < pos2.getY()) continue;
                l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
                continue;
            }
            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube() || !flag || pos1.getY() > pos2.getY()) continue;
            l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l);
        }
        j = l > j ? l - 1 : (j > 0 ? --j : 0);
        if (k > j - 1) {
            j = k;
        }
        if (i != j) {
            state = state.withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)j));
            if (worldIn.getBlockState(pos1) == iblockstate) {
                worldIn.setBlockState(pos1, state, 2);
            }
            this.blocksNeedingUpdate.add((Object)pos1);
            for (EnumFacing enumfacing1 : EnumFacing.values()) {
                this.blocksNeedingUpdate.add((Object)pos1.offset(enumfacing1));
            }
        }
        return state;
    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() == this) {
            worldIn.notifyNeighborsOfStateChange(pos, (Block)this);
            for (EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), (Block)this);
            }
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), (Block)this);
            }
            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }
            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                BlockPos blockpos = pos.offset(enumfacing2);
                if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                    continue;
                }
                this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (!worldIn.isRemote) {
            for (EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), (Block)this);
            }
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }
            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                BlockPos blockpos = pos.offset(enumfacing2);
                if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                    continue;
                }
                this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
            }
        }
    }

    private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength) {
        if (worldIn.getBlockState(pos).getBlock() != this) {
            return strength;
        }
        int i = (Integer)worldIn.getBlockState(pos).getValue((IProperty)POWER);
        return i > strength ? i : strength;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            if (this.canPlaceBlockAt(worldIn, pos)) {
                this.updateSurroundingRedstone(worldIn, pos, state);
            } else {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.redstone;
    }

    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return !this.canProvidePower ? 0 : this.getWeakPower(worldIn, pos, state, side);
    }

    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!this.canProvidePower) {
            return 0;
        }
        int i = (Integer)state.getValue((IProperty)POWER);
        if (i == 0) {
            return 0;
        }
        if (side == EnumFacing.UP) {
            return i;
        }
        EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (!this.func_176339_d(worldIn, pos, enumfacing)) continue;
            enumset.add((Object)enumfacing);
        }
        if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
            return i;
        }
        if (enumset.contains((Object)side) && !enumset.contains((Object)side.rotateYCCW()) && !enumset.contains((Object)side.rotateY())) {
            return i;
        }
        return 0;
    }

    private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        BlockPos blockpos = pos.offset(side);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        boolean flag = block.isNormalCube();
        boolean flag1 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
        return !flag1 && flag && BlockRedstoneWire.canConnectUpwardsTo(worldIn, blockpos.up()) ? true : (BlockRedstoneWire.canConnectTo(iblockstate, side) ? true : (block == Blocks.powered_repeater && iblockstate.getValue((IProperty)BlockRedstoneDiode.FACING) == side ? true : !flag && BlockRedstoneWire.canConnectUpwardsTo(worldIn, blockpos.down())));
    }

    protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos) {
        return BlockRedstoneWire.canConnectUpwardsTo(worldIn.getBlockState(pos));
    }

    protected static boolean canConnectUpwardsTo(IBlockState state) {
        return BlockRedstoneWire.canConnectTo(state, null);
    }

    protected static boolean canConnectTo(IBlockState blockState, EnumFacing side) {
        Block block = blockState.getBlock();
        if (block == Blocks.redstone_wire) {
            return true;
        }
        if (Blocks.unpowered_repeater.isAssociated(block)) {
            EnumFacing enumfacing = (EnumFacing)blockState.getValue((IProperty)BlockRedstoneRepeater.FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        }
        return block.canProvidePower() && side != null;
    }

    public boolean canProvidePower() {
        return this.canProvidePower;
    }

    private int colorMultiplier(int powerLevel) {
        float f = (float)powerLevel / 15.0f;
        float f1 = f * 0.6f + 0.4f;
        if (powerLevel == 0) {
            f1 = 0.3f;
        }
        float f2 = f * f * 0.7f - 0.5f;
        float f3 = f * f * 0.6f - 0.7f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        int i = MathHelper.clamp_int((int)((int)(f1 * 255.0f)), (int)0, (int)255);
        int j = MathHelper.clamp_int((int)((int)(f2 * 255.0f)), (int)0, (int)255);
        int k = MathHelper.clamp_int((int)((int)(f3 * 255.0f)), (int)0, (int)255);
        return 0xFF000000 | i << 16 | j << 8 | k;
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int i = (Integer)state.getValue((IProperty)POWER);
        if (i != 0) {
            double d0 = (double)pos.getX() + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
            double d1 = (float)pos.getY() + 0.0625f;
            double d2 = (double)pos.getZ() + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
            float f = (float)i / 15.0f;
            float f1 = f * 0.6f + 0.4f;
            float f2 = Math.max((float)0.0f, (float)(f * f * 0.7f - 0.5f));
            float f3 = Math.max((float)0.0f, (float)(f * f * 0.6f - 0.7f));
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3, new int[0]);
        }
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.redstone;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)POWER);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{NORTH, EAST, SOUTH, WEST, POWER});
    }
}
