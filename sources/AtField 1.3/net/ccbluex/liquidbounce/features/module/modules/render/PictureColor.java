/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="PictureColor", description="\u5168\u5c40\u989c\u8272", category=ModuleCategory.HYT)
public final class PictureColor
extends Module {
    private final IntegerValue coloralpha;
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorBlueValue;
    private final IntegerValue colorGreenValue = new IntegerValue("G", 72, 0, 255);

    public final IntegerValue getColorRedValue() {
        return this.colorRedValue;
    }

    public final IntegerValue getColoralpha() {
        return this.coloralpha;
    }

    public final IntegerValue getColorBlueValue() {
        return this.colorBlueValue;
    }

    public final IntegerValue getColorGreenValue() {
        return this.colorGreenValue;
    }

    public PictureColor() {
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
        this.coloralpha = new IntegerValue("alpha", 255, 0, 255);
    }
}

