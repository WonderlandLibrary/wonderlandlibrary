package me.rhys.client.module.movement.fly;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.modes.BlocksMC;
import me.rhys.client.module.movement.fly.modes.MineBox;
import me.rhys.client.module.movement.fly.modes.MineMenClub;
import me.rhys.client.module.movement.fly.modes.Motion;
import me.rhys.client.module.movement.fly.modes.Teleport;
import me.rhys.client.module.movement.fly.modes.Verus;
import me.rhys.client.module.movement.fly.modes.Verus2;
import me.rhys.client.module.movement.fly.modes.VerusDamage;
import me.rhys.client.module.movement.fly.modes.VerusHeavy;
import me.rhys.client.module.movement.fly.modes.ZoneCraft;

public class Fly extends Module {
  @Name("View Bobbing")
  public boolean viewBobbing = true;
  
  public Fly(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new Motion("Motion", this), (ModuleMode)new MineBox("MineBox", this), (ModuleMode)new Teleport("Teleport", this), (ModuleMode)new Verus2("Verus2", this), (ModuleMode)new VerusDamage("VerusDamage", this), (ModuleMode)new ZoneCraft("ZoneCraft", this), (ModuleMode)new BlocksMC("BlocksMC", this), (ModuleMode)new Verus("Verus", this), (ModuleMode)new MineMenClub("MineMen*", this), (ModuleMode)new VerusHeavy("VerusHeavy", this) });
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    if (this.viewBobbing)
      this.mc.thePlayer.cameraYaw = 0.099999376F; 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\Fly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */