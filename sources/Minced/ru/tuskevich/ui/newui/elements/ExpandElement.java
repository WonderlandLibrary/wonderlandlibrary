// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

public abstract class ExpandElement extends Element
{
    private boolean expanded;
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY)) {
            this.onPress(mouseX, mouseY, button);
            if (this.canExpand() && button == 1) {
                this.expanded = !this.expanded;
            }
        }
        if (this.isExpanded()) {
            super.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    public abstract boolean canExpand();
    
    public abstract int getHeightWithExpand();
    
    public abstract void onPress(final int p0, final int p1, final int p2);
}
