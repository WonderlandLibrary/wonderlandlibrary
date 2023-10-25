package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Verus2 extends ModuleMode<Fly> {
  @Name("UP")
  public boolean jump;
  
  private boolean blink;
  
  private long time;
  
  public Verus2(String name, Fly parent) {
    super(name, (Module)parent);
    this.jump = true;
    this.blink = false;
    this.time = 0L;
  }
  
  public void onDisable() {
    this.blink = false;
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  public void onEnable() {
    verusDamage();
    this.blink = true;
    this.time = 0L;
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {}
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    this.mc.thePlayer.sendMessage("Ticks: " + this.time);
    this.time++;
    EntityPlayerSP player = event.getPlayer();
    if (this.time == 8L && this.jump)
      player.motionY = 1.0D; 
    event.getPlayer().setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
    event.motionY = this.mc.thePlayer.motionY = 0.0D;
    event.setMovementSpeed(1.0D);
    if (this.time == 8L) {
      this.blink = false;
      this.mc.timer.timerSpeed = 0.6F;
    } 
    if (this.time == 12L) {
      this.blink = true;
      this.mc.timer.timerSpeed = 1.0F;
      this.time = 0L;
      this.mc.thePlayer.sendMessage("Reset Time!");
    } 
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive)
      event.setCancelled(true); 
    if (this.blink && 
      event.getPacket() instanceof C03PacketPlayer)
      event.setCancelled(true); 
  }
  
  public void verusDamage() {
    this.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.5D, this.mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem((World)this.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0F, 0.94F, 0.0F));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 3.05D, this.mc.thePlayer.posZ, false));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688697815D, this.mc.thePlayer.posZ, true));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\Verus2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */