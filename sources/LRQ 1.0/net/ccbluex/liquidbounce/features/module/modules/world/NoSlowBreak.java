/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="NoSlowBreak", description="Automatically adjusts breaking speed when using modules that influence it.", category=ModuleCategory.WORLD)
public final class NoSlowBreak
extends Module {
    private final BoolValue airValue = new BoolValue("Air", true);
    private final BoolValue waterValue = new BoolValue("Water", false);

    public final BoolValue getAirValue() {
        return this.airValue;
    }

    public final BoolValue getWaterValue() {
        return this.waterValue;
    }
}

