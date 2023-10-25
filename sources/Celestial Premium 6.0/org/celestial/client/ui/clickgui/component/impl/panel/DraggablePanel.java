/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui.component.impl.panel;

import net.minecraft.client.gui.ScaledResolution;
import org.celestial.client.ui.clickgui.component.Component;
import org.celestial.client.ui.clickgui.component.impl.ExpandableComponent;

public abstract class DraggablePanel
extends ExpandableComponent {
    private boolean dragging;
    private float prevX;
    private float prevY;

    public DraggablePanel(Component parent, String name, float x, float y, float width, float height) {
        super(parent, name, x, y, width, height);
        this.prevX = x;
        this.prevY = y;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (this.dragging) {
            this.setX((float)mouseX - this.prevX);
            this.setY((float)mouseY - this.prevY);
        }
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0) {
            this.dragging = true;
            this.prevX = (int)((double)mouseX - this.getX());
            this.prevY = (int)((double)mouseY - this.getY());
        }
    }

    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
        this.dragging = false;
    }
}

