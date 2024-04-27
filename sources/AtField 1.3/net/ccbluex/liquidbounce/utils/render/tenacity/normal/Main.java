/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render.tenacity.normal;

import java.util.List;
import java.util.stream.Collectors;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleManager;

public class Main {
    public static boolean reloadModules;
    public static int categoryCount;
    public static float allowedClickGuiHeight;

    public static List getModulesInCategory(ModuleCategory moduleCategory, ModuleManager moduleManager) {
        return moduleManager.getModules().stream().filter(arg_0 -> Main.lambda$getModulesInCategory$0(moduleCategory, arg_0)).collect(Collectors.toList());
    }

    static {
        allowedClickGuiHeight = 300.0f;
    }

    private static boolean lambda$getModulesInCategory$0(ModuleCategory moduleCategory, Module module) {
        return module.getCategory() == moduleCategory;
    }
}

