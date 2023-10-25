/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockColored
extends Block {
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(n));
    }

    public BlockColored(Material material) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        EnumDyeColor[] enumDyeColorArray = EnumDyeColor.values();
        int n = enumDyeColorArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumDyeColor enumDyeColor = enumDyeColorArray[n2];
            list.add(new ItemStack(item, 1, enumDyeColor.getMetadata()));
            ++n2;
        }
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(COLOR).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, COLOR);
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        return iBlockState.getValue(COLOR).getMapColor();
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return iBlockState.getValue(COLOR).getMetadata();
    }
}

