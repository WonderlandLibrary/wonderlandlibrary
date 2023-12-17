// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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

public abstract class BlockStoneSlab extends BlockSlab
{
    public static final PropertyBool SEAMLESS;
    public static final PropertyEnum<EnumType> VARIANT;
    
    public BlockStoneSlab() {
        super(Material.ROCK);
        IBlockState iblockstate = this.blockState.getBaseState();
        if (this.isDouble()) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockStoneSlab.SEAMLESS, false);
        }
        else {
            iblockstate = iblockstate.withProperty(BlockStoneSlab.HALF, EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(iblockstate.withProperty(BlockStoneSlab.VARIANT, EnumType.STONE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.STONE_SLAB);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Blocks.STONE_SLAB, 1, state.getValue(BlockStoneSlab.VARIANT).getMetadata());
    }
    
    @Override
    public String getTranslationKey(final int meta) {
        return super.getTranslationKey() + "." + EnumType.byMetadata(meta).getTranslationKey();
    }
    
    @Override
    public IProperty<?> getVariantProperty() {
        return BlockStoneSlab.VARIANT;
    }
    
    @Override
    public Comparable<?> getTypeForItem(final ItemStack stack) {
        return EnumType.byMetadata(stack.getMetadata() & 0x7);
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final EnumType blockstoneslab$enumtype : EnumType.values()) {
            if (blockstoneslab$enumtype != EnumType.WOOD) {
                items.add(new ItemStack(this, 1, blockstoneslab$enumtype.getMetadata()));
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(BlockStoneSlab.VARIANT, EnumType.byMetadata(meta & 0x7));
        if (this.isDouble()) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockStoneSlab.SEAMLESS, (meta & 0x8) != 0x0);
        }
        else {
            iblockstate = iblockstate.withProperty(BlockStoneSlab.HALF, ((meta & 0x8) == 0x0) ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockStoneSlab.VARIANT).getMetadata();
        if (this.isDouble()) {
            if (state.getValue((IProperty<Boolean>)BlockStoneSlab.SEAMLESS)) {
                i |= 0x8;
            }
        }
        else if (state.getValue(BlockStoneSlab.HALF) == EnumBlockHalf.TOP) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockStoneSlab.SEAMLESS, BlockStoneSlab.VARIANT }) : new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockStoneSlab.HALF, BlockStoneSlab.VARIANT });
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockStoneSlab.VARIANT).getMetadata();
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.getValue(BlockStoneSlab.VARIANT).getMapColor();
    }
    
    static {
        SEAMLESS = PropertyBool.create("seamless");
        VARIANT = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        STONE(0, MapColor.STONE, "stone"), 
        SAND(1, MapColor.SAND, "sandstone", "sand"), 
        WOOD(2, MapColor.WOOD, "wood_old", "wood"), 
        COBBLESTONE(3, MapColor.STONE, "cobblestone", "cobble"), 
        BRICK(4, MapColor.RED, "brick"), 
        SMOOTHBRICK(5, MapColor.STONE, "stone_brick", "smoothStoneBrick"), 
        NETHERBRICK(6, MapColor.NETHERRACK, "nether_brick", "netherBrick"), 
        QUARTZ(7, MapColor.QUARTZ, "quartz");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final MapColor mapColor;
        private final String name;
        private final String translationKey;
        
        private EnumType(final int p_i46381_3_, final MapColor p_i46381_4_, final String p_i46381_5_) {
            this(p_i46381_3_, p_i46381_4_, p_i46381_5_, p_i46381_5_);
        }
        
        private EnumType(final int p_i46382_3_, final MapColor p_i46382_4_, final String p_i46382_5_, final String p_i46382_6_) {
            this.meta = p_i46382_3_;
            this.mapColor = p_i46382_4_;
            this.name = p_i46382_5_;
            this.translationKey = p_i46382_6_;
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
            for (final EnumType blockstoneslab$enumtype : values()) {
                EnumType.META_LOOKUP[blockstoneslab$enumtype.getMetadata()] = blockstoneslab$enumtype;
            }
        }
    }
}
