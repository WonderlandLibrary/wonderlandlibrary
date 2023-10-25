package me.rhys.client.module.movement.highjump;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.highjump.modes.HighHop;
import me.rhys.client.module.movement.highjump.modes.Verus;

public class HighJump extends Module {
  @Name("View Bobbing")
  public boolean viewBobbing = true;
  
  public HighJump(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new Verus("Verus", this), (ModuleMode)new HighHop("HighHop", this) });
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    if (this.viewBobbing)
      this.mc.thePlayer.cameraYaw = 0.099999376F; 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\highjump\HighJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */