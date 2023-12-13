package me.nyan.flush.ui.elements;

import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ColorPicker extends Gui {
    private final float x, y, width, hueHeight, sbHeight;
    private int border = 4;
    private int speed = 4;

    private float hue, saturation, brightness;

    private boolean hueSelector, sbSelector;

    private Slider alphaSlider;

    public ColorPicker(float x, float y, float width, float hueHeight, float sbHeight, int argb, boolean showAlpha) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.hueHeight = hueHeight;
        this.sbHeight = sbHeight;

        if (showAlpha) {
            alphaSlider = new Slider("Alpha", 0, 0, width, 20, 0, 255,
                    ColorUtils.getAlpha(argb), 1, -1);
        }

        int r = ColorUtils.getRed(argb);
        int g = ColorUtils.getGreen(argb);
        int b = ColorUtils.getBlue(argb);
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }

    public void draw(int mouseX, int mouseY) {
        float y = this.y;
        drawHueSelector(x, y, width, hueHeight, mouseX, mouseY);
        y += hueHeight + border;

        drawSBSelector(x, y, width, sbHeight, mouseX, mouseY);
        y += sbHeight + border;

        if (alphaSlider != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            alphaSlider.drawScreen(mouseX - (int) x, mouseY - (int) y);
            GlStateManager.popMatrix();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0) {
            return;
        }

        float y = this.y;

        if (MouseUtils.hovered(mouseX, mouseY, x, y, x + width, y + hueHeight)) {
            hueSelector = true;
        }
        y += hueHeight + border;

        if (MouseUtils.hovered(mouseX, mouseY, x, y, x + width, y + sbHeight)) {
            sbSelector = true;
        }
        y += sbHeight + border;

        if (alphaSlider != null) {
            alphaSlider.mouseClicked(mouseX - (int) x, mouseY - (int) y, mouseButton);
        }
    }

    public void mouseReleased(int mouseButton) {
        if (mouseButton != 0) {
            return;
        }

        hueSelector = false;
        sbSelector = false;

        if (alphaSlider != null) {
            alphaSlider.mouseReleased();
        }
    }

    private void drawHueSelector(float x, float y, float width, float height, int mouseX, int mouseY) {
        for (float currentX = 0; currentX < width; currentX++) {
            float hue = currentX / width;
            int color = Color.HSBtoRGB(hue, 1, 1);
            RenderUtils.fillRectangle(x + currentX, y + height / 2F - 2, 1, 4, color);
        }

        RenderUtils.fillCircle(x + hue * width, y + height / 2F, height / 2F, -1);

        if (hueSelector) {
            this.hue = MathUtils.clamp((mouseX - x) / width, 0, 1);
        }
    }

    private void drawSBSelector(float x, float y, float width, float height, double mouseX, double mouseY) {
        glPointSize(speed);
        glBegin(GL_POINTS);
        for (float currentY = 0; currentY < height; currentY += speed / 2F) {
            for (float currentX = 0; currentX < width; currentX += speed / 2F) {
                float saturation = currentX / width;
                float brightness = 1 - currentY / height;
                RenderUtils.glColor(Color.HSBtoRGB(hue, saturation, brightness));
                glVertex2f(speed / 4F + x + currentX, speed / 4F + y + currentY);
            }
        }
        glEnd();

        if (sbSelector) {
            saturation = MathUtils.clamp((float) (mouseX - x) / width, 0, 1);
            brightness = MathUtils.clamp(1 - (float) (mouseY - y) / height, 0, 1);
        }

        RenderUtils.fillCircle(x + saturation * width, y + (1 - brightness) * height,
                3, getColor());
        RenderUtils.drawCircle(x + saturation * width, y + (1 - brightness) * height,
                3, -1, 1);

        RenderUtils.glColor(-1);
    }

    public int getHeight() {
        return (int) (hueHeight + border + sbHeight + (alphaSlider != null ? border + alphaSlider.getHeight() : 0));
    }

    public int getColor() {
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setColor(int argb) {
        int r = ColorUtils.getRed(argb);
        int g = ColorUtils.getGreen(argb);
        int b = ColorUtils.getBlue(argb);
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }
}
