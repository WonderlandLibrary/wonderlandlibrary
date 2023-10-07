/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="ResetVL", spacedName="Reset VL", description="Unflags you on Hypixel. (may not work as intended)", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016J\u0010\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lme/report/liquidware/modules/world/ResetVL;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "jumpAmount", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "jumped", "", "timer", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "y", "", "yMotion", "onDisable", "", "onEnable", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "KyinoClient"})
public final class ResetVL
extends Module {
    private double y;
    private int jumped;
    private final FloatValue yMotion = new FloatValue("YMotion", 0.08f, 0.05f, 0.15f);
    private final IntegerValue jumpAmount = new IntegerValue("Amount", 25, 5, 30);
    private final FloatValue timer = new FloatValue("Timer", 2.25f, 1.0f, 4.0f);

    @Override
    public void onEnable() {
        if (ResetVL.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        this.y = ResetVL.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
        this.jumped = 0;
    }

    @Override
    public void onDisable() {
        ResetVL.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.jumped <= ((Number)this.jumpAmount.get()).intValue()) {
            if (ResetVL.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                ResetVL.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.yMotion.get()).floatValue();
                int n = this.jumped;
                this.jumped = n + 1;
            }
            ResetVL.access$getMc$p$s1046033730().field_71439_g.field_70163_u = this.y;
            ResetVL.access$getMc$p$s1046033730().field_71428_T.field_74278_d = ((Number)this.timer.get()).floatValue();
        } else {
            this.setState(false);
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

