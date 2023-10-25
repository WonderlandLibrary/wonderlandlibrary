package de.Hero.settings;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

public class GuiColorChooser2
extends Gui {
    public double x;
    public double y;
    private int width = 145;
    private int height = 80;
    private double hueChooserX;
    private double colorChooserX;
    private double colorChooserY;
    public int color = Color.decode((String)"#FFFFFF").getRGB();
    public float[] hsbValues = new float[3];

    public GuiColorChooser2(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = Color.red.getRGB();
    }

    public GuiColorChooser2(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.setHueChooserByHue(this.hsbValues[0]);
        this.setHueChooserBySB(this.hsbValues[1], this.hsbValues[2]);
        this.color = color;
        Color c = new Color(color);
        this.hsbValues = Color.RGBtoHSB((int)c.getRed(), (int)c.getGreen(), (int)c.getBlue(), (float[])this.hsbValues);
    }

    public void draw(int mouseX, int mouseY) {
        int backGroundColor = new Color(0, 0, 0, 180).getRGB();
        int chooserWidth = this.width - 15;
        float i = 0.0f;
        while (i < (float)chooserWidth) {
            float f = 1.0f / (float)chooserWidth * i;
            float finalI = i;
            GuiColorChooser2.drawRect2((double)(this.x + 5.0 + (double)i), (double)(this.y + (double)this.height - 12.75), (double)(this.x + 10.0 + (double)i + 0.5), (double)(this.y + (double)this.height - 8.25), (int)Color.HSBtoRGB((float)f, (float)1.0f, (float)1.0f));
            i = (float)((double)i + 0.5);
        }
        int hsbChooserWidth = this.width - 5;
        int hsbChooserHeight = this.height - 25;
        for (float e = 0.0f; e < (float)hsbChooserWidth; e += 1.0f) {
            for (float f = 0.0f; f < (float)hsbChooserHeight; f += 1.0f) {
                float xPos = (float)(this.x + 2.5 + (double)e);
                float yPos = f;
                float satuartion = 1.0f / (float)hsbChooserWidth * e;
                float brightness = 1.0f / (float)hsbChooserHeight * f;
                GuiColorChooser2.drawRect2((double)xPos, (double)(this.y + 5.0 + (double)hsbChooserHeight - (double)yPos - 1.0), (double)(xPos + 1.0f), (double)(this.y + 5.0 + (double)hsbChooserHeight - (double)yPos + 1.0 - 1.0), (int)Color.HSBtoRGB((float)this.hsbValues[0], (float)satuartion, (float)brightness));
            }
        }
        int max = 255;
        Color onlyHue = new Color(Color.HSBtoRGB((float)this.hsbValues[0], (float)1.0f, (float)1.0f));
        int hueChooserColor = new Color(max - onlyHue.getRed(), max - onlyHue.getGreen(), max - onlyHue.getBlue()).getRGB();
        GuiColorChooser2.drawRect2((double)(this.x + 7.0 + this.hueChooserX), (double)(this.y + (double)this.height - 12.75), (double)(this.x + 7.0 + this.hueChooserX + 0.5), (double)(this.y + (double)this.height - 8.25), (int)hueChooserColor);
        Color allColor = new Color(Color.HSBtoRGB((float)this.hsbValues[0], (float)this.hsbValues[1], (float)this.hsbValues[2]));
        int colorChooserColor = new Color(max - allColor.getRed(), max - allColor.getGreen(), max - allColor.getBlue()).getRGB();
        if (Mouse.isButtonDown((int)0)) {
            if ((double)mouseY >= this.y + 5.0 + (double)this.height - 20.0 && (double)mouseY <= this.y + 5.0 + (double)this.height - 10.0) {
                double diff = (double)mouseX - this.x - 5.0;
                if (diff > (double)this.width - 10.5) {
                    diff = (double)this.width - 10.5;
                }
                if (diff < 0.0) {
                    diff = 0.0;
                }
                this.hueChooserX = diff;
                this.setHueChooserByHue((float)((double)(1.0f / (float)(this.width - 10)) * this.hueChooserX));
            }
            if ((double)mouseX >= this.x - 3.0 && (double)mouseX <= this.x + (double)this.width + 5.0 && (double)mouseY >= this.y + 5.0 && (double)mouseY <= this.y + (double)this.height - 20.0) {
                double diffY;
                double diffX = (double)mouseX - this.x - 3.0;
                if (diffX > (double)(this.width - 10)) {
                    diffX = this.width - 10;
                }
                if (diffX < 0.0) {
                    diffX = 0.0;
                }
                if ((diffY = (double)mouseY - this.y - 5.0) > 55.0) {
                    diffY = 55.0;
                }
                if (diffY < 0.25) {
                    diffY = 0.25;
                }
                this.colorChooserX = diffX - 3.0;
                this.colorChooserY = diffY;
                this.hsbValues[1] = (float)((double)(1.0f / (float)(this.width - 10)) * this.colorChooserX);
                this.hsbValues[2] = 1.0f - (float)(0.0181818176060915 * this.colorChooserY);
            }
        }
        this.color = Color.HSBtoRGB((float)this.hsbValues[0], (float)this.hsbValues[1], (float)this.hsbValues[2]);
    }

    public void setHue(float hue) {
        if (hue > 1.0f) {
            hue = 1.0f;
        }
        this.hsbValues[0] = hue;
    }

    public void setHueChooserByHue(float hue) {
        this.hueChooserX = (float)(this.width - 10) * hue;
        this.setHue(hue);
    }

    public void setHueChooserBySB(float s, float b) {
        this.colorChooserX = (float)(this.width - 10) * s;
        this.colorChooserY = 55.0f - 55.0f * b;
    }

    public void setSaturation(float sat) {
        if (sat > 1.0f) {
            sat = 1.0f;
        }
        this.hsbValues[1] = sat;
    }

    public void setBrightness(float bright) {
        if (bright > 1.0f) {
            bright = 1.0f;
        }
        this.hsbValues[2] = bright;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
