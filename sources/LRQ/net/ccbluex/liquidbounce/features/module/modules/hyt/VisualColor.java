/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.DefaultConstructorMarker
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="VisualColor", category=ModuleCategory.HYT, array=false, description="nmsl")
public final class VisualColor
extends Module {
    private static final BoolValue blur;
    private static final BoolValue Shadow;
    private static final IntegerValue r;
    private static final IntegerValue b;
    private static final IntegerValue g;
    private static final IntegerValue r2;
    private static final IntegerValue b2;
    private static final IntegerValue g2;
    private static final IntegerValue gradientSpeed;
    public static final Companion Companion;

    static {
        Companion = new Companion(null);
        blur = new BoolValue("blur", false);
        Shadow = new BoolValue("Shadow", false);
        r = new IntegerValue("ClientRed", 0, 0, 255);
        b = new IntegerValue("ClientGreen", 255, 0, 255);
        g = new IntegerValue("ClientBlue", 255, 0, 255);
        r2 = new IntegerValue("ClientRed2", 255, 0, 255);
        b2 = new IntegerValue("ClientGreen2", 40, 0, 255);
        g2 = new IntegerValue("ClientBlue2", 255, 0, 255);
        gradientSpeed = new IntegerValue("DoubleColor-Speed", 100, 10, 1000);
    }

    public static final class Companion {
        public final BoolValue getBlur() {
            return blur;
        }

        public final BoolValue getShadow() {
            return Shadow;
        }

        public final IntegerValue getR() {
            return r;
        }

        public final IntegerValue getB() {
            return b;
        }

        public final IntegerValue getG() {
            return g;
        }

        public final IntegerValue getR2() {
            return r2;
        }

        public final IntegerValue getB2() {
            return b2;
        }

        public final IntegerValue getG2() {
            return g2;
        }

        public final IntegerValue getGradientSpeed() {
            return gradientSpeed;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

