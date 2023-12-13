package cn.hanabi.utils;

import cn.hanabi.injection.interfaces.IItemTools;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;

public class InvUtils {
   private static final Minecraft mc = Minecraft.getMinecraft();
   private static final int hotBarSize = 9;
   private static final int inventoryContainerSize = 36;
   private static final int allInventorySize = 45;
   public static int pickaxeSlot = 37;
   public static int axeSlot = 38;
   public static int shovelSlot = 39;

   public static int findEmptySlot() {
      for(int i = 0; i < 8; ++i) {
         if (mc.thePlayer.inventory.mainInventory[i] == null) {
            return i;
         }
      }

      return mc.thePlayer.inventory.currentItem + (mc.thePlayer.inventory.getCurrentItem() == null ? 0 : (mc.thePlayer.inventory.currentItem < 8 ? 3 : -1));
   }

   public static int findEmptySlot(int priority) {
      return mc.thePlayer.inventory.mainInventory[priority] == null ? priority : findEmptySlot();
   }

   public static int getHotBarSize() {
      return 9;
   }

   public static int getInventoryContainerSize() {
      return 36;
   }

   public static int getAllInventorySize() {
      return 45;
   }

   public static Slot getSlot(int id) {
      return mc.thePlayer.inventoryContainer.getSlot(id);
   }

   public static void swapShift(int slot) {
      mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
   }

   public static void swap(int slot, int hotbarNum) {
      mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
   }

   public static boolean isFull() {
      return !Arrays.asList(mc.thePlayer.inventory.mainInventory).contains((Object)null);
   }

   public static int armorSlotToNormalSlot(int armorSlot) {
      return 8 - armorSlot;
   }

   public static void block() {
      mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
   }

   public static ItemStack getCurrentItem() {
      return mc.thePlayer.getCurrentEquippedItem() == null ? new ItemStack(Blocks.air) : mc.thePlayer.getCurrentEquippedItem();
   }

   public static ItemStack getItemBySlot(int slot) {
      return mc.thePlayer.inventory.mainInventory[slot] == null ? new ItemStack(Blocks.air) : mc.thePlayer.inventory.mainInventory[slot];
   }

   public static List getHotbarContent() {
      return new ArrayList(Arrays.asList(mc.thePlayer.inventory.mainInventory).subList(0, 9));
   }

   public static List getAllInventoryContent() {
      return new ArrayList(Arrays.asList(mc.thePlayer.inventory.mainInventory).subList(0, 35));
   }

   public static List getInventoryContent() {
      return new ArrayList(Arrays.asList(mc.thePlayer.inventory.mainInventory).subList(9, 35));
   }

   public static boolean isValidBlock(Block block, boolean toPlace) {
      if (block instanceof BlockContainer) {
         return false;
      } else if (toPlace) {
         return !(block instanceof BlockFalling) && block.isFullBlock() && block.isFullCube();
      } else {
         Material material = block.getMaterial();
         return !material.isReplaceable() && !material.isLiquid();
      }
   }

   public static boolean isGoodBlockStack(ItemStack stack) {
      return stack.stackSize >= 1 && isValidBlock(Block.getBlockFromItem(stack.getItem()), true);
   }

