/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package com.wallhacks.losebypass.gui;

import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.TextEditComponent;
import com.wallhacks.losebypass.manager.FontManager;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.font.GameFontRenderer;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GuiLogin {
    boolean done = false;
    boolean down = false;
    Minecraft mc;
    ResourceLocation logo = new ResourceLocation("textures/icons/logosimple.png");
    GameFontRenderer font = new GameFontRenderer(FontManager.getClientFont("Thick.ttf", 20.0f), true, false);
    GameFontRenderer logoFont = new GameFontRenderer(FontManager.getClientFont("Thick.ttf", 44.0f), true, false);
    private long systemTime = Minecraft.getSystemTime();
    private boolean field_73724_e;
    private ScaledResolution scaledResolution;
    TextEditComponent email = new TextEditComponent("", this.font, new Runnable(){

        @Override
        public void run() {
            GuiLogin.this.password.setTyping(true);
        }
    }, false);
    private Framebuffer framebuffer;
    TextEditComponent password = new TextEditComponent("", this.font, new Runnable(){

        @Override
        public void run() {
            GuiLogin.this.login(GuiLogin.this.email.getText(), GuiLogin.this.password.getText());
        }
    }, false);

    public GuiLogin(Minecraft mc) {
        Display.setResizable((boolean)false);
        this.mc = mc;
        this.scaledResolution = new ScaledResolution(mc);
        this.framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
        this.framebuffer.setFramebufferFilter(9728);
        this.password.setPassword(true);
        this.loop();
        Display.setResizable((boolean)true);
    }

    private void loop() {
        while (!this.done) {
            if (Display.isCloseRequested()) {
                System.exit(0);
                continue;
            }
            long i = Minecraft.getSystemTime();
            if (i - this.systemTime < 10L) continue;
            this.systemTime = i;
            this.scaledResolution = new ScaledResolution(this.mc);
            int j = this.scaledResolution.getScaleFactor();
            int k = this.scaledResolution.getScaledWidth();
            int l = this.scaledResolution.getScaledHeight();
            if (OpenGlHelper.isFramebufferEnabled()) {
                this.framebuffer.framebufferClear();
            } else {
                GlStateManager.clear(256);
            }
            this.framebuffer.bindFramebuffer(false);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, this.scaledResolution.getScaledWidth_double(), this.scaledResolution.getScaledHeight_double(), 0.0, 100.0, 300.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -200.0f);
            if (!OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.clear(16640);
            }
            int i1 = this.scaledResolution.getScaledWidth();
            int j1 = this.scaledResolution.getScaledHeight();
            int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
            int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
            this.drawLoginScreen(k1, l1);
            this.handleKeyboard();
            this.framebuffer.unbindFramebuffer();
            if (OpenGlHelper.isFramebufferEnabled()) {
                this.framebuffer.framebufferRender(k * j, l * j);
            }
            this.mc.updateDisplay();
            try {
                Thread.yield();
            }
            catch (Exception exception) {}
        }
    }

    private void handleKeyboard() {
        while (Keyboard.next()) {
            if (!Keyboard.getEventKeyState()) continue;
            int k = Keyboard.getEventKey();
            char c = Keyboard.getEventCharacter();
            this.password.keyTyped(c, k);
            this.email.keyTyped(c, k);
        }
    }

    private void drawLoginScreen(int mouseX, int mouseY) {
        GuiUtil.drawRect(0.0f, 0.0f, this.scaledResolution.getScaledWidth(), this.scaledResolution.getScaledHeight(), ClickGui.background2());
        int centerY = this.scaledResolution.getScaledHeight() / 2;
        int centerX = this.scaledResolution.getScaledWidth() / 2;
        GuiUtil.rounded(centerX - 100, (centerY += 30) - 34, centerX + 100, centerY - 18, ClickGui.background3(), 5);
        GuiUtil.rounded(centerX - 100, centerY - 8, centerX + 100, centerY + 8, ClickGui.background3(), 5);
        boolean hover = false;
        if (mouseX > centerX - 100 && mouseX < centerX + 100 && mouseY > centerY + 18 && mouseY < centerY + 34) {
            hover = true;
        }
        GuiUtil.rounded(centerX - 100, centerY + 18, centerX + 100, centerY + 34, !hover ? new Color(44, 92, 118, 255).getRGB() : new Color(44, 92, 118, 255).darker().getRGB(), 5);
        this.font.drawString("Login", (float)centerX - (float)this.font.getStringWidth("Login") / 2.0f, centerY + 22, -1);
        this.email.updatePosition(centerX - 100, centerY - 34, centerX + 100, centerY - 18);
        this.password.updatePosition(centerX - 100, centerY - 8, centerX + 100, centerY + 8);
        GuiUtil.drawCompleteImage(centerX - 85, centerY - 82, 50.0, 50.0, this.logo, new Color(44, 92, 118, 255));
        this.logoFont.drawString("LoseBypass", centerX - 34, centerY - 70, -1);
        if (Mouse.isButtonDown((int)0)) {
            if (!this.down) {
                this.down = true;
                if (!hover) {
                    this.email.mouseClicked(mouseX, mouseY);
                    this.password.mouseClicked(mouseX, mouseY);
                } else {
                    this.email.setTyping(false);
                    this.password.setTyping(false);
                    this.login(this.email.getText(), this.password.getText());
                }
            }
        } else {
            this.down = false;
        }
        if (Objects.equals(this.email.getText(), "") && !this.email.isTyping()) {
            this.font.drawString("email/password", centerX - 95, centerY - 31, new Color(0x808080).getRGB());
        } else {
            this.email.drawString();
        }
        if (Objects.equals(this.password.getText(), "") && !this.password.isTyping()) {
            this.font.drawString("password", centerX - 95, centerY - 5, new Color(0x808080).getRGB());
            return;
        }
        this.password.drawString();
    }

    private void login(String email, String password) {
        this.done = true;
    }
}

