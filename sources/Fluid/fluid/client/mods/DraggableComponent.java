// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods;

import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;

public class DraggableComponent
{
    private int x;
    private int y;
    private int width;
    private int height;
    private int color;
    private int lastX;
    private int lastY;
    private boolean dragging;
    
    public DraggableComponent(final int x, final int y, final int width, final int height, final int color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public int getxPosition() {
        return this.x;
    }
    
    public int getyPosition() {
        return this.y;
    }
    
    public void setxPosition(final int x) {
        this.x = x;
    }
    
    public void setyPosition(final int y) {
        this.y = y;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        this.draggingFix(mouseX, mouseY);
        Gui.drawRect(this.getxPosition(), this.getyPosition(), this.getxPosition() + this.getWidth(), this.getyPosition() + this.getHeight(), this.getColor());
        final boolean mouseOverX = mouseX >= this.getxPosition() && mouseX <= this.getxPosition() + this.getWidth();
        final boolean mouseOverY = mouseY >= this.getyPosition() && mouseY <= this.getyPosition() + this.getHeight();
        if (mouseOverX && mouseOverY && Mouse.isButtonDown(0) && !this.dragging) {
            this.lastX = this.x - mouseX;
            this.lastY = this.y - mouseY;
            this.dragging = true;
        }
    }
    
    private void draggingFix(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
            if (!Mouse.isButtonDown(0)) {
                this.dragging = false;
            }
        }
    }
}
