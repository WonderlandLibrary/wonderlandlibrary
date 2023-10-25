/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt
extends Block {
    public static final PropertyBool SNOWY;
    public static final PropertyEnum<DirtType> VARIANT;

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
        list.add(new ItemStack(this, 1, DirtType.COARSE_DIRT.getMetadata()));
        list.add(new ItemStack(this, 1, DirtType.PODZOL.getMetadata()));
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT).getMetadata();
    }

    static {
        VARIANT = PropertyEnum.create("variant", DirtType.class);
        SNOWY = PropertyBool.create("snowy");
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT, SNOWY);
    }

    @Override
    public int getDamageValue(World world, BlockPos blockPos) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        return iBlockState.getBlock() != this ? 0 : iBlockState.getValue(VARIANT).getMetadata();
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        if (iBlockState.getValue(VARIANT) == DirtType.PODZOL) {
            Block block = iBlockAccess.getBlockState(blockPos.up()).getBlock();
            iBlockState = iBlockState.withProperty(SNOWY, block == Blocks.snow || block == Blocks.snow_layer);
        }
        return iBlockState;
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        DirtType dirtType = iBlockState.getValue(VARIANT);
        if (dirtType == DirtType.PODZOL) {
            dirtType = DirtType.DIRT;
        }
        return dirtType.getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(VARIANT, DirtType.byMetadata(n));
    }

    protected BlockDirt() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, DirtType.DIRT).withProperty(SNOWY, false));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT).func_181066_d();
    }

    public static enum DirtType implements IStringSerializable
    {
        DIRT(0, "dirt", "default", MapColor.dirtColor),
        COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.dirtColor),
        PODZOL(2, "podzol", MapColor.obsidianColor);

        private final String unlocalizedName;
        private final MapColor field_181067_h;
        private static final DirtType[] METADATA_LOOKUP;
        private final String name;
        private final int metadata;

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        private DirtType(int n2, String string2, MapColor mapColor) {
            this(n2, string2, string2, mapColor);
        }

        private DirtType(int n2, String string2, String string3, MapColor mapColor) {
            this.metadata = n2;
            this.name = string2;
            this.unlocalizedName = string3;
            this.field_181067_h = mapColor;
        }

        public String toString() {
            return this.name;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public static DirtType byMetadata(int n) {
            if (n < 0 || n >= METADATA_LOOKUP.length) {
                n = 0;
            }
            return METADATA_LOOKUP[n];
        }

        static {
            METADATA_LOOKUP = new DirtType[DirtType.values().length];
            DirtType[] dirtTypeArray = DirtType.values();
            int n = dirtTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                DirtType dirtType;
                DirtType.METADATA_LOOKUP[dirtType.getMetadata()] = dirtType = dirtTypeArray[n2];
                ++n2;
            }
        }

        public MapColor func_181066_d() {
            return this.field_181067_h;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

