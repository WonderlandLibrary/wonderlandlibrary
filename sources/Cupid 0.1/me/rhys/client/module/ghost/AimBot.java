package me.rhys.client.module.ghost;

import java.util.List;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class AimBot extends Module {
  @Name("Distance")
  @Clamp(min = 0.0D, max = 10.0D)
  public float dist;
  
  @Name("RotationPitch+")
  @Clamp(min = 1.0D, max = 15.0D)
  public float rp;
  
  @Name("RotationYaw+")
  @Clamp(min = 0.0D, max = 15.0D)
  public float ry;
  
  public AimBot(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.dist = 5.0F;
    this.rp = 1.0F;
    this.ry = 0.0F;
  }
  
  @EventTarget
  public void onTick(PlayerUpdateEvent event) {
    List<EntityPlayer> list = this.mc.theWorld.playerEntities;
    for (int k = 0; k < list.size(); k++) {
      if (((EntityPlayer)list.get(k)).getName() != this.mc.thePlayer.getName()) {
        EntityPlayer entityplayer = list.get(1);
        if (this.mc.thePlayer.getDistanceToEntity((Entity)entityplayer) > this.mc.thePlayer.getDistanceToEntity((Entity)list.get(k)))
          entityplayer = list.get(k); 
        float f = this.mc.thePlayer.getDistanceToEntity((Entity)entityplayer);
        if (f < this.dist && this.mc.thePlayer.canEntityBeSeen((Entity)entityplayer))
          faceEntity((EntityLivingBase)entityplayer); 
      } 
    } 
  }
  
  public synchronized void faceEntity(EntityLivingBase entity) {
    float[] rotations = getRotationsNeeded((Entity)entity);
    if (rotations != null) {
      (Minecraft.getMinecraft()).thePlayer.rotationYaw = rotations[0] + this.ry;
      (Minecraft.getMinecraft()).thePlayer.rotationPitch = rotations[1] + this.rp;
    } 
  }
  
  public static float[] getRotationsNeeded(Entity entity) {
    double diffY;
    if (entity == null)
      return null; 
    double diffX = entity.posX - (Minecraft.getMinecraft()).thePlayer.posX;
    double diffZ = entity.posZ - (Minecraft.getMinecraft()).thePlayer.posZ;
    if (entity instanceof EntityLivingBase) {
      EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
      diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Minecraft.getMinecraft()).thePlayer.posY + (Minecraft.getMinecraft()).thePlayer.getEyeHeight();
    } else {
      diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (Minecraft.getMinecraft()).thePlayer.posY + (Minecraft.getMinecraft()).thePlayer.getEyeHeight();
    } 
    double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
    float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
    float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
    return new float[] { (Minecraft.getMinecraft()).thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - (Minecraft.getMinecraft()).thePlayer.rotationYaw), (Minecraft.getMinecraft()).thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - (Minecraft.getMinecraft()).thePlayer.rotationPitch) };
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\AimBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */