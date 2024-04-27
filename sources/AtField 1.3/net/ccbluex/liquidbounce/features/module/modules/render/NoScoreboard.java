/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

@ModuleInfo(name="NoScoreboard", description="Disables the scoreboard.", category=ModuleCategory.RENDER)
public final class NoScoreboard
extends Module {
    public static final NoScoreboard INSTANCE;

    static {
        NoScoreboard noScoreboard;
        INSTANCE = noScoreboard = new NoScoreboard();
    }

    private NoScoreboard() {
    }
}

