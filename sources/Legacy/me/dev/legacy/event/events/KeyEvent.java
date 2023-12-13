package me.dev.legacy.event.events;

import me.dev.legacy.event.EventStage;

public class KeyEvent extends EventStage
{
    public boolean info;
    public boolean pressed;

    public KeyEvent(final int stage) {
        super(stage);
        this.info = info;
        this.pressed = pressed;
    }
}

