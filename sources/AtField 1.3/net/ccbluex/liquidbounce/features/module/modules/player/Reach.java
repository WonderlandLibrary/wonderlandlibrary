/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="Reach", description="Increases your reach.", category=ModuleCategory.PLAYER)
public final class Reach
extends Module {
    private final FloatValue combatReachValue = new FloatValue("CombatReach", 3.5f, 3.0f, 7.0f);
    private final FloatValue buildReachValue = new FloatValue("BuildReach", 5.0f, 4.5f, 7.0f);

    public final FloatValue getBuildReachValue() {
        return this.buildReachValue;
    }

    public final float getMaxRange() {
        float f;
        float f2 = ((Number)this.combatReachValue.get()).floatValue();
        return f2 > (f = ((Number)this.buildReachValue.get()).floatValue()) ? f2 : f;
    }

    public final FloatValue getCombatReachValue() {
        return this.combatReachValue;
    }
}

