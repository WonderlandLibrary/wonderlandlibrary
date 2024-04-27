package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="HitBox", description="Makes hitboxes of targets bigger.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/HitBox;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "sizeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getSizeValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "Pride"})
public final class HitBox
extends Module {
    @NotNull
    private final FloatValue sizeValue = new FloatValue("Size", 0.4f, 0.0f, 1.0f);

    @NotNull
    public final FloatValue getSizeValue() {
        return this.sizeValue;
    }
}
