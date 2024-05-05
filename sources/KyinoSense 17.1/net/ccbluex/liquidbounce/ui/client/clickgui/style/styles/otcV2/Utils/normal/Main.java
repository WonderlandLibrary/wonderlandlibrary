/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.normal;

import java.util.List;
import java.util.stream.Collectors;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleManager;

public class Main {
    public static int categoryCount;
    public static boolean reloadModules;
    public static float allowedClickGuiHeight;

    public static List<Module> getModulesInCategory(ModuleCategory c, ModuleManager moduleManager) {
        return moduleManager.getModules().stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }

    static {
        allowedClickGuiHeight = 300.0f;
    }
}

