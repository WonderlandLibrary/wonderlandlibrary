package net.smoothboot.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.smoothboot.client.Client;
import net.smoothboot.client.mixinterface.IClientPlayerEntity;

public enum RotationUtil {
;

    private static MinecraftClient mc = MinecraftClient.getInstance();
    public static Vec3d getEyesPos()
    {
        ClientPlayerEntity player = mc.player;

        return new Vec3d(player.getX(),
                player.getY() + player.getEyeHeight(player.getPose()),
                player.getZ());
    }
    public static Vec3d getEyesPos(PlayerEntity player)
    {
        return new Vec3d(player.getX(),
                player.getY() + player.getEyeHeight(player.getPose()),
                player.getZ());
    }

    public static Vec3d getClientLookVec()
    {
        ClientPlayerEntity player = mc.player;
        float f = 0.017453292F;
        float pi = (float)Math.PI;

        float f1 = MathHelper.cos(-player.getYaw() * f - pi);
        float f2 = MathHelper.sin(-player.getYaw() * f - pi);
        float f3 = -MathHelper.cos(-player.getPitch() * f);
        float f4 = MathHelper.sin(-player.getPitch() * f);

        return new Vec3d(f2 * f3, f4, f1 * f3);
    }

    public static Vec3d getClientLookVec(float partialTicks)
    {
        ClientPlayerEntity player = mc.player;
        float f = 0.017453292F;
        float pi = (float)Math.PI;

        float f1 = MathHelper.cos(-player.getYaw(partialTicks) * f - pi);
        float f2 = MathHelper.sin(-player.getYaw(partialTicks) * f - pi);
        float f3 = -MathHelper.cos(-player.getPitch(partialTicks) * f);
        float f4 = MathHelper.sin(-player.getPitch(partialTicks) * f);

        return new Vec3d(f2 * f3, f4, f1 * f3);
    }


    public static Vec3d getServerLookVec()
    {
        RotationFaker rotationFaker = Client.INSTANCE.getRotationFaker();
        float serverYaw = rotationFaker.getServerYaw();
        float serverPitch = rotationFaker.getServerPitch();

        float f = MathHelper.cos(-serverYaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-serverYaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-serverPitch * 0.017453292F);
        float f3 = MathHelper.sin(-serverPitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static float limitAngleChange(float current, float intended,
                                         float maxChange)
    {
        float currentWrapped = MathHelper.wrapDegrees(current);
        float intendedWrapped = MathHelper.wrapDegrees(intended);

        float change = MathHelper.wrapDegrees(intendedWrapped - currentWrapped);
        change = MathHelper.clamp(change, -maxChange, maxChange);

        return current + change;
    }

    public static Rotation slowlyTurnTowards(Rotation end, float maxChange)
    {
        Entity player = mc.player;
        float startYaw = player.prevYaw;
        float startPitch = player.prevPitch;
        float endYaw = end.getYaw();
        float endPitch = end.getPitch();

        float yawChange = Math.abs(MathHelper.wrapDegrees(endYaw - startYaw));
        float pitchChange =
                Math.abs(MathHelper.wrapDegrees(endPitch - startPitch));

        float maxChangeYaw =
                Math.min(maxChange, maxChange * yawChange / pitchChange);
        float maxChangePitch =
                Math.min(maxChange, maxChange * pitchChange / yawChange);

        float nextYaw = limitAngleChange(startYaw, endYaw, maxChangeYaw);
        float nextPitch =
                limitAngleChange(startPitch, endPitch, maxChangePitch);

        return new Rotation(nextYaw, nextPitch);
    }


    public static Vec3d getPlayerLookVec(PlayerEntity player)
    {
        float f = 0.017453292F;
        float pi = (float)Math.PI;

        float f1 = MathHelper.cos(-player.getYaw() * f - pi);
        float f2 = MathHelper.sin(-player.getYaw() * f - pi);
        float f3 = -MathHelper.cos(-player.getPitch() * f);
        float f4 = MathHelper.sin(-player.getPitch() * f);

        return new Vec3d(f2 * f3, f4, f1 * f3).normalize();
    }

    public static boolean isFacingBox(Box box, double range)
    {
        Vec3d start = getEyesPos();
        Vec3d end = start.add(getServerLookVec().multiply(range));
        return box.raycast(start, end).isPresent();
    }
    public static double getAngleToLastReportedLookVec(Vec3d vec)
    {
        Rotation needed = getNeededRotations(vec);

        IClientPlayerEntity player = Client.imc.getPlayer();
        float lastReportedYaw = MathHelper.wrapDegrees(player.getLastYaw());
        float lastReportedPitch = MathHelper.wrapDegrees(player.getLastPitch());

        float diffYaw = MathHelper.wrapDegrees(lastReportedYaw - needed.yaw);
        float diffPitch =
                MathHelper.wrapDegrees(lastReportedPitch - needed.pitch);

        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }

    public static double getAngleToLastReportedLookVec(Rotation rotation)
    {
        float yaw = MathHelper.wrapDegrees(rotation.getYaw());
        float pitch = MathHelper.wrapDegrees(rotation.getPitch());

        IClientPlayerEntity player = Client.imc.getPlayer();
        float lastReportedYaw = MathHelper.wrapDegrees(player.getLastYaw());
        float lastReportedPitch = MathHelper.wrapDegrees(player.getLastPitch());

        float diffYaw = MathHelper.wrapDegrees(lastReportedYaw - yaw);
        float diffPitch = MathHelper.wrapDegrees(lastReportedPitch - pitch);

        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }


    public static boolean isAlreadyFacing(Rotation rotation)
    {
        return getAngleToLastReportedLookVec(rotation) <= 1.0;
    }

    public static final class Rotation
    {
        private final float yaw;
        private final float pitch;

        public Rotation(float yaw, float pitch)
        {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public static Rotation wrapped(float yaw, float pitch)
        {
            return new Rotation(MathHelper.wrapDegrees(yaw),
                    MathHelper.wrapDegrees(pitch));
        }

        public float getYaw()
        {
            return yaw;
        }

        public float getPitch()
        {
            return pitch;
        }
    }

    public static Rotation getNeededRotations(Vec3d vec)
    {
        Vec3d eyesPos = getEyesPos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));

        return Rotation.wrapped(yaw, pitch);
    }
    public static double getAngleToLookVec(Vec3d vec)
    {
        Rotation needed = getNeededRotations(vec);

        ClientPlayerEntity player = mc.player;
        float currentYaw = MathHelper.wrapDegrees(player.getYaw());
        float currentPitch = MathHelper.wrapDegrees(player.getPitch());

        float diffYaw = MathHelper.wrapDegrees(currentYaw - needed.yaw);
        float diffPitch = MathHelper.wrapDegrees(currentPitch - needed.pitch);

        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }
}
