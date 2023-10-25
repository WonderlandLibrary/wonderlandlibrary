package ru.smertnix.celestial.draggable.component.impl;

import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.DraggableComponent;
import ru.smertnix.celestial.feature.impl.hud.Hud;

public class DraggablePotionHUD extends DraggableComponent {

    public DraggablePotionHUD() {
        super("Potion Status", 10, 200, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Celestial.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.potions.getBoolValue();
    }
}
