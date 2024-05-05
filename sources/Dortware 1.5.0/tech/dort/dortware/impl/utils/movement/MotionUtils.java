package tech.dort.dortware.impl.utils.movement;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import org.apache.commons.lang3.RandomUtils;
import skidmonke.Client;
import tech.dort.dortware.api.util.Util;
import tech.dort.dortware.impl.events.MovementEvent;
import tech.dort.dortware.impl.modules.combat.KillAura;
import tech.dort.dortware.impl.modules.combat.TargetStrafe;
import tech.dort.dortware.impl.modules.movement.Flight;
import tech.dort.dortware.impl.utils.combat.AimUtil;
import tech.dort.dortware.impl.utils.networking.PacketUtil;

import javax.vecmath.Vector2d;

public final class MotionUtils implements Util {

    /**
     * Attempts to damage the user via fall damage.
     *
     * @param groundCheck - if true, you will need to be on the ground for this method to complete successfully
     */
    public static void damagePlayer(final boolean groundCheck) {
        if (!groundCheck || mc.thePlayer.onGround) {
            final double x = mc.thePlayer.posX;
            final double y = mc.thePlayer.posY;
            final double z = mc.thePlayer.posZ;

            double fallDistanceReq = 3;

            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
                fallDistanceReq += (float) (amplifier + 1);
            }

            Flight flight = Client.INSTANCE.getModuleManager().get(Flight.class);
            switch (flight.damageMode.getValue()) {
                case LILYPAD: {
                    final int packetCount = (int) Math.ceil(fallDistanceReq / 0.015624);
                    for (int i = 0; i < packetCount; i++) {
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.015624 * RandomUtils.nextFloat(1.0F, 1.0005F), z, false));
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    }
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                }
                break;

