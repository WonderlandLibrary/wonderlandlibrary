package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="FastPlace", description="Allows you to place blocks faster.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/FastPlace;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "speedValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getSpeedValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "Pride"})
public final class FastPlace
extends Module {
    @NotNull
    private final IntegerValue speedValue = new IntegerValue("Speed", 0, 0, 4);

    @NotNull
    public final IntegerValue getSpeedValue() {
        return this.speedValue;
    }
}
