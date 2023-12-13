// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.ui;

import java.awt.Font;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.client.gui.FontRenderer;
import java.text.DateFormat;
import net.augustus.font.testfontbase.FontUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.util.StringUtils;
import net.minecraft.entity.EntityLivingBase;
import net.augustus.utils.GuiUtils;
import net.augustus.modules.combat.KillAura;
import net.minecraft.entity.player.EntityPlayer;
import net.augustus.utils.RenderUtil;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.lwjgl.opengl.GL11;
import net.augustus.utils.PlayerUtil;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.augustus.Augustus;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import net.augustus.modules.Module;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.notify.rise5.Notification;
import net.augustus.notify.rise5.NotificationManager;
import net.augustus.notify.xenza.NotificationsManager;
import net.augustus.utils.ResourceUtil;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import net.augustus.font.UnicodeFontRenderer;
import net.minecraft.util.ResourceLocation;
import net.augustus.font.CustomFontUtil;
import net.augustus.utils.RainbowUtil;
import net.augustus.utils.interfaces.MC;
import net.augustus.utils.interfaces.MM;
import net.minecraft.client.gui.GuiIngame;

public class GuiIngameHook extends GuiIngame implements MM, MC
{
    private boolean leftSide;
    private int lastWidth;
    private float yAdd;
    public static float lastArrayListY;
    private final RainbowUtil rainbowUtil;
    public static CustomFontUtil customFont;
    private ResourceLocation augustusResourceLocation;
    private ResourceLocation amogusResourceLocation;
    private static UnicodeFontRenderer ryuFontRenderer;
    private static UnicodeFontRenderer oldFontRenderer;
    private static UnicodeFontRenderer vegaFontRenderer;
    public float alpha;
    public float rectSize;
    public float saturationDelay;
    public boolean underLine;
    private int yOff;
    private static float animated;
    public static Color color;
    
    public static Color getChroma() {
        final Color color = new Color(Color.HSBtoRGB((System.nanoTime() - 0.0f) / 2.0f / 1.0E9f, 1.0f, 1.0f));
        return new Color(color.getRed() / 255.0f * 1.0f, color.getGreen() / 255.0f * 1.0f, color.getBlue() / 255.0f * 1.0f, color.getAlpha() / 255.0f);
    }
    
    public GuiIngameHook(final Minecraft client) {
        super(client);
        this.leftSide = false;
        this.lastWidth = 0;
        this.yAdd = 0.0f;
        this.rainbowUtil = new RainbowUtil();
        this.augustusResourceLocation = null;
        this.amogusResourceLocation = null;
        this.alpha = 4.0f;
        this.rectSize = 1.0f;
        this.saturationDelay = 1.0f;
        this.underLine = true;
        this.augustusResourceLocation = ResourceUtil.loadResourceLocation("pictures/augustus.png", "augustus");
        this.amogusResourceLocation = ResourceUtil.loadResourceLocation("pictures/amogus.png", "amogus");
        GuiIngameHook.customFont = new CustomFontUtil("Verdana", 16);
    }
    
