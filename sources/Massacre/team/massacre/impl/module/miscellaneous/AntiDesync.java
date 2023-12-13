package team.massacre.impl.module.miscellaneous;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.impl.event.EventPacket;

public class AntiDesync extends Module {
   public AntiDesync() {
      super("Anti Desync", 0, Category.MISCELLANEOUS);
   }

   @Handler
   public void onEvent(EventPacket eventReceivePacket) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null && eventReceivePacket.getPacket() instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)eventReceivePacket.getPacket();
         packet.setYaw(this.mc.thePlayer.rotationYaw);
         packet.setPitch(this.mc.thePlayer.rotationPitch);
      }

   }
}
