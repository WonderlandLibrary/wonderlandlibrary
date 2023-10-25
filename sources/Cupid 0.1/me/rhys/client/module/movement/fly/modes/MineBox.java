package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.client.entity.EntityPlayerSP;

public class MineBox extends ModuleMode<Fly> {
  @Name("Speed")
  @Clamp(min = 2.0D, max = 10.0D)
  public double speed = 6.0D;
  
  @Name("MotionY")
  public boolean upanddownbitch = true;
  
  private boolean blink;
  
  public MineBox(String name, Fly parent) {
    super(name, (Module)parent);
    this.blink = false;
  }
  
  public void onEnable() {
    this.blink = true;
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
    this.blink = false;
  }
  
  @EventTarget
  void onMotion(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction)
      event.setCancelled(true); 
    if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive)
      event.setCancelled(true); 
    if (this.blink && 
      event.getPacket() instanceof net.minecraft.network.play.client.C03PacketPlayer)
      event.setCancelled(true); 
  }
  
  @EventTarget
  void playerMove(PlayerMoveEvent event) {
    EntityPlayerSP player = event.getPlayer();
    if (player == null)
      return; 
    this.blink = true;
    event.getPlayer().setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
    this.mc.timer.timerSpeed = 0.2F;
    if (this.upanddownbitch)
      event.motionY = player.motionY = this.mc.gameSettings.keyBindJump.pressed ? 0.44999998807907104D : (this.mc.gameSettings.keyBindSneak.pressed ? -0.20000000298023224D : 0.0D); 
    if (this.mc.thePlayer.isPlayerMoving()) {
      event.setMovementSpeed(this.speed);
    } else {
      event.motionZ = event.motionX = 0.0D;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\MineBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */