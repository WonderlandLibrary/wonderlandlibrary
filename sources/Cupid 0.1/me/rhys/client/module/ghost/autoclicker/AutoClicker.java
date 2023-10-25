package me.rhys.client.module.ghost.autoclicker;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.ghost.autoclicker.modes.Blatent;
import me.rhys.client.module.ghost.autoclicker.modes.Bypass;
import me.rhys.client.module.ghost.autoclicker.modes.Normal;

public class AutoClicker extends Module {
  public AutoClicker(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new Normal("Normal", this), (ModuleMode)new Bypass("Bypass", this), (ModuleMode)new Blatent("Blatent", this) });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\autoclicker\AutoClicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */