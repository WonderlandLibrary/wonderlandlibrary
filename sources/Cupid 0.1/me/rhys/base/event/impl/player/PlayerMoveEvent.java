package me.rhys.base.event.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class PlayerMoveEvent extends PlayerEvent {
  public double motionX;
  
  public double motionY;
  
  public double motionZ;
  
  public PlayerMoveEvent(EntityPlayerSP player, double motionX, double motionY, double motionZ) {
    super(player);
    this.motionX = motionX;
    this.motionY = motionY;
    this.motionZ = motionZ;
  }
  
  public void setMovementSpeed(double movementSpeed) {
    this
      .motionX = (float)-(Math.sin((Minecraft.getMinecraft()).thePlayer.getDirection()) * Math.max(movementSpeed, (Minecraft.getMinecraft()).thePlayer.getMovementSpeed()));
    this
      .motionZ = (float)(Math.cos((Minecraft.getMinecraft()).thePlayer.getDirection()) * Math.max(movementSpeed, (Minecraft.getMinecraft()).thePlayer.getMovementSpeed()));
  }
  
  public void subtract(double x, double y, double z) {
    this.motionX -= x;
    this.motionY -= y;
    this.motionZ -= z;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\PlayerMoveEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */