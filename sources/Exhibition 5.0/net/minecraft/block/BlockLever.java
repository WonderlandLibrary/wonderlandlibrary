// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockLever extends Block
{
    public static final PropertyEnum FACING;
    public static final PropertyBool POWERED;
    private static final String __OBFID = "CL_00000264";
    
    protected BlockLever() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLever.FACING, EnumOrientation.NORTH).withProperty(BlockLever.POWERED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
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
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return (side == EnumFacing.UP && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) || this.func_176358_d(worldIn, pos.offset(side.getOpposite()));
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return this.func_176358_d(worldIn, pos.offsetWest()) || this.func_176358_d(worldIn, pos.offsetEast()) || this.func_176358_d(worldIn, pos.offsetNorth()) || this.func_176358_d(worldIn, pos.offsetSouth()) || World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) || this.func_176358_d(worldIn, pos.offsetUp());
    }
    
    protected boolean func_176358_d(final World worldIn, final BlockPos p_176358_2_) {
        return worldIn.getBlockState(p_176358_2_).getBlock().isNormalCube();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState var9 = this.getDefaultState().withProperty(BlockLever.POWERED, false);
        if (this.func_176358_d(worldIn, pos.offset(facing.getOpposite()))) {
            return var9.withProperty(BlockLever.FACING, EnumOrientation.func_176856_a(facing, placer.func_174811_aO()));
        }
        for (final EnumFacing var11 : EnumFacing.Plane.HORIZONTAL) {
            if (var11 != facing && this.func_176358_d(worldIn, pos.offset(var11.getOpposite()))) {
                return var9.withProperty(BlockLever.FACING, EnumOrientation.func_176856_a(var11, placer.func_174811_aO()));
            }
        }
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
            return var9.withProperty(BlockLever.FACING, EnumOrientation.func_176856_a(EnumFacing.UP, placer.func_174811_aO()));
        }
        return var9;
    }
    
    public static int func_176357_a(final EnumFacing p_176357_0_) {
        switch (SwitchEnumFacing.FACING_LOOKUP[p_176357_0_.ordinal()]) {
            case 1: {
                return 0;
            }
            case 2: {
                return 5;
            }
            case 3: {
                return 4;
            }
            case 4: {
                return 3;
            }
            case 5: {
                return 2;
            }
            case 6: {
                return 1;
            }
            default: {
                return -1;
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (this.func_176356_e(worldIn, pos) && !this.func_176358_d(worldIn, pos.offset(((EnumOrientation)state.getValue(BlockLever.FACING)).func_176852_c().getOpposite()))) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    private boolean func_176356_e(final World worldIn, final BlockPos p_176356_2_) {
        if (this.canPlaceBlockAt(worldIn, p_176356_2_)) {
            return true;
        }
        this.dropBlockAsItem(worldIn, p_176356_2_, worldIn.getBlockState(p_176356_2_), 0);
        worldIn.setBlockToAir(p_176356_2_);
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        float var3 = 0.1875f;
        switch (SwitchEnumFacing.ORIENTATION_LOOKUP[((EnumOrientation)access.getBlockState(pos).getValue(BlockLever.FACING)).ordinal()]) {
            case 1: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - var3, var3 * 2.0f, 0.8f, 0.5f + var3);
                break;
            }
            case 2: {
                this.setBlockBounds(1.0f - var3 * 2.0f, 0.2f, 0.5f - var3, 1.0f, 0.8f, 0.5f + var3);
                break;
            }
            case 3: {
                this.setBlockBounds(0.5f - var3, 0.2f, 0.0f, 0.5f + var3, 0.8f, var3 * 2.0f);
                break;
            }
            case 4: {
                this.setBlockBounds(0.5f - var3, 0.2f, 1.0f - var3 * 2.0f, 0.5f + var3, 0.8f, 1.0f);
                break;
            }
            case 5:
            case 6: {
                var3 = 0.25f;
                this.setBlockBounds(0.5f - var3, 0.0f, 0.5f - var3, 0.5f + var3, 0.6f, 0.5f + var3);
                break;
            }
            case 7:
            case 8: {
                var3 = 0.25f;
                this.setBlockBounds(0.5f - var3, 0.4f, 0.5f - var3, 0.5f + var3, 1.0f, 0.5f + var3);
                break;
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        state = state.cycleProperty(BlockLever.POWERED);
        worldIn.setBlockState(pos, state, 3);
        worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, ((boolean)state.getValue(BlockLever.POWERED)) ? 0.6f : 0.5f);
        worldIn.notifyNeighborsOfStateChange(pos, this);
        final EnumFacing var9 = ((EnumOrientation)state.getValue(BlockLever.FACING)).func_176852_c();
        worldIn.notifyNeighborsOfStateChange(pos.offset(var9.getOpposite()), this);
        return true;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue(BlockLever.POWERED)) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            final EnumFacing var4 = ((EnumOrientation)state.getValue(BlockLever.FACING)).func_176852_c();
            worldIn.notifyNeighborsOfStateChange(pos.offset(var4.getOpposite()), this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue(BlockLever.POWERED) ? 15 : 0;
    }
    
    @Override
    public int isProvidingStrongPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue(BlockLever.POWERED) ? ((((EnumOrientation)state.getValue(BlockLever.FACING)).func_176852_c() == side) ? 15 : 0) : 0;
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.func_176853_a(meta & 0x7)).withProperty(BlockLever.POWERED, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumOrientation)state.getValue(BlockLever.FACING)).func_176855_a();
        if (state.getValue(BlockLever.POWERED)) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockLever.FACING, BlockLever.POWERED });
    }
    
    static {
        FACING = PropertyEnum.create("facing", EnumOrientation.class);
        POWERED = PropertyBool.create("powered");
    }
    
    public enum EnumOrientation implements IStringSerializable
    {
        DOWN_X("DOWN_X", 0, 0, "down_x", EnumFacing.DOWN), 
        EAST("EAST", 1, 1, "east", EnumFacing.EAST), 
        WEST("WEST", 2, 2, "west", EnumFacing.WEST), 
        SOUTH("SOUTH", 3, 3, "south", EnumFacing.SOUTH), 
        NORTH("NORTH", 4, 4, "north", EnumFacing.NORTH), 
        UP_Z("UP_Z", 5, 5, "up_z", EnumFacing.UP), 
        UP_X("UP_X", 6, 6, "up_x", EnumFacing.UP), 
        DOWN_Z("DOWN_Z", 7, 7, "down_z", EnumFacing.DOWN);
        
        private static final EnumOrientation[] field_176869_i;
        private final int field_176866_j;
        private final String field_176867_k;
        private final EnumFacing field_176864_l;
        private static final EnumOrientation[] $VALUES;
        private static final String __OBFID = "CL_00002102";
        
        private EnumOrientation(final String p_i45709_1_, final int p_i45709_2_, final int p_i45709_3_, final String p_i45709_4_, final EnumFacing p_i45709_5_) {
            this.field_176866_j = p_i45709_3_;
            this.field_176867_k = p_i45709_4_;
            this.field_176864_l = p_i45709_5_;
        }
        
        public int func_176855_a() {
            return this.field_176866_j;
        }
        
        public EnumFacing func_176852_c() {
            return this.field_176864_l;
        }
        
        @Override
        public String toString() {
            return this.field_176867_k;
        }
        
        public static EnumOrientation func_176853_a(int p_176853_0_) {
            if (p_176853_0_ < 0 || p_176853_0_ >= EnumOrientation.field_176869_i.length) {
                p_176853_0_ = 0;
            }
            return EnumOrientation.field_176869_i[p_176853_0_];
        }
        
        public static EnumOrientation func_176856_a(final EnumFacing p_176856_0_, final EnumFacing p_176856_1_) {
            switch (SwitchEnumFacing.FACING_LOOKUP[p_176856_0_.ordinal()]) {
                case 1: {
                    switch (SwitchEnumFacing.AXIS_LOOKUP[p_176856_1_.getAxis().ordinal()]) {
                        case 1: {
                            return EnumOrientation.DOWN_X;
                        }
                        case 2: {
                            return EnumOrientation.DOWN_Z;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
                        }
                    }
                    break;
                }
                case 2: {
                    switch (SwitchEnumFacing.AXIS_LOOKUP[p_176856_1_.getAxis().ordinal()]) {
                        case 1: {
                            return EnumOrientation.UP_X;
                        }
                        case 2: {
                            return EnumOrientation.UP_Z;
                        }
                        default: {
                            throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
                        }
                    }
                    break;
                }
                case 3: {
                    return EnumOrientation.NORTH;
                }
                case 4: {
                    return EnumOrientation.SOUTH;
                }
                case 5: {
                    return EnumOrientation.WEST;
                }
                case 6: {
                    return EnumOrientation.EAST;
                }
                default: {
                    throw new IllegalArgumentException("Invalid facing: " + p_176856_0_);
                }
            }
        }
        
        @Override
        public String getName() {
            return this.field_176867_k;
        }
        
        static {
            field_176869_i = new EnumOrientation[values().length];
            $VALUES = new EnumOrientation[] { EnumOrientation.DOWN_X, EnumOrientation.EAST, EnumOrientation.WEST, EnumOrientation.SOUTH, EnumOrientation.NORTH, EnumOrientation.UP_Z, EnumOrientation.UP_X, EnumOrientation.DOWN_Z };
            for (final EnumOrientation var4 : values()) {
                EnumOrientation.field_176869_i[var4.func_176855_a()] = var4;
            }
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] FACING_LOOKUP;
        static final int[] ORIENTATION_LOOKUP;
        static final int[] AXIS_LOOKUP;
        private static final String __OBFID = "CL_00002103";
        
        static {
            AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];
            try {
                SwitchEnumFacing.AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            ORIENTATION_LOOKUP = new int[EnumOrientation.values().length];
            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[EnumOrientation.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[EnumOrientation.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[EnumOrientation.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[EnumOrientation.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[EnumOrientation.UP_Z.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[EnumOrientation.UP_X.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[EnumOrientation.DOWN_X.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                SwitchEnumFacing.ORIENTATION_LOOKUP[EnumOrientation.DOWN_Z.ordinal()] = 8;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
            FACING_LOOKUP = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError11) {}
            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError12) {}
            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError13) {}
            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError14) {}
            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError15) {}
            try {
                SwitchEnumFacing.FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError16) {}
        }
    }
}
