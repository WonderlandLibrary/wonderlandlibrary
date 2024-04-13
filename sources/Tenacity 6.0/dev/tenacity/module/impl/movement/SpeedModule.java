package dev.tenacity.module.impl.movement;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.TimerUtil;
import dev.tenacity.util.player.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.input.Keyboard.*;

public final class SpeedModule extends Module {

    private boolean damage;
    private boolean strafe, wasOnGround;
    private final TimerUtil timerUtil = new TimerUtil();

    private final List<Packet<?>> packetList = new ArrayList<>();

    private double speed;
    private boolean downmotioncustominit = false;
    private float lastMovementYaw;

    //  public final NumberSetting cs = new NumberSetting("Speed:", 1, 0.1, 100, 0.1);
    public final NumberSetting speedcustom = new NumberSetting("Speed:", 0.5, 0.1, 10, 0.1);
    public final NumberSetting ymotioncustom = new NumberSetting("Y Motion:", 0.42, 0, 10, 0.1);
    public final NumberSetting downmotioncustom = new NumberSetting("Down motion:", -0.7543454, -0.1, -10, 0.1);
    public final NumberSetting timercustom = new NumberSetting("Timer", 1, 0.1, 10, 0.1);
    public final BooleanSetting downmotiontoggle = new BooleanSetting("Down Motion", false);
    public final NumberSetting ncpboost = new NumberSetting("Boost:", 0.5, 0.1, 0.65, 0.05);
    public final NumberSetting verusboost = new NumberSetting("Boost:", 1, 0.1, 10, 0.1);
    public final BooleanSetting verusboostbool = new BooleanSetting("Use boost", true);
    private final ModeSetting verus = new ModeSetting("Type", "Strafe", "Funny");
    private final ModeSetting vulcan = new ModeSetting("Type", "Semi Strafe", "Ground");
    private final ModeSetting mode = new ModeSetting("Mode", "Watchdog Semi Strafe", "HvH Mode", "BlocksMC", "Verus", "Vulcan", "Negativity", "NCP", "Grim", "Legit", "Custom");

