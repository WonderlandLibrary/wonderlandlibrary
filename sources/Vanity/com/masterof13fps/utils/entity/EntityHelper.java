package com.masterof13fps.utils.entity;

import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public final class EntityHelper
{
  private static Minecraft mc = null;
  
  
  public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing)
  {
    Entity temp = new EntitySnowball(mc.theWorld);
    temp.posX = (x + 0.5D);
    temp.posY = (y + 0.5D);
    temp.posZ = (z + 0.5D);
    temp.posX += facing.getDirectionVec().getX() * 0.25D;
    temp.posY += facing.getDirectionVec().getY() * 0.25D;
    temp.posZ += facing.getDirectionVec().getZ() * 0.25D;
    return getAngles(temp);
  }
  
  public static float[] getAngles(Entity e)
  {
    return new float[] { getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch };
  }
  

  public static float getYawChangeToEntity(Entity entity)
  {
    double deltaX = entity.posX - mc.thePlayer.posX;
    double deltaZ = entity.posZ - mc.thePlayer.posZ;
    double yawToEntity = 0;
    if ((deltaZ < 0.0D) && (deltaX < 0.0D))
    {
      yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
    }
    else
    {
      double yawToEntity1;
      if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
        yawToEntity1 = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else {
        yawToEntity1 = Math.toDegrees(-Math.atan(deltaX / deltaZ));
      }
    }
    return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)yawToEntity));
  }
  
  public static float getPitchChangeToEntity(Entity entity)
  {
    double deltaX = entity.posX - mc.thePlayer.posX;
    double deltaZ = entity.posZ - mc.thePlayer.posZ;
    double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - mc.thePlayer.posY;
    double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
    
    double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
    
    return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)pitchToEntity);
  }
  
}

