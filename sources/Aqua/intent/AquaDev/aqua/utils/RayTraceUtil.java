package intent.AquaDev.aqua.utils;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

public class RayTraceUtil {
    public static MovingObjectPosition rayCast(float partialTicks, float[] rots) {
        Minecraft mc = Minecraft.getMinecraft();
        MovingObjectPosition objectMouseOver = null;
        Entity entity = mc.getRenderViewEntity();
        if (entity != null && mc.theWorld != null) {
            mc.mcProfiler.startSection("pick");
            mc.pointedEntity = null;
            double d0 = mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.customRayTrace(d0, partialTicks, rots[0], rots[1]);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            boolean flag1 = true;
            if (mc.playerController.extendedReach()) {
                d0 = 6.0;
                d1 = 6.0;
            } else if (d0 > 3.0) {
                flag = true;
            }
            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            Vec3 vec31 = entity.getCustomLook(partialTicks, rots[0], rots[1]);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0f;
            List list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, (Predicate)new /* Unavailable Anonymous Inner Class!! */));
            double d2 = d1;
            Object realBB = null;
            for (int i = 0; i < list.size(); ++i) {
                double d3;
                Entity entity1 = (Entity)list.get(i);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (!(d2 >= 0.0)) continue;
                    pointedEntity = entity1;
                    vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                    d2 = 0.0;
                    continue;
                }
                if (movingobjectposition == null || !((d3 = vec3.distanceTo(movingobjectposition.hitVec)) < d2) && d2 != 0.0) continue;
                boolean flag2 = false;
                if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                    flag2 = Reflector.callBoolean((Object)entity1, (ReflectorMethod)Reflector.ForgeEntity_canRiderInteract, (Object[])new Object[0]);
                }
                if (entity1 == entity.ridingEntity && !flag2) {
                    if (d2 != 0.0) continue;
                    pointedEntity = entity1;
                    vec33 = movingobjectposition.hitVec;
                    continue;
                }
                pointedEntity = entity1;
                vec33 = movingobjectposition.hitVec;
                d2 = d3;
            }
            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }
            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    // empty if block
                }
            }
        }
        return objectMouseOver;
    }

    static final class rofl implements Predicate<Entity> {

        public boolean apply(Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }

        @Override
        public boolean test(Entity entity) {
            return false;
        }
    }
}
