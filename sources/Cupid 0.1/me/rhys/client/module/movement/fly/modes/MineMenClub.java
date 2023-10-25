package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class MineMenClub extends ModuleMode<Fly> {
  public MineMenClub(String name, Fly parent) {
    super(name, (Module)parent);
  }
  
  public void onDisable() {
    this.mc.thePlayer.capabilities.isFlying = false;
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  public void onEnable() {
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 5.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0D, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
    this.mc.timer.timerSpeed = 0.5F;
    this.mc.thePlayer.motionX = 0.0D;
    this.mc.thePlayer.motionY = 0.3D;
    this.mc.thePlayer.motionZ = 0.0D;
    this.mc.thePlayer.motionY = 0.0D;
    this.mc.thePlayer.capabilities.isFlying = true;
    this.mc.thePlayer.jump();
    this.mc.thePlayer.cameraYaw = 0.3F;
    if (this.mc.gameSettings.keyBindJump.isPressed()) {
      this.mc.thePlayer.motionY += 0.3D;
      if (this.mc.gameSettings.keyBindForward.isPressed()) {
        (Minecraft.getMinecraft()).thePlayer.jump();
        this.mc.thePlayer.capabilities.setFlySpeed(2.0F);
        if (this.mc.gameSettings.keyBindSneak.isPressed())
          this.mc.thePlayer.motionY += 0.3D; 
        if (this.mc.gameSettings.keyBindRight.isPressed()) {
          this.mc.thePlayer.motionX += 0.3D;
          this.mc.thePlayer.motionY += 0.3D;
          this.mc.thePlayer.motionZ += 0.3D;
        } 
        if (this.mc.gameSettings.keyBindLeft.isPressed()) {
          this.mc.thePlayer.motionX += 0.3D;
          this.mc.thePlayer.motionY += 0.3D;
          this.mc.thePlayer.motionZ += 0.3D;
        } 
      } 
    } 
  }
  
  @EventTarget
  public void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction)
      event.setCancelled(true); 
    if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive)
      event.setCancelled(true); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\MineMenClub.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */