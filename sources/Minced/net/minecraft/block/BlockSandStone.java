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

public class BlockSandStone extends Block
{
    public static final PropertyEnum<EnumType> TYPE;
    
    public BlockSandStone() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSandStone.TYPE, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockSandStone.TYPE).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final EnumType blocksandstone$enumtype : EnumType.values()) {
            items.add(new ItemStack(this, 1, blocksandstone$enumtype.getMetadata()));
        }
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.SAND;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSandStone.TYPE, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockSandStone.TYPE).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockSandStone.TYPE });
    }
    
    static {
        TYPE = PropertyEnum.create("type", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT(0, "sandstone", "default"), 
        CHISELED(1, "chiseled_sandstone", "chiseled"), 
        SMOOTH(2, "smooth_sandstone", "smooth");
        
        private static final EnumType[] META_LOOKUP;
        private final int metadata;
        private final String name;
        private final String translationKey;
        
        private EnumType(final int meta, final String name, final String unlocalizedName) {
            this.metadata = meta;
            this.name = name;
            this.translationKey = unlocalizedName;
        }
        
        public int getMetadata() {
            return this.metadata;
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
            for (final EnumType blocksandstone$enumtype : values()) {
                EnumType.META_LOOKUP[blocksandstone$enumtype.getMetadata()] = blocksandstone$enumtype;
            }
        }
    }
}
