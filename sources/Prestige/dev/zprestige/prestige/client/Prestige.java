package dev.zprestige.prestige.client;

import dev.zprestige.prestige.client.bypass.ScreenshareBypass;
import dev.zprestige.prestige.client.bypass.impl.LogBypass;
import dev.zprestige.prestige.client.event.EventBus;
import dev.zprestige.prestige.client.handler.Handler;
import dev.zprestige.prestige.client.handler.impl.KeybindHanlder;
import dev.zprestige.prestige.client.handler.impl.LoginHandler;
import dev.zprestige.prestige.client.handler.impl.ProtectionHandler;
import dev.zprestige.prestige.client.managers.*;
import dev.zprestige.prestige.client.protection.Session;
import dev.zprestige.prestige.client.shader.impl.GradientGlowShader;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.impl.ConfigScreen;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.fabricmc.api.ModInitializer;

import java.util.Arrays;
import java.util.List;

public class Prestige implements ModInitializer {

    public static Companion Companion = new Companion();
    private static final EventBus eventBus = new EventBus();
    private static final FontManager fontManager = new FontManager();
    private static final ModuleManager moduleManager = new ModuleManager();
    private static final ConfigManager configManager = new ConfigManager();
    private static final DamageManager damageManager = new DamageManager();
    private static final SocialsManager socialsManager = new SocialsManager();
    private static final RenderManager renderManager = new RenderManager();
    private static final AntiBotManager antiBotManager = new AntiBotManager();
    private static final ProtectionManager protectionManager = new ProtectionManager();
    private static final ClickManager clickManager = new ClickManager();
    private static final ScreenshareBypass screenshareBypass = new ScreenshareBypass();
    private static final Interface clickGUI = new Interface();
    private static ConfigScreen configScreen;
    private static final TargetManager targetManager = new TargetManager();
    private static final RotationManager rotationManager = new RotationManager();
    private static Session session;
    public static boolean selfDestructed;

    @Override
    public void onInitialize() {
        List<Handler> handlers = Arrays.asList(new KeybindHanlder(), new LoginHandler(), new ProtectionHandler());
        handlers.forEach(Handler::register);
        if (handlers.size() != 3) {
            ProtectionManager.exit("K");
        }
        RenderUtil.shader = new GradientGlowShader();
        screenshareBypass.setList(List.of(new LogBypass()));
    }

    public static class Companion {

        public static EventBus getEventBus() {
            return eventBus;
        }

        public static FontManager getFontManager() {
            return fontManager;
        }

        public static ModuleManager getModuleManager() {
            return moduleManager;
        }

        public static ConfigManager getConfigManager() {
            return configManager;
        }

        public static DamageManager getDamageManager() {
            return damageManager;
        }

        public static SocialsManager getSocialsManager() {
            return socialsManager;
        }

        public static RenderManager getRenderManager() {
            return renderManager;
        }

        public static AntiBotManager getAntiBotManager() {
            return antiBotManager;
        }

        public static ProtectionManager getProtectionManager() {
            return protectionManager;
        }

        public static ClickManager getClickManager() {
            return clickManager;
        }

        public static Interface getClickGUI() {
            return clickGUI;
        }

        public static ConfigScreen getConfigScreen() {
            return configScreen;
        }

        public static void setConfigScreen(ConfigScreen value) {
            configScreen = value;
        }

        public static TargetManager getTargetManager() {
            return targetManager;
        }

        public static RotationManager getRotationManager() {
            return rotationManager;
        }

        public static Session getSession() {
            return session;
        }

        public static void setSession(Session value) {
            session = value;
        }

        public static boolean getSelfDestructed() {
            return selfDestructed;
        }

        public static void setSelfDestructed(boolean value) {
            selfDestructed = value;
        }
    }
}
