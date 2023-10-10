package rosegoldclient.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.PlayerMoveEvent;

public class RotationUtils {

    private static float pitchDifference;
    public static float yawDifference;
    private static int ticks = -1;
    private static int tickCounter = 0;
    private static Runnable callback = null;

    private static float serverPitch;
    private static float serverYaw;

    public static class Rotation {
        public float pitch;
        public float yaw;

        public Rotation(float pitch, float yaw) {
            this.pitch = pitch;
            this.yaw = yaw;
        }
    }

    private static double wrapAngleTo180(double angle) {
        return angle - Math.floor(angle / 360 + 0.5) * 360;
    }

    private static float wrapAngleTo180(float angle) {
        return (float) (angle - Math.floor(angle / 360 + 0.5) * 360);
    }

    public static Rotation getRotationToBlock(BlockPos block) {
        double diffX = block.getX() - Main.mc.player.posX + 0.5;
        double diffY = block.getY() - Main.mc.player.posY + 0.5 - Main.mc.player.getEyeHeight();
        double diffZ = block.getZ() - Main.mc.player.posZ + 0.5;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90)*-1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }

    public static Rotation getBowRotationToEntity(Entity entity) {
        double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double dist = Main.mc.player.getDistance(entity);
        dist -= dist % 0.8;
        double xMulti = dist / 0.8 * xDelta;
        double zMulti = dist / 0.8 * zDelta;
        double x = entity.posX + xMulti - Main.mc.player.posX;
        double z = entity.posZ + zMulti - Main.mc.player.posZ;
        double y = Main.mc.player.posY + (double)Main.mc.player.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
        float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        double d1 = MathHelper.sqrt(x * x + z * z);
        float pitch = (float)(-(Math.atan2(y, d1) * 180.0 / Math.PI)) + (float)dist * 0.11f;
        return new Rotation(-pitch, yaw);
    }

    public static Rotation getRotationToEntity(Entity entity) {
        double diffX = entity.posX - Main.mc.player.posX;
        double diffY = entity.posY + entity.getEyeHeight() - Main.mc.player.posY - Main.mc.player.getEyeHeight() - 1;
        double diffZ = entity.posZ - Main.mc.player.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90)*-1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }

    public static Rotation getRotationToEntityFeet(Entity entity) {
        double diffX = entity.posX - Main.mc.player.posX;
        double diffY = entity.posY - Main.mc.player.posY - Main.mc.player.getEyeHeight();
        double diffZ = entity.posZ - Main.mc.player.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90)*-1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }

    public static Rotation getRotationToBlockUnderEntity(Entity entity) {
        BlockPos entityPos = entity.getPosition();
        for(int y = entityPos.getY() + 2; y > 0; y--) {
            BlockPos bp = new BlockPos(entityPos.getX(), y, entityPos.getZ());
            Block block = Main.mc.world.getBlockState(bp).getBlock();
            if(block == Blocks.AIR) continue;
            return getRotationToBlock(bp);
        }
        return null;
    }

    public static Rotation vec3ToRotation(Vec3d vec) {
        double diffX = vec.x - Main.mc.player.posX;
        double diffY = vec.y - Main.mc.player.posY - Main.mc.player.getEyeHeight();
        double diffZ = vec.z - Main.mc.player.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float pitch = (float) -Math.atan2(dist, diffY);
        float yaw = (float) Math.atan2(diffZ, diffX);
        pitch = (float) wrapAngleTo180((pitch * 180F / Math.PI + 90)*-1);
        yaw = (float) wrapAngleTo180((yaw * 180 / Math.PI) - 90);

        return new Rotation(pitch, yaw);
    }

    public static void smoothLook(Rotation rotation, int ticks, Runnable callback) {
        if(ticks == 0) {
            look(rotation);
            callback.run();
            return;
        }

        RotationUtils.callback = callback;

        pitchDifference = wrapAngleTo180(rotation.pitch - Main.mc.player.rotationPitch);
        yawDifference = wrapAngleTo180(rotation.yaw - Main.mc.player.rotationYaw);

        RotationUtils.ticks = ticks * 20;
        RotationUtils.tickCounter = 0;
    }

    public static void look(Rotation rotation) {
        Main.mc.player.rotationPitch = rotation.pitch;
        if(rotation.pitch > -80 && rotation.pitch < 80) {
            Main.mc.player.rotationYaw = rotation.yaw;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdatePre(PlayerMoveEvent.Pre pre) {
        serverPitch = Main.mc.player.rotationPitch;
        serverYaw = Main.mc.player.rotationYaw;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdatePost(PlayerMoveEvent.Post post) {
        Main.mc.player.rotationPitch = serverPitch;
        Main.mc.player.rotationYaw = serverYaw;
    }
}
