package me.rhys.client.module.movement;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import net.minecraft.client.entity.EntityPlayerSP;

public class AutoClip extends Module {
  public AutoClip(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    EntityPlayerSP player = this.mc.thePlayer;
    if (event.getPacket() instanceof net.minecraft.network.play.server.S01PacketJoinGame || event.getPacket() instanceof net.minecraft.network.play.server.S07PacketRespawn)
      player.setPositionAndUpdate(player.posX, player.posY + -4.0D, player.posZ); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\AutoClip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */