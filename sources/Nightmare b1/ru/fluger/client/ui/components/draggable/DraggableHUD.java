// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.draggable;

import java.util.Iterator;
import ru.fluger.client.ui.components.draggable.component.impl.DraggableWorldInfo;
import ru.fluger.client.ui.components.draggable.component.impl.DraggableArmorStatus;
import ru.fluger.client.ui.components.draggable.component.impl.DraggableTargetHUD;
import ru.fluger.client.ui.components.draggable.component.impl.DraggablePotionStatus;
import ru.fluger.client.ui.components.draggable.component.impl.DraggableWaterMark;
import com.google.common.collect.Lists;
import ru.fluger.client.ui.components.draggable.component.DraggableComponent;
import java.util.List;

public class DraggableHUD
{
    private DraggableScreen screen;
    private final List<DraggableComponent> components;
    
    public DraggableHUD() {
        this.screen = new DraggableScreen();
        (this.components = (List<DraggableComponent>)Lists.newArrayList()).add(new DraggableWaterMark());
        this.components.add(new DraggablePotionStatus());
        this.components.add(new DraggableTargetHUD());
        this.components.add(new DraggableArmorStatus());
        this.components.add(new DraggableWorldInfo());
    }
    
    public DraggableScreen getScreen() {
        return this.screen;
    }
    
    public List<DraggableComponent> getComponents() {
        return this.components;
    }
    
    public DraggableComponent getDraggableComponentByClass(final Class<? extends DraggableComponent> classs) {
        for (final DraggableComponent draggableComponent : this.components) {
            if (draggableComponent.getClass() == classs) {
                return draggableComponent;
            }
        }
        return null;
    }
}
