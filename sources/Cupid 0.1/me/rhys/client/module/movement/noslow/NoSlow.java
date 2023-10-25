package me.rhys.client.module.movement.noslow;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.movement.noslow.modes.AAC;
import me.rhys.client.module.movement.noslow.modes.NCP;
import me.rhys.client.module.movement.noslow.modes.Vanilla;

public class NoSlow extends Module {
  public NoSlow(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new Vanilla("Vanilla", this), (ModuleMode)new AAC("AAC", this), (ModuleMode)new NCP("NCP", this) });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\noslow\NoSlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */