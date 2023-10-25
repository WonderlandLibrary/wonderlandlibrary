package ru.smertnix.celestial.feature.impl.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class AutoSprint extends Feature {
    public static BooleanSetting keep = new BooleanSetting("Keep", "", true, () -> true);
    public AutoSprint() {
        super("Auto Sprint", "Вы можете автоматически бежать", FeatureCategory.Movement);
        addSettings(keep);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (mc.player.getFoodStats().getFoodLevel() / 2 > 3) {
            mc.player.setSprinting(MovementUtils.isMoving());
        }
    }

    @Override
    public void onDisable() {
        mc.player.setSprinting(false);
        super.onDisable();
    }
}
