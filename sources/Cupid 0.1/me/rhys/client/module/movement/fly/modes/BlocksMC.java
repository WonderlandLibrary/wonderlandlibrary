package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class BlocksMC extends ModuleMode<Fly> {
  public BlocksMC(String name, Fly parent) {
    super(name, (Module)parent);
  }
  
  public static float FlySpeed = 0.25F;
  
  private boolean flyable;
  
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
        this.mc.thePlayer.capabilities.setFlySpeed(FlySpeed);
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
  
  public void onDisable() {
    this.mc.thePlayer.capabilities.isFlying = false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\BlocksMC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */