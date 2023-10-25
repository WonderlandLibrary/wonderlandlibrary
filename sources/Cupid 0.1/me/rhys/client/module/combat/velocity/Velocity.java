package me.rhys.client.module.combat.velocity;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.combat.velocity.modes.Hypixel;
import me.rhys.client.module.combat.velocity.modes.Normal;
import me.rhys.client.module.combat.velocity.modes.Verus;
import me.rhys.client.module.combat.velocity.modes.Vulcan;
import net.minecraft.network.Packet;

public class Velocity extends Module {
  @Name("Explosions")
  private boolean explosions = true;
  
  public Velocity(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new Normal("Normal", this), (ModuleMode)new Vulcan("Vulcan", this), (ModuleMode)new Hypixel("Hypixel", this), (ModuleMode)new Verus("Verus", this) });
  }
  
  @EventTarget
  public void packetReceive(PacketEvent event) {
    if (event.getDirection().equals(Event.Direction.IN)) {
      Packet<?> packet = event.getPacket();
      if (packet instanceof net.minecraft.network.play.server.S27PacketExplosion)
        event.setCancelled(this.explosions); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\velocity\Velocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */