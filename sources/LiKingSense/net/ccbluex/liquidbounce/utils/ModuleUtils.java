/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;

public class ModuleUtils {
    @SafeVarargs
    public static void disableModules(Class<? extends Module> ... modules2) {
        for (Class<? extends Module> module : modules2) {
            LiquidBounce.moduleManager.getModule(module).setState(false);
        }
    }
}

