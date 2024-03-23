package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class HitboxEvent extends Event {
    public Entity entity;
    public Box box;

    public HitboxEvent(Entity entity, Box box) {
        this.entity = entity;
        this.box = box;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Box getBox() {
        return this.box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}
