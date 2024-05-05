package ru.smertnix.celestial.event.events.impl.player;

import net.minecraft.entity.Entity;
import ru.smertnix.celestial.event.events.callables.EventCancellable;

public class EventPostAttackSilent
        extends EventCancellable {
    private final Entity targetEntity;

    public EventPostAttackSilent(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
