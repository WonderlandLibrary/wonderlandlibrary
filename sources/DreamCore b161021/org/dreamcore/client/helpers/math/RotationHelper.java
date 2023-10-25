package org.dreamcore.client.helpers.math;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventPlayerState;
import org.dreamcore.client.feature.impl.combat.KillAura;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.player.MovementHelper;

public class RotationHelper implements Helper {

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
    }

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

    public static float[] getRotations(Entity entityIn, boolean random, float maxSpeed, float minSpeed, float yawRandom, float pitchRandom) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) * KillAura.rotPredict.getNumberValue() - mc.player.posX - mc.player.motionX * KillAura.rotPredict.getNumberValue();
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) * KillAura.rotPredict.getNumberValue() - mc.player.posZ - mc.player.motionZ * KillAura.rotPredict.getNumberValue();
        double diffY;

        diffY = entityIn.posY + entityIn.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight()) - KillAura.pitchValue.getNumberValue() - (KillAura.walls.getBoolValue() && KillAura.bypass.getBoolValue() && !((EntityLivingBase) entityIn).canEntityBeSeen(mc.player) ? -0.38 : 0);

        float randomYaw = 0;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(yawRandom, -yawRandom);
        }
        float randomPitch = 0;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(pitchRandom, -pitchRandom);
        }

        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180 / Math.PI) - 90)) + randomYaw;
        float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180 / Math.PI)) + randomPitch;

        yaw = (mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)));
        pitch = mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(mc.player.rotationYaw, yaw, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));
        pitch = RotationHelper.updateRotation(mc.player.rotationPitch, pitch, MathematicHelper.randomizeFloat(minSpeed, maxSpeed));

        return new float[]{yaw, pitch};
    }

    public static float[] getRotationVector(Vec3d vec, boolean randomRotation, float yawRandom, float pitchRandom, float speedRotation) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - (mc.player.posY + mc.player.getEyeHeight() + 0.5);
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float randomYaw = 0;
        if (randomRotation) {
            randomYaw = MathematicHelper.randomizeFloat(-yawRandom, yawRandom);
        }
        float randomPitch = 0;
        if (randomRotation) {
            randomPitch = MathematicHelper.randomizeFloat(-pitchRandom, pitchRandom);
        }

        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f) + randomYaw;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ))) + randomPitch;
        yaw = mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - mc.player.rotationYaw));
        pitch = mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        yaw = RotationHelper.updateRotation(mc.player.rotationYaw, yaw, speedRotation);
        pitch = RotationHelper.updateRotation(mc.player.rotationPitch, pitch, speedRotation);

        return new float[]{yaw, pitch};
    }

    public static float getAngleEntity(Entity entity) {
        float yaw = mc.player.rotationYaw;
        double xDist = entity.posX - mc.player.posX;
        double zDist = entity.posZ - mc.player.posZ;
        float yaw1 = (float) ((Math.atan2(zDist, xDist) * 180 / Math.PI) - 90);
        return yaw + MathHelper.wrapDegrees(yaw1 - yaw);
    }

    public static float[] getFacePosRemote(EntityLivingBase facing, Entity entity, boolean random) {
        Vec3d src = new Vec3d(facing.posX, facing.posY, facing.posZ);
        Vec3d dest = new Vec3d(entity.posX, entity.posY, entity.posZ);
        double diffX = dest.xCoord - src.xCoord;
        double diffY = dest.yCoord - (src.yCoord);
        double diffZ = dest.zCoord - src.zCoord;
        float randomYaw = 0;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(-2, 2);
        }
        float randomPitch = 0;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(-2, 2);
        }
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0F / Math.PI) - 90.0F + randomYaw;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0F / Math.PI) + randomPitch;
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    public static float updateRotation(float current, float newValue, float speed) {
        float f = MathHelper.wrapDegrees(newValue - current);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }

    public static class Rotation {

        public static boolean isReady = false;
        public static float packetPitch;
        public static float packetYaw;
        public static float lastPacketPitch;
        public static float lastPacketYaw;
        public static float renderPacketYaw;
        public static float lastRenderPacketYaw;
        public static float bodyYaw;
        public static float lastBodyYaw;
        public static int rotationCounter;
        public static boolean isAiming;

        public static boolean isAiming() {
            return !isAiming;
        }

        public static double calcMove() {
            double x = mc.player.posX - mc.player.prevPosX;
            double z = mc.player.posZ - mc.player.prevPosZ;
            return Math.hypot(x, z);
        }

        @EventTarget
        public void onPlayerState(EventPlayerState event) {
            if (isAiming() && event.isPre()) {
                isReady = true;
            } else if (isAiming() && isReady && event.isPost()) {
                packetPitch = mc.player.rotationPitch;
                packetYaw = mc.player.rotationYaw;
                lastPacketPitch = mc.player.rotationPitch;
                lastPacketYaw = mc.player.rotationYaw;
                bodyYaw = mc.player.renderYawOffset;
                isReady = false;
            } else {
                isReady = false;
            }
            if (event.isPre()) {
                lastBodyYaw = bodyYaw;
                lastPacketPitch = packetPitch;
                lastPacketYaw = packetYaw;
                bodyYaw = MathematicHelper.clamp(bodyYaw, packetYaw, 50F);
                if (calcMove() > 2.500000277905201E-7D) {
                    bodyYaw = MathematicHelper.clamp(MovementHelper.getMoveDirection(), packetYaw, 50F);
                    rotationCounter = 0;
                } else if (rotationCounter > 0) {
                    rotationCounter--;
                    bodyYaw = MathematicHelper.checkAngle(packetYaw, bodyYaw, 10F);
                }
                lastRenderPacketYaw = renderPacketYaw;
                renderPacketYaw = packetYaw;
            }
        }

        @EventTarget
        public void onSendPacket(EventSendPacket eventSendPacket) {
            if (eventSendPacket.getPacket() instanceof CPacketAnimation) {
                rotationCounter = 10;
            }
        }
    }
}
