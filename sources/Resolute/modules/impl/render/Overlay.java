// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.util.Collection;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import vip.Resolute.util.render.Translate;
import java.text.DateFormat;
import net.minecraft.client.gui.FontRenderer;
import java.util.Iterator;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.util.misc.MathUtils;
import java.util.concurrent.CopyOnWriteArrayList;
import vip.Resolute.util.render.ColorUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.minecraft.client.Minecraft;
import vip.Resolute.command.impl.Clientname;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.gui.GuiChat;
import vip.Resolute.util.font.FontUtil;
import vip.Resolute.util.render.CompassUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import vip.Resolute.auth.Authentication;
import vip.Resolute.Resolute;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import java.util.Comparator;
import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.awt.Color;
import java.util.ArrayList;
import vip.Resolute.util.misc.TimerUtil;
import java.text.DecimalFormat;
import vip.Resolute.util.font.MinecraftFontRenderer;
import java.util.List;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Overlay extends Module
{
    public ModeSetting theme;
    public static ModeSetting cfont;
    public BooleanSetting watermark;
    public ModeSetting displayMode;
    public ModeSetting waterMode;
    public ColorSetting waterColor;
    public BooleanSetting arraylist;
    public BooleanSetting armorStatus;
    public NumberSetting colorSpeed;
    public NumberSetting fadeSpeed;
    public ModeSetting colormode;
    public NumberSetting spacing;
    public ColorSetting arrayColor;
    public ColorSetting secondColor;
    public BooleanSetting background;
    public NumberSetting transparency;
    public static BooleanSetting notif;
    public static BooleanSetting potion;
    public BooleanSetting bps;
    public BooleanSetting line;
    public BooleanSetting arraybar;
    public BooleanSetting info;
    public BooleanSetting displayHealth;
    public BooleanSetting displayCompass;
    public BooleanSetting displayStatistics;
    public ModeSetting sortingMode;
    public static ModeSetting arrayPos;
    public static BooleanSetting scoreboard;
    public static NumberSetting scoreboardX;
    public static NumberSetting scoreboardY;
    public NumberSetting yadd;
    private List<Module> moduleCache;
    private MinecraftFontRenderer fontRenderer;
    private int width;
    private final DecimalFormat decimalFormat;
    private static final int MODULE_SPACING = 9;
    int y;
    boolean topArrayList;
    private TimerUtil timer;
    private TimerUtil hasUnLaggedShit;
    private ArrayList<Double> speedList;
    private float lastTick;
    private double lastSpeed;
    double lastDist;
    double xDist;
    double zDist;
    int wins;
    int lost;
    public static int kills;
    int flags;
    private long lastMS;
    private float hue;
    
    public boolean isModeSelected() {
        return this.theme.is("Normal");
    }
    
    public boolean isMode2Selected() {
        return this.colormode.is("Blend");
    }
    
    public boolean isMode3Selected() {
        return this.theme.is("Exhibition");
    }
    
    public Overlay() {
        super("Overlay", 0, "Ingame HUD", Category.RENDER);
        this.theme = new ModeSetting("Theme", "Normal", new String[] { "Normal" });
        this.watermark = new BooleanSetting("Watermark", true);
        this.displayMode = new ModeSetting("Watermark Mode", "LABEL", this::isModeSelected, new String[] { "ONETAP", "Minesense", "Neverlose", "LABEL", "Sixsense" });
        this.waterMode = new ModeSetting("Watermark Color Mode", "Fade", this::isModeSelected, new String[] { "Fade", "Rainbow", "Static" });
        this.waterColor = new ColorSetting("Watermark Color", new Color(125, 0, 235), this::isModeSelected);
        this.arraylist = new BooleanSetting("ArrayList", true);
        this.armorStatus = new BooleanSetting("Armor Status", true);
        this.colorSpeed = new NumberSetting("Speed", 15.0, this::isMode3Selected, 1.0, 25.0, 1.0);
        this.fadeSpeed = new NumberSetting("Fade Speed", 1.0, this::isModeSelected, 0.1, 5.0, 0.1);
        this.colormode = new ModeSetting("Color Mode", "Blend", this::isModeSelected, new String[] { "Rainbow", "Blend", "Astolfo", "Static", "Fade" });
        this.spacing = new NumberSetting("Spacing", 9.0, this::isModeSelected, 5.0, 15.0, 1.0);
        this.arrayColor = new ColorSetting("First Color", new Color(125, 0, 235), this::isModeSelected);
        this.secondColor = new ColorSetting("Second Color", new Color(0, 235, 4), this::isMode2Selected);
        this.background = new BooleanSetting("Background", true);
        this.transparency = new NumberSetting("Transparency", 3.0, 1.0, 10.0, 1.0);
        this.bps = new BooleanSetting("BPS", true);
        this.line = new BooleanSetting("Line", false);
        this.arraybar = new BooleanSetting("Left Bar", true);
        this.info = new BooleanSetting("Display Info", true);
        this.displayHealth = new BooleanSetting("Display Health", true);
        this.displayCompass = new BooleanSetting("Display Compass", false);
        this.displayStatistics = new BooleanSetting("Statistics", false);
        this.sortingMode = new ModeSetting("Sorting Mode", "Length", this::isModeSelected, new String[] { "Length", "Alphabetical", "Reversed", "Alpha Reversed" });
        this.yadd = new NumberSetting("Y Pos", -60.0, -60.0, 180.0, 1.0);
        this.decimalFormat = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ENGLISH));
        this.timer = new TimerUtil();
        this.hasUnLaggedShit = new TimerUtil();
        this.lastTick = -1.0f;
        this.lastSpeed = 0.01;
        this.wins = 0;
        this.lost = 0;
        this.flags = 0;
        this.hue = 0.0f;
        this.addSettings(this.theme, Overlay.cfont, this.watermark, this.displayMode, this.waterColor, this.arraylist, this.fadeSpeed, this.colorSpeed, this.colormode, this.spacing, this.arrayColor, this.secondColor, this.background, this.transparency, Overlay.notif, Overlay.potion, this.info, this.line, this.arraybar, this.displayHealth, this.displayCompass, this.displayStatistics, this.sortingMode, Overlay.arrayPos, Overlay.scoreboard, Overlay.scoreboardX, Overlay.scoreboardY);
        this.toggled = true;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.displayMode.getMode());
        if (e instanceof EventMotion) {
            this.xDist = Overlay.mc.thePlayer.posX - Overlay.mc.thePlayer.lastTickPosX;
            this.zDist = Overlay.mc.thePlayer.posZ - Overlay.mc.thePlayer.lastTickPosZ;
            this.lastDist = StrictMath.sqrt(this.xDist * this.xDist + this.zDist * this.zDist);
        }
        if (e instanceof EventUpdate) {
            if (!Overlay.cfont.is("Default")) {
                if (this.sortingMode.is("Length")) {
                    this.moduleCache.sort(SortingMode.LENGTH.getSorter());
                }
                if (this.sortingMode.is("Alphabetical")) {
                    this.moduleCache.sort(SortingMode.ALPHABETICAL.getSorter());
                }
                if (this.sortingMode.is("Reversed")) {
                    this.moduleCache.sort(SortingMode.LENGTH.getSorter().reversed());
                }
                if (this.sortingMode.is("Alpha Reversed")) {
                    this.moduleCache.sort(SortingMode.ALPHABETICAL.getSorter().reversed());
                }
            }
            else {
                if (this.sortingMode.is("Length")) {
                    this.moduleCache.sort(SortingMode.DEFLENGTH.getSorter());
                }
                if (this.sortingMode.is("Alphabetical")) {
                    this.moduleCache.sort(SortingMode.DEFALPHA.getSorter());
                }
                if (this.sortingMode.is("Reversed")) {
                    this.moduleCache.sort(SortingMode.DEFLENGTH.getSorter().reversed());
                }
                if (this.sortingMode.is("Alpha Reversed")) {
                    this.moduleCache.sort(SortingMode.DEFALPHA.getSorter().reversed());
                }
            }
        }
        if (e instanceof EventRender2D) {
            final EventRender2D event = (EventRender2D)e;
            final ScaledResolution sr = new ScaledResolution(Overlay.mc);
            int rainbowTick = 0;
            final Color rainbow2 = new Color(Color.HSBtoRGB((float)(Overlay.mc.thePlayer.ticksExisted / 50.0 + Math.sin(rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
            if (Overlay.arrayPos.is("Top")) {
                this.topArrayList = true;
            }
            else {
                this.topArrayList = false;
            }
            this.fontRenderer = Resolute.getFontRenderer();
            final int screenX = sr.getScaledWidth();
            final int screenY = sr.getScaledHeight();
            if (this.armorStatus.isEnabled()) {
                this.renderArmor(event);
            }
            final float speed = (float)this.fadeSpeed.getValue();
            final long ms = (long)(speed * 1000.0f);
            final float darkFactor = 0.48999998f;
            long currentMillis = -1L;
            final String inf = "Private Build: " + Resolute.build + " : UUID: " + Resolute.instance.uuid + " : " + Authentication.username;
            if (this.displayHealth.isEnabled()) {
                if (Overlay.mc.thePlayer.getHealth() >= 0.0f && Overlay.mc.thePlayer.getHealth() < 10.0f) {
                    this.width = 3;
                }
                if (Overlay.mc.thePlayer.getHealth() >= 10.0f && Overlay.mc.thePlayer.getHealth() < 100.0f) {
                    this.width = 3;
                }
                final float health = Overlay.mc.thePlayer.getHealth();
                final float absorptionHealth = Overlay.mc.thePlayer.getAbsorptionAmount();
                final String absorp = (absorptionHealth <= 0.0f) ? "" : ("§e" + this.decimalFormat.format(absorptionHealth / 2.0f) + "§6\u2764");
                final String string = this.decimalFormat.format(health / 2.0f) + "§c\u2764 " + absorp;
                final int x = new ScaledResolution(Overlay.mc).getScaledWidth() / 2 - this.width;
                final int y = new ScaledResolution(Overlay.mc).getScaledHeight() / 2 - 25;
                Overlay.mc.fontRendererObj.drawString(string, (absorptionHealth > 0.0f) ? (x - 15.5f) : (x - 3.5f), (float)y, this.getHealthColor(), true);
                GL11.glPushAttrib(1048575);
                GL11.glPushMatrix();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                Overlay.mc.getTextureManager().bindTexture(Gui.icons);
                for (float i = 0.0f; i < Overlay.mc.thePlayer.getMaxHealth() / 2.0f; ++i) {
                    Gui.drawTexturedModalRect2(sr.getScaledWidth() / 2.0f - Overlay.mc.thePlayer.getMaxHealth() / 2.5f * 10.0f / 2.0f + i * 8.0f, sr.getScaledHeight() / 2.0f - 35.0f, 16, 0, 9, 9);
                }
                for (float i = 0.0f; i < Overlay.mc.thePlayer.getHealth() / 2.0f; ++i) {
                    Gui.drawTexturedModalRect2(sr.getScaledWidth() / 2.0f - Overlay.mc.thePlayer.getMaxHealth() / 2.5f * 10.0f / 2.0f + i * 8.0f, sr.getScaledHeight() / 2.0f - 35.0f, 52, 0, 9, 9);
                }
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            if (this.displayCompass.isEnabled()) {
                final CompassUtil cpass = new CompassUtil(325.0f, 325.0f, 1.0f, 2, true);
                final ScaledResolution sc = new ScaledResolution(Overlay.mc);
                cpass.draw(sc);
            }
            if (this.displayStatistics.isEnabled()) {
                Gui.drawRect(sr.getScaledWidth() / 20 - 40, sr.getScaledHeight() / 5 + this.yadd.getValue(), sr.getScaledWidth() / 8 + 30, sr.getScaledHeight() / 3.5 + this.yadd.getValue() - 5.0, -1879048192);
                Gui.drawRect(sr.getScaledWidth() / 20 - 40, sr.getScaledHeight() / 2 + this.yadd.getValue() - 140.0, sr.getScaledWidth() / 8 + 30, sr.getScaledHeight() / 2 + this.yadd.getValue() - 138.5, -1871942548);
                Gui.drawRect(sr.getScaledWidth() / 20 - 40, sr.getScaledHeight() / 5 + this.yadd.getValue() - 0.0, sr.getScaledWidth() / 8 + 30, sr.getScaledHeight() / 5 + this.yadd.getValue() - 1.0, -16716033);
                FontUtil.robo.drawString("Session Information", sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 1.0f + this.yadd.getValue()), -1);
                FontUtil.robo.drawString("Session Time: " + this.formatTime(Resolute.sessionTime.elapsed()), sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 17.0f + this.yadd.getValue()), -1);
                FontUtil.robo.drawString("Kills: " + Overlay.kills, sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 28.0f + this.yadd.getValue()), -1);
            }
            if (this.info.isEnabled()) {
                FontUtil.roboSmall.drawStringWithShadow(inf, (float)(screenX - 1 - FontUtil.roboSmall.getStringWidth(inf)), (float)(screenY - ((Overlay.mc.currentScreen instanceof GuiChat) ? 24 : 11)), -3948095);
            }
            if (this.bps.isEnabled()) {
                FontUtil.roboSmall.drawStringWithShadow(String.format("%.2f blocks/s", this.lastDist * 20.0 * Overlay.mc.timer.timerSpeed), 2.0, (float)(screenY - ((Overlay.mc.currentScreen instanceof GuiChat) ? 24 : 11)), -1);
            }
            if (Overlay.potion.isEnabled()) {
                int potionY = 11;
                for (final PotionEffect effect : Overlay.mc.thePlayer.getActivePotionEffects()) {
                    final Potion potion = Potion.potionTypes[effect.getPotionID()];
                    final String effectName = I18n.format(potion.getName(), new Object[0]) + " " + (effect.getAmplifier() + 1) + " §7" + Potion.getDurationString(effect);
                    FontUtil.roboSmall.drawStringWithShadow(effectName, (float)(screenX - 2 - FontUtil.roboSmall.getStringWidth(effectName)), screenY - potionY - (this.info.isEnabled() ? 11 : 0) - (float)((Overlay.mc.currentScreen instanceof GuiChat) ? 13 : 0), potion.getLiquidColor());
                    potionY += FontUtil.roboSmall.getHeight();
                }
            }
            if (this.timer.hasElapsed(500L)) {
                if (this.timer.hasElapsed(150L)) {
                    RenderUtils.drawImage(new ResourceLocation("resolute/lag2.png"), sr.getScaledWidth() / 2 - 20, sr.getScaledHeight() / 2 - 65, 40, 40);
                    this.hasUnLaggedShit.reset();
                }
                RenderUtils.drawOutlinedString("§lLag Detected", sr.getScaledWidth() / 2.0f - Overlay.mc.fontRendererObj.getStringWidth("§lLag Detected") / 2.0f - 3.0f, sr.getScaledHeight() / 2.0f - 20.0f, new Color(255, 127, 0).getRGB(), new Color(0, 0, 0).getRGB());
            }
            else if (!this.hasUnLaggedShit.hasElapsed(200L)) {
                RenderUtils.drawImage(new ResourceLocation("resolute/lag.png"), sr.getScaledWidth() / 2 - 20, sr.getScaledHeight() / 2 - 65, 40, 40);
            }
            if (this.watermark.isEnabled()) {
                currentMillis = System.currentTimeMillis();
                final String text = Clientname.nameofwatermark;
                final float posX = 5.0f;
                if (this.theme.is("Normal")) {
                    if (this.displayMode.is("Minesense")) {
                        final char[] words2 = Clientname.nameofwatermark.toCharArray();
                        final FontRenderer fontRendererObj = Overlay.mc.fontRendererObj;
                        final StringBuilder append = new StringBuilder().append(words2[0]).append("§f").append(Clientname.nameofwatermark.substring(1, Clientname.nameofwatermark.length())).append(" §7[§f1.8.x§7] §7[§f");
                        final Minecraft mc = Overlay.mc;
                        fontRendererObj.drawStringWithShadow(append.append(Minecraft.getDebugFPS()).append("§f FPS§7]").toString(), 1.0f, 2.0f, this.waterColor.getColor());
                    }
                    else if (this.displayMode.is("ONETAP")) {
                        final Date date = Calendar.getInstance().getTime();
                        final DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                        final String strDate = dateFormat.format(date);
                        final String server = Overlay.mc.isSingleplayer() ? "local server" : Overlay.mc.getCurrentServerData().serverIP.toLowerCase();
                        final String text2 = "resolute.pub | " + Minecraft.getDebugFPS() + " fps | movement flags : 0 | " + Authentication.username + " | " + server + " | " + strDate;
                        final float width = (float)(FontUtil.tahomaSmall.getStringWidth(text2) + 6.0);
                        RenderUtils.drawRect(2.0, 2.0, width, 3.0, this.waterColor.getColor());
                        RenderUtils.drawRect(2.0, 3.0, width, 13.0, new Color(60, 60, 60).getRGB());
                        FontUtil.tahomaSmall.drawString(text2, 4.0, 5.5f, -1);
                    }
                    else if (this.displayMode.is("Sixsense")) {
                        final String server2 = Overlay.mc.isSingleplayer() ? "Singleplayer" : Overlay.mc.getCurrentServerData().serverIP.toLowerCase();
                        final String user = Authentication.username;
                        final StringBuilder append2 = new StringBuilder().append(text).append(" | ");
                        final Minecraft mc2 = Overlay.mc;
                        final String print = append2.append(Minecraft.getDebugFPS()).append(" FPS | ").append(user).append(" | ").append(server2).toString();
                        final float width2 = (float)(FontUtil.tahomaSmall.getStringWidth(print) + 8.0);
                        final int height = 20;
                        final int posX2 = 2;
                        final int posY1 = 2;
                        Gui.drawRect(posX2, posY1, posX2 + width2 + 2.0f, posY1 + height, new Color(5, 5, 5, 255).getRGB());
                        RenderUtils.drawBorderedRect(posX2 + 0.5, posY1 + 0.5, posX2 + width2 + 1.5, posY1 + height - 0.5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                        RenderUtils.drawBorderedRect(posX2 + 2, posY1 + 2, posX2 + width2, posY1 + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                        Gui.drawRect(posX2 + 2.5, posY1 + 2.5, posX2 + width2 - 0.5, posY1 + 4.5, new Color(9, 9, 9, 255).getRGB());
                        RenderUtils.drawGradientSideways(4.0, posY1 + 3, 4.0f + width2 / 3.0f, posY1 + 4, new Color(81, 149, 219, 255).getRGB(), new Color(180, 49, 218, 255).getRGB());
                        RenderUtils.drawGradientSideways(4.0f + width2 / 3.0f, posY1 + 3, 4.0f + width2 / 3.0f * 2.0f, posY1 + 4, new Color(180, 49, 218, 255).getRGB(), new Color(236, 93, 128, 255).getRGB());
                        RenderUtils.drawGradientSideways(4.0f + width2 / 3.0f * 2.0f, posY1 + 3, width2 / 3.0f * 3.0f + 1.0f, posY1 + 4, new Color(236, 93, 128, 255).getRGB(), new Color(167, 171, 90, 255).getRGB());
                        FontUtil.tahomaSmall.drawString(print, 7.5, 10.0f, Color.white.getRGB());
                    }
                    else if (this.displayMode.is("Neverlose")) {
                        final String watermark = " | " + Authentication.username + " | " + (Overlay.mc.isSingleplayer() ? "Singleplayer" : Overlay.mc.getCurrentServerData().serverIP) + " | FPS " + Minecraft.getDebugFPS();
                        Gui.drawRect(0.0, 0.0, FontUtil.neverlose.getStringWidth(text.toUpperCase()) + FontUtil.tahomaSmall.getStringWidth(watermark) + 5.0, 12.5, new Color(10, 10, 10, 255).getRGB());
                        FontUtil.neverlose.drawStringWithShadow(text.toUpperCase(), 1.0, 4.0f, -1);
                        FontUtil.tahomaSmall.drawStringWithShadow(watermark, FontUtil.neverlose.getStringWidth(text.toUpperCase()) + 2.0, 5.0f, Color.GRAY.brighter().getRGB());
                    }
                    else if (this.displayMode.is("LABEL")) {
                        final char[] words2 = Clientname.nameofwatermark.toCharArray();
                        final Date date2 = new Date();
                        final String time = date2.getHours() + ":" + date2.getMinutes() + ":" + date2.getSeconds();
                        FontUtil.roboSmall.drawStringWithShadow(words2[0] + "§f" + Clientname.nameofwatermark.substring(1) + "§f (" + time + ")", 1.0, 2.0f, this.waterColor.getColor());
                    }
                }
                else if (this.theme.is("Flux")) {
                    FontUtil.c22.drawStringWithShadow(text, 2.0, 5.0f, rainbow2.getRGB());
                }
                else if (this.theme.is("Exhibition")) {
                    final char[] words2 = Clientname.nameofwatermark.toCharArray();
                    final FontRenderer fontRendererObj2 = Overlay.mc.fontRendererObj;
                    final StringBuilder append3 = new StringBuilder().append(words2[0]).append("§f").append(Clientname.nameofwatermark.substring(1)).append(" §7[§f1.8.x§7] §7[§f");
                    final Minecraft mc3 = Overlay.mc;
                    fontRendererObj2.drawStringWithShadow(append3.append(Minecraft.getDebugFPS()).append("§f FPS§7]").toString(), 1.0f, 2.0f, Color.getHSBColor(this.hue / 255.0f, 0.55f, 0.9f).getRGB());
                }
            }
            if (this.arraylist.isEnabled()) {
                if (this.theme.is("Normal")) {
                    if (currentMillis == -1L) {
                        currentMillis = System.currentTimeMillis();
                    }
                    final int arrayListColor = this.arrayColor.getColor();
                    final int sArrayListColor = this.secondColor.getColor();
                    if (this.moduleCache == null) {
                        if (!Overlay.cfont.is("Default")) {
                            this.updateModulePositions(RenderUtils.getScaledResolution());
                        }
                        else {
                            this.defupdateModulePositions(RenderUtils.getScaledResolution());
                        }
                    }
                    final int moduleSpacing = (int)this.spacing.getValue();
                    final int heightOffset = (int)this.spacing.getValue();
                    if (Overlay.arrayPos.is("Top")) {
                        this.y = 2;
                    }
                    if (Overlay.arrayPos.is("Left")) {
                        this.y = ((this.displayMode.is("ONETAP") || this.displayMode.is("Minesense") || this.displayMode.is("Neverlose")) ? 14 : 24);
                    }
                    if (Overlay.arrayPos.is("Bottom")) {
                        this.y = screenY - 9;
                    }
                    int j = 0;
                    final boolean left = true;
                    for (final Module module : this.moduleCache) {
                        final Translate translate = module.getTranslate();
                        final String name = module.getDisplayName();
                        final float x2 = left ? 1.0f : ((float)((float)sr.getScaledWidth() - this.fontRenderer.getStringWidth(module.getDisplayName()) - 1.0));
                        if (module.isEnabled() && !module.isHidden()) {
                            float moduleWidth;
                            if (!Overlay.cfont.is("Default")) {
                                moduleWidth = (float)this.fontRenderer.getStringWidth(name);
                            }
                            else {
                                moduleWidth = (float)Overlay.mc.fontRendererObj.getStringWidth(name);
                            }
                            if (!Overlay.arrayPos.is("Left")) {
                                translate.animate2(screenX - moduleWidth - (this.line.isEnabled() ? 2 : 1), this.y);
                            }
                            else {
                                translate.animate2(x2, (float)this.y);
                            }
                            if (Overlay.arrayPos.is("Top") || Overlay.arrayPos.is("Left")) {
                                this.y += moduleSpacing;
                            }
                            if (Overlay.arrayPos.is("Bottom")) {
                                this.y -= moduleSpacing;
                            }
                        }
                        else if (!Overlay.arrayPos.is("Left")) {
                            translate.interpolate((float)sr.getScaledWidth(), Overlay.arrayPos.is("Bottom") ? 700.0f : -20.0f, 1.0);
                        }
                        else {
                            translate.interpolate(-50.0f, -20.0f, 1.0);
                        }
                        final boolean shown = Overlay.arrayPos.is("Left") ? (translate.getX() != -50.0) : (translate.getX() < screenX);
                        if (shown) {
                            int wColor = -1;
                            final float offset = (currentMillis + j * 100) % ms / (ms / 2.0f);
                            if (this.colormode.is("Fade")) {
                                wColor = this.fadeBetween(arrayListColor, this.darker(arrayListColor, darkFactor), offset);
                            }
                            if (this.colormode.is("Blend")) {
                                wColor = this.fadeBetween(arrayListColor, sArrayListColor, offset);
                            }
                            if (this.colormode.is("Astolfo")) {
                                wColor = ColorUtils.astolfoColors(10, this.y);
                            }
                            if (this.colormode.is("Static")) {
                                wColor = arrayListColor;
                            }
                            if (this.colormode.is("Rainbow")) {
                                wColor = RenderUtils.getRainbow(3000, j);
                            }
                            if (this.background.isEnabled()) {
                                if (Overlay.arrayPos.is("Left")) {
                                    float moduleWidth;
                                    if (!Overlay.cfont.is("Default")) {
                                        moduleWidth = (float)this.fontRenderer.getStringWidth(name);
                                    }
                                    else {
                                        moduleWidth = (float)Overlay.mc.fontRendererObj.getStringWidth(name);
                                    }
                                    if (this.transparency.getValue() == 1.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, 520093696);
                                    }
                                    if (this.transparency.getValue() == 2.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, 788529152);
                                    }
                                    if (this.transparency.getValue() == 3.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, 1056964608);
                                    }
                                    if (this.transparency.getValue() == 4.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, 1325400064);
                                    }
                                    if (this.transparency.getValue() == 5.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, 1593835520);
                                    }
                                    if (this.transparency.getValue() == 6.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, 1862270976);
                                    }
                                    if (this.transparency.getValue() == 7.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, 2130706432);
                                    }
                                    if (this.transparency.getValue() == 8.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, -1895825408);
                                    }
                                    if (this.transparency.getValue() == 9.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, -1627389952);
                                    }
                                    if (this.transparency.getValue() == 10.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), translate.getX() + moduleWidth, translate.getY() + heightOffset, -16777216);
                                    }
                                }
                                else {
                                    if (this.transparency.getValue() == 1.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, 520093696);
                                    }
                                    if (this.transparency.getValue() == 2.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, 788529152);
                                    }
                                    if (this.transparency.getValue() == 3.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, 1056964608);
                                    }
                                    if (this.transparency.getValue() == 4.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, 1325400064);
                                    }
                                    if (this.transparency.getValue() == 5.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, 1593835520);
                                    }
                                    if (this.transparency.getValue() == 6.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, 1862270976);
                                    }
                                    if (this.transparency.getValue() == 7.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, 2130706432);
                                    }
                                    if (this.transparency.getValue() == 8.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, -1895825408);
                                    }
                                    if (this.transparency.getValue() == 9.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, -1627389952);
                                    }
                                    if (this.transparency.getValue() == 10.0) {
                                        Gui.drawRect(translate.getX() - 1.0, translate.getY() - (moduleSpacing - heightOffset), screenX, translate.getY() + heightOffset, -16777216);
                                    }
                                }
                            }
                            if (!Overlay.cfont.is("Default")) {
                                this.fontRenderer.drawStringWithShadow(name, (float)translate.getX(), (float)translate.getY() + 2.0f, wColor);
                            }
                            else {
                                Overlay.mc.fontRendererObj.drawStringWithShadow(name, (float)translate.getX(), (float)translate.getY(), wColor);
                            }
                            if (this.arraybar.isEnabled()) {
                                Gui.drawRect(translate.getX() - 2.0, translate.getY(), translate.getX() - 1.0, translate.getY() + moduleSpacing, wColor - 1);
                            }
                            if (!Overlay.cfont.is("Default")) {
                                final float moduleWidth = (float)this.fontRenderer.getStringWidth(name);
                            }
                            else {
                                final float moduleWidth = (float)Overlay.mc.fontRendererObj.getStringWidth(name);
                            }
                            if (this.line.isEnabled()) {
                                if (Overlay.arrayPos.is("Left")) {
                                    Gui.drawRect(translate.getX() - 1.0, translate.getY() - 1.0, translate.getX(), translate.getY() + moduleSpacing - 1.0, wColor - 1);
                                }
                                else {
                                    Gui.drawRect(screenX - 1, translate.getY() - 1.0, screenX, translate.getY() + moduleSpacing - 1.0, wColor - 1);
                                }
                            }
                            ++j;
                        }
                    }
                }
                else if (this.theme.is("Flux")) {
                    if (!Overlay.mc.gameSettings.showDebugInfo) {
                        final MinecraftFontRenderer fontRenderer = FontUtil.c16;
                        final ArrayList<Module> sorted = new ArrayList<Module>();
                        for (final Module m : Resolute.modules) {
                            sorted.add(m);
                        }
                        String string2;
                        final MinecraftFontRenderer minecraftFontRenderer;
                        final StringBuilder sb;
                        String string3;
                        final double n;
                        final StringBuilder sb2;
                        sorted.sort((o1, o2) -> {
                            new StringBuilder().append(o2.getName());
                            if (o2.suffix == null) {
                                string2 = "";
                            }
                            else {
                                string2 = " " + o2.suffix;
                            }
                            minecraftFontRenderer.getStringWidth(sb.append(string2).toString());
                            new StringBuilder().append(o1.getName());
                            if (o1.suffix == null) {
                                string3 = "";
                            }
                            else {
                                string3 = " " + o1.suffix;
                            }
                            return (int)(n - minecraftFontRenderer.getStringWidth(sb2.append(string3).toString()));
                        });
                        int y2 = 8;
                        rainbowTick = 0;
                        for (final Module k : sorted) {
                            final Color rainbow3 = new Color(Color.HSBtoRGB((float)(Overlay.mc.thePlayer.ticksExisted / 50.0 + Math.sin(rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
                            final String name2 = k.getName() + ((k.suffix == null) ? "" : (" §W" + k.suffix));
                            if (k.getXAnim() < fontRenderer.getStringWidth(name2) && k.isEnabled()) {
                                if (k.getXAnim() < fontRenderer.getStringWidth(name2) / 3.0) {
                                    k.setXAnim(k.getXAnim() + 1.0f);
                                }
                                else {
                                    k.setXAnim(k.getXAnim() + 1.0f);
                                }
                            }
                            if (k.getXAnim() > -1.0f && !k.isEnabled()) {
                                k.setXAnim(k.getXAnim() - 1.0f);
                            }
                            if (k.getXAnim() > fontRenderer.getStringWidth(name2) && k.isEnabled()) {
                                k.setXAnim((float)fontRenderer.getStringWidth(name2));
                            }
                            if (k.getYAnim() < y2) {
                                final Module module3 = k;
                                final float yAnim = k.getYAnim();
                                final float n2 = 180.0f;
                                final Minecraft mc4 = Overlay.mc;
                                module3.setYAnim(yAnim + n2 / Minecraft.getDebugFPS());
                            }
                            if (k.getYAnim() > y2) {
                                final Module module4 = k;
                                final float yAnim2 = k.getYAnim();
                                final float n3 = 180.0f;
                                final Minecraft mc5 = Overlay.mc;
                                module4.setYAnim(yAnim2 - n3 / Minecraft.getDebugFPS());
                            }
                            if (Math.abs(k.getYAnim() - y2) < 1.0f) {
                                k.setYAnim((float)y2);
                            }
                            if (k.isEnabled()) {
                                final float x3 = sr.getScaledWidth() - k.getXAnim();
                                if ((boolean)this.background.isEnabled()) {
                                    RenderUtils.drawRect(x3 - 8.0f, k.getYAnim(), sr.getScaledWidth(), k.getYAnim() + 10.0f, new Color(0, 0, 0, 150).getRGB());
                                    RenderUtils.drawRect(new ScaledResolution(Overlay.mc).getScaledWidth_double() - 2.0, k.getYAnim() - 2.0f, new ScaledResolution(Overlay.mc).getScaledWidth_double(), k.getYAnim() + 10.0f, rainbow3.getRGB());
                                }
                                fontRenderer.drawStringWithShadow(name2, x3 - 5.0f, k.getYAnim() + 2.0f, rainbow3.getRGB());
                                y2 += 10;
                            }
                            if (++rainbowTick > 50) {
                                rainbowTick = 0;
                            }
                            else {
                                rainbowTick += (int)0.1;
                            }
                        }
                    }
                }
                else if (this.theme.is("Exhibition")) {
                    final FontRenderer fr = Overlay.mc.fontRendererObj;
                    final boolean left2 = true;
                    int y3 = left2 ? 12 : 1;
                    final List<Module> modules = new CopyOnWriteArrayList<Module>();
                    for (final Module k : Resolute.modules) {
                        if (k.isEnabled() || k.translate.getX() != -50.0) {
                            modules.add(k);
                        }
                        if (!k.isEnabled() || k.isHidden()) {
                            k.translate.interpolate(left2 ? -50.0f : ((float)sr.getScaledWidth()), -20.0f, 1.0);
                        }
                    }
                    String text3;
                    final FontRenderer fontRenderer2;
                    modules.sort(Comparator.comparingDouble(o -> {
                        if (o.suffix != null) {
                            text3 = o.getName() + " " + o.suffix;
                        }
                        else {
                            text3 = o.getName();
                        }
                        return -MathUtils.getIncremental(fontRenderer2.getStringWidth(text3), 0.5);
                    }));
                    final Iterator var42 = modules.iterator();
                    this.hue += (float)(this.colorSpeed.getValue() / 5.0);
                    if (this.hue > 255.0f) {
                        this.hue = 0.0f;
                    }
                    float h = this.hue;
                    while (var42.hasNext()) {
                        final Module module2 = var42.next();
                        if (h > 255.0f) {
                            h = 0.0f;
                        }
                        final String suffix = (module2.suffix != null) ? (" §7" + module2.suffix) : "";
                        final float x4 = left2 ? 2.0f : (sr.getScaledWidth() - (float)fr.getStringWidth(module2.getName() + suffix) - 1.0f);
                        if (module2.isEnabled() && !module2.isHidden()) {
                            module2.translate.interpolate(x4, (float)y3, 1.0);
                        }
                        final Color color = Color.getHSBColor(h / 255.0f, 0.55f, 0.9f);
                        final int c = color.getRGB();
                        fr.drawStringWithShadow(module2.getName() + suffix, (float)module2.translate.getX(), (float)module2.translate.getY(), c);
                        if (module2.isEnabled() && !module2.isHidden()) {
                            h += 9.0f;
                            y3 += 9;
                        }
                    }
                }
            }
        }
        if (e instanceof EventPacket) {
            final EventPacket eventPacket = (EventPacket)e;
            final Packet<?> packet = eventPacket.getPacket();
            if (((EventPacket)e).getPacket() instanceof S08PacketPlayerPosLook) {
                ++this.flags;
            }
            if (Overlay.mc.thePlayer != null && Overlay.mc.thePlayer.ticksExisted >= 0 && packet instanceof S45PacketTitle) {
                if (((S45PacketTitle)packet).getMessage() == null) {
                    return;
                }
                final String message = ((S45PacketTitle)packet).getMessage().getUnformattedText();
                if (message.equals("VICTORY!")) {
                    ++this.wins;
                }
                if (message.equals("YOU DIED!") || message.equals("GAME END") || message.equals("You are now a spectator!")) {
                    ++this.lost;
                }
            }
            if (Overlay.mc.thePlayer != null && Overlay.mc.thePlayer.ticksExisted >= 0 && packet instanceof S02PacketChat) {
                final String look = "killed by " + Overlay.mc.thePlayer.getName();
                final String look2 = "slain by " + Overlay.mc.thePlayer.getName();
                final String look3 = "void while escaping " + Overlay.mc.thePlayer.getName();
                final String look4 = "was killed with magic while fighting " + Overlay.mc.thePlayer.getName();
                final String look5 = "couldn't fly while escaping " + Overlay.mc.thePlayer.getName();
                final String look6 = "fell to their death while escaping " + Overlay.mc.thePlayer.getName();
                final String look7 = "foi morto por " + Overlay.mc.thePlayer.getName();
                final String look8 = "fue asesinado por " + Overlay.mc.thePlayer.getName();
                final String look9 = "fue destrozado a manos de " + Overlay.mc.thePlayer.getName();
                final S02PacketChat s02PacketChat = (S02PacketChat)packet;
                final String cp21 = s02PacketChat.getChatComponent().getUnformattedText();
                if (cp21.contains(look) || cp21.contains(look2) || cp21.contains(look3) || cp21.contains(look4) || cp21.contains(look5) || cp21.contains(look6) || cp21.contains(look7) || cp21.contains(look8) || cp21.contains(look9)) {
                    ++Overlay.kills;
                }
                if ((cp21.contains(Overlay.mc.thePlayer.getName() + "killed by ") && cp21.contains("elimination")) || cp21.contains(Overlay.mc.thePlayer.getName() + " morreu sozinho") || cp21.contains(Overlay.mc.thePlayer.getName() + " foi morto por")) {
                    ++this.lost;
                }
                if (cp21.contains(Overlay.mc.thePlayer.getName() + " venceu a partida!")) {
                    ++this.wins;
                }
            }
        }
        if (e instanceof EventUpdate) {
            Overlay.mc.thePlayer.setLocationOfCape(new ResourceLocation("resolute/cape.png"));
        }
        if (e instanceof EventPacket) {
            final Packet<?> receive = ((EventPacket)e).getPacket();
            if (!(receive instanceof S02PacketChat)) {
                this.timer.reset();
            }
        }
    }
    
    private String formatTime(long time) {
        time /= 1000L;
        return String.format("%d:%02d", time / 60L, time % 60L);
    }
    
    private void renderArmor(final EventRender2D event) {
        final ScaledResolution sr = new ScaledResolution(Overlay.mc);
        final boolean currentItem = true;
        GL11.glPushMatrix();
        final List<ItemStack> stuff = new ArrayList<ItemStack>();
        final boolean onwater = Overlay.mc.thePlayer.isEntityAlive() && Overlay.mc.thePlayer.isInsideOfMaterial(Material.water);
        int split = -3;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armer = Overlay.mc.thePlayer.inventory.armorInventory[index];
            if (armer != null) {
                stuff.add(armer);
            }
        }
        if (Overlay.mc.thePlayer.getCurrentEquippedItem() != null) {
            stuff.add(Overlay.mc.thePlayer.getCurrentEquippedItem());
        }
        for (final ItemStack errything : stuff) {
            if (Overlay.mc.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            Overlay.mc.getRenderItem().zLevel = -150.0f;
            Overlay.mc.getRenderItem().renderItemAndEffectIntoGUI(errything, split + sr.getScaledWidth() / 2 - 4, sr.getScaledHeight() - (onwater ? 65 : 55));
            Overlay.mc.getRenderItem().renderItemOverlays(Overlay.mc.fontRendererObj, errything, split + sr.getScaledWidth() / 2 - 4, sr.getScaledHeight() - (onwater ? 65 : 55));
            Overlay.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            errything.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }
    
    public static boolean checkPing(final EntityPlayer entity) {
        final NetworkPlayerInfo info = Overlay.mc.getNetHandler().getPlayerInfo(entity.getUniqueID());
        return info != null && info.getResponseTime() == 1;
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
    
    private void updateModulePositions(final ScaledResolution scaledResolution) {
        if (this.moduleCache == null) {
            this.setupModuleCache();
        }
        int y = 1;
        for (final Module module : this.moduleCache) {
            if (module.isEnabled()) {
                module.getTranslate().setX((float)(scaledResolution.getScaledWidth() - this.fontRenderer.getStringWidth(module.getDisplayName()) - 2.0));
            }
            else {
                module.getTranslate().setX((float)scaledResolution.getScaledWidth());
            }
            module.getTranslate().setY((float)y);
            if (module.isEnabled()) {
                y += (int)this.spacing.getValue();
            }
        }
    }
    
    private void defupdateModulePositions(final ScaledResolution scaledResolution) {
        if (this.moduleCache == null) {
            this.setupModuleCache();
        }
        int y = 1;
        for (final Module module : this.moduleCache) {
            if (module.isEnabled()) {
                module.getTranslate().setX((float)(scaledResolution.getScaledWidth() - Overlay.mc.fontRendererObj.getStringWidth(module.getDisplayName()) - 2));
            }
            else {
                module.getTranslate().setX((float)scaledResolution.getScaledWidth());
            }
            module.getTranslate().setY((float)y);
            if (module.isEnabled()) {
                y += (int)this.spacing.getValue();
            }
        }
    }
    
    private void setupModuleCache() {
        this.moduleCache = new ArrayList<Module>(Resolute.modules);
    }
    
    private int getHealthColor() {
        if (Overlay.mc.thePlayer.getHealth() <= 2.0f) {
            return new Color(255, 0, 0).getRGB();
        }
        if (Overlay.mc.thePlayer.getHealth() <= 6.0f) {
            return new Color(255, 110, 0).getRGB();
        }
        if (Overlay.mc.thePlayer.getHealth() <= 8.0f) {
            return new Color(255, 182, 0).getRGB();
        }
        if (Overlay.mc.thePlayer.getHealth() <= 10.0f) {
            return new Color(255, 255, 0).getRGB();
        }
        if (Overlay.mc.thePlayer.getHealth() <= 13.0f) {
            return new Color(255, 255, 0).getRGB();
        }
        if (Overlay.mc.thePlayer.getHealth() <= 15.5f) {
            return new Color(182, 255, 0).getRGB();
        }
        if (Overlay.mc.thePlayer.getHealth() <= 18.0f) {
            return new Color(108, 255, 0).getRGB();
        }
        if (Overlay.mc.thePlayer.getHealth() <= 20.0f) {
            return new Color(0, 255, 0).getRGB();
        }
        return 0;
    }
    
    static {
        Overlay.cfont = new ModeSetting("Font", "Roboto", new String[] { "Default", "Client", "Moon", "Tahoma", "Roboto", "SF", "SF Large" });
        Overlay.notif = new BooleanSetting("Notifications", true);
        Overlay.potion = new BooleanSetting("Potion", true);
        Overlay.arrayPos = new ModeSetting("Array Position", "Top", new String[] { "Top", "Left", "Bottom" });
        Overlay.scoreboard = new BooleanSetting("Scoreboard", true);
        Overlay.scoreboardX = new NumberSetting("Scoreboard X", 2.0, -1080.0, 50.0, 1.0);
        Overlay.scoreboardY = new NumberSetting("Scoreboard Y", 170.0, -100.0, 300.0, 1.0);
        Overlay.kills = 0;
    }
    
    private enum SortingMode
    {
        LENGTH((ModuleComparator)new LengthComparator()), 
        ALPHABETICAL((ModuleComparator)new AlphabeticalComparator()), 
        DEFLENGTH((ModuleComparator)new DefLengthComparator()), 
        DEFALPHA((ModuleComparator)new DefAlphabeticalComparator());
        
        private final ModuleComparator sorter;
        
        private SortingMode(final ModuleComparator sorter) {
            this.sorter = sorter;
        }
        
        public ModuleComparator getSorter() {
            return this.sorter;
        }
    }
    
    private abstract static class ModuleComparator implements Comparator<Module>
    {
        protected MinecraftFontRenderer fontRenderer;
        
        private ModuleComparator() {
            this.fontRenderer = Resolute.getFontRenderer();
        }
        
        public MinecraftFontRenderer getFontRenderer() {
            return Resolute.getFontRenderer();
        }
        
        @Override
        public abstract int compare(final Module p0, final Module p1);
    }
    
    private static class LengthComparator extends ModuleComparator
    {
        @Override
        public int compare(final Module o1, final Module o2) {
            return (int)this.getFontRenderer().getStringWidth(o2.getDisplayName()) - (int)this.getFontRenderer().getStringWidth(o1.getDisplayName());
        }
    }
    
    private static class AlphabeticalComparator extends ModuleComparator
    {
        @Override
        public int compare(final Module o1, final Module o2) {
            final String n = o1.getDisplayName();
            final String n2 = o2.getDisplayName();
            if (n.equals(n2)) {
                return 0;
            }
            if (n.length() == 0 || n2.length() == 0) {
                return 0;
            }
            return n.charAt(0) - n2.charAt(0);
        }
    }
    
    private static class DefLengthComparator extends ModuleComparator
    {
        @Override
        public int compare(final Module o1, final Module o2) {
            return Module.mc.fontRendererObj.getStringWidth(o2.getDisplayName()) - Module.mc.fontRendererObj.getStringWidth(o1.getDisplayName());
        }
    }
    
    private static class DefAlphabeticalComparator extends ModuleComparator
    {
        @Override
        public int compare(final Module o1, final Module o2) {
            final String n = o1.getDisplayName();
            final String n2 = o2.getDisplayName();
            if (n.equals(n2)) {
                return 0;
            }
            if (n.length() == 0 || n2.length() == 0) {
                return 0;
            }
            return n.charAt(0) - n2.charAt(0);
        }
    }
}
