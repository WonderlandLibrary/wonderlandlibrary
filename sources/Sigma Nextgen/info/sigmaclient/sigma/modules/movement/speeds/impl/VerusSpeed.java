package info.sigmaclient.sigma.modules.movement.speeds.impl;

import info.sigmaclient.sigma.event.player.MoveEvent;
import info.sigmaclient.sigma.modules.movement.Speed;
import info.sigmaclient.sigma.modules.movement.speeds.SpeedModule;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.potion.Effect;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class VerusSpeed extends SpeedModule {
    boolean wasSlow = false;
    double stair;
    public VerusSpeed(Speed speed) {
        super("Verus", "Speed for Verus", speed);
    }

    @Override
    public void onEnable() {
        wasSlow = false;
        stair = 0;
        super.onEnable();
    }

    @Override
    public void onMoveEvent(MoveEvent event) {
        if (!mc.player.isInWater()) {
            if(wasSlow && MovementUtils.isMoving()){
                stair -= (stair - MovementUtils.getBaseMoveSpeed()) * 0.7;
                if(mc.player.isPotionActive(Effect.get(1))){
                    stair *= 0.9;
                }
                wasSlow = false;
            }
            if (mc.player.onGround && MovementUtils.isMoving()) {
                event.setY(mc.player.getMotion().y = 0.41999998688697815);
                stair = MovementUtils.getBaseMoveSpeed() * 1.2;
                if(mc.player.isPotionActive(Effect.get(1))){
                    stair *= 0.9;
                }
                wasSlow = true;
            }
            if (stair > MovementUtils.getBaseMoveSpeed() && !wasSlow)
                stair -= stair / 156;
            double base = MovementUtils.getBaseMoveSpeed();
            if(mc.player.isPotionActive(Effect.get(1))){
                base *= 0.9;
            }
            stair = Math.max(base, stair);

            if ((mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)) {
                MovementUtils.strafing(event, stair);
            } else {
                wasSlow = false;
                mc.player.getMotion().x = 0;
                mc.player.getMotion().z = 0;
            }
        }
        super.onMoveEvent(event);
    }
}
