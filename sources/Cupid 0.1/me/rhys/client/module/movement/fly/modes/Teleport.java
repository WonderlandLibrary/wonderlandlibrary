package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Teleport extends ModuleMode<Fly> {
  @Name("MineBox")
  public boolean minebox;
  
  @Name("Speed")
  @Clamp(min = 1.0D, max = 5.0D)
  public float speed;
  
  public Teleport(String name, Fly parent) {
    super(name, (Module)parent);
    this.minebox = false;
    this.speed = 1.0F;
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  @EventTarget
  public void onMoveEvent(PlayerMotionEvent event) {
    this.mc.thePlayer.motionY = 0.0D;
    EntityPlayerSP player = event.getPlayer();
    event.getPlayer().setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
  }
  
  @EventTarget
  void onMotion(PlayerMoveEvent event) {
    event.motionY = 0.0D;
    event.setMovementSpeed(this.speed);
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction)
      this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition()); 
    if (this.minebox && 
      event.getPacket() instanceof C03PacketPlayer) {
      event.setCancelled(true);
      this.mc.timer.timerSpeed = 0.6F;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\Teleport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */