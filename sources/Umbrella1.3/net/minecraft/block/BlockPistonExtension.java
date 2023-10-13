/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension
extends Block {
    public static final PropertyDirection field_176326_a = PropertyDirection.create("facing");
    public static final PropertyEnum field_176325_b = PropertyEnum.create("type", EnumPistonType.class);
    public static final PropertyBool field_176327_M = PropertyBool.create("short");
    private static final String __OBFID = "CL_00000367";

    public BlockPistonExtension() {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176326_a, (Comparable)((Object)EnumFacing.NORTH)).withProperty(field_176325_b, (Comparable)((Object)EnumPistonType.DEFAULT)).withProperty(field_176327_M, Boolean.valueOf(false)));
        this.setStepSound(soundTypePiston);
        this.setHardness(0.5f);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        BlockPos var6;
        Block var7;
        EnumFacing var5;
        if (playerIn.capabilities.isCreativeMode && (var5 = (EnumFacing)((Object)state.getValue(field_176326_a))) != null && ((var7 = worldIn.getBlockState(var6 = pos.offset(var5.getOpposite())).getBlock()) == Blocks.piston || var7 == Blocks.sticky_piston)) {
            worldIn.setBlockToAir(var6);
        }
        super.onBlockHarvested(worldIn, pos, state, playerIn);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        EnumFacing var4 = ((EnumFacing)((Object)state.getValue(field_176326_a))).getOpposite();
        pos = pos.offset(var4);
        IBlockState var5 = worldIn.getBlockState(pos);
        if ((var5.getBlock() == Blocks.piston || var5.getBlock() == Blocks.sticky_piston) && ((Boolean)var5.getValue(BlockPistonBase.EXTENDED)).booleanValue()) {
            var5.getBlock().dropBlockAsItem(worldIn, pos, var5, 0);
            worldIn.setBlockToAir(pos);
        }
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
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        this.func_176324_d(state);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.func_176323_e(state);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    private void func_176323_e(IBlockState p_176323_1_) {
        float var2 = 0.25f;
        float var3 = 0.375f;
        float var4 = 0.625f;
        float var5 = 0.25f;
        float var6 = 0.75f;
        switch (SwitchEnumFacing.field_177247_a[((EnumFacing)((Object)p_176323_1_.getValue(field_176326_a))).ordinal()]) {
            case 1: {
                this.setBlockBounds(0.375f, 0.25f, 0.375f, 0.625f, 1.0f, 0.625f);
                break;
            }
            case 2: {
                this.setBlockBounds(0.375f, 0.0f, 0.375f, 0.625f, 0.75f, 0.625f);
                break;
            }
            case 3: {
                this.setBlockBounds(0.25f, 0.375f, 0.25f, 0.75f, 0.625f, 1.0f);
                break;
            }
            case 4: {
                this.setBlockBounds(0.25f, 0.375f, 0.0f, 0.75f, 0.625f, 0.75f);
                break;
            }
            case 5: {
                this.setBlockBounds(0.375f, 0.25f, 0.25f, 0.625f, 0.75f, 1.0f);
                break;
            }
            case 6: {
                this.setBlockBounds(0.0f, 0.375f, 0.25f, 0.75f, 0.625f, 0.75f);
            }
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.func_176324_d(access.getBlockState(pos));
    }

    public void func_176324_d(IBlockState p_176324_1_) {
        float var2 = 0.25f;
        EnumFacing var3 = (EnumFacing)((Object)p_176324_1_.getValue(field_176326_a));
        if (var3 != null) {
            switch (SwitchEnumFacing.field_177247_a[var3.ordinal()]) {
                case 1: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
                    break;
                }
                case 2: {
                    this.setBlockBounds(0.0f, 0.75f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 3: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.25f);
                    break;
                }
                case 4: {
                    this.setBlockBounds(0.0f, 0.0f, 0.75f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 5: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.25f, 1.0f, 1.0f);
                    break;
                }
                case 6: {
                    this.setBlockBounds(0.75f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing var5 = (EnumFacing)((Object)state.getValue(field_176326_a));
        BlockPos var6 = pos.offset(var5.getOpposite());
        IBlockState var7 = worldIn.getBlockState(var6);
        if (var7.getBlock() != Blocks.piston && var7.getBlock() != Blocks.sticky_piston) {
            worldIn.setBlockToAir(pos);
        } else {
            var7.getBlock().onNeighborBlockChange(worldIn, var6, var7, neighborBlock);
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    public static EnumFacing func_176322_b(int p_176322_0_) {
        int var1 = p_176322_0_ & 7;
        return var1 > 5 ? null : EnumFacing.getFront(var1);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(field_176325_b) == EnumPistonType.STICKY ? Item.getItemFromBlock(Blocks.sticky_piston) : Item.getItemFromBlock(Blocks.piston);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176326_a, (Comparable)((Object)BlockPistonExtension.func_176322_b(meta))).withProperty(field_176325_b, (Comparable)((Object)((meta & 8) > 0 ? EnumPistonType.STICKY : EnumPistonType.DEFAULT)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(field_176326_a))).getIndex();
        if (state.getValue(field_176325_b) == EnumPistonType.STICKY) {
            var3 |= 8;
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176326_a, field_176325_b, field_176327_M);
    }

    public static enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, "normal"),
        STICKY("STICKY", 1, "sticky");

        private final String field_176714_c;
        private static final EnumPistonType[] $VALUES;
        private static final String __OBFID = "CL_00002035";

        static {
            $VALUES = new EnumPistonType[]{DEFAULT, STICKY};
        }

        private EnumPistonType(String p_i45666_1_, int p_i45666_2_, String p_i45666_3_) {
            this.field_176714_c = p_i45666_3_;
        }

        public String toString() {
            return this.field_176714_c;
        }

        @Override
        public String getName() {
            return this.field_176714_c;
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_177247_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002036";

        static {
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

