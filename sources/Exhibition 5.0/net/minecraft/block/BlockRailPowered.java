// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRailPowered extends BlockRailBase
{
    public static final PropertyEnum field_176568_b;
    public static final PropertyBool field_176569_M;
    private static final String __OBFID = "CL_00000288";
    
    protected BlockRailPowered() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRailPowered.field_176568_b, EnumRailDirection.NORTH_SOUTH).withProperty(BlockRailPowered.field_176569_M, false));
    }
    
    protected boolean func_176566_a(final World worldIn, final BlockPos p_176566_2_, final IBlockState p_176566_3_, final boolean p_176566_4_, final int p_176566_5_) {
        if (p_176566_5_ >= 8) {
            return false;
        }
        int var6 = p_176566_2_.getX();
        int var7 = p_176566_2_.getY();
        int var8 = p_176566_2_.getZ();
        boolean var9 = true;
        EnumRailDirection var10 = (EnumRailDirection)p_176566_3_.getValue(BlockRailPowered.field_176568_b);
        switch (SwitchEnumRailDirection.field_180121_a[var10.ordinal()]) {
            case 1: {
                if (p_176566_4_) {
                    ++var8;
                    break;
                }
                --var8;
                break;
            }
            case 2: {
                if (p_176566_4_) {
                    --var6;
                    break;
                }
                ++var6;
                break;
            }
            case 3: {
                if (p_176566_4_) {
                    --var6;
                }
                else {
                    ++var6;
                    ++var7;
                    var9 = false;
                }
                var10 = EnumRailDirection.EAST_WEST;
                break;
            }
            case 4: {
                if (p_176566_4_) {
                    --var6;
                    ++var7;
                    var9 = false;
                }
                else {
                    ++var6;
                }
                var10 = EnumRailDirection.EAST_WEST;
                break;
            }
            case 5: {
                if (p_176566_4_) {
                    ++var8;
                }
                else {
                    --var8;
                    ++var7;
                    var9 = false;
                }
                var10 = EnumRailDirection.NORTH_SOUTH;
                break;
            }
            case 6: {
                if (p_176566_4_) {
                    ++var8;
                    ++var7;
                    var9 = false;
                }
                else {
                    --var8;
                }
                var10 = EnumRailDirection.NORTH_SOUTH;
                break;
            }
        }
        return this.func_176567_a(worldIn, new BlockPos(var6, var7, var8), p_176566_4_, p_176566_5_, var10) || (var9 && this.func_176567_a(worldIn, new BlockPos(var6, var7 - 1, var8), p_176566_4_, p_176566_5_, var10));
    }
    
    protected boolean func_176567_a(final World worldIn, final BlockPos p_176567_2_, final boolean p_176567_3_, final int p_176567_4_, final EnumRailDirection p_176567_5_) {
        final IBlockState var6 = worldIn.getBlockState(p_176567_2_);
        if (var6.getBlock() != this) {
            return false;
        }
        final EnumRailDirection var7 = (EnumRailDirection)var6.getValue(BlockRailPowered.field_176568_b);
        return (p_176567_5_ != EnumRailDirection.EAST_WEST || (var7 != EnumRailDirection.NORTH_SOUTH && var7 != EnumRailDirection.ASCENDING_NORTH && var7 != EnumRailDirection.ASCENDING_SOUTH)) && (p_176567_5_ != EnumRailDirection.NORTH_SOUTH || (var7 != EnumRailDirection.EAST_WEST && var7 != EnumRailDirection.ASCENDING_EAST && var7 != EnumRailDirection.ASCENDING_WEST)) && (boolean)var6.getValue(BlockRailPowered.field_176569_M) && (worldIn.isBlockPowered(p_176567_2_) || this.func_176566_a(worldIn, p_176567_2_, var6, p_176567_3_, p_176567_4_ + 1));
    }
    
    @Override
    protected void func_176561_b(final World worldIn, final BlockPos p_176561_2_, final IBlockState p_176561_3_, final Block p_176561_4_) {
        final boolean var5 = (boolean)p_176561_3_.getValue(BlockRailPowered.field_176569_M);
        final boolean var6 = worldIn.isBlockPowered(p_176561_2_) || this.func_176566_a(worldIn, p_176561_2_, p_176561_3_, true, 0) || this.func_176566_a(worldIn, p_176561_2_, p_176561_3_, false, 0);
        if (var6 != var5) {
            worldIn.setBlockState(p_176561_2_, p_176561_3_.withProperty(BlockRailPowered.field_176569_M, var6), 3);
            worldIn.notifyNeighborsOfStateChange(p_176561_2_.offsetDown(), this);
            if (((EnumRailDirection)p_176561_3_.getValue(BlockRailPowered.field_176568_b)).func_177018_c()) {
                worldIn.notifyNeighborsOfStateChange(p_176561_2_.offsetUp(), this);
            }
        }
    }
    
    @Override
    public IProperty func_176560_l() {
        return BlockRailPowered.field_176568_b;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRailPowered.field_176568_b, EnumRailDirection.func_177016_a(meta & 0x7)).withProperty(BlockRailPowered.field_176569_M, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumRailDirection)state.getValue(BlockRailPowered.field_176568_b)).func_177015_a();
        if (state.getValue(BlockRailPowered.field_176569_M)) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRailPowered.field_176568_b, BlockRailPowered.field_176569_M });
    }
    
    static {
        field_176568_b = PropertyEnum.create("shape", EnumRailDirection.class, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002080";
            
            public boolean func_180133_a(final EnumRailDirection p_180133_1_) {
                return p_180133_1_ != EnumRailDirection.NORTH_EAST && p_180133_1_ != EnumRailDirection.NORTH_WEST && p_180133_1_ != EnumRailDirection.SOUTH_EAST && p_180133_1_ != EnumRailDirection.SOUTH_WEST;
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_180133_a((EnumRailDirection)p_apply_1_);
            }
        });
        field_176569_M = PropertyBool.create("powered");
    }
    
    static final class SwitchEnumRailDirection
    {
        static final int[] field_180121_a;
        private static final String __OBFID = "CL_00002079";
        
        static {
            field_180121_a = new int[EnumRailDirection.values().length];
            try {
                SwitchEnumRailDirection.field_180121_a[EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumRailDirection.field_180121_a[EnumRailDirection.EAST_WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumRailDirection.field_180121_a[EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumRailDirection.field_180121_a[EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumRailDirection.field_180121_a[EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumRailDirection.field_180121_a[EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
