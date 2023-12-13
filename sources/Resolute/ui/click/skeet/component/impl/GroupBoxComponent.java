// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl;

import vip.Resolute.ui.click.skeet.framework.ExpandableComponent;
import java.util.Iterator;
import vip.Resolute.ui.click.skeet.component.impl.sub.key.KeyBindComponent;
import vip.Resolute.ui.click.skeet.framework.PredicateComponent;
import vip.Resolute.util.font.FontUtil;
import net.minecraft.client.gui.Gui;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.framework.Component;

public final class GroupBoxComponent extends Component
{
    private final String name;
    
    public GroupBoxComponent(final Component parent, final String name, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
        this.name = name;
    }
    
    @Override
    public void drawComponent(final ScaledResolution resolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final float length = (float)SkeetUI.GROUP_BOX_HEADER_RENDERER.getStringWidth(this.name);
        Gui.drawRect(x, y, x + width + 20.0f, y + height, SkeetUI.getColor(789516));
        Gui.drawRect(x + 0.5f, y + 0.5f, x + width - 0.5f + 20.0f, y + height - 0.5f, SkeetUI.getColor(2631720));
        Gui.drawRect(x + 4.0f, y, x + 4.0f + length + 2.0f + 20.0f, y + 1.0f, SkeetUI.getColor(1513239));
        Gui.drawRect(x + 1.0f, y + 1.0f, x + width - 1.0f + 20.0f, y + height - 1.0f, SkeetUI.getColor(1513239));
        if (SkeetUI.shouldRenderText()) {
            FontUtil.tahomaSmall.drawStringWithShadow(this.name, x + 5.0f, y - 0.5f, SkeetUI.getColor(14474460));
        }
        float childYLeft = 6.0f;
        float childYRight = 6.0f;
        boolean left = true;
        final float right = 49.166668f;
        for (final Component component : this.children) {
            if (component instanceof PredicateComponent) {
                final PredicateComponent predicateComponent = (PredicateComponent)component;
                if (!predicateComponent.isVisible()) {
                    continue;
                }
            }
            else if (component instanceof KeyBindComponent) {
                continue;
            }
            if (component.getWidth() >= 80.333336f) {
                component.setX(3.0f);
                component.setY(childYLeft);
                component.drawComponent(resolution, mouseX, mouseY);
                final float yOffset = component.getHeight() + 4.0f;
                childYLeft += yOffset;
                childYRight += yOffset;
                left = true;
            }
            else {
                component.setX(left ? 3.0f : 69.16667f);
                component.setY(left ? childYLeft : childYRight);
                component.drawComponent(resolution, mouseX, mouseY);
                if (left) {
                    childYLeft += component.getHeight() + 4.0f;
                }
                else {
                    childYRight += component.getHeight() + 4.0f;
                }
                left = (childYRight >= childYLeft);
            }
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        for (final Component child : this.getChildren()) {
            if (child instanceof ExpandableComponent) {
                final ExpandableComponent expandable = (ExpandableComponent)child;
                if (expandable.isExpanded()) {
                    final float x = expandable.getExpandedX();
                    final float y = expandable.getExpandedY();
                    if (mouseX >= x && mouseY > y && mouseX <= x + expandable.getExpandedWidth() && mouseY < y + expandable.getExpandedHeight()) {
                        child.onMouseClick(mouseX, mouseY, button);
                        return;
                    }
                }
            }
            child.onMouseClick(mouseX, mouseY, button);
        }
    }
    
    public boolean isHoveredEntire(final int mouseX, final int mouseY) {
        for (final Component child : this.getChildren()) {
            if (child instanceof ExpandableComponent) {
                final ExpandableComponent expandable = (ExpandableComponent)child;
                if (!expandable.isExpanded()) {
                    continue;
                }
                final float x = expandable.getExpandedX();
                final float y = expandable.getExpandedY();
                if (mouseX >= x && mouseY >= y && mouseX <= x + expandable.getExpandedWidth() && mouseY <= y + expandable.getExpandedHeight()) {
                    return true;
                }
                continue;
            }
        }
        return super.isHovered(mouseX, mouseY);
    }
    
    @Override
    public float getHeight() {
        float heightLeft;
        float heightRight;
        final float initHeight = heightRight = (heightLeft = super.getHeight());
        boolean left = true;
        for (final Component component : this.getChildren()) {
            if (component instanceof PredicateComponent) {
                final PredicateComponent predicateComponent = (PredicateComponent)component;
                if (!predicateComponent.isVisible()) {
                    continue;
                }
            }
            if (component.getWidth() >= 80.333336f) {
                final float yOffset = component.getHeight() + 4.0f;
                heightLeft += yOffset;
                heightLeft += yOffset;
                left = true;
            }
            else {
                if (left) {
                    heightLeft += component.getHeight() + 4.0f;
                }
                else {
                    heightRight += component.getHeight() + 4.0f;
                }
                left = (heightRight >= heightLeft);
            }
        }
        final float heightWithComponents = Math.max(heightLeft, heightRight);
        return (heightWithComponents - initHeight > initHeight) ? heightWithComponents : initHeight;
    }
}
