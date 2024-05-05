package io.github.nevalackin.client.event.game;

import io.github.nevalackin.client.event.Event;

public final class GetTimerSpeedEvent implements Event {

    private double timerSpeed;

    public GetTimerSpeedEvent(double timerSpeed) {
        this.timerSpeed = timerSpeed;
    }

    public double getTimerSpeed() {
        return timerSpeed;
    }

    public void setTimerSpeed(double timerSpeed) {
        this.timerSpeed = timerSpeed;
    }
}
