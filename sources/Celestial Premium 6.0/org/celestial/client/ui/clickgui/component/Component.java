/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui.component;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import org.celestial.client.helpers.Helper;

public class Component
implements Helper {
    public final Component parent;
    protected final List<Component> components = new ArrayList<Component>();
    private final String name;
    private double x;
    private double y;
    private float width;
    private float height;

    public Component(Component parent, String name, float x, float y, float width, float height) {
        this.parent = parent;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Component getParent() {
        return this.parent;
    }

    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        for (Component child : this.components) {
            child.drawComponent(scaledResolution, mouseX, mouseY);
        }
    }

    public void onMouseClick(int mouseX, int mouseY, int button) {
        for (Component child : this.components) {
            child.onMouseClick(mouseX, mouseY, button);
        }
    }

    public void onMouseRelease(int button) {
        for (Component child : this.components) {
            child.onMouseRelease(button);
        }
    }

    public void onKeyPress(int keyCode) {
        for (Component child : this.components) {
            child.onKeyPress(keyCode);
        }
    }

    public String getName() {
        return this.name;
    }

    public double getX() {
        Component familyMember = this.parent;
        double familyTreeX = this.x;
        while (familyMember != null) {
            familyTreeX += familyMember.x;
            familyMember = familyMember.parent;
        }
        return familyTreeX;
    }

    public void setX(float x) {
        this.x = x;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean isHovered(double mouseX, double mouseY) {
        double d;
        double d2;
        double x = this.getX();
        if (!(mouseX >= d2)) return false;
        double y = this.getY();
        if (!(mouseY >= d)) return false;
        if (!(mouseX < x + (double)this.getWidth())) return false;
        if (!(mouseY < y + (double)this.getHeight())) return false;
        return true;
    }

    public double getY() {
        Component familyMember = this.parent;
        double familyTreeY = this.y;
        while (familyMember != null) {
            familyTreeY += familyMember.y + 0.01;
            familyMember = familyMember.parent;
        }
        return familyTreeY;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Component> getComponents() {
        return this.components;
    }
}

