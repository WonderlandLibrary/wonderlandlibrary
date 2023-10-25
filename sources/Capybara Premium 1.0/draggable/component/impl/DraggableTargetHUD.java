package fun.rich.client.draggable.component.impl;

import fun.rich.client.Rich;
import fun.rich.client.draggable.component.DraggableComponent;
import fun.rich.client.feature.impl.combat.KillAura;
import fun.rich.client.feature.impl.hud.TargetHUD;

public class DraggableTargetHUD extends DraggableComponent {

    public DraggableTargetHUD() {
        super("TargetHUD", 350, 25, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Rich.instance.featureManager.getFeature(TargetHUD.class).isEnabled() && KillAura.target != null;
    }
}
