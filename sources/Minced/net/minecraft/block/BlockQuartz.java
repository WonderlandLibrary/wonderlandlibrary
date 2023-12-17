// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Rotation;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.NonNullList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockQuartz extends Block
{
    public static final PropertyEnum<EnumType> VARIANT;
    
    public BlockQuartz() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockQuartz.VARIANT, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (meta == EnumType.LINES_Y.getMetadata()) {
            switch (facing.getAxis()) {
                case Z: {
                    return this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.LINES_Z);
                }
                case X: {
                    return this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.LINES_X);
                }
                case Y: {
                    return this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.LINES_Y);
                }
            }
        }
        return (meta == EnumType.CHISELED.getMetadata()) ? this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.CHISELED) : this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.DEFAULT);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        final EnumType blockquartz$enumtype = state.getValue(BlockQuartz.VARIANT);
        return (blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z) ? blockquartz$enumtype.getMetadata() : EnumType.LINES_Y.getMetadata();
    }
    
    @Override
    protected ItemStack getSilkTouchDrop(final IBlockState state) {
        final EnumType blockquartz$enumtype = state.getValue(BlockQuartz.VARIANT);
        return (blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z) ? super.getSilkTouchDrop(state) : new ItemStack(Item.getItemFromBlock(this), 1, EnumType.LINES_Y.getMetadata());
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, EnumType.DEFAULT.getMetadata()));
        items.add(new ItemStack(this, 1, EnumType.CHISELED.getMetadata()));
        items.add(new ItemStack(this, 1, EnumType.LINES_Y.getMetadata()));
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.QUARTZ;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockQuartz.VARIANT, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockQuartz.VARIANT).getMetadata();
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                switch (state.getValue(BlockQuartz.VARIANT)) {
                    case LINES_X: {
                        return state.withProperty(BlockQuartz.VARIANT, EnumType.LINES_Z);
                    }
                    case LINES_Z: {
                        return state.withProperty(BlockQuartz.VARIANT, EnumType.LINES_X);
                    }
                    default: {
                        return state;
                    }
                }
                break;
            }
            default: {
                return state;
            }
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockQuartz.VARIANT });
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEFAULT(0, "default", "default"), 
        CHISELED(1, "chiseled", "chiseled"), 
        LINES_Y(2, "lines_y", "lines"), 
        LINES_X(3, "lines_x", "lines"), 
        LINES_Z(4, "lines_z", "lines");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String serializedName;
        private final String translationKey;
        
        private EnumType(final int meta, final String name, final String unlocalizedName) {
            this.meta = meta;
            this.serializedName = name;
            this.translationKey = unlocalizedName;
        }
        
        public int getMetadata() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.translationKey;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.serializedName;
        }
        
        static {
            META_LOOKUP = new EnumType[values().length];
            for (final EnumType blockquartz$enumtype : values()) {
                EnumType.META_LOOKUP[blockquartz$enumtype.getMetadata()] = blockquartz$enumtype;
            }
        }
    }
}
