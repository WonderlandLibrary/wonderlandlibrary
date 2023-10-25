package ru.smertnix.celestial.feature.impl.misc;

import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class Baritone extends Feature {
    public Baritone() {
        super("Baritone", "Баритон", FeatureCategory.Util);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    	
    }
}
