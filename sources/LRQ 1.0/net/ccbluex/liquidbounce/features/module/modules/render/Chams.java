/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="Chams", description="Allows you to see targets through blocks.", category=ModuleCategory.RENDER)
public final class Chams
extends Module {
    private final BoolValue targetsValue = new BoolValue("Targets", true);
    private final BoolValue chestsValue = new BoolValue("Chests", true);
    private final BoolValue itemsValue = new BoolValue("Items", true);

    public final BoolValue getTargetsValue() {
        return this.targetsValue;
    }

    public final BoolValue getChestsValue() {
        return this.chestsValue;
    }

    public final BoolValue getItemsValue() {
        return this.itemsValue;
    }
}

