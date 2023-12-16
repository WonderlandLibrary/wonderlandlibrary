package wtf.expensive.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import wtf.expensive.util.IMinecraft;

import java.util.Optional;
import java.util.function.Predicate;

public class RayTraceUtil implements IMinecraft {


    /**
     * Майнкрафт метод :/
     * Получает сущность на которую направлена ротация
     *
     * @param target   цель, с которой осуществляется взаимодействие
     * @param yaw      горизонтальный угол обзора
     * @param pitch    вертикальный угол обзора
     * @param distance максимальное расстояние
     * @return сущность, на которую направлена ротация, или null, если такой сущности нет
     */
    public static Entity getMouseOver(Entity target,
                                      float yaw,
                                      float pitch,
                                      double distance) {
        RayTraceResult objectMouseOver;
        Entity entity = mc.getRenderViewEntity();

        if (entity != null && mc.world != null) {
            objectMouseOver = null;
            boolean flag = distance > 3;

            Vector3d startVec = entity.getEyePosition(1);
            Vector3d directionVec = getVectorForRotation(pitch, yaw);
            Vector3d endVec = startVec.add(
                    directionVec.x * distance,
                    directionVec.y * distance,
                    directionVec.z * distance
            );

            AxisAlignedBB axisalignedbb = target.getBoundingBox().grow(target.getCollisionBorderSize());

            EntityRayTraceResult entityraytraceresult = rayTraceEntities(entity,
                    startVec,
                    endVec,
                    axisalignedbb,
                    (p_lambda$getMouseOver$0_0_) ->
                            !p_lambda$getMouseOver$0_0_.isSpectator()
                                    && p_lambda$getMouseOver$0_0_.canBeCollidedWith(), distance
            );

            if (entityraytraceresult != null) {
                if (flag && startVec.distanceTo(startVec) > distance) {
                    objectMouseOver = BlockRayTraceResult.createMiss(startVec, null, new BlockPos(startVec));
                }
                if ((distance < distance || objectMouseOver == null)) {
                    objectMouseOver = entityraytraceresult;
                }
            }
            if (objectMouseOver == null) {
                return null;
            }
            try {
                return ((EntityRayTraceResult) objectMouseOver).getEntity();
            } catch (ClassCastException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Тоже майнкрафт метод :/
     * Выполняет трассировку луча между сущностями
     *
     * @param shooter     сущность, инициировавшая трассировку
     * @param startVec    начальная точка трассировки
     * @param endVec      конечная точка трассировки
     * @param boundingBox ограничивающий параллелепипед для поиска сущностей
     * @param filter      предикат для фильтрации сущностей
     * @param distance    максимальное расстояние трассировки
     * @return результат трассировки луча между сущностями или null, если сущностей не найдено
     */
    public static EntityRayTraceResult rayTraceEntities(Entity shooter,
                                                        Vector3d startVec,
                                                        Vector3d endVec,
                                                        AxisAlignedBB boundingBox,
                                                        Predicate<Entity> filter,
                                                        double distance) {
        World world = shooter.world;
        double closestDistance = distance;
        Entity entity = null;
        Vector3d closestHitVec = null;

        for (Entity entity1 : world.getEntitiesInAABBexcluding(shooter, boundingBox, filter)) {
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double) entity1.getCollisionBorderSize());
            Optional<Vector3d> optional = axisalignedbb.rayTrace(startVec, endVec);

            if (axisalignedbb.contains(startVec)) {
                if (closestDistance >= 0.0D) {
                    entity = entity1;
                    closestHitVec = startVec;
                    closestDistance = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vector3d vector3d1 = optional.get();
                double d3 = startVec.distanceTo(optional.get());

                if (d3 < closestDistance || closestDistance == 0.0D) {
                    boolean flag1 = false;

                    if (!flag1 && entity1.getLowestRidingEntity() == shooter.getLowestRidingEntity()) {
                        if (closestDistance == 0.0D) {
                            entity = entity1;
                            closestHitVec = vector3d1;
                        }
                    } else {
                        entity = entity1;
                        closestHitVec = vector3d1;
                        closestDistance = d3;
                    }
                }
            }
        }

        return entity == null ? null : new EntityRayTraceResult(entity, closestHitVec);
    }

    /**
     * Выполняет трассировку луча и возвращает результат.
     *
     * @param rayTraceDistance дальность трассировки луча
     * @param yaw              горизонтальный угол обзора
     * @param pitch            вертикальный угол обзора
     * @param entity           сущность, для которой выполняется трассировка луча
     * @return результат трассировки луча в игровом мире
     */
    public static RayTraceResult rayTrace(double rayTraceDistance,
                                          float yaw,
                                          float pitch,
                                          Entity entity) {
        Vector3d startVec = mc.player.getEyePosition(1.0F);
        Vector3d directionVec = getVectorForRotation(pitch, yaw);
        Vector3d endVec = startVec.add(
                directionVec.x * rayTraceDistance,
                directionVec.y * rayTraceDistance,
                directionVec.z * rayTraceDistance
        );

        return mc.world.rayTraceBlocks(new RayTraceContext(
                startVec,
                endVec,
                RayTraceContext.BlockMode.OUTLINE,
                RayTraceContext.FluidMode.NONE,
                entity)
        );
    }


    public static RayTraceResult rayTraceResult(double rayTraceDistance,
                                                float yaw,
                                                float pitch,
                                                Entity entity) {

        RayTraceResult object = null;

        if (entity != null && mc.world != null) {
            float partialTicks = mc.getRenderPartialTicks();
            double distance = rayTraceDistance;
            object = rayTrace(rayTraceDistance, yaw, pitch, entity);
            Vector3d vector3d = entity.getEyePosition(partialTicks);
            boolean flag = false;
            double d1 = distance;

            if (mc.playerController.extendedReach()) {
                d1 = 6.0D;
                distance = d1;
            } else {
                if (distance > 3.0D) {
                    flag = true;
                }

                distance = distance;
            }

            d1 = d1 * d1;

            if (object != null) {
                d1 = object.getHitVec().squareDistanceTo(vector3d);
            }

            Vector3d vector3d1 = getVectorForRotation(pitch, yaw);
            Vector3d vector3d2 = vector3d.add(vector3d1.x * distance, vector3d1.y * distance, vector3d1.z * distance);
            float f = 1.0F;
            AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand(vector3d1.scale(distance)).grow(1.0D, 1.0D, 1.0D);
            EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(entity, vector3d, vector3d2, axisalignedbb, (p_lambda$getMouseOver$0_0_) ->
            {
                return !p_lambda$getMouseOver$0_0_.isSpectator() && p_lambda$getMouseOver$0_0_.canBeCollidedWith();
            }, d1);

            if (entityraytraceresult != null) {
                Entity entity1 = entityraytraceresult.getEntity();
                Vector3d vector3d3 = entityraytraceresult.getHitVec();
                double d2 = vector3d.squareDistanceTo(vector3d3);

                if (flag && d2 > 9.0D) {
                    object = BlockRayTraceResult.createMiss(vector3d3, Direction.getFacingFromVector(vector3d1.x, vector3d1.y, vector3d1.z), new BlockPos(vector3d3));
                } else if (d2 < d1 || object == null) {
                    object = entityraytraceresult;
                }
            }
        }
        return object;
    }


    public static boolean rayTraceWithBlock(double rayTraceDistance,
                                            float yaw,
                                            float pitch,
                                            Entity entity, Entity target) {

        RayTraceResult object = null;

        if (entity != null && mc.world != null) {
            float partialTicks = mc.getRenderPartialTicks();
            double distance = rayTraceDistance;
            object = rayTrace(rayTraceDistance, yaw, pitch, entity);
            Vector3d vector3d = entity.getEyePosition(partialTicks);
            boolean flag = false;
            double d1 = distance;

            if (mc.playerController.extendedReach()) {
                d1 = 6.0D;
                distance = d1;
            } else {
                if (distance > 3.0D) {
                    flag = true;
                }

                distance = distance;
            }

            d1 = d1 * d1;

            if (object != null) {
                d1 = object.getHitVec().squareDistanceTo(vector3d);
            }

            Vector3d vector3d1 = getVectorForRotation(pitch, yaw);
            Vector3d vector3d2 = vector3d.add(vector3d1.x * distance, vector3d1.y * distance, vector3d1.z * distance);
            float f = 1.0F;
            AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand(vector3d1.scale(distance)).grow(1.0D, 1.0D, 1.0D);
            EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(entity, vector3d, vector3d2, axisalignedbb, (p_lambda$getMouseOver$0_0_) ->
            {
                return !p_lambda$getMouseOver$0_0_.isSpectator() && p_lambda$getMouseOver$0_0_.canBeCollidedWith();
            }, d1);

            if (entityraytraceresult != null) {
                Entity entity1 = entityraytraceresult.getEntity();
                Vector3d vector3d3 = entityraytraceresult.getHitVec();
                double d2 = vector3d.squareDistanceTo(vector3d3);

                if (flag && d2 > 9.0D) {
                    object = BlockRayTraceResult.createMiss(vector3d3, Direction.getFacingFromVector(vector3d1.x, vector3d1.y, vector3d1.z), new BlockPos(vector3d3));
                } else if (d2 < d1 || object == null) {
                    object = entityraytraceresult;
                }
            }
        }
        if (object instanceof EntityRayTraceResult) {
            if (((EntityRayTraceResult) object).getEntity().getEntityId() == target.getEntityId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает вектор направления в зависимости от заданных углов поворота.
     *
     * @param pitch вертикальный угол поворота
     * @param yaw   горизонтальный угол поворота
     * @return вектор направления
     */
    public static Vector3d getVectorForRotation(float pitch, float yaw) {
        float yawRadians = -yaw * ((float) Math.PI / 180) - (float) Math.PI;
        float pitchRadians = -pitch * ((float) Math.PI / 180);

        float cosYaw = MathHelper.cos(yawRadians);
        float sinYaw = MathHelper.sin(yawRadians);
        float cosPitch = -MathHelper.cos(pitchRadians);
        float sinPitch = MathHelper.sin(pitchRadians);

        return new Vector3d(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch);
    }

}
