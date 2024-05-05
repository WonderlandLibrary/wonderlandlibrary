package net.minecraft.creativetab;

import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabs {
    public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
    public static final CreativeTabs tabBlock = new /* Unavailable Anonymous Inner Class!! */;
    public static final CreativeTabs tabDecorations = new /* Unavailable Anonymous Inner Class!! */;
    public static final CreativeTabs tabRedstone = new /* Unavailable Anonymous Inner Class!! */;
    public static final CreativeTabs tabTransport = new /* Unavailable Anonymous Inner Class!! */;
    public static final CreativeTabs tabMisc = new /* Unavailable Anonymous Inner Class!! */.setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ALL});
    public static final CreativeTabs tabAllSearch = new /* Unavailable Anonymous Inner Class!! */.setBackgroundImageName("item_search.png");
    public static final CreativeTabs tabFood = new /* Unavailable Anonymous Inner Class!! */;
    public static final CreativeTabs tabTools = new /* Unavailable Anonymous Inner Class!! */.setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs tabCombat = new /* Unavailable Anonymous Inner Class!! */.setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON});
    public static final CreativeTabs tabBrewing = new /* Unavailable Anonymous Inner Class!! */;
    public static final CreativeTabs tabMaterials = new /* Unavailable Anonymous Inner Class!! */;
    public static final CreativeTabs tabInventory = new /* Unavailable Anonymous Inner Class!! */.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String tabLabel;
    private String theTexture = "items.png";
    private boolean hasScrollbar = true;
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;

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

    public boolean hasRelevantEnchantmentType(EnumEnchantmentType enchantmentType) {
        if (this.enchantmentTypes == null) {
            return false;
        }
        for (EnumEnchantmentType enumenchantmenttype : this.enchantmentTypes) {
            if (enumenchantmenttype != enchantmentType) continue;
            return true;
        }
        return false;
    }

    public void displayAllReleventItems(List<ItemStack> p_78018_1_) {
        for (Item item : Item.itemRegistry) {
            if (item == null || item.getCreativeTab() != this) continue;
            item.getSubItems(item, this, p_78018_1_);
        }
        if (this.getRelevantEnchantmentTypes() != null) {
            this.addEnchantmentBooksToList(p_78018_1_, this.getRelevantEnchantmentTypes());
        }
    }

    public void addEnchantmentBooksToList(List<ItemStack> itemList, EnumEnchantmentType ... enchantmentType) {
        for (Enchantment enchantment : Enchantment.enchantmentsBookList) {
            if (enchantment == null || enchantment.type == null) continue;
            boolean flag = false;
            for (int i = 0; i < enchantmentType.length && !flag; ++i) {
                if (enchantment.type != enchantmentType[i]) continue;
                flag = true;
            }
            if (!flag) continue;
            itemList.add((Object)Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
        }
    }
}
