// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPlanks extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    
    public BlockPlanks() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPlanks.VARIANT, EnumType.OAK));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockPlanks.VARIANT).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        for (final EnumType blockplanks$enumtype : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPlanks.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return state.getValue(BlockPlanks.VARIANT).getMapColor();
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockPlanks.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPlanks.VARIANT });
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        OAK(0, "oak", MapColor.woodColor), 
        SPRUCE(1, "spruce", MapColor.obsidianColor), 
        BIRCH(2, "birch", MapColor.sandColor), 
        JUNGLE(3, "jungle", MapColor.dirtColor), 
        ACACIA(4, "acacia", MapColor.adobeColor), 
        DARK_OAK(5, "dark_oak", "big_oak", MapColor.brownColor);
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor mapColor;
        
        private EnumType(final int p_i46388_3_, final String p_i46388_4_, final MapColor p_i46388_5_) {
            this(p_i46388_3_, p_i46388_4_, p_i46388_4_, p_i46388_5_);
        }
        
        private EnumType(final int p_i46389_3_, final String p_i46389_4_, final String p_i46389_5_, final MapColor p_i46389_6_) {
            this.meta = p_i46389_3_;
            this.name = p_i46389_4_;
            this.unlocalizedName = p_i46389_5_;
            this.mapColor = p_i46389_6_;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        public MapColor getMapColor() {
            return this.mapColor;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
        
        static {
            META_LOOKUP = new EnumType[values().length];
            for (final EnumType blockplanks$enumtype : values()) {
                EnumType.META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype;
            }
        }
    }
}
