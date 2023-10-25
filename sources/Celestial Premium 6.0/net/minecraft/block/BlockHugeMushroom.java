/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHugeMushroom
extends Block {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
    private final Block smallBlock;

    public BlockHugeMushroom(Material materialIn, MapColor color, Block smallBlockIn) {
        super(materialIn, color);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.ALL_OUTSIDE));
        this.smallBlock = smallBlockIn;
    }

    @Override
    public int quantityDropped(Random random) {
        return Math.max(0, random.nextInt(10) - 7);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
        switch (state.getValue(VARIANT)) {
            case ALL_STEM: {
                return MapColor.CLOTH;
            }
            case ALL_INSIDE: {
                return MapColor.SAND;
            }
            case STEM: {
                return MapColor.SAND;
            }
        }
        return super.getMapColor(state, p_180659_2_, p_180659_3_);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this.smallBlock);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.smallBlock);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState();
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
        block0 : switch (rot) {
            case CLOCKWISE_180: {
                switch (state.getValue(VARIANT)) {
                    case STEM: {
                        break;
                    }
                    case NORTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_EAST);
                    }
                    case NORTH: {
                        return state.withProperty(VARIANT, EnumType.SOUTH);
                    }
                    case NORTH_EAST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_WEST);
                    }
                    case WEST: {
                        return state.withProperty(VARIANT, EnumType.EAST);
                    }
                    case EAST: {
                        return state.withProperty(VARIANT, EnumType.WEST);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_EAST);
                    }
                    case SOUTH: {
                        return state.withProperty(VARIANT, EnumType.NORTH);
                    }
                    case SOUTH_EAST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_WEST);
                    }
                    default: {
                        return state;
                    }
                }
            }
            case COUNTERCLOCKWISE_90: {
                switch (state.getValue(VARIANT)) {
                    case STEM: {
                        break;
                    }
                    case NORTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_WEST);
                    }
                    case NORTH: {
                        return state.withProperty(VARIANT, EnumType.WEST);
                    }
                    case NORTH_EAST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_WEST);
                    }
                    case WEST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH);
                    }
                    case EAST: {
                        return state.withProperty(VARIANT, EnumType.NORTH);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_EAST);
                    }
                    case SOUTH: {
                        return state.withProperty(VARIANT, EnumType.EAST);
                    }
                    case SOUTH_EAST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_EAST);
                    }
                    default: {
                        return state;
                    }
                }
            }
            case CLOCKWISE_90: {
                switch (state.getValue(VARIANT)) {
                    case STEM: {
                        break block0;
                    }
                    case NORTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_EAST);
                    }
                    case NORTH: {
                        return state.withProperty(VARIANT, EnumType.EAST);
                    }
                    case NORTH_EAST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_EAST);
                    }
                    case WEST: {
                        return state.withProperty(VARIANT, EnumType.NORTH);
                    }
                    case EAST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_WEST);
                    }
                    case SOUTH: {
                        return state.withProperty(VARIANT, EnumType.WEST);
                    }
                    case SOUTH_EAST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_WEST);
                    }
                }
                return state;
            }
        }
        return state;
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        EnumType blockhugemushroom$enumtype = state.getValue(VARIANT);
        block0 : switch (mirrorIn) {
            case LEFT_RIGHT: {
                switch (blockhugemushroom$enumtype) {
                    case NORTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_WEST);
                    }
                    case NORTH: {
                        return state.withProperty(VARIANT, EnumType.SOUTH);
                    }
                    case NORTH_EAST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_EAST);
                    }
                    default: {
                        return super.withMirror(state, mirrorIn);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_WEST);
                    }
                    case SOUTH: {
                        return state.withProperty(VARIANT, EnumType.NORTH);
                    }
                    case SOUTH_EAST: 
                }
                return state.withProperty(VARIANT, EnumType.NORTH_EAST);
            }
            case FRONT_BACK: {
                switch (blockhugemushroom$enumtype) {
                    case NORTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_EAST);
                    }
                    default: {
                        break block0;
                    }
                    case NORTH_EAST: {
                        return state.withProperty(VARIANT, EnumType.NORTH_WEST);
                    }
                    case WEST: {
                        return state.withProperty(VARIANT, EnumType.EAST);
                    }
                    case EAST: {
                        return state.withProperty(VARIANT, EnumType.WEST);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(VARIANT, EnumType.SOUTH_EAST);
                    }
                    case SOUTH_EAST: 
                }
                return state.withProperty(VARIANT, EnumType.SOUTH_WEST);
            }
        }
        return super.withMirror(state, mirrorIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, VARIANT);
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

        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;

        private EnumType(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static EnumType byMetadata(int meta) {
            EnumType blockhugemushroom$enumtype;
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }
            return (blockhugemushroom$enumtype = META_LOOKUP[meta]) == null ? META_LOOKUP[0] : blockhugemushroom$enumtype;
        }

        @Override
        public String getName() {
            return this.name;
        }

        static {
            META_LOOKUP = new EnumType[16];
            EnumType[] arrenumType = EnumType.values();
            int n = arrenumType.length;
            for (int i = 0; i < n; ++i) {
                EnumType blockhugemushroom$enumtype;
                EnumType.META_LOOKUP[blockhugemushroom$enumtype.getMetadata()] = blockhugemushroom$enumtype = arrenumType[i];
            }
        }
    }
}

