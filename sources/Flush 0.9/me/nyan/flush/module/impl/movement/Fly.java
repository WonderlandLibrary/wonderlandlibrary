package me.nyan.flush.module.impl.movement;

import me.nyan.flush.Flush;
import me.nyan.flush.event.Event;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.*;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventMove;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventUpdate;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Fly extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla",
            "Vanilla", "Verus", "VerusFast", "Funcraft");
    private final NumberSetting speedValue = new NumberSetting("Speed", this, 8, 1, 10, 0.1,
            () -> mode.is("vanilla") || mode.is("verusfast"));
    private final BooleanSetting boost = new BooleanSetting("Boost", this, true,
            () -> mode.is("verusfast")),
            bobbing = new BooleanSetting("Bobbing", this, true),
            blink = new BooleanSetting("Blink", this, false);

    private final ArrayList<Packet<?>> blinkpackets = new ArrayList<>();
    private final Timer timer = new Timer();
    private boolean shouldBoost;
    private int stage;
    private double lastDist, speed;

    private boolean lagbacked;

    public Fly() {
        super("Fly", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        shouldBoost = false;
        lagbacked = false;
        stage = 0;

        if (blink.getValue() && mc.theWorld == null) {
            toggle();
        }

        blinkpackets.clear();

        timer.reset();

        if (mode.is("vanilla")) {
            mc.thePlayer.motionY = 0;
        }

        shouldBoost = boost.getValue() && mc.thePlayer.onGround;

        switch (mode.getValue().toUpperCase()) {
            case "FUNCRAFT":
                speed = lastDist = stage = 0;
                lagbacked = false;
                break;
            case "VERUS":
            case "VERUSFAST":
                Flush.enableVerusDisabler();
                break;
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Flush.disableVerusDisabler();

        MovementUtils.stopMotion();
        mc.timer.timerSpeed = 1;
        mc.thePlayer.capabilities.isFlying = false;

        if (mc.isIntegratedServerRunning()) {
            return;
        }

        for (Packet<?> packet : blinkpackets) {
            mc.getNetHandler().addToSendQueue(packet);
        }
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (MovementUtils.isOnGround(0.05)) {
        }

        if (bobbing.getValue() && MovementUtils.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.cameraYaw = 0.1f;
        }

        switch (mode.getValue().toUpperCase()) {
            case "VANILLA":
                if (MovementUtils.isMoving()) {
                    MovementUtils.setSpeed(speedValue.getValue() / 2);
                } else {
                    MovementUtils.setSpeed(0);
                }

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = speedValue.getValue() / 4D;
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -speedValue.getValue() / 4D;
                } else {
                    mc.thePlayer.motionY = 0;
                }
                break;


            case "FUNCRAFT":
                /*
                if (!MovementUtils.isMoving()) {
                    mc.thePlayer.motionY = 0.00782657327;
                    MovementUtils.setSpeed(0);
                    mc.timer.timerSpeed = 1;
                }else {
                    mc.timer.timerSpeed = 0.2453457832223f;

                    MovementUtils.setSpeed(0.68332);

                    if(mc.thePlayer.ticksExisted % 8 == 0) {
                        if(mc.gameSettings.keyBindJump.isKeyDown())
                            mc.thePlayer.motionY = 0.492723;
                        else
                            mc.thePlayer.motionY = 0.283227;
                    }
                }
                 */

                /*
                if (MovementUtils.isMoving()) {
                    MovementUtils.setSpeed(MovementUtils.getSpeed()/1.01);

                    if (shouldBoost) {
                        if (!timer.hasTimeElapsed(boostDuration.getValueInt(), false)) {
                            mc.timer.timerSpeed = boostSpeed.getValueFloat();
                        } else if (mc.timer.timerSpeed * 0.95 > 1) {
                            mc.timer.timerSpeed *= 0.95;
                        } else {
                            mc.timer.timerSpeed = 1;
                        }
                    } else {
                        mc.timer.timerSpeed = 1;
                    }

                    if (mc.thePlayer.ticksExisted % 4 == 0) {
                        MovementUtils.vClip(1E-4);
                    }
                }

                mc.thePlayer.motionY = 0;

                 */

                /*
                if (timer.hasTimeElapsed(300, false)) {
                    if (MovementUtils.isOnGround(0.01)) {
                        MovementUtils.vClip(0.24);
                    }
                    if (!MovementUtils.isOnGround(3.33315597345063e-11)) {
                        MovementUtils.vClip(-(3.33315597345063e-11));
                    }

                    if (MovementUtils.isMoving()) {
                        mc.timer.timerSpeed = 1.8F;

                        if (MovementUtils.getSpeed() > MovementUtils.getBaseMoveSpeed() - 0.01) {
                            MovementUtils.setSpeed(MovementUtils.getSpeed() - 0.03);
                        }

                        if (mc.thePlayer.ticksExisted % 3 == 0) {
                            MovementUtils.vClip(1E-8);
                        }
                    } else {
                        MovementUtils.stopMotion();
                    }

                    mc.thePlayer.motionY = 0;
                    break;
                }

                if (mc.thePlayer.onGround) stage = 1;

                if (stage == 1) {
                    speed = lastDist / 159 + 1.38;
                    stage++;
                }

                if (stage == 2) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        mc.thePlayer.motionY = 0;

                        mc.timer.timerSpeed = 1.8F;
                        mc.gameSettings.keyBindRight.pressed = false;
                        mc.gameSettings.keyBindLeft.pressed = false;
                        mc.gameSettings.keyBindBack.pressed = false;

                        if (speed > 0.3) {
                            speed -= 0.01;
                        }

                        state++;

                        switch (state) {
                            case 1:
                                MovementUtils.vClip(1E-12);
                                break;
                            case 2:
                                MovementUtils.vClip(-1E-12);
                                break;
                            case 3:
                                MovementUtils.vClip(1E-12);
                                state = 0;
                                break;
                        }
                    }

                    if (!mc.thePlayer.onGround && MovementUtils.isMoving())
                        MovementUtils.setSpeed(speed);
                }

                 */
                break;

            case "VERUSFAST":
                Flush.enableVerusDisabler();

                speed = 0.066;
                if (!mc.thePlayer.isSprinting()) {
                    speed = -0.04;
                }

                if (mc.thePlayer.isSprinting() && mc.thePlayer.moveStrafing != 0) {
                    speed -= 0.01;
                }

                if (MovementUtils.isMoving()) {
                    MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() + speed);
                } else {
                    MovementUtils.setSpeed(0);
                }

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = 0.2;
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -0.2;
                } else {
                    mc.thePlayer.motionY = 0;
                }
                switch (stage) {
                    case 0:
                        if (MovementUtils.isOnGround(0.05) && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, 4, 0)).isEmpty()) {
                            MovementUtils.packetvClip(4);
                            MovementUtils.packetvClip(0);
                            for (int i = 0; i < 2; i++) {
                                MovementUtils.packetvClip(0, true);
                            }
                            shouldBoost = true;
                        } else {
                            shouldBoost = false;
                        }
                        stage++;
                        return;

                    case 1:
                        if (shouldBoost) {
                            if (!timer.hasTimeElapsed(1000, false)) {
                                if (mc.thePlayer.hurtTime > 0) {
                                    stage++;
                                }
                            } else {
                                shouldBoost = false;
                            }
                        }
                        break;

                    case 2:
                        mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
                        mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
                        mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
                        mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
                        if (MovementUtils.isMoving()) {
                            stage++;
                        }
                        break;

                    case 3:
                        if (MovementUtils.isMoving() && mc.thePlayer.hurtTime < 10) {
                            if (mc.thePlayer.hurtTime > 0) {
                                MovementUtils.setSpeed(Math.min(speedValue.getValue(), 9.85));
                            }
                        } else {
                            stage++;
                        }
                        break;
                }

                if (shouldBoost && stage > 0) {
                    if (stage < 2) {
                        MovementUtils.stopMotion();
                        mc.thePlayer.jumpMovementFactor = 0;
                    }
                }
                break;

            case "VERUS":
                Flush.enableVerusDisabler();

                speed = 0.066;
                if (!mc.thePlayer.isSprinting()) {
                    speed = -0.04;
                }

                if (mc.thePlayer.isSprinting() && mc.thePlayer.moveStrafing != 0) {
                    speed -= 0.01;
                }

                if (MovementUtils.isMoving()) {
                    MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() + speed);
                } else {
                    MovementUtils.setSpeed(0);
                }

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = 0.2;
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -0.2;
                } else {
                    mc.thePlayer.motionY = 0;
                }
                break;
        }

        getModule(TargetStrafe.class).strafe();
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (e.isPre()) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

            switch (mode.getValue().toUpperCase()) {
                case "VERUS":
                case "VERUSFAST":
                    if (MovementUtils.isOnGround(0.05)) {
                        e.setY(e.getY() + 0.05);
                    }
                    break;

                case "FUNCRAFT":
                    mc.thePlayer.jumpMovementFactor = 0;

                    if (!MovementUtils.isMoving() || mc.thePlayer.isCollidedHorizontally ||
                            (!mc.thePlayer.onGround && stage < 2)) {
                        lagbacked = true;
                    }

                    mc.thePlayer.motionY = 0;

                    if (stage > 0 || lagbacked) {
                        e.setGround(true);

                        if (MovementUtils.isOnGround(0.01)) {
                            MovementUtils.vClip(0.05);
                        }

                        double insaneValue = 3.33315597345063e-11;
                        if (!MovementUtils.isOnGround(insaneValue)) {
                            MovementUtils.vClip(-insaneValue);
                        }
                    }
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (blink.getValue()) {
            if (e.isOutgoing() && !(e.getPacket() instanceof C01PacketChatMessage)) {
                blinkpackets.add(e.getPacket());
                e.cancel();
            }
        }

        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            lagbacked = true;
        }
    }

    @SubscribeEvent
    public void onMove(EventMove e) {
        if (!(mode.is("funcraft"))) {
            return;
        }

        if (MovementUtils.isMoving()) {
            if (!lagbacked) {
                switch (stage) {
                    case 0:
                        speed = 0;
                        break;
                    case 1:
                        e.setY(mc.thePlayer.motionY = 0.3999);
                        speed *= 2.1;
                        break;
                    case 2:
                        speed = 1.7;
                        break;
                    default:
                        speed = lastDist - lastDist / 159;
                        break;
                }
                speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());
                MovementUtils.setSpeed(e, speed);
                stage++;
            } else {
                MovementUtils.setSpeed(e, MovementUtils.getBaseMoveSpeed());
            }
        }

        getModule(TargetStrafe.class).strafe(e);
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    public boolean isUsingVerusDisabler() {
        return isEnabled() && (mode.is("verus") || mode.is("verusfast"));
    }
}