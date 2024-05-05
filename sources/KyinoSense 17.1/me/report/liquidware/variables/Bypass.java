/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.variables;

import java.util.HashMap;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public class Bypass {
    public static HashMap<String, String> HuaYuTing = new HashMap();

    public static void loadBypass() {
        Bypass.loadHyt();
        ClientUtils.getLogger().info("[ChatBypass] \u52a0\u8f7d\u4e86 1 \u4e2a\u804a\u5929\u4fe1\u606f\u7ed5\u8fc7");
    }

    public static void loadHyt() {
        HuaYuTing.put("\u5f00\u6302", "\u5f00g");
    }
}

