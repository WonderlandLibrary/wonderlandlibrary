/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Reach", description="Increases your reach.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/Reach;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "buildReachValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getBuildReachValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "combatReachValue", "getCombatReachValue", "maxRange", "", "getMaxRange", "()F", "LiKingSense"})
public final class Reach
extends Module {
    @NotNull
    public final FloatValue combatReachValue = new FloatValue("CombatReach", 3.5f, 3.0f, 7.0f);
    @NotNull
    public final FloatValue buildReachValue = new FloatValue("BuildReach", 5.0f, 4.5f, 7.0f);

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

