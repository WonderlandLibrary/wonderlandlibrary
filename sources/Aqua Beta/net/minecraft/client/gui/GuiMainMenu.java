package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.Hero.settings.GuiColorChooser2;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.alt.design.AltManager;
import intent.AquaDev.aqua.fontrenderer.ClientFont;
import intent.AquaDev.aqua.fontrenderer.GlyphPageFontRenderer;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import net.aql.Lib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.IMGButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorConstructor;
import net.optifine.reflect.ReflectorMethod;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu
extends GuiScreen
implements GuiYesNoCallback {
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    public Animate anim = new Animate();
    public static GlyphPageFontRenderer font3 = ClientFont.font((int)40, (String)"Comfortaa-Regular", (boolean)true);
    private final float updateCounter;
    public static GuiColorChooser2 colorChooser2;
    private String splashText = "missingno";
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private final boolean field_175375_v = true;
    private final Object threadLock = new Object();
    boolean onStart = true;
    private String openGLWarning1;
    private String openGLWarning2 = field_96138_a;
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
    private GuiButton realmsButton;
    private boolean field_183502_L = false;
    private GuiScreen field_183503_M;
    private GuiButton modButton;
    private GuiScreen modUpdateNotification;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public GuiMainMenu() {
        BufferedReader bufferedreader = null;
        try {
            String s;
            ArrayList list = Lists.newArrayList();
            bufferedreader = new BufferedReader((Reader)new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
            while ((s = bufferedreader.readLine()) != null) {
                if ((s = s.trim()).isEmpty()) continue;
                list.add((Object)s);
            }
            if (!list.isEmpty()) {
                do {
                    this.splashText = (String)list.get(RANDOM.nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (IOException iOException) {
        }
        finally {
            if (bufferedreader != null) {
                try {
                    bufferedreader.close();
                }
                catch (IOException iOException) {}
            }
        }
        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format((String)"title.oldgl1", (Object[])new Object[0]);
            this.openGLWarning2 = I18n.format((String)"title.oldgl2", (Object[])new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    private boolean func_183501_a() {
        return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.field_183503_M != null;
    }

    public void updateScreen() {
        ++this.panoramaTimer;
        if (this.func_183501_a()) {
            this.field_183503_M.updateScreen();
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        } else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        int i = 24;
        int j = height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        } else {
            this.buttonList.add((Object)new IMGButton(1, width / 2 - 85, j + 72 - 20, 50, 50, "Singleplayer", new ResourceLocation("Aqua/gui/sp1.png")));
        }
        this.buttonList.add((Object)new IMGButton(2, width / 2 + 60, j + 72 - 20, 50, 50, "Multiplayer", new ResourceLocation("Aqua/gui/mp.png")));
        this.buttonList.add((Object)new IMGButton(26, width / 2 - 10, j + 72 - 20, 50, 50, "AltManager", new ResourceLocation("Aqua/gui/am.png")));
        Object object = this.threadLock;
        synchronized (object) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int k = Math.max((int)this.field_92023_s, (int)this.field_92024_r);
            this.field_92022_t = (width - k) / 2;
            this.field_92021_u = ((GuiButton)this.buttonList.get((int)0)).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }
        if (!Aqua.allowed) {
            try {
                Display.releaseContext();
            }
            catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
        this.mc.setConnectedToRealms(false);
        if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.field_183502_L) {
            RealmsBridge realmsbridge = new RealmsBridge();
            this.field_183502_L = true;
        }
        if (this.func_183501_a()) {
            this.field_183503_M.setGuiSize(width, height);
            this.field_183503_M.initGui();
        }
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        this.buttonList.add((Object)new GuiButton(1, width / 2 - 50, p_73969_1_, I18n.format((String)"menu.singleplayer", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(2, width / 2 - 50, p_73969_1_ + p_73969_2_ * 1, I18n.format((String)"menu.multiplayer", (Object[])new Object[0])));
    }

    private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
        this.buttonList.add((Object)new GuiButton(11, width / 2 - 100, p_73972_1_, I18n.format((String)"menu.playdemo", (Object[])new Object[0])));
        this.buttonResetDemo = new GuiButton(12, width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format((String)"menu.resetdemo", (Object[])new Object[0]));
        this.buttonList.add((Object)this.buttonResetDemo);
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        ISaveFormat isaveformat;
        WorldInfo worldinfo;
        if (button.id == 0) {
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        }
        if (button.id == 26) {
            this.mc.displayGuiScreen((GuiScreen)new AltManager((GuiScreen)this));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        if (button.id == 14 && this.realmsButton.visible) {
            this.switchToRealms();
        }
        if (button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
            this.mc.displayGuiScreen((GuiScreen)Reflector.newInstance((ReflectorConstructor)Reflector.GuiModList_Constructor, (Object[])new Object[]{this}));
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12 && (worldinfo = (isaveformat = this.mc.getSaveLoader()).getWorldInfo("Demo_World")) != null) {
            GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo((GuiYesNoCallback)this, (String)worldinfo.getWorldName(), (int)12);
            this.mc.displayGuiScreen((GuiScreen)guiyesno);
        }
    }

    private void switchToRealms() {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms((GuiScreen)this);
    }

    public void confirmClicked(boolean result, int id) {
        if (result && id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen((GuiScreen)this);
        } else if (id == 13) {
            if (result) {
                try {
                    Class oclass = Class.forName((String)"java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI(this.openGLWarningLink)});
                }
                catch (Throwable throwable) {
                    logger.error("Couldn't open link", throwable);
                }
            }
            this.mc.displayGuiScreen((GuiScreen)this);
        }
    }

    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode((int)5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GlStateManager.matrixMode((int)5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.rotate((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        int i = 8;
        int j = 64;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur1();
        }
        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();
            float f = ((float)(k % 8) / 8.0f - 0.5f) / 64.0f;
            float f1 = ((float)(k / 8) / 8.0f - 0.5f) / 64.0f;
            float f2 = 0.0f;
            GlStateManager.translate((float)f, (float)f1, (float)0.0f);
            GlStateManager.rotate((float)(MathHelper.sin((float)(((float)this.panoramaTimer + p_73970_3_) / 400.0f)) * 25.0f + 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)(-((float)this.panoramaTimer + p_73970_3_) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();
                if (l == 1) {
                    GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (l == 2) {
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (l == 3) {
                    GlStateManager.rotate((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (l == 4) {
                    GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (l == 5) {
                    GlStateManager.rotate((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                ResourceLocation[] aresourcelocation = titlePanoramaPaths;
                if (custompanoramaproperties != null) {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }
                this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int i1 = 255 / (k + 1);
                float f3 = 0.0f;
                worldrenderer.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, i1).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        }
        worldrenderer.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.matrixMode((int)5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)256, (int)256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;
        int j = 3;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur2();
        }
        for (int k = 0; k < j; ++k) {
            float f = 1.0f / (float)(k + 1);
            int l = width;
            int i1 = height;
            float f1 = (float)(k - 1) / 256.0f;
            worldrenderer.pos((double)l, (double)i1, (double)zLevel).tex((double)(0.0f + f1), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos((double)l, 0.0, (double)zLevel).tex((double)(1.0f + f1), 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(0.0, 0.0, (double)zLevel).tex((double)(1.0f + f1), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(0.0, (double)i1, (double)zLevel).tex((double)(0.0f + f1), 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
    }

    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport((int)0, (int)0, (int)256, (int)256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        int i = 3;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            i = custompanoramaproperties.getBlur3();
        }
        for (int j = 0; j < i; ++j) {
            this.rotateAndBlurSkybox(p_73971_3_);
            this.rotateAndBlurSkybox(p_73971_3_);
        }
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight);
        float f2 = width > height ? 120.0f / (float)width : 120.0f / (float)height;
        float f = (float)height * f2 / 256.0f;
        float f1 = (float)width * f2 / 256.0f;
        int k = width;
        int l = height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, (double)l, (double)zLevel).tex((double)(0.5f - f), (double)(0.5f + f1)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos((double)k, (double)l, (double)zLevel).tex((double)(0.5f - f), (double)(0.5f - f1)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos((double)k, 0.0, (double)zLevel).tex((double)(0.5f + f), (double)(0.5f - f1)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, (double)zLevel).tex((double)(0.5f + f), (double)(0.5f + f1)).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 274;
        int j = width / 2 - 137;
        int k = 30;
        int l = -2130706433;
        int i1 = 0xFFFFFF;
        int j1 = 0;
        int k1 = Integer.MIN_VALUE;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            l = custompanoramaproperties.getOverlay1Top();
            i1 = custompanoramaproperties.getOverlay1Bottom();
            j1 = custompanoramaproperties.getOverlay2Top();
            k1 = custompanoramaproperties.getOverlay2Bottom();
        }
        if (l != 0 || i1 != 0) {
            this.drawGradientRect(0, 0, width, height, l, i1);
        }
        if (j1 != 0 || k1 != 0) {
            this.drawGradientRect(0, 0, width, height, j1, k1);
        }
        Aqua.INSTANCE.shaderBackground.renderShader();
        if (this.onStart) {
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        }
        if (this.mouseOver(mouseX, mouseY, 0, 5, 140, 150)) {
            RenderUtil.drawRoundedRect2Alpha((double)-1.0, (double)5.0, (double)140.0, (double)150.0, (double)3.0, (Color)new Color(0, 0, 0, 100));
        } else {
            RenderUtil.drawRoundedRect2Alpha((double)-1.0, (double)5.0, (double)140.0, (double)20.0, (double)3.0, (Color)new Color(0, 0, 0, 100));
        }
        RenderUtil.drawRoundedRect2Alpha((double)0.0, (double)23.0, (double)138.5, (double)2.0, (double)0.0, (Color)new Color(255, 255, 255, 255));
        Aqua.INSTANCE.comfortaa4.drawString("Aqua B1.7", 45.0f, 10.0f, -1);
        RenderUtil.drawRoundedRect2Alpha((double)((float)width / 2.0f - 95.0f), (double)((float)height / 4.0f + 85.0f), (double)225.0, (double)80.0, (double)6.0, (Color)new Color(0, 0, 0, 100));
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(width / 2 + 90), (float)70.0f, (float)0.0f);
        GlStateManager.rotate((float)-20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float f = 1.8f - MathHelper.abs((float)(MathHelper.sin((float)((float)(Minecraft.getSystemTime() % 1000L) / 1000.0f * (float)Math.PI * 2.0f)) * 0.1f));
        GlStateManager.scale((float)f, (float)f, (float)f);
        GlStateManager.popMatrix();
        String s = "" + Aqua.name + " : \u00a77UID " + Lib.getUID();
        if (this.mc.isDemo()) {
            s = s + " Demo";
        }
        if (Reflector.FMLCommonHandler_getBrandings.exists()) {
            Object object = Reflector.call((ReflectorMethod)Reflector.FMLCommonHandler_instance, (Object[])new Object[0]);
            List list = Lists.reverse((List)((List)Reflector.call((Object)object, (ReflectorMethod)Reflector.FMLCommonHandler_getBrandings, (Object[])new Object[]{true})));
            for (int l1 = 0; l1 < list.size(); ++l1) {
                String s1 = (String)list.get(l1);
                if (Strings.isNullOrEmpty((String)s1)) continue;
                this.drawString(this.fontRendererObj, s1, 2, height - (10 + l1 * (FontRenderer.FONT_HEIGHT + 1)), 0xFFFFFF);
            }
            if (Reflector.ForgeHooksClient_renderMainMenu.exists()) {
                Object[] objectArray = new Object[4];
                objectArray[0] = this;
                objectArray[1] = this.fontRendererObj;
                objectArray[2] = width;
                objectArray[3] = height;
                Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_renderMainMenu, (Object[])objectArray);
            }
        } else {
            Aqua.INSTANCE.comfortaa3.drawString(s, 5.0f, (float)(height - 12), -1);
        }
        String s2 = "Client by LCA_MODZ";
        Aqua.INSTANCE.comfortaa3.drawString("Client by LCA_MODZ", (float)(width - this.fontRendererObj.getStringWidth("Client by LCA_MODZ") - 13), (float)(height - 13), -1);
        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
            GuiMainMenu.drawRect((int)(this.field_92022_t - 2), (int)(this.field_92021_u - 2), (int)(this.field_92020_v + 2), (int)(this.field_92019_w - 1), (int)0x55200000);
            this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.fontRendererObj, this.openGLWarning2, (width - this.field_92024_r) / 2, ((GuiButton)this.buttonList.get((int)0)).yPosition - 12, -1);
        }
        if (!this.onStart || Mouse.isButtonDown((int)0)) {
            // empty if block
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.func_183501_a()) {
            this.field_183503_M.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (this.modUpdateNotification != null) {
            this.modUpdateNotification.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Object object = this.threadLock;
        synchronized (object) {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink((GuiYesNoCallback)this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen((GuiScreen)guiconfirmopenlink);
            }
        }
        if (this.func_183501_a()) {
            this.field_183503_M.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void onGuiClosed() {
        if (this.field_183503_M != null) {
            this.field_183503_M.onGuiClosed();
        }
    }

    private boolean mouseOver(int x, int y, int modX, int modY, int modWidth, int modHeight) {
        return x >= modX && x <= modX + modWidth && y >= modY && y <= modY + modHeight;
    }

    static {
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
}
