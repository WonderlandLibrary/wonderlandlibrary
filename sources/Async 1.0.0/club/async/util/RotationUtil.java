package club.async.util;

import club.async.interfaces.MinecraftInterface;

import club.async.module.impl.combat.KillAura;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;

public final class RotationUtil implements MinecraftInterface {

    public static float[] getRotations(EntityLivingBase entityLivingBase, boolean useBestVec, boolean predictRotations) {
        Vec3 playerPos;
        double predictValue = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() / 50;
        if (predictRotations) {
            playerPos = new Vec3((entityLivingBase.posX + (entityLivingBase.posX - entityLivingBase.prevPosX) * predictValue),entityLivingBase.posY + ((entityLivingBase.posY - entityLivingBase.prevPosY) * predictValue) ,entityLivingBase.posZ + ((entityLivingBase.posZ - entityLivingBase.prevPosZ) * predictValue));
        } else {
            playerPos = new Vec3(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ);
        }
        Vec3 bestVec = null;
        if (useBestVec) {
            bestVec = getBestVec(playerPos, entityLivingBase);
            if (bestVec == null)
                return null;
        }
        Vec3 eyeHeight = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        double xDiff = (useBestVec ? bestVec.xCoord : playerPos.xCoord) - eyeHeight.xCoord;
        double zDiff = (useBestVec ? bestVec.zCoord : playerPos.zCoord) - eyeHeight.zCoord;
        double yDiff = (useBestVec ? bestVec.yCoord : playerPos.yCoord + entityLivingBase.height - 0.2) - eyeHeight.yCoord;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
        if(KillAura.getInstance().randomRotations.get()) {
            yaw += RandomUtil.getRandomNumber(-0.15F, 0.15F);
            pitch += RandomUtil.getRandomNumber(-0.15F, 0.15F);
        }
        return new float[] { yaw, MathHelper.clamp_float(pitch, -90, 90) };
    }

    public static float[] getRotations(BlockPos blockPos, EnumFacing face) {
        double d0 = blockPos.getX() - mc.thePlayer.posX + (double) face.getFrontOffsetX() / 2 + 0.5D;
        double d1 = blockPos.getY() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()) + 0.5D;
        double d2 = blockPos.getZ() - mc.thePlayer.posZ + (double) face.getFrontOffsetZ() / 2 + 0.5D;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float)(-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI));

        return new float[] {f, MathHelper.clamp_float(f1, -90, 90)};
    }

    public static float[] fixSensi(float[] rotations) {
        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float sens = (f * f * f * 8.0F) * 0.15F;
        float yaw = rotations[0], pitch = rotations[1];
        float yawGCD = (Math.round(yaw / sens) * sens);
        float pitchGCD = (Math.round(pitch / sens) * sens);
        pitchGCD = MathHelper.clamp_float(pitchGCD, -90.0F, 90.0F);
        return new float[]{yawGCD, pitchGCD};
    }

    public static Vec3 getBestVec(Vec3 playerPos, EntityLivingBase entityLivingBase) {
        Vec3 bestVec = null;
        Vec3 eyeHeight = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        double offset = 0.1;
        for (double x = playerPos.xCoord - entityLivingBase.width / 2; x <= playerPos.xCoord + entityLivingBase.width / 2; x += offset) {
            for (double y = playerPos.yCoord; y <= playerPos.yCoord + entityLivingBase.height; y += offset) {
                for (double z = playerPos.zCoord - entityLivingBase.width / 2; z <= playerPos.zCoord + entityLivingBase.width / 2; z += offset) {
                    Vec3 vec3 = new Vec3(x,y,z);
                    if ((bestVec == null || vec3.distanceTo(eyeHeight) < bestVec.distanceTo(eyeHeight)) && mc.theWorld.rayTraceBlocks(eyeHeight, vec3, false,true,false) == null)
                        bestVec = vec3;
                }
            }
        }
        if (bestVec == null)
            return null;
        return bestVec;
    }

}