    @Override
    public void renderGameOverlay(final float partialTicks) {
        super.renderGameOverlay(partialTicks);
        if (GuiIngameHook.mm.notifications.isToggled()) {
            final String selected = GuiIngameHook.mm.notifications.mode.getSelected();
            switch (selected) {
                case "Xenza": {
                    NotificationsManager.renderNotifications();
                    NotificationsManager.update();
                    break;
                }
                case "Rise5": {
                    if (!NotificationManager.notifications.isEmpty()) {
                        if (NotificationManager.notifications.getFirst().getEnd() > System.currentTimeMillis()) {
                            NotificationManager.notifications.getFirst().y = (float)(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 50);
                            NotificationManager.notifications.getFirst().render();
                        }
                        else {
                            NotificationManager.notifications.removeFirst();
                        }
                    }
                    if (NotificationManager.notifications.size() > 0) {
                        int i = 0;
                        try {
                            for (final Notification notification : NotificationManager.notifications) {
                                if (i == 0) {
                                    ++i;
                                }
                                else {
                                    notification.y = (float)(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 18 - 35 * (i + 1));
                                    notification.render();
                                    ++i;
                                }
                            }
                        }
                        catch (final ConcurrentModificationException ex) {}
                        break;
                    }
                    break;
                }
            }
        }
        if (GuiIngameHook.mm.arrayList.isToggled()) {
            final String selected2 = GuiIngameHook.mm.arrayList.mode.getSelected();
            switch (selected2) {
                case "Default": {
                    GuiIngameHook.mm.arrayList.renderArrayList();
                    break;
                }
                case "Vega": {
                    byte b1 = 0;
                    if (GuiIngameHook.mm.arrayList.sideOption.getSelected().equals("Left")) {
                        final String selected3 = GuiIngameHook.mm.hud.mode.getSelected();
                        switch (selected3) {
                            case "Ryu": {
                                b1 = (byte)(GuiIngameHook.vegaFontRenderer.getStringHeight("ALLAH") + 5.0f);
                                break;
                            }
                            case "Old": {
                                b1 = 48;
                                break;
                            }
                            case "Other": {
                                b1 = (byte)((GuiIngameHook.vegaFontRenderer.getStringHeight("ALLAH") * 1.75f + 4.0f + 1.0f) * 1.5);
                                break;
                            }
                            case "Basic": {
                                b1 = (byte)(GuiIngameHook.vegaFontRenderer.getStringHeight("ALLAH") + 1);
                                break;
                            }
                        }
                    }
                    final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
                    final ArrayList<Module> arrayList = GuiIngameHook.mm.getModules().stream().filter(Module::isToggled).sorted(Comparator.comparingDouble(paramModule -> -GuiIngameHook.vegaFontRenderer.getStringWidth(paramModule.getName()))).collect((Collector<? super Object, ?, ArrayList<Module>>)Collectors.toList());
                    for (byte b2 = 0; b2 < arrayList.size(); ++b2) {
                        final Module module1 = arrayList.get(b2);
                        final Module module2 = arrayList.get(b2 + ((b2 != arrayList.size() - 1) ? 1 : 0));
                        final int j = Augustus.getInstance().getColorUtil().getSaturationFadeColor(GuiIngameHook.color, (int)(b1 * this.saturationDelay), 100.0f).getRGB();
                        Gui.drawRect(sr.getScaledWidth() - GuiIngameHook.vegaFontRenderer.getStringWidth(module1.getName()) - 5, b1 * 13, sr.getScaledWidth(), b1 * 13 + 13, new Color(0.0f, 0.0f, 0.0f, this.alpha / 10.0f).getRGB());
                        Gui.drawRect(sr.getScaledWidth() - GuiIngameHook.vegaFontRenderer.getStringWidth(module1.getName()) - 6, this.underLine ? (b1 * 13 + 1) : (b1 * 13), sr.getScaledWidth() - GuiIngameHook.vegaFontRenderer.getStringWidth(module1.getName()) - 5, b1 * 13 + 13, j);
                        Gui.drawRect(sr.getScaledWidth() - GuiIngameHook.vegaFontRenderer.getStringWidth(module1.getName()) - 6, b1 * 13 + 13, (module2 != module1) ? (sr.getScaledWidth() - GuiIngameHook.vegaFontRenderer.getStringWidth(module2.getName()) - 5) : sr.getScaledWidth(), b1 * 13 + 14, j);
                        GuiIngameHook.vegaFontRenderer.drawString(module1.getName(), (float)(sr.getScaledWidth() - GuiIngameHook.vegaFontRenderer.getStringWidth(module1.getName()) - 1), (float)(1 + b1 * 13), j);
                        ++b1;
                    }
                    break;
                }
            }
        }
        if (GuiIngameHook.mm.hud.isToggled()) {
            if (GuiIngameHook.mm.hud.targethud.getBoolean()) {
                drawTargetHud();
            }
            this.hud();
        }
        else {
            this.yAdd = 0.0f;
        }
        if (GuiIngameHook.mm.hud.isToggled() && GuiIngameHook.mm.hud.armor.getBoolean()) {
            this.drawArmorHud();
        }
    }
    
