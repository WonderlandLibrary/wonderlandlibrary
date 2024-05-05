/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.render;

import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;

@Module.Registration(name="3DSkins", description="Combines skin layer to make them seem 3D", category=Module.Category.RENDER)
public class ThreeDSkins
extends Module {
    IntSetting range = this.intSetting("Range", 15, 5, 100);
    BooleanSetting hands = this.booleanSetting("Hands", false);
    private static ThreeDSkins instance;

    public ThreeDSkins() {
        instance = this;
    }

    public static boolean enabled() {
        return instance.isEnabled();
    }

    public static boolean hands() {
        return ThreeDSkins.instance.hands.isOn();
    }

    public static int range() {
        return (Integer)ThreeDSkins.instance.range.getValue() * (Integer)ThreeDSkins.instance.range.getValue();
    }
}

