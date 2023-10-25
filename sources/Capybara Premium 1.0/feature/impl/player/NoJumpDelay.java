package fun.expensive.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.utils.Helper;

public class NoJumpDelay extends Feature {
    public NoJumpDelay() {
        super("NoJumpDelay", "Уберает задержку при прыжке", FeatureCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (!isEnabled()) {
            return;
        }
        mc.player.jumpTicks = 0;
    }
}
