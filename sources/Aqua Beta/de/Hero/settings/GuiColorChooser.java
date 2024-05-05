package de.Hero.settings;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiColorChooser
extends Gui {
    public double x;
    public double y;
    private int width = 100;
    private int height = 80;
    private int finalGlowColor;
    private double hueChooserX;
    public static boolean isHovering = false;
    private double colorChooserX;
    private double colorChooserY;
    public int color = Color.decode((String)"#FFFFFF").getRGB();
    public float[] hsbValues = new float[3];

    public GuiColorChooser(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = Color.red.getRGB();
    }

    public GuiColorChooser(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.setHueChooserByHue(this.hsbValues[0]);
        this.setHueChooserBySB(this.hsbValues[1], this.hsbValues[2]);
        this.color = color;
        Color c = new Color(color);
        this.hsbValues = Color.RGBtoHSB((int)c.getRed(), (int)c.getGreen(), (int)c.getBlue(), (float[])this.hsbValues);
    }

    public void draw2(int mouseX, int mouseY) {
        int backGroundColor = new Color(0, 0, 0, 180).getRGB();
        GuiColorChooser.drawRect2((double)this.x, (double)(this.y - 5.0), (double)(this.x + (double)this.width), (double)(this.y + (double)this.height), (int)backGroundColor);
        int chooserWidth = this.width - 15;
        float i = 0.0f;
        while (i < (float)chooserWidth) {
            float f = 1.0f / (float)chooserWidth * i;
            float finalI = i;
            GuiColorChooser.drawRect2((double)(this.x + 5.0 + (double)i), (double)(this.y + (double)this.height - 12.75), (double)(this.x + 10.0 + (double)i + 0.5), (double)(this.y + (double)this.height - 8.25), (int)Color.HSBtoRGB((float)f, (float)1.0f, (float)1.0f));
            i = (float)((double)i + 0.5);
        }
        int hsbChooserWidth = this.width - 5;
        int hsbChooserHeight = this.height - 25;
        for (float e = 0.0f; e < (float)hsbChooserWidth; e += 0.5f) {
            for (float f = 0.0f; f < (float)hsbChooserHeight; f += 1.0f) {
                float xPos = (float)(this.x + 2.0 + (double)e);
                float yPos = f + 2.0f;
                float satuartion = 1.0f / (float)hsbChooserWidth * e;
                float f2 = 1.0f / (float)hsbChooserHeight * f;
            }
        }
        int max = 255;
        Color onlyHue = new Color(Color.HSBtoRGB((float)this.hsbValues[0], (float)1.0f, (float)1.0f));
        int hueChooserColor = new Color(max - onlyHue.getRed(), max - onlyHue.getGreen(), max - onlyHue.getBlue()).getRGB();
        GuiColorChooser.drawRect2((double)(this.x + 7.0 + this.hueChooserX), (double)(this.y + (double)this.height - 12.75), (double)(this.x + 7.0 + this.hueChooserX + 0.5), (double)(this.y + (double)this.height - 8.25), (int)hueChooserColor);
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

    public void draw(int mouseX, int mouseY) {
        float brightness;
        float yPos;
        float f;
        float e;
        int backGroundColor = new Color(0, 0, 0, 180).getRGB();
        GuiColorChooser.drawRect2((double)this.x, (double)(this.y - 5.0), (double)(this.x + (double)this.width), (double)(this.y + (double)this.height), (int)backGroundColor);
        int chooserWidth = this.width - 15;
        float i = 0.0f;
        while (i < (float)chooserWidth) {
            float f2 = 1.0f / (float)chooserWidth * i;
            float finalI = i;
            GuiColorChooser.drawRect2((double)(this.x + 5.0 + (double)i), (double)(this.y + (double)this.height - 5.0), (double)(this.x + 10.0 + (double)i + 0.5), (double)(this.y + (double)this.height - 8.25), (int)Color.HSBtoRGB((float)f2, (float)1.0f, (float)1.0f));
            i = (float)((double)i + 0.5);
        }
        int hsbChooserWidthRounded = this.width - 10;
        int hsbChooserWidth = this.width - 5;
        int hsbChooserHeight = this.height - 25;
        if (Aqua.setmgr.getSetting("GUIColorPickerGlow").isState() && Aqua.setmgr.getSetting("GUIGlowMode").getCurrentMode().equalsIgnoreCase("Complete") && Aqua.setmgr.getSetting("GUIRoundedPicker").isState()) {
            Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect2Alpha((double)((float)(this.x + 2.0) + 1.0f), (double)(this.y + 6.0 + (double)hsbChooserHeight - 48.0 - 3.0), (double)89.0, (double)(this.y + 5.0 + (double)hsbChooserHeight - this.y - 1.0), (double)3.0, (Color)new Color(this.finalGlowColor)), (boolean)false);
        }
        if (Aqua.setmgr.getSetting("GUIRoundedPicker").isState()) {
            for (e = 0.0f; e < (float)hsbChooserWidthRounded; e += 1.0f) {
                for (f = 0.0f; f < (float)hsbChooserHeight; f += 1.0f) {
                    float xPos = (float)(this.x + 2.0 + (double)e);
                    yPos = f - 5.0f;
                    float satuartion = 1.0f / (float)hsbChooserWidthRounded * e;
                    brightness = 1.0f / (float)hsbChooserHeight * f;
                    this.finalGlowColor = Color.HSBtoRGB((float)this.hsbValues[0], (float)satuartion, (float)brightness);
                    RenderUtil.drawRoundedRect2Alpha((double)((double)xPos + 0.7), (double)(this.y + 5.0 + (double)hsbChooserHeight - (double)yPos - 1.0), (double)5.0, (double)(this.y + 5.0 + (double)hsbChooserHeight - this.y - 55.0), (double)3.0, (Color)new Color(Color.HSBtoRGB((float)this.hsbValues[0], (float)satuartion, (float)brightness)));
                }
            }
        } else {
            for (e = 0.0f; e < (float)hsbChooserWidth; e += 1.0f) {
                for (f = 0.0f; f < (float)hsbChooserHeight; f += 1.0f) {
                    float xPos = (float)(this.x + 2.5 + (double)e);
                    yPos = f;
                    float satuartion = 1.0f / (float)hsbChooserWidth * e;
                    brightness = 1.0f / (float)hsbChooserHeight * f;
                    GuiColorChooser.drawRect2((double)xPos, (double)(this.y + 5.0 + (double)hsbChooserHeight - (double)yPos - 1.0), (double)(xPos + 1.0f), (double)(this.y + 5.0 + (double)hsbChooserHeight - (double)yPos + 1.0 - 1.0), (int)Color.HSBtoRGB((float)this.hsbValues[0], (float)satuartion, (float)brightness));
                }
            }
        }
        int max = 255;
        Color onlyHue = new Color(Color.HSBtoRGB((float)this.hsbValues[0], (float)1.0f, (float)1.0f));
        int hueChooserColor = new Color(max - onlyHue.getRed(), max - onlyHue.getGreen(), max - onlyHue.getBlue()).getRGB();
        Color allColor = new Color(Color.HSBtoRGB((float)this.hsbValues[0], (float)this.hsbValues[1], (float)this.hsbValues[2]));
        int colorChooserColor = new Color(max - allColor.getRed(), max - allColor.getGreen(), max - allColor.getBlue()).getRGB();
        RenderUtil.drawImage((int)((int)(this.x + 5.0 + this.colorChooserX - 2.5)), (int)((int)(this.y - 1.0 + this.colorChooserY + 5.0)), (int)10, (int)10, (ResourceLocation)new ResourceLocation("Aqua/cross.png"));
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
        Aqua.INSTANCE.comfortaa4.drawString("Color :", (float)this.x + 3.0f, (float)this.y - 5.0f, Color.gray.getRGB());
        Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect2Alpha((double)(this.x + (double)this.width - 19.0), (double)(this.y - 4.0), (double)15.0, (double)7.5, (double)2.0, (Color)new Color(this.finalGlowColor)), (boolean)false);
        RenderUtil.drawRoundedRect2Alpha((double)(this.x + (double)this.width - 19.0), (double)(this.y - 4.0), (double)15.0, (double)7.5, (double)2.0, (Color)new Color(this.finalGlowColor));
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

    private boolean mouseOver(int x, int y, int modX, int modY, int modWidth, int modHeight) {
        return x >= modX && x <= modX + modWidth && y >= modY && y <= modY + modHeight;
    }
}
