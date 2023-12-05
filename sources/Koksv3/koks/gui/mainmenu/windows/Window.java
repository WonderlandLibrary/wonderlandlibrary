package koks.gui.mainmenu.windows;

import koks.api.interfaces.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

/**
 * @author kroko
 * @created on 08.11.2020 : 18:00
 */
public abstract class Window implements Wrapper {

    float x, y, dragX, dragY;
    boolean drag, visible, close;
    int width, height, outline;
    int color, colorOutline;

    private Minecraft mc = Minecraft.getMinecraft();
    private FontRenderer fr = mc.fontRendererObj;

    public Window(float x, float y, int width, int height, int outline, int outlineColor, int color, boolean close) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorOutline = outline;
        this.color = color;
        visible = true;
    }

    public Window(float x, float y, int width, int height, int outline, int outlineColor, int color, boolean visible, boolean close) {
        this.x = x;
        this.y = y;
        this.colorOutline = outline;
        this.color = color;
        this.visible = visible;
        this.close = close;
        this.width = width;
        this.height = height;
        this.outline = outline;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return !isHoverClose(mouseX, mouseY) && mouseX >= getX() - getWidth() - getOutline() && mouseX <= getX() + getWidth() + getOutline() && mouseY >= getY() - getHeight() - getOutline() && mouseY <= getY() + getHeight() + getOutline();
    }

    public boolean isHoverClose(int mouseX, int mouseY) {
        return mouseX <= getX() + getWidth() - 1 && mouseX >= getX() + getWidth() - fr.getStringWidth("§lx") && mouseY <= y - height + 1 && mouseY >= y - height + 1 + fr.FONT_HEIGHT;
    }

    public void handleDragging(int mouseX, int mouseY) {
        if (drag && visible) {
            x = dragX + mouseX;
            y = dragY + mouseY;
        }
    }

    public void handleClose(int mouseX, int mouseY) {
        if(isHoverClose(mouseX, mouseY)) {
            visible = false;
        }
    }

    public void handleDrag(int mouseX, int mouseY) {
        if(isHovered(mouseX, mouseY)) {
            drag = true;
        }
    }

    public void drawBackground() {
        if(visible) {
            renderUtil.drawOutlineRect(x - width, y - height, x + width, y + height, outline, colorOutline, color);
            fr.drawString("§lx", x + width - 1, y - height + 1, Color.red.getRGB(), false);
        }
    }

    public abstract void drawScreen(int mouseX, int mouseY);

    public boolean isCloseable() {
        return close;
    }

    public void setCloseable(boolean close) {
        this.close = close;
    }

    public int getOutline() {
        return outline;
    }

    public void setOutline(int outline) {
        this.outline = outline;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDragX() {
        return dragX;
    }

    public void setDragX(float dragX) {
        this.dragX = dragX;
    }

    public float getDragY() {
        return dragY;
    }

    public void setDragY(float dragY) {
        this.dragY = dragY;
    }

    public boolean isDragging() {
        return drag;
    }

    public void setDragging(boolean drag) {
        this.drag = drag;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
