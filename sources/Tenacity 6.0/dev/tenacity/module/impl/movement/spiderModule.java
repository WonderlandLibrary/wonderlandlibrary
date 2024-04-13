package dev.tenacity.module.impl.movement;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import net.minecraft.network.Packet;
import net.minecraft.util.Timer;
import dev.tenacity.util.misc.TimerUtil;

import java.util.ArrayList;
import java.util.List;

public final class spiderModule extends Module {

    private final TimerUtil timerUtil = new TimerUtil();

    private final List<Packet<?>> packetList = new ArrayList<>();

    private float lastMovementYaw;

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "HvH Mode");

    public spiderModule() {
        super("Spider", "Makes you spoderman", ModuleCategory.MOVEMENT);
        initializeSettings(mode);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if (event.isPre()) {
            switch (mode.getCurrentMode()) {
                case "Vanilla": {
                    if (mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.motionY = 0.42;
                    }
                    break;
                }

                case "HvH Mode": {
                    if (mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.motionY = 9;
                    }
                    break;

//                }
//                case "Mineland": {
//                    if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
//                        mc.thePlayer.jump();
//                        MovementUtil.setSpeed(0.46);
//                        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//                            MovementUtil.setSpeed(0.1);
//                        }
//                    }
//                }
//                case "Verus": {
//                    if (Keyboard.isKeyDown(KEY_SPACE)) {
//                        MovementUtil.setSpeed(0.2);
//                    }
//                    if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
//                        mc.thePlayer.motionY = 0.42f;
//                        MovementUtil.setSpeed(0.3);
//                    }
//                    break;
//                }
//                case "BlocksMC": {
//                    if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
//                        if (mc.thePlayer.onGround)
//                            mc.thePlayer.jump();
//                        MovementUtil.setSpeed(0.35);
//                        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//                            toggle();
//                        }
//                        break;
//                    }
//                }
//
//                case "Negativity": {
//                    if (MovementUtil.isMoving()) {
//                        if (Keyboard.isKeyDown(KEY_W) || Keyboard.isKeyDown(KEY_S) || Keyboard.isKeyDown(KEY_A) || Keyboard.isKeyDown(KEY_D) && mc.thePlayer.isAirBorne) {
//                            mc.thePlayer.motionY = 0;
//                            mc.thePlayer.posY = mc.thePlayer.posY + 0.3;
//                            MovementUtil.setSpeed(0.5);
//                            if (timerUtil.hasTimeElapsed(2000, true)) {
//                                mc.thePlayer.motionY = -1000;
//                                mc.thePlayer.posY = mc.thePlayer.posY - 0.3;
//                                MovementUtil.setSpeed(0.5);
//                            }
//                            if (Keyboard.isKeyDown(KEY_SPACE)) {
//                                ChatUtil.error("Disabling due to illegal key press!");
//                                MovementUtil.setSpeed(0);
//                                toggle();
//                            }
//                        }
//                        break;
                }
            }
        }
    };
    @Override
    public void onEnable() {
        packetList.clear();
        lastMovementYaw = -1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if(!packetList.isEmpty()) {
            packetList.forEach(mc.getNetHandler()::addToSendQueueNoEvent);
            packetList.clear();
        }
        Timer.timerSpeed = 1;
        super.onDisable();
    }
}
