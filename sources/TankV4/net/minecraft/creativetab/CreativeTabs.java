package net.minecraft.creativetab;

import java.util.Iterator;
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
   private EnumEnchantmentType[] enchantmentTypes;
   private ItemStack iconItemStack;
   public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks") {
      public Item getTabIconItem() {
         return Item.getItemFromBlock(Blocks.brick_block);
      }
   };
   public static final CreativeTabs tabAllSearch;
   public static final CreativeTabs tabFood;
   public static final CreativeTabs tabMaterials;
   private boolean drawTitle = true;
   private String theTexture = "items.png";
   public static final CreativeTabs tabBrewing;
   public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation") {
      public Item getTabIconItem() {
         return Item.getItemFromBlock(Blocks.golden_rail);
      }
   };
   private final String tabLabel;
   public static final CreativeTabs tabInventory;
   public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone") {
      public Item getTabIconItem() {
         return Items.redstone;
      }
   };
   private final int tabIndex;
   public static final CreativeTabs tabTools;
   public static final CreativeTabs tabMisc;
   private boolean hasScrollbar = true;
   public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations") {
      public int getIconItemDamage() {
         return BlockDoublePlant.EnumPlantType.PAEONIA.getMeta();
      }

      public Item getTabIconItem() {
         return Item.getItemFromBlock(Blocks.double_plant);
      }
   };
   public static final CreativeTabs tabCombat;
   public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];

   public boolean drawInForegroundOfTab() {
      return this.drawTitle;
   }

   public String getBackgroundImageName() {
      return this.theTexture;
   }

   public boolean isTabInFirstRow() {
      return this.tabIndex < 6;
   }

   public CreativeTabs(int var1, String var2) {
      this.tabIndex = var1;
      this.tabLabel = var2;
      creativeTabArray[var1] = this;
   }

   public int getTabIndex() {
      return this.tabIndex;
   }

   public String getTabLabel() {
      return this.tabLabel;
   }

   public int getTabColumn() {
      return this.tabIndex % 6;
   }

   public boolean shouldHidePlayerInventory() {
      return this.hasScrollbar;
   }

   public CreativeTabs setNoTitle() {
      this.drawTitle = false;
      return this;
   }

   static {
      tabMisc = (new CreativeTabs(4, "misc") {
         public Item getTabIconItem() {
            return Items.lava_bucket;
         }
      }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ALL});
      tabAllSearch = (new CreativeTabs(5, "search") {
         public Item getTabIconItem() {
            return Items.compass;
         }
      }).setBackgroundImageName("item_search.png");
      tabFood = new CreativeTabs(6, "food") {
         public Item getTabIconItem() {
            return Items.apple;
         }
      };
      tabTools = (new CreativeTabs(7, "tools") {
         public Item getTabIconItem() {
            return Items.iron_axe;
         }
      }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
      tabCombat = (new CreativeTabs(8, "combat") {
         public Item getTabIconItem() {
            return Items.golden_sword;
         }
      }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON});
      tabBrewing = new CreativeTabs(9, "brewing") {
         public Item getTabIconItem() {
            return Items.potionitem;
         }
      };
      tabMaterials = new CreativeTabs(10, "materials") {
         public Item getTabIconItem() {
            return Items.stick;
         }
      };
      tabInventory = (new CreativeTabs(11, "inventory") {
         public Item getTabIconItem() {
            return Item.getItemFromBlock(Blocks.chest);
         }
      }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
   }

   public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
      return this.enchantmentTypes;
   }

   public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... var1) {
      this.enchantmentTypes = var1;
      return this;
   }

   public abstract Item getTabIconItem();

   public CreativeTabs setNoScrollbar() {
      this.hasScrollbar = false;
      return this;
   }

   public ItemStack getIconItemStack() {
      if (this.iconItemStack == null) {
         this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
      }

      return this.iconItemStack;
   }

   public void addEnchantmentBooksToList(List var1, EnumEnchantmentType... var2) {
      Enchantment[] var6;
      int var5 = (var6 = Enchantment.enchantmentsBookList).length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Enchantment var3 = var6[var4];
         if (var3 != null && var3.type != null) {
            boolean var7 = false;

            for(int var8 = 0; var8 < var2.length && !var7; ++var8) {
               if (var3.type == var2[var8]) {
                  var7 = true;
               }
            }

            if (var7) {
               var1.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(var3, var3.getMaxLevel())));
            }
         }
      }

   }

   public boolean hasRelevantEnchantmentType(EnumEnchantmentType var1) {
      if (this.enchantmentTypes == null) {
         return false;
      } else {
         EnumEnchantmentType[] var5;
         int var4 = (var5 = this.enchantmentTypes).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            EnumEnchantmentType var2 = var5[var3];
            if (var2 == var1) {
               return true;
            }
         }

         return false;
      }
   }

   public CreativeTabs setBackgroundImageName(String var1) {
      this.theTexture = var1;
      return this;
   }

   public String getTranslatedTabLabel() {
      return "itemGroup." + this.getTabLabel();
   }

   public void displayAllReleventItems(List var1) {
      Iterator var3 = Item.itemRegistry.iterator();

      while(var3.hasNext()) {
         Item var2 = (Item)var3.next();
         if (var2 != null && var2.getCreativeTab() == this) {
            var2.getSubItems(var2, this, var1);
         }
      }

      if (this.getRelevantEnchantmentTypes() != null) {
         this.addEnchantmentBooksToList(var1, this.getRelevantEnchantmentTypes());
      }

   }

   public int getIconItemDamage() {
      return 0;
   }
}
