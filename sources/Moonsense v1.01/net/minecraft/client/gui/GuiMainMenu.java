// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import se.michaelthelin.spotify.model_objects.specification.Image;
import moonsense.utils.CustomFontRenderer;
import java.awt.Color;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import java.net.URI;
import net.minecraft.realms.RealmsBridge;
import moonsense.integrations.spotify.SpotifyManager;
import moonsense.ui.screen.GuiAccountManager;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.ui.elements.button.GuiButtonIcon;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GLContext;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f;
    private static final Logger logger;
    private static final Random field_175374_h;
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    public int panoramaTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v;
    private final Object field_104025_t;
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;
    private static final ResourceLocation splashTexts;
    private static final ResourceLocation minecraftTitleTextures;
    private static final ResourceLocation[] titlePanoramaPaths;
    public static final String field_96138_a;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;
    private GuiButton field_175372_K;
    private static final String __OBFID = "CL_00001154";
    private int accountSlideProgress;
    private int spotifySlideProgress;
    
    static {
        field_175373_f = new AtomicInteger(0);
        logger = LogManager.getLogger();
        field_175374_h = new Random();
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[] { new ResourceLocation("streamlined/background/panorama_0.png"), new ResourceLocation("streamlined/background/panorama_1.png"), new ResourceLocation("streamlined/background/panorama_2.png"), new ResourceLocation("streamlined/background/panorama_3.png"), new ResourceLocation("streamlined/background/panorama_4.png"), new ResourceLocation("streamlined/background/panorama_5.png") };
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
    
    public GuiMainMenu() {
        this.field_175375_v = true;
        this.field_104025_t = new Object();
        this.field_146972_A = GuiMainMenu.field_96138_a;
        this.splashText = "missingno";
        BufferedReader var1 = null;
        try {
            final ArrayList var2 = Lists.newArrayList();
            var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(GuiMainMenu.splashTexts).getInputStream(), Charsets.UTF_8));
            String var3;
            while ((var3 = var1.readLine()) != null) {
                var3 = var3.trim();
                if (!var3.isEmpty()) {
                    var2.add(var3);
                }
            }
            if (!var2.isEmpty()) {
                do {
                    this.splashText = var2.get(GuiMainMenu.field_175374_h.nextInt(var2.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (IOException ex) {}
        finally {
            if (var1 != null) {
                try {
                    var1.close();
                }
                catch (IOException ex2) {}
            }
        }
        if (var1 != null) {
            try {
                var1.close();
            }
            catch (IOException ex3) {}
        }
        this.updateCounter = GuiMainMenu.field_175374_h.nextFloat();
        this.field_92025_p = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
            this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
            this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }
    
    @Override
    public void updateScreen() {
        ++this.panoramaTimer;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        final Calendar var1 = Calendar.getInstance();
        var1.setTime(new Date());
        if (var1.get(2) + 1 == 11 && var1.get(5) == 9) {
            this.splashText = "Happy birthday, ez!";
        }
        else if (var1.get(2) + 1 == 6 && var1.get(5) == 1) {
            this.splashText = "Happy birthday, Notch!";
        }
        else if (var1.get(2) + 1 == 12 && var1.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        }
        else if (var1.get(2) + 1 == 1 && var1.get(5) == 1) {
            this.splashText = "Happy new year!";
        }
        else if (var1.get(2) + 1 == 10 && var1.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        final boolean var2 = true;
        final int var3 = this.height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(var3, 24);
        }
        else {
            this.addSingleplayerMultiplayerButtons(var3, 24);
        }
        this.buttonList.add(new GuiButtonIcon(20, 5, 5, 20, 20, "account.png", "", true));
        this.buttonList.add(new GuiButtonIcon(24, 30, 5, 20, 20, "spotify.png", "", true));
        this.buttonList.add(new GuiButtonIcon(4, this.width - 25, 5, 20, 20, "close.png", "", true));
        this.buttonList.add(new GuiButtonIcon(21, this.width / 2 - 25 - 25 - 10, this.height - 40, 20, 20, "cosmetics.png", "Cosmetics", true));
        this.buttonList.add(new GuiButtonIcon(23, this.width / 2 - 25 - 10, this.height - 40, 20, 20, "skinchanger.png", "Skin Changer", true));
        this.buttonList.add(new GuiButtonIcon(0, this.width / 2 - 10, this.height - 40, 20, 20, "settings.png", "Settings", true));
        this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 + 25 - 10, this.height - 40, true));
        this.buttonList.add(new GuiButtonIcon(14, this.width / 2 + 25 + 25 - 10, this.height - 40, 20, 20, "realms.png", "Realms", true));
        final Object var4 = this.field_104025_t;
        synchronized (this.field_104025_t) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
            final int var5 = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - var5) / 2;
            this.field_92021_u = this.buttonList.get(0).yPosition - 24;
            this.field_92020_v = this.field_92022_t + var5;
            this.field_92019_w = this.field_92021_u + 24;
        }
        // monitorexit(this.field_104025_t)
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new GuiCustomButton(1, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, 200, 20, I18n.format("menu.singleplayer", new Object[0]), true));
        this.buttonList.add(new GuiCustomButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 200, 20, I18n.format("menu.multiplayer", new Object[0]), true));
    }
    
    private void addDemoButtons(final int p_73972_1_, final int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
        final ISaveFormat var3 = this.mc.getSaveLoader();
        final WorldInfo var4 = var3.getWorldInfo("Demo_World");
        if (var4 == null) {
            this.buttonResetDemo.enabled = false;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 14) {
            this.switchToRealms();
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12) {
            final ISaveFormat var2 = this.mc.getSaveLoader();
            final WorldInfo var3 = var2.getWorldInfo("Demo_World");
            if (var3 != null) {
                final GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
                this.mc.displayGuiScreen(var4);
            }
        }
        if (button.id == 20) {
            final GuiAccountManager g = new GuiAccountManager(this);
            g.panoramaTimer = this.panoramaTimer;
            this.mc.displayGuiScreen(g);
        }
        final int id = button.id;
        final int id2 = button.id;
        if (button.id == 24) {
            SpotifyManager.connectSpotify();
        }
    }
    
    private void switchToRealms() {
        final RealmsBridge var1 = new RealmsBridge();
        var1.switchToRealms(this);
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result && id == 12) {
            final ISaveFormat var6 = this.mc.getSaveLoader();
            var6.flushCache();
            var6.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13) {
            if (result) {
                try {
                    final Class var7 = Class.forName("java.awt.Desktop");
                    final Object var8 = var7.getMethod("getDesktop", (Class[])new Class[0]).invoke(null, new Object[0]);
                    var7.getMethod("browse", URI.class).invoke(var8, new URI(this.field_104024_v));
                }
                catch (Throwable var9) {
                    GuiMainMenu.logger.error("Couldn't open link", var9);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final byte var6 = 8;
        for (int var7 = 0; var7 < var6 * var6; ++var7) {
            GlStateManager.pushMatrix();
            final float var8 = (var7 % var6 / (float)var6 - 0.5f) / 64.0f;
            final float var9 = (var7 / var6 / (float)var6 - 0.5f) / 64.0f;
            final float var10 = 0.0f;
            GlStateManager.translate(var8, var9, var10);
            GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int var11 = 0; var11 < 6; ++var11) {
                GlStateManager.pushMatrix();
                if (var11 == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (var11 == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                this.mc.getTextureManager().bindTexture(GuiMainMenu.titlePanoramaPaths[var11]);
                var5.startDrawingQuads();
                var5.func_178974_a(16777215, 255 / (var7 + 1));
                final float var12 = 0.0f;
                var5.addVertexWithUV(-1.0, -1.0, 1.0, 0.0f + var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, -1.0, 1.0, 1.0f - var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, 1.0, 1.0, 1.0f - var12, 1.0f - var12);
                var5.addVertexWithUV(-1.0, 1.0, 1.0, 0.0f + var12, 1.0f - var12);
                var4.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        var5.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    
    private void rotateAndBlurSkybox(final float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.field_110351_G);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        GlStateManager.disableAlpha();
        final byte var4 = 3;
        for (int var5 = 0; var5 < var4; ++var5) {
            var3.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f / (var5 + 1));
            final int var6 = this.width;
            final int var7 = this.height;
            final float var8 = (var5 - var4 / 2) / 256.0f;
            var3.addVertexWithUV(var6, var7, this.zLevel, 0.0f + var8, 1.0);
            var3.addVertexWithUV(var6, 0.0, this.zLevel, 1.0f + var8, 1.0);
            var3.addVertexWithUV(0.0, 0.0, this.zLevel, 1.0f + var8, 0.0);
            var3.addVertexWithUV(0.0, var7, this.zLevel, 0.0f + var8, 0.0);
        }
        var2.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    
    private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        var5.startDrawingQuads();
        final float var6 = (this.width > this.height) ? (120.0f / this.width) : (120.0f / this.height);
        final float var7 = this.height * var6 / 256.0f;
        final float var8 = this.width * var6 / 256.0f;
        var5.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f);
        final int var9 = this.width;
        final int var10 = this.height;
        var5.addVertexWithUV(0.0, var10, this.zLevel, 0.5f - var7, 0.5f + var8);
        var5.addVertexWithUV(var9, var10, this.zLevel, 0.5f - var7, 0.5f - var8);
        var5.addVertexWithUV(var9, 0.0, this.zLevel, 0.5f + var7, 0.5f - var8);
        var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.5f + var7, 0.5f + var8);
        var4.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        final short var6 = 274;
        final int var7 = this.width / 2 - var6 / 2;
        final byte var8 = 30;
        this.mc.getTextureManager().bindTexture(MoonsenseClient.CLIENT_LOGO);
        final int spPosY = this.buttonList.get(0).yPosition;
        GuiUtils.setGlColor(-1);
        Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 35, spPosY - 94, 0.0f, 0.0f, 70, 70, 70.0f, 70.0f);
        MoonsenseClient.titleRenderer2Large.drawCenteredString("MOONSENSE CLIENT", (float)(this.width / 2), (float)(spPosY - 26), -1);
        GlStateManager.popMatrix();
        MoonsenseClient.textRenderer.drawString("Moonsense Client 1.8.9 (" + MoonsenseClient.getVersion() + ")", this.width - MoonsenseClient.textRenderer.getWidth("Streamlined Client 1.8.9 (" + MoonsenseClient.getVersion() + ")") - 5.0f, (float)(this.height - 24), -1);
        MoonsenseClient.textRenderer.drawString("Copyright Mojang Studios. Do not distribute!", this.width - MoonsenseClient.textRenderer.getWidth("Copyright Mojang Studios. Do not distribute!") - 5.0f, (float)(this.height - 14), -1);
        final GuiButtonIcon accountButton = this.buttonList.get(2);
        if (accountButton.hovered) {
            this.buttonList.get(3).visible = false;
            if (this.accountSlideProgress < 25) {
                this.accountSlideProgress += 5;
            }
            if (this.mc.session.getToken() == null || (this.mc.session.getToken() != null && this.mc.session.getToken().equalsIgnoreCase("0"))) {
                GuiUtils.drawRoundedOutline(accountButton.xPosition - 25 + this.accountSlideProgress, accountButton.yPosition, accountButton.xPosition + 100 + this.accountSlideProgress, accountButton.yPosition + 20, 3.0f, 2.0f, new Color(105, 116, 122, 100).getRGB());
                GuiUtils.drawRoundedRect((float)(accountButton.xPosition - 25 + this.accountSlideProgress), (float)accountButton.yPosition, (float)(accountButton.xPosition + 100 + this.accountSlideProgress), (float)(accountButton.yPosition + 20), 3.0f, new Color(110, 50, 55, 100).getRGB());
                MoonsenseClient.titleRenderer.drawString("Not logged in!", accountButton.xPosition + this.accountSlideProgress + 20, accountButton.yPosition + 5, Color.white.darker().getRGB());
            }
            else {
                GuiUtils.drawRoundedOutline(accountButton.xPosition - 25 + this.accountSlideProgress, accountButton.yPosition, accountButton.xPosition + 100 + this.accountSlideProgress, accountButton.yPosition + 20, 3.0f, 2.0f, new Color(105, 116, 122, 100).getRGB());
                GuiUtils.drawRoundedRect((float)(accountButton.xPosition - 25 + this.accountSlideProgress), (float)accountButton.yPosition, (float)(accountButton.xPosition + 100 + this.accountSlideProgress), (float)(accountButton.yPosition + 20), 3.0f, new Color(42, 50, 55, 100).getRGB());
                GuiUtils.drawHead(this.mc.session.getUsername(), accountButton.xPosition + this.accountSlideProgress - 3, accountButton.yPosition + 2, 16);
                MoonsenseClient.titleRenderer.drawString("Logged in as:", accountButton.xPosition + this.accountSlideProgress + 15, accountButton.yPosition + 2, Color.white.darker().getRGB());
                final CustomFontRenderer textRenderer = MoonsenseClient.textRenderer;
                final String username = this.mc.session.getUsername();
                final int x = accountButton.xPosition + this.accountSlideProgress + 15;
                final int yPosition = accountButton.yPosition;
                MoonsenseClient.textRenderer.getClass();
                textRenderer.drawString(username, x, yPosition + 9, Color.white.darker().getRGB());
            }
        }
        else {
            this.accountSlideProgress = 0;
            this.buttonList.get(3).visible = true;
        }
        final GuiButtonIcon spotifyButton = this.buttonList.get(3);
        if (spotifyButton.hovered) {
            if (this.spotifySlideProgress < 25) {
                this.spotifySlideProgress += 5;
            }
            if (SpotifyManager.getSpotifyAPI().getRefreshToken() == null || !MoonsenseClient.INSTANCE.isSpotifyLoggedIn()) {
                GuiUtils.drawRoundedOutline(spotifyButton.xPosition - 25 + this.spotifySlideProgress, spotifyButton.yPosition, spotifyButton.xPosition + 100 + this.spotifySlideProgress, spotifyButton.yPosition + 20, 3.0f, 2.0f, new Color(105, 116, 122, 100).getRGB());
                GuiUtils.drawRoundedRect((float)(spotifyButton.xPosition - 25 + this.spotifySlideProgress), (float)spotifyButton.yPosition, (float)(spotifyButton.xPosition + 100 + this.spotifySlideProgress), (float)(spotifyButton.yPosition + 20), 3.0f, new Color(110, 50, 55, 100).getRGB());
                MoonsenseClient.titleRenderer.drawString("Spotify Not Connected!", spotifyButton.xPosition + this.spotifySlideProgress, spotifyButton.yPosition + 5, Color.white.darker().getRGB());
            }
            else {
                GuiUtils.drawRoundedOutline(spotifyButton.xPosition - 25 + this.spotifySlideProgress, spotifyButton.yPosition, spotifyButton.xPosition + 100 + this.spotifySlideProgress, spotifyButton.yPosition + 20, 3.0f, 2.0f, new Color(105, 116, 122, 100).getRGB());
                GuiUtils.drawRoundedRect((float)(spotifyButton.xPosition - 25 + this.spotifySlideProgress), (float)spotifyButton.yPosition, (float)(spotifyButton.xPosition + 100 + this.spotifySlideProgress), (float)(spotifyButton.yPosition + 20), 3.0f, new Color(42, 50, 55, 100).getRGB());
                final Image[] imgArr = SpotifyManager.currentUser.getImages();
                if (imgArr != null && imgArr.length > 0) {
                    final Image img = imgArr[0];
                    if (img != null) {
                        this.mc.getTextureManager().bindTexture(SpotifyManager.getUserImageLocation(img.getUrl()));
                        GuiUtils.setGlColor(-1);
                        Gui.drawModalRectWithCustomSizedTexture(spotifyButton.xPosition + this.spotifySlideProgress - 3, spotifyButton.yPosition + 2, 0.0f, 0.0f, 16, 16, 16.0f, 16.0f);
                    }
                }
                MoonsenseClient.titleRenderer.drawString("Spotify Account:", spotifyButton.xPosition + this.spotifySlideProgress + 16, spotifyButton.yPosition + 2, Color.white.darker().getRGB());
                final CustomFontRenderer textRenderer2 = MoonsenseClient.textRenderer;
                final String displayName = SpotifyManager.currentUser.getDisplayName();
                final int x2 = spotifyButton.xPosition + this.spotifySlideProgress + 16;
                final int yPosition2 = spotifyButton.yPosition;
                MoonsenseClient.textRenderer.getClass();
                textRenderer2.drawString(displayName, x2, yPosition2 + 9, Color.white.darker().getRGB());
            }
        }
        else {
            this.spotifySlideProgress = 0;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final Object var4 = this.field_104025_t;
        synchronized (this.field_104025_t) {
            if (this.field_92025_p.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                final GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                var5.disableSecurityWarning();
                this.mc.displayGuiScreen(var5);
            }
        }
        // monitorexit(this.field_104025_t)
    }
}
