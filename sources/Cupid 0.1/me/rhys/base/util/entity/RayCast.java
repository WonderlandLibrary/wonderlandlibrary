package me.rhys.base.util.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import optifine.Reflector;

public class RayCast {
  private static Minecraft mc = Minecraft.getMinecraft();
  
  public static EntityLivingBase rayCast(Entity target, float yaw, float pitch) {
    if (mc.theWorld != null && mc.thePlayer != null) {
      Vec3 position = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
      Vec3 lookVector = mc.thePlayer.getVectorForRotation(pitch, yaw);
      double reachDistance = mc.playerController.getBlockReachDistance();
      Entity pointedEntity = null;
      for (Entity currentEntity : mc.theWorld.getEntitiesWithinAABBExcludingEntity((Entity)mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVector.xCoord * mc.playerController.getBlockReachDistance(), lookVector.yCoord * mc.playerController.getBlockReachDistance(), lookVector.zCoord * mc.playerController.getBlockReachDistance()).expand(1.0D, 1.0D, 1.0D))) {
        if (currentEntity.canBeCollidedWith() && !currentEntity.isEntityEqual(target)) {
          MovingObjectPosition objPosition = currentEntity.getEntityBoundingBox().expand(currentEntity.getCollisionBorderSize(), currentEntity.getCollisionBorderSize(), currentEntity.getCollisionBorderSize()).contract(0.1D, 0.1D, 0.1D).calculateIntercept(position, position.addVector(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance));
          if (objPosition != null) {
            double distance = position.distanceTo(objPosition.hitVec);
            if (distance < reachDistance) {
              if (currentEntity == mc.thePlayer.ridingEntity && (!Reflector.ForgeEntity_canRiderInteract.exists() || !Reflector.callBoolean(currentEntity, Reflector.ForgeEntity_canRiderInteract, new Object[0])) && reachDistance == 0.0D) {
                pointedEntity = currentEntity;
                continue;
              } 
              pointedEntity = currentEntity;
              reachDistance = distance;
            } 
          } 
        } 
      } 
      if (pointedEntity != null && !pointedEntity.isEntityEqual(target) && pointedEntity instanceof EntityLivingBase)
        return (EntityLivingBase)pointedEntity; 
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\entity\RayCast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */