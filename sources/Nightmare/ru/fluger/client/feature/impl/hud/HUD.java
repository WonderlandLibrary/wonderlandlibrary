// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.event.EventTarget;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.ui.components.draggable.component.impl.DraggableWorldInfo;
import ru.fluger.client.ui.components.draggable.component.impl.DraggableArmorStatus;
import java.util.Comparator;
import optifine.CustomColors;
import java.util.Collection;
import java.util.ArrayList;
import ru.fluger.client.ui.components.draggable.component.impl.DraggablePotionStatus;
import ru.fluger.client.helpers.render.RenderHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Objects;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import ru.fluger.client.prot.UserData;
import ru.fluger.client.ui.components.draggable.component.DraggableComponent;
import ru.fluger.client.Fluger;
import ru.fluger.client.ui.components.draggable.component.impl.DraggableWaterMark;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class HUD extends Feature
{
    public static float globalOffset;
    public static ListSetting colorList;
    public static ColorSetting onecolor;
    public static ColorSetting twocolor;
    public static NumberSetting rainbowSaturation;
    public static NumberSetting rainbowBright;
    public static NumberSetting glowRadius;
    public static NumberSetting glowAlpha;
    public static ListSetting logoMode;
    public static BooleanSetting logo;
    public static BooleanSetting logoGlow;
    public static ListSetting logoColor;
    public static BooleanSetting clientInfo;
    public static BooleanSetting worldInfo;
    public static BooleanSetting potion;
    public static BooleanSetting potionIcons;
    public static BooleanSetting potionTimeColor;
    public static BooleanSetting armor;
    public static ColorSetting customRect;
    public static NumberSetting time;
    public float animation;
    protected bir gui;
    
    public HUD() {
        super("HUD", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043d\u0430 \u044d\u043a\u0440\u0430\u043d\u0435", Type.Hud);
        this.animation = 0.0f;
        this.gui = new bir();
        HUD.colorList = new ListSetting("HUD Color", "Fade", () -> true, new String[] { "Astolfo", "Static", "Fade", "Rainbow", "Pulse", "Custom", "None", "Category" });
        HUD.onecolor = new ColorSetting("One Color", new Color(4, 176, 94, 25).brighter().getRGB(), () -> HUD.colorList.currentMode.equals("Fade") || HUD.colorList.currentMode.equals("Custom") || HUD.colorList.currentMode.equals("Static"));
        HUD.twocolor = new ColorSetting("Two Color", new Color(4, 176, 94, 25).getRGB(), () -> HUD.colorList.currentMode.equals("Custom"));
        HUD.rainbowSaturation = new NumberSetting("Rainbow Saturation", 0.8f, 0.1f, 1.0f, 0.1f, () -> HUD.colorList.currentMode.equals("Rainbow"));
        HUD.rainbowBright = new NumberSetting("Rainbow Brightness", 1.0f, 0.1f, 1.0f, 0.1f, () -> HUD.colorList.currentMode.equals("Rainbow"));
        HUD.logo = new BooleanSetting("Logo", true, () -> true);
        HUD.logoMode = new ListSetting("Logo Mode", "Simple", HUD.logo::getCurrentValue, new String[] { "Simple", "OneTap", "NeverLose" });
        HUD.logoColor = new ListSetting("Logo Color", "Client", () -> HUD.logo.getCurrentValue() && !HUD.logoMode.currentMode.equals("OneTap") && !HUD.logoMode.currentMode.equals("NeverLose"), new String[] { "Client", "Custom", "White" });
        HUD.logoGlow = new BooleanSetting("Logo Glow", true, () -> HUD.logoMode.currentMode.equals("Dev") || HUD.logoMode.currentMode.equals("New"));
        HUD.glowAlpha = new NumberSetting("Glow Alpha", 0.3f, 0.1f, 1.0f, 0.1f, () -> HUD.logoMode.currentMode.equals("Dev") && HUD.logoGlow.getCurrentValue());
        HUD.glowRadius = new NumberSetting("Glow Radius", 25.0f, 5.0f, 50.0f, 5.0f, () -> (HUD.logoMode.currentMode.equals("Dev") || HUD.logoMode.currentMode.equals("New")) && HUD.logoGlow.getCurrentValue());
        HUD.clientInfo = new BooleanSetting("Client Info", true, () -> true);
        HUD.worldInfo = new BooleanSetting("World Info", true, () -> true);
        HUD.potion = new BooleanSetting("Potion Status", false, () -> true);
        HUD.potionIcons = new BooleanSetting("Potion Icons", true, HUD.potion::getCurrentValue);
        HUD.potionTimeColor = new BooleanSetting("Potion Time Color", true, HUD.potion::getCurrentValue);
        HUD.customRect = new ColorSetting("Custom Rect Color", Color.PINK.getRGB(), () -> HUD.logoColor.currentMode.equals("Custom"));
        HUD.armor = new BooleanSetting("Armor Status", true, () -> true);
        HUD.time = new NumberSetting("Color Time", 30.0f, 1.0f, 100.0f, 1.0f, () -> true);
        this.addSettings(HUD.colorList, HUD.onecolor, HUD.twocolor, HUD.rainbowSaturation, HUD.rainbowBright, HUD.time, HUD.logo, HUD.logoMode, HUD.logoColor, HUD.logoGlow, HUD.glowRadius, HUD.glowAlpha, HUD.customRect, HUD.clientInfo, HUD.worldInfo, HUD.potion, HUD.potionIcons, HUD.potionTimeColor, HUD.armor);
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D e) {
        final float target = (HUD.mc.m instanceof bkn) ? 15.0f : 0.0f;
        final float delta;
        if (!Double.isFinite(HUD.globalOffset -= (delta = HUD.globalOffset - target) / Math.max(1, bib.af()) * 10.0f)) {
            HUD.globalOffset = 0.0f;
        }
        if (HUD.globalOffset > 15.0f) {
            HUD.globalOffset = 15.0f;
        }
        if (HUD.globalOffset < 0.0f) {
            HUD.globalOffset = 0.0f;
        }
        if (HUD.logo.getCurrentValue() && !HUD.mc.t.ax) {
            final String mode = HUD.logoMode.getCurrentMode();
            Color color = Color.BLACK;
            final String currentMode = HUD.logoColor.currentMode;
            switch (currentMode) {
                case "Custom": {
                    color = new Color(HUD.customRect.getColor());
                    break;
                }
                case "Client": {
                    color = ClientHelper.getClientColor();
                    break;
                }
                case "White": {
                    color = Color.WHITE;
                    break;
                }
            }
            final DraggableWaterMark dwm = (DraggableWaterMark)Fluger.instance.draggableHUD.getDraggableComponentByClass(DraggableWaterMark.class);
            dwm.setWidth(135);
            dwm.setHeight(20);
            if (mode.equalsIgnoreCase("OneTap")) {
                final int x = dwm.getX();
                final int y = dwm.getY();
                String server;
                if (HUD.mc.E()) {
                    server = "localhost";
                }
                else {
                    assert HUD.mc.C() != null;
                    server = HUD.mc.C().b.toLowerCase();
                }
                final String str = UserData.instance().getName();
                final String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                final String text = "VanePremium |" + str + " | " + server + " | FPS: " + bib.af() + " | " + dateFormat;
                final float width = (float)(HUD.mc.rubik_16.getStringWidth(text) + 3);
                RectHelper.drawRect(x, y - 1, x + width + 2.0f, y + 12, new Color(5, 5, 5, 145).getRGB());
                RectHelper.drawGradientSideways(x, y - 1.1f, x + width + 2.0f, y + 0.1f, color.getRGB(), color.darker().darker().getRGB());
                HUD.mc.rubik_16.drawStringWithShadow(text, x + 3, y + 3.5f, new Color(225, 225, 225, 255).getRGB());
            }
            else if (mode.equalsIgnoreCase("NeverLose")) {
                final int posX = dwm.getX();
                final int posY = dwm.getY();
                final int ping = Objects.requireNonNull(HUD.mc.v()).a(HUD.mc.h.bm()).c();
                final String dateFormat = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                final String clientName = Fluger.name + "Client";
                final String info = ChatFormatting.GRAY + " | " + ChatFormatting.RESET + ping + " ms" + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + "FPS " + bib.af() + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + UserData.instance().getName() + ChatFormatting.GRAY + " | " + ChatFormatting.RESET + dateFormat;
                RenderHelper.renderBlurredShadow(Color.BLACK, posX - 2, posY, HUD.mc.museo.getStringWidth(clientName.toUpperCase()) + HUD.mc.tahoma.getStringWidth(info) + 9, 13.0, 15);
                RectHelper.drawSmoothRectBetter(posX - 2, posY, HUD.mc.museo.getStringWidth(clientName.toUpperCase()) + HUD.mc.tahoma.getStringWidth(info) + 7, 13.0, new Color(20, 20, 20, 255).getRGB());
                HUD.mc.museo.drawStringWithShadow(clientName.toUpperCase(), posX, posY + 3, new Color(96, 158, 190).getRGB());
                HUD.mc.museo.drawStringWithShadow(clientName.toUpperCase(), posX + 1, posY + 3, -1);
                HUD.mc.tahoma.drawStringWithShadow(info, posX + HUD.mc.museo.getStringWidth(clientName.toUpperCase()) + 2, posY + 4, -1);
            }
            else if (mode.equalsIgnoreCase("Simple")) {
                Color color2 = Color.BLACK;
                final String currentMode2 = HUD.logoColor.currentMode;
                switch (currentMode2) {
                    case "Custom": {
                        color2 = new Color(HUD.customRect.getColor());
                        break;
                    }
                    case "Client": {
                        color2 = ClientHelper.getClientColor();
                        break;
                    }
                    case "White": {
                        color2 = Color.WHITE;
                        break;
                    }
                }
                final int x2 = dwm.getX();
                final int y2 = dwm.getY();
                String server;
                if (HUD.mc.E()) {
                    server = "localhost";
                }
                else {
                    assert HUD.mc.C() != null;
                    server = HUD.mc.C().b.toLowerCase();
                }
                final String dateFormat2 = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                final int ping2 = Objects.requireNonNull(HUD.mc.v()).a(HUD.mc.h.bm()).c();
                final String text2 = "VanePremium" + Fluger.status + " | " + server + " | Ping " + ping2 + " | " + dateFormat2;
                final float width2 = (float)(HUD.mc.fontRenderer.getStringWidth(text2) + 4);
                final int top = y2;
                final int left = x2;
                if (HUD.logoGlow.getCurrentValue()) {
                    RenderHelper.drawBlurredShadow((float)(left - 5), (float)top, (float)((int)width2 + 10), 9.0f, 20, color2);
                }
                RectHelper.drawSmoothRect((float)x2, (float)y2, x2 + width2, y2 + 11, new Color(5, 5, 5, 120).getRGB());
                HUD.mc.fontRenderer.drawStringWithShadow(text2, x2 + 2, y2 + 2, -1);
            }
        }
        if (HUD.potion.getCurrentValue()) {
            final DraggablePotionStatus dpt = (DraggablePotionStatus)Fluger.instance.draggableHUD.getDraggableComponentByClass(DraggablePotionStatus.class);
            dpt.setWidth(100);
            dpt.setHeight(150);
            final int xOff = 21;
            final int yOff = 14;
            int counter = 16;
            final Collection<va> collection = HUD.mc.h.ca();
            bus.c(1.0f, 1.0f, 1.0f, 1.0f);
            bus.g();
            int listOffset = 23;
            if (collection.size() > 5) {
                listOffset = 132 / (collection.size() - 1);
            }
            final List<va> potions = new ArrayList<va>(HUD.mc.h.ca());
            potions.sort(Comparator.comparingDouble(effect -> ClientHelper.getFontRender().getStringWidth(Objects.requireNonNull(uz.a(CustomColors.getPotionId(effect.f()))).a())));
            for (final va potion : potions) {
                final uz effect2 = uz.a(CustomColors.getPotionId(potion.f()));
                bus.c(1.0f, 1.0f, 1.0f, 1.0f);
                assert effect2 != null;
                if (effect2.c() && HUD.potionIcons.getCurrentValue()) {
                    HUD.mc.N().a(new nf("textures/gui/container/inventory.png"));
                    final int statusIconIndex = effect2.d();
                    this.gui.b(dpt.getX() + xOff - 20, dpt.getY() + counter - yOff, statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                }
                String level = cey.a(effect2.a(), new Object[0]);
                if (potion.c() == 1) {
                    level = level + " " + cey.a("enchantment.level.2", new Object[0]);
                }
                else if (potion.c() == 2) {
                    level = level + " " + cey.a("enchantment.level.3", new Object[0]);
                }
                else if (potion.c() == 3) {
                    level = level + " " + cey.a("enchantment.level.4", new Object[0]);
                }
                int getPotionColor = -1;
                if (potion.b() < 200) {
                    getPotionColor = new Color(215, 59, 59).getRGB();
                }
                else if (potion.b() < 400) {
                    getPotionColor = new Color(231, 143, 32).getRGB();
                }
                else if (potion.b() > 400) {
                    getPotionColor = new Color(172, 171, 171).getRGB();
                }
                final String durationString = uz.getDurationString(potion);
                ClientHelper.getFontRender().drawStringWithShadow(level, dpt.getX() + xOff, dpt.getY() + counter - yOff, -1);
                ClientHelper.getFontRender().drawStringWithShadow(durationString, dpt.getX() + xOff, dpt.getY() + counter + 10 - yOff, HUD.potionTimeColor.getCurrentValue() ? getPotionColor : -1);
                counter += listOffset;
            }
        }
        if (HUD.armor.getCurrentValue()) {
            final DraggableArmorStatus das = (DraggableArmorStatus)Fluger.instance.draggableHUD.getDraggableComponentByClass(DraggableArmorStatus.class);
            das.setWidth(100);
            das.setHeight(30);
            bus.G();
            bus.y();
            final int i = das.getX();
            final int y3 = das.getY();
            int count = 0;
            for (final aip is : HUD.mc.h.bv.b) {
                ++count;
                final int x2 = i - 90 + (9 - count) * 20 + 2;
                bus.k();
                HUD.mc.ad().a = 200.0f;
                HUD.mc.ad().b(is, x2, y3);
                HUD.mc.ad().a(HUD.mc.k, is, x2, y3, "");
                HUD.mc.ad().a = 0.0f;
                bus.y();
                bus.g();
                bus.j();
            }
            bus.k();
            bus.g();
            bus.H();
        }
        if (HUD.worldInfo.getCurrentValue()) {
            final DraggableWorldInfo dwi = (DraggableWorldInfo)Fluger.instance.draggableHUD.getDraggableComponentByClass(DraggableWorldInfo.class);
            dwi.setWidth(75);
            dwi.setHeight(27);
            final double prevPosX = HUD.mc.h.p - HUD.mc.h.m;
            final double prevPosZ = HUD.mc.h.r - HUD.mc.h.o;
            final float distance = (float)Math.sqrt(prevPosX * prevPosX + prevPosZ * prevPosZ);
            final BigDecimal speedValue = MathematicHelper.round(distance * 15.5f, 1);
            final String speed = "Speed: " + ChatFormatting.WHITE + String.format("%.2f b/s", speedValue);
            ClientHelper.getFontRender().drawStringWithShadow("FPS: " + ChatFormatting.WHITE + bib.af(), dwi.getX(), dwi.getY(), ClientHelper.getClientColor().getRGB());
            ClientHelper.getFontRender().drawStringWithShadow(speed, dwi.getX(), dwi.getY() + 9, ClientHelper.getClientColor().getRGB());
            ClientHelper.getFontRender().drawStringWithShadow("XYZ: " + ChatFormatting.WHITE + Math.round(HUD.mc.h.p) + " " + Math.round(HUD.mc.h.q) + " " + Math.round(HUD.mc.h.r), dwi.getX(), dwi.getY() + 18, ClientHelper.getClientColor().getRGB());
        }
    }
}
