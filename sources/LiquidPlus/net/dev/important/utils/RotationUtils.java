/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils;

import java.util.Random;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Listenable;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.TickEvent;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RaycastUtils;
import net.dev.important.utils.Rotation;
import net.dev.important.utils.VecRotation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(value=Side.CLIENT)
public final class RotationUtils
extends MinecraftInstance
implements Listenable {
    private static Random random = new Random();
    private static int keepLength;
    public static Rotation targetRotation;
    public static Rotation serverRotation;
    public static boolean keepCurrentRotation;
    private static double x;
    private static double y;
    private static double z;

    public static Rotation OtherRotation(AxisAlignedBB bb, Vec3 vec, boolean predict, boolean throughWalls, float distance) {
        Vec3 eyesPos = new Vec3(RotationUtils.mc.field_71439_g.field_70165_t, RotationUtils.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)RotationUtils.mc.field_71439_g.func_70047_e(), RotationUtils.mc.field_71439_g.field_70161_v);
        Vec3 eyes = RotationUtils.mc.field_71439_g.func_174824_e(1.0f);
        VecRotation vecRotation = null;
        for (double xSearch = 0.15; xSearch < 0.85; xSearch += 0.1) {
            for (double ySearch = 0.15; ySearch < 1.0; ySearch += 0.1) {
                for (double zSearch = 0.15; zSearch < 0.85; zSearch += 0.1) {
                    Vec3 vec3 = new Vec3(bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * xSearch, bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * ySearch, bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * zSearch);
                    Rotation rotation = RotationUtils.toRotation(vec3, predict);
                    double vecDist = eyes.func_72438_d(vec3);
                    if (vecDist > (double)distance || !throughWalls && !RotationUtils.isVisible(vec3)) continue;
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null) continue;
                    vecRotation = currentVec;
                }
            }
        }
        if (predict) {
            eyesPos.func_72441_c(RotationUtils.mc.field_71439_g.field_70159_w, RotationUtils.mc.field_71439_g.field_70181_x, RotationUtils.mc.field_71439_g.field_70179_y);
        }
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        return new Rotation(MathHelper.func_76142_g((float)((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f)), MathHelper.func_76142_g((float)((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))))));
    }

    public static VecRotation faceBlock(BlockPos blockPos) {
        if (blockPos == null) {
            return null;
        }
        VecRotation vecRotation = null;
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    Vec3 eyesPos = new Vec3(RotationUtils.mc.field_71439_g.field_70165_t, RotationUtils.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)RotationUtils.mc.field_71439_g.func_70047_e(), RotationUtils.mc.field_71439_g.field_70161_v);
                    Vec3 posVec = new Vec3((Vec3i)blockPos).func_72441_c(xSearch, ySearch, zSearch);
                    double dist = eyesPos.func_72438_d(posVec);
                    double diffX = posVec.field_72450_a - eyesPos.field_72450_a;
                    double diffY = posVec.field_72448_b - eyesPos.field_72448_b;
                    double diffZ = posVec.field_72449_c - eyesPos.field_72449_c;
                    double diffXZ = MathHelper.func_76133_a((double)(diffX * diffX + diffZ * diffZ));
                    Rotation rotation = new Rotation(MathHelper.func_76142_g((float)((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f)), MathHelper.func_76142_g((float)((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))))));
                    Vec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                    Vec3 vector = eyesPos.func_72441_c(rotationVector.field_72450_a * dist, rotationVector.field_72448_b * dist, rotationVector.field_72449_c * dist);
                    MovingObjectPosition obj = RotationUtils.mc.field_71441_e.func_147447_a(eyesPos, vector, false, false, true);
                    if (obj.field_72313_a != MovingObjectPosition.MovingObjectType.BLOCK) continue;
                    VecRotation currentVec = new VecRotation(posVec, rotation);
                    if (vecRotation != null && !(RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static void faceBow(Entity target, boolean silent, boolean predict, float predictSize) {
        EntityPlayerSP player = RotationUtils.mc.field_71439_g;
        double posX = target.field_70165_t + (predict ? (target.field_70165_t - target.field_70169_q) * (double)predictSize : 0.0) - (player.field_70165_t + (predict ? player.field_70165_t - player.field_70169_q : 0.0));
        double posY = target.func_174813_aQ().field_72338_b + (predict ? (target.func_174813_aQ().field_72338_b - target.field_70167_r) * (double)predictSize : 0.0) + (double)target.func_70047_e() - 0.15 - (player.func_174813_aQ().field_72338_b + (predict ? player.field_70163_u - player.field_70167_r : 0.0)) - (double)player.func_70047_e();
        double posZ = target.field_70161_v + (predict ? (target.field_70161_v - target.field_70166_s) * (double)predictSize : 0.0) - (player.field_70161_v + (predict ? player.field_70161_v - player.field_70166_s : 0.0));
        double posSqrt = Math.sqrt(posX * posX + posZ * posZ);
        float velocity = (float)player.func_71057_bx() / 20.0f;
        if ((velocity = (velocity * velocity + velocity * 2.0f) / 3.0f) > 1.0f) {
            velocity = 1.0f;
        }
        Rotation rotation = new Rotation((float)(Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f, (float)(-Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt((double)(velocity * velocity * velocity * velocity) - (double)0.006f * ((double)0.006f * (posSqrt * posSqrt) + 2.0 * posY * (double)(velocity * velocity)))) / ((double)0.006f * posSqrt)))));
        if (silent) {
            RotationUtils.setTargetRotation(rotation);
        } else {
            RotationUtils.limitAngleChange(new Rotation(player.field_70177_z, player.field_70125_A), rotation, 10 + new Random().nextInt(6)).toPlayer((EntityPlayer)RotationUtils.mc.field_71439_g);
        }
    }

    public static Rotation toRotation(Vec3 vec, boolean predict) {
        Vec3 eyesPos = new Vec3(RotationUtils.mc.field_71439_g.field_70165_t, RotationUtils.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)RotationUtils.mc.field_71439_g.func_70047_e(), RotationUtils.mc.field_71439_g.field_70161_v);
        if (predict) {
            eyesPos.func_72441_c(RotationUtils.mc.field_71439_g.field_70159_w, RotationUtils.mc.field_71439_g.field_70181_x, RotationUtils.mc.field_71439_g.field_70179_y);
        }
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        return new Rotation(MathHelper.func_76142_g((float)((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f)), MathHelper.func_76142_g((float)((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))))));
    }

    public static Vec3 getCenter(AxisAlignedBB bb) {
        return new Vec3(bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * 0.5, bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * 0.5, bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * 0.5);
    }

    public static VecRotation searchCenter(AxisAlignedBB bb, boolean outborder, boolean random, boolean predict, boolean throughWalls, float distance) {
        return RotationUtils.searchCenter(bb, outborder, random, predict, throughWalls, distance, 0.0f, false);
    }

    public static float[] getRotations2(EntityLivingBase ent) {
        double x = ent.field_70165_t;
        double z = ent.field_70161_v;
        double y = ent.field_70163_u + (double)(ent.func_70047_e() / 2.0f);
        return RotationUtils.getRotationFromPosition2(x, z, y);
    }

    public static float[] getRotationFromPosition2(double x, double z, double y) {
        double xDiff = x - Minecraft.func_71410_x().field_71439_g.field_70165_t;
        double zDiff = z - Minecraft.func_71410_x().field_71439_g.field_70161_v;
        double yDiff = y - Minecraft.func_71410_x().field_71439_g.field_70163_u - 1.2;
        double dist = MathHelper.func_76133_a((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float roundRotation(float yaw, int strength) {
        return Math.round(yaw / (float)strength) * strength;
    }

    public static VecRotation searchCenter(AxisAlignedBB bb, boolean outborder, boolean random, boolean predict, boolean throughWalls, float distance, float randomMultiply, boolean newRandom) {
        if (outborder) {
            Vec3 vec3 = new Vec3(bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * (x * 0.3 + 1.0), bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * (y * 0.3 + 1.0), bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * (z * 0.3 + 1.0));
            return new VecRotation(vec3, RotationUtils.toRotation(vec3, predict));
        }
        Vec3 randomVec = new Vec3(bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * x * (double)randomMultiply * (newRandom ? Math.random() : 1.0), bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * y * (double)randomMultiply * (newRandom ? Math.random() : 1.0), bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * z * (double)randomMultiply * (newRandom ? Math.random() : 1.0));
        Rotation randomRotation = RotationUtils.toRotation(randomVec, predict);
        Vec3 eyes = RotationUtils.mc.field_71439_g.func_174824_e(1.0f);
        VecRotation vecRotation = null;
        for (double xSearch = 0.15; xSearch < 0.85; xSearch += 0.1) {
            for (double ySearch = 0.15; ySearch < 1.0; ySearch += 0.1) {
                for (double zSearch = 0.15; zSearch < 0.85; zSearch += 0.1) {
                    Vec3 vec3 = new Vec3(bb.field_72340_a + (bb.field_72336_d - bb.field_72340_a) * xSearch, bb.field_72338_b + (bb.field_72337_e - bb.field_72338_b) * ySearch, bb.field_72339_c + (bb.field_72334_f - bb.field_72339_c) * zSearch);
                    Rotation rotation = RotationUtils.toRotation(vec3, predict);
                    double vecDist = eyes.func_72438_d(vec3);
                    if (vecDist > (double)distance || !throughWalls && !RotationUtils.isVisible(vec3)) continue;
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null && !(random ? RotationUtils.getRotationDifference(currentVec.getRotation(), randomRotation) < RotationUtils.getRotationDifference(vecRotation.getRotation(), randomRotation) : RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static double getRotationDifference(Entity entity) {
        Rotation rotation = RotationUtils.toRotation(RotationUtils.getCenter(entity.func_174813_aQ()), true);
        return RotationUtils.getRotationDifference(rotation, new Rotation(RotationUtils.mc.field_71439_g.field_70177_z, RotationUtils.mc.field_71439_g.field_70125_A));
    }

    public static double getRotationDifference(Rotation rotation) {
        return serverRotation == null ? 0.0 : RotationUtils.getRotationDifference(rotation, serverRotation);
    }

    public static double getRotationDifference(Rotation a, Rotation b) {
        return Math.hypot(RotationUtils.getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    @NotNull
    public static Rotation limitAngleChange(Rotation currentRotation, Rotation targetRotation, float turnSpeed) {
        float yawDifference = RotationUtils.getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        float pitchDifference = RotationUtils.getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        return new Rotation(currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
    }

    private static float getAngleDifference(float a, float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static Vec3 getVectorForRotation(Rotation rotation) {
        float yawCos = MathHelper.func_76134_b((float)(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI));
        float yawSin = MathHelper.func_76126_a((float)(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI));
        float pitchCos = -MathHelper.func_76134_b((float)(-rotation.getPitch() * ((float)Math.PI / 180)));
        float pitchSin = MathHelper.func_76126_a((float)(-rotation.getPitch() * ((float)Math.PI / 180)));
        return new Vec3((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
    }

    public static boolean isFaced(Entity targetEntity, double blockReachDistance) {
        return RaycastUtils.raycastEntity(blockReachDistance, entity -> entity == targetEntity) != null;
    }

    public static boolean isVisible(Vec3 vec3) {
        Vec3 eyesPos = new Vec3(RotationUtils.mc.field_71439_g.field_70165_t, RotationUtils.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)RotationUtils.mc.field_71439_g.func_70047_e(), RotationUtils.mc.field_71439_g.field_70161_v);
        return RotationUtils.mc.field_71441_e.func_72933_a(eyesPos, vec3) == null;
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

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            if (!(targetRotation == null || keepCurrentRotation || targetRotation.getYaw() == serverRotation.getYaw() && targetRotation.getPitch() == serverRotation.getPitch())) {
                packetPlayer.field_149476_e = targetRotation.getYaw();
                packetPlayer.field_149473_f = targetRotation.getPitch();
                packetPlayer.field_149481_i = true;
            }
            if (packetPlayer.field_149481_i) {
                serverRotation = new Rotation(packetPlayer.field_149476_e, packetPlayer.field_149473_f);
            }
        }
    }

    public static void setTargetRotation(Rotation rotation) {
        RotationUtils.setTargetRotation(rotation, 0);
    }

    public static void setTargetRotation(Rotation rotation, int keepLength) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90.0f || rotation.getPitch() < -90.0f) {
            return;
        }
        rotation.fixedSensitivity(RotationUtils.mc.field_71474_y.field_74341_c);
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
    }

    public static void reset() {
        keepLength = 0;
        targetRotation = null;
    }

    public static Rotation getRotationsEntity(EntityLivingBase entity) {
        return RotationUtils.getRotations(entity.field_70165_t, entity.field_70163_u + (double)entity.func_70047_e() - 0.4, entity.field_70161_v);
    }

    public static Rotation getRotations(Entity ent) {
        double x = ent.field_70165_t;
        double z = ent.field_70161_v;
        double y = ent.field_70163_u + (double)(ent.func_70047_e() / 2.0f);
        return RotationUtils.getRotationFromPosition(x, z, y);
    }

    public static Rotation getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = RotationUtils.mc.field_71439_g;
        double x = posX - player.field_70165_t;
        double y = posY - (player.field_70163_u + (double)player.func_70047_e());
        double z = posZ - player.field_70161_v;
        double dist = MathHelper.func_76133_a((double)(x * x + z * z));
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - RotationUtils.mc.field_71439_g.field_70165_t;
        double zDiff = z - RotationUtils.mc.field_71439_g.field_70161_v;
        double yDiff = y - RotationUtils.mc.field_71439_g.field_70163_u - 1.2;
        double dist = MathHelper.func_76133_a((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0 / Math.PI);
        return new Rotation(yaw, pitch);
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

