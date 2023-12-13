package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;
import net.minecraft.entity.Entity;

public class EventEntityLight extends Event {
    private final Entity entity;
    private boolean modify;
    private int light;

    public EventEntityLight(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
        modify = true;
    }

    public boolean isModify() {
        return modify;
    }
}
