package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public abstract class BlockFlower extends BlockBush
{
    protected PropertyEnum<EnumFlowerType> type;

    protected BlockFlower()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), this.getBlockType() == EnumFlowerColor.RED ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION));
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state)
    {
        return ((EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for (EnumFlowerType blockflower$enumflowertype : EnumFlowerType.getTypes(this.getBlockType()))
        {
            list.add(new ItemStack(itemIn, 1, blockflower$enumflowertype.getMeta()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(this.getTypeProperty(), EnumFlowerType.getType(this.getBlockType(), meta));
    }

    /**
     * Get the Type of this flower (Yellow/Red)
     */
    public abstract EnumFlowerColor getBlockType();

    public IProperty<EnumFlowerType> getTypeProperty()
    {
        if (this.type == null)
        {
            this.type = PropertyEnum.<EnumFlowerType>create("type", EnumFlowerType.class, new Predicate<EnumFlowerType>()
            {
                public boolean apply(EnumFlowerType p_apply_1_)
                {
                    return p_apply_1_.getBlockType() == BlockFlower.this.getBlockType();
                }
            });
        }

        return this.type;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {this.getTypeProperty()});
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    public EnumOffsetType getOffsetType()
    {
        return EnumOffsetType.XZ;
    }

    public static enum EnumFlowerColor
    {
        YELLOW,
        RED;

        public BlockFlower getBlock()
        {
            return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
        }
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

        private static final EnumFlowerType[][] TYPES_FOR_BLOCK = new EnumFlowerType[EnumFlowerColor.values().length][];
        private final EnumFlowerColor blockType;
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumFlowerType(EnumFlowerColor blockType, int meta, String name)
        {
            this(blockType, meta, name, name);
        }

        private EnumFlowerType(EnumFlowerColor blockType, int meta, String name, String unlocalizedName)
        {
            this.blockType = blockType;
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public EnumFlowerColor getBlockType()
        {
            return this.blockType;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumFlowerType getType(EnumFlowerColor blockType, int meta)
        {
            EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];

            if (meta < 0 || meta >= ablockflower$enumflowertype.length)
            {
                meta = 0;
            }

            return ablockflower$enumflowertype[meta];
        }

        public static EnumFlowerType[] getTypes(EnumFlowerColor flowerColor)
        {
            return TYPES_FOR_BLOCK[flowerColor.ordinal()];
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static {
            for (final EnumFlowerColor blockflower$enumflowercolor : EnumFlowerColor.values())
            {
                Collection<EnumFlowerType> collection = Collections2.<EnumFlowerType>filter(Lists.newArrayList(values()), new Predicate<EnumFlowerType>()
                {
                    public boolean apply(EnumFlowerType p_apply_1_)
                    {
                        return p_apply_1_.getBlockType() == blockflower$enumflowercolor;
                    }
                });
                TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = (EnumFlowerType[])collection.toArray(new EnumFlowerType[collection.size()]);
            }
        }
    }
}
