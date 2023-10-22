package cn.hanabi.modules.modules.combat;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword extends Mod {
   public static TimeHelper publicItemTimer = new TimeHelper();
   public Value slot = new Value("AutoSword", "Slot", 1.0, 1.0, 9.0, 1.0);
   TimeHelper time = new TimeHelper();

   public AutoSword() {
      super("AutoSword", Category.COMBAT);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (publicItemTimer.isDelayComplete(300L)) {
         this.setDisplayName("Slot " + ((Double)this.slot.getValueState()).intValue());
         if (this.time.isDelayComplete(1000L) && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
            int best = -1;
            float swordDamage = 0.0F;

            for(int i = 9; i < 45; ++i) {
               if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                  ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                  if (is.getItem() instanceof ItemSword) {
                     float swordD = this.getSharpnessLevel(is);
                     if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = i;
                     }
                  }
               }
            }

            ItemStack current = mc.thePlayer.inventoryContainer.getSlot(((Double)this.slot.getValueState()).intValue() + 35).getStack();
            if (best != -1 && (current == null || !(current.getItem() instanceof ItemSword) || swordDamage > this.getSharpnessLevel(current))) {
               publicItemTimer.reset();
               mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, best, ((Double)this.slot.getValueState()).intValue() - 1, 2, mc.thePlayer);
               this.time.reset();
            }

         }
      }
   }

   public boolean isBestWeapon(ItemStack stack) {
      float damage = this.getDamage(stack);

      for(int i = 9; i < 36; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (this.getDamage(is) > damage && is.getItem() instanceof ItemSword) {
               return false;
            }
         }
      }

      return stack.getItem() instanceof ItemSword;
   }

   private float getDamage(ItemStack stack) {
      float damage = 0.0F;
      Item item = stack.getItem();
      if (item instanceof ItemSword) {
         ItemSword sword = (ItemSword)item;
         damage += sword.getDamageVsEntity();
      }

      damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
      return damage;
   }

   public void getBestWeapon(int slot) {
      for(int i = 9; i < 36; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (this.isBestWeapon(is) && this.getDamage(is) > 0.0F && is.getItem() instanceof ItemSword) {
               this.swap(i, slot - 36);
               break;
            }
         }
      }

   }

   protected void swap(int slot, int hotbarNum) {
      mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
   }

   private float getSharpnessLevel(ItemStack stack) {
      float damage = ((ItemSword)stack.getItem()).getDamageVsEntity();
      damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F;
      damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
      return damage;
   }
}
