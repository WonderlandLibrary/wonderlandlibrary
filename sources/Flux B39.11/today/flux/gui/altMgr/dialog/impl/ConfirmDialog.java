package today.flux.gui.altMgr.dialog.impl;

import today.flux.gui.altMgr.dialog.DialogWindow;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.utility.ColorUtils;

import java.awt.*;

public class ConfirmDialog extends DialogWindow {

    public String message;

    public ConfirmDialog(float x, float y, float width, float height, String title, String message) {
        super(x, y, width, height, title);
        this.message = message;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);

        FontManager.sans16_2.drawCenteredString(this.message, this.x + (width / 2), this.y + (height / 2) - 7, ColorUtils.WHITE.c);

        int acceptButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + 3, this.y + height - 18, this.x + 3 + (width / 2f) - 5, this.y + height - 3) ? new Color(0xff4286f5).darker().getRGB() : 0xff4286f5;
        GuiRenderUtils.drawRoundedRect(this.x + 3, this.y + height - 18, width / 2f - 5, 15, 2, acceptButtonState, 1f, acceptButtonState);

        float acceptButtonWidth = (this.x + 3 + (width / 2f) - 5) - (this.x + 3);
        FontManager.sans16_2.drawCenteredString("Confirm", this.x + 3f + (acceptButtonWidth / 2f), this.y + height - 17, ColorUtils.WHITE.c);

        //RenderUtil.drawOutlinedRect(this.x + 3, this.y + height - 18, this.x + 3 + (width / 2f) - 5, this.y + height - 3, 1f, ColorUtils.RED.c);

        int denyButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + (width / 2f) + 7, this.y + height - 18, this.x + width - 3, this.y + height - 3) ? new Color(0xff4286f5).darker().getRGB() : 0xff4286f5;
        GuiRenderUtils.drawBorderedRect(this.x + (width / 2f) + 7, this.y + height - 18, width / 2f - 10, 15, 2, denyButtonState, denyButtonState);

        float denyButtonWidth = (this.x + width - 3) - (this.x + (width / 2f) + 7);
        FontManager.sans16_2.drawCenteredString("Cancel", x + (width / 2f) + 7 + (denyButtonWidth / 2f), this.y + height - 17, ColorUtils.WHITE.c);

        //RenderUtil.drawOutlinedRect(this.x + (width / 2f) + 7, this.y + height - 18, this.x + width - 3, this.y + height - 3, 1f, ColorUtils.RED.c);
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
            if(RenderUtil.isHovering(mouseX, mouseY, this.x + 3, this.y + height - 18, this.x + 3 + (width / 2f) - 5, this.y + height - 3)) {
                this.acceptAction();
            }

            // Deny Button
            if(RenderUtil.isHovering(mouseX, mouseY, this.x + (width / 2f) + 7, this.y + height - 18, this.x + width - 3, this.y + height - 3)) {
                this.denyAction();
            }
        }
    }

    //Remember to do super() on these shit functions to close the dialog window
    public void acceptAction() {
        this.destroy();
    }

    public void denyAction() {
        this.destroy();
    }
}
