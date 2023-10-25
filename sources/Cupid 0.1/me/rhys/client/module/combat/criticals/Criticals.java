package me.rhys.client.module.combat.criticals;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.combat.criticals.modes.Packet;
import me.rhys.client.module.combat.criticals.modes.Silent;

public class Criticals extends Module {
  public Packet packet;
  
  public Criticals(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)(this.packet = new Packet("Packet", this)), (ModuleMode)new Silent("Silent", this) });
  }
  
  public void processCriticalHit() {
    switch (getCurrentMode().getName()) {
      case "Packet":
        this.packet.doCriticalHit();
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\criticals\Criticals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */