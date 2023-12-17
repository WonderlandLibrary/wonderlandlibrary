// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.creativetab;

import net.minecraft.potion.PotionUtils;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.Items;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnumEnchantmentType;

public abstract class CreativeTabs
{
    public static final CreativeTabs[] CREATIVE_TAB_ARRAY;
    public static final CreativeTabs BUILDING_BLOCKS;
    public static final CreativeTabs DECORATIONS;
    public static final CreativeTabs REDSTONE;
    public static final CreativeTabs TRANSPORTATION;
    public static final CreativeTabs MISC;
    public static final CreativeTabs SEARCH;
    public static final CreativeTabs FOOD;
    public static final CreativeTabs TOOLS;
    public static final CreativeTabs COMBAT;
    public static final CreativeTabs BREWING;
    public static final CreativeTabs MATERIALS;
    public static final CreativeTabs HOTBAR;
    public static final CreativeTabs INVENTORY;
    private final int index;
    private final String tabLabel;
    private String backgroundTexture;
    private boolean hasScrollbar;
    private boolean drawTitle;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack icon;
    
    public CreativeTabs(final int index, final String label) {
        this.backgroundTexture = "items.png";
        this.hasScrollbar = true;
        this.drawTitle = true;
        this.enchantmentTypes = new EnumEnchantmentType[0];
        this.index = index;
        this.tabLabel = label;
        this.icon = ItemStack.EMPTY;
        CreativeTabs.CREATIVE_TAB_ARRAY[index] = this;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String getTabLabel() {
        return this.tabLabel;
    }
    
    public String getTranslationKey() {
        return "itemGroup." + this.getTabLabel();
    }
    
    public ItemStack getIcon() {
        if (this.icon.isEmpty()) {
            this.icon = this.createIcon();
        }
        return this.icon;
    }
    
    public abstract ItemStack createIcon();
    
    public String getBackgroundImageName() {
        return this.backgroundTexture;
    }
    
    public CreativeTabs setBackgroundImageName(final String texture) {
        this.backgroundTexture = texture;
        return this;
    }
    
    public boolean drawInForegroundOfTab() {
        return this.drawTitle;
    }
    
    public CreativeTabs setNoTitle() {
        this.drawTitle = false;
        return this;
    }
    
    public boolean hasScrollbar() {
        return this.hasScrollbar;
    }
    
    public CreativeTabs setNoScrollbar() {
        this.hasScrollbar = false;
        return this;
    }
    
    public int getColumn() {
        return this.index % 6;
    }
    
    public boolean isOnTopRow() {
        return this.index < 6;
    }
    
    public boolean isAlignedRight() {
        return this.getColumn() == 5;
    }
    
    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return this.enchantmentTypes;
    }
    
    public CreativeTabs setRelevantEnchantmentTypes(final EnumEnchantmentType... types) {
        this.enchantmentTypes = types;
        return this;
    }
    
    public boolean hasRelevantEnchantmentType(@Nullable final EnumEnchantmentType enchantmentType) {
        if (enchantmentType != null) {
            for (final EnumEnchantmentType enumenchantmenttype : this.enchantmentTypes) {
                if (enumenchantmenttype == enchantmentType) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void displayAllRelevantItems(final NonNullList<ItemStack> p_78018_1_) {
        for (final Item item : Item.REGISTRY) {
            item.getSubItems(this, p_78018_1_);
        }
    }
    
    static {
        CREATIVE_TAB_ARRAY = new CreativeTabs[12];
        BUILDING_BLOCKS = new CreativeTabs(0, "buildingBlocks") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Item.getItemFromBlock(Blocks.BRICK_BLOCK));
            }
        };
        DECORATIONS = new CreativeTabs(1, "decorations") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta());
            }
        };
        REDSTONE = new CreativeTabs(2, "redstone") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Items.REDSTONE);
            }
        };
        TRANSPORTATION = new CreativeTabs(3, "transportation") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Item.getItemFromBlock(Blocks.GOLDEN_RAIL));
            }
        };
        MISC = new CreativeTabs(6, "misc") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Items.LAVA_BUCKET);
            }
        };
        SEARCH = new CreativeTabs(5, "search") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Items.COMPASS);
            }
        }.setBackgroundImageName("item_search.png");
        FOOD = new CreativeTabs(7, "food") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Items.APPLE);
            }
        };
        TOOLS = new CreativeTabs(8, "tools") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Items.IRON_AXE);
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL, EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE);
        COMBAT = new CreativeTabs(9, "combat") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Items.GOLDEN_SWORD);
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL, EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_CHEST, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON, EnumEnchantmentType.WEARABLE, EnumEnchantmentType.BREAKABLE);
        BREWING = new CreativeTabs(10, "brewing") {
            @Override
            public ItemStack createIcon() {
                return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
            }
        };
        MATERIALS = CreativeTabs.MISC;
        HOTBAR = new CreativeTabs(4, "hotbar") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Blocks.BOOKSHELF);
            }
            
            @Override
            public void displayAllRelevantItems(final NonNullList<ItemStack> p_78018_1_) {
                throw new RuntimeException("Implement exception client-side.");
            }
            
            @Override
            public boolean isAlignedRight() {
                return true;
            }
        };
        INVENTORY = new CreativeTabs(11, "inventory") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Item.getItemFromBlock(Blocks.CHEST));
            }
        }.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    }
}
