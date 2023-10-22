package cn.hanabi.modules.modules.movement;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S30PacketWindowItems;

@ObfuscationClass
public class NoSlow extends Mod {
   public Value mode = (new Value("NoSlow", "Mode", 0)).LoadValue(new String[]{"Vanilla", "NCP"});
   TimeHelper ms = new TimeHelper();

   public NoSlow() {
      super("NoSlow", Category.MOVEMENT);
   }

   @EventTarget
   public void onPre(EventPreMotion e) {
      if (mc.thePlayer.isUsingItem()) {
         if (this.mode.isCurrentMode("NCP")) {
         }

      }
   }

   @EventTarget
   public void onPost(EventPostMotion e) {
      if (mc.thePlayer.isUsingItem()) {
         if (this.mode.isCurrentMode("NCP")) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
         }

      }
   }

   @EventTarget
   public void onPacket(EventPacket event) {
      if (mc.thePlayer.isUsingItem()) {
         if (event.getPacket() instanceof S30PacketWindowItems) {
            event.setCancelled(true);
            PlayerUtil.debug("Gay Sir");
         }

      }
   }
}
