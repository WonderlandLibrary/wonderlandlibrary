package tech.atani.client.listener.event.minecraft.player.movement;

import tech.atani.client.listener.event.Event;

public class DirectionSprintCheckEvent extends Event {
    boolean sprintCheck;

    public DirectionSprintCheckEvent(boolean sprintCheck) {
        this.sprintCheck = sprintCheck;
    }

    public boolean isSprintCheck() {
        return sprintCheck;
    }

    public void setSprintCheck(boolean sprintCheck) {
        this.sprintCheck = sprintCheck;
    }
}