package fun.rich.client.utils.math;

import fun.rich.client.feature.impl.combat.KillAura;
import fun.rich.client.utils.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RotationHelper implements Helper {
    public static boolean isLookingAtEntity(float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = mc.player.getPositionEyes(1.0F);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        return (entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null);
    }

    public static double getDistanceOfEntityToBlock(Entity entity, BlockPos pos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ());
    }

    public static double getDistance(double x, double y, double z, double x1, double y1, double z1) {
        double posX = x - x1;
        double posY = y - y1;
        double posZ = z - z1;
        return MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ);
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(RotationHelper.mc.player.posX, RotationHelper.mc.player.getEntityBoundingBox().minY + (double) RotationHelper.mc.player.getEyeHeight(), RotationHelper.mc.player.posZ);
    }

    public static float[] getRotationVector(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - (RotationHelper.mc.player.posY + (double) RotationHelper.mc.player.getEyeHeight() + 0.5);
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        yaw = RotationHelper.mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - RotationHelper.mc.player.rotationYaw));
        pitch = RotationHelper.mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - RotationHelper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[]{yaw, pitch};
    }

    public static boolean isLookingAtEntity(boolean blockCheck, float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = mc.player.getPositionEyes(1.0f);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        if (blockCheck && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            return false;
        }
        return entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null;
    }


    public static double getDistance(double x1, double z1, double x2, double z2) {
        double deltaX = x1 - x2;
        double deltaZ = z1 - z2;
        return Math.hypot(deltaX, deltaZ);
    }

    public static float getAngle(Entity entity) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        return (float) (yaw - AnimationHelper.Interpolate(Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.prevRotationYaw, 1.0D));
    }

    public static float[] getTargetRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().player.posX;
        double zDiff = z - Minecraft.getMinecraft().player.posZ;
        double yDiff = y - Minecraft.getMinecraft().player.posY - 1.7;

        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }

    public static boolean isAimAtMe(Entity entity, float breakRadius) {
        float entityYaw = MathHelper.wrapDegrees(entity.rotationYaw);
        return Math.abs(MathHelper.wrapDegrees(getYawToEntity(entity, mc.player) - entityYaw)) <= breakRadius;
    }

    public static boolean posCheck(Entity entity) {
        return checkPosition(mc.player.posY, entity.posY - 1.5, entity.posY + 1.5);
    }

    public static boolean checkPosition(double pos1, double pos2, double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }

    public static float getYawToEntity(Entity mainEntity, Entity targetEntity) {
        double pX = mainEntity.posX;
        double pZ = mainEntity.posZ;
        double eX = targetEntity.posX;
        double eZ = targetEntity.posZ;
        double dX = pX - eX;
        double dZ = pZ - eZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float) yaw;
    }

    public static float[] getRotations(final double x, final double y, final double z) {
        final double n = x + 0.5;
        final double diffX = n - Minecraft.getMinecraft().player.posX;
        final double n2 = (y + 0.5) / 2.0;
        final double posY = Minecraft.getMinecraft().player.posY;
        final double diffY = n2 - (posY + Minecraft.getMinecraft().player.getEyeHeight());
        final double n3 = z + 0.5;
        final double diffZ = n3 - Minecraft.getMinecraft().player.posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Entity entityIn) {
        double diffX = entityIn.posX - mc.player.posX;
        double diffZ = entityIn.posZ - mc.player.posZ;
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.2f;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (mc.player.posY + mc.player.getEyeHeight());
        }
        if (!mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (mc.player.posY + mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90)+ GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(-1.75f, 1.75f)));
        float pitch = (float) ((Math.toDegrees(-Math.atan2(diffY, diffXZ))) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(-1.8f, 1.75f)));

        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);

        return new float[]{yaw, pitch};
    }

    public static float[] getFakeRotations(Entity entityIn) {
        double diffX = entityIn.posX - mc.player.posX;
        double diffZ = entityIn.posZ - mc.player.posZ;
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - 0.2f;
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (mc.player.posY + mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0));
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180.0 / Math.PI));

        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);

        return new float[]{yaw, pitch};
    }

    public static float[] getCustomRotations(Entity entityIn) {
        double diffX = entityIn.posX - mc.player.posX;
        double diffZ = entityIn.posZ - mc.player.posZ;
        double diffY;

        if (entityIn instanceof EntityLivingBase) {
            if (!KillAura.staticPitch.getBoolValue()) {
                diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - KillAura.pitchHead.getNumberValue();
            } else {
                diffY = entityIn.getEyeHeight() - mc.player.getEyeHeight() - KillAura.pitchHead.getNumberValue();
            }
        } else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2 - (mc.player.posY + mc.player.getEyeHeight());
        }
        if (!mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (mc.player.posY + mc.player.getEyeHeight());
        }

        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) ((Math.toDegrees(Math.atan2(diffZ, diffX)) - 90)) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(KillAura.yawrandom.getNumberValue(), -KillAura.yawrandom.getNumberValue()));
        float pitch = (float) Math.toDegrees(-(Math.atan2(diffY, diffXZ))) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(KillAura.pitchRandom.getNumberValue(), -KillAura.pitchRandom.getNumberValue()));

        yaw = (mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);

        return new float[]{yaw, pitch};
    }
}
