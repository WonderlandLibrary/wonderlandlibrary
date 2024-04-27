/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="FastPlace", description="Allows you to place blocks faster.", category=ModuleCategory.WORLD)
public final class FastPlace
extends Module {
    private final IntegerValue speedValue = new IntegerValue("Speed", 0, 0, 4);

    public final IntegerValue getSpeedValue() {
        return this.speedValue;
    }
}

