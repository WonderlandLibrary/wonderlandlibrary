/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\u0006H\u0016J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u0006H\u0016J\b\u0010\r\u001a\u00020\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/aac/AAC4BHop;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "legitHop", "", "onDisable", "", "onEnable", "onMotion", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onTick", "onUpdate", "LiKingSense"})
public final class AAC4BHop
extends SpeedMode {
    public boolean legitHop;

    @Override
    public void onDisable() {
        MinecraftInstance.mc.getThePlayer().setSpeedInAir(0.02f);
    }

    @Override
    public void onTick() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MovementUtils.isMoving()) {
            if (this.legitHop) {
                if (thePlayer.getOnGround()) {
                    thePlayer.jump();
                    thePlayer.setOnGround(false);
                    this.legitHop = 0;
                }
                return;
            }
            if (thePlayer.getOnGround()) {
                thePlayer.setOnGround(false);
                MovementUtils.strafe(0.375f);
                thePlayer.jump();
                thePlayer.setMotionY(0.41);
            } else {
                thePlayer.setSpeedInAir(0.0211f);
            }
        } else {
            thePlayer.setMotionX(0.0);
            thePlayer.setMotionZ(0.0);
            this.legitHop = 1;
        }
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
    }

    @Override
    public void onEnable() {
        this.legitHop = 1;
    }

    public AAC4BHop() {
        super("AAC4BHop");
    }
}

