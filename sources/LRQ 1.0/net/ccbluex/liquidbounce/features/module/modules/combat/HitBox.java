/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="HitBox", description="Makes hitboxes of targets bigger.", category=ModuleCategory.COMBAT)
public final class HitBox
extends Module {
    private final FloatValue sizeValue = new FloatValue("Size", 0.4f, 0.0f, 1.0f);

    public final FloatValue getSizeValue() {
        return this.sizeValue;
    }
}

