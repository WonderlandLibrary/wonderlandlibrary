// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui;

import java.awt.Color;
import vip.Resolute.util.font.FontUtil;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import vip.Resolute.com.viamcp.gui.GuiProtocolSelector;
import vip.Resolute.ui.login.gui.GuiAltManager;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptions;
import vip.Resolute.auth.LoginScreen;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.resources.I18n;
import vip.Resolute.Resolute;
import java.io.IOException;
import vip.Resolute.util.render.TranslationUtils;
import vip.Resolute.ui.shader.GLSLSandboxShader;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class MainMenu extends GuiScreen
{
    private GuiButton buttonResetDemo;
    private DynamicTexture viewportTexture;
    private GLSLSandboxShader backgroundShader;
    private long initTime;
    public TranslationUtils translate;
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    public MainMenu() {
        this.initTime = System.currentTimeMillis();
        try {
            this.backgroundShader = new GLSLSandboxShader("/mainMenuShader.frag");
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to load backgound shader", e);
        }
    }
    
    @Override
    public void initGui() {
        Resolute.getInstance().getDiscordRP().update("Idle", "");
        this.translate = new TranslationUtils(0.0f, 0.0f);
        this.viewportTexture = new DynamicTexture(256, 256);
        final int i = 24;
        final int j = MainMenu.height / 4 + 48;
        this.addSingleplayerMultiplayerButtons(j, 24);
        this.buttonList.add(new GuiButton(0, MainMenu.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, MainMenu.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButtonLanguage(5, MainMenu.width / 2 - 124, j + 72 + 12));
        this.buttonList.add(new GuiButton(69, 5, 5, 90, 20, "Protocol"));
        this.mc.func_181537_a(false);
        if (!Resolute.authorized) {
            this.mc.displayGuiScreen(new LoginScreen());
        }
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, MainMenu.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, MainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(new GuiButton(100, MainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "Alt Manager"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        else if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        else if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        else if (button.id == 14) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        else if (button.id == 100) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }
        else if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 69) {
            this.mc.displayGuiScreen(new GuiProtocolSelector(this));
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        final int i = 274;
        final int j = MainMenu.width / 2 - i / 2;
        final int k = 30;
        this.backgroundShader.useShader(MainMenu.width * 2, MainMenu.height * 2, (float)mouseX, (float)mouseY, (System.currentTimeMillis() - this.initTime) / 1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        final float textX = (float)(MainMenu.width / 2 - 60);
        final int textHeight = MainMenu.height / 4;
        final String title = "Resolute";
        final long currentMillis = System.currentTimeMillis();
        float posX = textX;
        final long ms = 4000L;
        this.translate.interpolate((float)MainMenu.width, (float)MainMenu.height, 5.0);
        final double xmod = MainMenu.height - this.translate.getY();
        GL11.glPushMatrix();
        GlStateManager.translate(0.0, 0.5 * xmod, 0.0);
        for (int index = 0; index < title.length(); ++index) {
            final String ch = String.valueOf(title.charAt(index));
            final float offset = (currentMillis + index * 100) % ms / (ms / 2.0f);
            FontUtil.oxide.drawStringWithShadow(ch, posX, (float)textHeight, this.fadeBetween(new Color(190, 0, 0).getRGB(), this.darker(new Color(237, 0, 255).getRGB(), 0.49f), offset));
            posX += (float)FontUtil.oxide.getStringWidth(ch);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPopMatrix();
    }
    
    private int darker(final int color, final float factor) {
        final int r = (int)((color >> 16 & 0xFF) * factor);
        final int g = (int)((color >> 8 & 0xFF) * factor);
        final int b = (int)((color & 0xFF) * factor);
        final int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) | (a & 0xFF) << 24;
    }
    
    private int fadeBetween(final int color1, final int color2, float offset) {
        if (offset > 1.0f) {
            offset = 1.0f - offset % 1.0f;
        }
        final double invert = 1.0f - offset;
        final int r = (int)((color1 >> 16 & 0xFF) * invert + (color2 >> 16 & 0xFF) * offset);
        final int g = (int)((color1 >> 8 & 0xFF) * invert + (color2 >> 8 & 0xFF) * offset);
        final int b = (int)((color1 & 0xFF) * invert + (color2 & 0xFF) * offset);
        final int a = (int)((color1 >> 24 & 0xFF) * invert + (color2 >> 24 & 0xFF) * offset);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
}
