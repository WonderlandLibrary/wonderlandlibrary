package cn.hanabi.utils;

import cn.hanabi.utils.rotation.Rotation;
import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public final class RaycastUtils {
   private static final Minecraft mc = Minecraft.getMinecraft();

   public static Entity raycastEntity(double range, Rotation rotation, IEntityFilter entityFilter) {
      return raycastEntity(range, rotation.getYaw(), rotation.getPitch(), entityFilter);
   }

   private static Entity raycastEntity(double range, float yaw, float pitch, IEntityFilter entityFilter) {
      Entity renderViewEntity = mc.getRenderViewEntity();
      if (renderViewEntity != null && mc.theWorld != null) {
         double blockReachDistance = range;
         Vec3 eyePosition = renderViewEntity.getPositionEyes(1.0F);
         float yawCos = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
         float yawSin = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
         float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
         float pitchSin = MathHelper.sin(-pitch * 0.017453292F);
         Vec3 entityLook = new Vec3((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
         Vec3 vector = eyePosition.addVector(entityLook.xCoord * range, entityLook.yCoord * range, entityLook.zCoord * range);
         List entityList = mc.theWorld.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.xCoord * range, entityLook.yCoord * range, entityLook.zCoord * range).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
         Entity pointedEntity = null;
         Iterator var17 = entityList.iterator();

         while(true) {
            while(true) {
               Entity entity;
               do {
                  if (!var17.hasNext()) {
                     return pointedEntity;
                  }

                  entity = (Entity)var17.next();
               } while(entityFilter.canRaycast(entity));

               float collisionBorderSize = entity.getCollisionBorderSize();
               AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
               MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);
               if (axisAlignedBB.isVecInside(eyePosition)) {
                  if (blockReachDistance >= 0.0) {
                     pointedEntity = entity;
                     blockReachDistance = 0.0;
                  }
               } else if (movingObjectPosition != null) {
                  double eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec);
                  if (eyeDistance < blockReachDistance || blockReachDistance == 0.0) {
                     if (entity == renderViewEntity.ridingEntity && !renderViewEntity.canRiderInteract()) {
                        if (blockReachDistance == 0.0) {
                           pointedEntity = entity;
                        }
                     } else {
                        pointedEntity = entity;
                        blockReachDistance = eyeDistance;
                     }
                  }
               }
            }
         }
      } else {
         return null;
      }
   }

   public interface IEntityFilter {
      boolean canRaycast(Entity var1);
   }
}
