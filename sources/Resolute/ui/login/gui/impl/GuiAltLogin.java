// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.login.gui.impl;

import org.lwjgl.input.Keyboard;
import vip.Resolute.util.font.MinecraftFontRenderer;
import net.minecraft.util.EnumChatFormatting;
import vip.Resolute.util.font.FontUtil;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import vip.Resolute.ui.login.gui.thread.AltLoginThread;
import vip.Resolute.ui.login.gui.components.GuiPasswordField;
import net.minecraft.client.gui.GuiScreen;

public final class GuiAltLogin extends GuiScreen
{
    private GuiPasswordField password;
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
            case 2: {
                String data = null;
                try {
                    data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (data.contains(":")) {
                        final String[] credentials = data.split(":");
                        this.username.setText(credentials[0]);
                        this.password.setText(credentials[1]);
                    }
                }
                catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        final MinecraftFontRenderer fr = FontUtil.moon;
        fr.drawCenteredString("Alt Login", (float)(GuiAltLogin.width / 2), 20.0f, -1);
        fr.drawCenteredString((this.thread == null) ? (EnumChatFormatting.GRAY + "Idle...") : this.thread.getStatus(), (float)(GuiAltLogin.width / 2), 29.0f, -1);
        if (this.username.getText().isEmpty()) {
            this.mc.fontRendererObj.drawString("Username / E-Mail", GuiAltLogin.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.mc.fontRendererObj.drawString("Password", GuiAltLogin.width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x, y, z);
    }
    
    @Override
    public void initGui() {
        final int var3 = GuiAltLogin.height / 4 + 24;
        this.buttonList.add(new GuiButton(0, GuiAltLogin.width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, GuiAltLogin.width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        this.buttonList.add(new GuiButton(2, GuiAltLogin.width / 2 - 100, var3 + 72 + 12 + 48, "Import user:pass"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, GuiAltLogin.width / 2 - 100, 60, 200, 20);
        this.password = new GuiPasswordField(this.mc.fontRendererObj, GuiAltLogin.width / 2 - 100, 100, 200, 20);
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
    protected void mouseClicked(final int x, final int y, final int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
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
