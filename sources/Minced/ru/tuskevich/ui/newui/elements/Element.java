// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;

public abstract class Element
{
    protected Minecraft mc;
    public double x;
    public double y;
    public double width;
    public double height;
    public double addHeight;
    protected final List<Element> elements;
    
    public Element() {
        this.mc = Minecraft.getMinecraft();
        this.elements = new ArrayList<Element>();
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getWidth() {
        return this.width;
    }
    
    public void setWidth(final double width) {
        this.width = width;
    }
    
    public double getHeight() {
        if (!this.isShown()) {
            return 0.0;
        }
        return this.height;
    }
    
    public void setHeight(final double height) {
        this.height = height;
    }
    
    public boolean collided(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
    
    public boolean collided(final int mouseX, final int mouseY, final double posX, final double posY, final float width, final float height) {
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }
    
    protected boolean isHovered(final double mouseX, final double mouseY) {
        final double x;
        final double y;
        return mouseX >= (x = this.getX()) && mouseY >= (y = this.getY()) && mouseX < x + this.getWidth() && mouseY < y + this.getHeight();
    }
    
    public void handleMouseInput() {
    }
    
    public abstract void draw(final int p0, final int p1);
    
    public void mouseClicked(final int x, final int y, final int button) {
    }
    
    public boolean isShown() {
        return true;
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
    }
    
    public void keypressed(final char c, final int key) {
    }
}
