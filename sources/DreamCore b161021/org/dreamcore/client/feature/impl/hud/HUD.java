package org.dreamcore.client.feature.impl.hud;

import javafx.animation.Interpolator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender2D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.combat.KillAura;
import org.dreamcore.client.helpers.player.MovementHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;
import org.dreamcore.client.ui.components.draggable.DraggableModule;

import java.awt.*;

public class HUD extends Feature {

    public static float globalOffset;
    public static ListSetting font;
    public static BooleanSetting clientInfo;
    public static BooleanSetting worldInfo;
    public static BooleanSetting potion;
    public static BooleanSetting potionIcons;
    public static BooleanSetting potionTimeColor;
    public static BooleanSetting armor;
    public static BooleanSetting rustHUD;
    public static BooleanSetting indicator;
    public static ListSetting indicatorMode;
    public static BooleanSetting blur = new BooleanSetting("Blur", false, () -> true);
    public static ListSetting colorList = new ListSetting("Global Color", "Astolfo", () -> true, "Astolfo", "Static", "Fade", "Rainbow", "Custom", "None", "Category");
    public static NumberSetting time = new NumberSetting("Color Time", 30, 1, 100, 1, () -> true);
    public static ColorSetting onecolor = new ColorSetting("One Color", new Color(0xFFFFFF).getRGB(), () -> HUD.colorList.currentMode.equals("Fade") || HUD.colorList.currentMode.equals("Custom") || HUD.colorList.currentMode.equals("Static"));
    public static ColorSetting twocolor = new ColorSetting("Two Color", new Color(0xFF0000).getRGB(), () -> HUD.colorList.currentMode.equals("Custom"));
    public float animation = 0;
    private float cooledAttackStrength = 0;
    private float move = 0;

    public HUD() {
        super("HUD", "Показывает информацию на экране", Type.Hud);
        clientInfo = new BooleanSetting("Client Info", true, () -> true);
        worldInfo = new BooleanSetting("World Info", true, () -> true);
        potion = new BooleanSetting("Potion Status", true, () -> true);
        potionIcons = new BooleanSetting("Potion Icons", true, potion::getBoolValue);
        potionTimeColor = new BooleanSetting("Potion Time Color", true, potion::getBoolValue);
        armor = new BooleanSetting("Armor Status", true, () -> true);
        rustHUD = new BooleanSetting("Rust", false, () -> true);
        indicator = new BooleanSetting("Indicator", false, () -> true);
        indicatorMode = new ListSetting("Indicator Mode", "Onetap v2", () -> indicator.getBoolValue(), "Onetap v2", "Skeet");
        font = new ListSetting("FontList", "RobotoRegular", () -> true, "Minecraft", "MontserratRegular", "MontserratLight", "Menlo", "Comfortaa", "SF UI", "Calibri", "Verdana", "CircleRegular", "RobotoRegular", "JetBrains Mono", "LucidaConsole", "Corporative Sans", "Lato", "RaleWay", "Product Sans", "Arial", "Open Sans", "Kollektif", "Ubuntu");
        addSettings(colorList, time, onecolor, twocolor, font, clientInfo, worldInfo, potion, potionIcons, potionTimeColor, armor, rustHUD, blur, indicator, indicatorMode);
    }

