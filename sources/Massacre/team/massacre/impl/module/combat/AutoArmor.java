package team.massacre.impl.module.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
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

public class AutoArmor extends Module {
   private TimerUtil timer = new TimerUtil();
   public final DoubleProperty delay;
   private boolean openInventory;
   public final Property<Boolean> inventoryOnly;

   public AutoArmor() {
      super("AutoArmor", 0, Category.COMBAT);
      this.delay = new DoubleProperty("Delay", 150.0D, 10.0D, 750.0D, 10.0D, Representation.MILLISECONDS);
      this.inventoryOnly = new Property("Inventory Only", true);
      this.addValues(new Property[]{this.inventoryOnly, this.delay});
   }

   public void onEnable() {
      super.onEnable();
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
      long delay = ((Double)this.delay.getValue()).longValue();
      if (!(Boolean)this.inventoryOnly.getValue() || this.mc.currentScreen instanceof GuiInventory || !this.mc.thePlayer.isMoving()) {
         if ((this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory || this.mc.currentScreen instanceof GuiChat) && this.timer.hasHit((float)delay)) {
            this.getBestArmor();
         }

      }
   }

   public void getBestArmor() {
      for(int type = 1; type < 5; ++type) {
         if (this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
            ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
            if (isBestArmor(is, type)) {
               continue;
            }

            this.drop(4 + type);
         }

         for(int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (isBestArmor(is, type) && getProtection(is) > 0.0F) {
                  this.shiftClick(i);
                  if (((Double)this.delay.getValue()).longValue() > 0L) {
                     return;
                  }
               }
            }
         }
      }

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

   public void shiftClick(int slot) {
      this.open();
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, this.mc.thePlayer);
      this.close();
   }

   public void drop(int slot) {
      this.open();
      this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, this.mc.thePlayer);
      this.close();
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
         prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.baneOfArthropods.effectId, stack) / 100.0D);
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
