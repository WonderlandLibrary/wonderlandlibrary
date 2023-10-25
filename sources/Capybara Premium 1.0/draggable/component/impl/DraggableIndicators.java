package fun.rich.client.draggable.component.impl;

import fun.rich.client.Rich;
import fun.rich.client.draggable.component.DraggableComponent;
import fun.rich.client.feature.impl.hud.Hud;

public class DraggableIndicators extends DraggableComponent {

    public DraggableIndicators() {
        super("Indicators", 350, 25, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Rich.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.indicators.getBoolValue();
    }
}
