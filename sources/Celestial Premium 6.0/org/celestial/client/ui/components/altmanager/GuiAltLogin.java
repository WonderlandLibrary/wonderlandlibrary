/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.altmanager;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.altmanager.PasswordField;
import org.celestial.client.ui.components.altmanager.alt.Alt;
import org.celestial.client.ui.components.altmanager.alt.AltLoginThread;
import org.lwjgl.input.Keyboard;

public final class GuiAltLogin
extends GuiScreen {
    private final GuiScreen previousScreen;
    private PasswordField password;
    private AltLoginThread thread;
    private GuiTextField username;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        try {
            switch (button.id) {
                case 0: {
                    this.thread = new AltLoginThread(new Alt(this.username.getText(), this.password.getText()));
                    this.thread.start();
                    break;
                }
                case 1: {
                    this.mc.displayGuiScreen(this.previousScreen);
                    break;
                }
                case 2: {
                    String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!data.contains(":")) break;
                    String[] credentials = data.split(":");
                    this.username.setText(credentials[0]);
                    this.password.setText(credentials[1]);
                }
            }
        }
        catch (Throwable e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RectHelper.drawRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(17, 17, 17, 255).getRGB());
        RenderHelper.drawImage(new ResourceLocation("celestial/skeet.png"), 0.0f, 0.0f, sr.getScaledWidth(), 2.0f, ClientHelper.getClientColor());
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.mc.fontRendererObj.drawCenteredString("Alt Login", (float)this.width / 2.0f, 20.0f, -1);
        this.mc.fontRendererObj.drawCenteredString(this.thread == null ? (Object)((Object)TextFormatting.GRAY) + "Alts..." : this.thread.getStatus(), (float)this.width / 2.0f, 29.0f, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            this.drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            this.drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x, y, z);
    }

    @Override
    public void initGui() {
        int height1 = this.height / 4 + 24;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, height1 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, height1 + 72 + 12 + 24, "Back"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, height1 + 72 + 12 - 24, "Import User:Pass"));
        this.username = new GuiTextField(height1, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
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
    protected void mouseClicked(int x, int y, int button) {
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

