package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="EnchantEffect", description="Custom EnchantColor", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\n\n\b\b\u000020B¢J\b\n0J\b0J\b\f0\tJ\b\r0J\b0R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/EnchantEffect;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "alphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "colorBlueValue", "colorGreenValue", "colorRedValue", "rainbow", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getBlueValue", "getGreenValue", "getRainbow", "getRedValue", "getalphaValue", "Pride"})
public final class EnchantEffect
extends Module {
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final IntegerValue alphaValue = new IntegerValue("Alpha", 255, 0, 255);
    private final BoolValue rainbow = new BoolValue("RainBow", false);

    @Nullable
    public final IntegerValue getRedValue() {
        return this.colorRedValue;
    }

    @Nullable
    public final BoolValue getRainbow() {
        return this.rainbow;
    }

    @Nullable
    public final IntegerValue getGreenValue() {
        return this.colorGreenValue;
    }

    @Nullable
    public final IntegerValue getBlueValue() {
        return this.colorBlueValue;
    }

    @Nullable
    public final IntegerValue getalphaValue() {
        return this.alphaValue;
    }
}
