/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.wintware.client.ui.altmanager;

import java.awt.Color;
import java.io.IOException;
import java.security.SecureRandom;
import me.wintware.client.ui.altmanager.AltLoginThread;
import me.wintware.client.ui.altmanager.PasswordField;
import me.wintware.client.ui.login.GLSLSandboxShader;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public final class GuiAltLogin
extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;
    private GLSLSandboxShader backgroundShader;
    private final long initTime = System.currentTimeMillis();
    private static String alphabet = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    private static final SecureRandom secureRandom = new SecureRandom();

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    public static String randomString(int strLength) {
        StringBuilder stringBuilder = new StringBuilder(strLength);
        for (int i = 0; i < strLength; ++i) {
            stringBuilder.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
        }
        return stringBuilder.toString();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 2: {
                this.thread = new AltLoginThread("Wint" + GuiAltLogin.randomString(5), "");
                this.thread.start();
                break;
            }
            case 0: {
                this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                this.thread.start();
            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtil.drawGradientSideways(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(52, 52, 52).getRGB(), new Color(52, 52, 52).getRGB());
        int lol = height / 4 + 24;
        RenderUtil.drawSmoothRect(width / 2 - 68, lol + 110, width / 2 + 60, lol - 20, new Color(40, 40, 40, 255).getRGB());
        RenderUtil.drawRectWithEdge(width / 2 - 68, (double)lol - 19.5, 0.5, lol - 31, new Color(0, 105, 153), new Color(0, 105, 173));
        this.username.drawTextBox();
        this.password.drawTextBox();
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("WintWare.wtf", sr.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth("WintWare.wtf") - 2, sr.getScaledHeight() - Minecraft.getMinecraft().arraylist.getStringHeight("WintWare.wtf") - 2, -1);
        Minecraft.getMinecraft().fontRenderer.drawCenteredString("Alt Login", width / 2 + -6, lol, -1);
        Minecraft.getMinecraft().clickguismall.drawCenteredString(this.thread == null ? TextFormatting.GRAY + "" : this.thread.getStatus(), width / 2 - 4, lol + 98, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            Minecraft.getMinecraft().clickguismall.drawStringWithShadow("Email", width / 2 - 52, lol + 24, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            Minecraft.getMinecraft().clickguismall.drawStringWithShadow("Password", width / 2 - 53, lol + 44, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int lol = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 50, lol + 60, 90, 13, "Login"));
        this.buttonList.add(new GuiButton(2, width / 2 - 50, lol + 63 + 12, 90, 13, "Random Name"));
        this.username = new GuiTextField(lol, this.mc.fontRendererObj, width / 2 - 55, lol + 20, 100, 13);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 55, lol + 40, 100, 13);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException iOException) {
            // empty catch block
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
            this.actionPerformed(this.buttonList.get(0));
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

    static {
        alphabet = alphabet + alphabet.toLowerCase();
    }
}

