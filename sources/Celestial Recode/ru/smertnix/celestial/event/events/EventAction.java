package ru.smertnix.celestial.event.events;

public class EventAction implements Event{
    public boolean sprintState;

    public EventAction(boolean sprintState) {
        this.sprintState = sprintState;
    }
}

