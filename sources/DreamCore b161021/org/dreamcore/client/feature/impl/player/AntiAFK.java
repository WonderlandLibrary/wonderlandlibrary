package org.dreamcore.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.GCDCalcHelper;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class AntiAFK extends Feature {

    private final BooleanSetting sendMessage;
    private final BooleanSetting spin;
    private final BooleanSetting click = new BooleanSetting("Click", true, () -> true);
    public NumberSetting sendDelay;
    public BooleanSetting invalidRotation = new BooleanSetting("Invalid Rotation", true, () -> true);
    public BooleanSetting jump = new BooleanSetting("Jump", true, () -> true);
    public float rot = 0;

    public AntiAFK() {
        super("AntiAFK", "Позволяет встать в афк режим, без риска кикнуться", Type.Player);
        spin = new BooleanSetting("Spin", true, () -> true);
        sendMessage = new BooleanSetting("Send Message", true, () -> true);
        sendDelay = new NumberSetting("Send Delay", 500, 100, 1000, 100, sendMessage::getBoolValue);
        addSettings(spin, sendMessage, click, sendDelay, invalidRotation, jump);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (!MovementHelper.isMoving()) {
            if (spin.getBoolValue()) {
                float yaw = GCDCalcHelper.getFixedRotation((float) (Math.floor(spinAim(1F)) + MathematicHelper.randomizeFloat(-4, 1)));
                event.setYaw(yaw);
                mc.player.renderYawOffset = yaw;
                mc.player.rotationPitchHead = 0;
                mc.player.rotationYawHead = yaw;
            }

            if (mc.player.ticksExisted % 60 == 0 && invalidRotation.getBoolValue()) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + 1, mc.player.posY + 1, mc.player.posZ + 1, mc.player.rotationYaw, mc.player.rotationPitch, true));
            }

            if (mc.player.ticksExisted % 60 == 0 && click.getBoolValue()) {
                mc.clickMouse();
            }

            if (mc.player.ticksExisted % 40 == 0 && jump.getBoolValue()) {
                mc.player.jump();
            }

            if (mc.player.ticksExisted % 400 == 0 && sendMessage.getBoolValue()) {
                mc.player.sendChatMessage("/homehome");
            }
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
