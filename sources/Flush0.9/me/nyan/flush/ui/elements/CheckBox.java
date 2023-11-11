package me.nyan.flush.ui.elements;

import me.nyan.flush.Flush;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;

public class CheckBox extends Gui {
    private String text;
    private int enabledColor;
    private int disabledColor;
    private int currentBackground = 0xFF1E1E1E;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean value;

    public CheckBox(String text, float x, float y, float width, float height, boolean value, int color) {
        this.text = text;
        this.enabledColor = color;
        disabledColor = 0xFF1E1E1E;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.value = value;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int targetColor = value ? enabledColor : disabledColor;
        currentBackground = ColorUtils.animateColor(currentBackground, targetColor, 64);

        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 18);

        Gui.drawRect(x, y, x + width, y + height, currentBackground);
        font.drawXYCenteredString(text, x + width / 2F, y + height / 2F, ColorUtils.contrast(currentBackground));
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            value = !value;
            return true;
        }

        return false;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public int getEnabledColor() {
        return enabledColor;
    }

    public void setEnabledColor(int enabledColor) {
        this.enabledColor = enabledColor;
    }

    public int getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(int disabledColor) {
        this.disabledColor = disabledColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}