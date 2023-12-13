package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AutoRod extends Mod {
   public Value delay = new Value("AutoRod", "Delay", 100.0, 50.0, 1000.0, 50.0);
   public Value hotkey = new Value("AutoRod", "Auto Disable", false);
   private final TimeHelper time = new TimeHelper();
   private final TimeHelper time2 = new TimeHelper();
   private Boolean switchBack = false;
   private Boolean useRod = false;

   public AutoRod() {
      super("AutoRod", Category.GHOST);
   }

   public static int bestWeapon(Entity target) {
      Minecraft mc = Minecraft.getMinecraft();
      int firstSlot = mc.thePlayer.inventory.currentItem = 0;
      int bestWeapon = -1;
      int j = 1;

      for(byte i = 0; i < 9; ++i) {
         mc.thePlayer.inventory.currentItem = i;
         ItemStack itemStack = mc.thePlayer.getHeldItem();
         if (itemStack != null) {
            int itemAtkDamage = (int)getItemAtkDamage(itemStack);
            itemAtkDamage = (int)((float)itemAtkDamage + EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED));
            if (itemAtkDamage > j) {
               j = itemAtkDamage;
               bestWeapon = i;
            }
         }
      }

      if (bestWeapon != -1) {
         return bestWeapon;
      } else {
         return firstSlot;
      }
   }

   public static float getItemAtkDamage(ItemStack itemStack) {
      Multimap multimap = itemStack.getAttributeModifiers();
      if (!multimap.isEmpty()) {
         Iterator iterator = multimap.entries().iterator();
         if (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            if (attributeModifier.getAmount() > 1.0) {
               return 1.0F + (float)damage;
            }

            return 1.0F;
         }
      }

      return 1.0F;
   }

   @EventTarget
   private void onUpdate(EventPreMotion event) {
      int item = Item.getIdFromItem(mc.thePlayer.getHeldItem().getItem());
      float rodDelay = ((Double)this.delay.getValue()).floatValue();
      if (mc.currentScreen == null) {
         if (!(Boolean)this.hotkey.getValue()) {
            if (!this.useRod && item == 346) {
               this.Rod();
               this.useRod = true;
            }

            if (this.time.isDelayComplete(rodDelay - 50.0F) && this.switchBack) {
               this.switchBack();
               this.switchBack = false;
            }

            if (this.time.isDelayComplete(rodDelay) && this.useRod) {
               this.useRod = false;
            }
         } else if (item == 346) {
            if (this.time2.isDelayComplete(rodDelay + 200.0F)) {
               this.Rod();
               this.time2.reset();
            }

            if (this.time.isDelayComplete(rodDelay)) {
               mc.thePlayer.inventory.currentItem = bestWeapon(mc.thePlayer);
               this.time.reset();
               this.toggleModule();
            }
         } else if (this.time.isDelayComplete(100L)) {
            this.switchToRod();
            this.time.reset();
         }

      }
   }

   private int findRod(int startSlot, int endSlot) {
      for(int i = startSlot; i < endSlot; ++i) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && stack.getItem() == Items.fishing_rod) {
            return i;
         }
      }

      return -1;
   }

   private void switchToRod() {
      for(int i = 36; i < 45; ++i) {
         ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (itemstack != null && Item.getIdFromItem(itemstack.getItem()) == 346) {
            mc.thePlayer.inventory.currentItem = i - 36;
            break;
         }
      }

   }

   private void Rod() {
      int rod = this.findRod(36, 45);
      mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventoryContainer.getSlot(rod).getStack());
      this.switchBack = true;
      this.time.reset();
   }

   private void switchBack() {
      mc.thePlayer.inventory.currentItem = bestWeapon(mc.thePlayer);
   }
}
