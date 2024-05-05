package tech.atani.client.listener.event.minecraft.game;

import tech.atani.client.listener.event.Event;

public class TimerManipulationEvent extends Event {
    private long time;

    public TimerManipulationEvent(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
