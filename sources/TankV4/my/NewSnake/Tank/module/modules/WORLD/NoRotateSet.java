package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.PacketSendEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@Module.Mod
public class NoRotateSet extends Module {
   @EventTarget
   public void onEvent(PacketSendEvent var1) {
      if (var1.getPacket() instanceof S08PacketPlayerPosLook) {
         Minecraft var10000 = mc;
         S08PacketPlayerPosLook.pitch = Minecraft.thePlayer.rotationPitch;
         var10000 = mc;
         S08PacketPlayerPosLook.yaw = Minecraft.thePlayer.rotationYaw;
      }

   }
}
