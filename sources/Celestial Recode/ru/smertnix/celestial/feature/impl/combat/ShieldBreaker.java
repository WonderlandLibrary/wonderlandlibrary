package ru.smertnix.celestial.feature.impl.combat;

import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;

public class ShieldBreaker extends Feature {
    public static BooleanSetting auraOnly = new BooleanSetting("Aura Only", false, () -> true);

    public ShieldBreaker() {
        super("Shield Breaker", "Позволяет ломать щит противнику рукой", FeatureCategory.Combat);
        addSettings(auraOnly);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    	
    }
}
