/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="NoFOV", description="Disables FOV changes caused by speed effect, etc.", category=ModuleCategory.RENDER)
public final class NoFOV
extends Module {
    private final FloatValue fovValue = new FloatValue("FOV", 1.0f, 0.0f, 1.5f);

    public final FloatValue getFovValue() {
        return this.fovValue;
    }
}

