// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop.comp;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.gui.ScaledResolution;
import java.util.ArrayList;
import java.util.List;

public class Component
{
    protected final List<Component> children;
    private final Component parent;
    private final String name;
    private int x;
    private int y;
    private int width;
    private int height;
    
    public Component(final Component parent, final String name, final int x, final int y, final int width, final int height) {
        this.children = new ArrayList<Component>();
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
    
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        for (final Component child : this.children) {
            child.drawComponent(scaledResolution, mouseX, mouseY);
        }
    }
    
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        for (final Component child : this.children) {
            child.onMouseClick(mouseX, mouseY, button);
        }
    }
    
    public void onMouseRelease(final int button) {
        for (final Component child : this.children) {
            child.onMouseRelease(button);
        }
    }
    
    public void onKeyPress(final int keyCode) {
        for (final Component child : this.children) {
            child.onKeyPress(keyCode);
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getX() {
        Component familyMember = this.parent;
        int familyTreeX = this.x;
        while (familyMember != null) {
            familyTreeX += familyMember.x;
            familyMember = familyMember.parent;
        }
        return familyTreeX;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    protected boolean isHovered(final int mouseX, final int mouseY) {
        final int x;
        final int y;
        return mouseX >= (x = this.getX()) && mouseY >= (y = this.getY()) && mouseX < x + this.getWidth() && mouseY < y + this.getHeight();
    }
    
    public int getY() {
        Component familyMember = this.parent;
        int familyTreeY = this.y;
        while (familyMember != null) {
            familyTreeY += familyMember.y;
            familyMember = familyMember.parent;
        }
        return familyTreeY;
    }
    
    protected int getBackgroundColor(final boolean hovered) {
        return hovered ? new Color(0, 0, 0, 230).getRGB() : new Color(0, 0, 0, 200).getRGB();
    }
    
    protected int getSecondaryBackgroundColor(final boolean hovered) {
        return hovered ? new Color(0, 0, 0, 230).getRGB() : new Color(0, 0, 0, 200).getRGB();
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public List<Component> getChildren() {
        return this.children;
    }
}
