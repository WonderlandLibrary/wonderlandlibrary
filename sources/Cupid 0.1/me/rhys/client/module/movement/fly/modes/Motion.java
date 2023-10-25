package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.client.entity.EntityPlayerSP;

public class Motion extends ModuleMode<Fly> {
  @Name("Speed")
  @Clamp(min = 0.0D, max = 9.0D)
  public double speed = 1.0D;
  
  @Name("Ground Spoof")
  public boolean groundSpoof = false;
  
  @Name("PositionY Ground")
  public boolean positionGround = false;
  
  public Motion(String name, Fly parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    event.setOnGround(this.groundSpoof);
    if (this.positionGround)
      event.getPosition().setY(Math.round(event.getPosition().getY())); 
  }
  
  @EventTarget
  void playerMove(PlayerMoveEvent event) {
    EntityPlayerSP player = event.getPlayer();
    if (player == null)
      return; 
    event.motionY = player.motionY = this.mc.gameSettings.keyBindJump.pressed ? this.speed : (this.mc.gameSettings.keyBindSneak.pressed ? -this.speed : 0.0D);
    if (this.mc.thePlayer.isPlayerMoving()) {
      event.setMovementSpeed(this.speed);
    } else {
      event.motionZ = event.motionX = 0.0D;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\Motion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */