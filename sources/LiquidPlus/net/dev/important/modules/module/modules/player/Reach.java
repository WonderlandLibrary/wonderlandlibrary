/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.player;

import kotlin.Metadata;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.FloatValue;
import org.jetbrains.annotations.NotNull;

@Info(name="Reach", description="Increases your reach.", category=Category.PLAYER, cnName="\u589e\u52a0\u653b\u51fb\u8ddd\u79bb")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\r"}, d2={"Lnet/dev/important/modules/module/modules/player/Reach;", "Lnet/dev/important/modules/module/Module;", "()V", "buildReachValue", "Lnet/dev/important/value/FloatValue;", "getBuildReachValue", "()Lnet/dev/important/value/FloatValue;", "combatReachValue", "getCombatReachValue", "maxRange", "", "getMaxRange", "()F", "LiquidBounce"})
public final class Reach
extends Module {
    @NotNull
    private final FloatValue combatReachValue = new FloatValue("CombatReach", 3.5f, 3.0f, 7.0f, "m");
    @NotNull
    private final FloatValue buildReachValue = new FloatValue("BuildReach", 5.0f, 4.5f, 7.0f, "m");

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

