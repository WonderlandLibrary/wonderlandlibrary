// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;
import net.optifine.CustomPanoramaProperties;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.optifine.CustomPanorama;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import java.net.URI;
import net.minecraft.world.demo.DemoWorldServer;
import dev.tenacity.Tenacity;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;
import java.util.Date;
import java.util.Calendar;
import dev.tenacity.ui.mainmenu.CustomMainMenu;
import java.util.List;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f;
    private static final Logger logger;
    private static final Random RANDOM;
    private float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v;
    private final Object threadLock;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
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
    private ResourceLocation backgroundTexture;
    private GuiScreen M;
    private GuiButton modButton;
    private GuiScreen modUpdateNotification;
    
    public GuiMainMenu() {
        this.field_175375_v = true;
        this.threadLock = new Object();
        this.openGLWarning2 = GuiMainMenu.field_96138_a;
        final boolean l = false;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;
        try {
            final List<String> list = (List<String>)Lists.newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(GuiMainMenu.splashTexts).getInputStream(), Charsets.UTF_8));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                if (!s.isEmpty()) {
                    list.add(s);
                }
            }
            if (!list.isEmpty()) {
                do {
                    this.splashText = list.get(GuiMainMenu.RANDOM.nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (IOException ex) {}
        finally {
            if (bufferedreader != null) {
                try {
                    bufferedreader.close();
                }
                catch (IOException ex2) {}
            }
        }
        this.updateCounter = GuiMainMenu.RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }
    
    private boolean a() {
        return false;
    }
    
    @Override
    public void updateScreen() {
        ++this.panoramaTimer;
        if (this.a()) {
            this.M.updateScreen();
        }
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
        GuiMainMenu.mc.displayGuiScreen(new CustomMainMenu());
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = GuiMainMenu.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        final int i = 24;
        final int j = this.height / 4 + 48;
        if (GuiMainMenu.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        }
        else {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, j + 72 + 12));
        synchronized (this.threadLock) {
            this.field_92023_s = (int)this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = (int)this.fontRendererObj.getStringWidth(this.openGLWarning2);
            final int k = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - k) / 2;
            this.field_92021_u = this.buttonList.get(0).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }
        if (this.a()) {
            this.M.a(this.width, this.height);
            this.M.initGui();
        }
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "high quality alt manager"));
    }
    
    private void addDemoButtons(final int p_73972_1_, final int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
        final ISaveFormat isaveformat = GuiMainMenu.mc.getSaveLoader();
        final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            GuiMainMenu.mc.displayGuiScreen(new GuiOptions(this, GuiMainMenu.mc.gameSettings));
        }
        if (button.id == 5) {
            GuiMainMenu.mc.displayGuiScreen(new GuiLanguage(this, GuiMainMenu.mc.gameSettings, GuiMainMenu.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            GuiMainMenu.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 2) {
            GuiMainMenu.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 3) {
            GuiMainMenu.mc.displayGuiScreen(Tenacity.INSTANCE.getAltManager());
        }
        if (button.id == 4) {
            GuiMainMenu.mc.shutdown();
        }
        if (button.id == 11) {
            GuiMainMenu.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12) {
            final ISaveFormat isaveformat = GuiMainMenu.mc.getSaveLoader();
            final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
            if (worldinfo != null) {
                final GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo(this, worldinfo.getWorldName(), 12);
                GuiMainMenu.mc.displayGuiScreen(guiyesno);
            }
        }
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result && id == 12) {
            final ISaveFormat isaveformat = GuiMainMenu.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            GuiMainMenu.mc.displayGuiScreen(this);
        }
        else if (id == 13) {
            if (result) {
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (Throwable throwable) {
                    GuiMainMenu.logger.error("Couldn't open link", throwable);
                }
            }
            GuiMainMenu.mc.displayGuiScreen(this);
        }
    }
    
    private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
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
        final int i = 8;
        int j = 64;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur1();
        }
        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();
            final float f = (k % i / (float)i - 0.5f) / 64.0f;
            final float f2 = (k / i / (float)i - 0.5f) / 64.0f;
            final float f3 = 0.0f;
            GlStateManager.translate(f, f2, f3);
            GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();
                if (l == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (l == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (l == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (l == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (l == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                ResourceLocation[] aresourcelocation = GuiMainMenu.titlePanoramaPaths;
                if (custompanoramaproperties != null) {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }
                GuiMainMenu.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                final int i2 = 255 / (k + 1);
                final float f4 = 0.0f;
                worldrenderer.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, i2).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, i2).endVertex();
                worldrenderer.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, i2).endVertex();
                worldrenderer.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, i2).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        worldrenderer.setTranslation(0.0, 0.0, 0.0);
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
        GuiMainMenu.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        final int i = 3;
        int j = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur2();
        }
        for (int k = 0; k < j; ++k) {
            final float f = 1.0f / (k + 1);
            final int l = this.width;
            final int i2 = this.height;
            final float f2 = (k - i / 2) / 256.0f;
            worldrenderer.pos(l, i2, GuiMainMenu.zLevel).tex(0.0f + f2, 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(l, 0.0, GuiMainMenu.zLevel).tex(1.0f + f2, 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(0.0, 0.0, GuiMainMenu.zLevel).tex(1.0f + f2, 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(0.0, i2, GuiMainMenu.zLevel).tex(0.0f + f2, 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    
    private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        GuiMainMenu.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        int i = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            i = custompanoramaproperties.getBlur3();
        }
        for (int j = 0; j < i; ++j) {
            this.rotateAndBlurSkybox(p_73971_3_);
            this.rotateAndBlurSkybox(p_73971_3_);
        }
        GuiMainMenu.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, GuiMainMenu.mc.displayWidth, GuiMainMenu.mc.displayHeight);
        final float f2 = (this.width > this.height) ? (120.0f / this.width) : (120.0f / this.height);
        final float f3 = this.height * f2 / 256.0f;
        final float f4 = this.width * f2 / 256.0f;
        final int k = this.width;
        final int l = this.height;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, l, GuiMainMenu.zLevel).tex(0.5f - f3, 0.5f + f4).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(k, l, GuiMainMenu.zLevel).tex(0.5f - f3, 0.5f - f4).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(k, 0.0, GuiMainMenu.zLevel).tex(0.5f + f3, 0.5f - f4).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, GuiMainMenu.zLevel).tex(0.5f + f3, 0.5f + f4).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final int i = 274;
        final int j = this.width / 2 - i / 2;
        final int k = 30;
        int l = -2130706433;
        int i2 = 16777215;
        int j2 = 0;
        int k2 = Integer.MIN_VALUE;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            l = custompanoramaproperties.getOverlay1Top();
            i2 = custompanoramaproperties.getOverlay1Bottom();
            j2 = custompanoramaproperties.getOverlay2Top();
            k2 = custompanoramaproperties.getOverlay2Bottom();
        }
        if (l != 0 || i2 != 0) {
            Gui.drawGradientRect(0.0, 0.0, this.width, this.height, l, i2);
        }
        if (j2 != 0 || k2 != 0) {
            Gui.drawGradientRect(0.0, 0.0, this.width, this.height, j2, k2);
        }
        GuiMainMenu.mc.getTextureManager().bindTexture(GuiMainMenu.minecraftTitleTextures);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.updateCounter < 1.0E-4) {
            this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 99, 44);
            this.drawTexturedModalRect(j + 99, k + 0, 129, 0, 27, 44);
            this.drawTexturedModalRect(j + 99 + 26, k + 0, 126, 0, 3, 44);
            this.drawTexturedModalRect(j + 99 + 26 + 3, k + 0, 99, 0, 26, 44);
            this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
        }
        else {
            this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 155, 44);
            this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2 + 90), 70.0f, 0.0f);
        GlStateManager.rotate(-20.0f, 0.0f, 0.0f, 1.0f);
        float f = 1.8f - MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0f * 3.1415927f * 2.0f) * 0.1f);
        f = f * 100.0f / (this.fontRendererObj.getStringWidth(this.splashText) + 32.0f);
        GlStateManager.scale(f, f, f);
        this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        String s = "Minecraft 1.8.9";
        if (GuiMainMenu.mc.isDemo()) {
            s += " Demo";
        }
        this.drawString(this.fontRendererObj, s, 2, this.height - 10, -1);
        final String s2 = "Copyright Mojang AB. Do not distribute!";
        this.drawString(this.fontRendererObj, s2, (int)(this.width - this.fontRendererObj.getStringWidth(s2) - 2.0f), this.height - 10, -1);
        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
            Gui.drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2, this.buttonList.get(0).yPosition - 12, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.a()) {
            this.M.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (this.modUpdateNotification != null) {
            this.modUpdateNotification.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        synchronized (this.threadLock) {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                final GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                GuiMainMenu.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
        if (this.a()) {
            this.M.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    public void onGuiClosed() {
        if (this.M != null) {
            this.M.onGuiClosed();
        }
    }
    
    static {
        field_175373_f = new AtomicInteger(0);
        logger = LogManager.getLogger();
        RANDOM = new Random();
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
}