    @Override
    public void updateTick() {
        super.updateTick();
        if (GuiIngameHook.mm.arrayList.isToggled()) {}
    }
    
    private void hud() {
        final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
        final String selected;
        final String var2 = selected = GuiIngameHook.mm.hud.mode.getSelected();
        int n = -1;
        switch (selected.hashCode()) {
            case 79367: {
                if (selected.equals("Old")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 82670: {
                if (selected.equals("Ryu")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 76517104: {
                if (selected.equals("Other")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 63955982: {
                if (selected.equals("Basic")) {
                    n = 3;
                    break;
                }
                break;
            }
        }
        Label_0386: {
            switch (n) {
                case 0: {
                    final String selected2;
                    final String var3 = selected2 = GuiIngameHook.mm.hud.side.getSelected();
                    switch (selected2) {
                        case "Left": {
                            GuiIngameHook.oldFontRenderer.drawString("X", 5.0f, 5.0f, getChroma().getRGB());
                            GuiIngameHook.oldFontRenderer.drawString("ENZA", 5.0f + GuiIngameHook.oldFontRenderer.getStringWidth("X"), 5.0f, -1);
                            break Label_0386;
                        }
                        case "Right": {
                            GuiIngameHook.oldFontRenderer.drawString("X", sr.getScaledWidth() - 5.0f - GuiIngameHook.oldFontRenderer.getStringWidth("XENZA"), 5.0f, getChroma().getRGB());
                            GuiIngameHook.oldFontRenderer.drawString("ENZA", sr.getScaledWidth() - 5.0f - GuiIngameHook.oldFontRenderer.getStringWidth("ENZA"), 5.0f, -1);
                            break Label_0386;
                        }
                        default: {
                            break Label_0386;
                        }
                    }
                    break;
                }
                case 1: {
                    final String name = "Ryu  ";
                    float width = (float)this.getFontRenderer().getStringWidth(name);
                    this.yAdd = (float)(this.getFontRenderer().FONT_HEIGHT + 1);
                    final String selected3;
                    final String var4 = selected3 = GuiIngameHook.mm.hud.side.getSelected();
                    switch (selected3) {
                        case "Left": {
                            width = (float)GuiIngameHook.ryuFontRenderer.getStringWidth(name);
                            GuiIngameHook.ryuFontRenderer.drawStringWithShadow(name, 1.0f, 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB());
                            GuiIngameHook.ryuFontRenderer.drawStringWithShadow("# ", width, 1.0f, new Color(162, 162, 162, 200).getRGB());
                            GuiIngameHook.ryuFontRenderer.drawStringWithShadow(Minecraft.getDebugFPS() + "fps", width + GuiIngameHook.ryuFontRenderer.getStringWidth("# "), 1.0f, Color.white.getRGB());
                            break;
                        }
                        case "Right": {
                            PlayerUtil.sendChat("Ryu == Left");
                            GuiIngameHook.mm.hud.side.setString("Left");
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    GL11.glPushMatrix();
                    GL11.glScaled(1.5, 1.5, 0.0);
                    this.yAdd = (float)(int)((this.getFontRenderer().FONT_HEIGHT * 1.75f + 4.0f + 1.0f) * 1.5);
                    final String selected4;
                    final String var5 = selected4 = GuiIngameHook.mm.hud.side.getSelected();
                    switch (selected4) {
                        case "Left": {
                            if (GuiIngameHook.mm.hud.backGround.getBoolean()) {
                                Gui.drawRect(0, 0, (int)(this.getFontRenderer().getStringWidth("X") * 1.75f + this.getFontRenderer().getStringWidth("enza") + 4.0f), (int)(this.getFontRenderer().FONT_HEIGHT * 1.75f + 3.0f), GuiIngameHook.mm.hud.backGroundColor.getColor().getRGB());
                            }
                            GL11.glPushMatrix();
                            GL11.glScaled(1.75, 1.75, 0.0);
                            this.getFontRenderer().drawString("X", 1.0f, 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                            GL11.glScaled(1.0, 1.0, 0.0);
                            GL11.glPopMatrix();
                            this.getFontRenderer().drawString("enza", this.getFontRenderer().getStringWidth("X") * 1.75f + 2.0f, 2 + this.getFontRenderer().FONT_HEIGHT - 4.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                            break;
                        }
                        case "Right": {
                            if (GuiIngameHook.mm.hud.backGround.getBoolean()) {
                                Gui.drawRect((int)(sr.getScaledWidth() / 1.5f - (int)(this.getFontRenderer().getStringWidth("X") * 1.75f + this.getFontRenderer().getStringWidth("enza") + 4.0f)), 0, (int)(sr.getScaledWidth() / 1.5f) + 1, (int)(this.getFontRenderer().FONT_HEIGHT * 1.75f + 3.0f), GuiIngameHook.mm.hud.backGroundColor.getColor().getRGB());
                            }
                            GL11.glPushMatrix();
                            GL11.glScaled(1.75, 1.75, 0.0);
                            this.getFontRenderer().drawString("X", sr.getScaledWidth() / 1.5f / 1.75f - this.getFontRenderer().getStringWidth("X") - this.getFontRenderer().getStringWidth("enza") / 1.75f - 0.25f, 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                            GL11.glScaled(1.0, 1.0, 0.0);
                            GL11.glPopMatrix();
                            this.getFontRenderer().drawString("enza", sr.getScaledWidth() / 1.5f - 1.75f - this.getFontRenderer().getStringWidth("X") * 1.5f * 1.75f - this.getFontRenderer().getStringWidth("enza") / 1.5f + 4.0f, 2 + this.getFontRenderer().FONT_HEIGHT - 4.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                            break;
                        }
                    }
                    GL11.glScaled(1.0, 1.0, 0.0);
                    GL11.glPopMatrix();
                    break;
                }
                case 3: {
                    final GregorianCalendar cal = (GregorianCalendar)Calendar.getInstance();
                    final Date time = new Date();
                    cal.setTime(time);
                    final int h = (GuiIngameHook.mm.protector.isToggled() && GuiIngameHook.mm.protector.protectTime.getBoolean()) ? GuiIngameHook.mm.protector.getRandomHour() : cal.get(10);
                    String m = (cal.get(12) <= 9) ? ("0" + cal.get(12)) : String.valueOf(cal.get(12));
                    m = ((GuiIngameHook.mm.protector.isToggled() && GuiIngameHook.mm.protector.protectTime.getBoolean()) ? String.valueOf(GuiIngameHook.mm.protector.getRandomMinute()) : m);
                    String printTime;
                    if (cal.get(9) == 0) {
                        printTime = ((h < 9) ? (" (0" + h + ":" + m + " AM)") : (" (" + h + ":" + m + " AM)"));
                    }
                    else {
                        printTime = ((h < 9) ? (" (0" + h + ":" + m + " PM)") : (" (" + h + ":" + m + " PM)"));
                    }
                    final String name2 = Augustus.getInstance().getName() + " " + Augustus.getInstance().getVersion();
                    final float width2 = (float)this.getFontRenderer().getStringWidth(name2);
                    this.yAdd = (float)(this.getFontRenderer().FONT_HEIGHT + 1);
                    final String selected5;
                    final String var6 = selected5 = GuiIngameHook.mm.hud.side.getSelected();
                    switch (selected5) {
                        case "Left": {
                            if (GuiIngameHook.mm.hud.backGround.getBoolean()) {
                                RenderUtil.drawFloatRect(0.0f, 0.0f, this.getFontRenderer().getStringWidth(printTime) + 3 + width2, (float)(this.getFontRenderer().FONT_HEIGHT + 1), GuiIngameHook.mm.hud.backGroundColor.getColor().getRGB());
                            }
                            this.getFontRenderer().drawString(name2, 1.0f, 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                            this.getFontRenderer().drawString(printTime, width2, 1.0f, new Color(182, 186, 189).getRGB(), true, 0.5f);
                            break;
                        }
                        case "Right": {
                            if (GuiIngameHook.mm.hud.backGround.getBoolean()) {
                                RenderUtil.drawFloatRect(sr.getScaledWidth() - this.getFontRenderer().getStringWidth(printTime) - 3 - width2, 0.0f, (float)sr.getScaledWidth(), (float)(this.getFontRenderer().FONT_HEIGHT + 1), GuiIngameHook.mm.hud.backGroundColor.getColor().getRGB());
                            }
                            this.getFontRenderer().drawString(name2, sr.getScaledWidth() - width2 - this.getFontRenderer().getStringWidth(printTime), 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                            this.getFontRenderer().drawString(printTime, (float)(sr.getScaledWidth() - this.getFontRenderer().getStringWidth(printTime) - 1), 1.0f, new Color(182, 186, 189).getRGB(), true, 0.5f);
                            break;
                        }
                    }
                    break;
                }
                default: {
                    this.yAdd = 0.0f;
                    break;
                }
            }
        }
        if (!GuiIngameHook.mm.arrayList.sideOption.getSelected().equals(GuiIngameHook.mm.hud.side.getSelected())) {
            this.yAdd = 0.0f;
        }
    }
    
    public static void drawTargetHud() {
        if (KillAura.target instanceof EntityPlayer) {
            final int x = (int)GuiIngameHook.mm.hud.targethud_x.getValue();
            final int y = (int)GuiIngameHook.mm.hud.targethud_y.getValue();
            final String lowerCase = GuiIngameHook.mm.hud.targetMode.getSelected().toLowerCase();
            switch (lowerCase) {
                case "other": {
                    GuiUtils.drawRect1(x, y, 200.0, 150.0, GuiIngameHook.color.getRGB());
                    break;
                }
                case "basic": {
                    final EntityPlayer currentTarget = (EntityPlayer)KillAura.target;
                    final Color healthColor = new Color(getHealthColor(currentTarget));
                    final String playerName = "Name: " + StringUtils.stripControlCodes(currentTarget.getName());
                    final int distance = (int)GuiIngameHook.mc.thePlayer.getDistanceToEntity(currentTarget);
                    final float f1 = 133.0f / Minecraft.getDebugFPS() * 1.05f;
                    final float f2 = 140.0f / currentTarget.getMaxHealth() * Math.min(currentTarget.getHealth(), currentTarget.getMaxHealth());
                    GuiUtils.drawRect1(x - 1.0f, y - 1.0f, 142.0, 44.0, new Color(0, 0, 0, 100).getRGB());
                    GuiUtils.drawRect1(x, y, 140.0, 40.0, new Color(0, 0, 0, 75).getRGB());
                    GuiUtils.drawRect1(x, y + 40.0f, 140.0, 2.0, new Color(0, 0, 0).getRGB());
                    GuiIngameHook.mc.fontRendererObj.drawString(playerName, (int)(x + 25.0f), (int)(y + 4.0f), -1);
                    GuiIngameHook.mc.fontRendererObj.drawString("Distance: " + distance + "m", (int)(x + 25.0f), (int)(y + 15.0f), -1);
                    GuiIngameHook.mc.fontRendererObj.drawString("Armor: " + Math.round((float)currentTarget.getTotalArmorValue()), (int)(x + 25.0f), (int)(y + 25.0f), -1);
                    if (GuiIngameHook.mc.currentScreen == null) {
                        GuiInventory.drawEntityOnScreen(x + 12, y + 31, 13, currentTarget.rotationYaw, -currentTarget.rotationPitch, currentTarget);
                    }
                    if (f2 < GuiIngameHook.animated || f2 > GuiIngameHook.animated) {
                        if (Math.abs(f2 - GuiIngameHook.animated) <= f1) {
                            GuiIngameHook.animated = f2;
                        }
                        else {
                            GuiIngameHook.animated += ((GuiIngameHook.animated < f2) ? (f1 * 3.0f) : (-f1));
                        }
                    }
                    GuiUtils.drawRect1(x, y + 40.0f, GuiIngameHook.animated, 2.0, healthColor.getRGB());
                    break;
                }
            }
        }
    }
    
    public static int getHealthColor(final EntityLivingBase entityLivingBase) {
        final float percentage = 100.0f * entityLivingBase.getHealth() / 2.0f / entityLivingBase.getMaxHealth() / 2.0f;
        return (percentage > 75.0f) ? 1703705 : ((percentage > 50.0f) ? 16776960 : ((percentage > 25.0f) ? 16733440 : 16713984));
    }
    
    public static void drawCustomHotBar() {
        final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
        final FontRenderer fr = GuiIngameHook.mc.fontRendererObj;
        final int i = (GuiIngameHook.mc.getNetHandler() != null && GuiIngameHook.mc.getNetHandler().getPlayerInfo(GuiIngameHook.mc.thePlayer.getUniqueID()) != null) ? GuiIngameHook.mc.getNetHandler().getPlayerInfo(GuiIngameHook.mc.thePlayer.getUniqueID()).getResponseTime() : 0;
        final int x = GuiIngameHook.mm.protector.isToggled() ? ((int)GuiIngameHook.mc.thePlayer.posX + GuiIngameHook.mm.protector.getRandomX()) : ((int)GuiIngameHook.mc.thePlayer.posX);
        final int y = (int)GuiIngameHook.mc.thePlayer.posY;
        final int z = GuiIngameHook.mm.protector.isToggled() ? ((int)GuiIngameHook.mc.thePlayer.posZ + GuiIngameHook.mm.protector.getRandomZ()) : ((int)GuiIngameHook.mc.thePlayer.posZ);
        fr.drawString("§fFPS: §7" + Minecraft.getDebugFPS() + "   §fPing: §7" + i, (int)(4.0f + FontUtil.espHotbar.getStringWidth("A")), sr.getScaledHeight() - 20, Color.white.getRGB());
        fr.drawString("§fX: §7" + x + "  §fY: §7" + y + "  §fZ: §7" + z, (int)(4.0f + FontUtil.espHotbar.getStringWidth("X")), sr.getScaledHeight() - 9, Color.white.getRGB());
        FontUtil.espHotbar.drawStringWithShadow("X", 1.5, sr.getScaledHeight() - 19, Color.white.getRGB());
        if (!GuiIngameHook.mm.protector.isToggled() || !GuiIngameHook.mm.protector.protectTime.getBoolean()) {
            final GregorianCalendar now = new GregorianCalendar();
            final DateFormat df = DateFormat.getDateInstance(2);
            fr.drawString(df.format(now.getTime()), (float)(sr.getScaledWidth() - 10 - fr.getStringWidth(df.format(now.getTime()))), (float)(sr.getScaledHeight() - 1 - fr.FONT_HEIGHT), new Color(255, 255, 255, 221).getRGB(), true, 0.5f);
            final DateFormat df2 = DateFormat.getTimeInstance(3);
            fr.drawString(df2.format(now.getTime()), (float)(sr.getScaledWidth() - 10 - fr.getStringWidth(df2.format(now.getTime())) * 0.5 - fr.getStringWidth(df.format(now.getTime())) * 0.5), (float)(sr.getScaledHeight() - 11 - fr.FONT_HEIGHT), new Color(255, 255, 255, 221).getRGB(), true, 0.5f);
        }
    }
    
    public static void drawCustomCrossHair() {
        final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
        final double screenWidth = sr.getScaledWidth_double();
        final double screenHeight = sr.getScaledHeight_double();
        final Vec2d middle = new Vec2d(screenWidth / 2.0, screenHeight / 2.0);
        final double crossHairWidth = GuiIngameHook.mm.crossHair.width.getValue() / 2.0;
        final double crossHairLength = GuiIngameHook.mm.crossHair.length.getValue();
        final double crossHairMargin = GuiIngameHook.mm.crossHair.margin.getValue();
        int color = GuiIngameHook.mm.crossHair.color.getColor().getRGB();
        if (GuiIngameHook.mm.crossHair.rainbow.getBoolean()) {
            GuiIngameHook.mm.crossHair.rainbowUtil.updateRainbow((GuiIngameHook.mm.crossHair.rainbowSpeed.getValue() == 1000.0) ? ((float)(GuiIngameHook.mm.crossHair.rainbowSpeed.getValue() * 9.999999747378752E-6)) : ((float)(GuiIngameHook.mm.crossHair.rainbowSpeed.getValue() * 9.999999974752427E-7)), 255);
            color = GuiIngameHook.mm.crossHair.rainbowUtil.getColor().getRGB();
        }
        RenderUtil.drawRect(middle.x - crossHairWidth, middle.y + crossHairMargin + crossHairLength, middle.x + crossHairWidth, middle.y + crossHairMargin, color);
        if (!GuiIngameHook.mm.crossHair.tStyle.getBoolean()) {
            RenderUtil.drawRect(middle.x - crossHairWidth, middle.y - crossHairMargin - crossHairLength, middle.x + crossHairWidth, middle.y - crossHairMargin, color);
        }
        RenderUtil.drawRect(middle.x + crossHairMargin, middle.y + crossHairWidth, middle.x + crossHairMargin + crossHairLength, middle.y - crossHairWidth, color);
        RenderUtil.drawRect(middle.x - crossHairMargin - crossHairLength, middle.y + crossHairWidth, middle.x - crossHairMargin, middle.y - crossHairWidth, color);
        if (GuiIngameHook.mm.crossHair.dot.getBoolean()) {
            RenderUtil.drawRect(middle.x - crossHairWidth, middle.y + crossHairWidth, middle.x + crossHairWidth, middle.y - crossHairWidth, color);
        }
    }
    
    private void drawArmorHud() {
        final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
        final int height = sr.getScaledHeight() - 56;
        final int width = sr.getScaledWidth() / 2 + 91;
        GuiIngameHook.mc.getRenderItem().zLevel = -100.0f;
        if (GuiIngameHook.mc.thePlayer.getCurrentArmor(3) != null) {
            GuiIngameHook.mc.getRenderItem().renderItemIntoGUI(GuiIngameHook.mc.thePlayer.getCurrentArmor(3), width - 81, height);
        }
        if (GuiIngameHook.mc.thePlayer.getCurrentArmor(2) != null) {
            GuiIngameHook.mc.getRenderItem().renderItemIntoGUI(GuiIngameHook.mc.thePlayer.getCurrentArmor(2), width - 61, height);
        }
        if (GuiIngameHook.mc.thePlayer.getCurrentArmor(1) != null) {
            GuiIngameHook.mc.getRenderItem().renderItemIntoGUI(GuiIngameHook.mc.thePlayer.getCurrentArmor(1), width - 41, height);
        }
        if (GuiIngameHook.mc.thePlayer.getCurrentArmor(0) != null) {
            GuiIngameHook.mc.getRenderItem().renderItemIntoGUI(GuiIngameHook.mc.thePlayer.getCurrentArmor(0), width - 21, height);
        }
    }
    
    static {
        try {
            GuiIngameHook.ryuFontRenderer = new UnicodeFontRenderer(Font.createFont(0, GuiIngameHook.class.getResourceAsStream("/ressources/psr.ttf")).deriveFont(20.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            GuiIngameHook.oldFontRenderer = new UnicodeFontRenderer(Font.createFont(0, GuiIngameHook.class.getResourceAsStream("/ressources/Comfortaa-Bold.ttf")).deriveFont(56.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            GuiIngameHook.vegaFontRenderer = new UnicodeFontRenderer(Font.createFont(0, GuiIngameHook.class.getResourceAsStream("/ressources/roboto.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        GuiIngameHook.color = new Color(0, 0, 0, 100);
    }
}
