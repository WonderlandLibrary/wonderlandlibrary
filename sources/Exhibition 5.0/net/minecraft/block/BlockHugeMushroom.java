// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockHugeMushroom extends Block
{
    public static final PropertyEnum field_176380_a;
    private final Block field_176379_b;
    private static final String __OBFID = "CL_00000258";
    
    public BlockHugeMushroom(final Material p_i45711_1_, final Block p_i45711_2_) {
        super(p_i45711_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHugeMushroom.field_176380_a, EnumType.ALL_OUTSIDE));
        this.field_176379_b = p_i45711_2_;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return Math.max(0, random.nextInt(10) - 7);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(this.field_176379_b);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(this.field_176379_b);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockHugeMushroom.field_176380_a, EnumType.func_176895_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockHugeMushroom.field_176380_a)).func_176896_a();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockHugeMushroom.field_176380_a });
    }
    
    static {
        field_176380_a = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        NORTH_WEST("NORTH_WEST", 0, 1, "north_west"), 
        NORTH("NORTH", 1, 2, "north"), 
        NORTH_EAST("NORTH_EAST", 2, 3, "north_east"), 
        WEST("WEST", 3, 4, "west"), 
        CENTER("CENTER", 4, 5, "center"), 
        EAST("EAST", 5, 6, "east"), 
        SOUTH_WEST("SOUTH_WEST", 6, 7, "south_west"), 
        SOUTH("SOUTH", 7, 8, "south"), 
        SOUTH_EAST("SOUTH_EAST", 8, 9, "south_east"), 
        STEM("STEM", 9, 10, "stem"), 
        ALL_INSIDE("ALL_INSIDE", 10, 0, "all_inside"), 
        ALL_OUTSIDE("ALL_OUTSIDE", 11, 14, "all_outside"), 
        ALL_STEM("ALL_STEM", 12, 15, "all_stem");
        
        private static final EnumType[] field_176905_n;
        private final int field_176906_o;
        private final String field_176914_p;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002105";
        
        private EnumType(final String p_i45710_1_, final int p_i45710_2_, final int p_i45710_3_, final String p_i45710_4_) {
            this.field_176906_o = p_i45710_3_;
            this.field_176914_p = p_i45710_4_;
        }
        
        public int func_176896_a() {
            return this.field_176906_o;
        }
        
        @Override
        public String toString() {
            return this.field_176914_p;
        }
        
        public static EnumType func_176895_a(int p_176895_0_) {
            if (p_176895_0_ < 0 || p_176895_0_ >= EnumType.field_176905_n.length) {
                p_176895_0_ = 0;
            }
            final EnumType var1 = EnumType.field_176905_n[p_176895_0_];
            return (var1 == null) ? EnumType.field_176905_n[0] : var1;
        }
        
        @Override
        public String getName() {
            return this.field_176914_p;
        }
        
        static {
            field_176905_n = new EnumType[16];
            $VALUES = new EnumType[] { EnumType.NORTH_WEST, EnumType.NORTH, EnumType.NORTH_EAST, EnumType.WEST, EnumType.CENTER, EnumType.EAST, EnumType.SOUTH_WEST, EnumType.SOUTH, EnumType.SOUTH_EAST, EnumType.STEM, EnumType.ALL_INSIDE, EnumType.ALL_OUTSIDE, EnumType.ALL_STEM };
            for (final EnumType var4 : values()) {
                EnumType.field_176905_n[var4.func_176896_a()] = var4;
            }
        }
    }
}
