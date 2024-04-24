package Method.Client.utils.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.Serializable;

public class TrueFalseBox implements Serializable {

    public int x;
    public int y;
    public int width;
    public int height;
    public boolean state;

    public TrueFalseBox(int x, int y, int width, int height, boolean state) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.state = state;
    }

    public TrueFalseBox() {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (withinBoundsComponent(mouseX, mouseY) && mouseButton == 0) {
            this.state = !this.state;
        }
    }

    public void drawScreen() {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.state ? new Color(100, 252, 95, 188).getRGB() : new Color(243, 45, 38, 189).getRGB());

        RenderUtils.drawRectOutline(this.x, this.y, this.x + this.width, this.y + this.height, 1, -1);

        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.state ? "True" : "False", this.x + 2, this.y, -1);
        GlStateManager.popMatrix();
    }

    private boolean withinBoundsComponent(int mouseX, int mouseY) {
        return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
