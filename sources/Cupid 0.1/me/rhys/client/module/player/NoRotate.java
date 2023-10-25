package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {
  public NoRotate(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof S08PacketPlayerPosLook && 
      this.mc.thePlayer != null) {
      ((S08PacketPlayerPosLook)event.getPacket()).setPitch(this.mc.thePlayer.rotationPitch);
      ((S08PacketPlayerPosLook)event.getPacket()).setYaw(this.mc.thePlayer.rotationYaw);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\NoRotate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */