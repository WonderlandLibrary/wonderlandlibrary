// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.tomorrow;

import java.awt.Color;

public class Rect
{
    float x;
    float y;
    float width;
    float height;
    Color color;
    Runnable runnable;
    
    public Rect(final float x, final float y, final float width, final float height, final Color color, final Runnable runnable) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.runnable = runnable;
    }
    
    public void render(final float mouseX, final float mouseY) {
        if (isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY)) {
            this.runnable.run();
        }
        RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.color.getRGB());
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final float mouseX, final float mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
