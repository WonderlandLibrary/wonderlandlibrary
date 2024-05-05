package space.lunaclient.luna.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class FakePlayer
  extends EntityOtherPlayerMP
{
  public FakePlayer()
  {
    super(Minecraft.theWorld, Minecraft.thePlayer.getGameProfile());
    copyLocationAndAnglesFrom(Minecraft.thePlayer);
    this.inventory.copyInventory(Minecraft.thePlayer.inventory);
    copyPlayerModel(Minecraft.thePlayer, this);
    this.rotationYawHead = Minecraft.thePlayer.rotationYawHead;
    this.renderYawOffset = Minecraft.thePlayer.renderYawOffset;
    this.lastTickPosX = this.posX;
    this.lastTickPosY = this.posY;
    this.lastTickPosZ = this.posZ;
    Minecraft.theWorld.addEntityToWorld(getEntityId(), this);
  }
  
  public static void copyPlayerModel(EntityPlayer from, EntityPlayer to)
  {
    to.getDataWatcher().updateObject(10, Byte.valueOf(from.getDataWatcher().getWatchableObjectByte(10)));
  }
  
  public void resetPlayerPosition()
  {
    Minecraft.thePlayer.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
  }
  
  public void despawn()
  {
    Minecraft.theWorld.removeEntityFromWorld(getEntityId());
  }
}
