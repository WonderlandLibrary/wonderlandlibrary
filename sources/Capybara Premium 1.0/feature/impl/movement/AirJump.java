package fun.expensive.client.feature.impl.movement;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;

public class AirJump extends Feature {

    public AirJump() {
        super("AirJump", "Позволяет прыгать по воздуху", FeatureCategory.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if(mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.jump();
        }
    }
}
