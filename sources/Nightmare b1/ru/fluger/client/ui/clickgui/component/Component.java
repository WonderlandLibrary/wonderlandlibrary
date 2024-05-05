// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import ru.fluger.client.helpers.Helper;

public class Component implements Helper
{
    public final Component parent;
    protected final List<Component> components;
    private final String name;
    private double x;
    private double y;
    private float width;
    private float height;
    
    public Component(final Component parent, final String name, final float x, final float y, final float width, final float height) {
        this.components = new ArrayList<Component>();
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
    
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        for (final Component child : this.components) {
            child.drawComponent(scaledResolution, mouseX, mouseY);
        }
    }
    
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        for (final Component child : this.components) {
            child.onMouseClick(mouseX, mouseY, button);
        }
    }
    
    public void onMouseRelease(final int button) {
        for (final Component child : this.components) {
            child.onMouseRelease(button);
        }
    }
    
    public void onKeyPress(final int keyCode) {
        for (final Component child : this.components) {
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
    
    public void setX(final float x) {
        this.x = x;
    }
    
    protected boolean isHovered(final double mouseX, final double mouseY) {
        final double x;
        final double y;
        return mouseX >= (x = this.getX()) && mouseY >= (y = this.getY()) && mouseX < x + this.getWidth() && mouseY < y + this.getHeight();
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
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = (float)width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = (float)height;
    }
    
    public List<Component> getComponents() {
        return this.components;
    }
}
