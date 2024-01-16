// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.components.draggable;

import java.util.Iterator;
import ru.fluger.client.ui.components.draggable.component.DraggableComponent;
import ru.fluger.client.Fluger;

public class DraggableScreen
{
    public void draw(final int mouseX, final int mouseY) {
        for (final DraggableComponent draggableComponent : Fluger.instance.draggableHUD.getComponents()) {
            if (draggableComponent.allowDraw()) {
                this.drawComponent(mouseX, mouseY, draggableComponent);
            }
        }
    }
    
    private void drawComponent(final int mouseX, final int mouseY, final DraggableComponent draggableComponent) {
        draggableComponent.draw(mouseX, mouseY);
        final boolean hover = rk.isMouseHoveringOnRect(draggableComponent.getX(), draggableComponent.getY(), draggableComponent.getWidth(), draggableComponent.getHeight(), mouseX, mouseY);
    }
    
    public void click(final int mouseX, final int mouseY) {
        for (final DraggableComponent draggableComponent : Fluger.instance.draggableHUD.getComponents()) {
            if (draggableComponent.allowDraw()) {
                draggableComponent.click(mouseX, mouseY);
            }
        }
    }
    
    public void release() {
        for (final DraggableComponent draggableComponent : Fluger.instance.draggableHUD.getComponents()) {
            draggableComponent.release();
        }
    }
}
