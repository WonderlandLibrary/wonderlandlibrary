package wtf.monsoon.impl.ui.alt;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.util.entity.SessionChanger;
import wtf.monsoon.api.util.entity.UsernameUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;


public final class GuiAltLogin
extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
    	String usernameS;
    	String passwordS;
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 57840: {
                Desktop d = Desktop.getDesktop();
                try {
                    d.browse(new URI("shoppy.gg/@drilledalts"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case 0: {
            	if (username.getText().contains(":")) {
                    String[] combo = username.getText().split(":");
                    usernameS = combo[0];
                    passwordS = combo[1];
                } else {
                    usernameS = username.getText();
                    passwordS = password.getText();
                }
                this.thread = new AltLoginThread(usernameS, passwordS);
                this.thread.start();
            }
            case 201: {
                SessionChanger.getInstance().setUserOffline(UsernameUtil.getNewName());
                //this.thread = new AltLoginThread(UsernameUtil.getNewName(), "");
                System.out.println(UsernameUtil.getNewName());
            }
            case 203: {

            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(this.mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
        this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? (Object)((Object)EnumChatFormatting.GRAY) + "Idle..." : this.thread.getStatus(), width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Username / E-Mail / Name / Combo", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(201, width / 2 - 100, var3 + 72 + 12 + 48, "MonsoonGen (generate cracked)"));
        this.buttonList.add(new GuiButton(57840, width / 2 - 100, var3 + 72 + 12 + 72, "Buy Alts"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 96, "Back"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
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

