// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component;

import vip.Resolute.ui.click.skeet.component.impl.GroupBoxComponent;
import java.util.Iterator;
import vip.Resolute.ui.click.skeet.framework.ExpandableComponent;
import java.util.List;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.framework.Component;

public abstract class TabComponent extends Component
{
    private final String name;
    
    public TabComponent(final Component parent, final String name, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
        this.setupChildren();
        this.name = name;
    }
    
    public abstract void setupChildren();
    
    @Override
    public void drawComponent(final ScaledResolution resolution, final int mouseX, final int mouseY) {
        SkeetUI.FONT_RENDERER.drawStringWithShadow(this.name, this.getX() + 8.0f, this.getY() + 8.0f - 3.0f, SkeetUI.getColor(16777215));
        float x = 8.0f;
        final List<Component> children = this.getChildren();
        for (int i = 0; i < children.size(); ++i) {
            final Component child = children.get(i);
            child.setX(x);
            if (i < 3) {
                child.setY(14.0f);
            }
            child.drawComponent(resolution, mouseX, mouseY);
            x += 122.333336f;
            if (x + 8.0f + 94.333336f > 365.0f) {
                x = 8.0f;
            }
            if (i > 2) {
                int above = i - 3;
                int totalY = 14;
                do {
                    final Component componentAbove = this.getChildren().get(above);
                    totalY = (int)(totalY + componentAbove.getHeight() + 6.0f);
                    above -= 3;
                } while (above >= 0);
                child.setY((float)totalY);
            }
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        for (Component child : this.getChildren()) {
            final Iterator var5 = child.getChildren().iterator();
            while (var5.hasNext()) {
                child = var5.next();
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandable = (ExpandableComponent)child;
                    if (!expandable.isExpanded()) {
                        continue;
                    }
                    final float x = expandable.getExpandedX();
                    final float y = expandable.getExpandedY();
                    if (mouseX >= x && mouseY > y && mouseX <= x + expandable.getExpandedWidth() && mouseY < y + expandable.getExpandedHeight()) {
                        child.onMouseClick(mouseX, mouseY, button);
                        return;
                    }
                    continue;
                }
            }
        }
        for (final Component child : this.getChildren()) {
            if (child.isHovered(mouseX, mouseY)) {
                child.onMouseClick(mouseX, mouseY, button);
                return;
            }
        }
        super.onMouseClick(mouseX, mouseY, button);
    }
    
    @Override
    public boolean isHovered(final int mouseX, final int mouseY) {
        for (final Component child : this.getChildren()) {
            if (child instanceof GroupBoxComponent) {
                final GroupBoxComponent groupBox = (GroupBoxComponent)child;
                if (groupBox.isHoveredEntire(mouseX, mouseY)) {
                    return true;
                }
                continue;
            }
        }
        return super.isHovered(mouseX, mouseY);
    }
}
