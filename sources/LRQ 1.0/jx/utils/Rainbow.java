/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package jx.utils;

import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="Rainbow", category=ModuleCategory.RENDER, canEnable=false, description="Custom")
public final class Rainbow
extends Module {
    @JvmField
    public static final FloatValue rainbowStart;
    @JvmField
    public static final FloatValue rainbowStop;
    @JvmField
    public static final FloatValue rainbowSaturation;
    @JvmField
    public static final FloatValue rainbowBrightness;
    @JvmField
    public static final IntegerValue rainbowSpeed;
    public static final Companion Companion;

    static {
        Companion = new Companion(null);
        rainbowStart = new FloatValue("Start", 0.1f, 0.0f, 1.0f);
        rainbowStop = new FloatValue("Stop", 0.2f, 0.0f, 1.0f);
        rainbowSaturation = new FloatValue("Saturation", 0.7f, 0.0f, 1.0f);
        rainbowBrightness = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        rainbowSpeed = new IntegerValue("Speed", 1500, 500, 7000);
    }

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

