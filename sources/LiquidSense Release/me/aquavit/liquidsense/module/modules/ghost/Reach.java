package me.aquavit.liquidsense.module.modules.ghost;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.FloatValue;

@ModuleInfo(name = "Reach", description = "Increases your reach.", category = ModuleCategory.GHOST)
public class Reach extends Module {
    public static FloatValue combatReachValue = new FloatValue("CombatReach", 3.5f, 3f, 7f);
    public static FloatValue buildReachValue = new FloatValue("BuildReach", 5f, 4.5f, 7f);

    public static float getMaxRange() {
        float buildRange = buildReachValue.get();
        float combatRange = combatReachValue.get();
        return Math.max(combatRange, buildRange);
    }
}
