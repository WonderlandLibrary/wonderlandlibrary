/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="GodLightSync", description="\u4f60\u597d", category=ModuleCategory.RENDER, canEnable=false)
public final class GodLightSync
extends Module {
    private final IntegerValue r = new IntegerValue("Red", 229, 0, 255);
    private final IntegerValue g = new IntegerValue("Green", 100, 0, 255);
    private final IntegerValue b = new IntegerValue("Blue", 173, 0, 255);
    private final IntegerValue r2 = new IntegerValue("Red2", 109, 0, 255);
    private final IntegerValue g2 = new IntegerValue("Green2", 255, 0, 255);
    private final IntegerValue b2 = new IntegerValue("Blue2", 255, 0, 255);

    public final IntegerValue getR() {
        return this.r;
    }

    public final IntegerValue getG() {
        return this.g;
    }

    public final IntegerValue getB() {
        return this.b;
    }

    public final IntegerValue getR2() {
        return this.r2;
    }

    public final IntegerValue getG2() {
        return this.g2;
    }

    public final IntegerValue getB2() {
        return this.b2;
    }
}

