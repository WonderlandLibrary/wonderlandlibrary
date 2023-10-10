package today.flux.gui.altMgr.dialog.impl;

import today.flux.gui.altMgr.dialog.DialogWindow;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.utility.ColorUtils;

import java.awt.*;

public class InfoDialog extends DialogWindow {

    public String message;
    public String buttonText;

    public InfoDialog(float x, float y, float width, float height, String title, String message) {
        super(x, y, width, height, title);
        this.message = message;
        this.buttonText = "Cancel";
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        float stringWidth = FontManager.sans16_2.getStringWidth(this.message);
        FontManager.sans16_2.drawString(this.message, this.x + (width / 2) - (stringWidth / 2), this.y + (height / 2) - 7, ColorUtils.WHITE.c);

        int acceptButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + (width / 2) - 50, this.y + height - 18, this.x + (width / 2) + 50, this.y + height - 3) ? new Color(0xff4286f5).darker().getRGB() : 0xff4286f5;
        GuiRenderUtils.drawRoundedRect(this.x + (width / 2) - 50, this.y + height - 18, 100, 15, 2, acceptButtonState, 1f, acceptButtonState);

        FontManager.sans16_2.drawCenteredString(this.buttonText, this.x + (width / 2f), this.y + height - 16, ColorUtils.WHITE.c);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        super.mouseClick(mouseX, mouseY, mouseButton);

        if(mouseButton == 0) {
            // Accept Button
            if(RenderUtil.isHovering(mouseX, mouseY, this.x + (width / 2) - 50, this.y + height - 18, this.x + (width / 2) + 50, this.y + height - 3)) {
                this.acceptAction();
            }
        }
    }

    //Remember to do super() on these shit functions to close the dialog window
    public void acceptAction() {
        this.destroy();
    }
}
