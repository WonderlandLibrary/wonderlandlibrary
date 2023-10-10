package today.flux.gui.loginui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.utility.ColorUtils;

public class PopupDialog {
    public float x = 0;
    public float y = 0;

    public float x2;
    public float y2;
    public boolean drag = false;
    public today.flux.gui.crink.NewClickGUI.MouseHandler handler = new today.flux.gui.crink.NewClickGUI.MouseHandler(0);

    public String title;
    public String message;
    public float windowWidth;
    public float windowHeight;

    public boolean hovering = false;
    public boolean destroy = false;

    public static Minecraft mc = Minecraft.getMinecraft();

    public PopupDialog(String title, String message, float windowWidth, float windowHeight, boolean center) {
        this.title = title;
        this.message = message;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        if(center) {
            this.center();
        }
    }

    public void render(int mouseX, int mouseY) {
        this.dragWindow(mouseX, mouseY);

        GuiRenderUtils.drawRoundedRect(this.x, this.y, windowWidth, windowHeight, 2, 0xff5c7082, 0.1f, 0x00000000);
        GuiRenderUtils.drawRoundedRect(this.x, this.y, windowWidth, 15, 2, 0xff5db4ea, 0.1f, 0x00000000);
        RenderUtil.drawRect(this.x, this.y + 13, this.x + windowWidth, this.y + 16, 0xff5db4ea);

        FontManager.sans16.drawString(this.title, this.x + 5, this.y + 2, ColorUtils.WHITE.c);
        float stringWidth = FontManager.sans16.getStringWidth(this.message);
        FontManager.sans16.drawString(this.message, this.x + (windowWidth / 2) - (stringWidth / 2), this.y + (windowHeight / 2) - 7, ColorUtils.WHITE.c);

        int buttonState = RenderUtil.isHovering(mouseX, mouseY, this.x + (windowWidth / 2) - (windowWidth / 4), this.y + windowHeight - 18, this.x + (windowWidth / 2) + (windowWidth / 4), this.y + windowHeight - 3) ? 0xff5db4ea : 0xff455b6b;

        GuiRenderUtils.drawRoundedRect(this.x + (windowWidth / 2) - (windowWidth / 4), this.y + windowHeight - 18, (windowWidth / 2), 15, 2, buttonState, 0.1f, buttonState);

        FontManager.sans16.drawString("OK",this.x + (windowWidth / 2) - (FontManager.sans16.getStringWidth("OK") / 2), this.y + windowHeight - 16, ColorUtils.WHITE.c);

    }

    public void doClick(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovering(mouseX, mouseY, this.x + (windowWidth / 2) - (windowWidth / 4), this.y + windowHeight - 18, this.x + (windowWidth / 2) + (windowWidth / 4), this.y + windowHeight - 3) && mouseButton == 0) {
            this.destroy = true;
        }
    }

    public void dragWindow(int mouseX, int mouseY) {

        if (!Mouse.isButtonDown(0) && this.drag) {
            this.drag = false;
        }

        if (RenderUtil.isHovering(mouseX, mouseY, x, y, x + windowWidth, y + 16) && this.handler.canExcecute()) {
            this.drag = true;
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }

        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }
    }


    public void center() {
        ScaledResolution resolution = new ScaledResolution(mc);
        this.x = resolution.getScaledWidth() / 2f - (windowWidth / 2f);
        this.y = resolution.getScaledHeight() / 2f - (windowHeight / 2f);
    }
}
