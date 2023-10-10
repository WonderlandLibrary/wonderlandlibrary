package today.flux.gui.altMgr.dialog.impl;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import today.flux.gui.altMgr.dialog.DialogTextField;
import today.flux.gui.altMgr.dialog.DialogWindow;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.utility.ColorUtils;

import java.awt.*;

public class InputDialog extends DialogWindow {

    public DialogTextField dtf;
    public String displayMsg;

    public String acceptButtonText;
    public String denyButtonText;

    public InputDialog(float x, float y, float width, float height, String title, String inputBoxText, String displayMsg) {
        this(x, y, width, height, title, inputBoxText, displayMsg, "Confirm", "Cancel");
    }

    public InputDialog(float x, float y, float width, float height, String title, String inputBoxText, String displayMsg, String acceptButtonText, String denyButtonText) {
        super(x, y, width, height, title);

        this.acceptButtonText = acceptButtonText;
        this.denyButtonText = denyButtonText;

        this.displayMsg = displayMsg;
        dtf = new DialogTextField(0, Minecraft.getMinecraft().fontRendererObj, (int) x, (int)y, (int) width - 6, 20);
        dtf.setMaxStringLength(65535);
        dtf.setText(inputBoxText);

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        super.draw(mouseX, mouseY);
        dtf.xPosition = (int) x + 3;
        dtf.yPosition = (int) y + (int) height - 45;
        dtf.drawTextBox();

        FontManager.sans16_2.drawString(displayMsg, this.x + 3f, this.y + 18, ColorUtils.WHITE.c);

        int acceptButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + 3, this.y + height - 18, this.x + 3 + (width / 2f) - 5, this.y + height - 3) ? new Color(0xff4286f5).darker().getRGB() : 0xff4286f5;
        GuiRenderUtils.drawRoundedRect(this.x + 3, this.y + height - 18, width / 2f - 5, 15, 2, acceptButtonState, 1f, acceptButtonState);

        float acceptButtonWidth = (this.x + 3 + (width / 2f) - 5) - (this.x + 3);
        FontManager.sans16_2.drawCenteredString(acceptButtonText, this.x + 3f + (acceptButtonWidth / 2f), this.y + height - 17, ColorUtils.WHITE.c);

        int denyButtonState = RenderUtil.isHovering(mouseX, mouseY, this.x + (width / 2f) + 7, this.y + height - 18, this.x + width - 3, this.y + height - 3) ? new Color(0xff4286f5).darker().getRGB() : 0xff4286f5;
        GuiRenderUtils.drawRoundedRect(this.x + (width / 2f) + 7, this.y + height - 18, width / 2f - 10, 15, 2, denyButtonState, 1f, denyButtonState);

        float denyButtonWidth = (this.x + width - 3) - (this.x + (width / 2f) + 7);
        FontManager.sans16_2.drawCenteredString(denyButtonText, x + (width / 2f) + 7 + (denyButtonWidth / 2f), this.y + height - 17, ColorUtils.WHITE.c);
    }

    @Override
    public void updateScreen() {
        dtf.updateCursorCounter();

        super.updateScreen();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        dtf.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        super.mouseClick(mouseX, mouseY, mouseButton);

        dtf.mouseClicked(mouseX, mouseY, mouseButton);
        if(mouseButton == 0) {
            // Accept Button
            if(RenderUtil.isHovering(mouseX, mouseY, this.x + 3, this.y + height - 18, this.x + 3 + (width / 2f) - 5, this.y + height - 3)) {
                Keyboard.enableRepeatEvents(false);
                this.acceptAction();
            }

            // Deny Button
            if(RenderUtil.isHovering(mouseX, mouseY, this.x + (width / 2f) + 7, this.y + height - 18, this.x + width - 3, this.y + height - 3)) {
                Keyboard.enableRepeatEvents(false);
                this.denyAction();
            }
        }
    }

    //Remember to do super() on these shit functions to close the dialog window (or manually do destroy?)
    public void acceptAction() {
        this.destroy();
    }

    public void denyAction() {
        this.destroy();
    }
}