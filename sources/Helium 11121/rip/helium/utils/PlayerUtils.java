package rip.helium.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class PlayerUtils {

    private final static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isInLiquid() {
        final double x = mc.thePlayer.posX;
        final double y = mc.thePlayer.getEntityBoundingBox().minY;
        final double z = mc.thePlayer.posZ;

        if (mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockLiquid) {
            return true;
        }

        return mc.theWorld.getBlockState(new BlockPos(x, y + mc.thePlayer.getEyeHeight(), z))
                .getBlock() instanceof BlockLiquid;
    }

    public static void damage() {
        final double offset = 0.055D;
        for (int i = 0; i < (getMaxFallDist() / (offset - 0.005D)) + 1; i++) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 0.005D, mc.thePlayer.posZ, false));
        }
    }

    public static void damageHypixel() {
        if (mc.thePlayer.onGround) {
            final double offset = 0.4122222218322211111111F;
            final NetHandlerPlayClient netHandler = mc.getNetHandler();
            final EntityPlayerSP player = mc.thePlayer;
            final double x = player.posX;
            final double y = player.posY;
            final double z = player.posZ;
            for (int i = 0; i < 9; i++) {
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + offset, z, false));
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.000002737272, z, false));
                netHandler.addToSendQueue(new C03PacketPlayer(false));
            }
            netHandler.addToSendQueue(new C03PacketPlayer(true));
        }
    }

    public static boolean isOnLiquid() {
        final AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0, -0.01, 0.0)
                .contract(0.001, 0.001, 0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        final Vec3 var10 = new Vec3(0.0, 0.0, 0.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final Block var14 = Minecraft.getMinecraft().theWorld.getBlock(var11, var12, var13);
                    if (!(var14 instanceof BlockAir) && !(var14 instanceof BlockLiquid)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static float getMaxFallDist() {
        final PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? (potioneffect.getAmplifier() + 1) : 0;
        return mc.thePlayer.getMaxFallHeight() + f;
    }

    public static boolean isOnSameTeam(EntityLivingBase entity) {
        if (entity.getTeam() != null && mc.thePlayer.getTeam() != null) {
            char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            char c2 = mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }

        return false;
    }

    public static boolean isBlockUnder() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;
        double yDiff = y - mc.thePlayer.posY - 1.2D;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0D / Math.PI));
        return new float[]{yaw, pitch};
    }

    /**
     * @param entity entity to get yaw change from current yaw.
     * @return yaw difference to get entity's yaw on your screen.
     */
    public static float getYawChange(EntityLivingBase entity) {
        double posX = entity.posX;
        double posZ = entity.posZ;
        double deltaX = posX - mc.thePlayer.posX;
        double deltaZ = posZ - mc.thePlayer.posZ;
        double yawToEntity;
        double degrees = Math.toDegrees(Math.atan(deltaZ / deltaX));

        if (deltaZ < 0.0D && deltaX < 0.0D) {
            yawToEntity = 90.0D + degrees;
        } else if (deltaZ < 0.0D && deltaX > 0.0D) {
            yawToEntity = -90.0D + degrees;
        } else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }

        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
    }

    /**
     * @param entityIn The entity to get rotations to.
     * @return float[] containing yaw as first element and pitch as second.
     */
    public static float[] getNeededRotations(EntityLivingBase entityIn) {
        double d0 = entityIn.posX - mc.thePlayer.posX;
        double d1 = entityIn.posZ - mc.thePlayer.posZ;
        double d2 = entityIn.posY + entityIn.getEyeHeight() - (mc.thePlayer.getEntityBoundingBox().minY
                + (mc.thePlayer.getEntityBoundingBox().maxY - mc.thePlayer.getEntityBoundingBox().minY));
        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f = (float) (MathHelper.func_181159_b(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float) (-(MathHelper.func_181159_b(d2, d3) * 180.0D / Math.PI));
        return new float[]{f, f1};
    }

    /**
     * @param entityIn The entity to get rotations to.
     * @param speed    Speed at which you will rotate.
     * @return Rotations to entityIn incremented by speed.
     */
    public static float[] getRotations(EntityLivingBase entityIn, float speed) {
        float yaw = incrementRotation(mc.thePlayer.rotationYaw, getNeededRotations(entityIn)[0], speed);
        float pitch = incrementRotation(mc.thePlayer.rotationPitch, getNeededRotations(entityIn)[1], speed);
        return new float[]{yaw, pitch};
    }

    private static float incrementRotation(float currentRotation, float intendedRotation, float increment) {
        float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);

        if (f > increment) {
            f = increment;
        }

        if (f < -increment) {
            f = -increment;
        }

        return currentRotation + f;
    }

    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (float) (amplifier + 1) * 0.1F;
        }

        return baseJumpHeight;
    }

}
