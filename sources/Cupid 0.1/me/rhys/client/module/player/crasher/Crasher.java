package me.rhys.client.module.player.crasher;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.player.crasher.impl.Interact;
import me.rhys.client.module.player.crasher.impl.LargePosition;
import me.rhys.client.module.player.crasher.impl.MultiVerse;

public class Crasher extends Module {
  public Crasher(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new MultiVerse("MultiVerse", this), (ModuleMode)new Interact("Interact", this), (ModuleMode)new LargePosition("LargePositon", this) });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\crasher\Crasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */