package team.massacre.impl.module.miscellaneous;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.Representation;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventPacket;
import team.massacre.impl.event.EventUpdate;
import team.massacre.impl.event.WindowClickEvent;
import team.massacre.utils.PlayerUtils;
import team.massacre.utils.TimerUtil;

public class InvManager extends Module {
   public final Property<Boolean> Food = new Property("Food", true);
   public final Property<Boolean> sort = new Property("Sort", true);
   public final Property<Boolean> Archery = new Property("Archery", true);
   public final Property<Boolean> Sword = new Property("Sword", true);
   public final Property<Boolean> InvCleaner = new Property("Cleaner", true);
   public final Property<Boolean> UHC = new Property("UHC", false);
   public final Property<Boolean> inventoryOnly = new Property("Inventory Only", true);
   public final DoubleProperty Delay;
   public final DoubleProperty BlockCap;
   private boolean openInventory;
   private TimerUtil timer;
   private int lastSlot;
   private final int weaponSlot;
   private final int pickaxeSlot;
   private final int axeSlot;
   private final int shovelSlot;
   final ArrayList<Integer> whitelistedItems;

   public InvManager() {
      super("InvCleaner", 0, Category.MISCELLANEOUS);
      this.Delay = new DoubleProperty("Delay", 150.0D, 10.0D, 750.0D, 10.0D, Representation.MILLISECONDS);
      this.BlockCap = new DoubleProperty("Block Cap", 128.0D, 0.0D, 256.0D, 8.0D);
      this.timer = new TimerUtil();
      this.weaponSlot = 36;
      this.pickaxeSlot = 37;
      this.axeSlot = 38;
      this.shovelSlot = 39;
      this.whitelistedItems = new ArrayList();
      this.addValues(new Property[]{this.Food, this.sort, this.Archery, this.Sword, this.InvCleaner, this.UHC, this.inventoryOnly, this.Delay, this.BlockCap});
   }

   public void onEnable() {
      super.onEnable();
      this.lastSlot = -1;
   }

   public void onDisable() {
      super.onDisable();
   }

   @Handler
   public void ee(WindowClickEvent event) {
      this.timer.reset();
   }

   @Handler
   public void a(EventPacket event) {
      Packet packet;
      if (this.openInventory) {
         packet = event.getPacket();
         if (packet instanceof C16PacketClientStatus) {
            C16PacketClientStatus clientStatus = (C16PacketClientStatus)event.getPacket();
            if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
               event.cancelEvent();
            }
         } else if (packet instanceof C0DPacketCloseWindow) {
            event.cancelEvent();
         } else if (packet instanceof C02PacketUseEntity) {
            this.close();
         }
      }

