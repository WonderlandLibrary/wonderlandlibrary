// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient;

import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.event.events.EventKey;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.elementars.eclient.util.ColorUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.xulu.newgui.util.ColorUtil;
import net.minecraft.client.gui.ScaledResolution;
import com.elementars.eclient.module.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import java.awt.Color;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.event.events.AnnouncerRegistry;
import com.elementars.eclient.event.events.KeyRegistry;
import com.elementars.eclient.util.ConnectionUtil;
import com.elementars.eclient.util.LagUtil;
import com.elementars.eclient.util.LagCompensator;
import com.elementars.eclient.font.FontInit;
import com.elementars.eclient.cape.Capes;
import com.elementars.eclient.util.ColorTextUtils;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import com.elementars.eclient.font.XFontRenderer;
import com.elementars.eclient.font.custom.CustomFont;
import java.awt.Font;
import com.elementars.eclient.command.Command;
import java.awt.GraphicsEnvironment;
import com.elementars.eclient.font.CFontManager;
import com.elementars.eclient.guirewrite.HUD;
import dev.xulu.newgui.NewGUI;
import com.elementars.eclient.command.CommandManager;
import com.elementars.eclient.macro.MacroManager;
import com.elementars.eclient.module.ModuleManager;
import com.elementars.eclient.event.EventManager;
import dev.xulu.settings.ValueManager;
import com.elementars.eclient.util.TaskScheduler;
import com.elementars.eclient.util.GLSLSandboxShader;
import com.elementars.eclient.font.rainbow.RainbowCycle;
import java.text.DecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import com.elementars.eclient.util.Helper;

@Mod(modid = "eclient", name = "Xulu", version = "v1.5.2")
public class Xulu implements Helper
{
    public static final String id = "eclient";
    public static final String name = "Xulu";
    public static final String version = "v1.5.2";
    public static final String creator = "Elementars";
    public Minecraft mc;
    public static DecimalFormat df;
    public static RainbowCycle rainbowCycle;
    public static GLSLSandboxShader backgroundShader;
    public static long initTime;
    @Mod.Instance
    public static Xulu INSTANCE;
    private DiscordRP discordRP;
    public static final TaskScheduler TASK_SCHEDULER;
    public static final ValueManager VALUE_MANAGER;
    public static final EventManager EVENT_MANAGER;
    public static final ModuleManager MODULE_MANAGER;
    public static final MacroManager MACRO_MANAGER;
    public static final CommandManager COMMAND_MANAGER;
    public static NewGUI newGUI;
    public static HUD hud;
    public static boolean CustomFont;
    public static int rgb;
    public static CFontManager cFontRenderer;
    private boolean shownLag;
    private int beginY;
    private int endY;
    private int yCount;
    
    public Xulu() {
        this.mc = Minecraft.getMinecraft();
        this.discordRP = new DiscordRP();
        this.shownLag = false;
    }
    
