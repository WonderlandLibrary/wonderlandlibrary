/*
 * Decompiled with CFR 0.152.
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
    public static final CreativeTabs tabTools;
    public static final CreativeTabs tabBrewing;
    public static final CreativeTabs tabMaterials;
    public static final CreativeTabs tabRedstone;
    private String theTexture = "items.png";
    public static final CreativeTabs tabCombat;
    private ItemStack iconItemStack;
    private EnumEnchantmentType[] enchantmentTypes;
    private final int tabIndex;
    public static final CreativeTabs tabFood;
    public static final CreativeTabs tabMisc;
    public static final CreativeTabs[] creativeTabArray;
    private boolean drawTitle = true;
    public static final CreativeTabs tabDecorations;
    public static final CreativeTabs tabBlock;
    private boolean hasScrollbar = true;
    public static final CreativeTabs tabAllSearch;
    public static final CreativeTabs tabInventory;
    public static final CreativeTabs tabTransport;
    private final String tabLabel;

    public int getTabIndex() {
        return this.tabIndex;
    }

    public void addEnchantmentBooksToList(List<ItemStack> list, EnumEnchantmentType ... enumEnchantmentTypeArray) {
        Enchantment[] enchantmentArray = Enchantment.enchantmentsBookList;
        int n = Enchantment.enchantmentsBookList.length;
        int n2 = 0;
        while (n2 < n) {
            Enchantment enchantment = enchantmentArray[n2];
            if (enchantment != null && enchantment.type != null) {
                boolean bl = false;
                int n3 = 0;
                while (n3 < enumEnchantmentTypeArray.length && !bl) {
                    if (enchantment.type == enumEnchantmentTypeArray[n3]) {
                        bl = true;
                    }
                    ++n3;
                }
                if (bl) {
                    list.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
                }
            }
            ++n2;
        }
    }

    public int getTabColumn() {
        return this.tabIndex % 6;
    }

    public int getIconItemDamage() {
        return 0;
    }

    public String getTranslatedTabLabel() {
        return "itemGroup." + this.getTabLabel();
    }

    public void displayAllReleventItems(List<ItemStack> list) {
        for (Item item : Item.itemRegistry) {
            if (item == null || item.getCreativeTab() != this) continue;
            item.getSubItems(item, this, list);
        }
        if (this.getRelevantEnchantmentTypes() != null) {
            this.addEnchantmentBooksToList(list, this.getRelevantEnchantmentTypes());
        }
    }

    public boolean isTabInFirstRow() {
        return this.tabIndex < 6;
    }

    public boolean hasRelevantEnchantmentType(EnumEnchantmentType enumEnchantmentType) {
        if (this.enchantmentTypes == null) {
            return false;
        }
        EnumEnchantmentType[] enumEnchantmentTypeArray = this.enchantmentTypes;
        int n = this.enchantmentTypes.length;
        int n2 = 0;
        while (n2 < n) {
            EnumEnchantmentType enumEnchantmentType2 = enumEnchantmentTypeArray[n2];
            if (enumEnchantmentType2 == enumEnchantmentType) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public CreativeTabs setNoScrollbar() {
        this.hasScrollbar = false;
        return this;
    }

    public CreativeTabs(int n, String string) {
        this.tabIndex = n;
        this.tabLabel = string;
        CreativeTabs.creativeTabArray[n] = this;
    }

    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return this.enchantmentTypes;
    }

    public ItemStack getIconItemStack() {
        if (this.iconItemStack == null) {
            this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
        }
        return this.iconItemStack;
    }

    public CreativeTabs setNoTitle() {
        this.drawTitle = false;
        return this;
    }

    public boolean drawInForegroundOfTab() {
        return this.drawTitle;
    }

    public boolean shouldHidePlayerInventory() {
        return this.hasScrollbar;
    }

    public String getBackgroundImageName() {
        return this.theTexture;
    }

    public CreativeTabs setBackgroundImageName(String string) {
        this.theTexture = string;
        return this;
    }

    public String getTabLabel() {
        return this.tabLabel;
    }

    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType ... enumEnchantmentTypeArray) {
        this.enchantmentTypes = enumEnchantmentTypeArray;
        return this;
    }

    public abstract Item getTabIconItem();

    static {
        creativeTabArray = new CreativeTabs[12];
        tabBlock = new CreativeTabs(0, "buildingBlocks"){

            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.brick_block);
            }
        };
        tabDecorations = new CreativeTabs(1, "decorations"){

            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.double_plant);
            }

            @Override
            public int getIconItemDamage() {
                return BlockDoublePlant.EnumPlantType.PAEONIA.getMeta();
            }
        };
        tabRedstone = new CreativeTabs(2, "redstone"){

            @Override
            public Item getTabIconItem() {
                return Items.redstone;
            }
        };
        tabTransport = new CreativeTabs(3, "transportation"){

            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.golden_rail);
            }
        };
        tabMisc = new CreativeTabs(4, "misc"){

            @Override
            public Item getTabIconItem() {
                return Items.lava_bucket;
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL);
        tabAllSearch = new CreativeTabs(5, "search"){

            @Override
            public Item getTabIconItem() {
                return Items.compass;
            }
        }.setBackgroundImageName("item_search.png");
        tabFood = new CreativeTabs(6, "food"){

            @Override
            public Item getTabIconItem() {
                return Items.apple;
            }
        };
        tabTools = new CreativeTabs(7, "tools"){

            @Override
            public Item getTabIconItem() {
                return Items.iron_axe;
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE);
        tabCombat = new CreativeTabs(8, "combat"){

            @Override
            public Item getTabIconItem() {
                return Items.golden_sword;
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON);
        tabBrewing = new CreativeTabs(9, "brewing"){

            @Override
            public Item getTabIconItem() {
                return Items.potionitem;
            }
        };
        tabMaterials = new CreativeTabs(10, "materials"){

            @Override
            public Item getTabIconItem() {
                return Items.stick;
            }
        };
        tabInventory = new CreativeTabs(11, "inventory"){

            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.chest);
            }
        }.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    }
}

