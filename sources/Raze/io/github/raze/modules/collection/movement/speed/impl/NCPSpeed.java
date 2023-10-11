package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.visual.ChatUtil;
import io.github.raze.utilities.collection.world.MoveUtil;

public class NCPSpeed extends ModeSpeed {

    public NCPSpeed() { super("NCP"); }

    private boolean ncpLowMessageSaid, ncpFastMessageSaid;

    @Override
    public void onEnable() {
        if(parent.ncpMode.compare("Low") && !ncpLowMessageSaid) {
            ChatUtil.addChatMessage("Note! This mode only works on some NCP servers.", true);
            ncpLowMessageSaid = true;
        }

        if(parent.ncpMode.compare("Fast") && !ncpFastMessageSaid) {
            ChatUtil.addChatMessage("Note! This mode will flag.", true);
            ncpFastMessageSaid = true;
        }
        super.onEnable();
    }

    @Listen
    public void onMotionEvent(EventMotion event) {
        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            switch (parent.ncpMode.get()) {
                case "Normal":
                    if(parent.ncpTimer.get()) {
                        if(mc.thePlayer.fallDistance > 0.75) {
                            mc.timer.timerSpeed = 1.23F;
                        } else {
                            mc.timer.timerSpeed = 1;
                        }
                    }
                    if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                        mc.thePlayer.jump();
                        MoveUtil.strafe((float) (0.433 + MoveUtil.getSpeedBoost(0.6F) + Math.random() / 40 + mc.thePlayer.moveForward / 30));
                    } else if(MoveUtil.isMoving()) {
                        MoveUtil.strafe((float) MoveUtil.getSpeed());
                    }
                    break;

                case "Slow":
                    mc.gameSettings.keyBindJump.pressed = MoveUtil.isMoving();

                    MoveUtil.strafe((float) MoveUtil.getSpeed());
                    break;

                case "Fast":
                    if(parent.ncpTimer.get()) {
                        if(mc.thePlayer.fallDistance > 0.75) {
                            mc.timer.timerSpeed = 1.33F;
                        } else {
                            mc.timer.timerSpeed = 1.03F;
                        }
                    }
                    if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                        mc.thePlayer.jump();
                        MoveUtil.strafe((float) (0.45 + MoveUtil.getSpeedBoost(0.7F) + Math.random() / 30 + mc.thePlayer.moveForward / 30));
                    } else if(MoveUtil.isMoving()) {
                        MoveUtil.strafe((float) MoveUtil.getSpeed());
                    }

                    if(MoveUtil.getSpeed() < 0.265) {
                        MoveUtil.strafe(0.265F);
                    }
                    break;

                case "Stable":
                    mc.gameSettings.keyBindJump.pressed = MoveUtil.isMoving();

                    mc.thePlayer.setSprinting(false);

                    if(parent.ncpTimer.get()) {
                        if(mc.thePlayer.onGround) {
                            mc.timer.timerSpeed = 1.9F;
                        } else {
                            mc.timer.timerSpeed = (float) (1 + Math.random() / 5);
                        }
                    }

                    if(mc.thePlayer.onGround) {
                        MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() + MoveUtil.getSpeedBoost(1.3F));
                    } else {
                        MoveUtil.strafe((float) (0.265 + MoveUtil.getSpeedBoost(1)));
                    }
                    break;
            }
        }
        if (event.getState() == Event.State.POST) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            if (parent.ncpMode.compare("Low")) {
                if (mc.gameSettings.keyBindJump.pressed)
                    return;

                if (mc.thePlayer.onGround) {
                    MoveUtil.strafe((float) (0.33 + MoveUtil.getSpeedBoost(0.6F) + Math.random() / 100));
                    mc.thePlayer.motionY = 0.1;
                    event.setY(0.42);
                } else {
                    MoveUtil.strafe((float) MoveUtil.getSpeed());
                }
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
