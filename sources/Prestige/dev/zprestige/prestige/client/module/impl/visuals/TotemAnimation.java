package dev.zprestige.prestige.client.module.impl.visuals;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.FloatingItemEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.IntSetting;

public class TotemAnimation extends Module {

    public IntSetting speed = setting("Speed", 1, 0, 5).description("Speed of the totem animation");

    public TotemAnimation() {
        super("Totem Animation", Category.Visual, "Increases the speed of totem animations");
    }

    @EventListener
    public void event(FloatingItemEvent event) {
        event.setSpeed(speed.getObject());
    }
}
