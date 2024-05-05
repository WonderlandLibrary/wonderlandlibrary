package io.github.raze.modules.collection.movement.speed.impl;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.collection.network.EventPacketSend;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.movement.speed.ModeSpeed;
import io.github.raze.utilities.collection.visual.ChatUtil;
import io.github.raze.utilities.collection.world.MoveUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class VulcanSpeed extends ModeSpeed {

    public VulcanSpeed() {
        super("Vulcan");
    }

    private int ticks;
    private double y;

    @Listen
    public void onMotion(EventMotion event) {
        if (!MoveUtil.isMoving())
            return;

        if (event.getState() == Event.State.PRE) {
            mc.thePlayer.setSprinting(MoveUtil.isMoving());

            mc.gameSettings.keyBindJump.pressed = false;
            if (mc.thePlayer.onGround) {
                ticks = 0;
            } else {
                ticks++;
            }

            switch (parent.vulcanMode.get()) {
                case "BHop":
                    switch (ticks) {
                        case 0:
                            mc.thePlayer.jump();

                            MoveUtil.strafe(mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.6F : 0.485F);
                            break;

                        case 1:
                        case 2:
                            MoveUtil.strafe();
                            break;

                        case 5:
                            mc.thePlayer.motionY = MoveUtil.predictedMotion(mc.thePlayer.motionY, 2);
                            break;
                        case 10:
                            MoveUtil.strafe((float) (MoveUtil.getSpeed() * 0.8));
                    }
                    break;
                case "BHop 2":
                    if(mc.thePlayer.onGround) {
                        ticks = 0;
                     } else {
                        ticks++;
                    }
                    switch(ticks) {
                        case 0:
                            mc.thePlayer.jump();

                            MoveUtil.strafe(mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.6F : 0.485F);
                            break;
                        case 1:
                        case 2:
                        case 8:
                            MoveUtil.strafe();
                            break;
                        case 5:
                            mc.thePlayer.motionY = MoveUtil.predictedMotion(mc.thePlayer.motionY, 6);
                            break;
                    }
                    break;
                case "Low":
                    switch (ticks) {
                        case 0:
                            mc.thePlayer.jump();

                            MoveUtil.strafe(mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.53F : 0.4F);
                            break;

                        case 2:
                        case 1:
                            MoveUtil.strafe();
                            break;
                        case 4:
                            mc.thePlayer.motionY = MoveUtil.predictedMotion(mc.thePlayer.motionY, 3);
                            break;
                        case 7:
                            mc.thePlayer.motionY = -1337;
                            break;
                    }
                    break;
                case "Funny":
                    if (mc.thePlayer.onGround) {
                        if(parent.vulcanTimer.get()) {
                            mc.timer.timerSpeed = 1.02F;
                        }
                        y = 0.01;
                        mc.thePlayer.motionY = 0.01;
                        MoveUtil.strafe((float) (0.4175 + MoveUtil.getSpeedBoost(1.5F)));
                    } else {
                        if(parent.vulcanTimer.get()) {
                            mc.timer.timerSpeed = 1;
                        }
                        if (y == 0.01) {
                             MoveUtil.strafe(mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtil.getBaseMoveSpeed() * 1.11F : MoveUtil.getBaseMoveSpeed() * 1.04F);
                            y = 0;
                        }

                        if(mc.thePlayer.ticksExisted % 10 == 0) {

                            mc.thePlayer.motionX *= 1.00575;
                            mc.thePlayer.motionZ *= 1.00575;

                            MoveUtil.strafe((float) (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtil.getSpeed() * 1.04 : MoveUtil.getSpeed()));

                            if(parent.vulcanTimer.get()) {
                                mc.timer.timerSpeed = 1.6F;
                            }

                        }
                        break;
                    }
            }
        }
    }

    @Listen
    public void onPacketSend(EventPacketSend event) {
        if(event.getPacket() instanceof C03PacketPlayer) {
            ((C03PacketPlayer) event.getPacket()).y = mc.thePlayer.posY + y;
        }
    }


    @Listen
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }

}
