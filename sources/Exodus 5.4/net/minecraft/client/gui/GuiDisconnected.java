/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected
extends GuiScreen {
    private IChatComponent message;
    private int field_175353_i;
    private final GuiScreen parentScreen;
    private String reason;
    private List<String> multilineMessage;

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, width / 2, height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 0xAAAAAA);
        int n3 = height / 2 - this.field_175353_i / 2;
        if (this.multilineMessage != null) {
            for (String string : this.multilineMessage) {
                this.drawCenteredString(this.fontRendererObj, string, width / 2, n3, 0xFFFFFF);
                n3 += this.fontRendererObj.FONT_HEIGHT;
            }
        }
        super.drawScreen(n, n2, f);
    }

    public GuiDisconnected(GuiScreen guiScreen, String string, IChatComponent iChatComponent) {
        this.parentScreen = guiScreen;
        this.reason = I18n.format(string, new Object[0]);
        this.message = iChatComponent;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
}

