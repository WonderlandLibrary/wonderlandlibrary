/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="TrueSight", description="Allows you to see invisible entities and barriers.", category=ModuleCategory.RENDER)
public final class TrueSight
extends Module {
    private final BoolValue barriersValue = new BoolValue("Barriers", true);
    private final BoolValue entitiesValue = new BoolValue("Entities", true);

    public final BoolValue getBarriersValue() {
        return this.barriersValue;
    }

    public final BoolValue getEntitiesValue() {
        return this.entitiesValue;
    }
}

