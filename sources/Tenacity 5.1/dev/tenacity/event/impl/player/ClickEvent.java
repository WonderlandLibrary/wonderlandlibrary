package dev.tenacity.event.impl.player;

import dev.tenacity.event.Event;

public class ClickEvent extends Event
{
    boolean fake;

    public ClickEvent(boolean fake) { this.fake = fake; }

    public boolean isFake() { return fake; }
}
