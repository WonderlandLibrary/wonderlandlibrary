/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire
extends Block {
    public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.create("east", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.create("west", EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private boolean canProvidePower = true;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();

    public BlockRedstoneWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, EnumAttachPosition.NONE).withProperty(EAST, EnumAttachPosition.NONE).withProperty(SOUTH, EnumAttachPosition.NONE).withProperty(WEST, EnumAttachPosition.NONE).withProperty(POWER, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        return state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
    }

    private EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
        EnumAttachPosition enumAttachPosition;
        Block block1;
        BlockPos blockpos = pos.offset(direction);
        Block block = worldIn.getBlockState(pos.offset(direction)).getBlock();
        if (BlockRedstoneWire.canConnectTo(worldIn.getBlockState(blockpos), direction)) return EnumAttachPosition.SIDE;
        if (!block.isBlockNormalCube()) {
            if (BlockRedstoneWire.canConnectUpwardsTo(worldIn.getBlockState(blockpos.down()))) return EnumAttachPosition.SIDE;
        }
        if (!(block1 = worldIn.getBlockState(pos.up()).getBlock()).isBlockNormalCube() && block.isBlockNormalCube() && BlockRedstoneWire.canConnectUpwardsTo(worldIn.getBlockState(blockpos.up()))) {
            enumAttachPosition = EnumAttachPosition.UP;
            return enumAttachPosition;
        }
        enumAttachPosition = EnumAttachPosition.NONE;
        return enumAttachPosition;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        int n;
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != this) {
            n = super.colorMultiplier(worldIn, pos, renderPass);
            return n;
        }
        n = this.colorMultiplier(iblockstate.getValue(POWER));
        return n;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) return true;
        if (worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone) return true;
        return false;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state) {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        ArrayList<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            BlockPos blockpos = (BlockPos)iterator.next();
            worldIn.notifyNeighborsOfStateChange(blockpos, this);
        }
        return state;
    }

    private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state) {
        IBlockState iblockstate = state;
        int i = state.getValue(POWER);
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
        if (i == j) return state;
        state = state.withProperty(POWER, j);
        if (worldIn.getBlockState(pos1) == iblockstate) {
            worldIn.setBlockState(pos1, state, 2);
        }
        this.blocksNeedingUpdate.add(pos1);
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing1 = enumFacingArray[n2];
            this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
            ++n2;
        }
        return state;
    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() != this) return;
        worldIn.notifyNeighborsOfStateChange(pos, this);
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing = enumFacingArray[n2];
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            ++n2;
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.isRemote) return;
        this.updateSurroundingRedstone(worldIn, pos, state);
        for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL) {
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
        }
        for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
            this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
        }
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        while (iterator.hasNext()) {
            EnumFacing enumfacing2 = iterator.next();
            BlockPos blockpos = pos.offset(enumfacing2);
            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
                this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                continue;
            }
            this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (worldIn.isRemote) return;
        for (EnumFacing enumfacing : EnumFacing.values()) {
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
        }
        this.updateSurroundingRedstone(worldIn, pos, state);
        for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
            this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
        }
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        while (iterator.hasNext()) {
            EnumFacing enumfacing2 = (EnumFacing)iterator.next();
            BlockPos blockpos = pos.offset(enumfacing2);
            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
                this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                continue;
            }
            this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
        }
    }

    private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength) {
        int n;
        if (worldIn.getBlockState(pos).getBlock() != this) {
            return strength;
        }
        int i = worldIn.getBlockState(pos).getValue(POWER);
        if (i > strength) {
            n = i;
            return n;
        }
        n = strength;
        return n;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (worldIn.isRemote) return;
        if (this.canPlaceBlockAt(worldIn, pos)) {
            this.updateSurroundingRedstone(worldIn, pos, state);
            return;
        }
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.redstone;
    }

    @Override
    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!this.canProvidePower) {
            return 0;
        }
        int n = this.getWeakPower(worldIn, pos, state, side);
        return n;
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!this.canProvidePower) {
            return 0;
        }
        int i = state.getValue(POWER);
        if (i == 0) {
            return 0;
        }
        if (side == EnumFacing.UP) {
            return i;
        }
        EnumSet<EnumFacing> enumset = EnumSet.noneOf(EnumFacing.class);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (!this.func_176339_d(worldIn, pos, enumfacing)) continue;
            enumset.add(enumfacing);
        }
        if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
            return i;
        }
        if (!enumset.contains(side)) return 0;
        if (enumset.contains(side.rotateYCCW())) return 0;
        if (enumset.contains(side.rotateY())) return 0;
        return i;
    }

    private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        BlockPos blockpos = pos.offset(side);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        boolean flag = block.isNormalCube();
        boolean flag1 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
        if (!flag1 && flag && BlockRedstoneWire.canConnectUpwardsTo(worldIn, blockpos.up())) {
            return true;
        }
        if (BlockRedstoneWire.canConnectTo(iblockstate, side)) {
            return true;
        }
        if (block == Blocks.powered_repeater && iblockstate.getValue(BlockRedstoneDiode.FACING) == side) {
            return true;
        }
        if (flag) return false;
        if (!BlockRedstoneWire.canConnectUpwardsTo(worldIn, blockpos.down())) return false;
        return true;
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
            EnumFacing enumfacing = blockState.getValue(BlockRedstoneRepeater.FACING);
            if (enumfacing == side) return true;
            if (enumfacing.getOpposite() == side) return true;
            return false;
        }
        if (!block.canProvidePower()) return false;
        if (side == null) return false;
        return true;
    }

    @Override
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
        int i = MathHelper.clamp_int((int)(f1 * 255.0f), 0, 255);
        int j = MathHelper.clamp_int((int)(f2 * 255.0f), 0, 255);
        int k = MathHelper.clamp_int((int)(f3 * 255.0f), 0, 255);
        return 0xFF000000 | i << 16 | j << 8 | k;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int i = state.getValue(POWER);
        if (i == 0) return;
        double d0 = (double)pos.getX() + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
        double d1 = (float)pos.getY() + 0.0625f;
        double d2 = (double)pos.getZ() + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
        float f = (float)i / 15.0f;
        float f1 = f * 0.6f + 0.4f;
        float f2 = Math.max(0.0f, f * f * 0.7f - 0.5f);
        float f3 = Math.max(0.0f, f * f * 0.6f - 0.7f);
        worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3, new int[0]);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.redstone;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWER, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWER);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, NORTH, EAST, SOUTH, WEST, POWER);
    }

    static enum EnumAttachPosition implements IStringSerializable
    {
        UP("up"),
        SIDE("side"),
        NONE("none");

        private final String name;

        private EnumAttachPosition(String name) {
            this.name = name;
        }

        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

