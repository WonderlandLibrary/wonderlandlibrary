// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import moonsense.config.utils.Position;

public class Rectangle
{
    private int x;
    private int y;
    private int width;
    private int height;
    public static final Rectangle ZERO;
    
    static {
        ZERO = ofDimensions(0, 0);
    }
    
    public Rectangle(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public static Rectangle ofDimensions(final int width, final int height) {
        return new Rectangle(0, 0, width, height);
    }
    
    public Rectangle offset(final int x, final int y) {
        return new Rectangle(this.x + x, this.y + y, this.width, this.height);
    }
    
    public boolean contains(final Position position) {
        return this.contains((int)position.getX(), (int)position.getY());
    }
    
    public boolean contains(final int x, final int y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }
    
    public void fill(final Color color) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, color.getRGB());
    }
    
    public void stroke(final Color color) {
        GuiUtils.drawRectOutline((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height), color.getRGB());
    }
    
    public Rectangle multiply(final float scale) {
        return new Rectangle(this.x, this.y, (int)(this.width * scale), (int)(this.height * scale));
    }
    
    public Rectangle(final int x, final int y) {
        this(x, y, 1, 1);
    }
    
    public int getEndX() {
        return this.x + this.width;
    }
    
    public int getEndY() {
        return this.y + this.height;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
}
