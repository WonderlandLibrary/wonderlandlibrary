package team.massacre.impl.module.miscellaneous;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.PacketUtil;

public class FastUse extends Module {
   public EnumProperty<FastUse.GoFasterEatie> mode;

   public FastUse() {
      super("Fast Use", 0, Category.MISCELLANEOUS);
      this.mode = new EnumProperty("Mode", FastUse.GoFasterEatie.Vanilla);
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      this.setSuffix(((FastUse.GoFasterEatie)this.mode.getValue()).name());
      switch((FastUse.GoFasterEatie)this.mode.getValue()) {
      case Vanilla:
         if (this.isEating()) {
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer());
         }
         break;
      case Redeky:
         if (this.isEating()) {
            for(int i = 0; i < 20; ++i) {
               this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
         }
      }

   }

   private boolean isEating() {
      return this.mc.thePlayer.getHeldItem() != null && (this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || this.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion) && this.mc.thePlayer.isEating();
   }

   public static enum GoFasterEatie {
      Vanilla,
      Redeky;
   }
}
