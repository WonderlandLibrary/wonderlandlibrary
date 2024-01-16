// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl.panel;

import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;

public abstract class DraggablePanel extends ExpandableComponent
{
    private boolean dragging;
    private float prevX;
    private float prevY;
    
    public DraggablePanel(final Component parent, final String name, final float x, final float y, final float width, final float height) {
        super(parent, name, x, y, width, height);
        this.prevX = x;
        this.prevY = y;
    }
    
    @Override
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.setX(mouseX - this.prevX);
            this.setY(mouseY - this.prevY);
        }
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
        if (button == 0) {
            this.dragging = true;
            this.prevX = (float)(int)(mouseX - this.getX());
            this.prevY = (float)(int)(mouseY - this.getY());
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        super.onMouseRelease(button);
        this.dragging = false;
    }
}
