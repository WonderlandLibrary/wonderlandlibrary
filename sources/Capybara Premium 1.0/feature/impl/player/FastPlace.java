package fun.expensive.client.feature.impl.player;

import fun.expensive.client.event.EventTarget;
import fun.expensive.client.event.events.impl.player.EventUpdate;
import fun.expensive.client.feature.Feature;
import fun.expensive.client.feature.impl.FeatureCategory;
import fun.expensive.client.utils.Helper;

public class FastPlace extends Feature {
    public FastPlace() {
        super("FastPlace", FeatureCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        mc.rightClickDelayTimer = 0;
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 6;
        super.onDisable();
    }
}
