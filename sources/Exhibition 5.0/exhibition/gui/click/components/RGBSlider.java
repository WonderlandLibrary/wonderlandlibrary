// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;

public class RGBSlider
{
    public float x;
    public float y;
    public boolean dragging;
    public double dragX;
    public ColorPreview colorPreview;
    public double lastDragX;
    public Colors rgba;
    
    public RGBSlider(final float x, final float y, final ColorPreview colorPreview, final Colors colors) {
        this.x = x;
        this.y = y;
        this.colorPreview = colorPreview;
        int colorShit = 0;
        switch (colors) {
            case RED: {
                colorShit = colorPreview.colorObject.getRed();
                break;
            }
            case GREEN: {
                colorShit = colorPreview.colorObject.getGreen();
                break;
            }
            case BLUE: {
                colorShit = colorPreview.colorObject.getBlue();
                break;
            }
            case ALPHA: {
                colorShit = colorPreview.colorObject.getAlpha();
                break;
            }
        }
        this.rgba = colors;
        this.dragX = colorShit * 60 / 255;
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderDraw(this, x, y);
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderClick(this, x, y, button);
        }
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderMovedOrUp(this, x, y, button);
        }
    }
    
    public enum Colors
    {
        RED, 
        GREEN, 
        BLUE, 
        ALPHA;
    }
}
