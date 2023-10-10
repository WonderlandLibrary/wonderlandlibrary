/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public abstract class BlockFlower
extends BlockBush {
    protected PropertyEnum<EnumFlowerType> type;

    protected BlockFlower() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), this.getBlockType() == EnumFlowerColor.RED ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(this.getTypeProperty()).getMeta();
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        EnumFlowerType[] enumFlowerTypeArray = EnumFlowerType.getTypes(this.getBlockType());
        int n = enumFlowerTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFlowerType blockflower$enumflowertype = enumFlowerTypeArray[n2];
            list.add(new ItemStack(itemIn, 1, blockflower$enumflowertype.getMeta()));
            ++n2;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(this.getTypeProperty(), EnumFlowerType.getType(this.getBlockType(), meta));
    }

    public abstract EnumFlowerColor getBlockType();

    public IProperty<EnumFlowerType> getTypeProperty() {
        if (this.type != null) return this.type;
        this.type = PropertyEnum.create("type", EnumFlowerType.class, new Predicate<EnumFlowerType>(){

            @Override
            public boolean apply(EnumFlowerType p_apply_1_) {
                if (p_apply_1_.getBlockType() != BlockFlower.this.getBlockType()) return false;
                return true;
            }
        });
        return this.type;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(this.getTypeProperty()).getMeta();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, this.getTypeProperty());
    }

    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumFlowerType implements IStringSerializable
    {
        DANDELION(EnumFlowerColor.YELLOW, 0, "dandelion"),
        POPPY(EnumFlowerColor.RED, 0, "poppy"),
        BLUE_ORCHID(EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
        ALLIUM(EnumFlowerColor.RED, 2, "allium"),
        HOUSTONIA(EnumFlowerColor.RED, 3, "houstonia"),
        RED_TULIP(EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
        ORANGE_TULIP(EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
        WHITE_TULIP(EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
        PINK_TULIP(EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
        OXEYE_DAISY(EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

        private static final EnumFlowerType[][] TYPES_FOR_BLOCK;
        private final EnumFlowerColor blockType;
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumFlowerType(EnumFlowerColor blockType, int meta, String name) {
            this(blockType, meta, name, name);
        }

        private EnumFlowerType(EnumFlowerColor blockType, int meta, String name, String unlocalizedName) {
            this.blockType = blockType;
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public EnumFlowerColor getBlockType() {
            return this.blockType;
        }

        public int getMeta() {
            return this.meta;
        }

        public static EnumFlowerType getType(EnumFlowerColor blockType, int meta) {
            EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];
            if (meta >= 0) {
                if (meta < ablockflower$enumflowertype.length) return ablockflower$enumflowertype[meta];
            }
            meta = 0;
            return ablockflower$enumflowertype[meta];
        }

        public static EnumFlowerType[] getTypes(EnumFlowerColor flowerColor) {
            return TYPES_FOR_BLOCK[flowerColor.ordinal()];
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            TYPES_FOR_BLOCK = new EnumFlowerType[EnumFlowerColor.values().length][];
            EnumFlowerColor[] enumFlowerColorArray = EnumFlowerColor.values();
            int n = enumFlowerColorArray.length;
            int n2 = 0;
            while (n2 < n) {
                final EnumFlowerColor blockflower$enumflowercolor = enumFlowerColorArray[n2];
                Collection<EnumFlowerType> collection = Collections2.filter(Lists.newArrayList(EnumFlowerType.values()), new Predicate<EnumFlowerType>(){

                    @Override
                    public boolean apply(EnumFlowerType p_apply_1_) {
                        if (p_apply_1_.getBlockType() != blockflower$enumflowercolor) return false;
                        return true;
                    }
                });
                EnumFlowerType.TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = collection.toArray(new EnumFlowerType[collection.size()]);
                ++n2;
            }
        }
    }

    public static enum EnumFlowerColor {
        YELLOW,
        RED;


        public BlockFlower getBlock() {
            BlockFlower blockFlower;
            if (this == YELLOW) {
                blockFlower = Blocks.yellow_flower;
                return blockFlower;
            }
            blockFlower = Blocks.red_flower;
            return blockFlower;
        }
    }
}

