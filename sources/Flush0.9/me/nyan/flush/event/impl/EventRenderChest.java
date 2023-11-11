package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;

public class EventRenderChest extends Event {
    private final ChestType type;

    public EventRenderChest(ChestType type) {
        this.type = type;
    }

    public ChestType getType() {
        return type;
    }

    public enum ChestType {
        CHEST,
        ENDERCHEST
    }
}