package wtf.evolution.event.events.impl;

import net.minecraft.client.entity.AbstractClientPlayer;
import wtf.evolution.event.events.Event;
import wtf.evolution.event.events.callables.EventCancellable;

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