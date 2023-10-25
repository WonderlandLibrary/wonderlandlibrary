package ru.smertnix.celestial.feature.impl.combat;

import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class Reach extends Feature {

    public static NumberSetting hitboxSize = new NumberSetting("Distance", 3.5f, 0.1f, 2.f, 0.1f, () -> true);

    public Reach() {
        super("Reach", "Увеличивает дистанцию для удара игрока", FeatureCategory.Combat);
        addSettings(hitboxSize);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    }
}
