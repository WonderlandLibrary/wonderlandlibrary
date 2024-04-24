package Method.Client.utils.visual;

import Method.Client.utils.Utils;

import java.awt.*;
import java.io.Serializable;

public class SimpleButton implements Serializable {

    public int x;
    public int y;
    public int width;
    public int height;
    public int id;
    public String text;
    public String tooltip;

    public SimpleButton(int id, int x, int y, int width, int height, String text, String tooltip) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.tooltip = tooltip;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return Utils.ScaledMouseCheck(mouseX, mouseY, this.x, this.y, this.width, this.height) && mouseButton == 0;
    }


    public void drawScreen(int mouseX, int mouseY) {
        RenderUtils.scaledRectOutline(this.x, this.y, this.x + this.width, this.y + this.height, 1, -1);
        RenderUtils.scaledText(this.text, this.x + 2, this.y + 2, -1, true);

        if (!Utils.ScaledMouseCheck(mouseX, mouseY, this.x, this.y, this.width, this.height))
            RenderUtils.scaledRect(this.x, this.y, this.width, this.height, new Color(134, 134, 134, 21).getRGB());
        else {
            RenderUtils.scaledRectOutline(mouseX, mouseY, mouseX + 30, mouseY + 20, 1, new Color(157, 238, 151, 189).getRGB());
            RenderUtils.scaledText(this.tooltip, mouseX + 2, mouseY + 2, -1, true);
            RenderUtils.scaledRect(this.x, this.y, this.width, this.height, new Color(117, 246, 90, 92).getRGB());
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public void updatePos(double x, double y, double width, double height) {
        this.x = (int) x;
        this.y = (int) y;
        this.width = (int) width;
        this.height = (int) height;
    }
}
