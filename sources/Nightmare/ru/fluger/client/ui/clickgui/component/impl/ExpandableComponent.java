// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl;

import ru.fluger.client.ui.clickgui.component.Component;

public abstract class ExpandableComponent extends Component
{
    private boolean expanded;
    
    public ExpandableComponent(final Component parent, final String name, final float x, final float y, final float width, final float height) {
        super(parent, name, x, y, width, height);
    }
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY)) {
            this.onPress(mouseX, mouseY, button);
            if (this.canExpand() && button == 1) {
                this.expanded = !this.expanded;
            }
        }
        if (this.isExpanded()) {
            super.onMouseClick(mouseX, mouseY, button);
        }
    }
    
    public abstract boolean canExpand();
    
    public abstract int getHeightWithExpand();
    
    public abstract void onPress(final int p0, final int p1, final int p2);
}
