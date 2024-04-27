package net.ccbluex.liquidbounce.features.module.modules.color;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Gident", description="没用", category=ModuleCategory.COLOR)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u0000 20:B¢¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/color/Gident;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "Companion", "Pride"})
public final class Gident
extends Module {
    @JvmField
    @NotNull
    public static final IntegerValue gidentspeed;
    @JvmField
    @NotNull
    public static final IntegerValue redValue;
    @JvmField
    @NotNull
    public static final IntegerValue greenValue;
    @JvmField
    @NotNull
    public static final IntegerValue blueValue;
    @JvmField
    @NotNull
    public static final IntegerValue redValue2;
    @JvmField
    @NotNull
    public static final IntegerValue greenValue2;
    @JvmField
    @NotNull
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\b\b\u000020B\b¢R08X¢\n\u0000R08X¢\n\u0000R08X¢\n\u0000R08X¢\n\u0000R\b08X¢\n\u0000R\t08X¢\n\u0000R\n08X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/color/Gident$Companion;", "", "()V", "blueValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "blueValue2", "gidentspeed", "greenValue", "greenValue2", "redValue", "redValue2", "Pride"})
    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
