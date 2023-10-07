/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.Timer;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils2233;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007J\b\u0010\b\u001a\u00020\u0004H\u0016J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\u0004H\u0016J(\u0010\f\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/Report/Blocksmc;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "onDisable", "", "onJump", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotion", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "setMotion", "baseMoveSpeed", "", "d", "b", "", "KyinoClient"})
public final class Blocksmc
extends SpeedMode {
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (Blocksmc.access$getMc$p$s361255530().field_71439_g != null && MovementUtils.isMoving()) {
            event.cancelEvent();
        }
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onMotion() {
        EntityPlayerSP entityPlayerSP = Blocksmc.access$getMc$p$s361255530().field_71439_g;
        if (entityPlayerSP == null) {
            return;
        }
        EntityPlayerSP thePlayer = entityPlayerSP;
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        Module speedModule = module;
        Module scaffoldModule = LiquidBounce.INSTANCE.getModuleManager().getModule(Scaffold.class);
        Module timer = LiquidBounce.INSTANCE.getModuleManager().getModule(Timer.class);
        if (MovementUtils.isMoving()) {
            if (thePlayer.field_70122_E && thePlayer.field_70124_G) {
                Module module2 = scaffoldModule;
                if (module2 == null) {
                    Intrinsics.throwNpe();
                }
                thePlayer.field_70181_x = MovementUtils2233.getJumpBoostModifier(module2.getState() ? 0.41999 : 0.42, true);
                if (scaffoldModule.getState()) {
                    MovementUtils.strafe(0.37f);
                } else {
                    double d = 0.3 + (double)MovementUtils2233.getSpeedEffect() * 0.1;
                    double d2 = MovementUtils2233.getBaseMoveSpeed(0.2873);
                    boolean bl = false;
                    MovementUtils.strafe((float)Math.max(d, d2));
                }
            } else {
                Module module3 = timer;
                if (module3 == null) {
                    Intrinsics.throwNpe();
                }
                if (!module3.getState()) {
                    // empty if block
                }
                MovementUtils2233.setMotion(MovementUtils2233.getSpeed(), true);
            }
        }
    }

    @Override
    public void onDisable() {
        Module scaffoldModule = LiquidBounce.INSTANCE.getModuleManager().getModule(Scaffold.class);
        EntityPlayerSP entityPlayerSP = Blocksmc.access$getMc$p$s361255530().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (!entityPlayerSP.func_70093_af()) {
            Module module = scaffoldModule;
            if (module == null) {
                Intrinsics.throwNpe();
            }
            if (!module.getState()) {
                MovementUtils.strafe(0.2f);
            }
        }
    }

    @Override
    public void setMotion(@NotNull MoveEvent event, double baseMoveSpeed, double d, boolean b) {
        Intrinsics.checkParameterIsNotNull(event, "event");
    }

    @Override
    public void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        EntityPlayerSP entityPlayerSP = Blocksmc.access$getMc$p$s361255530().field_71439_g;
        if (entityPlayerSP == null) {
            return;
        }
        EntityPlayerSP thePlayer = entityPlayerSP;
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        Module speedModule = module;
        if (MovementUtils.isMoving()) {
            if (thePlayer.field_70123_F) {
                this.setMotion(event, MovementUtils2233.getBaseMoveSpeed(0.258), 1.0, true);
            }
        }
    }

    public Blocksmc() {
        super("Blocksmc");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s361255530() {
        return SpeedMode.mc;
    }
}

