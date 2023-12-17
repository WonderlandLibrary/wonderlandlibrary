// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.altmanager;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.text.TextFormatting;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import ru.tuskevich.ui.altmanager.alt.Alt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import ru.tuskevich.ui.altmanager.alt.AltLoginThread;
import net.minecraft.client.gui.GuiScreen;

public final class GuiAltLogin extends GuiScreen
{
    private final GuiScreen previousScreen;
    private PasswordField password;
    private AltLoginThread thread;
    private GuiTextField username;
    
    public GuiAltLogin(final GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        try {
            switch (button.id) {
                case 0: {
                    (this.thread = new AltLoginThread(new Alt(this.username.getText(), this.password.getText()))).start();
                    break;
                }
                case 1: {
                    GuiAltLogin.mc.displayGuiScreen(this.previousScreen);
                    break;
                }
                case 2: {
                    final String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (data.contains(":")) {
                        final String[] credentials = data.split(":");
                        this.username.setText(credentials[0]);
                        this.password.setText(credentials[1]);
                        break;
                    }
                    break;
                }
            }
        }
        catch (Throwable e) {
            throw new RuntimeException();
        }
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
        RenderUtility.drawRectNotWH(0.0, 0.0, this.width, this.height, new Color(22, 22, 22, 255).getRGB());
        this.username.drawTextBox();
        this.password.drawTextBox();
        Fonts.MONTSERRAT16.drawStringWithShadow("Alt Login", this.width / 2.0f - 10.0f, 20.0f, -1);
        Fonts.MONTSERRAT16.drawStringWithShadow((this.thread == null) ? (TextFormatting.GRAY + "Alts...") : this.thread.getStatus(), this.width / 2.0f, 29.0f, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            Fonts.MONTSERRAT16.drawStringWithShadow("Username / E-Mail", (float)(this.width / 2 - 96), 66.0f, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            Fonts.MONTSERRAT16.drawStringWithShadow("Password", (float)(this.width / 2 - 96), 106.0f, -7829368);
        }
        super.drawScreen(x, y, z);
    }
    
    @Override
    public void initGui() {
        final int height1 = this.height / 4 + 24;
        this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, height1 + 72 + 12, "Login"));
        this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, height1 + 72 + 12 + 24, "Back"));
        this.buttonList.add(new GuiAltButton(2, this.width / 2 - 100, height1 + 72 + 12 - 24, "Import User:Pass"));
        this.username = new GuiTextField(height1, GuiAltLogin.mc.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(GuiAltLogin.mc.fontRenderer, this.width / 2 - 100, 100, 200, 20);
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
