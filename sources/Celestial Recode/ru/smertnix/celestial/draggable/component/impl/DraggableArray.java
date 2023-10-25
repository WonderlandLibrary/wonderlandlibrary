package ru.smertnix.celestial.draggable.component.impl;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.DraggableComponent;
import ru.smertnix.celestial.feature.impl.hud.FeatureList;
import ru.smertnix.celestial.feature.impl.hud.Hud;

public class DraggableArray extends DraggableComponent {

    public DraggableArray() {
        super("ArrayList", 10, 50, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Celestial.instance.featureManager.getFeature(FeatureList.class).isEnabled();
    }
}