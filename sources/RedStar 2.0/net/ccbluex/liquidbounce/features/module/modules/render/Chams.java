package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Chams", description="Allows you to see targets through blocks.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\bR0¢\b\n\u0000\b\bR\t0¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Chams;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "chestsValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getChestsValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "itemsValue", "getItemsValue", "targetsValue", "getTargetsValue", "Pride"})
public final class Chams
extends Module {
    @NotNull
    private final BoolValue targetsValue = new BoolValue("Targets", true);
    @NotNull
    private final BoolValue chestsValue = new BoolValue("Chests", true);
    @NotNull
    private final BoolValue itemsValue = new BoolValue("Items", true);

    @NotNull
    public final BoolValue getTargetsValue() {
        return this.targetsValue;
    }

    @NotNull
    public final BoolValue getChestsValue() {
        return this.chestsValue;
    }

    @NotNull
    public final BoolValue getItemsValue() {
        return this.itemsValue;
    }
}