                case CARPET: {
                    final int packetCount = (int) Math.ceil(fallDistanceReq / 0.0624);
                    for (int i = 0; i < packetCount; i++) {
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625 * RandomUtils.nextFloat(0.99F, 0.999F), z, false));
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    }
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                }
                break;

                case HIGH: {
                    final int packetCount = (int) Math.ceil(fallDistanceReq / 0.124);
                    for (int i = 0; i < packetCount; i++) {
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.125, z, false));
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    }
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                }
                break;

                case JUMP: {
                    final int packetCount = (int) Math.ceil(fallDistanceReq / 0.42F);
                    for (int i = 0; i < packetCount; i++) {
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.42F, z, false));
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    }
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                }
                break;

                case SLAB: {
                    final int packetCount = (int) Math.ceil(fallDistanceReq / 0.49F);
                    for (int i = 0; i < packetCount; i++) {
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.5F, z, false));
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    }
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
                }
                break;
            }
        }
    }

    public static boolean isOnWater() {
        return mc.thePlayer.isCollidedVertically && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer)).getBlock() instanceof BlockLiquid;
    }

    public static boolean isBlockUnder() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }

    public static double getMotion(float baseMotionY) {
        Potion potion = Potion.jump;
        if (mc.thePlayer.isPotionActive(potion)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
            baseMotionY += (amplifier + 1) * 0.1F;
        }

        return baseMotionY;
    }

    public static double getMotion(ACType antiCheatType) {
        Potion potion = Potion.jump;
        double baseMotionY = 0.42F;

        switch (antiCheatType) {
            case NCP:
                baseMotionY = 0.4F;
                break;
            case SLOTH:
                baseMotionY = 0.346;
                break;
            case AAC:
                baseMotionY = 0.425f;
                break;
            case MMC:
                baseMotionY = 0.34F;
                break;
        }

        if (mc.thePlayer.isPotionActive(potion)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
            baseMotionY += (amplifier + 1) * 0.1F;
        }

        return baseMotionY;
    }

    /**
     * @author aristhena
     */

    public static void setMotion(MovementEvent event, double moveSpeed) {
        EntityLivingBase entity = KillAura.currentTarget;
        TargetStrafe targetStrafeClass = Client.INSTANCE.getModuleManager().get(TargetStrafe.class);
        boolean targetStrafe = TargetStrafe.canStrafe();
        MovementInput movementInput = mc.thePlayer.movementInput;

        double moveForward = targetStrafe ? mc.thePlayer.getDistanceToEntity(entity) <= targetStrafeClass.range.getValue() ? 0 : 1 : movementInput.moveForward;
        double moveStrafe = targetStrafe ? TargetStrafe.dir : movementInput.moveStrafe;

        double rotationYaw = targetStrafe ? AimUtil.getRotationsRandom(entity).getRotationYaw() : mc.thePlayer.rotationYaw;

        event.setStrafeSpeed(moveSpeed);

        if (moveForward == 0.0D && moveStrafe == 0.0D) {
            event.setMotionX(0);
            event.setMotionZ(0);
        } else {
            if (moveStrafe > 0) {
                moveStrafe = 1;
            } else if (moveStrafe < 0) {
                moveStrafe = -1;
            }
            if (moveForward != 0.0D) {
                if (moveStrafe > 0.0D) {
                    rotationYaw += moveForward > 0.0D ? -45 : 45;
                } else if (moveStrafe < 0.0D) {
                    rotationYaw += moveForward > 0.0D ? 45 : -45;
                }
                moveStrafe = 0.0D;
                if (moveForward > 0.0D) {
                    moveForward = 1.0D;
                } else if (moveForward < 0.0D) {
                    moveForward = -1.0D;
                }
            }
            double cos = Math.cos(Math.toRadians(rotationYaw + 90.0F));
            double sin = Math.sin(Math.toRadians(rotationYaw + 90.0F));
            event.setMotionX(moveForward * moveSpeed * cos
                    + moveStrafe * moveSpeed * sin);
            event.setMotionZ(moveForward * moveSpeed * sin
                    - moveStrafe * moveSpeed * cos);
        }
    }

    public static void setMotion(double moveSpeed) {
        EntityLivingBase entity = KillAura.currentTarget;
        TargetStrafe targetStrafeClass = Client.INSTANCE.getModuleManager().get(TargetStrafe.class);
        boolean targetStrafe = TargetStrafe.canStrafe();
        MovementInput movementInput = mc.thePlayer.movementInput;

        double moveForward = targetStrafe ? mc.thePlayer.getDistanceToEntity(entity) <= targetStrafeClass.range.getValue() ? 0 : 1 : movementInput.moveForward;
        double moveStrafe = targetStrafe ? TargetStrafe.dir : movementInput.moveStrafe;
        double rotationYaw = targetStrafe ? AimUtil.getRotationsRandom(entity).getRotationYaw() : mc.thePlayer.rotationYaw;

        if (moveForward == 0.0D && moveStrafe == 0.0D) {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        } else {
            if (moveStrafe > 0) {
                moveStrafe = 1;
            } else if (moveStrafe < 0) {
                moveStrafe = -1;
            }
            if (moveForward != 0.0D) {
                if (moveStrafe > 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? -45 : 45);
                } else if (moveStrafe < 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? 45 : -45);
                }
                moveStrafe = 0.0D;
                if (moveForward > 0.0D) {
                    moveForward = 1.0D;
                } else if (moveForward < 0.0D) {
                    moveForward = -1.0D;
                }
            }
            rotationYaw *= 0.995;
            double cos = Math.cos(Math.toRadians(rotationYaw + 90.0F));
            double sin = Math.sin(Math.toRadians(rotationYaw + 90.0F));
            mc.thePlayer.motionX = moveForward * moveSpeed * cos
                    + moveStrafe * moveSpeed * sin;
            mc.thePlayer.motionZ = moveForward * moveSpeed * sin
                    - moveStrafe * moveSpeed * cos;
        }
    }

    /**
     * Gets the movement speed of the {@code EntityPlayerSP}
     *
     * @return the base speed
     */
    public static double getSpeed() {
        double baseSpeed = 0.2873;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
            baseSpeed *= 1 + 0.2 * amp;
        }

        return baseSpeed;
    }

    public static float getStaticSpeed() {
        return 0.2873F;
    }

    public static double[] teleportForward(final double speed) {
        final float forward = 1.0F;
        final float side = 0;
        final float yaw = mc.thePlayer.prevRotationYaw + (mc.thePlayer.rotationYaw - mc.thePlayer.prevRotationYaw) * mc.timer.renderPartialTicks;
        final double sin = Math.sin(Math.toRadians(yaw + 90.0F));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0F));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[]{posX, posZ};
    }

    /**
     * Gets the movement speed of the {@code EntityPlayerSP}
     *
     * @return the base speed
     */
    public static double getBaseSpeed(ACType value) {
        double val;
        double potVal;
        switch (value) {
            case MINEPLEX:
                val = 0.2873;
                potVal = 0.3;
                break;
            case MMC:
                val = 0.24;
                potVal = 0.0999;
                break;
            case VERUS:
                val = mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
                potVal = 0.2;
                break;
            case AAC:
                val = 0.34;
                potVal = 0.1525;
                break;
            case OLD_AGC:
                val = 0.24D;
                potVal = 0.0535;
                break;
            case SLOTH:
                val = 0.4873;
                potVal = 0.305;
                break;
            case HYPIXEL:
                val = 0.2873D;
                potVal = 0.21D;
                break;
            case HYPIXEL_RANKED:
                val = 0.2873D;
                potVal = 0.16D;
                break;
            case HYPIXEL_GROUND:
                val = 0.2873;
                potVal = 0.19;
                break;
            default:
                val = 0.2873;
                potVal = 0.2;
                break;
        }
        double baseSpeed = val;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
            baseSpeed *= 1 + potVal * amp;
        }

        return baseSpeed;
    }

    public static void sendPosition(double x, double y, double z, boolean ground) {
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z, ground));
    }

    public static Vector2d getMotion(double moveSpeed) {
        EntityLivingBase entity = KillAura.currentTarget;
        TargetStrafe targetStrafeClass = Client.INSTANCE.getModuleManager().get(TargetStrafe.class);
        boolean targetStrafe = TargetStrafe.canStrafe();
        MovementInput movementInput = mc.thePlayer.movementInput;

        double moveForward = targetStrafe ? mc.thePlayer.getDistanceToEntity(entity) <= targetStrafeClass.range.getValue() ? 0 : 1 : movementInput.moveForward;
        double moveStrafe = targetStrafe ? TargetStrafe.dir : movementInput.moveStrafe;

        double rotationYaw = targetStrafe ? AimUtil.getRotationsRandom(entity).getRotationYaw() : mc.thePlayer.rotationYaw;
        if (moveForward != 0.0D || moveStrafe != 0.0D) {
            if (moveStrafe > 0) {
                moveStrafe = 1;
            } else if (moveStrafe < 0) {
                moveStrafe = -1;
            }
            if (moveForward != 0.0D) {
                if (moveStrafe > 0.0D) {
                    rotationYaw += moveForward > 0.0D ? -45 : 45;
                } else if (moveStrafe < 0.0D) {
                    rotationYaw += moveForward > 0.0D ? 45 : -45;
                }
                moveStrafe = 0.0D;
                if (moveForward > 0.0D) {
                    moveForward = 1.0D;
                } else if (moveForward < 0.0D) {
                    moveForward = -1.0D;
                }
            }
            rotationYaw *= 0.995;
            double cos = Math.cos(Math.toRadians(rotationYaw + 90.0F));
            double sin = Math.sin(Math.toRadians(rotationYaw + 90.0F));
            return new Vector2d(moveForward * moveSpeed * cos
                    + moveStrafe * moveSpeed * sin, moveForward * moveSpeed * sin
                    - moveStrafe * moveSpeed * cos);
        }
        return new Vector2d(0, 0);
    }

    public static void sendMotion(double speed, double dist) {
        Vector2d motion;
        final double x = mc.thePlayer.posX;
        final double z = mc.thePlayer.posZ;
        double d = dist;
        while (d < speed) {
            if (d > speed) {
                d = speed;
            }
            motion = getMotion(d);
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            mc.thePlayer.setPosition(x + motion.x, mc.thePlayer.posY, z + motion.y);
            d += dist;
        }
    }

    public static void setYPacket(double motionY, float motionYSpeed, float motionYSpeedNext) {
        double d = 0;
        if (motionY <= 0) {
            return;
        }
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        while (d < motionY) {
            d += motionYSpeed;
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            d += motionYSpeedNext;
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        }
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ);
    }
}