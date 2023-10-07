// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;

public abstract class BlockStoneSlabNew extends BlockSlab
{
    public static final PropertyBool field_176558_b;
    public static final PropertyEnum field_176559_M;
    private static final String __OBFID = "CL_00002087";
    
    public BlockStoneSlabNew() {
        super(Material.rock);
        IBlockState var1 = this.blockState.getBaseState();
        if (this.isDouble()) {
            var1 = var1.withProperty(BlockStoneSlabNew.field_176558_b, false);
        }
        else {
            var1 = var1.withProperty(BlockSlab.HALF_PROP, EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(var1.withProperty(BlockStoneSlabNew.field_176559_M, EnumType.RED_SANDSTONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.stone_slab2);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.stone_slab2);
    }
    
    @Override
    public String getFullSlabName(final int p_150002_1_) {
        return super.getUnlocalizedName() + "." + EnumType.func_176916_a(p_150002_1_).func_176918_c();
    }
    
    @Override
    public IProperty func_176551_l() {
        return BlockStoneSlabNew.field_176559_M;
    }
    
    @Override
    public Object func_176553_a(final ItemStack p_176553_1_) {
        return EnumType.func_176916_a(p_176553_1_.getMetadata() & 0x7);
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab2)) {
            for (final EnumType var7 : EnumType.values()) {
                list.add(new ItemStack(itemIn, 1, var7.func_176915_a()));
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(BlockStoneSlabNew.field_176559_M, EnumType.func_176916_a(meta & 0x7));
        if (this.isDouble()) {
            var2 = var2.withProperty(BlockStoneSlabNew.field_176558_b, (meta & 0x8) != 0x0);
        }
        else {
            var2 = var2.withProperty(BlockSlab.HALF_PROP, ((meta & 0x8) == 0x0) ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return var2;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumType)state.getValue(BlockStoneSlabNew.field_176559_M)).func_176915_a();
        if (this.isDouble()) {
            if (state.getValue(BlockStoneSlabNew.field_176558_b)) {
                var3 |= 0x8;
            }
        }
        else if (state.getValue(BlockSlab.HALF_PROP) == EnumBlockHalf.TOP) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return this.isDouble() ? new BlockState(this, new IProperty[] { BlockStoneSlabNew.field_176558_b, BlockStoneSlabNew.field_176559_M }) : new BlockState(this, new IProperty[] { BlockSlab.HALF_PROP, BlockStoneSlabNew.field_176559_M });
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockStoneSlabNew.field_176559_M)).func_176915_a();
    }
    
    static {
        field_176558_b = PropertyBool.create("seamless");
        field_176559_M = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        RED_SANDSTONE("RED_SANDSTONE", 0, 0, "red_sandstone");
        
        private static final EnumType[] field_176921_b;
        private final int field_176922_c;
        private final String field_176919_d;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002086";
        
        private EnumType(final String p_i45697_1_, final int p_i45697_2_, final int p_i45697_3_, final String p_i45697_4_) {
            this.field_176922_c = p_i45697_3_;
            this.field_176919_d = p_i45697_4_;
        }
        
        public int func_176915_a() {
            return this.field_176922_c;
        }
        
        @Override
        public String toString() {
            return this.field_176919_d;
        }
        
        public static EnumType func_176916_a(int p_176916_0_) {
            if (p_176916_0_ < 0 || p_176916_0_ >= EnumType.field_176921_b.length) {
                p_176916_0_ = 0;
            }
            return EnumType.field_176921_b[p_176916_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176919_d;
        }
        
        public String func_176918_c() {
            return this.field_176919_d;
        }
        
        static {
            field_176921_b = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.RED_SANDSTONE };
            for (final EnumType var4 : values()) {
                EnumType.field_176921_b[var4.func_176915_a()] = var4;
            }
        }
    }
}
