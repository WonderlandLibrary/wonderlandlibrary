package cn.hanabi.modules.modules.combat;

import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.InvUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;

public class AutoHead extends Mod {
   public static Value slot = new Value("AutoHead", "Slot", 7.0, 1.0, 9.0, 1.0);
   public static Value delay = new Value("AutoHead", "Delay", 0.0, 0.0, 2.0, 0.05);
   public static Value health = new Value("AutoHead", "Health", 14.0, 1.0, 20.0, 0.5);
   public static Value healMode = new Value("AutoHead", "HealMode", 0);
   TimeHelper timer = new TimeHelper();

   public AutoHead() {
      super("AutoHead", Category.COMBAT);
      healMode.LoadValue(new String[]{"Regen", "Absorption"});
   }

   @EventTarget
   public void onMotion(EventPreMotion event) {
      if (this.timer.isDelayComplete((Double)delay.getValue() * 1000.0)) {
         if (!((double)mc.thePlayer.getHealth() >= (Double)health.getValue())) {
            if (!healMode.isCurrentMode("Regen") || !mc.thePlayer.isPotionActive(Potion.regeneration)) {
               if (!healMode.isCurrentMode("Absorption") || mc.thePlayer.getAbsorptionAmount() == 0.0F) {
                  InventoryPlayer inventory = mc.thePlayer.inventory;
                  int slot = this.getHeadFromInventory();
                  if (slot != -1) {
                     int tempSlot = inventory.currentItem;
                     PlayerUtil.debug(slot);
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot - 36));
                     mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                     this.timer.reset();
                  }
               }
            }
         }
      }
   }

   private int getHeadFromInventory() {
      int i;
      ItemStack stack;
      for(i = 36; i < 45; ++i) {
         stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (!InvUtils.isItemEmpty(stack.getItem()) && Item.getIdFromItem(stack.getItem()) == 397) {
            return i;
         }
      }

      for(i = 9; i < 36; ++i) {
         stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (!InvUtils.isItemEmpty(stack.getItem()) && Item.getIdFromItem(stack.getItem()) == 397) {
            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, ((Double)slot.getValue()).intValue(), 2, mc.thePlayer);
         }
      }

      return -1;
   }
}
