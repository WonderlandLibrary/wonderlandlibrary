// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public abstract class BlockWoodSlab extends BlockSlab
{
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT;
    
    public BlockWoodSlab() {
        super(Material.WOOD);
        IBlockState iblockstate = this.blockState.getBaseState();
        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(BlockWoodSlab.HALF, EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(iblockstate.withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.getValue(BlockWoodSlab.VARIANT).getMapColor();
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.WOODEN_SLAB);
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Blocks.WOODEN_SLAB, 1, state.getValue(BlockWoodSlab.VARIANT).getMetadata());
    }
    
    @Override
    public String getTranslationKey(final int meta) {
        return super.getTranslationKey() + "." + BlockPlanks.EnumType.byMetadata(meta).getTranslationKey();
    }
    
    @Override
    public IProperty<?> getVariantProperty() {
        return BlockWoodSlab.VARIANT;
    }
    
    @Override
    public Comparable<?> getTypeForItem(final ItemStack stack) {
        return BlockPlanks.EnumType.byMetadata(stack.getMetadata() & 0x7);
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (final BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values()) {
            items.add(new ItemStack(this, 1, blockplanks$enumtype.getMetadata()));
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.byMetadata(meta & 0x7));
        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(BlockWoodSlab.HALF, ((meta & 0x8) == 0x0) ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockWoodSlab.VARIANT).getMetadata();
        if (!this.isDouble() && state.getValue(BlockWoodSlab.HALF) == EnumBlockHalf.TOP) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockWoodSlab.VARIANT }) : new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockWoodSlab.HALF, BlockWoodSlab.VARIANT });
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockWoodSlab.VARIANT).getMetadata();
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
    }
}