    @EventTarget
    public void onRender2(EventRender2D e) {
        if (indicator.getBoolValue()) {
            if (Float.isNaN(cooledAttackStrength)) {
                cooledAttackStrength = mc.player.getCooledAttackStrength(0);
            }
            if (Float.isNaN(move)) {
                move = MovementHelper.getSpeed();
            }

            cooledAttackStrength = (float) Interpolator.LINEAR.interpolate(cooledAttackStrength, mc.player.getCooledAttackStrength(0) * 50, 5F / Minecraft.getDebugFPS());
            move = (float) Interpolator.LINEAR.interpolate(move, MovementHelper.getSpeed() * 50, 5F / Minecraft.getDebugFPS());

            if (indicatorMode.currentMode.equals("Onetap v2")) {
                RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 80, 105, 26, new Color(61, 58, 58).getRGB());
                RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 97, 105, 2, new Color(123, 0, 255).getRGB());
                RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 95, 105, 12, new Color(61, 58, 58).getRGB());
                mc.clickguismall.drawStringWithShadow("flags", 32 + mc.clickguismall.getStringWidth("flags"), e.getResolution().getScaledHeight() / 2F - 92, -1);
                mc.clickguismall.drawStringWithShadow("cooldown", 10, e.getResolution().getScaledHeight() / 2F - 75, -1);
                RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
                RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, cooledAttackStrength, 1.5F, new Color(123, 0, 255).getRGB());
                mc.clickguismall.drawStringWithShadow("move", 10, e.getResolution().getScaledHeight() / 2F - 64, -1);
                RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
                RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, move, 1.5F, new Color(123, 0, 255).getRGB());

                float y = e.getResolution().getScaledHeight() / 2F - -7;

                for (Feature feature : dreamcore.instance.featureManager.getFeatureList()) {
                    if (feature.getBind() != 0 && !feature.getLabel().equals("ClickGui") && !feature.getLabel().equals("ClientFont")) {
                        RectHelper.drawSmoothRectBetter(6, y, 105, 13, new Color(61, 58, 58).getRGB());
                        RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 10, 105, 2, new Color(123, 0, 255).getRGB());
                        RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 8, 105, 12, new Color(61, 58, 58).getRGB());
                        mc.clickguismall.drawStringWithShadow("keybinds", 10 + mc.clickguismall.getStringWidth("keybinds"), e.getResolution().getScaledHeight() / 2F - 5, -1);
                        String toggled = feature.getState() ? " [toggled]" : " [disable]";
                        mc.smallfontRenderer.drawStringWithShadow(feature.getLabel().toLowerCase(), 10, y + 4, -1);
                        mc.smallfontRenderer.drawStringWithShadow(toggled, 75, y + 4, -1);
                        y += 12;
                    }
                }
            }
        }
        if (indicatorMode.currentMode.equals("Skeet") && indicator.getBoolValue()) {
            RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 80, 105, 26, new Color(61, 58, 58, 140).getRGB());
            RectHelper.drawGradientRectBetter(5.5F, e.getResolution().getScaledHeight() / 2F - 97, 106, 2, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).darker().darker().getRGB());
            RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 95, 105, 12, new Color(61, 58, 58, 140).getRGB());
            mc.clickguismall.drawStringWithShadow("flags", 32 + mc.clickguismall.getStringWidth("flags"), e.getResolution().getScaledHeight() / 2F - 92, -1);
            mc.clickguismall.drawStringWithShadow("cooldown", 10, e.getResolution().getScaledHeight() / 2F - 75, -1);
            RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
            RectHelper.drawGradientRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, cooledAttackStrength, 1.5F, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).darker().darker().getRGB());
            mc.clickguismall.drawStringWithShadow("move", 10, e.getResolution().getScaledHeight() / 2F - 64, -1);
            RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
            RectHelper.drawGradientRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, move, 1.5F, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).darker().darker().getRGB());

            float y = e.getResolution().getScaledHeight() / 2F - -7;

            for (Feature feature : dreamcore.instance.featureManager.getFeatureList()) {
                if (feature.getBind() != 0 && !feature.getLabel().equals("ClickGui") && !feature.getLabel().equals("ClientFont")) {
                    RectHelper.drawSmoothRectBetter(6, y, 105, 13, new Color(61, 58, 58, 140).getRGB());
                    RectHelper.drawGradientRectBetter(5.5F, e.getResolution().getScaledHeight() / 2F - 10, 106, 2, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).darker().darker().getRGB());
                    RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 8, 105, 12, new Color(61, 58, 58, 140).getRGB());
                    mc.clickguismall.drawStringWithShadow("keybinds", 10 + mc.clickguismall.getStringWidth("keybinds"), e.getResolution().getScaledHeight() / 2F - 5, -1);
                    String toggled = feature.getState() ? " [toggled]" : " [disable]";
                    mc.smallfontRenderer.drawStringWithShadow(feature.getLabel().toLowerCase(), 10, y + 5, -1);
                    mc.smallfontRenderer.drawStringWithShadow(toggled, 75, y + 5, -1);
                    y += 12;
                }
            }
        }

    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        for (DraggableModule draggableModule : dreamcore.instance.draggableManager.getMods()) {
            if (getState() && !draggableModule.name.equals("LogoComponent")) {
                draggableModule.draw();
            }
        }
        float target = (mc.currentScreen instanceof GuiChat) ? 15 : 0;
        float delta = globalOffset - target;
        globalOffset -= delta / Math.max(1, Minecraft.getDebugFPS()) * 10;
        if (!Double.isFinite(globalOffset)) {
            globalOffset = 0;
        }
        if (globalOffset > 15) {
            globalOffset = 15;
        }
        if (globalOffset < 0) {
            globalOffset = 0;
        }
    }
}
