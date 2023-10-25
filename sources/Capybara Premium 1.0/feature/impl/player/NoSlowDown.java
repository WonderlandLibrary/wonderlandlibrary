package fun.expensive.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventSendPacket;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.Helper;
import fun.rich.client.utils.movement.MovementUtils;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoSlowDown extends Feature {
    public static NumberSetting percentage = new NumberSetting("Percentage", 100, 0, 100, 1, () -> true, NumberSetting.NumberType.PERCENTAGE);
    private final ListSetting noSlowMode = new ListSetting("NoSlow Mode", "Matrix", () -> true, "Vanilla", "Matrix", "Matrix New");
    public int usingTicks;

    public NoSlowDown() {
        super("NoSlowDown", "Убирает замедление при использовании еды и других предметов", FeatureCategory.Player);
        addSettings(noSlowMode, percentage);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket eventSendPacket) {
        this.setSuffix(noSlowMode.getCurrentMode() + ", " + percentage.getNumberValue() + "%");
        CPacketPlayer packet = (CPacketPlayer) eventSendPacket.getPacket();
        if (noSlowMode.currentMode.equals("Matrix New")) {
            if (mc.player.isHandActive() && MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.pressed) {
                packet.y = mc.player.ticksExisted % 2 == 0 ? packet.y + 0.0006 : packet.y + 0.0002;
                packet.onGround = false;
                mc.player.onGround = false;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(noSlowMode.getCurrentMode() + ", " + percentage.getNumberValue() + "%");
        usingTicks = mc.player.isUsingItem() ? ++usingTicks : 0;
        if (!this.isEnabled() || !mc.player.isUsingItem()) {
            return;
        }
        if (noSlowMode.currentMode.equals("Matrix")) {
            if (mc.player.isUsingItem()) {
                if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (mc.player.ticksExisted % 2 == 0) {
                        mc.player.motionX *= 0.35;
                        mc.player.motionZ *= 0.35;
                    }
                } else if (mc.player.fallDistance > 0.2) {
                    mc.player.motionX *= 0.9100000262260437;
                    mc.player.motionZ *= 0.9100000262260437;
                }
            }
        }
    }
}
