package me.rhys.client.module.ghost;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;

public class Reach extends Module {
  @Name("Reach")
  @Clamp(min = 0.0D, max = 6.0D)
  public static double reach = 3.0D;
  
  public static boolean enabled = false;
  
  public Reach(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  public void onEnable() {
    enabled = true;
    super.onDisable();
  }
  
  public void onDisable() {
    enabled = false;
    super.onDisable();
  }
  
  @EventTarget
  public void packetReceive(PacketEvent event) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\Reach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */