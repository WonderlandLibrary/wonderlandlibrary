package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import org.apache.commons.lang3.ArrayUtils;

public class ChestStealer extends Mod {
   public static boolean isChest;
   public static Value onlychest;
   private final Value delay = new Value("ChestStealer", "Delay", 30.0, 0.0, 1000.0, 10.0);
   private final Value trash;
   private final Value tools;
   private final int[] itemHelmet;
   private final int[] itemChestplate;
   private final int[] itemLeggings;
   private final int[] itemBoots;
   TimeHelper time2;
   TimeHelper time;

   public ChestStealer() {
      super("ChestStealer", Category.PLAYER);
      this.trash = new Value("ChestStealer", "Pick Trash", Boolean.TRUE);
      this.tools = new Value("ChestStealer", "Tools", Boolean.TRUE);
      this.time2 = new TimeHelper();
      this.time = new TimeHelper();
      this.itemHelmet = new int[]{298, 302, 306, 310, 314};
      this.itemChestplate = new int[]{299, 303, 307, 311, 315};
      this.itemLeggings = new int[]{300, 304, 308, 312, 316};
      this.itemBoots = new int[]{301, 305, 309, 313, 317};
   }

   public static boolean isContain(int[] arr, int targetValue) {
      return ArrayUtils.contains(arr, targetValue);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      this.setDisplayName("Delay " + this.delay.getValueState());
      if (isChest || !(Boolean)onlychest.getValueState()) {
         if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest c = (ContainerChest)mc.thePlayer.openContainer;
            if (this.isChestEmpty(c)) {
               mc.thePlayer.closeScreen();
            }

            for(int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
               if (c.getLowerChestInventory().getStackInSlot(i) != null && this.time.isDelayComplete((Double)this.delay.getValueState() + (double)(new Random()).nextInt(100)) && (this.itemIsUseful(c, i) || (Boolean)this.trash.getValueState()) && (new Random()).nextInt(100) <= 80) {
                  mc.playerController.windowClick(c.windowId, i, 0, 1, mc.thePlayer);
                  this.time.reset();
               }
            }
         }

      }
   }

   private boolean isChestEmpty(ContainerChest c) {
      for(int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
         if (c.getLowerChestInventory().getStackInSlot(i) != null && (this.itemIsUseful(c, i) || (Boolean)this.trash.getValueState())) {
            return false;
         }
      }

      return true;
   }

   public boolean isPotionNegative(ItemStack itemStack) {
      ItemPotion potion = (ItemPotion)itemStack.getItem();
      List potionEffectList = potion.getEffects(itemStack);
      return potionEffectList.stream().map((potionEffect) -> {
         return Potion.potionTypes[potionEffect.getPotionID()];
      }).anyMatch(Potion::isBadEffect);
   }

   private boolean itemIsUseful(ContainerChest c, int i) {
      ItemStack itemStack = c.getLowerChestInventory().getStackInSlot(i);
      Item item = itemStack.getItem();
      if ((item instanceof ItemAxe || item instanceof ItemPickaxe) && (Boolean)this.tools.getValueState()) {
         return true;
      } else if (item instanceof ItemFood) {
         return true;
      } else if (item instanceof ItemEnderPearl) {
         return true;
      } else if (item instanceof ItemPotion && !this.isPotionNegative(itemStack)) {
         return true;
      } else if (item instanceof ItemSword && this.isBestSword(c, itemStack)) {
         return true;
      } else {
         return item instanceof ItemArmor && this.isBestArmor(c, itemStack) ? true : item instanceof ItemBlock;
      }
   }

   private float getSwordDamage(ItemStack itemStack) {
      float damage = 0.0F;
      Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
      if (attributeModifier.isPresent()) {
         damage = (float)((AttributeModifier)attributeModifier.get()).getAmount();
      }

      return damage + EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
   }

   private boolean isBestSword(ContainerChest c, ItemStack item) {
      float itemdamage1 = this.getSwordDamage(item);
      float itemdamage2 = 0.0F;

      int i;
      float tempdamage;
      for(i = 0; i < 45; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            tempdamage = this.getSwordDamage(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
            if (tempdamage >= itemdamage2) {
               itemdamage2 = tempdamage;
            }
         }
      }

      for(i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
         if (c.getLowerChestInventory().getStackInSlot(i) != null) {
            tempdamage = this.getSwordDamage(c.getLowerChestInventory().getStackInSlot(i));
            if (tempdamage >= itemdamage2) {
               itemdamage2 = tempdamage;
            }
         }
      }

      return itemdamage1 == itemdamage2;
   }

   private boolean isBestArmor(ContainerChest c, ItemStack item) {
      float itempro1 = (float)((ItemArmor)item.getItem()).damageReduceAmount;
      float itempro2 = 0.0F;
      int i;
      float temppro;
      if (isContain(this.itemHelmet, Item.getIdFromItem(item.getItem()))) {
         for(i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemHelmet, Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
               temppro = (float)((ItemArmor)mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
               if (temppro > itempro2) {
                  itempro2 = temppro;
               }
            }
         }

         for(i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemHelmet, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
               temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
               if (temppro > itempro2) {
                  itempro2 = temppro;
               }
            }
         }
      }

      if (isContain(this.itemChestplate, Item.getIdFromItem(item.getItem()))) {
         for(i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemChestplate, Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
               temppro = (float)((ItemArmor)mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
               if (temppro > itempro2) {
                  itempro2 = temppro;
               }
            }
         }

         for(i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemChestplate, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
               temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
               if (temppro > itempro2) {
                  itempro2 = temppro;
               }
            }
         }
      }

      if (isContain(this.itemLeggings, Item.getIdFromItem(item.getItem()))) {
         for(i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemLeggings, Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
               temppro = (float)((ItemArmor)mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
               if (temppro > itempro2) {
                  itempro2 = temppro;
               }
            }
         }

         for(i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemLeggings, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
               temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
               if (temppro > itempro2) {
                  itempro2 = temppro;
               }
            }
         }
      }

      if (isContain(this.itemBoots, Item.getIdFromItem(item.getItem()))) {
         for(i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemBoots, Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
               temppro = (float)((ItemArmor)mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
               if (temppro > itempro2) {
                  itempro2 = temppro;
               }
            }
         }

         for(i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemBoots, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
               temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
               if (temppro > itempro2) {
                  itempro2 = temppro;
               }
            }
         }
      }

      return itempro1 == itempro2;
   }

   static {
      onlychest = new Value("ChestStealer", "Only Chest", Boolean.TRUE);
   }
}
