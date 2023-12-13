package vip.radium.gui.csgo.component;

import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.impl.GroupBoxComponent;
import vip.radium.utils.render.LockedResolution;

import java.util.List;

public abstract class TabComponent extends Component {

    private final String name;

    public TabComponent(Component parent, String name, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
        setupChildren();
        this.name = name;
    }

    public abstract void setupChildren();

    @Override
    public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
        SkeetUI.FONT_RENDERER.drawStringWithShadow(name, getX() + SkeetUI.GROUP_BOX_MARGIN,
                getY() + SkeetUI.GROUP_BOX_MARGIN - 3, SkeetUI.getColor(0xFFFFFF));

        float x = SkeetUI.GROUP_BOX_MARGIN;

        final List<Component> children = getChildren();

        for (int i = 0; i < children.size(); i++) {
            final Component child = children.get(i);

            child.setX(x);
            if (i < 3)
                child.setY(SkeetUI.GROUP_BOX_MARGIN * 2 - 2);
            child.drawComponent(resolution, mouseX, mouseY);

            x += SkeetUI.GROUP_BOX_MARGIN + SkeetUI.GROUP_BOX_WIDTH;

            if (x + SkeetUI.GROUP_BOX_MARGIN + SkeetUI.GROUP_BOX_WIDTH > SkeetUI.USABLE_AREA_WIDTH)
                x = SkeetUI.GROUP_BOX_MARGIN;

            if (i > 2) {
                Component componentAbove;
                int above = i - 3;

                int totalY = SkeetUI.GROUP_BOX_MARGIN * 2 - 2;

                do {
                    componentAbove = getChildren().get(above);
                    totalY += componentAbove.getHeight() + SkeetUI.GROUP_BOX_MARGIN;
                    above -= 3;
                } while (above >= 0);

                child.setY(totalY);
            }
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (Component groupBox : getChildren()) {
            for (Component child : groupBox.getChildren()) {
                if (child instanceof ExpandableComponent) {
                    ExpandableComponent expandable = (ExpandableComponent) child;

                    if (expandable.isExpanded()) {
                        final float x = expandable.getExpandedX();
                        final float y = expandable.getExpandedY();
                        if (mouseX >= x && mouseY > y && mouseX <= x + expandable.getExpandedWidth() && mouseY < y + expandable.getExpandedHeight()) {
                            child.onMouseClick(mouseX, mouseY, button);
                            return;
                        }
                        //Close other expanded tabs
                        //expandable.setExpanded(false);
                    }
                }
            }
        }

        for (Component child : getChildren()) {
            if (child.isHovered(mouseX, mouseY)) {
                child.onMouseClick(mouseX, mouseY, button);
                return;
            }
        }


        super.onMouseClick(mouseX, mouseY, button);
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        for (Component child : getChildren()) {
            if (child instanceof GroupBoxComponent) {
                final GroupBoxComponent groupBox = (GroupBoxComponent) child;

                if (groupBox.isHoveredEntire(mouseX, mouseY)) {
                    return true;
                }
            }
        }

        return super.isHovered(mouseX, mouseY);
    }
}