    public static String[] getFonts() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }
    
    public static String getCorrectFont(final String stringIn) {
        for (final String s : getFonts()) {
            if (s.equalsIgnoreCase(stringIn)) {
                return s;
            }
        }
        return null;
    }
    
    public static void setCFontRenderer(final String stringIn, final int size) {
        try {
            if (getCorrectFont(stringIn) == null) {
                Command.sendChatMessage("Invalid font!");
                return;
            }
            if (stringIn.equalsIgnoreCase("Comfortaa Regular")) {
                CFontManager.customFont = new CustomFont(new Font("Comfortaa Regular", 0, size), true, false);
                return;
            }
            CFontManager.customFont = new CustomFont(new Font(getCorrectFont(stringIn), 0, size), true, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setcFontRendererDefault() {
        try {
            CFontManager.customFont = new CustomFont(new Font("Verdana", 0, 18), true, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setXdolfFontRenderer(final String stringIn, final int size) {
        try {
            if (getCorrectFont(stringIn) == null && !stringIn.equalsIgnoreCase("Xulu")) {
                Command.sendChatMessage("Invalid font!");
                return;
            }
            CFontManager.xFontRenderer = new XFontRenderer(new Font(getCorrectFont(stringIn), 0, size), true, 8);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setXdolfFontRendererDefault() {
        try {
            CFontManager.xFontRenderer = new XFontRenderer(new Font("Verdana", 0, 36), true, 8);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        System.out.println("[Xulu] Starting client, v1.5.2, created by Elementars");
        Display.setTitle("Xulu v1.5.2");
        this.discordRP.start();
        ColorTextUtils.initColors();
        Capes.getUsersCape();
        final FontInit fontInit = new FontInit();
        fontInit.initFonts();
        Xulu.MODULE_MANAGER.init();
        Xulu.COMMAND_MANAGER.init();
        Xulu.cFontRenderer = new CFontManager();
        Xulu.fileManager.loadDummy();
        Xulu.newGUI = new NewGUI();
        Xulu.hud = new HUD();
        HUD.registerElements();
        Xulu.hud.refreshPanel();
        LagCompensator.INSTANCE = new LagCompensator();
        LagUtil.INSTANCE = new LagUtil();
        ConnectionUtil.INSTANCE = new ConnectionUtil();
        Xulu.fileManager.loadHacks();
        Xulu.fileManager.loadDrawn();
        Xulu.fileManager.loadSettingsList();
        Xulu.fileManager.loadBinds();
        Xulu.fileManager.loadMacros();
        Xulu.fileManager.loadPrefix();
        Xulu.fileManager.loadNewGui();
        Xulu.fileManager.loadFriends();
        Xulu.fileManager.loadEnemies();
        Xulu.fileManager.loadHUD();
        Xulu.fileManager.loadFont();
        Xulu.fileManager.loadStickyNote();
        Xulu.fileManager.loadWelcomeMessage();
        Xulu.fileManager.loadXray();
        Xulu.fileManager.loadSearch();
        Xulu.fileManager.loadWaypoints();
        Xulu.fileManager.loadNicks();
        Xulu.EVENT_BUS.register((Object)new KeyRegistry());
        Xulu.EVENT_BUS.register((Object)this);
        AnnouncerRegistry.initAnnouncer();
        Xulu.EVENT_MANAGER.register(this);
        Xulu.rgb = Color.HSBtoRGB(0.01f, Global.rainbowSaturation.getValue() / 255.0f, Global.rainbowLightness.getValue() / 255.0f);
    }
    
    @SubscribeEvent
    public void onRenderGui(final RenderGameOverlayEvent.Post event) {
        Xulu.CustomFont = Xulu.MODULE_MANAGER.getModule(com.elementars.eclient.module.core.CustomFont.class).isToggled();
        if (this.beginY != (Xulu.CustomFont ? (-Xulu.cFontRenderer.getHeight()) : ((float)(-Xulu.fontRenderer.FONT_HEIGHT)))) {
            this.beginY = (Xulu.CustomFont ? ((int)(-Xulu.cFontRenderer.getHeight())) : (-Xulu.fontRenderer.FONT_HEIGHT));
        }
        if (this.endY != 3.0f) {
            this.endY = 3;
        }
        final ScaledResolution s = new ScaledResolution(this.mc);
        Rainbow.updateRainbow();
        Xulu.rgb = Rainbow.rgb;
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }
        if (!Xulu.VALUE_MANAGER.getValueByName("Rainbow Watermark").getValue()) {
            Xulu.rgb = ColorUtil.getClickGUIColor().getRGB();
        }
        final String playername = this.mc.player.getName();
        final int height = s.getScaledHeight() - 3;
        if (Global.coordinates.getValue()) {
            final String coords = Xulu.df.format(this.mc.player.posX) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Xulu.df.format(this.mc.player.posY) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Xulu.df.format(this.mc.player.posZ) + ChatFormatting.GRAY + " [" + ChatFormatting.RESET + ((this.mc.player.dimension == -1) ? (Xulu.df.format(this.mc.player.posX * 8.0) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Xulu.df.format(this.mc.player.posY) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Xulu.df.format(this.mc.player.posZ * 8.0)) : (Xulu.df.format(this.mc.player.posX / 8.0) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Xulu.df.format(this.mc.player.posY) + ChatFormatting.GRAY + ", " + ChatFormatting.RESET + Xulu.df.format(this.mc.player.posZ / 8.0))) + ChatFormatting.GRAY + "]";
            if (Xulu.CustomFont) {
                Xulu.cFontRenderer.drawStringWithShadow(coords, 3.0, height - Xulu.cFontRenderer.getHeight() - 1.0f - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 11 : 0), ColorUtils.changeAlpha(Color.white.getRGB(), Global.hudAlpha.getValue()));
            }
            else {
                Xulu.fontRenderer.drawStringWithShadow(coords, 3.0f, (float)(height - Xulu.fontRenderer.FONT_HEIGHT - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 11 : 0)), ColorUtils.changeAlpha(Color.white.getRGB(), Global.hudAlpha.getValue()));
            }
        }
        if (Global.direction.getValue()) {
            if (Xulu.CustomFont) {
                Xulu.cFontRenderer.drawStringWithShadow(this.getFacing(this.mc.player.getHorizontalFacing().getName().toUpperCase()), 3.0, height - Xulu.cFontRenderer.getHeight() - (Global.coordinates.getValue() ? 11 : 1) - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 11 : 0), ColorUtils.changeAlpha(Color.white.getRGB(), Global.hudAlpha.getValue()));
            }
            else {
                Xulu.fontRenderer.drawStringWithShadow(this.getFacing(this.mc.player.getHorizontalFacing().getName().toUpperCase()), 3.0f, (float)(height - Xulu.fontRenderer.FONT_HEIGHT - (Global.coordinates.getValue() ? 10 : 0) - (this.mc.ingameGUI.getChatGUI().getChatOpen() ? 11 : 0)), ColorUtils.changeAlpha(Color.white.getRGB(), Global.hudAlpha.getValue()));
            }
        }
        if (Global.showLag.getValue() && !this.mc.isIntegratedServerRunning()) {
            if (LagUtil.INSTANCE.getLastTimeDiff() != 0L && LagUtil.INSTANCE.getLastTimeDiff() > 5000L) {
                final String text = String.format("Server has been lagging for %01.1fs", (LagUtil.INSTANCE.getLastTimeDiff() - 1000L) / 1000.0f);
                if (!this.shownLag) {
                    this.yCount = this.beginY;
                }
                if (Xulu.CustomFont) {
                    Xulu.cFontRenderer.drawStringWithShadow(text, s.getScaledWidth() / 2 - Xulu.cFontRenderer.getStringWidth(text) / 2, this.yCount, Global.lagColor.getValue().equalsIgnoreCase("Default") ? Color.LIGHT_GRAY.getRGB() : ColorUtil.getClickGUIColor().getRGB());
                }
                else {
                    Xulu.fontRenderer.drawStringWithShadow(text, (float)(s.getScaledWidth() / 2 - Xulu.fontRenderer.getStringWidth(text) / 2), (float)this.yCount, Global.lagColor.getValue().equalsIgnoreCase("Default") ? Color.LIGHT_GRAY.getRGB() : ColorUtil.getClickGUIColor().getRGB());
                }
                this.shownLag = true;
                if (this.yCount != this.endY) {
                    ++this.yCount;
                }
            }
            else if (this.shownLag) {
                final String text = "Server has been lagging for 0.0s";
                if (Xulu.CustomFont) {
                    Xulu.cFontRenderer.drawStringWithShadow(text, s.getScaledWidth() / 2 - Xulu.cFontRenderer.getStringWidth(text) / 2, this.yCount, Global.lagColor.getValue().equalsIgnoreCase("Default") ? Color.LIGHT_GRAY.getRGB() : ColorUtil.getClickGUIColor().getRGB());
                }
                else {
                    Xulu.fontRenderer.drawStringWithShadow(text, (float)(s.getScaledWidth() / 2 - Xulu.fontRenderer.getStringWidth(text) / 2), (float)this.yCount, Global.lagColor.getValue().equalsIgnoreCase("Default") ? Color.LIGHT_GRAY.getRGB() : ColorUtil.getClickGUIColor().getRGB());
                }
                if (this.yCount != this.beginY) {
                    --this.yCount;
                }
                else {
                    this.shownLag = false;
                }
            }
        }
    }
    
    public static void save() {
        Xulu.fileManager.saveHacks();
        Xulu.fileManager.saveBinds();
        Xulu.fileManager.saveDrawn();
        Xulu.fileManager.saveSettingsList();
        Xulu.fileManager.saveMacros();
        Xulu.fileManager.savePrefix();
        Xulu.fileManager.saveNewGui();
        Xulu.fileManager.saveFriends();
        Xulu.fileManager.saveEnemies();
        Xulu.fileManager.saveHUD();
        Xulu.fileManager.saveFont();
        Xulu.fileManager.saveStickyNote();
        Xulu.fileManager.saveWelcomeMessage();
        Xulu.fileManager.saveDummy();
        Xulu.fileManager.saveXray();
        Xulu.fileManager.saveSearch();
        Xulu.fileManager.saveWaypoints();
        Xulu.fileManager.saveNicks();
        System.out.println("Xulu Saved!");
    }
    
    public static void load() {
        Xulu.fileManager.loadHacks();
        Xulu.fileManager.loadBinds();
        Xulu.fileManager.loadDrawn();
        Xulu.fileManager.loadSettingsList();
        Xulu.fileManager.loadMacros();
        Xulu.fileManager.loadPrefix();
        Xulu.fileManager.loadNewGui();
        Xulu.fileManager.loadFriends();
        Xulu.fileManager.loadEnemies();
        Xulu.fileManager.loadHUD();
        Xulu.fileManager.loadFont();
        Xulu.fileManager.loadStickyNote();
        Xulu.fileManager.loadWelcomeMessage();
        Xulu.fileManager.loadDummy();
        Xulu.fileManager.loadXray();
        Xulu.fileManager.loadSearch();
        Xulu.fileManager.loadWaypoints();
        Xulu.fileManager.loadNicks();
        System.out.println("Xulu Loaded!");
    }
    
    public void stopClient() {
        save();
        Xulu.MODULE_MANAGER.getModules().forEach(Module::destroy);
        Xulu.EVENT_MANAGER.unregister(this);
    }
    
    private String getFacing(final String in) {
        final String facing = getTitle(in);
        String add;
        if (in.equalsIgnoreCase("North")) {
            add = " §7[§r-Z§7]";
        }
        else if (in.equalsIgnoreCase("East")) {
            add = " §7[§r+X§7]";
        }
        else if (in.equalsIgnoreCase("South")) {
            add = " §7[§r+Z§7]";
        }
        else if (in.equalsIgnoreCase("West")) {
            add = " §7[§r-X§7]";
        }
        else {
            add = " ERROR";
        }
        return facing + add;
    }
    
    public static String getTitle(String in) {
        in = Character.toUpperCase(in.toLowerCase().charAt(0)) + in.toLowerCase().substring(1);
        return in;
    }
    
    @EventTarget
    public void onKey(final EventKey eventKey) {
        Xulu.MACRO_MANAGER.runMacros(eventKey.getKey());
        Xulu.MODULE_MANAGER.getModules().stream().filter(module -> module.getKey() == eventKey.getKey()).forEach(Module::toggle);
    }
    
    static {
        Xulu.df = new DecimalFormat("##,###,###,###,##0.00");
        Xulu.rainbowCycle = new RainbowCycle();
        TASK_SCHEDULER = new TaskScheduler();
        VALUE_MANAGER = new ValueManager();
        EVENT_MANAGER = new EventManager();
        MODULE_MANAGER = new ModuleManager();
        MACRO_MANAGER = new MacroManager();
        COMMAND_MANAGER = new CommandManager();
    }
    
    public static class Rainbow
    {
        private static int rgb;
        public static int a;
        public static int r;
        public static int g;
        public static int b;
        static float hue;
        
        public static void updateRainbow() {
            Rainbow.rgb = Color.HSBtoRGB(Rainbow.hue, Global.rainbowSaturation.getValue() / 255.0f, Global.rainbowLightness.getValue() / 255.0f);
            Rainbow.a = (Rainbow.rgb >>> 24 & 0xFF);
            Rainbow.r = (Rainbow.rgb >>> 16 & 0xFF);
            Rainbow.g = (Rainbow.rgb >>> 8 & 0xFF);
            Rainbow.b = (Rainbow.rgb & 0xFF);
            Rainbow.hue += Global.rainbowspeed2.getValue() / 100000.0f;
            if (Rainbow.hue > 1.0f) {
                --Rainbow.hue;
            }
        }
        
        static {
            Rainbow.hue = 0.01f;
        }
    }
}