   public static int getEnchantment(ItemStack itemStack, Enchantment enchantment) {
      if (itemStack != null && itemStack.getEnchantmentTagList() != null && !itemStack.getEnchantmentTagList().hasNoTags()) {
         for(int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); ++i) {
            NBTTagCompound tagCompound = itemStack.getEnchantmentTagList().getCompoundTagAt(i);
            if (tagCompound.hasKey("ench") && tagCompound.getShort("ench") == enchantment.effectId || tagCompound.hasKey("id") && tagCompound.getShort("id") == enchantment.effectId) {
               return tagCompound.getShort("lvl");
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static int getEmptySlotInHotbar() {
      for(int i = 0; i < 9; ++i) {
         if (mc.thePlayer.inventory.mainInventory[i] == null) {
            return i;
         }
      }

      return -1;
   }

   public static float getArmorScore(ItemStack itemStack) {
      if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
         ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
         float score = 0.0F;
         score += (float)itemArmor.damageReduceAmount;
         if (EnchantmentHelper.getEnchantments(itemStack).size() <= 0) {
            score = (float)((double)score - 0.1);
         }

         int protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);
         score = (float)((double)score + (double)protection * 0.2);
         return score;
      } else {
         return -1.0F;
      }
   }

   public static boolean hasWeapon() {
      if (mc.thePlayer.inventory.getCurrentItem() != null) {
         return false;
      } else {
         return mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe || mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
      }
   }

   public static boolean isHeldingSword() {
      return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
   }

   public static void getBestPickaxe() {
      for(int i = 9; i < 45; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (isBestPickaxe(is) && pickaxeSlot != i && isBestWeapon(is)) {
               if (!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
                  swap(i, pickaxeSlot - 36);
               } else if (!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
                  swap(i, pickaxeSlot - 36);
               }
            }
         }
      }

   }

   public static void getBestShovel() {
      for(int i = 9; i < 45; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (isBestShovel(is) && shovelSlot != i && isBestWeapon(is)) {
               if (!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
                  swap(i, shovelSlot - 36);
               } else if (!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) {
                  swap(i, shovelSlot - 36);
               }
            }
         }
      }

   }

   public static void getBestAxe() {
      for(int i = 9; i < 45; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (isBestAxe(is) && axeSlot != i && isBestWeapon(is)) {
               if (!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
                  swap(i, axeSlot - 36);
               } else if (!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) {
                  swap(i, axeSlot - 36);
               }
            }
         }
      }

   }

   public static boolean isBestPickaxe(ItemStack stack) {
      Item item = stack.getItem();
      if (!(item instanceof ItemPickaxe)) {
         return false;
      } else {
         float value = getToolEffect(stack);

         for(int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static boolean isBestShovel(ItemStack stack) {
      Item item = stack.getItem();
      if (!(item instanceof ItemSpade)) {
         return false;
      } else {
         float value = getToolEffect(stack);

         for(int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static boolean isBestAxe(ItemStack stack) {
      Item item = stack.getItem();
      if (!(item instanceof ItemAxe)) {
         return false;
      } else {
         float value = getToolEffect(stack);

         for(int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && isBestWeapon(stack)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static float getToolEffect(ItemStack stack) {
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

         value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
         value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
         return value;
      }
   }

   public static boolean isBestWeapon(ItemStack stack) {
      float damage = getDamage(stack);

      for(int i = 9; i < 45; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (getDamage(is) > damage && is.getItem() instanceof ItemSword) {
               return true;
            }
         }
      }

      return !(stack.getItem() instanceof ItemSword);
   }

   public static float getDamage(ItemStack stack) {
      float damage = 0.0F;
      Item item = stack.getItem();
      if (item instanceof ItemTool) {
         IItemTools tool = (IItemTools)item;
         damage += tool.getdamageVsEntity();
      }

      if (item instanceof ItemSword) {
         ItemSword sword = (ItemSword)item;
         damage += sword.getDamageVsEntity();
      }

      damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
      return damage;
   }

   public static double getItemDamage(ItemStack stack) {
      double damage = 0.0;
      Multimap attributeModifierMap = stack.getAttributeModifiers();
      Iterator var4 = attributeModifierMap.keySet().iterator();

      while(var4.hasNext()) {
         String attributeName = (String)var4.next();
         if (attributeName.equals("generic.attackDamage")) {
            Iterator attributeModifiers = attributeModifierMap.get(attributeName).iterator();
            if (attributeModifiers.hasNext()) {
               damage += ((AttributeModifier)attributeModifiers.next()).getAmount();
            }
            break;
         }
      }

      if (stack.isItemEnchanted()) {
         damage += (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
         damage += (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25;
      }

      return damage;
   }

   public static boolean isItemEmpty(Item item) {
      return item == null || Item.getIdFromItem(item) == 0;
   }
}
