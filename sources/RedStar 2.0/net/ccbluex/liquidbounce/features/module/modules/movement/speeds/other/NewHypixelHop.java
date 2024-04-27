package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;

public class NewHypixelHop
extends SpeedMode {
    private boolean legitJump;
    private final MSTimer timer = new MSTimer();
    private boolean stage = false;

    public NewHypixelHop() {
        super("HypixelHop");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        if (!(NewHypixelHop.mc2.player.field_70134_J || NewHypixelHop.mc2.player.func_180799_ab() || NewHypixelHop.mc2.player.func_70090_H() || NewHypixelHop.mc2.player.func_70617_f_() || NewHypixelHop.mc2.player.field_184239_as != null)) {
            Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
            if (speed == null) {
                return;
            }
            if (!(NewHypixelHop.mc2.player.field_70134_J || NewHypixelHop.mc2.player.func_180799_ab() || NewHypixelHop.mc2.player.func_70090_H() || NewHypixelHop.mc2.player.func_70617_f_() || NewHypixelHop.mc2.player.field_184239_as != null || !MovementUtils.isMoving())) {
                NewHypixelHop.mc2.gameSettings.keyBindJump.pressed = false;
                if (NewHypixelHop.mc2.player.field_70122_E) {
                    NewHypixelHop.mc2.player.func_70664_aZ();
                    NewHypixelHop.mc2.player.field_71102_ce = 0.02f;
                    MovementUtils.strafe(0.43f);
                }
                if (this.stage) {
                    if ((double)NewHypixelHop.mc2.player.field_70143_R <= 0.1) {
                        mc.getTimer().setTimerSpeed(((Float)speed.HypixelTimerValue.get()).floatValue());
                    }
                    if (this.timer.hasTimePassed(650L)) {
                        this.timer.reset();
                        this.stage = !this.stage;
                    }
                } else {
                    if ((double)NewHypixelHop.mc2.player.field_70143_R > 0.1 && (double)NewHypixelHop.mc2.player.field_70143_R < 1.3) {
                        mc.getTimer().setTimerSpeed(((Float)speed.HypixelDealyTimerValue.get()).floatValue());
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
    public void onMove(MoveEvent event) {
    }
}
