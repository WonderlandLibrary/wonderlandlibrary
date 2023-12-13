package io.github.nevalackin.client.module.render.world;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.world.UpdateWorldTimeEvent;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;

public final class WorldTime extends Module {

    private final EnumProperty<Time> timeProperty = new EnumProperty<>("Time", Time.NIGHT);

    public WorldTime() {
        super("World Time", Category.RENDER, Category.SubCategory.RENDER_WORLD);

        this.register(this.timeProperty);
    }

    @EventLink
    private final Listener<UpdateWorldTimeEvent> onUpdateWorldTime = event -> {
        event.setTime(this.timeProperty.getValue().time);
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private enum Time {
        MORNING("Morning", 1000),
        DAY("Day", 6000),
        SUNSET("Sunset", 13000),
        NIGHT("Night", 16000);

        private final String name;
        private final long time;

        Time(String name, long time) {
            this.name = name;
            this.time = time;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
