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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class BlockSand extends BlockFalling
{
    public static final PropertyEnum<EnumType> VARIANT;
    
    public BlockSand() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSand.VARIANT, EnumType.SAND));
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockSand.VARIANT).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final EnumType blocksand$enumtype : EnumType.values()) {
            items.add(new ItemStack(this, 1, blocksand$enumtype.getMetadata()));
        }
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.getValue(BlockSand.VARIANT).getMapColor();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSand.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockSand.VARIANT).getMetadata();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockSand.VARIANT });
    }
    
    @Override
    public int getDustColor(final IBlockState state) {
        final EnumType blocksand$enumtype = state.getValue(BlockSand.VARIANT);
        return blocksand$enumtype.getDustColor();
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        SAND(0, "sand", "default", MapColor.SAND, -2370656), 
        RED_SAND(1, "red_sand", "red", MapColor.ADOBE, -5679071);
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final MapColor mapColor;
        private final String translationKey;
        private final int dustColor;
        
        private EnumType(final int p_i47157_3_, final String p_i47157_4_, final String p_i47157_5_, final MapColor p_i47157_6_, final int p_i47157_7_) {
            this.meta = p_i47157_3_;
            this.name = p_i47157_4_;
            this.mapColor = p_i47157_6_;
            this.translationKey = p_i47157_5_;
            this.dustColor = p_i47157_7_;
        }
        
        public int getDustColor() {
            return this.dustColor;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public MapColor getMapColor() {
            return this.mapColor;
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
            for (final EnumType blocksand$enumtype : values()) {
                EnumType.META_LOOKUP[blocksand$enumtype.getMetadata()] = blocksand$enumtype;
            }
        }
    }
}
