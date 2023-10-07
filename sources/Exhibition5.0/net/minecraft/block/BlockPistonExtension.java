// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyDirection;

public class BlockPistonExtension extends Block
{
    public static final PropertyDirection field_176326_a;
    public static final PropertyEnum field_176325_b;
    public static final PropertyBool field_176327_M;
    private static final String __OBFID = "CL_00000367";
    
    public BlockPistonExtension() {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPistonExtension.field_176326_a, EnumFacing.NORTH).withProperty(BlockPistonExtension.field_176325_b, EnumPistonType.DEFAULT).withProperty(BlockPistonExtension.field_176327_M, false));
        this.setStepSound(Block.soundTypePiston);
        this.setHardness(0.5f);
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn) {
        if (playerIn.capabilities.isCreativeMode) {
            final EnumFacing var5 = (EnumFacing)state.getValue(BlockPistonExtension.field_176326_a);
            if (var5 != null) {
                final BlockPos var6 = pos.offset(var5.getOpposite());
                final Block var7 = worldIn.getBlockState(var6).getBlock();
                if (var7 == Blocks.piston || var7 == Blocks.sticky_piston) {
                    worldIn.setBlockToAir(var6);
                }
            }
        }
        super.onBlockHarvested(worldIn, pos, state, playerIn);
    }
    
    @Override
    public void breakBlock(final World worldIn, BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        final EnumFacing var4 = ((EnumFacing)state.getValue(BlockPistonExtension.field_176326_a)).getOpposite();
        pos = pos.offset(var4);
        final IBlockState var5 = worldIn.getBlockState(pos);
        if ((var5.getBlock() == Blocks.piston || var5.getBlock() == Blocks.sticky_piston) && (boolean)var5.getValue(BlockPistonBase.EXTENDED)) {
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
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        return false;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List list, final Entity collidingEntity) {
        this.func_176324_d(state);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.func_176323_e(state);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void func_176323_e(final IBlockState p_176323_1_) {
        final float var2 = 0.25f;
        final float var3 = 0.375f;
        final float var4 = 0.625f;
        final float var5 = 0.25f;
        final float var6 = 0.75f;
        switch (SwitchEnumFacing.field_177247_a[((EnumFacing)p_176323_1_.getValue(BlockPistonExtension.field_176326_a)).ordinal()]) {
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
                break;
            }
        }
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.func_176324_d(access.getBlockState(pos));
    }
    
    public void func_176324_d(final IBlockState p_176324_1_) {
        final float var2 = 0.25f;
        final EnumFacing var3 = (EnumFacing)p_176324_1_.getValue(BlockPistonExtension.field_176326_a);
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
                    break;
                }
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing var5 = (EnumFacing)state.getValue(BlockPistonExtension.field_176326_a);
        final BlockPos var6 = pos.offset(var5.getOpposite());
        final IBlockState var7 = worldIn.getBlockState(var6);
        if (var7.getBlock() != Blocks.piston && var7.getBlock() != Blocks.sticky_piston) {
            worldIn.setBlockToAir(pos);
        }
        else {
            var7.getBlock().onNeighborBlockChange(worldIn, var6, var7, neighborBlock);
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    public static EnumFacing func_176322_b(final int p_176322_0_) {
        final int var1 = p_176322_0_ & 0x7;
        return (var1 > 5) ? null : EnumFacing.getFront(var1);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return (worldIn.getBlockState(pos).getValue(BlockPistonExtension.field_176325_b) == EnumPistonType.STICKY) ? Item.getItemFromBlock(Blocks.sticky_piston) : Item.getItemFromBlock(Blocks.piston);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPistonExtension.field_176326_a, func_176322_b(meta)).withProperty(BlockPistonExtension.field_176325_b, ((meta & 0x8) > 0) ? EnumPistonType.STICKY : EnumPistonType.DEFAULT);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockPistonExtension.field_176326_a)).getIndex();
        if (state.getValue(BlockPistonExtension.field_176325_b) == EnumPistonType.STICKY) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPistonExtension.field_176326_a, BlockPistonExtension.field_176325_b, BlockPistonExtension.field_176327_M });
    }
    
    static {
        field_176326_a = PropertyDirection.create("facing");
        field_176325_b = PropertyEnum.create("type", EnumPistonType.class);
        field_176327_M = PropertyBool.create("short");
    }
    
    public enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, "normal"), 
        STICKY("STICKY", 1, "sticky");
        
        private final String field_176714_c;
        private static final EnumPistonType[] $VALUES;
        private static final String __OBFID = "CL_00002035";
        
        private EnumPistonType(final String p_i45666_1_, final int p_i45666_2_, final String p_i45666_3_) {
            this.field_176714_c = p_i45666_3_;
        }
        
        @Override
        public String toString() {
            return this.field_176714_c;
        }
        
        @Override
        public String getName() {
            return this.field_176714_c;
        }
        
        static {
            $VALUES = new EnumPistonType[] { EnumPistonType.DEFAULT, EnumPistonType.STICKY };
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_177247_a;
        private static final String __OBFID = "CL_00002036";
        
        static {
            field_177247_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumFacing.field_177247_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
