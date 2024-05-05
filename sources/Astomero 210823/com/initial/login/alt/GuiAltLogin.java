package com.initial.login.alt;

import net.minecraft.util.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import java.io.*;

public final class GuiAltLogin extends GuiScreen
{
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;
    
    public GuiAltLogin(final GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                (this.thread = new AltLoginThread(this.username.getText(), this.password.getText())).start();
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int x2, final int y2, final float z2) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(this.mc.fontRendererObj, "Alt Login", GuiAltLogin.width / 2, 20, -1);
        this.drawCenteredString(this.mc.fontRendererObj, (this.thread == null) ? (EnumChatFormatting.GRAY + "Idle...") : this.thread.getStatus(), GuiAltLogin.width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            Gui.drawString(this.mc.fontRendererObj, "Username or email", GuiAltLogin.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            Gui.drawString(this.mc.fontRendererObj, "Password", GuiAltLogin.width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }
    
    @Override
    public void initGui() {
        final int var3 = GuiAltLogin.height / 4 + 24;
        this.buttonList.add(new GuiButton(0, GuiAltLogin.width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, GuiAltLogin.width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, GuiAltLogin.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, GuiAltLogin.width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    protected void keyTyped(final char character, final int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            }
            else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }
    
    @Override
    protected void mouseClicked(final int x2, final int y2, final int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}
