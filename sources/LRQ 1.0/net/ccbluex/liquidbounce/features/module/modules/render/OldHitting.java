/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="OldHitting", description="faq", category=ModuleCategory.RENDER)
public final class OldHitting
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"LiquidSense", "Push", "1.8"}, "LiquidSense");

    public final ListValue getModeValue() {
        return this.modeValue;
    }
}

