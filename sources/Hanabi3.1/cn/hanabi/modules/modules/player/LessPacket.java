package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventPacket;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;

public class LessPacket extends Mod {
   public LessPacket() {
      super("LessPacket", Category.PLAYER);
   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (e.getPacket() instanceof C03PacketPlayer) {
         C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
         if (!packet.isMoving() && !packet.getRotating()) {
         }
      }

   }
}
