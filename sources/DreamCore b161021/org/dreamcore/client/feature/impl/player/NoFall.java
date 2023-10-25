package org.dreamcore.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.ListSetting;

public class NoFall extends Feature {

    public static ListSetting noFallMode;
    public TimerHelper timerHelper = new TimerHelper();

    public NoFall() {
        super("NoFall", "Позволяет получить меньший дамаг при падении", Type.Player);
        noFallMode = new ListSetting("NoFall Mode", "Vanilla", () -> true, "Vanilla", "GroundCancel", "Spartan", "AAC-Flags", "Matrix", "Hypixel");
        addSettings(noFallMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = noFallMode.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Vanilla")) {
            if (mc.player.fallDistance > 3) {
                event.setOnGround(true);
                mc.player.connection.sendPacket(new CPacketPlayer(true));
            }
        } else if (mode.equalsIgnoreCase("Spartan")) {
            if (mc.player.fallDistance > 3.5f) {
                if (timerHelper.hasReached(150L)) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    timerHelper.reset();
                } else {
                    mc.player.onGround = false;
                }
            }
        } else if (mode.equalsIgnoreCase("AAC-Flags")) {
            mc.player.motionY -= 0.1;
            event.setOnGround(true);
            mc.player.capabilities.disableDamage = true;
        } else if (mode.equalsIgnoreCase("Hypixel")) {
            if (mc.player.fallDistance > 3.4) {
                event.setOnGround(mc.player.ticksExisted % 2 == 0);
            }
        } else if (mode.equalsIgnoreCase("Matrix")) {
            if (mc.player.fallDistance > 3) {
                mc.player.fallDistance = (float) (Math.random() * 1.0E-12);
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
                mc.player.fallDistance = 0;
            }
        } else if (mode.equalsIgnoreCase("GroundCancel")) {
            event.setOnGround(false);
        }
    }
}