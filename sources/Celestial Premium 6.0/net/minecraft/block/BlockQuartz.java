/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuartz
extends Block {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

    public BlockQuartz() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (meta == EnumType.LINES_Y.getMetadata()) {
            switch (facing.getAxis()) {
                case Z: {
                    return this.getDefaultState().withProperty(VARIANT, EnumType.LINES_Z);
                }
                case X: {
                    return this.getDefaultState().withProperty(VARIANT, EnumType.LINES_X);
                }
                case Y: {
                    return this.getDefaultState().withProperty(VARIANT, EnumType.LINES_Y);
                }
            }
        }
        return meta == EnumType.CHISELED.getMetadata() ? this.getDefaultState().withProperty(VARIANT, EnumType.CHISELED) : this.getDefaultState().withProperty(VARIANT, EnumType.DEFAULT);
    }

    @Override
    public int damageDropped(IBlockState state) {
        EnumType blockquartz$enumtype = state.getValue(VARIANT);
        return blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z ? blockquartz$enumtype.getMetadata() : EnumType.LINES_Y.getMetadata();
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        EnumType blockquartz$enumtype = state.getValue(VARIANT);
        return blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z ? super.getSilkTouchDrop(state) : new ItemStack(Item.getItemFromBlock(this), 1, EnumType.LINES_Y.getMetadata());
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        tab.add(new ItemStack(this, 1, EnumType.DEFAULT.getMetadata()));
        tab.add(new ItemStack(this, 1, EnumType.CHISELED.getMetadata()));
        tab.add(new ItemStack(this, 1, EnumType.LINES_Y.getMetadata()));
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
        return MapColor.QUARTZ;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90: 
            case CLOCKWISE_90: {
                switch (state.getValue(VARIANT)) {
                    case LINES_X: {
                        return state.withProperty(VARIANT, EnumType.LINES_Z);
                    }
                    case LINES_Z: {
                        return state.withProperty(VARIANT, EnumType.LINES_X);
                    }
                }
                return state;
            }
        }
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, VARIANT);
    }

    public static enum EnumType implements IStringSerializable
    {
        DEFAULT(0, "default", "default"),
        CHISELED(1, "chiseled", "chiseled"),
        LINES_Y(2, "lines_y", "lines"),
        LINES_X(3, "lines_x", "lines"),
        LINES_Z(4, "lines_z", "lines");

        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        private EnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }
            return META_LOOKUP[meta];
        }

        @Override
        public String getName() {
            return this.serializedName;
        }

        static {
            META_LOOKUP = new EnumType[EnumType.values().length];
            EnumType[] arrenumType = EnumType.values();
            int n = arrenumType.length;
            for (int i = 0; i < n; ++i) {
                EnumType blockquartz$enumtype;
                EnumType.META_LOOKUP[blockquartz$enumtype.getMetadata()] = blockquartz$enumtype = arrenumType[i];
            }
        }
    }
}

