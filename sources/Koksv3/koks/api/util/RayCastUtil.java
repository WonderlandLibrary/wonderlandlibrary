package koks.api.util;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import optifine.Reflector;

import java.util.List;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 20:18
 */

public class RayCastUtil {

    private final Minecraft mc = Minecraft.getMinecraft();
    private Entity pointedEntity;

    public Entity rayCastedEntity(double range, float yaw, float pitch) {
        Entity entity = this.mc.getRenderViewEntity();

        if (entity != null && this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            this.mc.objectMouseOver = rayTrace(entity, yaw, pitch, (float) range);
            Vec3 vec3 = entity.getPositionEyes(1F);
            boolean flag = false;
            boolean flag1 = true;

            Vec3 vec31 = entity.getLook(1F);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
            this.pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0F;
            List list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (range >= 0.0D) {
                        this.pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        range = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < range || range == 0.0D) {
                        boolean flag2 = false;

                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag2 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }

                        if (entity1 == entity.ridingEntity && !flag2) {
                            if (range == 0.0D) {
                                this.pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        } else {
                            this.pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            range = d3;
                        }
                    }
                }
            }
            if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > range) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing) null, new BlockPos(vec33));
            }
        }
        return pointedEntity;
    }

    public MovingObjectPosition rayTrace(Entity entity, float yaw, float pitch, float reach) {
        Vec3 vec3 = entity.getPositionEyes(1F);
        Vec3 vec31 = getLook(yaw, pitch);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * reach, vec31.yCoord * reach, vec31.zCoord * reach);
        return mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public Vec3 getLook(float yaw, float pitch) {
        return Entity.getVectorForRotation(pitch, yaw);
    }

    public boolean isRayCastBlock(BlockPos bp, MovingObjectPosition ray) {
        return ray != null && ray.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && bp.equals(ray.getBlockPos());
    }

    public MovingObjectPosition rayCastedBlock(float yaw, float pitch) {
        float range = mc.playerController.getBlockReachDistance();

        Vec3 vec31 = getLook(yaw, pitch);

        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);


        MovingObjectPosition ray = mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, false);

        if (ray != null && ray.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            return ray;
        return null;
    }
}