    public SpeedModule() {
        super("Speed", "Makes you move faster", ModuleCategory.MOVEMENT);
        downmotioncustom.addParent(mode, modeSetting -> mode.isMode("Custom"));
        speedcustom.addParent(mode, modeSetting -> mode.isMode("Custom"));
        downmotiontoggle.addParent(mode, modeSetting -> mode.isMode("Custom"));
        ymotioncustom.addParent(mode, modeSetting -> mode.isMode("Custom"));
        timercustom.addParent(mode, modeSetting -> mode.isMode("Custom"));
        verus.addParent(mode, modeSetting -> mode.isMode("Verus"));
        vulcan.addParent(mode, modeSetting -> mode.isMode("Vulcan"));
        ncpboost.addParent(mode, modeSetting -> mode.isMode("NCP"));
        verusboost.addParent(mode, modeSetting -> mode.isMode("Verus"));
        verusboostbool.addParent(mode, modeSetting -> mode.isMode("Verus"));
        initializeSettings(mode, verus, vulcan, ncpboost, verusboost, speedcustom, downmotiontoggle, ymotioncustom, timercustom);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if (event.isPre()) {
            switch (mode.getCurrentMode()) {
                case "Watchdog Semi Strafe": {
                    if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                        mc.thePlayer.jump();
                        MovementUtil.setSpeed(MovementUtil.getAllowedHDistNCP());
                    }
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        MovementUtil.setSpeed(0.1);
                    }
                    break;
                }

                case "Vulcan": {
                    switch (vulcan.getCurrentMode()) {
                        case "Semi Strafe": {
                            if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                                MovementUtil.setSpeed(MovementUtil.getAllowedHDistNCP());
                            }
                            if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                                mc.thePlayer.motionY = 0.42f;
                            } else if (mc.thePlayer.hurtTime == 9) {
                                MovementUtil.setSpeed(MovementUtil.getSpeed());
                            }
                            if (Keyboard.isKeyDown(KEY_SPACE)) {
                                ChatUtil.error("Fatal error: Spacer pressed: Code 23");
                                toggle();
                            }
                            break;
                        }
                        case "Ground": {

                            break;
                        }
                    }
                    break;
                }

                case "Legit": {
                    if (MovementUtil.isMoving() && mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        return;
                    }
                    break;
                }

                case "Verus": {
                    switch (verus.getCurrentMode()) {
                        case "Strafe": {
                            if (event.isPre()) {
                                mc.thePlayer.cameraYaw = 0f;
                                if (mc.thePlayer.hurtTime >= 1) {
                                    MovementUtil.setSpeed((float) verusboost.getCurrentValue() + MovementUtil.getSpeed());
                                }
                                if (MovementUtil.isMoving()) {
                                    if (mc.thePlayer.onGround) {
                                        mc.thePlayer.motionY = 0.42;
                                        wasOnGround = true;
                                    } else if (wasOnGround) {
                                        if (!mc.thePlayer.isCollidedHorizontally) {
                                            mc.thePlayer.motionY = -0.0784000015258789;
                                        }
                                        wasOnGround = false;
                                    }
                                    MovementUtil.setSpeed(0.33);
                                } else {
                                    mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                                }
                            }
                        }
                        break;

                        case "Funny": {
                            if (mc.gameSettings.keyBindJump.isKeyDown() && MovementUtil.isMoving()) {
                                MovementUtil.setSpeed(0.1);
                            } else if (!mc.gameSettings.keyBindJump.isKeyDown() && MovementUtil.isMoving()) {
                                    MovementUtil.setSpeed(0.32);
                            }
                            if (mc.thePlayer.hurtTime == 9) {
                               MovementUtil.setSpeed(MovementUtil.getSpeed() + (float) verusboost.getCurrentValue());
                            }
                            if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                                mc.thePlayer.jump();
                            }
                            break;
                        }

                        case "Grim": {
                            if (mc.thePlayer.onGround && MovementUtil.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown()) {
                                mc.thePlayer.jump();
                            } else {
                                return;
                            }
                            break;
                        }

                        case "BlocksMC": {
                            if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                                if (mc.thePlayer.onGround)
                                    mc.thePlayer.jump();
                                MovementUtil.setSpeed(0.35);
                                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                                    toggle();
                                }
                            }
                        }
                        break;

                        case "HvH Mode": {
                            if (MovementUtil.isMoving()) {
                                MovementUtil.setSpeed(4);
                            } else if (mc.thePlayer.hurtTime == 9) {
                                mc.thePlayer.motionY = 0.42f;
                            }
                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.motionY = 0.42f;
                            }
                            break;
                        }

                        case "NCP": {

//                        mc.thePlayer.sendChatMessage(".hclip 0.2");
//                        if (Keyboard.isKeyDown(KEY_W) ||  Keyboard.isKeyDown(KEY_D) ||  Keyboard.isKeyDown(KEY_A) ||  Keyboard.isKeyDown(KEY_S)) {
//                            MovementUtil.setSpeed(0);
//                        }
                            if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
                                mc.thePlayer.motionY = 0.42f;
                                mc.thePlayer.posY = mc.thePlayer.posY + 1;
                                Timer.timerSpeed = 1f;
                                MovementUtil.setSpeed(MovementUtil.getAllowedHDistNCP());
                            } else if (mc.thePlayer.hurtTime == 9) {
                                MovementUtil.setSpeed(MovementUtil.getSpeed() + (float) ncpboost.getCurrentValue());
                                mc.thePlayer.posY = mc.thePlayer.posY + 0.5;
                            }
                            if (timerUtil.hasTimeElapsed(500, true)) {
                                mc.thePlayer.posY = mc.thePlayer.posY + 0.5;
                                if (timerUtil.hasTimeElapsed(100, true)) {
                                    mc.thePlayer.posY = mc.thePlayer.posY + 0.5;
                                }
                            }
                            break;
                        }

                        case "Custom": {
                            if (downmotiontoggle.isEnabled() && !downmotioncustominit) {
                                initializeSettings(downmotioncustom);
                                downmotioncustominit = true;
                            }
                            if (MovementUtil.isMoving()) {
                                if (mc.thePlayer.onGround) {
                                    if (downmotiontoggle.isEnabled()) {
                                        mc.thePlayer.motionY = (float) downmotioncustom.getCurrentValue();
                                    }
                                }
                                Timer.timerSpeed = (float) timercustom.getCurrentValue();
                                mc.thePlayer.motionY = (float) ymotioncustom.getCurrentValue();
                                MovementUtil.setSpeed((float) speedcustom.getCurrentValue());
                                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                                    MovementUtil.setSpeed((float) speedcustom.getCurrentValue());
                                }
                            }
                            break;
                        }

                        case "Negativity": {
                            if (MovementUtil.isMoving()) {
                                if (Keyboard.isKeyDown(KEY_W) || Keyboard.isKeyDown(KEY_S) || Keyboard.isKeyDown(KEY_A) || Keyboard.isKeyDown(KEY_D) && mc.thePlayer.isAirBorne) {
                                    mc.thePlayer.motionY = 0;
                                    mc.thePlayer.posY = mc.thePlayer.posY + 0.3;
                                    MovementUtil.setSpeed(0.5);
                                    if (timerUtil.hasTimeElapsed(2000, true)) {
                                        mc.thePlayer.motionY = -1000;
                                        mc.thePlayer.posY = mc.thePlayer.posY - 0.3;
                                        MovementUtil.setSpeed(0.5);
                                    }
                                    if (Keyboard.isKeyDown(KEY_SPACE)) {
                                        ChatUtil.error("Disabling due to illegal key press!");
                                        MovementUtil.setSpeed(0);
                                        toggle();
                                    }
                                }
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
    };

            @Override
            public void onEnable () {
                downmotioncustominit = false;
                if (mc.thePlayer == null) {
                    return;
                }
                damage = false;
                packetList.clear();
                lastMovementYaw = -1;
                super.onEnable();
            }

            @Override
            public void onDisable () {
                downmotioncustominit = false;
                if (!packetList.isEmpty()) {
                    packetList.forEach(mc.getNetHandler()::addToSendQueueNoEvent);
                    packetList.clear();
                }
                Timer.timerSpeed = 1;
                super.onDisable();
            }
            private final IEventListener<UpdateEvent> onUpdateEvent = event -> setSuffix(mode.getCurrentMode());
        }