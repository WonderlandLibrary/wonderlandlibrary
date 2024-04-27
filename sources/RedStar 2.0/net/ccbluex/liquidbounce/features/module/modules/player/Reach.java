package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Reach", description="Increases your reach.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\n\n\b\b\u000020B¢R0¢\b\n\u0000\bR0¢\b\n\u0000\b\bR\t0\n8F¢\b\f¨\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/Reach;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "buildReachValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getBuildReachValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "combatReachValue", "getCombatReachValue", "maxRange", "", "getMaxRange", "()F", "Pride"})
public final class Reach
extends Module {
    @NotNull
    private final FloatValue combatReachValue = new FloatValue("CombatReach", 3.5f, 3.0f, 7.0f);
    @NotNull
    private final FloatValue buildReachValue = new FloatValue("BuildReach", 5.0f, 4.5f, 7.0f);

    @NotNull
    public final FloatValue getCombatReachValue() {
        return this.combatReachValue;
    }

    @NotNull
    public final FloatValue getBuildReachValue() {
        return this.buildReachValue;
    }

    public final float getMaxRange() {
        float buildRange;
        float combatRange = ((Number)this.combatReachValue.get()).floatValue();
        return combatRange > (buildRange = ((Number)this.buildReachValue.get()).floatValue()) ? combatRange : buildRange;
    }
}
