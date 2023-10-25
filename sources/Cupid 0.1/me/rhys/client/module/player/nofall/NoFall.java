package me.rhys.client.module.player.nofall;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.player.nofall.modes.NoGround;
import me.rhys.client.module.player.nofall.modes.Normal;
import me.rhys.client.module.player.nofall.modes.Verus;

public class NoFall extends Module {
  public NoFall(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new Normal("Normal", this), (ModuleMode)new Verus("Verus", this), (ModuleMode)new NoGround("NoGround", this) });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\nofall\NoFall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */