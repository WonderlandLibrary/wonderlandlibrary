package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoFOV", description="Disables FOV changes caused by speed effect, etc.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/NoFOV;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "fovValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getFovValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "Pride"})
public final class NoFOV
extends Module {
    @NotNull
    private final FloatValue fovValue = new FloatValue("FOV", 1.0f, 0.0f, 1.5f);

    @NotNull
    public final FloatValue getFovValue() {
        return this.fovValue;
    }
}
