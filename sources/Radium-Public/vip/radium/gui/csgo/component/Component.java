package vip.radium.gui.csgo.component;


import vip.radium.utils.render.LockedResolution;

import java.util.ArrayList;
import java.util.List;

public class Component {

    protected final List<Component> children = new ArrayList<>();
    private final Component parent;
    private float x;
    private float y;
    private float width;
    private float height;

    public Component(Component parent,
                     float x,
                     float y,
                     float width,
                     float height) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Component getParent() {
        return parent;
    }

    public void addChild(Component child) {
        children.add(child);
    }

    public void drawComponent(LockedResolution lockedResolution,
                              int mouseX,
                              int mouseY) {
        for (Component child : children) {
            child.drawComponent(lockedResolution, mouseX, mouseY);
        }
    }

    public void onMouseClick(int mouseX,
                             int mouseY,
                             int button) {
        for (Component child : children) {
            child.onMouseClick(mouseX, mouseY, button);
        }
    }

    public void onMouseRelease(int button) {
        for (Component child : children) {
            child.onMouseRelease(button);
        }
    }

    public void onKeyPress(int keyCode) {
        for (Component child : children) {
            child.onKeyPress(keyCode);
        }
    }

    public float getX() {
        Component familyMember = parent;
        float familyTreeX = x;

        while (familyMember != null) {
            familyTreeX += familyMember.x;
            familyMember = familyMember.parent;
        }

        return familyTreeX;
    }

    public void setX(float x) {
        this.x = x;
    }

    public boolean isHovered(int mouseX,
                                int mouseY) {
        float x;
        float y;
        return mouseX >= (x = getX()) && mouseY >= (y = getY()) && mouseX <= x + getWidth() && mouseY <= y + getHeight();
    }

    public float getY() {
        Component familyMember = parent;
        float familyTreeY = y;

        while (familyMember != null) {
            familyTreeY += familyMember.y;
            familyMember = familyMember.parent;
        }

        return familyTreeY;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public List<Component> getChildren() {
        return children;
    }
}
