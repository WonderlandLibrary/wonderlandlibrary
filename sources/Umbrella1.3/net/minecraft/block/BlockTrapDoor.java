/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor
extends Block {
    public static final PropertyDirection field_176284_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool field_176283_b = PropertyBool.create("open");
    public static final PropertyEnum field_176285_M = PropertyEnum.create("half", DoorHalf.class);
    private static final String __OBFID = "CL_00000327";

    protected BlockTrapDoor(Material p_i45434_1_) {
        super(p_i45434_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176284_a, (Comparable)((Object)EnumFacing.NORTH)).withProperty(field_176283_b, Boolean.valueOf(false)).withProperty(field_176285_M, (Comparable)((Object)DoorHalf.BOTTOM)));
        float var2 = 0.5f;
        float var3 = 1.0f;
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
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return (Boolean)blockAccess.getBlockState(pos).getValue(field_176283_b) == false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.func_180693_d(access.getBlockState(pos));
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float var1 = 0.1875f;
        this.setBlockBounds(0.0f, 0.40625f, 0.0f, 1.0f, 0.59375f, 1.0f);
    }

    public void func_180693_d(IBlockState p_180693_1_) {
        if (p_180693_1_.getBlock() == this) {
            boolean var2 = p_180693_1_.getValue(field_176285_M) == DoorHalf.TOP;
            Boolean var3 = (Boolean)p_180693_1_.getValue(field_176283_b);
            EnumFacing var4 = (EnumFacing)((Object)p_180693_1_.getValue(field_176284_a));
            float var5 = 0.1875f;
            if (var2) {
                this.setBlockBounds(0.0f, 0.8125f, 0.0f, 1.0f, 1.0f, 1.0f);
            } else {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.1875f, 1.0f);
            }
            if (var3.booleanValue()) {
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.blockMaterial == Material.iron) {
            return true;
        }
        state = state.cycleProperty(field_176283_b);
        worldIn.setBlockState(pos, state, 2);
        worldIn.playAuxSFXAtEntity(playerIn, (Boolean)state.getValue(field_176283_b) != false ? 1003 : 1006, pos, 0);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            BlockPos var5 = pos.offset(((EnumFacing)((Object)state.getValue(field_176284_a))).getOpposite());
            if (!BlockTrapDoor.isValidSupportBlock(worldIn.getBlockState(var5).getBlock())) {
                worldIn.setBlockToAir(pos);
                this.dropBlockAsItem(worldIn, pos, state, 0);
            } else {
                boolean var7;
                boolean var6 = worldIn.isBlockPowered(pos);
                if ((var6 || neighborBlock.canProvidePower()) && (var7 = ((Boolean)state.getValue(field_176283_b)).booleanValue()) != var6) {
                    worldIn.setBlockState(pos, state.withProperty(field_176283_b, Boolean.valueOf(var6)), 2);
                    worldIn.playAuxSFXAtEntity(null, var6 ? 1003 : 1006, pos, 0);
                }
            }
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = this.getDefaultState();
        if (facing.getAxis().isHorizontal()) {
            var9 = var9.withProperty(field_176284_a, (Comparable)((Object)facing)).withProperty(field_176283_b, Boolean.valueOf(false));
            var9 = var9.withProperty(field_176285_M, (Comparable)((Object)(hitY > 0.5f ? DoorHalf.TOP : DoorHalf.BOTTOM)));
        }
        return var9;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return !side.getAxis().isVertical() && BlockTrapDoor.isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
    }

    protected static EnumFacing func_176281_b(int p_176281_0_) {
        switch (p_176281_0_ & 3) {
            case 0: {
                return EnumFacing.NORTH;
            }
            case 1: {
                return EnumFacing.SOUTH;
            }
            case 2: {
                return EnumFacing.WEST;
            }
        }
        return EnumFacing.EAST;
    }

    protected static int func_176282_a(EnumFacing p_176282_0_) {
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
        }
        return 3;
    }

    private static boolean isValidSupportBlock(Block p_150119_0_) {
        return p_150119_0_.blockMaterial.isOpaque() && p_150119_0_.isFullCube() || p_150119_0_ == Blocks.glowstone || p_150119_0_ instanceof BlockSlab || p_150119_0_ instanceof BlockStairs;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176284_a, (Comparable)((Object)BlockTrapDoor.func_176281_b(meta))).withProperty(field_176283_b, Boolean.valueOf((meta & 4) != 0)).withProperty(field_176285_M, (Comparable)((Object)((meta & 8) == 0 ? DoorHalf.BOTTOM : DoorHalf.TOP)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | BlockTrapDoor.func_176282_a((EnumFacing)((Object)state.getValue(field_176284_a)));
        if (((Boolean)state.getValue(field_176283_b)).booleanValue()) {
            var3 |= 4;
        }
        if (state.getValue(field_176285_M) == DoorHalf.TOP) {
            var3 |= 8;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176284_a, field_176283_b, field_176285_M);
    }

    public static enum DoorHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"),
        BOTTOM("BOTTOM", 1, "bottom");

        private final String field_176671_c;
        private static final DoorHalf[] $VALUES;
        private static final String __OBFID = "CL_00002051";

        static {
            $VALUES = new DoorHalf[]{TOP, BOTTOM};
        }

        private DoorHalf(String p_i45674_1_, int p_i45674_2_, String p_i45674_3_) {
            this.field_176671_c = p_i45674_3_;
        }

        public String toString() {
            return this.field_176671_c;
        }

        @Override
        public String getName() {
            return this.field_176671_c;
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_177058_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002052";

        static {
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177058_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

