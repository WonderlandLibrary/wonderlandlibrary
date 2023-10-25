package me.rhys.client.module.combat.criticals.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.combat.criticals.Criticals;

public class Silent extends ModuleMode<Criticals> {
  @Name("Visual NoJump")
  public boolean ffkdkfd;
  
  public Silent(String name, Criticals parent) {
    super(name, (Module)parent);
    this.ffkdkfd = true;
  }
  
  @EventTarget
  public void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C02PacketUseEntity && 
      this.mc.thePlayer.onGround && !this.mc.thePlayer.isAirBorne)
      this.mc.thePlayer.jump(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\criticals\modes\Silent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */