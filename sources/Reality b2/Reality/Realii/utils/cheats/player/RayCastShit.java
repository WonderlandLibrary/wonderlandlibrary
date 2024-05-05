package Reality.Realii.utils.cheats.player;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import Reality.Realii.utils.cheats.player.Helper;


public class RayCastShit {
	
	public static boolean isMouseOverEntity(float yaw, float pitch, Entity target, double d) {
	    Minecraft mc = Minecraft.getMinecraft();
	    Entity renderEntity = mc.getRenderViewEntity();
	    float partialTicks = mc.timer.renderPartialTicks;
	    MovingObjectPosition rayTrace = getMouseOver(partialTicks, renderEntity, d, yaw, pitch);
	    return rayTrace != null && rayTrace.entityHit != null && rayTrace.entityHit == target;
	}

	public  static boolean isMouseOverBlock(float yaw, float pitch, BlockPos pos, double range) {
	    Minecraft mc = Minecraft.getMinecraft();
	    Entity renderEntity = mc.getRenderViewEntity();
	    float partialTicks = mc.timer.renderPartialTicks;
	    MovingObjectPosition rayTrace = getMouseOver(partialTicks, renderEntity, range, yaw, pitch);
	    return rayTrace != null && rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && rayTrace.getBlockPos().equals(pos);
	}

	private static MovingObjectPosition getMouseOver(float partialTicks, Entity entity, double range, float yaw, float pitch) {
	    Minecraft mc = Minecraft.getMinecraft();
	    double blockReachDistance = mc.playerController.getBlockReachDistance();
	    Vec3 eyePos = entity.getPositionEyes(partialTicks);
	    Vec3 lookVec = entity.getLook(partialTicks);
	    Vec3 direction = eyePos.addVector(lookVec.xCoord * blockReachDistance, lookVec.yCoord * blockReachDistance, lookVec.zCoord * blockReachDistance);
	    MovingObjectPosition rayTrace = mc.theWorld.rayTraceBlocks(eyePos, direction, false, true, true);
	    double maxDist = blockReachDistance;
	    if (rayTrace != null) {
	        maxDist = rayTrace.hitVec.distanceTo(eyePos);
	    }
	    List<Entity> entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().addCoord(lookVec.xCoord * blockReachDistance, lookVec.yCoord * blockReachDistance, lookVec.zCoord * blockReachDistance).expand(1.0D, 1.0D, 1.0D));
	    for (Entity e : entities) {
	        if (e.canBeCollidedWith()) {
	            float collisionSize = e.getCollisionBorderSize();
	            AxisAlignedBB entityBoundingBox = e.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
	            MovingObjectPosition entityTrace = entityBoundingBox.calculateIntercept(eyePos, direction);
	            if (entityBoundingBox.isVecInside(eyePos)) {
	                if (0.0D < maxDist || maxDist == 0.0D) {
	                    maxDist = 0.0D;
	                    rayTrace = new MovingObjectPosition(e);
	                }
	            } else if (entityTrace != null) {
	                double dist = eyePos.distanceTo(entityTrace.hitVec);
	                if (dist < maxDist || maxDist == 0.0D) {
	                    maxDist = dist;
	                    rayTrace = new MovingObjectPosition(e);
	                }
	            }
	        }
	    }
	    return rayTrace;
	}
	
	public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = Helper.mc.timer.renderPartialTicks;
        final Entity entity = Helper.mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;
        Entity mcPointedEntity = null;

        if (entity != null && Helper.mc.theWorld != null) {

            Helper.mc.mcProfiler.startSection("pick");
            final double d0 = Helper.mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0,yaw,pitch, partialTicks);
            double d1 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = Helper.mc.thePlayer.getVectorForRotation(pitch, yaw);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = Helper.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mcPointedEntity = pointedEntity;
                }
            }

            Helper.mc.mcProfiler.endSection();

            return mcPointedEntity == target;
        }

        return false;
    }

    public static MovingObjectPosition rayCast(float partialTicks, float[] rots) {
        MovingObjectPosition objectMouseOver = null;
        Entity entity = Helper.mc.getRenderViewEntity();
        if (entity != null && Helper.mc.theWorld != null) {
            double d0 = Helper.mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0,rots[0],rots[1], partialTicks);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;
            boolean flag1 = true;

            if (Helper.mc.playerController.extendedReach()) {
                d0 = 6.0D;
                d1 = 6.0D;
            } else {
                if (d0 > 3.0D) {
                    flag = true;
                }

                d0 = d0;
            }
            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }
            Vec3 vec31 = entity.getVectorForRotation(rots[1], rots[0]);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0F;
            List list = Helper.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, new EntityRenderer.EntityRenderer$1(Helper.mc.entityRenderer)));
            double d2 = d1;
            AxisAlignedBB realBB = null;
            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        boolean flag2 = false;



                        if (entity1 == entity.ridingEntity && !flag2) {
                            if (d2 == 0.0D) {
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        } else {
                            pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0D) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }
            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    pointedEntity = pointedEntity;
                }
            }
        }
        return objectMouseOver;
    }

    public static MovingObjectPosition isMouseOverFixed(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = Helper.mc.timer.renderPartialTicks;
        final Entity entity = Helper.mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;
        Entity mcPointedEntity = null;

        if (entity != null && Helper.mc.theWorld != null) {

            Helper.mc.mcProfiler.startSection("pick");
            final double d0 = Helper.mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0,yaw,pitch, partialTicks);
            double d1 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = Helper.mc.thePlayer.getVectorForRotation(pitch, yaw);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = Helper.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mcPointedEntity = pointedEntity;
                }
            }

            Helper.mc.mcProfiler.endSection();

            return objectMouseOver;
        }

        return null;
    }
	


}
	
	
	


