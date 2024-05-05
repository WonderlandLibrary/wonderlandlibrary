// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.math;

import ru.fluger.client.event.events.impl.packet.EventSendPacket;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventPlayerState;
import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.feature.impl.combat.KillAura;
import ru.fluger.client.helpers.Helper;

public class RotationHelper implements Helper
{
    public static boolean checkPosition(final double pos1, final double pos2, final double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }
    
    public static boolean posCheck(final vg entity) {
        return checkPosition(RotationHelper.mc.h.q, entity.q - 1.5, entity.q + 1.5);
    }
    
    public static bhe getEyesPos() {
        return new bhe(RotationHelper.mc.h.p, RotationHelper.mc.h.bw().b + RotationHelper.mc.h.by(), RotationHelper.mc.h.r);
    }
    
    public static float updateRotation(final float current, final float needed, final float speed) {
        float f = rk.g(needed - current);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }
    
    public static float getYawToEntity(final vg mainEntity, final vg targetEntity) {
        final double pX = mainEntity.p;
        final double pZ = mainEntity.r;
        final double eX = targetEntity.p;
        final double eZ = targetEntity.r;
        final double dX = pX - eX;
        final double dZ = pZ - eZ;
        final double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float)yaw;
    }
    
    public static float getNormalizedYaw(final float yaw) {
        float yawStageFirst = yaw % 360.0f;
        if (yawStageFirst > 180.0f) {
            return yawStageFirst -= 360.0f;
        }
        if (yawStageFirst < -180.0f) {
            return yawStageFirst += 360.0f;
        }
        return yawStageFirst;
    }
    
    public static boolean isAimAtMe2(final vg entityLiving, final float scope) {
        final double diffZ = RotationHelper.mc.h.r - entityLiving.r;
        final double diffX = RotationHelper.mc.h.p - entityLiving.p;
        final float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        final double difference = angleDifference(yaw, entityLiving.v);
        return difference <= scope;
    }
    
    public static double angleDifference(final double a, final double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }
    
    public static boolean isAimAtMe(final vg entity, final float breakRadius) {
        final float entityYaw = getNormalizedYaw(entity.v);
        return Math.abs(getNormalizedYaw(getYawToEntity(entity, RotationHelper.mc.h)) - entityYaw) <= breakRadius;
    }
    
    public static boolean isLookingAtEntity(final boolean blockCheck, final float yaw, final float pitch, final float xExp, final float yExp, final float zExp, final vg entity, final double range) {
        final bhe src = RotationHelper.mc.h.f(1.0f);
        final bhe vectorForRotation = vg.f(pitch, yaw);
        final bhe dest = src.b(vectorForRotation.b * range, vectorForRotation.c * range, vectorForRotation.d * range);
        final bhc rayTraceResult = RotationHelper.mc.f.a(src, dest, false, false, true);
        return rayTraceResult != null && (!blockCheck || rayTraceResult.a != bhc.a.b) && entity.bw().c(xExp, yExp, zExp).b(src, dest) != null;
    }
    
    public static float[] getRotationsCustom(final vg entityIn, final float speed, final boolean random) {
        final String mode = KillAura.rotationMode.getOptions();
        final float yawCurrent = (mode.equalsIgnoreCase("Matrix") || mode.equalsIgnoreCase("Sunrise") || mode.equalsIgnoreCase("AAC")) ? Rotation.packetYaw : RotationHelper.mc.h.v;
        final float pitchCurrent = (mode.equalsIgnoreCase("Matrix") || mode.equalsIgnoreCase("Sunrise") || mode.equalsIgnoreCase("AAC")) ? Rotation.packetPitch : RotationHelper.mc.h.w;
        float randomYaw = 0.0f;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -1.6f, KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -1.6f);
        }
        float randomPitch = 0.0f;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -1.6f, KillAura.rotationMode.currentMode.equals("ReallyWorld") ? -2.0f : -1.6f);
        }
        final float yaw = updateRotation(yawCurrent + randomYaw, getRotations(entityIn, random)[0], (!KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld")) ? Float.MAX_VALUE : (KillAura.rotationMode.currentMode.equals("Sunrise") ? 25.0f : speed));
        final float pitch = updateRotation(pitchCurrent + randomPitch, getRotations(entityIn, random)[1], (!KillAura.rotationMode.currentMode.equals("Sunrise") || KillAura.rotationMode.currentMode.equals("ReallyWorld")) ? Float.MAX_VALUE : (KillAura.rotationMode.currentMode.equals("Sunrise") ? 25.0f : speed));
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotations(final vg entityIn, final boolean random) {
        final double diffX = entityIn.p + (entityIn.p - entityIn.m) - RotationHelper.mc.h.p - RotationHelper.mc.h.s;
        final double diffZ = entityIn.r + (entityIn.r - entityIn.o) - RotationHelper.mc.h.r - RotationHelper.mc.h.u;
        double diffY = (entityIn instanceof vp) ? (entityIn.q + entityIn.by() - (RotationHelper.mc.h.q + RotationHelper.mc.h.by()) - ((KillAura.walls.getCurrentValue() && !((vp)entityIn).D(RotationHelper.mc.h)) ? -0.38 : ((KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn)) ? 1.0 : (KillAura.rotationMode.currentMode.equals("ReallyWorld") ? 0.2 : 0.25)))) : ((entityIn.bw().b + entityIn.bw().e) / 2.0 - (RotationHelper.mc.h.q + RotationHelper.mc.h.by()));
        final double diffXZ = rk.a(diffX * diffX + diffZ * diffZ);
        if (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn)) {
            diffY /= diffXZ;
        }
        float randomYaw = 0.0f;
        if (random) {
            randomYaw = MathematicHelper.randomizeFloat(-1.6f, 1.6f);
        }
        float randomPitch = 0.0f;
        if (random) {
            randomPitch = MathematicHelper.randomizeFloat(-1.6f, 1.6f);
        }
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0) + randomYaw;
        float pitch = (float)(-(Math.atan2(diffY, diffXZ) * 180.0 / ((KillAura.walls.getCurrentValue() && !((vp)entityIn).D(RotationHelper.mc.h)) ? 3.1 : (3.141592653589793 + ((KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn)) ? 10 : 0))))) + randomPitch;
        if (KillAura.rotationMode.currentMode.equals("Sunrise") && posCheck(entityIn)) {
            pitch += 26.0f;
        }
        yaw = RotationHelper.mc.h.v + GCDCalcHelper.getFixedRotation(rk.g(yaw - RotationHelper.mc.h.v));
        pitch = RotationHelper.mc.h.w + GCDCalcHelper.getFixedRotation(rk.g(pitch - RotationHelper.mc.h.w));
        final float minValue = -90.0f;
        final float maxValue = 90.0f;
        pitch = rk.a(pitch, minValue, maxValue);
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotationVector(final bhe vec) {
        final bhe eyesPos = getEyesPos();
        final double diffX = vec.b - eyesPos.b;
        final double diffY = vec.c - (RotationHelper.mc.h.q + RotationHelper.mc.h.by() + 0.5);
        final double diffZ = vec.d - eyesPos.d;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        yaw = RotationHelper.mc.h.v + GCDCalcHelper.getFixedRotation(rk.g(yaw - RotationHelper.mc.h.v));
        pitch = RotationHelper.mc.h.w + GCDCalcHelper.getFixedRotation(rk.g(pitch - RotationHelper.mc.h.w));
        pitch = rk.a(pitch, -90.0f, 90.0f);
        return new float[] { yaw, pitch };
    }
    
    public static float getAngleEntity(final vg entity) {
        return getYawBetween(RotationHelper.mc.h.v, RotationHelper.mc.h.p, RotationHelper.mc.h.r, entity.p, entity.r);
    }
    
    public static float getYawBetween(final float yaw, final double srcX, final double srcZ, final double destX, final double destZ) {
        final double xDist = destX - srcX;
        final double zDist = destZ - srcZ;
        final float yaw2 = (float)(Math.atan2(zDist, xDist) * 180.0 / 3.141592653589793 - 90.0);
        return yaw + rk.g(yaw2 - yaw);
    }
    
    public static float[] getFacePosEntityRemote(final vp facing, final vg en) {
        return getFacePosRemote(new bhe(facing.p, facing.q, facing.r), new bhe(en.p, en.q, en.r));
    }
    
    public static float[] getFacePosRemote(final bhe src, final bhe dest) {
        final double diffX = dest.b - src.b;
        final double diffY = dest.c - src.c;
        final double diffZ = dest.d - src.d;
        final double dist = rk.a(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { rk.g(yaw), rk.g(pitch) };
    }
    
    public static void setRotations(final float yaw, final float pitch) {
        RotationHelper.mc.h.aN = yaw;
        RotationHelper.mc.h.aP = yaw;
        RotationHelper.mc.h.rotationPitchHead = pitch;
    }
    
    public static void setRotations(final EventPreMotion event, final float yaw, final float pitch, final float visualYaw, final float visualPitch) {
        event.setYaw(yaw);
        event.setPitch(pitch);
        RotationHelper.mc.h.aN = visualYaw;
        RotationHelper.mc.h.aP = visualYaw;
        RotationHelper.mc.h.rotationPitchHead = visualPitch;
    }
    
    public static class Rotation
    {
        public static boolean isReady;
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
            return Rotation.isAiming;
        }
        
        public static void setAiming() {
            final double x = Helper.mc.h.p - Helper.mc.h.m;
            final double z = Helper.mc.h.r - Helper.mc.h.o;
            if (x * x + z * z > 2.500000277905201E-7) {
                Rotation.bodyYaw = MathematicHelper.clamp(MovementHelper.getMoveDirection(), Rotation.packetYaw, 50.0f);
                Rotation.rotationCounter = 0;
            }
            else if (Rotation.rotationCounter > 0) {
                --Rotation.rotationCounter;
                Rotation.bodyYaw = MathematicHelper.checkAngle(Rotation.packetYaw, Rotation.bodyYaw, 10.0f);
            }
        }
        
        @EventTarget
        public void onPlayerState(final EventPlayerState eventPlayerState) {
            if (!isAiming() && eventPlayerState.isPre()) {
                Rotation.isReady = true;
            }
            else if (!isAiming() && Rotation.isReady && eventPlayerState.isPost()) {
                Rotation.packetPitch = Helper.mc.h.w;
                Rotation.packetYaw = Helper.mc.h.v;
                Rotation.lastPacketPitch = Helper.mc.h.w;
                Rotation.lastPacketYaw = Helper.mc.h.v;
                Rotation.bodyYaw = Helper.mc.h.aN;
                Rotation.isReady = false;
            }
            else {
                Rotation.isReady = false;
            }
            if (eventPlayerState.isPre()) {
                Rotation.lastBodyYaw = Rotation.bodyYaw;
                Rotation.lastPacketPitch = Rotation.packetPitch;
                Rotation.lastPacketYaw = Rotation.packetYaw;
                Rotation.bodyYaw = MathematicHelper.clamp(Rotation.bodyYaw, Rotation.packetYaw, 50.0f);
                setAiming();
                Rotation.lastRenderPacketYaw = Rotation.renderPacketYaw;
                Rotation.renderPacketYaw = Rotation.packetYaw;
            }
        }
        
        @EventTarget
        public void onSendPacket(final EventSendPacket eventSendPacket) {
            if (eventSendPacket.getPacket() instanceof ly) {
                Rotation.rotationCounter = 10;
            }
        }
        
        static {
            Rotation.isReady = false;
        }
    }
}
