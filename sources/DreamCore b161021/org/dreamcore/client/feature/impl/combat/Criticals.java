/* 3eLeHyy#0089 */

package org.dreamcore.client.feature.impl.combat;

import net.minecraft.network.play.client.CPacketPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventAttackSilent;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;

public class Criticals extends Feature {

    private final BooleanSetting jump;
    private final ListSetting critMode;
    private final TimerHelper ncpTimer = new TimerHelper();

    public Criticals() {
        super("Criticals", "Автоматически наносит сущности критичиский урон при ударе", Type.Combat);
        critMode = new ListSetting("Criticals Mode", "Packet", () -> true, "Packet", "WatchDog", "Ncp", "Matrix Packet 14", "Test");
        this.jump = new BooleanSetting("Mini-Jump", false, () -> !critMode.currentMode.equals("Matrix Packet 14"));
        addSettings(critMode, jump);
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent event) {
        String mode = critMode.getOptions();
        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;
        if (mode.equalsIgnoreCase("Packet")) {
            if (jump.getBoolValue()) {
                mc.player.setPosition(x, y + 0.04, z);
            }
            sendPacket(new CPacketPlayer.Position(x, y + 0.11, z, false));
            sendPacket(new CPacketPlayer.Position(x, y + 0.1100013579, z, false));
            sendPacket(new CPacketPlayer.Position(x, y + 0.0000013579, z, false));
            mc.player.onCriticalHit(event.getTargetEntity());
        } else if (mode.equalsIgnoreCase("Matrix Packet 14")) {
            mc.player.onGround = false;
            double yMotion = 1.0E-12;
            mc.player.fallDistance = (float) yMotion;
            mc.player.motionY = yMotion;
            mc.player.isCollidedVertically = true;
            sendPacket(new CPacketPlayer.Position(x, y + MathematicHelper.randomizeFloat(0.00000001F, 0.0000004F), z, false));
            sendPacket(new CPacketPlayer.Position(x, y + MathematicHelper.randomizeFloat(0.00000001F, 0.0000002F), z, false));

        } else if (mode.equalsIgnoreCase("Test")) {

            mc.player.onGround = false;
            double yMotion = 1.0E-12;
            mc.player.fallDistance = (float) yMotion;
            mc.player.motionY = yMotion;
            mc.player.isCollidedVertically = true;
        } else if (mode.equalsIgnoreCase("Ncp")) {
            if (jump.getBoolValue()) {
                mc.player.setPosition(x, y + 0.04, z);
            }
            if (timerHelper.hasReached(150) && mc.player.onGround) {
                sendPacket(new CPacketPlayer.Position(x, y + 0.11, z, false));
                sendPacket(new CPacketPlayer.Position(x, y + 0.1100013579, z, false));
                sendPacket(new CPacketPlayer.Position(x, y + 0.0000013579, z, false));
                mc.player.onCriticalHit(event.getTargetEntity());
                timerHelper.reset();
            }
        } else if (mode.equalsIgnoreCase("WatchDog")) {
            if (jump.getBoolValue()) {
                mc.player.setPosition(x, y + 0.04, z);
            }
            double random = MathematicHelper.randomizeFloat(4.0E-7f, 4.0E-5f);
            if (timerHelper.hasReached(100)) {
                for (double value : new double[]{0.007017625 + random, 0.007349825 + random, 0.006102874 + random}) {
                    mc.player.fallDistance = (float) value;
                    mc.player.isCollidedVertically = true;
                    mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY + value, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
                    timerHelper.reset();
                }
            }
            mc.player.onCriticalHit(event.getTargetEntity());
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = critMode.getOptions();
        if (mode.equalsIgnoreCase("Test")) {
            if (mc.player.onGround) {
                mc.player.onGround = false;
                mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + MathematicHelper.randomizeFloat(0.2F, 0.2F), mc.player.posZ);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = critMode.getOptions();
        this.setSuffix(mode);
    }
}