      if (this.openInventory) {
         packet = event.getPacket();
         if (packet instanceof S2DPacketOpenWindow) {
            this.close();
         } else if (packet instanceof S2EPacketCloseWindow) {
            event.cancelEvent();
         }
      }

   }

   @Handler
   public void onMotionUpdate(EventUpdate event) {
      if (!(this.mc.thePlayer.openContainer instanceof ContainerChest) || !(this.mc.currentScreen instanceof GuiContainer)) {
         long delay = ((Double)this.Delay.getValue()).longValue();
         if (!(Boolean)this.inventoryOnly.getValue() || this.mc.currentScreen instanceof GuiInventory || !this.mc.thePlayer.isMoving()) {
            if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory || this.mc.currentScreen instanceof GuiChat) {
               if (this.timer.hasHit((float)delay)) {
                  if (!this.mc.thePlayer.inventoryContainer.getSlot(36).getHasStack()) {
                     this.getBestWeapon(36);
                  } else if (!this.isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(36).getStack())) {
                     this.getBestWeapon(36);
                  }
               }

               if ((Boolean)this.sort.getValue()) {
                  if (this.timer.hasHit((float)delay)) {
                     this.getBestPickaxe(37);
                  }

                  if (this.timer.hasHit((float)delay)) {
                     this.getBestShovel(39);
                  }

                  if (this.timer.hasHit((float)delay)) {
                     this.getBestAxe(38);
                  }
               }

               if (this.timer.hasHit((float)delay) && (Boolean)this.InvCleaner.getValue() && !this.mc.thePlayer.isUsingItem()) {
                  for(int i = 9; i < 45; ++i) {
                     if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (this.shouldDrop(is, i)) {
                           this.drop(i);
                           if (delay > 0L) {
                              break;
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public void shiftClick(int slot) {
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, this.mc.thePlayer);
   }

   public void swap(int slot1, int hotbarSlot) {
      this.open();
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, this.mc.thePlayer);
      this.close();
   }

   public void drop(int slot) {
      this.open();
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, this.mc.thePlayer);
      this.close();
   }

   public boolean isBestWeapon(ItemStack stack) {
      float damage = this.getDamage(stack);

      for(int i = 9; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (this.getDamage(is) > damage && (is.getItem() instanceof ItemSword || !(Boolean)this.Sword.getValue())) {
               return false;
            }
         }
      }

      if (!(stack.getItem() instanceof ItemSword) && (Boolean)this.Sword.getValue()) {
         return false;
      } else {
         return true;
      }
   }

   public void getBestWeapon(int slot) {
      for(int i = 9; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (this.isBestWeapon(is) && this.getDamage(is) > 0.0F && (is.getItem() instanceof ItemSword || !(Boolean)this.Sword.getValue())) {
               this.swap(i, slot - 36);
               break;
            }
         }
      }

   }

   private float getDamage(ItemStack stack) {
      float damage = 0.0F;
      Item item = stack.getItem();
      if (item instanceof ItemTool) {
         ItemTool tool = (ItemTool)item;
         damage += (float)tool.getMaxDamage();
      }

      if (item instanceof ItemSword) {
         ItemSword sword = (ItemSword)item;
         damage += sword.getDamageVsEntity();
      }

      damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
      return damage;
   }

   public boolean shouldDrop(ItemStack stack, int slot) {
      if (stack.getDisplayName().contains("???")) {
         return false;
      } else if (stack.getDisplayName().contains("???")) {
         return false;
      } else if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
         return false;
      } else if (stack.getDisplayName().toLowerCase().contains("tracking compass")) {
         return false;
      } else {
         if ((Boolean)this.UHC.getValue()) {
            if (stack.getDisplayName().toLowerCase().contains("?")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("apple")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("head")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("gold")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("crafting table")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("stick")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("and") && stack.getDisplayName().toLowerCase().contains("ril")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("axe of perun")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("barbarian")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("bloodlust")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("dragonchest")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("dragon sword")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("dragon armor")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("excalibur")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("exodus")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("fusion armor")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("hermes boots")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("hide of leviathan")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("scythe")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("seven-league boots")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("shoes of vidar")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("apprentice")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("master")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("vorpal")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("enchanted")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("spiked")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("tarnhelm")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("philosopher")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("anvil")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("panacea")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("fusion")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("excalibur")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("??")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("??????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("?????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("??????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("??")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("????????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("??¯")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("backpack")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("??????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("????????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("?????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("??????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("???????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("??????")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("hermes")) {
               return false;
            }

            if (stack.getDisplayName().toLowerCase().contains("barbarian")) {
               return false;
            }
         }

         if ((slot != 36 || !this.isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(36).getStack())) && (slot != 37 || !this.isBestPickaxe(this.mc.thePlayer.inventoryContainer.getSlot(37).getStack())) && (slot != 38 || !this.isBestAxe(this.mc.thePlayer.inventoryContainer.getSlot(38).getStack())) && (slot != 39 || !this.isBestShovel(this.mc.thePlayer.inventoryContainer.getSlot(39).getStack()))) {
            if (stack.getItem() instanceof ItemArmor) {
               for(int type = 1; type < 5; ++type) {
                  if (this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                     ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                     if (isBestArmor(is, type)) {
                        continue;
                     }
                  }

                  if (isBestArmor(stack, type)) {
                     return false;
                  }
               }
            }

            if (((Double)this.BlockCap.getValue()).intValue() != 0 && stack.getItem() instanceof ItemBlock && this.getBlockCount() > ((Double)this.BlockCap.getValue()).intValue()) {
               return true;
            } else if (stack.getItem() instanceof ItemPotion && this.isBadPotion(stack)) {
               return true;
            } else if (stack.getItem() instanceof ItemFood && (Boolean)this.Food.getValue() && !(stack.getItem() instanceof ItemAppleGold)) {
               return true;
            } else if (!(stack.getItem() instanceof ItemHoe) && !(stack.getItem() instanceof ItemTool) && !(stack.getItem() instanceof ItemSword) && !(stack.getItem() instanceof ItemArmor)) {
               if ((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow")) && (Boolean)this.Archery.getValue()) {
                  return true;
               } else {
                  return stack.getItem().getUnlocalizedName().contains("tnt") || stack.getItem().getUnlocalizedName().contains("stick") || stack.getItem().getUnlocalizedName().contains("egg") || stack.getItem().getUnlocalizedName().contains("string") || stack.getItem().getUnlocalizedName().contains("cake") || stack.getItem().getUnlocalizedName().contains("mushroom") || stack.getItem().getUnlocalizedName().contains("flint") || stack.getItem().getUnlocalizedName().contains("compass") || stack.getItem().getUnlocalizedName().contains("dyePowder") || stack.getItem().getUnlocalizedName().contains("feather") || stack.getItem().getUnlocalizedName().contains("bucket") || stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect") || stack.getItem().getUnlocalizedName().contains("snow") || stack.getItem().getUnlocalizedName().contains("fish") || stack.getItem().getUnlocalizedName().contains("enchant") || stack.getItem().getUnlocalizedName().contains("exp") || stack.getItem().getUnlocalizedName().contains("shears") || stack.getItem().getUnlocalizedName().contains("anvil") || stack.getItem().getUnlocalizedName().contains("torch") || stack.getItem().getUnlocalizedName().contains("seeds") || stack.getItem().getUnlocalizedName().contains("leather") || stack.getItem().getUnlocalizedName().contains("reeds") || stack.getItem().getUnlocalizedName().contains("skull") || stack.getItem().getUnlocalizedName().contains("record") || stack.getItem().getUnlocalizedName().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getUnlocalizedName().contains("piston");
               }
            } else {
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public ArrayList<Integer> getWhitelistedItem() {
      return this.whitelistedItems;
   }

   private int getBlockCount() {
      int blockCount = 0;

      for(int i = 0; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (is.getItem() instanceof ItemBlock) {
               blockCount += is.stackSize;
            }
         }
      }

      return blockCount;
   }

   private void getBestPickaxe(int slot) {
      for(int i = 9; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (this.isBestPickaxe(is) && 37 != i && !this.isBestWeapon(is)) {
               if (!this.mc.thePlayer.inventoryContainer.getSlot(37).getHasStack()) {
                  this.swap(i, 1);
                  if (((Double)this.Delay.getValue()).longValue() > 0L) {
                     return;
                  }
               } else if (!this.isBestPickaxe(this.mc.thePlayer.inventoryContainer.getSlot(37).getStack())) {
                  this.swap(i, 1);
                  if (((Double)this.Delay.getValue()).longValue() > 0L) {
                     return;
                  }
               }
            }
         }
      }

   }

   private void getBestShovel(int slot) {
      for(int i = 9; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (this.isBestShovel(is) && 39 != i && !this.isBestWeapon(is)) {
               if (!this.mc.thePlayer.inventoryContainer.getSlot(39).getHasStack()) {
                  this.swap(i, 3);
                  this.timer.reset();
                  if (((Double)this.Delay.getValue()).longValue() > 0L) {
                     return;
                  }
               } else if (!this.isBestShovel(this.mc.thePlayer.inventoryContainer.getSlot(39).getStack())) {
                  this.swap(i, 3);
                  this.timer.reset();
                  if (((Double)this.Delay.getValue()).longValue() > 0L) {
                     return;
                  }
               }
            }
         }
      }

   }

   private void getBestAxe(int slot) {
      for(int i = 9; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (this.isBestAxe(is) && 38 != i && !this.isBestWeapon(is)) {
               if (!this.mc.thePlayer.inventoryContainer.getSlot(38).getHasStack()) {
                  this.swap(i, 2);
                  this.timer.reset();
                  if (((Double)this.Delay.getValue()).longValue() > 0L) {
                     return;
                  }
               } else if (!this.isBestAxe(this.mc.thePlayer.inventoryContainer.getSlot(38).getStack())) {
                  this.swap(i, 2);
                  this.timer.reset();
                  if (((Double)this.Delay.getValue()).longValue() > 0L) {
                     return;
                  }
               }
            }
         }
      }

   }

   private boolean isBestPickaxe(ItemStack stack) {
      Item item = stack.getItem();
      if (!(item instanceof ItemPickaxe)) {
         return false;
      } else {
         float value = this.getToolEffect(stack);

         for(int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (this.getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean isBestShovel(ItemStack stack) {
      Item item = stack.getItem();
      if (!(item instanceof ItemSpade)) {
         return false;
      } else {
         float value = this.getToolEffect(stack);

         for(int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (this.getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean isBestAxe(ItemStack stack) {
      Item item = stack.getItem();
      if (!(item instanceof ItemAxe)) {
         return false;
      } else {
         float value = this.getToolEffect(stack);

         for(int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (this.getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !this.isBestWeapon(stack)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private float getToolEffect(ItemStack stack) {
      Item item = stack.getItem();
      if (!(item instanceof ItemTool)) {
         return 0.0F;
      } else {
         String name = item.getUnlocalizedName();
         ItemTool tool = (ItemTool)item;
         float value = 1.0F;
         if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
               value -= 5.0F;
            }
         } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
               value -= 5.0F;
            }
         } else {
            if (!(item instanceof ItemAxe)) {
               return 1.0F;
            }

            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
               value -= 5.0F;
            }
         }

         value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D);
         value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0D);
         return value;
      }
   }

   private boolean isBadPotion(ItemStack stack) {
      if (stack != null && stack.getItem() instanceof ItemPotion) {
         ItemPotion potion = (ItemPotion)stack.getItem();
         if (potion.getEffects(stack) == null) {
            return true;
         }

         Iterator var3 = potion.getEffects(stack).iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            PotionEffect effect = (PotionEffect)o;
            if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
               return true;
            }
         }
      }

      return false;
   }

   boolean invContainsType(int type) {
      for(int i = 9; i < 45; ++i) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (item instanceof ItemArmor) {
               ItemArmor armor = (ItemArmor)item;
               if (type == armor.armorType) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean isBestArmor(ItemStack stack, int type) {
      float prot = getProtection(stack);
      String strType = "";
      if (type == 1) {
         strType = "helmet";
      } else if (type == 2) {
         strType = "chestplate";
      } else if (type == 3) {
         strType = "leggings";
      } else if (type == 4) {
         strType = "boots";
      }

      if (!stack.getUnlocalizedName().contains(strType)) {
         return false;
      } else {
         for(int i = 5; i < 45; ++i) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
               if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static float getProtection(ItemStack stack) {
      float prot = 0.0F;
      if (stack.getItem() instanceof ItemArmor) {
         ItemArmor armor = (ItemArmor)stack.getItem();
         prot = (float)((double)prot + (double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D);
         prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0D);
         prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0D);
         prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0D);
         prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0D);
         prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack) / 100.0D);
      }

      return prot;
   }

   private void open() {
      if (!this.openInventory) {
         this.timer.reset();
         if (!(Boolean)this.inventoryOnly.getValue()) {
            PlayerUtils.openInventory();
         }

         this.openInventory = true;
      }

   }

   private void close() {
      if (this.openInventory) {
         if (!(Boolean)this.inventoryOnly.getValue()) {
            PlayerUtils.closeInventory();
         }

         this.openInventory = false;
      }

   }
}
