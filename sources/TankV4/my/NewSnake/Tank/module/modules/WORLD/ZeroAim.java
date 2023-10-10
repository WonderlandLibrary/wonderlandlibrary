package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.PacketReceiveEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@Module.Mod(
   displayName = "Zero Rotations"
)
public class ZeroAim extends Module {
   @EventTarget
   public void onPacketReceive(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook var2 = (S08PacketPlayerPosLook)var1.getPacket();
         Minecraft var10000 = mc;
         if (Minecraft.thePlayer == null) {
            var10000 = mc;
            if (Minecraft.theWorld == null) {
               var10000 = mc;
               if (Minecraft.thePlayer.rotationYaw == -180.0F) {
                  var10000 = mc;
                  if (Minecraft.thePlayer.rotationPitch == 0.0F) {
                     return;
                  }
               }
            }
         }

         var10000 = mc;
         S08PacketPlayerPosLook.yaw = Minecraft.thePlayer.rotationYaw;
         var10000 = mc;
         S08PacketPlayerPosLook.pitch = Minecraft.thePlayer.rotationPitch;
      }

   }
}
