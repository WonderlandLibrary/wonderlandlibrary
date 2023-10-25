package fun.expensive.client.feature.impl.movement;

import fun.rich.client.Rich;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventSendPacket;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.movement.MovementUtils;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;

public class Strafe extends Feature {
    public static BooleanSetting smart = new BooleanSetting("Smart", false, () -> true);

    public Strafe() {
        super("Strafe", "Позволяет стрейфить на матриксе", FeatureCategory.Movement);
        addSettings(smart);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (!isEnabled()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            if (MovementUtils.getSpeed() < 0.2177f) {
                MovementUtils.strafe();
                if (Math.abs(mc.player.movementInput.moveStrafe) < 0.1 && mc.gameSettings.keyBindForward.pressed) {
                    MovementUtils.strafe();
                }
                if (mc.player.onGround) {
                    MovementUtils.strafe();
                }
            }
        }
    }
}
