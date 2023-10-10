/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import tk.rektsky.Client;
import tk.rektsky.module.Module;
import tk.rektsky.module.impl.combat.KillAura;
import tk.rektsky.module.impl.render.ToggleNotifications;
import tk.rektsky.module.settings.Setting;
import tk.rektsky.utils.YamlUtil;

public class ModulesManager {
    public static ArrayList<Module> MODULES = new ArrayList();
    public static Class[] IGNORED = new Class[0];

    public static void loadModuleSetting(YamlUtil.ConfiguredModule module) {
        module.getModule().rawSetToggled(module.isEnabled());
        module.getModule().keyCode = module.getKeybind();
        for (Setting setting : module.getSettings().keySet()) {
            module.getModule().settings.remove(setting);
            module.getModule().settings.add(setting);
        }
    }

    public static Module getModuleByName(String name) {
        for (Module module : MODULES) {
            if (!module.name.equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public static <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : MODULES) {
            if (!clazz.isInstance(module)) continue;
            return (T)module;
        }
        return null;
    }

    public static Module[] getModules() {
        return MODULES.toArray(new Module[0]);
    }

    public static void reloadModules() {
        MODULES.clear();
        ToggleNotifications mod = new ToggleNotifications();
        mod.settings.clear();
        for (Field field : mod.getClass().getFields()) {
            if (field.getType().getSuperclass() != Setting.class) continue;
            try {
                mod.settings.add((Setting)field.get(mod));
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        MODULES.add(mod);
        String prefix = "";
        if (!KillAura.class.getName().startsWith(prefix)) {
            prefix = "";
        }
        Reflections reflections = new Reflections(prefix, new Scanner[0]);
        Set<Class<Module>> modules = reflections.getSubTypesOf(Module.class);
        for (Class<Module> module : modules) {
            Module m2;
            if (Arrays.asList(IGNORED).contains(module) || module == ToggleNotifications.class) continue;
            try {
                m2 = module.newInstance();
                m2.settings.clear();
                for (Field field : module.getFields()) {
                    if (field.getType().getSuperclass() != Setting.class) continue;
                    m2.settings.add((Setting)field.get(m2));
                }
            }
            catch (IllegalAccessException | InstantiationException e2) {
                continue;
            }
            if (Client.finishedSetup) {
                Client.addClientChat((Object)((Object)ChatFormatting.GREEN) + "Reloading Module: " + (Object)((Object)ChatFormatting.YELLOW) + m2.name);
            }
            MODULES.add(m2);
        }
    }

    public static void inti() {
        ModulesManager.reloadModules();
    }
}

