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
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="CustomUI", description="Custom", category=ModuleCategory.RENDER)
public final class CustomUI
extends Module {
    @JvmField
    public static final IntegerValue r;
    @JvmField
    public static final IntegerValue g;
    @JvmField
    public static final IntegerValue b;
    @JvmField
    public static final IntegerValue r2;
    @JvmField
    public static final IntegerValue g2;
    @JvmField
    public static final IntegerValue b2;
    @JvmField
    public static final IntegerValue a;
    @JvmField
    public static final FloatValue radius;
    @JvmField
    public static final FloatValue outlinet;
    @JvmField
    public static final ListValue drawMode;
    @JvmField
    public static BoolValue Chinese;
    public static final Companion Companion;

    static {
        Companion = new Companion(null);
        r = new IntegerValue("\u7ea2\u8272", 39, 0, 255);
        g = new IntegerValue("\u7eff\u8272", 120, 0, 255);
        b = new IntegerValue("\u84dd\u8272", 186, 0, 255);
        r2 = new IntegerValue("\u7ea2\u82722", 20, 0, 255);
        g2 = new IntegerValue("\u7eff\u82722", 50, 0, 255);
        b2 = new IntegerValue("\u84dd\u82722", 80, 0, 255);
        a = new IntegerValue("\u900f\u660e\u5ea6", 180, 0, 255);
        radius = new FloatValue("\u5706\u89d2\u5927\u5c0f", 3.0f, 0.0f, 10.0f);
        outlinet = new FloatValue("\u63cf\u8fb9\u5927\u5c0f", 0.4f, 0.0f, 5.0f);
        drawMode = new ListValue("\u7ed8\u5236\u6a21\u5f0f", new String[]{"\u5706\u89d2\u77e9\u5f62", "\u63cf\u8fb9\u548c\u5706\u89d2\u77e9\u5f62"}, "\u5706\u89d2\u77e9\u5f62");
        Chinese = new BoolValue("\u5168\u5c40\u4e2d\u6587", true);
    }

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

