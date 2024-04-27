/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.LiquidBounce;

public class ModuleUtils {
    @SafeVarargs
    public static void disableModules(Class ... classArray) {
        for (Class clazz : classArray) {
            LiquidBounce.moduleManager.getModule(clazz).setState(false);
        }
    }
}

