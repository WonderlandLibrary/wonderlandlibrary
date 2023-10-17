package de.tired.util.combat;

import de.tired.util.combat.rotation.NewRotationUtil;
import de.tired.util.combat.rotation.Rotation;
import de.tired.base.interfaces.IHook;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;

import java.util.List;

public class RayCastUtil implements IHook {

    public MovingObjectPosition rayTraceBlocks(Vec3 origin, float yaw, float pitch, float range) {
        final Vec3 direction = NewRotationUtil.getVectorForRotation(new Rotation(yaw, pitch));
        return rayTraceBlocks(origin, direction, range);
    }

    public MovingObjectPosition rayTraceBlocks(Vec3 origin, Vec3 direction, float range) {
        return rayTraceBlocks(origin, origin.add(direction.normalize().scale(range)));
    }

    public MovingObjectPosition rayTraceBlocks(Vec3 from, Vec3 to) {
        return MC.theWorld.rayTraceBlocks(from, to, false);
    }

    public MovingObjectPosition rayCast(float yaw, float pitch, float reach){
        return rayCast(yaw,pitch,reach,1.0f);
    }


    public MovingObjectPosition rayCast(float yaw, float pitch, float reach, float partialTicks) {
        final Vec3 from = MC.thePlayer.getPositionEyes(partialTicks);

        MovingObjectPosition movingObjectPosition;
        Entity pointedEntity = null;

        movingObjectPosition = rayTraceBlocks(from, yaw, pitch, reach);
        double distanceToObject = reach;

        if (movingObjectPosition != null) {
            distanceToObject = movingObjectPosition.hitVec.distanceTo(from);
        }

        final Vec3 lookVector = NewRotationUtil.getVectorForRotation(new Rotation(yaw, pitch));
        final Vec3 lookRay = from.addVector(lookVector.xCoord * reach, lookVector.yCoord * reach, lookVector.zCoord * reach);
        Vec3 hitVec = null;

        final float expansion = 1.0F;
        List entities = MC.theWorld.getEntitiesInAABBexcluding(MC.thePlayer, MC.thePlayer.getEntityBoundingBox()
                        .addCoord(lookVector.xCoord * reach, lookVector.yCoord * reach, lookVector.zCoord * reach)
                        .expand(expansion, expansion, expansion),
                Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double smallestDistance = distanceToObject;

        for (Object ent : entities) {
            final Entity entity = (Entity) ent;
            final float collisionBorderSize = entity.getCollisionBorderSize();
            final AxisAlignedBB collisionBox = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
            final MovingObjectPosition movingobjectposition = collisionBox.calculateIntercept(from, lookRay);

            if (collisionBox.isVecInside(from)) {
                if (smallestDistance >= 0.0D) {
                    pointedEntity = entity;
                    hitVec = movingobjectposition == null ? from : movingobjectposition.hitVec;
                    smallestDistance = 0.0D;
                }
            } else if (movingobjectposition != null) {
                final double dist = from.distanceTo(movingobjectposition.hitVec);

                if (dist < smallestDistance || smallestDistance == 0.0D) {
                    if (entity == entity.ridingEntity) {
                        if (smallestDistance == 0.0D) {
                            pointedEntity = entity;
                            hitVec = movingobjectposition.hitVec;
                        }
                    } else {
                        pointedEntity = entity;
                        hitVec = movingobjectposition.hitVec;
                        smallestDistance = dist;
                    }
                }
            }
        }

        if (pointedEntity != null && (smallestDistance < distanceToObject || movingObjectPosition == null)) {
            movingObjectPosition = new MovingObjectPosition(pointedEntity, hitVec);
        }

        return movingObjectPosition;
    }


    public static final Entity raycastEntity(double range, float[] rotations) {
        final Entity player = MC.getRenderViewEntity();

        if (player != null && MC.theWorld != null) {
            final Vec3 eyeHeight = player.getPositionEyes(MC.timer.renderPartialTicks);

            final Vec3 looks = getVectorForRotation(rotations[0], rotations[1]);
            final Vec3 vec = eyeHeight.addVector(looks.xCoord * range, looks.yCoord * range, looks.zCoord * range);
            final List<Entity> list = MC.theWorld.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().addCoord(looks.xCoord * range, looks.yCoord * range, looks.zCoord * range).expand(1, 1, 1), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));

            Entity raycastedEntity = null;

            for (Entity entity : list) {
                if (!(entity instanceof EntityLivingBase)) continue;

                final float borderSize = entity.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(borderSize, borderSize, borderSize);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(eyeHeight, vec);

                if (axisalignedbb.isVecInside(eyeHeight)) {
                    if (range >= 0.0D) {
                        raycastedEntity = entity;
                        range = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double distance = eyeHeight.distanceTo(movingobjectposition.hitVec);

                    if (distance < range || range == 0.0D) {

                        if (entity == player.ridingEntity) {
                            if (range == 0.0D) {
                                raycastedEntity = entity;
                            }
                        } else {
                            raycastedEntity = entity;
                            range = distance;
                        }
                    }
                }
            }
            return raycastedEntity;
        }
        return null;
    }

    public static final Vec3 getVectorForRotation(float yaw, float pitch) {
        final double f = Math.cos(Math.toRadians(-yaw) - Math.PI);
        final double f1 = Math.sin(Math.toRadians(-yaw) - Math.PI);
        final double f2 = -Math.cos(Math.toRadians(-pitch));
        final double f3 = Math.sin(Math.toRadians(-pitch));
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }


}
