package me.rhys.client.module.movement;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sprint extends Module {
  @Name("Omni")
  public boolean omniDir = false;
  
  @Name("No Packet")
  public boolean noPacket = false;
  
  public Sprint(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof C0BPacketEntityAction) {
      C0BPacketEntityAction c0BPacketEntityAction = (C0BPacketEntityAction)event.getPacket();
      switch (c0BPacketEntityAction.getAction()) {
        case STOP_SPRINTING:
        case START_SPRINTING:
          event.setCancelled(true);
          break;
      } 
    } 
  }
  
  @EventTarget
  void onPlayerMove(PlayerMoveEvent event) {
    this.mc.thePlayer.setSprinting(this.omniDir ? ((this.mc.thePlayer.moveForward != 0.0F || this.mc.thePlayer.moveStrafing != 0.0F)) : ((this.mc.thePlayer.moveForward > 0.0F)));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\Sprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */