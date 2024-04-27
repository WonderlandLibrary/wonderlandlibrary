package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoSwing", description="Disabled swing effect when hitting an entity/mining a block.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/NoSwing;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "serverSideValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getServerSideValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "Pride"})
public final class NoSwing
extends Module {
    @NotNull
    private final BoolValue serverSideValue = new BoolValue("ServerSide", true);

    @NotNull
    public final BoolValue getServerSideValue() {
        return this.serverSideValue;
    }
}
