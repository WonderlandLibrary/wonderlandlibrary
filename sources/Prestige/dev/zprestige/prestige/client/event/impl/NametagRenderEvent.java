package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

public class NametagRenderEvent extends Event {
    public Entity entity;
    public Text text;

    public NametagRenderEvent(Entity entity, Text text) {
        this.entity = entity;
        this.text = text;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Text getText() {
        return this.text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
