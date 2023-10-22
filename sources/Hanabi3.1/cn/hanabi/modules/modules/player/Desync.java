package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.injection.interfaces.IS08PacketPlayerPosLook;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.TimeHelper;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Desync extends Mod {
   private final TimeHelper timer = new TimeHelper();

   public Desync() {
      super("Desync", Category.PLAYER);
   }

   @EventTarget
   public void onPre(EventPreMotion e) {
   }

   @EventTarget
   public void onUpdate(EventPacket event) {
      if (event.getPacket() instanceof S08PacketPlayerPosLook) {
         if (!this.timer.isDelayComplete(5000L)) {
            return;
         }

         if (event.packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.packet;
            ((IS08PacketPlayerPosLook)packet).setYaw(mc.thePlayer.rotationYaw);
            ((IS08PacketPlayerPosLook)packet).setPitch(mc.thePlayer.rotationPitch);
         }
      }

   }

   @EventTarget
   public void onRespawn(EventWorldChange event) {
      this.timer.reset();
   }
}
