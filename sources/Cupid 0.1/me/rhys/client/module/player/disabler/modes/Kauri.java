package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.disabler.Disabler;

public class Kauri extends ModuleMode<Disabler> {
  @Name("Mode")
  public ModeMode webmode;
  
  public Kauri(String name, Disabler parent) {
    super(name, (Module)parent);
    this.webmode = ModeMode.OLD;
  }
  
  private enum ModeMode {
    OLD, DEV;
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    switch (this.webmode) {
      case OLD:
        if (event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction) {
          event.setCancelled(true);
          break;
        } 
      case DEV:
        if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive)
          event.setCancelled(true); 
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\Kauri.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */