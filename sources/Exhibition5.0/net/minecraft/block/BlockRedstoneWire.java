// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import java.util.EnumSet;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import java.util.Set;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRedstoneWire extends Block
{
    public static final PropertyEnum NORTH;
    public static final PropertyEnum EAST;
    public static final PropertyEnum SOUTH;
    public static final PropertyEnum WEST;
    public static final PropertyInteger POWER;
    private boolean canProvidePower;
    private final Set field_150179_b;
    private static final String __OBFID = "CL_00000295";
    
    public BlockRedstoneWire() {
        super(Material.circuits);
        this.canProvidePower = true;
        this.field_150179_b = Sets.newHashSet();
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRedstoneWire.NORTH, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.EAST, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.SOUTH, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.WEST, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.POWER, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        state = state.withProperty(BlockRedstoneWire.WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(BlockRedstoneWire.EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(BlockRedstoneWire.NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(BlockRedstoneWire.SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }
    
    private EnumAttachPosition getAttachPosition(final IBlockAccess p_176341_1_, final BlockPos p_176341_2_, final EnumFacing p_176341_3_) {
        final BlockPos var4 = p_176341_2_.offset(p_176341_3_);
        final Block var5 = p_176341_1_.getBlockState(p_176341_2_.offset(p_176341_3_)).getBlock();
        if (!func_176343_a(p_176341_1_.getBlockState(var4), p_176341_3_) && (var5.isSolidFullCube() || !func_176346_d(p_176341_1_.getBlockState(var4.offsetDown())))) {
            final Block var6 = p_176341_1_.getBlockState(p_176341_2_.offsetUp()).getBlock();
            return (!var6.isSolidFullCube() && var5.isSolidFullCube() && func_176346_d(p_176341_1_.getBlockState(var4.offsetUp()))) ? EnumAttachPosition.UP : EnumAttachPosition.NONE;
        }
        return EnumAttachPosition.SIDE;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
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
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        final IBlockState var4 = worldIn.getBlockState(pos);
        return (var4.getBlock() != this) ? super.colorMultiplier(worldIn, pos, renderPass) : this.func_176337_b((int)var4.getValue(BlockRedstoneWire.POWER));
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) || worldIn.getBlockState(pos.offsetDown()).getBlock() == Blocks.glowstone;
    }
    
    private IBlockState updateSurroundingRedstone(final World worldIn, final BlockPos p_176338_2_, IBlockState p_176338_3_) {
        p_176338_3_ = this.func_176345_a(worldIn, p_176338_2_, p_176338_2_, p_176338_3_);
        final ArrayList var4 = Lists.newArrayList((Iterable)this.field_150179_b);
        this.field_150179_b.clear();
        for (final BlockPos var6 : var4) {
            worldIn.notifyNeighborsOfStateChange(var6, this);
        }
        return p_176338_3_;
    }
    
    private IBlockState func_176345_a(final World worldIn, final BlockPos p_176345_2_, final BlockPos p_176345_3_, IBlockState p_176345_4_) {
        final IBlockState var5 = p_176345_4_;
        final int var6 = (int)p_176345_4_.getValue(BlockRedstoneWire.POWER);
        final byte var7 = 0;
        int var8 = this.func_176342_a(worldIn, p_176345_3_, var7);
        this.canProvidePower = false;
        final int var9 = worldIn.func_175687_A(p_176345_2_);
        this.canProvidePower = true;
        if (var9 > 0 && var9 > var8 - 1) {
            var8 = var9;
        }
        int var10 = 0;
        for (final EnumFacing var12 : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos var13 = p_176345_2_.offset(var12);
            final boolean var14 = var13.getX() != p_176345_3_.getX() || var13.getZ() != p_176345_3_.getZ();
            if (var14) {
                var10 = this.func_176342_a(worldIn, var13, var10);
            }
            if (worldIn.getBlockState(var13).getBlock().isNormalCube() && !worldIn.getBlockState(p_176345_2_.offsetUp()).getBlock().isNormalCube()) {
                if (!var14 || p_176345_2_.getY() < p_176345_3_.getY()) {
                    continue;
                }
                var10 = this.func_176342_a(worldIn, var13.offsetUp(), var10);
            }
            else {
                if (worldIn.getBlockState(var13).getBlock().isNormalCube() || !var14 || p_176345_2_.getY() > p_176345_3_.getY()) {
                    continue;
                }
                var10 = this.func_176342_a(worldIn, var13.offsetDown(), var10);
            }
        }
        if (var10 > var8) {
            var8 = var10 - 1;
        }
        else if (var8 > 0) {
            --var8;
        }
        else {
            var8 = 0;
        }
        if (var9 > var8 - 1) {
            var8 = var9;
        }
        if (var6 != var8) {
            p_176345_4_ = p_176345_4_.withProperty(BlockRedstoneWire.POWER, var8);
            if (worldIn.getBlockState(p_176345_2_) == var5) {
                worldIn.setBlockState(p_176345_2_, p_176345_4_, 2);
            }
            this.field_150179_b.add(p_176345_2_);
            for (final EnumFacing var18 : EnumFacing.values()) {
                this.field_150179_b.add(p_176345_2_.offset(var18));
            }
        }
        return p_176345_4_;
    }
    
    private void func_176344_d(final World worldIn, final BlockPos p_176344_2_) {
        if (worldIn.getBlockState(p_176344_2_).getBlock() == this) {
            worldIn.notifyNeighborsOfStateChange(p_176344_2_, this);
            for (final EnumFacing var6 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(p_176344_2_.offset(var6), this);
            }
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (final EnumFacing var5 : EnumFacing.Plane.VERTICAL) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var5), this);
            }
            for (final EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
                this.func_176344_d(worldIn, pos.offset(var5));
            }
            for (final EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
                final BlockPos var6 = pos.offset(var5);
                if (worldIn.getBlockState(var6).getBlock().isNormalCube()) {
                    this.func_176344_d(worldIn, var6.offsetUp());
                }
                else {
                    this.func_176344_d(worldIn, var6.offsetDown());
                }
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (!worldIn.isRemote) {
            for (final EnumFacing var7 : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }
            this.updateSurroundingRedstone(worldIn, pos, state);
            for (final EnumFacing var9 : EnumFacing.Plane.HORIZONTAL) {
                this.func_176344_d(worldIn, pos.offset(var9));
            }
            for (final EnumFacing var9 : EnumFacing.Plane.HORIZONTAL) {
                final BlockPos var10 = pos.offset(var9);
                if (worldIn.getBlockState(var10).getBlock().isNormalCube()) {
                    this.func_176344_d(worldIn, var10.offsetUp());
                }
                else {
                    this.func_176344_d(worldIn, var10.offsetDown());
                }
            }
        }
    }
    
    private int func_176342_a(final World worldIn, final BlockPos p_176342_2_, final int p_176342_3_) {
        if (worldIn.getBlockState(p_176342_2_).getBlock() != this) {
            return p_176342_3_;
        }
        final int var4 = (int)worldIn.getBlockState(p_176342_2_).getValue(BlockRedstoneWire.POWER);
        return (var4 > p_176342_3_) ? var4 : p_176342_3_;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            if (this.canPlaceBlockAt(worldIn, pos)) {
                this.updateSurroundingRedstone(worldIn, pos, state);
            }
            else {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.redstone;
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return this.canProvidePower ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        if (!this.canProvidePower) {
            return 0;
        }
        final int var5 = (int)state.getValue(BlockRedstoneWire.POWER);
        if (var5 == 0) {
            return 0;
        }
        if (side == EnumFacing.UP) {
            return var5;
        }
        final EnumSet var6 = EnumSet.noneOf(EnumFacing.class);
        for (final EnumFacing var8 : EnumFacing.Plane.HORIZONTAL) {
            if (this.func_176339_d(worldIn, pos, var8)) {
                var6.add(var8);
            }
        }
        if (side.getAxis().isHorizontal() && var6.isEmpty()) {
            return var5;
        }
        if (var6.contains(side) && !var6.contains(side.rotateYCCW()) && !var6.contains(side.rotateY())) {
            return var5;
        }
        return 0;
    }
    
    private boolean func_176339_d(final IBlockAccess p_176339_1_, final BlockPos p_176339_2_, final EnumFacing p_176339_3_) {
        final BlockPos var4 = p_176339_2_.offset(p_176339_3_);
        final IBlockState var5 = p_176339_1_.getBlockState(var4);
        final Block var6 = var5.getBlock();
        final boolean var7 = var6.isNormalCube();
        final boolean var8 = p_176339_1_.getBlockState(p_176339_2_.offsetUp()).getBlock().isNormalCube();
        return (!var8 && var7 && func_176340_e(p_176339_1_, var4.offsetUp())) || func_176343_a(var5, p_176339_3_) || (var6 == Blocks.powered_repeater && var5.getValue(BlockDirectional.AGE) == p_176339_3_) || (!var7 && func_176340_e(p_176339_1_, var4.offsetDown()));
    }
    
    protected static boolean func_176340_e(final IBlockAccess p_176340_0_, final BlockPos p_176340_1_) {
        return func_176346_d(p_176340_0_.getBlockState(p_176340_1_));
    }
    
    protected static boolean func_176346_d(final IBlockState p_176346_0_) {
        return func_176343_a(p_176346_0_, null);
    }
    
    protected static boolean func_176343_a(final IBlockState p_176343_0_, final EnumFacing p_176343_1_) {
        final Block var2 = p_176343_0_.getBlock();
        if (var2 == Blocks.redstone_wire) {
            return true;
        }
        if (Blocks.unpowered_repeater.func_149907_e(var2)) {
            final EnumFacing var3 = (EnumFacing)p_176343_0_.getValue(BlockDirectional.AGE);
            return var3 == p_176343_1_ || var3.getOpposite() == p_176343_1_;
        }
        return var2.canProvidePower() && p_176343_1_ != null;
    }
    
    @Override
    public boolean canProvidePower() {
        return this.canProvidePower;
    }
    
    private int func_176337_b(final int p_176337_1_) {
        final float var2 = p_176337_1_ / 15.0f;
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
        final int var6 = MathHelper.clamp_int((int)(var3 * 255.0f), 0, 255);
        final int var7 = MathHelper.clamp_int((int)(var4 * 255.0f), 0, 255);
        final int var8 = MathHelper.clamp_int((int)(var5 * 255.0f), 0, 255);
        return 0xFF000000 | var6 << 16 | var7 << 8 | var8;
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final int var5 = (int)state.getValue(BlockRedstoneWire.POWER);
        if (var5 != 0) {
            final double var6 = pos.getX() + 0.5 + (rand.nextFloat() - 0.5) * 0.2;
            final double var7 = pos.getY() + 0.0625f;
            final double var8 = pos.getZ() + 0.5 + (rand.nextFloat() - 0.5) * 0.2;
            final float var9 = var5 / 15.0f;
            final float var10 = var9 * 0.6f + 0.4f;
            final float var11 = Math.max(0.0f, var9 * var9 * 0.7f - 0.5f);
            final float var12 = Math.max(0.0f, var9 * var9 * 0.6f - 0.7f);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var6, var7, var8, var10, var11, var12, new int[0]);
        }
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.redstone;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRedstoneWire.POWER, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockRedstoneWire.POWER);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRedstoneWire.NORTH, BlockRedstoneWire.EAST, BlockRedstoneWire.SOUTH, BlockRedstoneWire.WEST, BlockRedstoneWire.POWER });
    }
    
    static {
        NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
        EAST = PropertyEnum.create("east", EnumAttachPosition.class);
        SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
        WEST = PropertyEnum.create("west", EnumAttachPosition.class);
        POWER = PropertyInteger.create("power", 0, 15);
    }
    
    enum EnumAttachPosition implements IStringSerializable
    {
        UP("UP", 0, "up"), 
        SIDE("SIDE", 1, "side"), 
        NONE("NONE", 2, "none");
        
        private final String field_176820_d;
        private static final EnumAttachPosition[] $VALUES;
        private static final String __OBFID = "CL_00002070";
        
        private EnumAttachPosition(final String p_i45689_1_, final int p_i45689_2_, final String p_i45689_3_) {
            this.field_176820_d = p_i45689_3_;
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return this.field_176820_d;
        }
        
        static {
            $VALUES = new EnumAttachPosition[] { EnumAttachPosition.UP, EnumAttachPosition.SIDE, EnumAttachPosition.NONE };
        }
    }
}
