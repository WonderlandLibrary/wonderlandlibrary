// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockTrapDoor extends Block
{
    public static final PropertyDirection field_176284_a;
    public static final PropertyBool field_176283_b;
    public static final PropertyEnum field_176285_M;
    private static final String __OBFID = "CL_00000327";
    
    protected BlockTrapDoor(final Material p_i45434_1_) {
        super(p_i45434_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTrapDoor.field_176284_a, EnumFacing.NORTH).withProperty(BlockTrapDoor.field_176283_b, false).withProperty(BlockTrapDoor.field_176285_M, DoorHalf.BOTTOM));
        final float var2 = 0.5f;
        final float var3 = 1.0f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
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
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return !(boolean)blockAccess.getBlockState(pos).getValue(BlockTrapDoor.field_176283_b);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.func_180693_d(access.getBlockState(pos));
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        final float var1 = 0.1875f;
        this.setBlockBounds(0.0f, 0.40625f, 0.0f, 1.0f, 0.59375f, 1.0f);
    }
    
    public void func_180693_d(final IBlockState p_180693_1_) {
        if (p_180693_1_.getBlock() == this) {
            final boolean var2 = p_180693_1_.getValue(BlockTrapDoor.field_176285_M) == DoorHalf.TOP;
            final Boolean var3 = (Boolean)p_180693_1_.getValue(BlockTrapDoor.field_176283_b);
            final EnumFacing var4 = (EnumFacing)p_180693_1_.getValue(BlockTrapDoor.field_176284_a);
            final float var5 = 0.1875f;
            if (var2) {
                this.setBlockBounds(0.0f, 0.8125f, 0.0f, 1.0f, 1.0f, 1.0f);
            }
            else {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.1875f, 1.0f);
            }
            if (var3) {
                if (var4 == EnumFacing.NORTH) {
                    this.setBlockBounds(0.0f, 0.0f, 0.8125f, 1.0f, 1.0f, 1.0f);
                }
                if (var4 == EnumFacing.SOUTH) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.1875f);
                }
                if (var4 == EnumFacing.WEST) {
                    this.setBlockBounds(0.8125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
                if (var4 == EnumFacing.EAST) {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.1875f, 1.0f, 1.0f);
                }
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (this.blockMaterial == Material.iron) {
            return true;
        }
        state = state.cycleProperty(BlockTrapDoor.field_176283_b);
        worldIn.setBlockState(pos, state, 2);
        worldIn.playAuxSFXAtEntity(playerIn, ((boolean)state.getValue(BlockTrapDoor.field_176283_b)) ? 1003 : 1006, pos, 0);
        return true;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            final BlockPos var5 = pos.offset(((EnumFacing)state.getValue(BlockTrapDoor.field_176284_a)).getOpposite());
            if (!isValidSupportBlock(worldIn.getBlockState(var5).getBlock())) {
                worldIn.setBlockToAir(pos);
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
            else {
                final boolean var6 = worldIn.isBlockPowered(pos);
                if (var6 || neighborBlock.canProvidePower()) {
                    final boolean var7 = (boolean)state.getValue(BlockTrapDoor.field_176283_b);
                    if (var7 != var6) {
                        worldIn.setBlockState(pos, state.withProperty(BlockTrapDoor.field_176283_b, var6), 2);
                        worldIn.playAuxSFXAtEntity(null, var6 ? 1003 : 1006, pos, 0);
                    }
                }
            }
        }
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState var9 = this.getDefaultState();
        if (facing.getAxis().isHorizontal()) {
            var9 = var9.withProperty(BlockTrapDoor.field_176284_a, facing).withProperty(BlockTrapDoor.field_176283_b, false);
            var9 = var9.withProperty(BlockTrapDoor.field_176285_M, (hitY > 0.5f) ? DoorHalf.TOP : DoorHalf.BOTTOM);
        }
        return var9;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return !side.getAxis().isVertical() && isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
    }
    
    protected static EnumFacing func_176281_b(final int p_176281_0_) {
        switch (p_176281_0_ & 0x3) {
            case 0: {
                return EnumFacing.NORTH;
            }
            case 1: {
                return EnumFacing.SOUTH;
            }
            case 2: {
                return EnumFacing.WEST;
            }
            default: {
                return EnumFacing.EAST;
            }
        }
    }
    
    protected static int func_176282_a(final EnumFacing p_176282_0_) {
        switch (SwitchEnumFacing.field_177058_a[p_176282_0_.ordinal()]) {
            case 1: {
                return 0;
            }
            case 2: {
                return 1;
            }
            case 3: {
                return 2;
            }
            default: {
                return 3;
            }
        }
    }
    
    private static boolean isValidSupportBlock(final Block p_150119_0_) {
        return (p_150119_0_.blockMaterial.isOpaque() && p_150119_0_.isFullCube()) || p_150119_0_ == Blocks.glowstone || p_150119_0_ instanceof BlockSlab || p_150119_0_ instanceof BlockStairs;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockTrapDoor.field_176284_a, func_176281_b(meta)).withProperty(BlockTrapDoor.field_176283_b, (meta & 0x4) != 0x0).withProperty(BlockTrapDoor.field_176285_M, ((meta & 0x8) == 0x0) ? DoorHalf.BOTTOM : DoorHalf.TOP);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | func_176282_a((EnumFacing)state.getValue(BlockTrapDoor.field_176284_a));
        if (state.getValue(BlockTrapDoor.field_176283_b)) {
            var3 |= 0x4;
        }
        if (state.getValue(BlockTrapDoor.field_176285_M) == DoorHalf.TOP) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTrapDoor.field_176284_a, BlockTrapDoor.field_176283_b, BlockTrapDoor.field_176285_M });
    }
    
    static {
        field_176284_a = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
        field_176283_b = PropertyBool.create("open");
        field_176285_M = PropertyEnum.create("half", DoorHalf.class);
    }
    
    public enum DoorHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"), 
        BOTTOM("BOTTOM", 1, "bottom");
        
        private final String field_176671_c;
        private static final DoorHalf[] $VALUES;
        private static final String __OBFID = "CL_00002051";
        
        private DoorHalf(final String p_i45674_1_, final int p_i45674_2_, final String p_i45674_3_) {
            this.field_176671_c = p_i45674_3_;
        }
        
        @Override
        public String toString() {
            return this.field_176671_c;
        }
        
        @Override
        public String getName() {
            return this.field_176671_c;
        }
        
        static {
            $VALUES = new DoorHalf[] { DoorHalf.TOP, DoorHalf.BOTTOM };
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_177058_a;
        private static final String __OBFID = "CL_00002052";
        
        static {
            field_177058_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
