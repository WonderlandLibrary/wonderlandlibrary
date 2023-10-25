/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockHugeMushroom
extends Block {
    private final Block smallBlock;
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(n));
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(VARIANT).getMetadata();
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState();
    }

    public BlockHugeMushroom(Material material, MapColor mapColor, Block block) {
        super(material, mapColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.ALL_OUTSIDE));
        this.smallBlock = block;
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Item.getItemFromBlock(this.smallBlock);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, VARIANT);
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        switch (iBlockState.getValue(VARIANT)) {
            case ALL_STEM: {
                return MapColor.clothColor;
            }
            case ALL_INSIDE: {
                return MapColor.sandColor;
            }
            case STEM: {
                return MapColor.sandColor;
            }
        }
        return super.getMapColor(iBlockState);
    }

    @Override
    public int quantityDropped(Random random) {
        return Math.max(0, random.nextInt(10) - 7);
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Item.getItemFromBlock(this.smallBlock);
    }

    public static enum EnumType implements IStringSerializable
    {
        NORTH_WEST(1, "north_west"),
        NORTH(2, "north"),
        NORTH_EAST(3, "north_east"),
        WEST(4, "west"),
        CENTER(5, "center"),
        EAST(6, "east"),
        SOUTH_WEST(7, "south_west"),
        SOUTH(8, "south"),
        SOUTH_EAST(9, "south_east"),
        STEM(10, "stem"),
        ALL_INSIDE(0, "all_inside"),
        ALL_OUTSIDE(14, "all_outside"),
        ALL_STEM(15, "all_stem");

        private final int meta;
        private final String name;
        private static final EnumType[] META_LOOKUP;

        public int getMetadata() {
            return this.meta;
        }

        private EnumType(int n2, String string2) {
            this.meta = n2;
            this.name = string2;
        }

        static {
            META_LOOKUP = new EnumType[16];
            EnumType[] enumTypeArray = EnumType.values();
            int n = enumTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumType enumType;
                EnumType.META_LOOKUP[enumType.getMetadata()] = enumType = enumTypeArray[n2];
                ++n2;
            }
        }

        public static EnumType byMetadata(int n) {
            EnumType enumType;
            if (n < 0 || n >= META_LOOKUP.length) {
                n = 0;
            }
            return (enumType = META_LOOKUP[n]) == null ? META_LOOKUP[0] : enumType;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }
    }
}

