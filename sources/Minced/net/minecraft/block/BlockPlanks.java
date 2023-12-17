// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPlanks extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    
    public BlockPlanks() {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPlanks.VARIANT, EnumType.OAK));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockPlanks.VARIANT).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final EnumType blockplanks$enumtype : EnumType.values()) {
            items.add(new ItemStack(this, 1, blockplanks$enumtype.getMetadata()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPlanks.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.getValue(BlockPlanks.VARIANT).getMapColor();
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockPlanks.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPlanks.VARIANT });
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        OAK(0, "oak", MapColor.WOOD), 
        SPRUCE(1, "spruce", MapColor.OBSIDIAN), 
        BIRCH(2, "birch", MapColor.SAND), 
        JUNGLE(3, "jungle", MapColor.DIRT), 
        ACACIA(4, "acacia", MapColor.ADOBE), 
        DARK_OAK(5, "dark_oak", "big_oak", MapColor.BROWN);
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final String translationKey;
        private final MapColor mapColor;
        
        private EnumType(final int metaIn, final String nameIn, final MapColor mapColorIn) {
            this(metaIn, nameIn, nameIn, mapColorIn);
        }
        
        private EnumType(final int metaIn, final String nameIn, final String unlocalizedNameIn, final MapColor mapColorIn) {
            this.meta = metaIn;
            this.name = nameIn;
            this.translationKey = unlocalizedNameIn;
            this.mapColor = mapColorIn;
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
        
        public String getTranslationKey() {
            return this.translationKey;
        }
        
        static {
            META_LOOKUP = new EnumType[values().length];
            for (final EnumType blockplanks$enumtype : values()) {
                EnumType.META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype;
            }
        }
    }
}
