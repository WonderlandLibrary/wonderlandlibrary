/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package com.wallhacks.losebypass.manager;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.InputEvent;
import com.wallhacks.losebypass.event.events.Render2DEvent;
import com.wallhacks.losebypass.event.events.TickEvent;
import com.wallhacks.losebypass.gui.HudEditor;
import com.wallhacks.losebypass.gui.tabs.HudTab;
import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.clientsetting.ClientSetting;
import com.wallhacks.losebypass.systems.clientsetting.clientsettings.ClickGuiConfig;
import com.wallhacks.losebypass.systems.hud.HudComponent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.utils.ColorUtil;
import com.wallhacks.losebypass.utils.ReflectionUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class SystemManager {
    private static final LinkedHashMap<Class<? extends Module>, Module> modules = new LinkedHashMap();
    private static final LinkedHashMap<Class<? extends HudComponent>, HudComponent> components = new LinkedHashMap();
    private static final LinkedHashMap<Class<? extends ClientSetting>, ClientSetting> clientSettings = new LinkedHashMap();
    public static boolean editingHud = false;
    static long lastFrame;

    public SystemManager() {
        ArrayList<Class<?>> modClasses;
        LoseBypass.eventBus.register(this);
        try {
            modClasses = ReflectionUtil.getClassesForPackage("org.dragonguard");
            modClasses.spliterator().forEachRemaining(aClass -> {
                if (!Module.class.isAssignableFrom((Class<?>)aClass)) return;
                try {
                    Module module = (Module)aClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                    modules.put(module.getClass(), module);
                    if (!module.getIsAlwaysListening()) {
                        if (!module.isEnabled()) return;
                    }
                    LoseBypass.eventBus.register(module);
                    return;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            modClasses = ReflectionUtil.getClassesForPackage("org.dragonguard");
            modClasses.spliterator().forEachRemaining(aClass -> {
                if (!HudComponent.class.isAssignableFrom((Class<?>)aClass)) return;
                try {
                    HudComponent component = (HudComponent)aClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                    components.put(component.getClass(), component);
                    return;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            modClasses = ReflectionUtil.getClassesForPackage("org.dragonguard");
            modClasses.spliterator().forEachRemaining(aClass -> {
                if (!ClientSetting.class.isAssignableFrom((Class<?>)aClass)) return;
                try {
                    ClientSetting module = (ClientSetting)aClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                    clientSettings.put(module.getClass(), module);
                    return;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        LoseBypass.logger.info("Loaded mods: " + modules.size());
        LoseBypass.logger.info("Loaded client settings: " + clientSettings.size());
    }

    public static List<SettingsHolder> getSystems() {
        ArrayList<SettingsHolder> l = new ArrayList<SettingsHolder>();
        l.addAll(SystemManager.getClientSettings());
        l.addAll(SystemManager.getModules());
        l.addAll(SystemManager.getHudComponents());
        l.add(HudTab.hudSettings);
        return l;
    }

    public static Collection<HudComponent> getHudComponents() {
        return components.values();
    }

    public static Collection<Module> getModules() {
        return modules.values();
    }

    public static Collection<ClientSetting> getClientSettings() {
        return clientSettings.values();
    }

    /*
     * Unable to fully structure code
     */
    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) return;
        var2_2 = SystemManager.getModules().iterator();
        block0: while (true) {
            if (var2_2.hasNext() == false) return;
            module = var2_2.next();
            if (!module.isHold()) continue;
            if (module.getBind() >= 0) {
                if (Keyboard.isKeyDown((int)module.getBind())) {
                    if (module.isEnabled()) continue;
                    module.enable();
                    continue;
                }
                if (!module.isEnabled()) continue;
                module.disable();
                continue;
            }
            i = 0;
            while (true) {
                if (i < 6) ** break;
                continue block0;
                if (module.getBind() == -(2 + i)) {
                    this.handleHold(Mouse.isButtonDown((int)i), module);
                }
                ++i;
            }
            break;
        }
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (!(Minecraft.getMinecraft().currentScreen instanceof HudEditor)) {
            SystemManager.getHudComponents().forEach(hud -> {
                if (!hud.isEnabled()) return;
                hud.drawComponent(false, System.currentTimeMillis() - lastFrame);
            });
        }
        ColorUtil.glColor(Color.white);
        lastFrame = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onInput(InputEvent event) {
        for (Module module : SystemManager.getModules()) {
            if (module.isHold() || module.getBind() != event.getKey()) continue;
            module.setEnabled(!module.isEnabled());
        }
        if (event.getKey() != ClickGuiConfig.getInstance().getBind()) return;
        if (!editingHud) {
            Minecraft.getMinecraft().displayGuiScreen(LoseBypass.clickGuiScreen);
            return;
        }
        Minecraft.getMinecraft().displayGuiScreen(HudTab.hudEditor);
    }

    private void handleHold(boolean down, Module module) {
        module.setEnabled(down);
    }
}

