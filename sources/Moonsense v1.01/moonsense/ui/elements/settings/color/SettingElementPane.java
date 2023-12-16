// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings.color;

import org.lwjgl.opengl.GL11;

public abstract class SettingElementPane
{
    public int x;
    public int y;
    public int width;
    public int height;
    protected final int min;
    protected final int max;
    private int xOffset;
    private int yOffset;
    public boolean hovered;
    protected boolean dragging;
    
    public SettingElementPane(final int x, final int y, final int width, final int height, final int min, final int max) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.min = min;
        this.max = max;
    }
    
    public void renderPane(final int mouseX, final int mouseY) {
        this.hovered = (mouseX >= this.getX() && mouseX <= this.getX() + this.width && mouseY >= this.getY() && mouseY <= this.getY() + this.height);
        if (this.dragging) {
            this.dragging(mouseX, mouseY);
        }
    }
    
    public void dragging(final int mouseX, final int mouseY) {
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.hovered) {
            this.dragging = true;
        }
        return this.hovered;
    }
    
    public void mouseReleased() {
        this.dragging = false;
    }
    
    protected int getX() {
        return this.x + this.xOffset;
    }
    
    protected int getY() {
        return this.y + this.yOffset;
    }
    
    public SettingElementPane setXOffset(final int xOffset) {
        this.xOffset = xOffset;
        return this;
    }
    
    public SettingElementPane setYOffset(final int yOffset) {
        this.yOffset = yOffset;
        return this;
    }
    
    protected void scissorFunc(final int x, final int y, final int width, final int height) {
        GL11.glScissor(x, y, width, height);
    }
}
