package dev.rise.util.player;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

/**
 * DamageUtil, used to damage yourself in modules using various methods
 *
 * @author auth
 * @since 22/09/2021
 */

@UtilityClass
public final class DamageUtil {

    private final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Attempts to damage the user via fall damage.
     *
     * @param type          - the type of self damage to use in the method
     * @param value         - the value to use for the self damage and fall distance calculation
     * @param groundCheck   - if true, you will need to be on the ground for this method to complete successfully
     * @param hurtTimeCheck - if true, you will need to be not taking damage for the method to complete successfully
     */
    public static void damagePlayer(final DamageType type, final double value, final boolean groundCheck, final boolean hurtTimeCheck) {
        if ((!groundCheck || mc.thePlayer.onGround) && (!hurtTimeCheck || mc.thePlayer.hurtTime == 0)) {
            final double x = mc.thePlayer.posX;
            final double y = mc.thePlayer.posY;
            final double z = mc.thePlayer.posZ;

            double fallDistanceReq = 3.1;

            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
                fallDistanceReq += (float) (amplifier + 1);
            }

            final int packetCount = (int) Math.ceil(fallDistanceReq / value); // Don't change this unless you know the change wont break the self damage.
            for (int i = 0; i < packetCount; i++) {
                switch (type) {
                    case POSITION_ROTATION: {
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(x, y + value, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        break;
                    }

                    case POSITION: {
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + value, z, false));
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        break;
                    }
                }
            }
            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer(true));
        }
    }

    /**
     * Attempts to damage the user via fall damage.
     *
     * @param type          - the type of self damage to use in the method
     * @param value         - the value to use for the self damage and fall distance calculation
     * @param packets       - the amount of packets the self damage will send in order to damage the player
     * @param groundCheck   - if true, you will need to be on the ground for this method to complete successfully
     * @param hurtTimeCheck - if true, you will need to be not taking damage for the method to complete successfully
     */
    public static void damagePlayer(final DamageType type, final double value, final int packets, final boolean groundCheck, final boolean hurtTimeCheck) {
        if ((!groundCheck || mc.thePlayer.onGround) && (!hurtTimeCheck || mc.thePlayer.hurtTime == 0)) {
            final double x = mc.thePlayer.posX;
            final double y = mc.thePlayer.posY;
            final double z = mc.thePlayer.posZ;

            for (int i = 0; i < packets; i++) {
                switch (type) {
                    case POSITION_ROTATION: {
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(x, y + value, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        break;
                    }

                    case POSITION: {
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + value, z, false));
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        break;
                    }
                }
            }
            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer(true));
        }
    }

    /**
     * The types of damage methods
     */
    public enum DamageType {
        POSITION_ROTATION,
        POSITION
    }
}
