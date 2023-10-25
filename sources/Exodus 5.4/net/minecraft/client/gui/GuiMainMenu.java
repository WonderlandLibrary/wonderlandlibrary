/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.io.Charsets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.GLContext
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.ui.GuiAltManager;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLContext;

public class GuiMainMenu
extends GuiScreen
implements GuiYesNoCallback {
    private static final Random RANDOM;
    public static final String field_96138_a;
    private final Object threadLock;
    private String openGLWarningLink;
    private ResourceLocation backgroundTexture;
    private String openGLWarning2;
    private static final ResourceLocation minecraftTitleTextures;
    private String openGLWarning1;
    String texture;
    private int field_92024_r;
    private String splashText;
    private int field_92021_u;
    private GuiButton realmsButton;
    private GuiButton buttonResetDemo;
    private DynamicTexture viewportTexture;
    private static final Logger logger;
    private int field_92022_t;
    private int field_92019_w;
    private int field_92023_s;
    private int field_92020_v;
    private static final ResourceLocation[] titlePanoramaPaths;
    private int panoramaTimer;
    private boolean field_175375_v;
    private static final ResourceLocation splashTexts;
    private float updateCounter;
    private static final AtomicInteger field_175373_f;

    private void addSingleplayerMultiplayerButtons(int n, int n2) {
        this.buttonList.add(new GuiButton(1, GuiScreen.width / 2 - 50, GuiScreen.height / 2 - 17, 98, 20, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, GuiScreen.width / 2 - 50, GuiScreen.height / 2 + 5, 98, 20, I18n.format("menu.multiplayer", new Object[0])));
    }

    @Override
    public void initGui() {
        this.texture = "Terror/chillbg.jpg";
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
        int n = 24;
        int n2 = height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(n2, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(n2, 24);
        }
        this.buttonList.add(new GuiButton(0, width / 2 - 50, height / 2 + 50, 98, 20, I18n.format("Settings", new Object[0])));
        this.buttonList.add(new GuiButton(4, width / 2 - 50, height / 2 + 73, 98, 20, I18n.format("Exit", new Object[0])));
        this.buttonList.add(new GuiButton(500, width / 2 - 50, height / 2 + 27, 98, 20, I18n.format("Alt Manager", new Object[0])));
        this.drawCenteredString(Minecraft.fontRendererObj, Exodus.INSTANCE.name, width, height, -1);
        Object object = this.threadLock;
        synchronized (object) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int n3 = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (width - n3) / 2;
            this.field_92021_u = ((GuiButton)this.buttonList.get((int)0)).yPosition - 24;
            this.field_92020_v = this.field_92022_t + n3;
            this.field_92019_w = this.field_92021_u + 24;
        }
        this.mc.func_181537_a(false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void switchToRealms() {
        RealmsBridge realmsBridge = new RealmsBridge();
        realmsBridge.switchToRealms(this);
    }

    @Override
    public void confirmClicked(boolean bl, int n) {
        if (bl && n == 12) {
            ISaveFormat iSaveFormat = this.mc.getSaveLoader();
            iSaveFormat.flushCache();
            iSaveFormat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (n == 13) {
            if (bl) {
                try {
                    Class<?> clazz = Class.forName("java.awt.Desktop");
                    Object object = clazz.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    clazz.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (Throwable throwable) {
                    logger.error("Couldn't open link", throwable);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }

    @Override
    public void updateScreen() {
        ++this.panoramaTimer;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        ISaveFormat iSaveFormat;
        WorldInfo worldInfo;
        if (guiButton.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, Minecraft.gameSettings));
        }
        if (guiButton.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, Minecraft.gameSettings, this.mc.getLanguageManager()));
        }
        if (guiButton.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (guiButton.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (guiButton.id == 14 && this.realmsButton.visible) {
            this.switchToRealms();
        }
        if (guiButton.id == 4) {
            this.mc.shutdown();
        }
        if (guiButton.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (guiButton.id == 12 && (worldInfo = (iSaveFormat = this.mc.getSaveLoader()).getWorldInfo("Demo_World")) != null) {
            GuiYesNo guiYesNo = GuiSelectWorld.func_152129_a(this, worldInfo.getWorldName(), 12);
            this.mc.displayGuiScreen(guiYesNo);
        }
        if (guiButton.id == 500) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }
    }

    private void addDemoButtons(int n, int n2) {
        this.buttonList.add(new GuiButton(11, width / 2 - 100, n, I18n.format("menu.playdemo", new Object[0])));
        this.buttonResetDemo = new GuiButton(12, width / 2 - 100, n + n2 * 1, I18n.format("menu.resetdemo", new Object[0]));
        this.buttonList.add(this.buttonResetDemo);
        ISaveFormat iSaveFormat = this.mc.getSaveLoader();
        WorldInfo worldInfo = iSaveFormat.getWorldInfo("Demo_World");
        if (worldInfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
    }

    static {
        field_175373_f = new AtomicInteger(0);
        logger = LogManager.getLogger();
        RANDOM = new Random();
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
        field_96138_a = "Please click " + (Object)((Object)EnumChatFormatting.UNDERLINE) + "here" + (Object)((Object)EnumChatFormatting.RESET) + " for more information.";
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        Object object = this.threadLock;
        synchronized (object) {
            if (this.openGLWarning1.length() > 0 && n >= this.field_92022_t && n <= this.field_92020_v && n2 >= this.field_92021_u && n2 <= this.field_92019_w) {
                GuiConfirmOpenLink guiConfirmOpenLink = new GuiConfirmOpenLink((GuiYesNoCallback)this, this.openGLWarningLink, 13, true);
                guiConfirmOpenLink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiConfirmOpenLink);
            }
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(this.texture));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        int n3 = 274;
        int n4 = width / 2 - n3 / 2;
        int n5 = 30;
        GuiMainMenu.drawGradientRect(0.0, 0.0, width, height, -2130706433, 0xFFFFFF);
        GuiMainMenu.drawGradientRect(0.0, 0.0, width, height, 0, Integer.MIN_VALUE);
        String string = "Exodus B5 | Minecraft 1.8.8";
        if (this.mc.isDemo()) {
            string = String.valueOf(string) + " Demo";
        }
        FontUtil.normal.drawString(string, 2.0, height - 10, -1);
        String string2 = "Exodus is not affiliated with Mojang AB!";
        FontUtil.normal.drawString(string2, width - this.fontRendererObj.getStringWidth(string2) - 2, height - 10, -1);
        GlStateManager.translate(4.0f, 4.0f, 0.0f);
        GlStateManager.scale(2.0f, 2.0f, 1.0f);
        GlStateManager.translate(-4.0f, -4.0f, 0.0f);
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawString("E", 225.0, 85.0, CustomIngameGui.getColorInt(height / 8));
        Minecraft.getMinecraft();
        Minecraft.fontRendererObj.drawString("xodus", 231.0, 85.0, -1);
        GlStateManager.translate(4.0f, 4.0f, 0.0f);
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.translate(-4.0f, -4.0f, 0.0f);
        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
            GuiMainMenu.drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 0x55200000);
            this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.fontRendererObj, this.openGLWarning2, (width - this.field_92024_r) / 2, ((GuiButton)this.buttonList.get((int)0)).yPosition - 12, -1);
        }
        super.drawScreen(n, n2, f);
    }

    public GuiMainMenu() {
        block13: {
            BufferedReader bufferedReader;
            block12: {
                this.texture = "Terror/background.jpg";
                this.field_175375_v = true;
                this.threadLock = new Object();
                this.openGLWarning2 = field_96138_a;
                this.splashText = "missingno";
                bufferedReader = null;
                try {
                    String string;
                    ArrayList arrayList = Lists.newArrayList();
                    bufferedReader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
                    while ((string = bufferedReader.readLine()) != null) {
                        if ((string = string.trim()).isEmpty()) continue;
                        arrayList.add(string);
                    }
                    if (arrayList.isEmpty()) break block12;
                    do {
                        this.splashText = (String)arrayList.get(RANDOM.nextInt(arrayList.size()));
                    } while (this.splashText.hashCode() == 125780783);
                }
                catch (IOException iOException) {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        }
                        catch (IOException iOException2) {}
                    }
                    break block13;
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }
}

