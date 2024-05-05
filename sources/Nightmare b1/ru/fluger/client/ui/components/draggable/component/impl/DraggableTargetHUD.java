// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.draggable.component.impl;

import ru.fluger.client.feature.impl.combat.KillAura;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.hud.TargetHUD;
import ru.fluger.client.Fluger;
import ru.fluger.client.ui.components.draggable.component.DraggableComponent;

public class DraggableTargetHUD extends DraggableComponent
{
    public DraggableTargetHUD() {
        super("TargetHUD", 350, 25, 1, 1);
    }
    
    @Override
    public boolean allowDraw() {
        return Fluger.instance.featureManager.getFeatureByClass(TargetHUD.class).getState() && KillAura.target != null;
    }
}
