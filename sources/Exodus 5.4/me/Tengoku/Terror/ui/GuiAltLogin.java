/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  fr.litarvan.openauth.microsoft.MicrosoftAuthResult
 *  fr.litarvan.openauth.microsoft.MicrosoftAuthenticator
 *  org.lwjgl.input.Keyboard
 */
package me.Tengoku.Terror.ui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import java.io.IOException;
import java.net.Proxy;
import me.Tengoku.Terror.ui.AltLoginThread;
import me.Tengoku.Terror.ui.PasswordField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

public final class GuiAltLogin
extends GuiScreen {
    private AltLoginThread thread;
    private PasswordField password;
    private final GuiScreen previousScreen;
    private GuiTextField username;

    @Override
    protected void keyTyped(char c, int n) {
        try {
            super.keyTyped(c, n);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        if (c == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (c == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(c, n);
        this.password.textboxKeyTyped(c, n);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.drawCenteredString(Minecraft.fontRendererObj, "Alt Login", width / 2, 20, -1);
        this.drawCenteredString(Minecraft.fontRendererObj, this.thread == null ? (Object)((Object)EnumChatFormatting.GRAY) + "Idle..." : this.thread.getStatus(), width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(Minecraft.fontRendererObj, "Email:Password", width / 2 - 96, 66, -7829368);
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case 69: {
                MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
                if (GuiAltLogin.getClipboardString() != null && GuiAltLogin.getClipboardString().split(":").length > 1) {
                    MicrosoftAuthResult microsoftAuthResult = microsoftAuthenticator.loginWithCredentials(GuiAltLogin.getClipboardString().split(":")[0], GuiAltLogin.getClipboardString().split(":")[1]);
                    System.out.printf("Logged in with '%s'%n", microsoftAuthResult.getProfile().getName());
                    try {
                        this.mc.session = new Session(microsoftAuthResult.getProfile().getName(), microsoftAuthResult.getProfile().getId(), microsoftAuthResult.getAccessToken(), "legacy");
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                if (GuiAltLogin.getClipboardString() == null || GuiAltLogin.getClipboardString().split(":").length <= 1) break;
                this.thread = new AltLoginThread(GuiAltLogin.getClipboardString().split(":")[0], GuiAltLogin.getClipboardString().split(":")[1]);
                this.thread.start();
            }
        }
    }

    private Session createSession(String string, String string2) {
        YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication yggdrasilUserAuthentication = (YggdrasilUserAuthentication)yggdrasilAuthenticationService.createUserAuthentication(Agent.MINECRAFT);
        yggdrasilUserAuthentication.setUsername(string);
        yggdrasilUserAuthentication.setPassword(string2);
        yggdrasilUserAuthentication.logIn();
        try {
            return new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException authenticationException) {
            authenticationException.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }

    public GuiAltLogin(GuiScreen guiScreen) {
        this.previousScreen = guiScreen;
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) {
        try {
            super.mouseClicked(n, n2, n3);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        this.username.mouseClicked(n, n2, n3);
        this.password.mouseClicked(n, n2, n3);
    }

    @Override
    public void initGui() {
        int n = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, n + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(69, width / 2 - 100, n + 72 + 90, "Microsoft Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, n + 72 + 12 + 24, "Back"));
        this.username = new GuiTextField(n, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(Minecraft.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents((boolean)true);
    }
}

