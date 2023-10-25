package me.rhys.client.module.render;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.Render3DEvent;
import me.rhys.base.event.impl.player.TickEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;

public class TimeChanger extends Module {
  @Name("Time")
  @Clamp(min = 1.0D, max = 22000.0D)
  public int time;
  
  public TimeChanger(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.time = 22000;
  }
  
  @EventTarget
  public void onRender(Render3DEvent event) {
    if (this.mc.theWorld == null)
      return; 
    this.mc.theWorld.setWorldTime((int)this.time);
    this.mc.theWorld.setTotalWorldTime(this.mc.theWorld.getWorldTime());
  }
  
  @EventTarget
  public void onWorldTick(TickEvent event) {
    this.mc.theWorld.setWorldTime(this.time);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\TimeChanger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */