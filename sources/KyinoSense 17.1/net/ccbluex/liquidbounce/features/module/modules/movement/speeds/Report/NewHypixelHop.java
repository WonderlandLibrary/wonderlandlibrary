/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import obfuscator.NativeMethod;

public class NewHypixelHop
extends SpeedMode {
    private boolean legitJump;
    private final MSTimer timer = new MSTimer();
    private boolean stage = false;

    public NewHypixelHop() {
        super("NewHypixelHop");
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onEnable() {
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate() {
        if (!(NewHypixelHop.mc.field_71439_g.field_70134_J || NewHypixelHop.mc.field_71439_g.func_180799_ab() || NewHypixelHop.mc.field_71439_g.func_70090_H() || NewHypixelHop.mc.field_71439_g.func_70617_f_() || NewHypixelHop.mc.field_71439_g.field_70154_o != null)) {
            Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
            if (speed == null) {
                return;
            }
            if (!(NewHypixelHop.mc.field_71439_g.field_70134_J || NewHypixelHop.mc.field_71439_g.func_180799_ab() || NewHypixelHop.mc.field_71439_g.func_70090_H() || NewHypixelHop.mc.field_71439_g.func_70617_f_() || NewHypixelHop.mc.field_71439_g.field_70154_o != null || !MovementUtils.isMoving())) {
                NewHypixelHop.mc.field_71474_y.field_74314_A.field_74513_e = false;
                if (NewHypixelHop.mc.field_71439_g.field_70122_E) {
                    NewHypixelHop.mc.field_71439_g.func_70664_aZ();
                    NewHypixelHop.mc.field_71439_g.field_71102_ce = 0.02f;
                    MovementUtils.strafe(0.43f);
                }
                if (this.stage) {
                    if ((double)NewHypixelHop.mc.field_71439_g.field_70143_R <= 0.1) {
                        NewHypixelHop.mc.field_71428_T.field_74278_d = ((Float)speed.HypixelTimerValue.get()).floatValue();
                    }
                    if (this.timer.hasTimePassed(650L)) {
                        this.timer.reset();
                        this.stage = !this.stage;
                    }
                } else {
                    if ((double)NewHypixelHop.mc.field_71439_g.field_70143_R > 0.1 && (double)NewHypixelHop.mc.field_71439_g.field_70143_R < 1.3) {
                        NewHypixelHop.mc.field_71428_T.field_74278_d = ((Float)speed.HypixelDealyTimerValue.get()).floatValue();
                    }
                    if (this.timer.hasTimePassed(400L)) {
                        this.timer.reset();
                        this.stage = !this.stage;
                    }
                }
                MovementUtils.strafe();
            }
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMove(MoveEvent event) {
    }
}

