// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop.comp.impl.panel;

import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.drop.comp.Component;
import vip.Resolute.ui.click.drop.comp.impl.ExpandableComponent;

public abstract class DraggablePanel extends ExpandableComponent
{
    private boolean dragging;
    private int prevX;
    private int prevY;
    
    public DraggablePanel(final Component parent, final String name, final int x, final int y, final int width, final int height) {
        super(parent, name, x, y, width, height);
        this.prevX = x;
        this.prevY = y;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.setX(mouseX - this.prevX);
            this.setY(mouseY - this.prevY);
        }
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
        if (button == 0) {
            this.dragging = true;
            this.prevX = mouseX - this.getX();
            this.prevY = mouseY - this.getY();
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        super.onMouseRelease(button);
        this.dragging = false;
    }
}
