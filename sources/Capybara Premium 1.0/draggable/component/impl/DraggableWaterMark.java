package fun.rich.client.draggable.component.impl;

import fun.rich.client.Rich;
import fun.rich.client.draggable.component.DraggableComponent;
import fun.rich.client.feature.impl.hud.Hud;

public class DraggableWaterMark extends DraggableComponent {
    public DraggableWaterMark() {
        super("WaterMark", 0, 1, 4, 1);
    }

    @Override
    public boolean allowDraw() {
        return Rich.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.waterMark.getBoolValue();
    }
}
