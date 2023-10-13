/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 */
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
    public static final PropertyEnum NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
    public static final PropertyEnum EAST = PropertyEnum.create("east", EnumAttachPosition.class);
    public static final PropertyEnum SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
    public static final PropertyEnum WEST = PropertyEnum.create("west", EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private boolean canProvidePower = true;
    private final Set field_150179_b = Sets.newHashSet();
    private static final String __OBFID = "CL_00000295";

    public BlockRedstoneWire() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, (Comparable)((Object)EnumAttachPosition.NONE)).withProperty(EAST, (Comparable)((Object)EnumAttachPosition.NONE)).withProperty(SOUTH, (Comparable)((Object)EnumAttachPosition.NONE)).withProperty(WEST, (Comparable)((Object)EnumAttachPosition.NONE)).withProperty(POWER, Integer.valueOf(0)));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        state = state.withProperty(WEST, (Comparable)((Object)this.getAttachPosition(worldIn, pos, EnumFacing.WEST)));
        state = state.withProperty(EAST, (Comparable)((Object)this.getAttachPosition(worldIn, pos, EnumFacing.EAST)));
        state = state.withProperty(NORTH, (Comparable)((Object)this.getAttachPosition(worldIn, pos, EnumFacing.NORTH)));
        state = state.withProperty(SOUTH, (Comparable)((Object)this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH)));
        return state;
    }

    private EnumAttachPosition getAttachPosition(IBlockAccess p_176341_1_, BlockPos p_176341_2_, EnumFacing p_176341_3_) {
        BlockPos var4 = p_176341_2_.offset(p_176341_3_);
        Block var5 = p_176341_1_.getBlockState(p_176341_2_.offset(p_176341_3_)).getBlock();
        if (!(BlockRedstoneWire.func_176343_a(p_176341_1_.getBlockState(var4), p_176341_3_) || !var5.isSolidFullCube() && BlockRedstoneWire.func_176346_d(p_176341_1_.getBlockState(var4.offsetDown())))) {
            Block var6 = p_176341_1_.getBlockState(p_176341_2_.offsetUp()).getBlock();
            return !var6.isSolidFullCube() && var5.isSolidFullCube() && BlockRedstoneWire.func_176346_d(p_176341_1_.getBlockState(var4.offsetUp())) ? EnumAttachPosition.UP : EnumAttachPosition.NONE;
        }
        return EnumAttachPosition.SIDE;
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
        IBlockState var4 = worldIn.getBlockState(pos);
        return var4.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : this.func_176337_b((Integer)var4.getValue(POWER));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) || worldIn.getBlockState(pos.offsetDown()).getBlock() == Blocks.glowstone;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos p_176338_2_, IBlockState p_176338_3_) {
        p_176338_3_ = this.func_176345_a(worldIn, p_176338_2_, p_176338_2_, p_176338_3_);
        ArrayList var4 = Lists.newArrayList((Iterable)this.field_150179_b);
        this.field_150179_b.clear();
        for (BlockPos var6 : var4) {
            worldIn.notifyNeighborsOfStateChange(var6, this);
        }
        return p_176338_3_;
    }

    private IBlockState func_176345_a(World worldIn, BlockPos p_176345_2_, BlockPos p_176345_3_, IBlockState p_176345_4_) {
        IBlockState var5 = p_176345_4_;
        int var6 = (Integer)p_176345_4_.getValue(POWER);
        int var7 = 0;
        int var14 = this.func_176342_a(worldIn, p_176345_3_, var7);
        this.canProvidePower = false;
        int var8 = worldIn.func_175687_A(p_176345_2_);
        this.canProvidePower = true;
        if (var8 > 0 && var8 > var14 - 1) {
            var14 = var8;
        }
        int var9 = 0;
        for (EnumFacing var11 : EnumFacing.Plane.HORIZONTAL) {
            boolean var13;
            BlockPos var12 = p_176345_2_.offset(var11);
            boolean bl = var13 = var12.getX() != p_176345_3_.getX() || var12.getZ() != p_176345_3_.getZ();
            if (var13) {
                var9 = this.func_176342_a(worldIn, var12, var9);
            }
            if (worldIn.getBlockState(var12).getBlock().isNormalCube() && !worldIn.getBlockState(p_176345_2_.offsetUp()).getBlock().isNormalCube()) {
                if (!var13 || p_176345_2_.getY() < p_176345_3_.getY()) continue;
                var9 = this.func_176342_a(worldIn, var12.offsetUp(), var9);
                continue;
            }
            if (worldIn.getBlockState(var12).getBlock().isNormalCube() || !var13 || p_176345_2_.getY() > p_176345_3_.getY()) continue;
            var9 = this.func_176342_a(worldIn, var12.offsetDown(), var9);
        }
        var14 = var9 > var14 ? var9 - 1 : (var14 > 0 ? --var14 : 0);
        if (var8 > var14 - 1) {
            var14 = var8;
        }
        if (var6 != var14) {
            p_176345_4_ = p_176345_4_.withProperty(POWER, Integer.valueOf(var14));
            if (worldIn.getBlockState(p_176345_2_) == var5) {
                worldIn.setBlockState(p_176345_2_, p_176345_4_, 2);
            }
            this.field_150179_b.add(p_176345_2_);
            for (EnumFacing var18 : EnumFacing.values()) {
                this.field_150179_b.add(p_176345_2_.offset(var18));
            }
        }
        return p_176345_4_;
    }

    private void func_176344_d(World worldIn, BlockPos p_176344_2_) {
        if (worldIn.getBlockState(p_176344_2_).getBlock() == this) {
            worldIn.notifyNeighborsOfStateChange(p_176344_2_, this);
            for (EnumFacing var6 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(p_176344_2_.offset(var6), this);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (EnumFacing var5 : EnumFacing.Plane.VERTICAL) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var5), this);
            }
            for (EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
                this.func_176344_d(worldIn, pos.offset(var5));
            }
            for (EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
                BlockPos var6 = pos.offset(var5);
                if (worldIn.getBlockState(var6).getBlock().isNormalCube()) {
                    this.func_176344_d(worldIn, var6.offsetUp());
                    continue;
                }
                this.func_176344_d(worldIn, var6.offsetDown());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (!worldIn.isRemote) {
            for (EnumFacing var7 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (EnumFacing var9 : EnumFacing.Plane.HORIZONTAL) {
                this.func_176344_d(worldIn, pos.offset(var9));
            }
            for (EnumFacing var9 : EnumFacing.Plane.HORIZONTAL) {
                BlockPos var10 = pos.offset(var9);
                if (worldIn.getBlockState(var10).getBlock().isNormalCube()) {
                    this.func_176344_d(worldIn, var10.offsetUp());
                    continue;
                }
                this.func_176344_d(worldIn, var10.offsetDown());
            }
        }
    }

    private int func_176342_a(World worldIn, BlockPos p_176342_2_, int p_176342_3_) {
        if (worldIn.getBlockState(p_176342_2_).getBlock() != this) {
            return p_176342_3_;
        }
        int var4 = (Integer)worldIn.getBlockState(p_176342_2_).getValue(POWER);
        return var4 > p_176342_3_ ? var4 : p_176342_3_;
    }

    @Override
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

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.redstone;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return !this.canProvidePower ? 0 : this.isProvidingWeakPower(worldIn, pos, state, side);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!this.canProvidePower) {
            return 0;
        }
        int var5 = (Integer)state.getValue(POWER);
        if (var5 == 0) {
            return 0;
        }
        if (side == EnumFacing.UP) {
            return var5;
        }
        EnumSet<EnumFacing> var6 = EnumSet.noneOf(EnumFacing.class);
        for (EnumFacing var8 : EnumFacing.Plane.HORIZONTAL) {
            if (!this.func_176339_d(worldIn, pos, var8)) continue;
            var6.add(var8);
        }
        if (side.getAxis().isHorizontal() && var6.isEmpty()) {
            return var5;
        }
        if (var6.contains(side) && !var6.contains(side.rotateYCCW()) && !var6.contains(side.rotateY())) {
            return var5;
        }
        return 0;
    }

    private boolean func_176339_d(IBlockAccess p_176339_1_, BlockPos p_176339_2_, EnumFacing p_176339_3_) {
        BlockPos var4 = p_176339_2_.offset(p_176339_3_);
        IBlockState var5 = p_176339_1_.getBlockState(var4);
        Block var6 = var5.getBlock();
        boolean var7 = var6.isNormalCube();
        boolean var8 = p_176339_1_.getBlockState(p_176339_2_.offsetUp()).getBlock().isNormalCube();
        return !var8 && var7 && BlockRedstoneWire.func_176340_e(p_176339_1_, var4.offsetUp()) ? true : (BlockRedstoneWire.func_176343_a(var5, p_176339_3_) ? true : (var6 == Blocks.powered_repeater && var5.getValue(BlockRedstoneDiode.AGE) == p_176339_3_ ? true : !var7 && BlockRedstoneWire.func_176340_e(p_176339_1_, var4.offsetDown())));
    }

    protected static boolean func_176340_e(IBlockAccess p_176340_0_, BlockPos p_176340_1_) {
        return BlockRedstoneWire.func_176346_d(p_176340_0_.getBlockState(p_176340_1_));
    }

    protected static boolean func_176346_d(IBlockState p_176346_0_) {
        return BlockRedstoneWire.func_176343_a(p_176346_0_, null);
    }

    protected static boolean func_176343_a(IBlockState p_176343_0_, EnumFacing p_176343_1_) {
        Block var2 = p_176343_0_.getBlock();
        if (var2 == Blocks.redstone_wire) {
            return true;
        }
        if (Blocks.unpowered_repeater.func_149907_e(var2)) {
            EnumFacing var3 = (EnumFacing)((Object)p_176343_0_.getValue(BlockRedstoneRepeater.AGE));
            return var3 == p_176343_1_ || var3.getOpposite() == p_176343_1_;
        }
        return var2.canProvidePower() && p_176343_1_ != null;
    }

    @Override
    public boolean canProvidePower() {
        return this.canProvidePower;
    }

    private int func_176337_b(int p_176337_1_) {
        float var2 = (float)p_176337_1_ / 15.0f;
        float var3 = var2 * 0.6f + 0.4f;
        if (p_176337_1_ == 0) {
            var3 = 0.3f;
        }
        float var4 = var2 * var2 * 0.7f - 0.5f;
        float var5 = var2 * var2 * 0.6f - 0.7f;
        if (var4 < 0.0f) {
            var4 = 0.0f;
        }
        if (var5 < 0.0f) {
            var5 = 0.0f;
        }
        int var6 = MathHelper.clamp_int((int)(var3 * 255.0f), 0, 255);
        int var7 = MathHelper.clamp_int((int)(var4 * 255.0f), 0, 255);
        int var8 = MathHelper.clamp_int((int)(var5 * 255.0f), 0, 255);
        return 0xFF000000 | var6 << 16 | var7 << 8 | var8;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int var5 = (Integer)state.getValue(POWER);
        if (var5 != 0) {
            double var6 = (double)pos.getX() + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
            double var8 = (float)pos.getY() + 0.0625f;
            double var10 = (double)pos.getZ() + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
            float var12 = (float)var5 / 15.0f;
            float var13 = var12 * 0.6f + 0.4f;
            float var14 = Math.max(0.0f, var12 * var12 * 0.7f - 0.5f);
            float var15 = Math.max(0.0f, var12 * var12 * 0.6f - 0.7f);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var6, var8, var10, (double)var13, (double)var14, (double)var15, new int[0]);
        }
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
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue(POWER);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, NORTH, EAST, SOUTH, WEST, POWER);
    }

    static enum EnumAttachPosition implements IStringSerializable
    {
        UP("UP", 0, "up"),
        SIDE("SIDE", 1, "side"),
        NONE("NONE", 2, "none");

        private final String field_176820_d;
        private static final EnumAttachPosition[] $VALUES;
        private static final String __OBFID = "CL_00002070";

        static {
            $VALUES = new EnumAttachPosition[]{UP, SIDE, NONE};
        }

        private EnumAttachPosition(String p_i45689_1_, int p_i45689_2_, String p_i45689_3_) {
            this.field_176820_d = p_i45689_3_;
        }

        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this.field_176820_d;
        }
    }
}

