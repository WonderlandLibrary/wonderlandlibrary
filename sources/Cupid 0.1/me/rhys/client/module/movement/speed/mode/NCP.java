package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.speed.Speed;
import net.minecraft.potion.Potion;

public class NCP extends ModuleMode<Speed> {
  private double lastDistance;
  
  private double movementSpeed;
  
  private int stage;
  
  public NCP(String name, Speed parent) {
    super(name, (Module)parent);
  }
  
  public void onEnable() {
    this.stage = 0;
    this.lastDistance = 0.0D;
    this.movementSpeed = this.mc.thePlayer.getMovementSpeed();
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    double x = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
    double z = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
    this.lastDistance = Math.sqrt(x * x + z * z);
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    if (!this.mc.thePlayer.isPlayerMoving())
      return; 
    boolean hasSpeed = (this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null);
    if (this.mc.thePlayer.onGround || this.stage == 0) {
      this.stage = 0;
      event.motionY = this.mc.thePlayer.motionY = 0.41999998688697815D;
      this.movementSpeed = this.mc.thePlayer.getMovementSpeed() * (hasSpeed ? 2.1F : 2.2F);
    } else if (this.stage == 1) {
      event.motionY = this.mc.thePlayer.motionY - 0.004999999888241291D;
      this.movementSpeed = this.lastDistance - 0.66D * (this.lastDistance - this.mc.thePlayer.getMovementSpeed());
    } else {
      event.motionY = this.mc.thePlayer.motionY - 0.008200000040233135D;
      this.movementSpeed = this.lastDistance - this.mc.thePlayer.getMovementSpeed() / 33.1D;
    } 
    event.setMovementSpeed(Math.max(this.movementSpeed, this.mc.thePlayer.getMovementSpeed()));
    this.stage++;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\speed\mode\NCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */