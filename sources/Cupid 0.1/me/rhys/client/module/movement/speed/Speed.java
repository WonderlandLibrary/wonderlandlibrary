package me.rhys.client.module.movement.speed;

import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.movement.speed.mode.BHop;
import me.rhys.client.module.movement.speed.mode.Ground;
import me.rhys.client.module.movement.speed.mode.MotionY;
import me.rhys.client.module.movement.speed.mode.NCP;
import me.rhys.client.module.movement.speed.mode.Teleport;
import me.rhys.client.module.movement.speed.mode.Verus;
import me.rhys.client.module.movement.speed.mode.YPort;

public class Speed extends Module {
  public Speed(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new BHop("BHop", this), (ModuleMode)new NCP("NCP", this), (ModuleMode)new Verus("Verus", this), (ModuleMode)new Teleport("Teleport", this), (ModuleMode)new Ground("Ground", this), (ModuleMode)new MotionY("MotionY", this), (ModuleMode)new YPort("YPort", this) });
  }
  
  public void pausePlayerSpeed(PlayerMoveEvent event) {
    event.motionX = event.motionZ = this.mc.thePlayer.motionZ = this.mc.thePlayer.motionX = 0.0D;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\speed\Speed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */