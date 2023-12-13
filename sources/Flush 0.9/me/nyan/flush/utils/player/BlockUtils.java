package me.nyan.flush.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float[] getDirectionToBlock(int x, int y, int z, EnumFacing facing) {
        EntityEgg entity = new EntityEgg(mc.theWorld);

        entity.posX = (double) x + 0.5D;
        entity.posY = (double) y + 0.5D;
        entity.posZ = (double) z + 0.5D;
        entity.posX += (double) facing.getDirectionVec().getX() * 0.25D;
        entity.posY += (double) facing.getDirectionVec().getY() * 0.25D;
        entity.posZ += (double) facing.getDirectionVec().getZ() * 0.25D;
        return getDirectionToEntity(entity);
    }

    private static float[] getDirectionToEntity(Entity entity) {
        return new float[]{getYaw(entity) + mc.thePlayer.rotationYaw, getPitch(entity) + mc.thePlayer.rotationPitch};
    }

    public static float[] getRotationNeededForBlock(EntityPlayer entity, BlockPos pos) {
        double x = pos.getX() - entity.posX;
        double y = pos.getY() + 0.5 - (entity.posY + entity.getEyeHeight());
        double z = pos.getZ() - entity.posZ;
        double d4 = Math.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(y, d4) * 180.0D / Math.PI);
        return new float[] {yaw, pitch};
    }

    public static float getYaw(Entity entity) {
        double x = entity.posX - mc.thePlayer.posX;
        double z = entity.posZ - mc.thePlayer.posZ;
        double yaw;

        if (z < 0.0D && x < 0.0D) {
            yaw = 90.0D + Math.toDegrees(Math.atan(z / x));
        } else if (z < 0.0D && x > 0.0D) {
            yaw = -90.0D + Math.toDegrees(Math.atan(z / x));
        } else {
            yaw = Math.toDegrees(-Math.atan(x / z));
        }

        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yaw));
    }

    public static float getPitch(Entity var0) {
        double x = var0.posX - mc.thePlayer.posX;
        double y = var0.posY - 1.6D + (double) var0.getEyeHeight() - mc.thePlayer.posY;
        double z = var0.posZ - mc.thePlayer.posZ;
        double pitch = -Math.toDegrees(Math.atan(y / MathHelper.sqrt_double(x * x + z * z)));

        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitch);
    }
}