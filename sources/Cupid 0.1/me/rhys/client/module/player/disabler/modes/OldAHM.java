package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.disabler.Disabler;

public class OldAHM extends ModuleMode<Disabler> {
  @Name("Web")
  public boolean web;
  
  @Name("PosLook")
  public boolean poslook;
  
  public OldAHM(String name, Disabler parent) {
    super(name, (Module)parent);
    this.web = false;
    this.poslook = false;
  }
  
  @EventTarget
  void onUpdate(PlayerUpdateEvent event) {
    if (this.web) {
      this.mc.thePlayer.setInWeb();
      this.mc.thePlayer.speedOnGround = 0.05F;
    } 
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction) {
      event.setCancelled(true);
    } else if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive) {
      event.setCancelled(true);
    } else if (this.poslook && event.getPacket() instanceof net.minecraft.network.play.server.S08PacketPlayerPosLook) {
      event.setCancelled(true);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\OldAHM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */