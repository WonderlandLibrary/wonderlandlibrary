package today.flux.utility;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class RaycastUtils {

    public static EntityLivingBase rayTrace(float yaw, float pitch, float range) {
        Entity entity = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getGameProfile());

        float wasYaw = Minecraft.getMinecraft().getRenderViewEntity().rotationYaw;
        float wasPitch = Minecraft.getMinecraft().getRenderViewEntity().rotationPitch;

        Minecraft.getMinecraft().getRenderViewEntity().rotationYaw = yaw;
        Minecraft.getMinecraft().getRenderViewEntity().rotationPitch = pitch;

        entity.copyDataFromOld(Minecraft.getMinecraft().getRenderViewEntity());
        entity.copyLocationAndAnglesFrom(Minecraft.getMinecraft().getRenderViewEntity());

        Minecraft.getMinecraft().getRenderViewEntity().rotationPitch = wasPitch;
        Minecraft.getMinecraft().getRenderViewEntity().rotationYaw = wasYaw;

        return entityRaytrace(entity, 1.0f, range);
    }

    private static EntityLivingBase entityRaytrace(Entity entity, float partialTicks, double blockReachDistance) {
        Entity pointedEntity = null;
        Vec3 eyePos = entity.getPositionEyes(partialTicks);
        Vec3 look = entity.getLook(partialTicks);
        Vec3 vec32 = eyePos.addVector(look.xCoord * blockReachDistance, look.yCoord * blockReachDistance, look.zCoord * blockReachDistance);
        float f = 1.0f;
        List list = Minecraft.getMinecraft().theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(look.xCoord * blockReachDistance, look.yCoord * blockReachDistance, look.zCoord * blockReachDistance).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
			public boolean apply(Entity p_apply_1_) {
				return p_apply_1_.canBeCollidedWith();
			}
		}));
        double d2 = blockReachDistance;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1 == Minecraft.getMinecraft().thePlayer)
                continue;

            float f1 = entity1.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(eyePos, vec32);

            if (axisalignedbb.isVecInside(eyePos)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    d2 = 0.0D;
                }
            } else if (movingobjectposition != null) {
                double d3 = eyePos.distanceTo(movingobjectposition.hitVec);

                if (d3 < d2 || d2 == 0.0D) {
                    if (entity1 == entity.ridingEntity) {
                        if (d2 == 0.0D) {
                            pointedEntity = entity1;
                        }
                    } else {
                        pointedEntity = entity1;
                        d2 = d3;
                    }
                }
            }
        }

        return (EntityLivingBase) pointedEntity;
    }
}
