/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.math;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventPlayerState;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.helpers.Helper;
import org.celestial.client.helpers.math.GCDCalcHelper;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.player.MovementHelper;

public class RotationHelper
implements Helper {
    public static boolean checkPosition(double pos1, double pos2, double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }

    public static boolean posCheck(Entity entity) {
        return RotationHelper.checkPosition(RotationHelper.mc.player.posY, entity.posY - 1.5, entity.posY + 1.5);
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(RotationHelper.mc.player.posX, RotationHelper.mc.player.getEntityBoundingBox().minY + (double)RotationHelper.mc.player.getEyeHeight(), RotationHelper.mc.player.posZ);
    }

    public static float updateRotation(float current, float needed, float speed) {
        float f = MathHelper.wrapDegrees(needed - current);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }

    public static float getYawToEntity(Entity mainEntity, Entity targetEntity) {
        double pX = mainEntity.posX;
        double pZ = mainEntity.posZ;
        double eX = targetEntity.posX;
        double eZ = targetEntity.posZ;
        double dX = pX - eX;
        double dZ = pZ - eZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float)yaw;
    }

    public static float getNormalizedYaw(float yaw) {
        float yawStageFirst = yaw % 360.0f;
        if (yawStageFirst > 180.0f) {
            return yawStageFirst -= 360.0f;
        }
        if (yawStageFirst < -180.0f) {
            return yawStageFirst += 360.0f;
        }
        return yawStageFirst;
    }

    public static boolean isAimAtMe2(Entity entityLiving, float scope) {
        double diffZ = RotationHelper.mc.player.posZ - entityLiving.posZ;
        double diffX = RotationHelper.mc.player.posX - entityLiving.posX;
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = RotationHelper.angleDifference(yaw, entityLiving.rotationYaw);
        return difference <= (double)scope;
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }

    public static boolean isAimAtMe(Entity entity, float breakRadius) {
        float entityYaw = RotationHelper.getNormalizedYaw(entity.rotationYaw);
        return Math.abs(RotationHelper.getNormalizedYaw(RotationHelper.getYawToEntity(entity, RotationHelper.mc.player)) - entityYaw) <= breakRadius;
    }

    public static boolean isLookingAtEntity(boolean blockCheck, float yaw, float pitch, float xExp, float yExp, float zExp, Entity entity, double range) {
        Vec3d src = RotationHelper.mc.player.getPositionEyes(1.0f);
        Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        Vec3d dest = src.add(vectorForRotation.x * range, vectorForRotation.y * range, vectorForRotation.z * range);
        RayTraceResult rayTraceResult = RotationHelper.mc.world.rayTraceBlocks(src, dest, false, false, true);
        if (rayTraceResult == null) {
            return false;
        }
        if (blockCheck && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            return false;
        }
        return entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null;
    }

    public static float[] getRotationsCustom(Entity entityIn, float speed, boolean random) {
        String mode = KillAura.rotationMode.getOptions();
        float yawCurrent = mode.equalsIgnoreCase("Packet") || mode.equalsIgnoreCase("Sunrise") || mode.equalsIgnoreCase("AAC") ? Rotation.packetYaw : RotationHelper.mc.player.rotationYaw;
        float pitchCurrent = mode.equalsIgnoreCase("Packet") || mode.equalsIgnoreCase("Sunrise") || mode.equalsIgnoreCase("AAC") ? Rotation.packetPitch : RotationHelper.mc.player.rotationPitch;
        float randomYaw = 0.0f;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -KillAura.rotYawRandom.getCurrentValue(), KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -KillAura.rotYawRandom.getCurrentValue());
        }
        float randomPitch = 0.0f;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -KillAura.rotPitchRandom.getCurrentValue(), KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -KillAura.rotPitchRandom.getCurrentValue());
        }
        float yaw = RotationHelper.updateRotation(yawCurrent + randomYaw, RotationHelper.getRotations(entityIn, random)[0], KillAura.rotSpeed.getCurrentValue() >= 5.0f && !KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") ? Float.MAX_VALUE : (KillAura.rotationMode.currentMode.equals("Sunrise") ? 25.0f : speed));
        float pitch = RotationHelper.updateRotation(pitchCurrent + randomPitch, RotationHelper.getRotations(entityIn, random)[1], KillAura.rotSpeed.getCurrentValue() >= 5.0f && !KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld") ? Float.MAX_VALUE : (KillAura.rotationMode.currentMode.equals("Sunrise") ? 25.0f : speed));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Entity entityIn, boolean random) {
        double diffX = entityIn.posX + (entityIn.posX - entityIn.prevPosX) * (double)KillAura.rotPredict.getCurrentValue() - RotationHelper.mc.player.posX - RotationHelper.mc.player.motionX * (double)KillAura.rotPredict.getCurrentValue();
        double diffZ = entityIn.posZ + (entityIn.posZ - entityIn.prevPosZ) * (double)KillAura.rotPredict.getCurrentValue() - RotationHelper.mc.player.posZ - RotationHelper.mc.player.motionZ * (double)KillAura.rotPredict.getCurrentValue();
        double diffY = entityIn instanceof EntityLivingBase ? entityIn.posY + (double)entityIn.getEyeHeight() - (RotationHelper.mc.player.posY + (double)RotationHelper.mc.player.getEyeHeight()) - (KillAura.walls.getCurrentValue() && KillAura.wallsBypass.getCurrentValue() && !((EntityLivingBase)entityIn).canEntityBeSeen(RotationHelper.mc.player) ? -0.38 : (KillAura.rotationMode.currentMode.equals("Sunrise") && RotationHelper.posCheck(entityIn) ? 1.0 : (KillAura.rotationMode.currentMode.equals("ReallyWorld") ? 0.2 : (double)KillAura.rotPitchDown.getCurrentValue()))) : (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (RotationHelper.mc.player.posY + (double)RotationHelper.mc.player.getEyeHeight());
        double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        if (KillAura.rotationMode.currentMode.equals("Sunrise") && RotationHelper.posCheck(entityIn)) {
            diffY /= diffXZ;
        }
        float randomYaw = 0.0f;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(-KillAura.rotYawRandom.getCurrentValue(), KillAura.rotYawRandom.getCurrentValue());
        }
        float randomPitch = 0.0f;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(-KillAura.rotPitchRandom.getCurrentValue(), KillAura.rotPitchRandom.getCurrentValue());
        }
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0) + randomYaw;
        float pitch = (float)(-(Math.atan2(diffY, diffXZ) * 180.0 / (KillAura.walls.getCurrentValue() && KillAura.wallsBypass.getCurrentValue() && !((EntityLivingBase)entityIn).canEntityBeSeen(RotationHelper.mc.player) ? 3.1 : Math.PI + (double)(KillAura.rotationMode.currentMode.equals("Sunrise") && RotationHelper.posCheck(entityIn) ? 10 : 0)))) + randomPitch;
        if (KillAura.rotationMode.currentMode.equals("Sunrise") && RotationHelper.posCheck(entityIn)) {
            pitch += 26.0f;
        }
        yaw = RotationHelper.mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - RotationHelper.mc.player.rotationYaw));
        pitch = RotationHelper.mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - RotationHelper.mc.player.rotationPitch));
        float minValue = -90.0f;
        float maxValue = 90.0f;
        pitch = MathHelper.clamp(pitch, minValue, maxValue);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationVector(Vec3d vec) {
        Vec3d eyesPos = RotationHelper.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - (RotationHelper.mc.player.posY + (double)RotationHelper.mc.player.getEyeHeight() + 0.5);
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        yaw = RotationHelper.mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - RotationHelper.mc.player.rotationYaw));
        pitch = RotationHelper.mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - RotationHelper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[]{yaw, pitch};
    }

    public static float getAngleEntity(Entity entity) {
        return RotationHelper.getYawBetween(RotationHelper.mc.player.rotationYaw, RotationHelper.mc.player.posX, RotationHelper.mc.player.posZ, entity.posX, entity.posZ);
    }

    public static float getYawBetween(float yaw, double srcX, double srcZ, double destX, double destZ) {
        double xDist = destX - srcX;
        double zDist = destZ - srcZ;
        float yaw1 = (float)(Math.atan2(zDist, xDist) * 180.0 / Math.PI - 90.0);
        return yaw + MathHelper.wrapDegrees(yaw1 - yaw);
    }

    public static float[] getFacePosEntityRemote(EntityLivingBase facing, Entity en) {
        return RotationHelper.getFacePosRemote(new Vec3d(facing.posX, facing.posY, facing.posZ), new Vec3d(en.posX, en.posY, en.posZ));
    }

    public static float[] getFacePosRemote(Vec3d src, Vec3d dest) {
        double diffX = dest.x - src.x;
        double diffY = dest.y - src.y;
        double diffZ = dest.z - src.z;
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    public static void setRotations(float yaw, float pitch) {
        RotationHelper.mc.player.renderYawOffset = yaw;
        RotationHelper.mc.player.rotationYawHead = yaw;
        RotationHelper.mc.player.rotationPitchHead = pitch;
    }

    public static void setRotations(EventPreMotion event, float yaw, float pitch, float visualYaw, float visualPitch) {
        event.setYaw(yaw);
        event.setPitch(pitch);
        RotationHelper.mc.player.renderYawOffset = visualYaw;
        RotationHelper.mc.player.rotationYawHead = visualYaw;
        RotationHelper.mc.player.rotationPitchHead = visualPitch;
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
        private static boolean isAiming;

        public static boolean isAiming() {
            return isAiming;
        }

        public static void setAiming() {
            double x = Helper.mc.player.posX - Helper.mc.player.prevPosX;
            double z = Helper.mc.player.posZ - Helper.mc.player.prevPosZ;
            if (x * x + z * z > 2.500000277905201E-7) {
                bodyYaw = MathematicHelper.clamp(MovementHelper.getMoveDirection(), packetYaw, 50.0f);
                rotationCounter = 0;
            } else if (rotationCounter > 0) {
                --rotationCounter;
                bodyYaw = MathematicHelper.checkAngle(packetYaw, bodyYaw, 10.0f);
            }
        }

        @EventTarget
        public void onPlayerState(EventPlayerState eventPlayerState) {
            if (!Rotation.isAiming() && eventPlayerState.isPre()) {
                isReady = true;
            } else if (!Rotation.isAiming() && isReady && eventPlayerState.isPost()) {
                packetPitch = Helper.mc.player.rotationPitch;
                packetYaw = Helper.mc.player.rotationYaw;
                lastPacketPitch = Helper.mc.player.rotationPitch;
                lastPacketYaw = Helper.mc.player.rotationYaw;
                bodyYaw = Helper.mc.player.renderYawOffset;
                isReady = false;
            } else {
                isReady = false;
            }
            if (eventPlayerState.isPre()) {
                lastBodyYaw = bodyYaw;
                lastPacketPitch = packetPitch;
                lastPacketYaw = packetYaw;
                bodyYaw = MathematicHelper.clamp(bodyYaw, packetYaw, 50.0f);
                Rotation.setAiming();
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

