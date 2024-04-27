package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AntiBlind", description="Cancels blindness effects.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\bR0¢\b\n\u0000\b\bR\t0¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/AntiBlind;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "confusionEffect", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getConfusionEffect", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "fireEffect", "getFireEffect", "pumpkinEffect", "getPumpkinEffect", "Pride"})
public final class AntiBlind
extends Module {
    @NotNull
    private final BoolValue confusionEffect = new BoolValue("Confusion", true);
    @NotNull
    private final BoolValue pumpkinEffect = new BoolValue("Pumpkin", true);
    @NotNull
    private final BoolValue fireEffect = new BoolValue("Fire", false);

    @NotNull
    public final BoolValue getConfusionEffect() {
        return this.confusionEffect;
    }

    @NotNull
    public final BoolValue getPumpkinEffect() {
        return this.pumpkinEffect;
    }

    @NotNull
    public final BoolValue getFireEffect() {
        return this.fireEffect;
    }
}
