package me.rhys.client.module.player.pingspoof;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.player.pingspoof.mode.Delay;

public class PingSpoof extends Module {
  public PingSpoof(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((ModuleMode)new Delay("Delay", this));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\pingspoof\PingSpoof.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */