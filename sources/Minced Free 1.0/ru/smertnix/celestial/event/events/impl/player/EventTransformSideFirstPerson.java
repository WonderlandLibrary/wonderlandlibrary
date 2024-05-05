package ru.smertnix.celestial.event.events.impl.player;

import net.minecraft.util.EnumHandSide;
import ru.smertnix.celestial.event.events.Event;

public class EventTransformSideFirstPerson implements Event {

    private final EnumHandSide enumHandSide;

    public EventTransformSideFirstPerson(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
