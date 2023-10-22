package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventTick;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.InvUtils;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Mod {
   public static boolean isDone = false;
   private final TimeHelper timer = new TimeHelper();
   private final TimeHelper glitchFixer = new TimeHelper();
   private final Value openInv = new Value("AutoArmor", "Sort In Inv", false);
   private final Value noMove = new Value("AutoArmor", "No Move", false);
   private final Value delay = new Value("AutoArmor", "Delay", 150.0, 0.0, 1000.0, 10.0);

   public AutoArmor() {
      super("AutoArmor", Category.PLAYER);
   }

   @EventTarget
   public void onTick(EventTick event) {
      if (!(Boolean)this.noMove.getValue() || !MoveUtils.isMoving()) {
         if ((Boolean)this.openInv.getValueState()) {
            if (!(mc.currentScreen instanceof GuiInventory)) {
               return;
            }
         } else {
            if (mc.currentScreen != null) {
               this.glitchFixer.reset();
            }

            if (!this.glitchFixer.isDelayComplete(300L)) {
               return;
            }
         }

         if (this.timer.isDelayComplete((Double)this.delay.getValue())) {
            if (!mc.thePlayer.capabilities.isCreativeMode && (mc.currentScreen == null || (Boolean)this.openInv.getValueState())) {
               ArmorType[] var3 = ArmorType.values();
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  ArmorType armorType = var3[var5];
                  int slot;
                  if ((slot = this.findArmor(armorType, InvUtils.getArmorScore(mc.thePlayer.inventory.armorItemInSlot(armorType.ordinal())))) != -1) {
                     isDone = false;
                     if (mc.thePlayer.inventory.armorItemInSlot(armorType.ordinal()) != null) {
                        this.dropArmor(armorType.ordinal());
                        this.timer.reset();
                        return;
                     }

                     this.warmArmor(slot);
                     this.timer.reset();
                     return;
                  }

                  isDone = true;
               }

            } else {
               this.timer.reset();
            }
         }
      }
   }

   private int findArmor(ArmorType armorType, float minimum) {
      float best = 0.0F;
      int result = -1;

      for(int i = 0; i < mc.thePlayer.inventory.mainInventory.length; ++i) {
         ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
         if (!(InvUtils.getArmorScore(itemStack) < 0.0F) && !(InvUtils.getArmorScore(itemStack) <= minimum) && !(InvUtils.getArmorScore(itemStack) < best) && this.isValid(armorType, itemStack)) {
            best = InvUtils.getArmorScore(itemStack);
            result = i;
         }
      }

      return result;
   }

   private boolean isValid(ArmorType type, ItemStack itemStack) {
      if (!(itemStack.getItem() instanceof ItemArmor)) {
         return false;
      } else {
         ItemArmor armor = (ItemArmor)itemStack.getItem();
         if (type == ArmorType.HELMET && armor.armorType == 0) {
            return true;
         } else if (type == ArmorType.CHEST_PLATE && armor.armorType == 1) {
            return true;
         } else if (type == ArmorType.LEGGINGS && armor.armorType == 2) {
            return true;
         } else {
            return type == ArmorType.BOOTS && armor.armorType == 3;
         }
      }
   }

   private void warmArmor(int slot_In) {
      if (slot_In >= 0 && slot_In <= 8) {
         this.clickSlot(slot_In + 36, 0, true);
      } else {
         this.clickSlot(slot_In, 0, true);
      }

   }

   private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
      mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, mc.thePlayer);
   }

   private void dropArmor(int armorSlot) {
      int slot = InvUtils.armorSlotToNormalSlot(armorSlot);
      if (!InvUtils.isFull()) {
         this.clickSlot(slot, 0, true);
      } else {
         mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
      }

   }
}
