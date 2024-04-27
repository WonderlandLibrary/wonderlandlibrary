/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="Gident", description="Custom", category=ModuleCategory.RENDER)
public final class Gident
extends Module {
    @JvmField
    public static final IntegerValue gidentspeed;
    @JvmField
    public static final IntegerValue redValue;
    @JvmField
    public static final IntegerValue greenValue;
    @JvmField
    public static final IntegerValue blueValue;
    @JvmField
    public static final IntegerValue redValue2;
    @JvmField
    public static final IntegerValue greenValue2;
    @JvmField
    public static final IntegerValue blueValue2;
    public static final Companion Companion;

    static {
        Companion = new Companion(null);
        gidentspeed = new IntegerValue("GidentSpeed", 100, 1, 1000);
        redValue = new IntegerValue("Red", 255, 0, 255);
        greenValue = new IntegerValue("Green", 255, 0, 255);
        blueValue = new IntegerValue("Blue", 255, 0, 255);
        redValue2 = new IntegerValue("Red2", 255, 0, 255);
        greenValue2 = new IntegerValue("Green2", 255, 0, 255);
        blueValue2 = new IntegerValue("Blue2", 255, 0, 255);
    }

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

