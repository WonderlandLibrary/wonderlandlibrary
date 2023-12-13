/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import tk.rektsky.Client;
import tk.rektsky.utils.RektSkyUtil;

public class GuiDisconnected
extends GuiScreen {
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 10 + this.fontRendererObj.FONT_HEIGHT * 2, I18n.format("New name and reconnect", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 20 + this.fontRendererObj.FONT_HEIGHT * 4, I18n.format("Reconnect", new Object[0])));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 1 && Client.lastServer != null) {
            Client.sessionManager.setUserOffline("Reeker" + RektSkyUtil.genRandomString(10));
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, Client.lastServer));
        }
        if (button.id == 2 && Client.lastServer != null) {
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, Client.lastServer));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 0xAAAAAA);
        int i2 = this.height / 2 - this.field_175353_i / 2;
        if (this.multilineMessage != null) {
            for (String s2 : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, s2, this.width / 2, i2, 0xFFFFFF);
                i2 += this.fontRendererObj.FONT_HEIGHT;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

