/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="AntiBlind", description="Cancels blindness effects.", category=ModuleCategory.RENDER)
public final class AntiBlind
extends Module {
    private final BoolValue pumpkinEffect;
    private final BoolValue confusionEffect = new BoolValue("Confusion", true);
    private final BoolValue fireEffect;

    public final BoolValue getPumpkinEffect() {
        return this.pumpkinEffect;
    }

    public final BoolValue getConfusionEffect() {
        return this.confusionEffect;
    }

    public final BoolValue getFireEffect() {
        return this.fireEffect;
    }

    public AntiBlind() {
        this.pumpkinEffect = new BoolValue("Pumpkin", true);
        this.fireEffect = new BoolValue("Fire", false);
    }
}

