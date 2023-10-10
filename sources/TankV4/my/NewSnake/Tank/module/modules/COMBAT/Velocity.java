package my.NewSnake.Tank.module.modules.COMBAT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.PacketReceiveEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

@Module.Mod
public class Velocity extends Module {
   @Option.Op(
      min = 0.0D,
      max = 200.0D,
      increment = 5.0D
   )
   private double percent = 0.0D;

   @EventTarget
   private void onPacketReceive(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof S12PacketEntityVelocity) {
         S12PacketEntityVelocity var2 = (S12PacketEntityVelocity)var1.getPacket();
         if (ClientUtils.world().getEntityByID(var2.getEntityID()) == ClientUtils.player()) {
            if (this.percent > 0.0D) {
               var2.motionX *= (int)(this.percent / 100.0D);
               var2.motionY *= (int)(this.percent / 100.0D);
               var2.motionZ *= (int)(this.percent / 100.0D);
            } else {
               var1.setCancelled(true);
            }
         }
      } else if (var1.getPacket() instanceof S27PacketExplosion) {
         S27PacketExplosion var6;
         S27PacketExplosion var3 = var6 = (S27PacketExplosion)var1.getPacket();
         var6.field_149152_f *= (float)(this.percent / 100.0D);
         var3.field_149153_g *= (float)(this.percent / 100.0D);
         var3.field_149159_h *= (float)(this.percent / 100.0D);
      }

   }
}
