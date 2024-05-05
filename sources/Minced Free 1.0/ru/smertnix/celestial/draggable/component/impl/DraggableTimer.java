package ru.smertnix.celestial.draggable.component.impl;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.DraggableComponent;
import ru.smertnix.celestial.feature.impl.movement.Timer;

public class DraggableTimer extends DraggableComponent {

    public DraggableTimer() {
        super("Timer", 160, 400, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Celestial.instance.featureManager.getFeature(Timer.class).isEnabled() && Timer.smart.getBoolValue();
    }
}
