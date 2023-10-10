/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class BlockWoodSlab
extends BlockSlab {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);

    public BlockWoodSlab() {
        super(Material.wood);
        IBlockState iblockstate = this.blockState.getBaseState();
        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(iblockstate.withProperty(VARIANT, BlockPlanks.EnumType.OAK));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return state.getValue(VARIANT).func_181070_c();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName() + "." + BlockPlanks.EnumType.byMetadata(meta).getUnlocalizedName();
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public Object getVariant(ItemStack stack) {
        return BlockPlanks.EnumType.byMetadata(stack.getMetadata() & 7);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        if (itemIn == Item.getItemFromBlock(Blocks.double_wooden_slab)) return;
        BlockPlanks.EnumType[] enumTypeArray = BlockPlanks.EnumType.values();
        int n = enumTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            BlockPlanks.EnumType blockplanks$enumtype = enumTypeArray[n2];
            list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
            ++n2;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta & 7));
        if (this.isDouble()) return iblockstate;
        return iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (this.isDouble()) return i |= state.getValue(VARIANT).getMetadata();
        if (state.getValue(HALF) != BlockSlab.EnumBlockHalf.TOP) return i |= state.getValue(VARIANT).getMetadata();
        i |= 8;
        return i |= state.getValue(VARIANT).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        BlockState blockState;
        if (this.isDouble()) {
            blockState = new BlockState(this, VARIANT);
            return blockState;
        }
        blockState = new BlockState(this, HALF, VARIANT);
        return blockState;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }
}

