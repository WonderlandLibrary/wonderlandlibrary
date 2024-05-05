package ru.smertnix.celestial.event.events;

import net.minecraft.client.entity.AbstractClientPlayer;
import ru.smertnix.celestial.event.events.callables.EventCancellable;

public class EventCustomModel extends EventCancellable implements Event {
    private AbstractClientPlayer player;

    public EventCustomModel(AbstractClientPlayer player) {
        this.player = player;
    }

    public void setPlayer(AbstractClientPlayer player) {
        this.player = player;
    }

    public AbstractClientPlayer getPlayer() {
        return this.player;
    }
}