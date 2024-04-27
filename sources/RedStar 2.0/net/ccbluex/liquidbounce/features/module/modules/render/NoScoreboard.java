package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

@ModuleInfo(name="NoScoreboard", description="Disables the scoreboard.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\bÇ\u000020B\b¢¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/NoScoreboard;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "Pride"})
public final class NoScoreboard
extends Module {
    public static final NoScoreboard INSTANCE;

    private NoScoreboard() {
    }

    static {
        NoScoreboard noScoreboard;
        INSTANCE = noScoreboard = new NoScoreboard();
    }
}
