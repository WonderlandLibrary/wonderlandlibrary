// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockStone extends Block
{
    public static final PropertyEnum VARIANT_PROP;
    private static final String __OBFID = "CL_00000317";
    
    public BlockStone() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStone.VARIANT_PROP, EnumType.STONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (state.getValue(BlockStone.VARIANT_PROP) == EnumType.STONE) ? Item.getItemFromBlock(Blocks.cobblestone) : Item.getItemFromBlock(Blocks.stone);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockStone.VARIANT_PROP)).getMetaFromState();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.getMetaFromState()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStone.VARIANT_PROP, EnumType.getStateFromMeta(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockStone.VARIANT_PROP)).getMetaFromState();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStone.VARIANT_PROP });
    }
    
    static {
        VARIANT_PROP = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        STONE("STONE", 0, 0, "stone"), 
        GRANITE("GRANITE", 1, 1, "granite"), 
        GRANITE_SMOOTH("GRANITE_SMOOTH", 2, 2, "smooth_granite", "graniteSmooth"), 
        DIORITE("DIORITE", 3, 3, "diorite"), 
        DIORITE_SMOOTH("DIORITE_SMOOTH", 4, 4, "smooth_diorite", "dioriteSmooth"), 
        ANDESITE("ANDESITE", 5, 5, "andesite"), 
        ANDESITE_SMOOTH("ANDESITE_SMOOTH", 6, 6, "smooth_andesite", "andesiteSmooth");
        
        private static final EnumType[] BLOCKSTATES;
        private final int meta;
        private final String name;
        private final String field_176654_k;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002058";
        
        private EnumType(final String p_i45680_1_, final int p_i45680_2_, final int p_i45680_3_, final String p_i45680_4_) {
            this(p_i45680_1_, p_i45680_2_, p_i45680_3_, p_i45680_4_, p_i45680_4_);
        }
        
        private EnumType(final String p_i45681_1_, final int p_i45681_2_, final int p_i45681_3_, final String p_i45681_4_, final String p_i45681_5_) {
            this.meta = p_i45681_3_;
            this.name = p_i45681_4_;
            this.field_176654_k = p_i45681_5_;
        }
        
        public int getMetaFromState() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumType getStateFromMeta(int p_176643_0_) {
            if (p_176643_0_ < 0 || p_176643_0_ >= EnumType.BLOCKSTATES.length) {
                p_176643_0_ = 0;
            }
            return EnumType.BLOCKSTATES[p_176643_0_];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        public String func_176644_c() {
            return this.field_176654_k;
        }
        
        static {
            BLOCKSTATES = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.STONE, EnumType.GRANITE, EnumType.GRANITE_SMOOTH, EnumType.DIORITE, EnumType.DIORITE_SMOOTH, EnumType.ANDESITE, EnumType.ANDESITE_SMOOTH };
            for (final EnumType var4 : values()) {
                EnumType.BLOCKSTATES[var4.getMetaFromState()] = var4;
            }
        }
    }
}
