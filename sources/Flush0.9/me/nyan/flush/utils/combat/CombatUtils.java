package me.nyan.flush.utils.combat;

import com.google.common.base.Predicates;
import me.nyan.flush.utils.other.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;

import java.util.List;

public class CombatUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Entity rayCast(double range, float yaw, float pitch) {
        Vec3 eyes = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
        Vec3 vec = getVectorForRotation(pitch, yaw);
        Vec3 vec3 = eyes.addVector(vec.xCoord * range, vec.yCoord * range, vec.zCoord * range);
        Entity pointedEntity = null;
        List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(mc.getRenderViewEntity(),
                mc.getRenderViewEntity().getEntityBoundingBox().addCoord(vec.xCoord * range, vec.yCoord * range, vec.zCoord * range
                ).expand(1, 1, 1), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));

        for (Entity entity : list) {
            float borderSize = entity.getCollisionBorderSize();
            AxisAlignedBB boundingBox = entity.getEntityBoundingBox().expand(borderSize, borderSize, borderSize);
            MovingObjectPosition movingObjectPosition = boundingBox.calculateIntercept(eyes, vec3);

            if (boundingBox.isVecInside(eyes)) {
                if (range >= 0.0D) {
                    pointedEntity = entity;
                    range = 0.0D;
                }
            } else if (movingObjectPosition != null) {
                double distance = eyes.distanceTo(movingObjectPosition.hitVec);
                if (distance < range || range == 0.0D) {
                    if (entity == mc.getRenderViewEntity().ridingEntity) {
                        if (range == 0.0D)
                            pointedEntity = entity;
                    } else {
                        pointedEntity = entity;
                        range = distance;
                    }
                }
            }
        }

        return pointedEntity;
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float x = MathHelper.sin(-yaw * (MathHelper.PI / 180F) - MathHelper.PI);
        float z = MathHelper.cos(-yaw * (MathHelper.PI / 180F) - MathHelper.PI);
        float y = MathHelper.sin(-pitch * (MathHelper.PI / 180F));
        float f = -MathHelper.cos(-pitch * (MathHelper.PI / 180F));
        return new Vec3(x * f, y, z * f);
    }

    public static float[] getRotationsLys(EntityLivingBase entity) {
        double distanceToEntityX = entity.posX + (entity.posX - entity.lastTickPosX) - mc.thePlayer.posX;
        double distanceToEntityY = entity.posY + entity.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double distanceToEntityZ = entity.posZ + (entity.posZ - entity.lastTickPosZ) - mc.thePlayer.posZ;
        double pythagorean = Math.sqrt(distanceToEntityX * distanceToEntityX + distanceToEntityZ * distanceToEntityZ);
        float yaw = (float) Math.toDegrees(Math.acos(distanceToEntityZ / pythagorean));
        if (distanceToEntityX > 0) {
            yaw = -yaw;
        }
        float pitch = (float) Math.toDegrees(Math.acos(pythagorean / (Math.sqrt(distanceToEntityY * distanceToEntityY + pythagorean * pythagorean))));
        if (distanceToEntityY > 0) {
            pitch = -pitch;
        }
        if (yaw < -180 || yaw > 180 || pitch > 90 || pitch < -90) {
            return new float[]{0, 0};
        }
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(double posX, double posY, double posZ) {
        double x = posX - mc.thePlayer.posX;
        double y = posY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = posZ - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180f / MathHelper.PI) - 90f;
        float pitch = (float) (-(Math.atan2(y, dist) * 180f / MathHelper.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Vec3 vec3) {
        return getRotations(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public static float[] getRotations(EntityLivingBase entity, boolean randomize) {
        double x = entity.posX;
        double y = entity.posY + entity.getEyeHeight() - 0.4;
        double z = entity.posZ;
        if (randomize) {
            x += MathUtils.getRandomNumber(0.03, -0.03);
            z += MathUtils.getRandomNumber(0.03, -0.03);
            y += MathUtils.getRandomNumber(0.07, -0.07);
        }
        return getRotations(x, y, z);
    }

    public static float getFacingDifference(float a, float b) {
        return MathHelper.wrapAngleTo180_float(a - b);
    }
}