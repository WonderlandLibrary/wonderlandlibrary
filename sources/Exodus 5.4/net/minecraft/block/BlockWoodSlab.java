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

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public Object getVariant(ItemStack itemStack) {
        return BlockPlanks.EnumType.byMetadata(itemStack.getMetadata() & 7);
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT).func_181070_c();
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT).getMetadata();
    }

    @Override
    public String getUnlocalizedName(int n) {
        return String.valueOf(super.getUnlocalizedName()) + "." + BlockPlanks.EnumType.byMetadata(n).getUnlocalizedName();
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Item.getItemFromBlock(Blocks.wooden_slab);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(VARIANT).getMetadata();
        if (!this.isDouble() && iBlockState.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
            n |= 8;
        }
        return n;
    }

    public BlockWoodSlab() {
        super(Material.wood);
        IBlockState iBlockState = this.blockState.getBaseState();
        if (!this.isDouble()) {
            iBlockState = iBlockState.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(iBlockState.withProperty(VARIANT, BlockPlanks.EnumType.OAK));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    protected BlockState createBlockState() {
        return this.isDouble() ? new BlockState(this, VARIANT) : new BlockState(this, HALF, VARIANT);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        if (item != Item.getItemFromBlock(Blocks.double_wooden_slab)) {
            BlockPlanks.EnumType[] enumTypeArray = BlockPlanks.EnumType.values();
            int n = enumTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                BlockPlanks.EnumType enumType = enumTypeArray[n2];
                list.add(new ItemStack(item, 1, enumType.getMetadata()));
                ++n2;
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        IBlockState iBlockState = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(n & 7));
        if (!this.isDouble()) {
            iBlockState = iBlockState.withProperty(HALF, (n & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }
        return iBlockState;
    }
}

