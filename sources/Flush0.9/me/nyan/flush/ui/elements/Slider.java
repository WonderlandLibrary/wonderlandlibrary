package me.nyan.flush.ui.elements;

import me.nyan.flush.Flush;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

public class Slider extends Gui {
    private String text;
    private int color;
    private int background;
    private float x;
    private float y;
    private float width;
    private float height;
    private double value;
    private final double stepValue;
    private double min;
    private double max;
    private boolean dragging;

    public Slider(String text, float x, float y, float width, float height, double min, double max, double value, double stepValue, int color) {
        dragging = false;
        this.text = text;
        this.color = color;
        background = 0xFF1E1E1E;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.min = min;
        this.max = max;
        this.stepValue = stepValue;
        setValue(value);
    }

    public void drawScreen(int mouseX, int mouseY) {
        double percentage = (value - min) / (max - min);

        Gui.drawRect(x, y, x + width, y + height, background);
        Gui.drawRect(x, y, x + (percentage * width), y + height, color);

        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 16);
        int stringWidth = font.getStringWidth(text);
        int currentWidth = 0;
        for (char ch : text.toCharArray()) {
            font.drawString(
                    String.valueOf(ch),
                    x + getWidth() / 2F - stringWidth / 2F + currentWidth,
                    y + height / 2F - font.getFontHeight() + 1,
                    (x + getWidth() / 2F - stringWidth / 2F + currentWidth + font.getStringWidth(String.valueOf(ch))
                            < x + getWidth() * percentage ? ColorUtils.contrast(color)
                            : ColorUtils.contrast(background))
            );
            currentWidth += font.getStringWidth(String.valueOf(ch));
        }

        String stringValue = (value % 1.0 == 0.0 ? String.valueOf(value).replace(".0", "") :
                String.valueOf(value));
        int stringWidth2 = font.getStringWidth(stringValue);
        int currentWidth2 = 0;
        for (char ch : stringValue.toCharArray()) {
            font.drawString(
                    String.valueOf(ch),
                    x + getWidth() / 2F - stringWidth2 / 2F + currentWidth2,
                    y + height / 2F - 1,
                    (x + getWidth() / 2F - stringWidth2 / 2F + currentWidth2 + font.getStringWidth(String.valueOf(ch))
                            < x + getWidth() * percentage ? ColorUtils.contrast(color)
                            : ColorUtils.contrast(background))
            );
            currentWidth2 += font.getStringWidth(String.valueOf(ch));
        }

        if (dragging) {
            double diff = max - min;
            double val = min + (MathHelper.clamp_double((mouseX - x) / width, 0, 1)) * diff;
            setValue(Math.round(val * 100D) / 100D);
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            dragging = true;
            return true;
        }

        return false;
    }

    public void mouseReleased() {
        dragging = false;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public double getValue() {
        return value;
    }

    public int getValueInt() {
        return (int) value;
    }

    public void setValue(double value) {
        this.value = MathUtils.snapToStep(value, stepValue);
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
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