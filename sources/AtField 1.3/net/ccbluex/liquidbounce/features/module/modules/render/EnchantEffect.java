/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="EnchantEffect", description="Change Sword Color", category=ModuleCategory.RENDER)
public final class EnchantEffect
extends Module {
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue alphaValue;
    private final BoolValue rainbow;
    private final IntegerValue colorBlueValue;
    private final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);

    public final BoolValue getRainbow() {
        return this.rainbow;
    }

    public EnchantEffect() {
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.alphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.rainbow = new BoolValue("RainBow", false);
    }

    public final IntegerValue getalphaValue() {
        return this.alphaValue;
    }

    public final IntegerValue getBlueValue() {
        return this.colorBlueValue;
    }

    public final IntegerValue getRedValue() {
        return this.colorRedValue;
    }

    public final IntegerValue getGreenValue() {
        return this.colorGreenValue;
    }
}

