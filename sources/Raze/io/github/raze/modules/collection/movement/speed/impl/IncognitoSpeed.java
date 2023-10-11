package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.world.MoveUtil;

public class IncognitoSpeed extends ModeSpeed {

    public IncognitoSpeed() { super("Incognito"); }

    @Listen
    public void onMotion(EventMotion event) {
        if(event.getState() == Event.State.POST) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            switch (parent.incognitoMode.get()) {
                case "Normal":
                    mc.gameSettings.keyBindJump.pressed = MoveUtil.isMoving();

                    if(mc.thePlayer.onGround) {
                        MoveUtil.strafe((float) (0.36 + Math.random() / 70 + MoveUtil.getSpeedBoost(1)));
                    } else {
                        MoveUtil.strafe((float) (MoveUtil.getSpeed() - (float) (Math.random() - 0.5F) / 70F));
                    }

                    if(MoveUtil.getSpeed() < 0.25F) {
                        MoveUtil.strafe((float) (MoveUtil.getSpeed() + 0.02));
                    }
                    break;
                case "Exploit":
                    mc.gameSettings.keyBindJump.pressed = MoveUtil.isMoving();

                    float speed = (float) (Math.random() * 2.5);
                    if(0.4 > speed) {
                        speed = 1;
                    }

                    if(mc.thePlayer.onGround) {
                        mc.timer.timerSpeed = (float) (speed + Math.random() / 2);
                        MoveUtil.strafe((float) (0.36 + Math.random() / 70 + MoveUtil.getSpeedBoost(1)));
                    } else {
                        mc.timer.timerSpeed = speed;
                        MoveUtil.strafe((float) (MoveUtil.getSpeed() - (float) (Math.random() - 0.5F) / 70F));
                    }

                    if(MoveUtil.getSpeed() < 0.25F) {
                        MoveUtil.strafe((float) (MoveUtil.getSpeed() + 0.02));
                    }
                    break;
            }
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.gameSettings.keyBindJump.pressed = false;
        super.onDisable();
    }
}
