package cn.hanabi.modules.modules.combat;

import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoHeal extends Mod {
   public boolean potting;
   private int slot;
   private int last;
   public Value healthValue = new Value("AutoHeal", "Health Pot", true);
   public Value speedValue = new Value("AutoHeal", "Speed Pot", true);
   public Value potHealthValue = new Value("AutoHeal", "Pot Health", 15.0, 3.0, 20.0, 1.0);
   private final TimeHelper timer = new TimeHelper();

   public AutoHeal() {
      super("AutoHeal", Category.COMBAT);
   }

   public void onEnable() {
      super.onEnable();
      this.slot = -1;
      this.last = -1;
      this.timer.reset();
      this.potting = false;
   }

   public void onDisable() {
      super.onDisable();
      this.potting = false;
   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (e.getPacket() instanceof S08PacketPlayerPosLook) {
         this.timer.reset();
      }

   }

   @EventTarget(1)
   public void onUpdate(EventPreMotion event) {
      this.slot = this.getSlot();
      if (this.timer.isDelayComplete(1000L) && KillAura.target == null) {
         int regenId = Potion.regeneration.getId();
         int speedId;
         if (!mc.thePlayer.isPotionActive(regenId) && !this.potting && mc.thePlayer.onGround && (Boolean)this.healthValue.getValue() && (double)mc.thePlayer.getHealth() <= (Double)this.potHealthValue.getValue() && this.hasPot(regenId)) {
            speedId = this.hasPot(regenId, this.slot);
            if (speedId != -1) {
               this.swap(speedId, this.slot);
            }

            this.potting = true;
            this.timer.reset();
         }

         speedId = Potion.moveSpeed.getId();
         if (!mc.thePlayer.isPotionActive(speedId) && !this.potting && mc.thePlayer.onGround && (Boolean)this.speedValue.getValue() && this.hasPot(speedId)) {
            int cum = this.hasPot(speedId, this.slot);
            if (cum != -1) {
               this.swap(cum, this.slot);
            }

            this.timer.reset();
            this.potting = true;
         }

         if (this.potting) {
            this.last = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = this.slot;
            mc.thePlayer.motionY = 0.051011199999;
            event.setPitch(90.0F);
         }
      }

   }

   @EventTarget
   public void onMove(EventMove event) {
   }

   @EventTarget
   public void onPost(EventPostMotion event) {
      if (this.potting) {
         if (mc.thePlayer.onGround && mc.thePlayer.inventory.getCurrentItem() != null && mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())) {
            mc.entityRenderer.itemRenderer.resetEquippedProgress2();
         }

         if (this.last != -1) {
            mc.thePlayer.inventory.currentItem = this.last;
         }

         this.potting = false;
         this.last = -1;
      }

   }

   private int hasPot(int id, int targetSlot) {
      for(int i = 9; i < 45; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is.getItem() instanceof ItemPotion) {
               ItemPotion pot = (ItemPotion)is.getItem();
               if (!pot.getEffects(is).isEmpty()) {
                  PotionEffect effect = (PotionEffect)pot.getEffects(is).get(0);
                  if (effect.getPotionID() == id && ItemPotion.isSplash(is.getItemDamage()) && this.isBestPot(pot, is) && 36 + targetSlot != i) {
                     return i;
                  }
               }
            }
         }
      }

      return -1;
   }

   private boolean hasPot(int id) {
      for(int i = 9; i < 45; ++i) {
         if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is.getItem() instanceof ItemPotion) {
               ItemPotion pot = (ItemPotion)is.getItem();
               if (!pot.getEffects(is).isEmpty()) {
                  PotionEffect effect = (PotionEffect)pot.getEffects(is).get(0);
                  if (effect.getPotionID() == id && ItemPotion.isSplash(is.getItemDamage()) && this.isBestPot(pot, is)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean isBestPot(ItemPotion potion, ItemStack stack) {
      if (potion.getEffects(stack) != null && potion.getEffects(stack).size() == 1) {
         PotionEffect effect = (PotionEffect)potion.getEffects(stack).get(0);
         int potionID = effect.getPotionID();
         int amplifier = effect.getAmplifier();
         int duration = effect.getDuration();

         for(int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
               ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (is.getItem() instanceof ItemPotion) {
                  ItemPotion pot = (ItemPotion)is.getItem();
                  if (pot.getEffects(is) != null) {
                     Iterator var10 = pot.getEffects(is).iterator();

                     while(var10.hasNext()) {
                        PotionEffect o = (PotionEffect)var10.next();
                        int id = o.getPotionID();
                        int ampl = o.getAmplifier();
                        int dur = o.getDuration();
                        if (id == potionID && ItemPotion.isSplash(is.getItemDamage())) {
                           if (ampl > amplifier) {
                              return false;
                           }

                           if (ampl == amplifier && dur > duration) {
                              return false;
                           }
                        }
                     }
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private int getSlot() {
      int spoofSlot = 8;

      for(int i = 36; i < 45; ++i) {
         if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            spoofSlot = i - 36;
            break;
         }

         if (mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion) {
            spoofSlot = i - 36;
            break;
         }
      }

      return spoofSlot;
   }

   private void swap(int slot1, int hotbarSlot) {
      mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
   }

   public boolean isPotting() {
      return this.potting;
   }
}
