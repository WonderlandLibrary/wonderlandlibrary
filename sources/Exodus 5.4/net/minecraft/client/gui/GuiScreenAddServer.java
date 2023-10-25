/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import java.io.IOException;
import java.net.IDN;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer
extends GuiScreen {
    private Predicate<String> field_181032_r = new Predicate<String>(){

        public boolean apply(String string) {
            if (string.length() == 0) {
                return true;
            }
            String[] stringArray = string.split(":");
            if (stringArray.length == 0) {
                return true;
            }
            try {
                String string2 = IDN.toASCII(stringArray[0]);
                return true;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                return false;
            }
        }
    };
    private GuiButton serverResourcePacks;
    private final GuiScreen parentScreen;
    private GuiTextField serverNameField;
    private final ServerData serverData;
    private GuiTextField serverIPField;

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        this.serverIPField.mouseClicked(n, n2, n3);
        this.serverNameField.mouseClicked(n, n2, n3);
    }

    public GuiScreenAddServer(GuiScreen guiScreen, ServerData serverData) {
        this.parentScreen = guiScreen;
        this.serverData = serverData;
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        this.serverNameField.textboxKeyTyped(c, n);
        this.serverIPField.textboxKeyTyped(c, n);
        if (n == 15) {
            this.serverNameField.setFocused(!this.serverNameField.isFocused());
            this.serverIPField.setFocused(!this.serverIPField.isFocused());
        }
        if (n == 28 || n == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        ((GuiButton)this.buttonList.get((int)0)).enabled = this.serverIPField.getText().length() > 0 && this.serverIPField.getText().split(":").length > 0 && this.serverNameField.getText().length() > 0;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("addServer.title", new Object[0]), width / 2, 17, 0xFFFFFF);
        this.drawString(this.fontRendererObj, I18n.format("addServer.enterName", new Object[0]), width / 2 - 100, 53, 0xA0A0A0);
        this.drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), width / 2 - 100, 94, 0xA0A0A0);
        this.serverNameField.drawTextBox();
        this.serverIPField.drawTextBox();
        super.drawScreen(n, n2, f);
    }

    @Override
    public void updateScreen() {
        this.serverNameField.updateCursorCounter();
        this.serverIPField.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 2) {
                this.serverData.setResourceMode(ServerData.ServerResourceMode.values()[(this.serverData.getResourceMode().ordinal() + 1) % ServerData.ServerResourceMode.values().length]);
                this.serverResourcePacks.displayString = String.valueOf(I18n.format("addServer.resourcePack", new Object[0])) + ": " + this.serverData.getResourceMode().getMotd().getFormattedText();
            } else if (guiButton.id == 1) {
                this.parentScreen.confirmClicked(false, 0);
            } else if (guiButton.id == 0) {
                this.serverData.serverName = this.serverNameField.getText();
                this.serverData.serverIP = this.serverIPField.getText();
                this.parentScreen.confirmClicked(true, 0);
            }
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 18, I18n.format("addServer.add", new Object[0])));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 18, I18n.format("gui.cancel", new Object[0])));
        this.serverResourcePacks = new GuiButton(2, width / 2 - 100, height / 4 + 72, String.valueOf(I18n.format("addServer.resourcePack", new Object[0])) + ": " + this.serverData.getResourceMode().getMotd().getFormattedText());
        this.buttonList.add(this.serverResourcePacks);
        this.serverNameField = new GuiTextField(0, this.fontRendererObj, width / 2 - 100, 66, 200, 20);
        this.serverNameField.setFocused(true);
        this.serverNameField.setText(this.serverData.serverName);
        this.serverIPField = new GuiTextField(1, this.fontRendererObj, width / 2 - 100, 106, 200, 20);
        this.serverIPField.setMaxStringLength(128);
        this.serverIPField.setText(this.serverData.serverIP);
        this.serverIPField.func_175205_a(this.field_181032_r);
        ((GuiButton)this.buttonList.get((int)0)).enabled = this.serverIPField.getText().length() > 0 && this.serverIPField.getText().split(":").length > 0 && this.serverNameField.getText().length() > 0;
    }
}

