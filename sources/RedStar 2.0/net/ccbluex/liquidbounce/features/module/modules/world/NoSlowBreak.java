package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoSlowBreak", description="Automatically adjusts breaking speed when using modules that influence it.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\bR0¢\b\n\u0000\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/NoSlowBreak;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getAirValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "waterValue", "getWaterValue", "Pride"})
public final class NoSlowBreak
extends Module {
    @NotNull
    private final BoolValue airValue = new BoolValue("Air", true);
    @NotNull
    private final BoolValue waterValue = new BoolValue("Water", false);

    @NotNull
    public final BoolValue getAirValue() {
        return this.airValue;
    }

    @NotNull
    public final BoolValue getWaterValue() {
        return this.waterValue;
    }
}
