package ru.smertnix.celestial.feature.impl.player;

import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.utils.Helper;

public class NoJumpDelay extends Feature {
    public NoJumpDelay() {
        super("NoJumpDelay", "Позволяет прыгать без задержки", FeatureCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (!isEnabled()) {
            return;
        }
        mc.player.jumpTicks = 0;
    }
}
