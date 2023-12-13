package vip.radium.gui.csgo.component.impl;

import net.minecraft.client.gui.Gui;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.ExpandableComponent;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.impl.sub.key.KeyBindComponent;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;

public final class GroupBoxComponent extends Component {

    private final String name;

    public GroupBoxComponent(Component parent, String name, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
        this.name = name;
    }

    @Override
    public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
        final float x = getX();
        final float y = getY();
        final float width = getWidth();
        final float height = getHeight();

        final float length = SkeetUI.GROUP_BOX_HEADER_RENDERER.getWidth(name);

        Gui.drawRect(x, y, x + width, y + height,
                SkeetUI.getColor(0x0C0C0C));
        Gui.drawRect(x + 0.5F, y + 0.5F, x + width - 0.5F, y + height - 0.5F,
                SkeetUI.getColor(0x282828));
        Gui.drawRect(x + 4, y, x + 4 + length + 2, y + 1,
                SkeetUI.getColor(0x171717));
        Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1,
                SkeetUI.getColor(0x171717));

        if (SkeetUI.shouldRenderText())
            RenderingUtils.drawOutlinedString(SkeetUI.GROUP_BOX_HEADER_RENDERER, name, x + 5, y - 0.5F,
                    SkeetUI.getColor(0xDCDCDC), SkeetUI.getColor(0));

        float childYLeft = SkeetUI.ENABLE_BUTTON_Y_OFFSET;
        float childYRight = SkeetUI.ENABLE_BUTTON_Y_OFFSET;
        boolean left = true;
        final float right = SkeetUI.HALF_GROUP_BOX + SkeetUI.GROUP_BOX_LEFT_MARGIN * 3;
        for (Component component : children) {
            if (component instanceof PredicateComponent) {
                PredicateComponent predicateComponent = (PredicateComponent) component;
                if (!predicateComponent.isVisible())
                    continue;
            } else if (component instanceof KeyBindComponent) {
                continue;
            }

            if (component.getWidth() >= SkeetUI.HALF_GROUP_BOX * 2) {
                component.setX(SkeetUI.GROUP_BOX_LEFT_MARGIN);
                component.setY(childYLeft);
                component.drawComponent(resolution, mouseX, mouseY);
                final float yOffset = component.getHeight() + SkeetUI.ENABLE_BUTTON_Y_GAP;
                childYLeft += yOffset;
                childYRight += yOffset;
                left = true;
            } else {
                component.setX(left ? SkeetUI.GROUP_BOX_LEFT_MARGIN : right);
                component.setY(left ? childYLeft : childYRight);
                component.drawComponent(resolution, mouseX, mouseY);

                if (left)
                    childYLeft += component.getHeight() + SkeetUI.ENABLE_BUTTON_Y_GAP;
                else
                    childYRight += component.getHeight() + SkeetUI.ENABLE_BUTTON_Y_GAP;
                left = childYRight >= childYLeft;
            }
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (Component child : getChildren()) {
            if (child instanceof ExpandableComponent) {
                ExpandableComponent expandable = (ExpandableComponent) child;

                if (expandable.isExpanded()) {
                    final float x = expandable.getExpandedX();
                    final float y = expandable.getExpandedY();
                    if (mouseX >= x && mouseY > y && mouseX <= x + expandable.getExpandedWidth() && mouseY < y + expandable.getExpandedHeight()) {
                        child.onMouseClick(mouseX, mouseY, button);
                        return;
                    }
                }
            }
        }

        super.onMouseClick(mouseX, mouseY, button);
    }

    public boolean isHoveredEntire(int mouseX, int mouseY) {
        for (Component child : getChildren()) {
            if (child instanceof ExpandableComponent) {
                ExpandableComponent expandable = (ExpandableComponent) child;

                if (expandable.isExpanded()) {
                    final float x = expandable.getExpandedX();
                    final float y = expandable.getExpandedY();
                    if (mouseX >= x && mouseY >= y && mouseX <= x + expandable.getExpandedWidth() && mouseY <= y + expandable.getExpandedHeight()) {
                        return true;
                    }
                }
            }
        }


        return super.isHovered(mouseX, mouseY);
    }

    @Override
    public float getHeight() {
        final float initHeight = super.getHeight();
        float heightLeft = initHeight;
        float heightRight = initHeight;


        boolean left = true;
        for (Component component : getChildren()) {
            if (component instanceof PredicateComponent) {
                PredicateComponent predicateComponent = (PredicateComponent) component;
                if (!predicateComponent.isVisible())
                    continue;
            }

            if (component.getWidth() >= SkeetUI.HALF_GROUP_BOX * 2) {
                final float yOffset = component.getHeight() + SkeetUI.ENABLE_BUTTON_Y_GAP;
                heightLeft += yOffset;
                heightLeft += yOffset;
                left = true;
            } else {
                if (left) {
                    heightLeft += component.getHeight() + SkeetUI.ENABLE_BUTTON_Y_GAP;
                } else {
                    heightRight += component.getHeight() + SkeetUI.ENABLE_BUTTON_Y_GAP;
                }
                left = heightRight >= heightLeft;
            }
        }


        final float heightWithComponents = Math.max(heightLeft, heightRight);

        return heightWithComponents - initHeight > initHeight ? heightWithComponents : initHeight;
    }
}
