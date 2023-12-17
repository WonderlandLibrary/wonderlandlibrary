// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.multiplayer.ServerData;

public class GuiScreenServerList extends GuiScreen
{
    private final GuiScreen lastScreen;
    private final ServerData serverData;
    private GuiTextField ipEdit;
    
    public GuiScreenServerList(final GuiScreen lastScreenIn, final ServerData serverDataIn) {
        this.lastScreen = lastScreenIn;
        this.serverData = serverDataIn;
    }
    
    @Override
    public void updateScreen() {
        this.ipEdit.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
        (this.ipEdit = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 116, 200, 20)).setMaxStringLength(128);
        this.ipEdit.setFocused(true);
        this.ipEdit.setText(GuiScreenServerList.mc.gameSettings.lastServer);
        this.buttonList.get(0).enabled = (!this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        GuiScreenServerList.mc.gameSettings.lastServer = this.ipEdit.getText();
        GuiScreenServerList.mc.gameSettings.saveOptions();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.lastScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0) {
                this.serverData.serverIP = this.ipEdit.getText();
                this.lastScreen.confirmClicked(true, 0);
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.ipEdit.textboxKeyTyped(typedChar, keyCode)) {
            this.buttonList.get(0).enabled = (!this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0);
        }
        else if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.ipEdit.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("selectServer.direct", new Object[0]), this.width / 2, 20, 16777215);
        this.drawString(this.fontRenderer, I18n.format("addServer.enterIp", new Object[0]), this.width / 2 - 100, 100, 10526880);
        this.ipEdit.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
