package net.ccbluex.liquidbounce.features.module.modules.color;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Rainbow", category=ModuleCategory.COLOR, canEnable=false, description="Custom")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u0000 20:B¢¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/color/Rainbow;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "Companion", "Pride"})
public final class Rainbow
extends Module {
    @JvmField
    @NotNull
    public static final FloatValue rainbowStart;
    @JvmField
    @NotNull
    public static final FloatValue rainbowStop;
    @JvmField
    @NotNull
    public static final FloatValue rainbowSaturation;
    @JvmField
    @NotNull
    public static final FloatValue rainbowBrightness;
    @JvmField
    @NotNull
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\b\n\n\b\n\n\b\b\u000020B\b¢R08X¢\n\u0000R08X¢\n\u0000R08X¢\n\u0000R\b08X¢\n\u0000R\t08X¢\n\u0000¨\n"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/color/Rainbow$Companion;", "", "()V", "rainbowBrightness", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rainbowSaturation", "rainbowSpeed", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "rainbowStart", "rainbowStop", "Pride"})
    public static final class Companion {
        private Companion() {
        }

        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
