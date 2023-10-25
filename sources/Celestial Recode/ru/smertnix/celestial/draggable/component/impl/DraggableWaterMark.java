package ru.smertnix.celestial.draggable.component.impl;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.DraggableComponent;
import ru.smertnix.celestial.feature.impl.hud.Hud;

public class DraggableWaterMark extends DraggableComponent {
    public DraggableWaterMark() {
        super("WaterMark", 10, 10, 4, 1);
    }

    @Override
    public boolean allowDraw() {
        return Celestial.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.waterMark.getBoolValue();
    }
}
