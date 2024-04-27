package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="TrueSight", description="Allows you to see invisible entities and barriers.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\bR0¢\b\n\u0000\b\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/TrueSight;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "barriersValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getBarriersValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "entitiesValue", "getEntitiesValue", "Pride"})
public final class TrueSight
extends Module {
    @NotNull
    private final BoolValue barriersValue = new BoolValue("Barriers", true);
    @NotNull
    private final BoolValue entitiesValue = new BoolValue("Entities", true);

    @NotNull
    public final BoolValue getBarriersValue() {
        return this.barriersValue;
    }

    @NotNull
    public final BoolValue getEntitiesValue() {
        return this.entitiesValue;
    }
}
