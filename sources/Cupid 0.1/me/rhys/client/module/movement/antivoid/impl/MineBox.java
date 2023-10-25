package me.rhys.client.module.movement.antivoid.impl;

import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.Timer;
import me.rhys.base.util.entity.Location;
import me.rhys.client.module.movement.antivoid.AntiVoid;
import me.rhys.client.module.movement.fly.Fly;
import me.rhys.client.module.movement.highjump.HighJump;
import net.minecraft.entity.Entity;

public class MineBox extends ModuleMode<AntiVoid> {
  private Location lastGroundLocation;
  
  Timer timer = new Timer();
  
  public MineBox(String name, AntiVoid parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  public void onMove(PlayerMoveEvent event) {
    if (Lite.MODULE_FACTORY.findByClass(Fly.class).getData().isEnabled() || Lite.MODULE_FACTORY
      .findByClass(HighJump.class).getData().isEnabled()) {
      this.timer.reset();
      return;
    } 
    boolean valid = true;
    for (int i = 0; i < 6; i++) {
      if (((AntiVoid)this.parent).isBlockUnder(i))
        valid = false; 
    } 
    if (this.mc.thePlayer.fallDistance > 2.0F && this.timer.hasReached(500.0D) && valid) {
      this.timer.reset();
      ((AntiVoid)this.parent).lastCatch = System.currentTimeMillis();
      this.mc.thePlayer.setPositionAndUpdate(this.lastGroundLocation.getX(), 
          Math.round(this.lastGroundLocation.getY()), this.lastGroundLocation.getZ());
    } else if (!valid && this.mc.thePlayer.onGround && this.mc.thePlayer.ticksExisted % 3 == 0) {
      this.lastGroundLocation = new Location((Entity)this.mc.thePlayer);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\antivoid\impl\MineBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */