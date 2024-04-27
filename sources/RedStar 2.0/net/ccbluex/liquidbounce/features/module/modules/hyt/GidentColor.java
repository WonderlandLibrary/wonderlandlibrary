package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="GidentColor", category=ModuleCategory.HYT, array=false, description="更改color")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u0000 20:B¢¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/hyt/GidentColor;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "Companion", "Pride"})
public final class GidentColor
extends Module {
    @NotNull
    private static final BoolValue blur;
    @NotNull
    private static final BoolValue Shadow;
    @NotNull
    private static final IntegerValue r;
    @NotNull
    private static final IntegerValue b;
    @NotNull
    private static final IntegerValue g;
    @NotNull
    private static final IntegerValue r2;
    @NotNull
    private static final IntegerValue b2;
    @NotNull
    private static final IntegerValue g2;
    @NotNull
    private static final IntegerValue gradientSpeed;
    public static final Companion Companion;

    static {
        Companion = new Companion(null);
        blur = new BoolValue("blur", false);
        Shadow = new BoolValue("Shadow", false);
        r = new IntegerValue("ClientRed", 255, 0, 255);
        b = new IntegerValue("ClientGreen", 0, 0, 255);
        g = new IntegerValue("ClientBlue", 60, 0, 255);
        r2 = new IntegerValue("ClientRed2", 255, 0, 255);
        b2 = new IntegerValue("ClientGreen2", 255, 0, 255);
        g2 = new IntegerValue("ClientBlue2", 255, 0, 255);
        gradientSpeed = new IntegerValue("GidentColor-Speed", 100, 10, 1000);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\b\n\n\b\b\u000020B\b¢R0¢\b\n\u0000\bR0\b¢\b\n\u0000\b\t\nR0\b¢\b\n\u0000\b\f\nR\r0¢\b\n\u0000\bR0\b¢\b\n\u0000\b\nR0\b¢\b\n\u0000\b\nR0\b¢\b\n\u0000\b\nR0\b¢\b\n\u0000\b\nR0\b¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/hyt/GidentColor$Companion;", "", "()V", "Shadow", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getShadow", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "b", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getB", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "b2", "getB2", "blur", "getBlur", "g", "getG", "g2", "getG2", "gradientSpeed", "getGradientSpeed", "r", "getR", "r2", "getR2", "Pride"})
    public static final class Companion {
        @NotNull
        public final BoolValue getBlur() {
            return blur;
        }

        @NotNull
        public final BoolValue getShadow() {
            return Shadow;
        }

        @NotNull
        public final IntegerValue getR() {
            return r;
        }

        @NotNull
        public final IntegerValue getB() {
            return b;
        }

        @NotNull
        public final IntegerValue getG() {
            return g;
        }

        @NotNull
        public final IntegerValue getR2() {
            return r2;
        }

        @NotNull
        public final IntegerValue getB2() {
            return b2;
        }

        @NotNull
        public final IntegerValue getG2() {
            return g2;
        }

        @NotNull
        public final IntegerValue getGradientSpeed() {
            return gradientSpeed;
        }

        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
