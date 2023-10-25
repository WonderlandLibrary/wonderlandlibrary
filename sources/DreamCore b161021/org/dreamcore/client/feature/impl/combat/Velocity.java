package org.dreamcore.client.feature.impl.combat;

import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class Velocity extends Feature {

    public static BooleanSetting cancelOtherDamage;
    public static ListSetting velocityMode;

    public static BooleanSetting cancelEntityVelocity = new BooleanSetting("Cancel Entity Velocity", true, () -> velocityMode.currentMode.equals("Custom"));
    public static BooleanSetting cancelExplosion = new BooleanSetting("Cancel Explosion", true, () -> velocityMode.currentMode.equals("Custom"));
    public static BooleanSetting useMotion = new BooleanSetting("Use Motion", true, () -> velocityMode.currentMode.equals("Custom"));
    public static NumberSetting motionX = new NumberSetting("Motion X", 0, 0, 100, 1, () -> velocityMode.currentMode.equals("Custom") && useMotion.getBoolValue() && !cancelEntityVelocity.getBoolValue());
    public static NumberSetting motionY = new NumberSetting("Motion Y", 0, 0, 100, 1, () -> velocityMode.currentMode.equals("Custom") && useMotion.getBoolValue() && !cancelEntityVelocity.getBoolValue());
    public static NumberSetting motionZ = new NumberSetting("Motion Z", 0, 0, 100, 1, () -> velocityMode.currentMode.equals("Custom") && useMotion.getBoolValue() && !cancelEntityVelocity.getBoolValue());
    public static BooleanSetting usePacketMotion = new BooleanSetting("Use Packet Motion", true, () -> velocityMode.currentMode.equals("Custom"));
    public static NumberSetting packetX = new NumberSetting("Packet X", 0, 0, 100, 1, () -> velocityMode.currentMode.equals("Custom") && !cancelEntityVelocity.getBoolValue() && usePacketMotion.getBoolValue());
    public static NumberSetting packetY = new NumberSetting("Packet Y", 0, 0, 100, 1, () -> velocityMode.currentMode.equals("Custom") && !cancelEntityVelocity.getBoolValue() && usePacketMotion.getBoolValue());
    public static NumberSetting packetZ = new NumberSetting("Packet Z", 0, 0, 100, 1, () -> velocityMode.currentMode.equals("Custom") && !cancelEntityVelocity.getBoolValue() && usePacketMotion.getBoolValue());
    public static NumberSetting hurt = new NumberSetting("Hurt", 0, 0, 10, 1, () -> velocityMode.currentMode.equals("Custom"));

    public Velocity() {
        super("Velocity", "Уменьшает кнокбэк при ударе", Type.Combat);
        velocityMode = new ListSetting("Velocity Mode", "Packet", () -> true, "Packet", "Matrix", "Reverse", "Custom");
        cancelOtherDamage = new BooleanSetting("Cancel Other Damage", true, () -> true);
        addSettings(velocityMode, cancelEntityVelocity, cancelExplosion, useMotion, usePacketMotion, hurt, packetX, packetY, packetZ, motionX, motionY, motionZ, cancelOtherDamage);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        String mode = velocityMode.getOptions();
        if (cancelOtherDamage.getBoolValue()) {
            if (mc.player.hurtTime > 0 && event.getPacket() instanceof SPacketEntityVelocity) {
                if (mc.player.isPotionActive(MobEffects.POISON) || (mc.player.isPotionActive(MobEffects.WITHER) || mc.player.isBurning())) {
                    event.setCancelled(true);
                }
            }
        }
        if (mode.equalsIgnoreCase("Reverse")) {
            if (mc.player.hurtTime > 0) {
                MovementHelper.strafePlayer(MovementHelper.getSpeed());
                mc.player.speedInAir = 0.02F;
            }
        }
        if (mode.equalsIgnoreCase("Packet")) {
            if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
                if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                    event.setCancelled(true);
                }
            }
        } else if (mode.equals("Matrix")) {
            if (mc.player.hurtTime > 8) {
                mc.player.onGround = true;
            }
        } else if (mode.equals("Custom")) {
            if ((cancelEntityVelocity.getBoolValue() && event.getPacket() instanceof SPacketEntityVelocity) || (cancelExplosion.getBoolValue() && event.getPacket() instanceof SPacketExplosion)) {
                event.setCancelled(true);
            } else if (mc.player.hurtTime > hurt.getNumberValue() || (cancelExplosion.getBoolValue() && event.getPacket() instanceof SPacketExplosion)) {
                SPacketEntityVelocity sPacketEntityVelocity = (SPacketEntityVelocity) event.getPacket();

                if (usePacketMotion.getBoolValue()) {
                    sPacketEntityVelocity.motionX = (int) (sPacketEntityVelocity.motionX / 100 * packetX.getNumberValue());
                    sPacketEntityVelocity.motionY = (int) (sPacketEntityVelocity.motionY / 100 * packetY.getNumberValue());
                    sPacketEntityVelocity.motionZ = (int) (sPacketEntityVelocity.motionZ / 100 * packetZ.getNumberValue());
                }

                if (useMotion.getBoolValue()) {
                    mc.player.motionX = (int) (mc.player.motionX / 100 * motionX.getNumberValue());
                    mc.player.motionY = (int) (mc.player.motionY / 100 * motionY.getNumberValue());
                    mc.player.motionZ = (int) (mc.player.motionZ / 100 * motionZ.getNumberValue());
                }
            }
        }
    }
}
