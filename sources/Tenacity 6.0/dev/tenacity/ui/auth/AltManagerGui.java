package dev.tenacity.ui.auth;

import dev.tenacity.util.misc.CustomFontRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class AltManagerGui extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonSpacing = 5;

        int totalHeight = 4 * buttonHeight + 3 * buttonSpacing;

        int startY = (this.height / 2) - (totalHeight / 2);

        int buttonX = (this.width / 2) - (buttonWidth / 2);

        this.buttonList.add(new GuiButton(2, buttonX, startY, buttonWidth, buttonHeight, "Microsoft"));
        this.buttonList.add(new GuiButton(4, buttonX, startY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight, "Gamepass"));
        this.buttonList.add(new GuiButton(3, buttonX, startY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight, "Cookie"));
        this.buttonList.add(new GuiButton(1, buttonX, startY + 3 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight, "Cracked"));

        int exitButtonY = startY + 4 * (buttonHeight + buttonSpacing);

        this.buttonList.add(new GuiButton(0, buttonX, exitButtonY, buttonWidth, buttonHeight, "Exit"));

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            this.buttonList.clear();
            mc.displayGuiScreen(new GuiLogin());
        }
        if (button.id == 0) {
            CustomFontRenderer customFontRenderer = new CustomFontRenderer();
            mc.displayGuiScreen(new GuiMainMenu());
        }
        if (button.id == 2) {
            mc.displayGuiScreen(new PCIYWB());
        }
        if (button.id == 3) {
            mc.displayGuiScreen(new PCIYWB());
        }
        if (button.id == 4) {
            mc.displayGuiScreen(new PCIYWB());
        }
    }
}