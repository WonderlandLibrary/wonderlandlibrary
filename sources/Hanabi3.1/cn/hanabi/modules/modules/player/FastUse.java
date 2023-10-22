package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.injection.interfaces.IMinecraft;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastUse extends Mod {
   private final Value mode = new Value("FastUse", "Mode", 0);
   private boolean usedtimer = false;
   private final TimeHelper timer = new TimeHelper();

   public FastUse() {
      super("FastUse", Category.PLAYER);
      this.mode.addValue("Instant");
      this.mode.addValue("Timer");
   }

   public void onEnable() {
      this.usedtimer = false;
      this.timer.reset();
   }

   public void onDisable() {
      if (this.usedtimer) {
         ((IMinecraft)mc).getTimer().timerSpeed = 1.0F;
         this.usedtimer = false;
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
      Item usingItem;
      if (this.mode.isCurrentMode("Instant")) {
         if (mc.thePlayer.isUsingItem()) {
            usingItem = mc.thePlayer.getItemInUse().getItem();
            if ((usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) && this.timer.hasReached(750L)) {
               mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getItemInUse()));

               for(int i = 0; i < 39; ++i) {
                  mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
               }

               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
               mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               mc.playerController.onStoppedUsingItem(mc.thePlayer);
               this.timer.reset();
            }

            if (!mc.thePlayer.isUsingItem()) {
               this.timer.reset();
            }
         }
      } else if (this.mode.isCurrentMode("Hypixel")) {
         if (mc.thePlayer.isUsingItem()) {
            usingItem = mc.thePlayer.getItemInUse().getItem();
            if ((usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) && mc.thePlayer.getItemInUseDuration() >= 1) {
               mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (!mc.thePlayer.isUsingItem()) {
               this.timer.reset();
            }
         }
      } else if (this.mode.isCurrentMode("Timer")) {
         if (this.usedtimer) {
            ((IMinecraft)mc).getTimer().timerSpeed = 1.0F;
            this.usedtimer = false;
         }

         if (mc.thePlayer.isUsingItem()) {
            usingItem = mc.thePlayer.getItemInUse().getItem();
            if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) {
               ((IMinecraft)mc).getTimer().timerSpeed = 1.22F;
               this.usedtimer = true;
            }
         }
      }

   }
}
