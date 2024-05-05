package net.minecraft.creativetab;

import javax.annotation.Nullable;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;

public abstract class CreativeTabs
{
    public static final CreativeTabs[] CREATIVE_TAB_ARRAY = new CreativeTabs[12];
    public static final CreativeTabs BUILDING_BLOCKS = new CreativeTabs(0, "buildingBlocks")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Item.getItemFromBlock(Blocks.BRICK_BLOCK));
        }
    };
    public static final CreativeTabs DECORATIONS = new CreativeTabs(1, "decorations")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta());
        }
    };
    public static final CreativeTabs REDSTONE = new CreativeTabs(2, "redstone")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Items.REDSTONE);
        }
    };
    public static final CreativeTabs TRANSPORTATION = new CreativeTabs(3, "transportation")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Item.getItemFromBlock(Blocks.GOLDEN_RAIL));
        }
    };
    public static final CreativeTabs MISC = new CreativeTabs(6, "misc")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Items.LAVA_BUCKET);
        }
    };
    public static final CreativeTabs SEARCH = (new CreativeTabs(5, "search")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Items.COMPASS);
        }
    }).setBackgroundImageName("item_search.png");
    public static final CreativeTabs FOOD = new CreativeTabs(7, "food")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Items.APPLE);
        }
    };
    public static final CreativeTabs TOOLS = (new CreativeTabs(8, "tools")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Items.IRON_AXE);
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.ALL, EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs COMBAT = (new CreativeTabs(9, "combat")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Items.GOLDEN_SWORD);
        }
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.ALL, EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_CHEST, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON, EnumEnchantmentType.WEARABLE, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs BREWING = new CreativeTabs(10, "brewing")
    {
        public ItemStack createIcon()
        {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
        }
    };
    public static final CreativeTabs MATERIALS = MISC;
    public static final CreativeTabs HOTBAR = new CreativeTabs(4, "hotbar")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Blocks.BOOKSHELF);
        }
        public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_)
        {
            throw new RuntimeException("Implement exception client-side.");
        }
        public boolean isAlignedRight()
        {
            return true;
        }
    };
    public static final CreativeTabs INVENTORY = (new CreativeTabs(11, "inventory")
    {
        public ItemStack createIcon()
        {
            return new ItemStack(Item.getItemFromBlock(Blocks.CHEST));
        }
    }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int index;
    private final String tabLabel;

    /** Texture to use. */
    private String backgroundTexture = "items.png";
    private boolean hasScrollbar = true;

    /** Whether to draw the title in the foreground of the creative GUI */
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes = new EnumEnchantmentType[0];
    private ItemStack icon;

    public CreativeTabs(int index, String label)
    {
        this.index = index;
        this.tabLabel = label;
        this.icon = ItemStack.EMPTY;
        CREATIVE_TAB_ARRAY[index] = this;
    }

    public int getIndex()
    {
        return this.index;
    }

    public String getTabLabel()
    {
        return this.tabLabel;
    }

    /**
     * Gets the translated Label.
     */
    public String getTranslationKey()
    {
        return "itemGroup." + this.getTabLabel();
    }

    public ItemStack getIcon()
    {
        if (this.icon.isEmpty())
        {
            this.icon = this.createIcon();
        }

        return this.icon;
    }

    public abstract ItemStack createIcon();

    public String getBackgroundImageName()
    {
        return this.backgroundTexture;
    }

    public CreativeTabs setBackgroundImageName(String texture)
    {
        this.backgroundTexture = texture;
        return this;
    }

    public boolean drawInForegroundOfTab()
    {
        return this.drawTitle;
    }

    public CreativeTabs setNoTitle()
    {
        this.drawTitle = false;
        return this;
    }

    public boolean hasScrollbar()
    {
        return this.hasScrollbar;
    }

    public CreativeTabs setNoScrollbar()
    {
        this.hasScrollbar = false;
        return this;
    }

    /**
     * returns index % 6
     */
    public int getColumn()
    {
        return this.index % 6;
    }

    /**
     * returns tabIndex < 6
     */
    public boolean isOnTopRow()
    {
        return this.index < 6;
    }

    public boolean isAlignedRight()
    {
        return this.getColumn() == 5;
    }

    /**
     * Returns the enchantment types relevant to this tab
     */
    public EnumEnchantmentType[] getRelevantEnchantmentTypes()
    {
        return this.enchantmentTypes;
    }

    /**
     * Sets the enchantment types for populating this tab with enchanting books
     */
    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... types)
    {
        this.enchantmentTypes = types;
        return this;
    }

    public boolean hasRelevantEnchantmentType(@Nullable EnumEnchantmentType enchantmentType)
    {
        if (enchantmentType != null)
        {
            for (EnumEnchantmentType enumenchantmenttype : this.enchantmentTypes)
            {
                if (enumenchantmenttype == enchantmentType)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * only shows items which have tabToDisplayOn == this
     */
    public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_)
    {
        for (Item item : Item.REGISTRY)
        {
            item.getSubItems(this, p_78018_1_);
        }
    }
}
