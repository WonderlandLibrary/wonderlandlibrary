/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.StepEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\bH\u0016J\u0012\u0010\n\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J\u0010\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007J\b\u0010\u0010\u001a\u00020\bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/Report/BlocksmcTimer;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "isStep", "", "upTimerValue", "wasTimer", "onEnable", "", "onMotion", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onStep", "e", "Lnet/ccbluex/liquidbounce/event/StepEvent;", "onUpdate", "KyinoClient"})
public abstract class BlocksmcTimer
extends SpeedMode {
    private boolean wasTimer;
    private boolean isStep;
    private boolean upTimerValue = true;

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        if (this.wasTimer) {
            BlocksmcTimer.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.2f;
            this.wasTimer = false;
        }
        if (MovementUtils.isMoving()) {
            if (this.isStep) {
                this.isStep = false;
                return;
            }
            if (BlocksmcTimer.access$getMc$p$s361255530().field_71439_g.field_70122_E) {
                BlocksmcTimer.access$getMc$p$s361255530().field_71439_g.func_70664_aZ();
                if (!BlocksmcTimer.access$getMc$p$s361255530().field_71439_g.field_70160_al) {
                    return;
                }
                if (this.upTimerValue) {
                    BlocksmcTimer.access$getMc$p$s361255530().field_71428_T.field_74278_d = 2.5f;
                }
                this.wasTimer = true;
                MovementUtils.strafe(0.4848f);
            }
            MovementUtils.strafe();
        }
    }

    @Override
    public void onMove(@Nullable MoveEvent event) {
    }

    @Override
    public void onEnable() {
        BlocksmcTimer.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.0f;
        this.isStep = false;
        this.wasTimer = false;
    }

    @EventTarget
    public final void onStep(@NotNull StepEvent e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        this.isStep = true;
    }

    public BlocksmcTimer() {
        super("OldBlocksmcTimer");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s361255530() {
        return SpeedMode.mc;
    }
}

