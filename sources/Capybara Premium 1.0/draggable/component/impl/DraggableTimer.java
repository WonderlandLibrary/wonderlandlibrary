package fun.rich.client.draggable.component.impl;

import fun.rich.client.Rich;
import fun.rich.client.draggable.component.DraggableComponent;
import fun.rich.client.feature.impl.movement.Timer;

public class DraggableTimer extends DraggableComponent {

    public DraggableTimer() {
        super("Timer", 160, 400, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Rich.instance.featureManager.getFeature(Timer.class).isEnabled() && Timer.smart.getBoolValue();
    }
}
