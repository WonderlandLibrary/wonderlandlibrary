package today.flux.gui.altMgr.dialog;

import org.lwjgl.input.Mouse;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.utility.ColorUtils;

public class DialogWindow {

    public float x;
    public float y;

    // Dragging
    public float x2;
    public float y2;
    public boolean drag = false;
    public today.flux.gui.crink.NewClickGUI.MouseHandler handler = new today.flux.gui.crink.NewClickGUI.MouseHandler(0);

    public float width;
    public float height;
    public String title;
    public boolean destroy = false;

    public DialogWindow(float x, float y, float width, float height, String title) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void draw(int mouseX, int mouseY) {
        dragWindow(mouseX, mouseY);

        GuiRenderUtils.drawRect(this.x, this.y, width, 16, 0xff4286f5);
        GuiRenderUtils.drawRect(this.x, this.y + 15, width, height - 15, 0xff32353b);
        FontManager.sans16_2.drawString(this.title, this.x + 3, this.y + 1, ColorUtils.WHITE.c);
    }

    public void updateScreen() {

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {

    }

    public void dragWindow(int mouseX, int mouseY) {
        if (!Mouse.isButtonDown(0) && this.drag) {
            this.drag = false;
        }

        if (RenderUtil.isHovering(mouseX, mouseY, x, y, x + width, y + 16) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }

        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }

    public void makeCenter(float scrW, float scrH) {
        this.x = scrW / 2f - (width / 2f);
        this.y = scrH / 2f - (height / 2f);
    }

    public void destroy() {
        this.destroy = true;
    }
}
