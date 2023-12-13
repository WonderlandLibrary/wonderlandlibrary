/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.components;

import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.components.ColorPreview;
import me.arithmo.gui.click.ui.UI;
import me.arithmo.management.ColorObject;

public class RGBSlider {
    public float x;
    public float y;
    public boolean dragging;
    public double dragX;
    public ColorPreview colorPreview;
    public double lastDragX;
    public Colors rgba;

    public RGBSlider(float x, float y, ColorPreview colorPreview, Colors colors) {
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
            }
        }
        this.rgba = colors;
        this.dragX = colorShit * 90 / 255;
    }

    public void draw(float x, float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderDraw(this, x, y);
        }
    }

    public void mouseClicked(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderClick(this, x, y, button);
        }
    }

    public void mouseReleased(int x, int y, int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderMovedOrUp(this, x, y, button);
        }
    }

    public static enum Colors {
        RED,
        GREEN,
        BLUE,
        ALPHA;
        

        private Colors() {
        }
    }

}

