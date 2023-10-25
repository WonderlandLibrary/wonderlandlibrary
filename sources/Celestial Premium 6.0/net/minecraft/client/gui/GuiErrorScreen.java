/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiErrorScreen
extends GuiScreen {
    private final String title;
    private final String message;

    public GuiErrorScreen(String titleIn, String messageIn) {
        this.title = titleIn;
        this.message = messageIn;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 140, I18n.format("gui.cancel", new Object[0])));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0.0f, 0.0f, this.width, this.height, -12574688, -11530224);
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 90, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, this.message, this.width / 2, 110, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.mc.displayGuiScreen(null);
    }
}

