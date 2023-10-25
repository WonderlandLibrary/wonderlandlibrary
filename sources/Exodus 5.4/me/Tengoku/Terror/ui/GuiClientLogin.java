/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.Tengoku.Terror.ui;

import java.io.IOException;
import me.Tengoku.Terror.ui.AltLoginThread;
import me.Tengoku.Terror.ui.PasswordField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public final class GuiClientLogin
extends GuiScreen {
    private AltLoginThread thread;
    private GuiTextField username;
    private PasswordField password;
    private final GuiScreen previousScreen;

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    public GuiClientLogin(GuiScreen guiScreen) {
        this.previousScreen = guiScreen;
    }

    @Override
    public void updateScreen() {
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
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(Minecraft.fontRendererObj, "Quick Fix You Can Try: Go on this website and restart your game epicskinz.000webhostapp.com", (int)((double)width / 2.5), 35, -1);
        this.drawCenteredString(Minecraft.fontRendererObj, "The authentification server is currently down. Please try again later.", width / 2, 20, -1);
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case 1: {
                this.mc.shutdown();
            }
        }
    }

    @Override
    public void initGui() {
        int n = height / 4 + 24;
        this.buttonList.add(new GuiButton(1, width / 2 - 100, n + 72 + 12 + 24, "Exit"));
        Keyboard.enableRepeatEvents((boolean)true);
    }
}

