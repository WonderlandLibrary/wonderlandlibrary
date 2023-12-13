/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.creativetab;

import java.util.List;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabs {
    public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
    public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks"){
        private static final String __OBFID = "CL_00000006";

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.brick_block);
        }
    };
    public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations"){
        private static final String __OBFID = "CL_00000010";

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.double_plant);
        }

        @Override
        public int getIconItemDamage() {
            return BlockDoublePlant.EnumPlantType.PAEONIA.func_176936_a();
        }
    };
    public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone"){
        private static final String __OBFID = "CL_00000011";

        @Override
        public Item getTabIconItem() {
            return Items.redstone;
        }
    };
    public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation"){
        private static final String __OBFID = "CL_00000012";

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.golden_rail);
        }
    };
    public static final CreativeTabs tabMisc = new CreativeTabs(4, "misc"){
        private static final String __OBFID = "CL_00000014";

        @Override
        public Item getTabIconItem() {
            return Items.lava_bucket;
        }
    }.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL);
    public static final CreativeTabs tabAllSearch = new CreativeTabs(5, "search"){
        private static final String __OBFID = "CL_00000015";

        @Override
        public Item getTabIconItem() {
            return Items.compass;
        }
    }.setBackgroundImageName("item_search.png");
    public static final CreativeTabs tabFood = new CreativeTabs(6, "food"){
        private static final String __OBFID = "CL_00000016";

        @Override
        public Item getTabIconItem() {
            return Items.apple;
        }
    };
    public static final CreativeTabs tabTools = new CreativeTabs(7, "tools"){
        private static final String __OBFID = "CL_00000017";

        @Override
        public Item getTabIconItem() {
            return Items.iron_axe;
        }
    }.setRelevantEnchantmentTypes(EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE);
    public static final CreativeTabs tabCombat = new CreativeTabs(8, "combat"){
        private static final String __OBFID = "CL_00000018";

        @Override
        public Item getTabIconItem() {
            return Items.golden_sword;
        }
    }.setRelevantEnchantmentTypes(EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON);
    public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing"){
        private static final String __OBFID = "CL_00000007";

        @Override
        public Item getTabIconItem() {
            return Items.potionitem;
        }
    };
    public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials"){
        private static final String __OBFID = "CL_00000008";

        @Override
        public Item getTabIconItem() {
            return Items.stick;
        }
    };
    public static final CreativeTabs tabInventory = new CreativeTabs(11, "inventory"){
        private static final String __OBFID = "CL_00000009";

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.chest);
        }
    }.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String tabLabel;
    private String theTexture = "items.png";
    private boolean hasScrollbar = true;
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;
    private static final String __OBFID = "CL_00000005";

    public CreativeTabs(int index, String label) {
        this.tabIndex = index;
        this.tabLabel = label;
        CreativeTabs.creativeTabArray[index] = this;
    }

    public int getTabIndex() {
        return this.tabIndex;
    }

    public String getTabLabel() {
        return this.tabLabel;
    }

    public String getTranslatedTabLabel() {
        return "itemGroup." + this.getTabLabel();
    }

    public ItemStack getIconItemStack() {
        if (this.iconItemStack == null) {
            this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
        }
        return this.iconItemStack;
    }

    public abstract Item getTabIconItem();

    public int getIconItemDamage() {
        return 0;
    }

    public String getBackgroundImageName() {
        return this.theTexture;
    }

    public CreativeTabs setBackgroundImageName(String texture) {
        this.theTexture = texture;
        return this;
    }

    public boolean drawInForegroundOfTab() {
        return this.drawTitle;
    }

    public CreativeTabs setNoTitle() {
        this.drawTitle = false;
        return this;
    }

    public boolean shouldHidePlayerInventory() {
        return this.hasScrollbar;
    }

    public CreativeTabs setNoScrollbar() {
        this.hasScrollbar = false;
        return this;
    }

    public int getTabColumn() {
        return this.tabIndex % 6;
    }

    public boolean isTabInFirstRow() {
        return this.tabIndex < 6;
    }

    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return this.enchantmentTypes;
    }

    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType ... types) {
        this.enchantmentTypes = types;
        return this;
    }

    public boolean hasRelevantEnchantmentType(EnumEnchantmentType p_111226_1_) {
        if (this.enchantmentTypes == null) {
            return false;
        }
        for (EnumEnchantmentType var5 : this.enchantmentTypes) {
            if (var5 != p_111226_1_) continue;
            return true;
        }
        return false;
    }

    public void displayAllReleventItems(List p_78018_1_) {
        for (Item var3 : Item.itemRegistry) {
            if (var3 == null || var3.getCreativeTab() != this) continue;
            var3.getSubItems(var3, this, p_78018_1_);
        }
        if (this.getRelevantEnchantmentTypes() != null) {
            this.addEnchantmentBooksToList(p_78018_1_, this.getRelevantEnchantmentTypes());
        }
    }

    public void addEnchantmentBooksToList(List p_92116_1_, EnumEnchantmentType ... p_92116_2_) {
        for (Enchantment var6 : Enchantment.enchantmentsList) {
            if (var6 == null || var6.type == null) continue;
            boolean var7 = false;
            for (int var8 = 0; var8 < p_92116_2_.length && !var7; ++var8) {
                if (var6.type != p_92116_2_[var8]) continue;
                var7 = true;
            }
            if (!var7) continue;
            p_92116_1_.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(var6, var6.getMaxLevel())));
        }
    }
}

