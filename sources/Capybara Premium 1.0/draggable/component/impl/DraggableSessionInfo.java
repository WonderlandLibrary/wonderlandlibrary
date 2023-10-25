package fun.rich.client.draggable.component.impl;

import fun.rich.client.Rich;
import fun.rich.client.draggable.component.DraggableComponent;
import fun.rich.client.feature.impl.hud.Hud;

public class DraggableSessionInfo extends DraggableComponent {

    public DraggableSessionInfo() {
        super("Session Info", 0, 10, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Rich.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.sessionInfo.getBoolValue();
    }
}
