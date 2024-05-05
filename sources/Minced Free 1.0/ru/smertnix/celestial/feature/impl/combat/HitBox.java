package ru.smertnix.celestial.feature.impl.combat;

import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class HitBox extends Feature {

    public static NumberSetting hitboxSize = new NumberSetting("Size", 0.5f, 0.1f, 2.f, 0.1f, () -> true);

    public HitBox() {
        super("HitBox", "Увеличивает хитбокс противника", FeatureCategory.Combat);
        addSettings(hitboxSize);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    }
}
