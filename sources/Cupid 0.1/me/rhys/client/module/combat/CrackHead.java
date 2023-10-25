package me.rhys.client.module.combat;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;

public class CrackHead extends Module {
  @Name("Visual NoJump")
  public boolean ffkdkfd = true;
  
  public CrackHead(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C02PacketUseEntity && 
      this.mc.thePlayer.onGround) {
      this.mc.thePlayer.jump();
      if (this.ffkdkfd)
        this.mc.thePlayer.cameraYaw = 1.0F; 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\CrackHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */