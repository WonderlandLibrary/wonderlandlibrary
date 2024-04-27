/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package net.ccbluex.liquidbounce.utils;

import java.util.Random;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.FastBow;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.minecraft.util.math.MathHelper;

public final class RotationUtils
extends MinecraftInstance
implements Listenable {
    private static final Random random = new Random();
    private static int keepLength;
    public static Rotation targetRotation;
    public static Rotation serverRotation;
    public static boolean keepCurrentRotation;
    private static double x;
    private static double y;
    private static double z;

    public static Rotation getRotationsEntity(IEntityLivingBase entity) {
        return RotationUtils.getRotations(entity.getPosX(), entity.getPosY() + (double)entity.getEyeHeight() - 0.4, entity.getPosZ());
    }

    public static Rotation getNewRotations(WVec3 vec, boolean predict) {
        WVec3 eyesPos = new WVec3(RotationUtils.mc2.field_71439_g.field_70165_t, RotationUtils.mc2.field_71439_g.func_174813_aQ().field_72338_b + (double)RotationUtils.mc2.field_71439_g.func_70047_e(), RotationUtils.mc2.field_71439_g.field_70161_v);
        double diffX = vec.getXCoord() - eyesPos.getXCoord();
        double diffY = vec.getYCoord() - eyesPos.getYCoord();
        double diffZ = vec.getZCoord() - eyesPos.getZCoord();
        double dist = MathHelper.func_76133_a((double)(diffX * diffX + diffZ * diffZ));
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / Math.PI);
        return new Rotation(yaw, pitch);
    }

    public static Rotation getRotations(double posX, double posY, double posZ) {
        double x = posX - mc.getThePlayer().getPosX();
        double y = posY - (mc.getThePlayer().getPosY() + (double)mc.getThePlayer().getEyeHeight());
        double z = posZ - mc.getThePlayer().getPosZ();
        double dist = MathHelper.func_76133_a((double)(x * x + z * z));
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new Rotation(yaw, pitch);
    }

    public static Rotation OtherRotation(IAxisAlignedBB bb, WVec3 vec, boolean predict, boolean throughWalls, float distance) {
        WVec3 eyesPos = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
        WVec3 eyes = mc.getThePlayer().getPositionEyes(1.0f);
        VecRotation vecRotation = null;
        for (double xSearch = 0.15; xSearch < 0.85; xSearch += 0.1) {
            for (double ySearch = 0.15; ySearch < 1.0; ySearch += 0.1) {
                for (double zSearch = 0.15; zSearch < 0.85; zSearch += 0.1) {
                    WVec3 vec3 = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * xSearch, bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * ySearch, bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * zSearch);
                    Rotation rotation = RotationUtils.toRotation(vec3, predict);
                    double vecDist = eyes.distanceTo(vec3);
                    if (vecDist > (double)distance || !throughWalls && !RotationUtils.isVisible(vec3)) continue;
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null) continue;
                    vecRotation = currentVec;
                }
            }
        }
        if (predict) {
            eyesPos.addVector(mc.getThePlayer().getMotionX(), mc.getThePlayer().getMotionY(), mc.getThePlayer().getMotionZ());
        }
        double diffX = vec.getXCoord() - eyesPos.getXCoord();
        double diffY = vec.getYCoord() - eyesPos.getYCoord();
        double diffZ = vec.getZCoord() - eyesPos.getZCoord();
        return new Rotation(WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    public static VecRotation lockView(IAxisAlignedBB bb, boolean outborder, boolean random, boolean predict, boolean throughWalls, float distance) {
        if (outborder) {
            WVec3 vec3 = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * (x * 0.3 + 1.0), bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * (y * 0.3 + 1.0), bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * (z * 0.3 + 1.0));
            return new VecRotation(vec3, RotationUtils.toRotation(vec3, predict));
        }
        WVec3 randomVec = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * x * 0.8, bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * y * 0.8, bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * z * 0.8);
        Rotation randomRotation = RotationUtils.toRotation(randomVec, predict);
        WVec3 eyes = mc.getThePlayer().getPositionEyes(1.0f);
        double xMin = 0.0;
        double yMin = 0.0;
        double zMin = 0.0;
        double xMax = 0.0;
        double yMax = 0.0;
        double zMax = 0.0;
        double xDist = 0.0;
        double yDist = 0.0;
        double zDist = 0.0;
        VecRotation vecRotation = null;
        xMin = 0.45;
        xMax = 0.55;
        xDist = 0.0125;
        yMin = 0.65;
        yMax = 0.75;
        yDist = 0.0125;
        zMin = 0.45;
        zMax = 0.55;
        zDist = 0.0125;
        for (double xSearch = xMin; xSearch < xMax; xSearch += xDist) {
            for (double ySearch = yMin; ySearch < yMax; ySearch += yDist) {
                for (double zSearch = zMin; zSearch < zMax; zSearch += zDist) {
                    WVec3 vec3 = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * xSearch, bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * ySearch, bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * zSearch);
                    Rotation rotation = RotationUtils.toRotation(vec3, predict);
                    double vecDist = eyes.distanceTo(vec3);
                    if (vecDist > (double)distance || !throughWalls && !RotationUtils.isVisible(vec3)) continue;
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null && !(random ? RotationUtils.getRotationDifference(currentVec.getRotation(), randomRotation) < RotationUtils.getRotationDifference(vecRotation.getRotation(), randomRotation) : RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static VecRotation faceBlock(WBlockPos blockPos) {
        if (blockPos == null) {
            return null;
        }
        VecRotation vecRotation = null;
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    WVec3 eyesPos = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
                    WVec3 posVec = new WVec3(blockPos).addVector(xSearch, ySearch, zSearch);
                    double dist = eyesPos.distanceTo(posVec);
                    double diffX = posVec.getXCoord() - eyesPos.getXCoord();
                    double diffY = posVec.getYCoord() - eyesPos.getYCoord();
                    double diffZ = posVec.getZCoord() - eyesPos.getZCoord();
                    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    Rotation rotation = new Rotation(WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)))));
                    WVec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                    WVec3 vector = eyesPos.addVector(rotationVector.getXCoord() * dist, rotationVector.getYCoord() * dist, rotationVector.getZCoord() * dist);
                    IMovingObjectPosition obj = mc.getTheWorld().rayTraceBlocks(eyesPos, vector, false, false, true);
                    if (obj == null || obj.getTypeOfHit() != IMovingObjectPosition.WMovingObjectType.BLOCK) continue;
                    VecRotation currentVec = new VecRotation(posVec, rotation);
                    if (vecRotation != null && !(RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static void faceBow(IEntity target, boolean silent, boolean predict, float predictSize) {
        IEntityPlayerSP player = mc.getThePlayer();
        double posX = target.getPosX() + (predict ? (target.getPosX() - target.getPrevPosX()) * (double)predictSize : 0.0) - (player.getPosX() + (predict ? player.getPosX() - player.getPrevPosX() : 0.0));
        double posY = target.getEntityBoundingBox().getMinY() + (predict ? (target.getEntityBoundingBox().getMinY() - target.getPrevPosY()) * (double)predictSize : 0.0) + (double)target.getEyeHeight() - 0.15 - (player.getEntityBoundingBox().getMinY() + (predict ? player.getPosY() - player.getPrevPosY() : 0.0)) - (double)player.getEyeHeight();
        double posZ = target.getPosZ() + (predict ? (target.getPosZ() - target.getPrevPosZ()) * (double)predictSize : 0.0) - (player.getPosZ() + (predict ? player.getPosZ() - player.getPrevPosZ() : 0.0));
        double posSqrt = Math.sqrt(posX * posX + posZ * posZ);
        float velocity = LiquidBounce.moduleManager.getModule(FastBow.class).getState() ? 1.0f : (float)player.getItemInUseDuration() / 20.0f;
        if ((velocity = (velocity * velocity + velocity * 2.0f) / 3.0f) > 1.0f) {
            velocity = 1.0f;
        }
        Rotation rotation = new Rotation((float)(Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f, (float)(-Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt((double)(velocity * velocity * velocity * velocity) - (double)0.006f * ((double)0.006f * (posSqrt * posSqrt) + 2.0 * posY * (double)(velocity * velocity)))) / ((double)0.006f * posSqrt)))));
        if (silent) {
            RotationUtils.setTargetRotation(rotation);
        } else {
            RotationUtils.limitAngleChange(new Rotation(player.getRotationYaw(), player.getRotationPitch()), rotation, 10 + new Random().nextInt(6)).toPlayer(mc.getThePlayer());
        }
    }

    public static Rotation toRotation(WVec3 vec, boolean predict) {
        WVec3 eyesPos = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
        if (predict) {
            eyesPos.addVector(mc.getThePlayer().getMotionX(), mc.getThePlayer().getMotionY(), mc.getThePlayer().getMotionZ());
        }
        double diffX = vec.getXCoord() - eyesPos.getXCoord();
        double diffY = vec.getYCoord() - eyesPos.getYCoord();
        double diffZ = vec.getZCoord() - eyesPos.getZCoord();
        return new Rotation(WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    public static WVec3 getCenter(IAxisAlignedBB bb) {
        return new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * 0.5, bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * 0.5, bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * 0.5);
    }

    public static VecRotation searchCenter1(IAxisAlignedBB bb, boolean outborder, boolean random, boolean predict, boolean throughWalls, float distance, boolean xS, boolean yS) {
        if (outborder) {
            WVec3 vec3 = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * (x * 0.3 + 1.0), bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * (y * 0.3 + 1.0), bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * (z * 0.3 + 1.0));
            return new VecRotation(vec3, RotationUtils.toRotation(vec3, predict));
        }
        WVec3 randomVec = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * x * 0.8, bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * y * 0.8, bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * z * 0.8);
        Rotation randomRotation = RotationUtils.toRotation(randomVec, predict);
        WVec3 eyes = mc.getThePlayer().getPositionEyes(1.0f);
        VecRotation vecRotation = null;
        for (double xSearch = 0.15; xSearch < 0.85; xSearch += 0.1) {
            for (double ySearch = 0.15; ySearch < 1.0; ySearch += 0.1) {
                for (double zSearch = 0.15; zSearch < 0.85; zSearch += 0.1) {
                    WVec3 vec3 = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * (xS ? xSearch : 0.5), bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * (yS ? ySearch : 0.5), bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * (xS ? zSearch : 0.5));
                    Rotation rotation = RotationUtils.toRotation(vec3, predict);
                    double vecDist = eyes.distanceTo(vec3);
                    if (vecDist > (double)distance || !throughWalls && !RotationUtils.isVisible(vec3)) continue;
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null && !(random ? RotationUtils.getRotationDifference(currentVec.getRotation(), randomRotation) < RotationUtils.getRotationDifference(vecRotation.getRotation(), randomRotation) : RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static VecRotation searchCenter(IAxisAlignedBB bb, boolean outborder, boolean random, boolean predict, boolean throughWalls, float distance) {
        if (outborder) {
            WVec3 vec3 = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * (x * 0.3 + 1.0), bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * (y * 0.3 + 1.0), bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * (z * 0.3 + 1.0));
            return new VecRotation(vec3, RotationUtils.toRotation(vec3, predict));
        }
        WVec3 randomVec = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * x * 0.8, bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * y * 0.8, bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * z * 0.8);
        Rotation randomRotation = RotationUtils.toRotation(randomVec, predict);
        WVec3 eyes = mc.getThePlayer().getPositionEyes(1.0f);
        VecRotation vecRotation = null;
        for (double xSearch = 0.15; xSearch < 0.85; xSearch += 0.1) {
            for (double ySearch = 0.15; ySearch < 1.0; ySearch += 0.1) {
                for (double zSearch = 0.15; zSearch < 0.85; zSearch += 0.1) {
                    WVec3 vec3 = new WVec3(bb.getMinX() + (bb.getMaxX() - bb.getMinX()) * xSearch, bb.getMinY() + (bb.getMaxY() - bb.getMinY()) * ySearch, bb.getMinZ() + (bb.getMaxZ() - bb.getMinZ()) * zSearch);
                    Rotation rotation = RotationUtils.toRotation(vec3, predict);
                    double vecDist = eyes.distanceTo(vec3);
                    if (vecDist > (double)distance || !throughWalls && !RotationUtils.isVisible(vec3)) continue;
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null && !(random ? RotationUtils.getRotationDifference(currentVec.getRotation(), randomRotation) < RotationUtils.getRotationDifference(vecRotation.getRotation(), randomRotation) : RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static double getRotationDifference(IEntity entity) {
        Rotation rotation = RotationUtils.toRotation(RotationUtils.getCenter(entity.getEntityBoundingBox()), true);
        return RotationUtils.getRotationDifference(rotation, new Rotation(mc.getThePlayer().getRotationYaw(), mc.getThePlayer().getRotationPitch()));
    }

    public static double getRotationDifference(Rotation rotation) {
        return serverRotation == null ? 0.0 : RotationUtils.getRotationDifference(rotation, serverRotation);
    }

    public static double getRotationDifference(Rotation a, Rotation b) {
        return Math.hypot(RotationUtils.getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    public static Rotation limitAngleChange(Rotation currentRotation, Rotation targetRotation, float turnSpeed) {
        float yawDifference = RotationUtils.getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        float pitchDifference = RotationUtils.getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        return new Rotation(currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
    }

    private static float getAngleDifference(float a, float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static WVec3 getVectorForRotation(Rotation rotation) {
        float yawCos = (float)Math.cos(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float yawSin = (float)Math.sin(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float pitchCos = (float)(-Math.cos(-rotation.getPitch() * ((float)Math.PI / 180)));
        float pitchSin = (float)Math.sin(-rotation.getPitch() * ((float)Math.PI / 180));
        return new WVec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }

    public static boolean isFaced(IEntity targetEntity, double blockReachDistance) {
        return RaycastUtils.raycastEntity(blockReachDistance, entity -> targetEntity != null && targetEntity.equals(entity)) != null;
    }

    public static boolean isVisible(WVec3 vec3) {
        WVec3 eyesPos = new WVec3(mc.getThePlayer().getPosX(), mc.getThePlayer().getEntityBoundingBox().getMinY() + (double)mc.getThePlayer().getEyeHeight(), mc.getThePlayer().getPosZ());
        return mc.getTheWorld().rayTraceBlocks(eyesPos, vec3) == null;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (targetRotation != null && --keepLength <= 0) {
            RotationUtils.reset();
        }
        if (random.nextGaussian() > 0.8) {
            x = Math.random();
        }
        if (random.nextGaussian() > 0.8) {
            y = Math.random();
        }
        if (random.nextGaussian() > 0.8) {
            z = Math.random();
        }
    }

    public static Rotation getNCPRotations(WVec3 vec, boolean predict) {
        WVec3 eyesPos = new WVec3(RotationUtils.mc2.field_71439_g.field_70165_t, RotationUtils.mc2.field_71439_g.func_174813_aQ().field_72338_b + (double)RotationUtils.mc2.field_71439_g.func_70047_e(), RotationUtils.mc2.field_71439_g.field_70161_v);
        if (predict) {
            eyesPos.addVector(RotationUtils.mc2.field_71439_g.field_70159_w, RotationUtils.mc2.field_71439_g.field_70181_x, RotationUtils.mc2.field_71439_g.field_70179_y);
        }
        double diffX = vec.getXCoord() - eyesPos.getXCoord();
        double diffY = vec.getYCoord() - eyesPos.getYCoord();
        double diffZ = vec.getZCoord() - eyesPos.getZCoord();
        double hypotenuse = MathHelper.func_76133_a((double)(diffX * diffX + diffZ * diffZ));
        return new Rotation((float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f, (float)(-Math.atan2(diffY, hypotenuse) * 180.0 / Math.PI));
    }

    public static void setTargetRotation(Rotation rotation, int keepLength) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90.0f || rotation.getPitch() < -90.0f) {
            return;
        }
        rotation.fixedSensitivity(mc.getGameSettings().getMouseSensitivity());
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
    }

    public static void setTargetRotation(Rotation rotation) {
        RotationUtils.setTargetRotation(rotation, 0);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        IPacket packet = event.getPacket();
        if (classProvider.isCPacketPlayer(packet)) {
            ICPacketPlayer packetPlayer = packet.asCPacketPlayer();
            if (!(targetRotation == null || keepCurrentRotation || targetRotation.getYaw() == serverRotation.getYaw() && targetRotation.getPitch() == serverRotation.getPitch())) {
                packetPlayer.setYaw(targetRotation.getYaw());
                packetPlayer.setPitch(targetRotation.getPitch());
                packetPlayer.setRotating(true);
            }
            if (packetPlayer.isRotating()) {
                serverRotation = new Rotation(packetPlayer.getYaw(), packetPlayer.getPitch());
            }
        }
    }

    public static void reset() {
        keepLength = 0;
        targetRotation = null;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    static {
        serverRotation = new Rotation(0.0f, 0.0f);
        keepCurrentRotation = false;
        x = random.nextDouble();
        y = random.nextDouble();
        z = random.nextDouble();
    }
}

