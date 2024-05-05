// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.draggable.component.impl;

import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.Fluger;
import ru.fluger.client.ui.components.draggable.component.DraggableComponent;

public class DraggableArmorStatus extends DraggableComponent
{
    public DraggableArmorStatus() {
        super("Armor Status", 380, 357, 4, 1);
    }
    
    @Override
    public boolean allowDraw() {
        return Fluger.instance.featureManager.getFeatureByClass(HUD.class).getState() && HUD.armor.getCurrentValue();
    }
}
