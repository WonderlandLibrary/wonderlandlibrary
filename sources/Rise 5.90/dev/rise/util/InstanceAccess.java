package dev.rise.util;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.module.Module;
import dev.rise.module.manager.ModuleManager;
import dev.rise.notifications.NotificationManager;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public interface InstanceAccess {
    Minecraft mc = Minecraft.getMinecraft();
    Rise instance = Rise.INSTANCE;

    ModuleManager moduleManager = Rise.INSTANCE.getModuleManager();
    NotificationManager notificationManager = Rise.INSTANCE.getNotificationManager();

    TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Light 18");
    TTFFontRenderer fontRendererMedium = CustomFont.FONT_MANAGER.getFont("Light 24");
    TTFFontRenderer fontRendererBig = CustomFont.FONT_MANAGER.getFont("Light 36");
    TTFFontRenderer fontRendererHuge = CustomFont.FONT_MANAGER.getFont("Light 72");

    TTFFontRenderer fontRendererBold = CustomFont.FONT_MANAGER.getFont("Light 18");

    TTFFontRenderer altoSmall = CustomFont.FONT_MANAGER.getFont("Biko 18");
    TTFFontRenderer altoCock = CustomFont.FONT_MANAGER.getFont("Biko 28");
    TTFFontRenderer alto = CustomFont.FONT_MANAGER.getFont("Biko 36");
    TTFFontRenderer altoHuge = CustomFont.FONT_MANAGER.getFont("Biko 48");

    TTFFontRenderer comfortaa = CustomFont.FONT_MANAGER.getFont("Comfortaa 18");
    TTFFontRenderer comfortaaNigger = CustomFont.FONT_MANAGER.getFont("Comfortaa 26");
    TTFFontRenderer comfortaaBig = CustomFont.FONT_MANAGER.getFont("Comfortaa 32");
    TTFFontRenderer skidFont = CustomFont.FONT_MANAGER.getFont("Skid 24");
    TTFFontRenderer skidFontBig = CustomFont.FONT_MANAGER.getFont("Skid 48");
    TTFFontRenderer skeet = CustomFont.FONT_MANAGER.getFont("SkeetBold 12");
    TTFFontRenderer skeetBig = CustomFont.FONT_MANAGER.getFont("Skeet 18");
    TTFFontRenderer oneTap = CustomFont.FONT_MANAGER.getFont("Skeet 16");

    TTFFontRenderer museo = CustomFont.FONT_MANAGER.getFont("Museo 20");
    TTFFontRenderer eaves = CustomFont.FONT_MANAGER.getFont("Eaves 18");

    default void registerNotification(final String text) {
        Rise.INSTANCE.getNotificationManager().registerNotification(text);
    }

    default Module getModule(final Class<? extends Module> clazz) {
        for (final Module module : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (module.getClass() == clazz) {
                return module;
            }
        }

        return null;
    }

    default boolean getBoolean(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : this.getModule(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((BooleanSetting) setting).isEnabled();
            }
        }

        return false;
    }

    default String getMode(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : this.getModule(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((ModeSetting) setting).getMode();
            }
        }

        return null;
    }

    default double getNumber(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : this.getModule(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((NumberSetting) setting).getValue();
            }
        }

        return 0;
    }
}
