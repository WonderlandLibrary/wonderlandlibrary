package org.dreamcore.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class NoSlowDown extends Feature {

    public static NumberSetting percentage;
    public static BooleanSetting soulSand;
    public static BooleanSetting slimeBlock;
    public static ListSetting noSlowMode;

    public NoSlowDown() {
        super("NoSlowDown", "Убирает замедление при использовании еды и других предметов", Type.Movement);
        percentage = new NumberSetting("Percentage", 100, 0, 100, 1, () -> true);
        noSlowMode = new ListSetting("NoSlow Mode", "Vanilla", () -> true, "Vanilla", "Matrix");
        soulSand = new BooleanSetting("Soul Sand", false, () -> true);
        slimeBlock = new BooleanSetting("Slime", true, () -> true);
        addSettings(noSlowMode, percentage, soulSand, slimeBlock);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(percentage.getNumberValue() + "% " + noSlowMode.getCurrentMode());
        if (noSlowMode.currentMode.equals("Matrix")) {
            if (mc.player.isUsingItem() && MovementHelper.isMoving() && mc.player.fallDistance > 0.7) {
                mc.player.motionX *= 0.97F;
                mc.player.motionZ *= 0.97F;
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (noSlowMode.currentMode.equals("Matrix")) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer cPacketPlayer = (CPacketPlayer) event.getPacket();
                if (mc.player.isUsingItem() && MovementHelper.isMoving() && !mc.gameSettings.keyBindJump.pressed) {
                    cPacketPlayer.y = mc.player.ticksExisted % 2 == 0 ? cPacketPlayer.y + 0.0006 : cPacketPlayer.y + 0.0002;
                    cPacketPlayer.onGround = false;
                    mc.player.onGround = false;
                }
            }
        }
    }
}