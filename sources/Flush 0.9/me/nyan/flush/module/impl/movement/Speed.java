package me.nyan.flush.module.impl.movement;

import me.nyan.flush.Flush;
import me.nyan.flush.event.Event;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventMove;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.combat.Aura;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.MathUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Speed extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "VanillaHop",
            "Vanilla", "Hypixel", "NCP", "VerusHop", /*"VerusLowHop", */"VerusGround", "Mineplex",
            "CubeHop", "Funcraft", "Redesky", "Strafe", "VanillaHop", "VanillaLowHop", "LegitHop");
    private final NumberSetting speedValue = new NumberSetting("Speed", this, 4, 1, 10, 0.1,
            () -> mode.is("vanilla") || mode.is("vanillahop") || mode.is("vanillalowhop"));
    private final NumberSetting height = new NumberSetting("Jump Height", this, 0.42, 0.05, 1, 0.01,
            () -> mode.is("vanillahop"));

    private int level;
    private double speed, lastDist;
    private boolean doSlow = false;

    private double motion;

    private boolean shouldBoost;

    public Speed() {
        super("Speed", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        shouldBoost = true;
        lastDist = 0;
        speed = 0;
        level = 4;

        mc.timer.timerSpeed = 1f;

        if (mode.is("veruslowhop")) {
            Flush.enableVerusDisabler();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Flush.disableVerusDisabler();
        if (!mode.is("redesky")) {
            MovementUtils.stopMotion();
        }
        speed = MovementUtils.getBaseMoveSpeed();

        if (!isEnabled(Fly.class)) {
            mc.timer.timerSpeed = 1;
        }
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (isEnabled(Fly.class)) {
            return;
        }

        switch (mode.getValue().toUpperCase()) {
            case "VANILLA":
                if (MovementUtils.isMoving()) {
                    MovementUtils.setSpeed(speedValue.getValue() / 6F);
                } else {
                    MovementUtils.setSpeed(0);
                }
                break;

            case "VANILLAHOP":
                if (MovementUtils.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = height.getValue();
                    } else
                        MovementUtils.setSpeed(speedValue.getValue() / 6F);
                } else {
                    MovementUtils.setSpeed(0);
                }
                break;

            case "NCP":
                if (!mc.thePlayer.isInWater()) {
                    if (MovementUtils.isMoving()) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0.4136434;
                            MovementUtils.setSpeed(0.139224);
                        } else {
                            MovementUtils.setSpeed(0.27193223);
                        }
                    }
                }
                break;

            case "LEGITHOP":
                if (mc.thePlayer.onGround && mc.thePlayer.jumpTicks == 0 && MovementUtils.isMoving()) {
                    mc.thePlayer.jump();
                    mc.thePlayer.jumpTicks = 10;
                }
                break;

            case "VANILLALOWHOP":
                if (!mc.thePlayer.isInWater()) {
                    if (MovementUtils.isMoving()) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0.2;
                        } else {
                            MovementUtils.setSpeed(speedValue.getValue() / 8D);
                        }
                    } else {
                        MovementUtils.stopMotion();
                    }
                }

                break;

            case "REDESKY":
                // dort's LiquidBounce speed script
                if (!MovementUtils.isMoving()) {
                    return;
                }

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    motion = 1.02995;
                    doSlow = true;
                } else {
                    if (doSlow) {
                        motion += 0.0015625;
                        doSlow = false;
                    } else {
                        motion *= 0.99585;
                    }
                    motion = Math.max(0.999998, motion);
                }

                mc.thePlayer.motionX *= motion;
                mc.thePlayer.motionZ *= motion;
                break;

            case "CUBEHOP":
                if (mc.thePlayer.isInWater()) {
                    return;
                }

                if (MovementUtils.isMoving()) {
                    mc.timer.timerSpeed = 1.14f;

                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.41886434;
                        MovementUtils.setSpeed(0.1620124);
                        mc.thePlayer.motionX *= 2.314;
                        mc.thePlayer.motionZ *= 2.314;
                    } else {
                        mc.thePlayer.motionX *= 0.9512;
                        mc.thePlayer.motionZ *= 0.9512;
                        MovementUtils.setSpeed(MovementUtils.getSpeed() * 1.071);
                    }
                } else {
                    mc.timer.timerSpeed = 1;
                    MovementUtils.stopMotion();
                }
                break;

            case "HYPIXEL":
                if (MovementUtils.isMoving()) {
                    mc.timer.timerSpeed = 0.94F;

                    if (mc.thePlayer.onGround) {
                        if (mc.thePlayer.jumpTicks == 0) {
                            mc.thePlayer.jump();
                            mc.thePlayer.jumpTicks = 10;
                        }
                        MovementUtils.setSpeed(0.4540124);
                    } else {
                        MovementUtils.setSpeed(MovementUtils.getSpeed() * 0.998);
                    }
                } else {
                    mc.timer.timerSpeed = 1;
                    MovementUtils.stopMotion();
                }
                break;

            case "FUNCRAFT":
                /*
                if (MovementUtils.isMoving()) {
                    mc.timer.timerSpeed = 1.04f;

                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        MovementUtils.setSpeed(0.170124D);
                        mc.thePlayer.motionX *= 2.978;
                        mc.thePlayer.motionZ *= 2.978;
                    } else {
                        mc.thePlayer.motionX *= 0.9503;
                        mc.thePlayer.motionZ *= 0.9503;
                        MovementUtils.setSpeed(MovementUtils.getSpeed() * 1.061);
                    }
                } else {
                    mc.timer.timerSpeed = 1;
                    MovementUtils.stopMotion();
                }

                 */
                break;

            case "FUNCRAFT YPORT":
                /*
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (MovementUtils.isMoving()) {
                        mc.timer.timerSpeed = 1.14f;

                        if (mc.thePlayer.onGround) {
                            MovementUtils.setSpeed(0.140124D);
                            mc.thePlayer.motionX *= 2.178;
                            mc.thePlayer.motionZ *= 2.178;
                        } else {
                            mc.thePlayer.motionX *= 0.9503;
                            mc.thePlayer.motionZ *= 0.9503;
                            MovementUtils.setSpeed(MovementUtils.getSpeed() * 1.061);
                        }
                    } else {
                        mc.timer.timerSpeed = 1;
                        MovementUtils.stopMotion();
                    }
                } else {
                    if (MovementUtils.isMoving()) {
                        if (mc.thePlayer.fallDistance < 1)
                            mc.thePlayer.motionY = -2;

                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            MovementUtils.setSpeed(0.470124D);
                        }

                        MovementUtils.setSpeed(MovementUtils.getSpeed() * 1.004);

                        if (mc.thePlayer.ticksExisted % 5 == 0) {
                            mc.timer.timerSpeed = 2.3f;
                        } else {
                            mc.timer.timerSpeed = 1;
                        }
                    } else {
                        mc.timer.timerSpeed = 1;
                    }
                }

                 */


                if (MovementUtils.isMoving()) {
                    mc.timer.timerSpeed = 1.4F;
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed());
                    } else
                        mc.thePlayer.motionY -= 0.43;
                } else
                    mc.timer.timerSpeed = 1;
                break;

            case "STRAFE":
                if (MovementUtils.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed());
                    }

                    MovementUtils.setSpeed(MovementUtils.getSpeed());
                }
                break;

            case "MINEPLEX":
                if (MovementUtils.isMoving()) {
                    mc.timer.timerSpeed = 1.04f;

                    if (mc.thePlayer.onGround) {
                        if (mc.thePlayer.jumpTicks == 0) {
                            mc.thePlayer.jump();
                            mc.thePlayer.jumpTicks = 10;
                        }
                        MovementUtils.setSpeed(0.1680124);
                    } else {
                        MovementUtils.setSpeed(MovementUtils.getSpeed() + 0.008 *
                                Math.max(mc.thePlayer.hurtTime, 1));
                        mc.thePlayer.motionY -= 0.01;
                        MovementUtils.hClip(0.02);
                    }
                } else {
                    mc.timer.timerSpeed = 1;
                    MovementUtils.stopMotion();
                }
                break;

            case "VERUSHOP":
                if (MovementUtils.isMoving() && !isEnabled(Fly.class) && mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                break;
            case "VERUSLOWHOP":
                boolean enableDisabler = true;
                if (MovementUtils.isMoving() && !isEnabled(Fly.class)) {
                    if (mc.thePlayer.onGround) {
                        if (getModule(Aura.class).target == null) {
                            mc.thePlayer.motionY = 0.3;
                        } else {
                            mc.thePlayer.jump();
                            enableDisabler = false;
                        }
                    }
                } else {
                    enableDisabler = false;
                }

                if (enableDisabler) {
                    Flush.enableVerusDisabler();
                } else {
                    Flush.disableVerusDisabler();
                }
                break;
        }

        getModule(TargetStrafe.class).strafe();
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (e.getState() == Event.State.PRE) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }

    @SubscribeEvent
    public void onMove(EventMove e) {
        if (isEnabled(Fly.class)) {
            return;
        }

        switch (mode.getValue().toUpperCase()) {
            case "VERUSHOP":
            case "VERUSGROUND":
            case "VERUSLOWHOP":
                if (isEnabled(Fly.class)) {
                    break;
                }
                if (!MovementUtils.isMoving()) {
                    e.setX(0.0);
                    e.setZ(0.0);
                    break;
                }

                if (mc.thePlayer.onGround) {
                    speed = 0.29;
                } else {
                    speed = 0.072;
                }

                if (mode.is("verusground")) {
                    speed = 0.066;
                    if (!mc.thePlayer.isSprinting()) {
                        speed = -0.04;
                    }
                } else {
                    if (mc.thePlayer.isSprinting()) {
                        speed += 0.04;
                    }
                }

                if (mc.thePlayer.isSprinting() && mc.thePlayer.moveStrafing != 0) {
                    speed -= 0.01;
                }

                MovementUtils.setSpeed(e, MovementUtils.getBaseMoveSpeed() + speed);
                break;

            case "FUNCRAFT":
                if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
                    level = 2;
                }

                if (MathUtils.round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == 0.138) {
                    mc.thePlayer.motionY -= 0.03;
                }

                if (level == 1 && MovementUtils.isMoving()) {
                    level++;
                } else if (level == 2) {
                    level++;
                    e.setY(mc.thePlayer.motionY = 0.399399995803833);
                    speed *= 2.129;
                } else if (level == 3) {
                    level++;
                    double difference = 0.11 * (lastDist - MovementUtils.getBaseMoveSpeed());
                    speed = lastDist - difference;
                } else if (level == 5) {
                    speed = MovementUtils.getBaseMoveSpeed();
                    lastDist = 0;
                    level++;
                } else if (level == 6) {
                    if (MovementUtils.isOnGround(-mc.thePlayer.motionY) || mc.thePlayer.isCollidedVertically) {
                        level = 1;
                    }
                    lastDist = 0;
                    speed = MovementUtils.getBaseMoveSpeed();
                    return;
                } else {
                    if (MovementUtils.isOnGround(-mc.thePlayer.motionY) || mc.thePlayer.isCollidedVertically) {
                        speed = MovementUtils.getBaseMoveSpeed();
                        lastDist = 0;
                        level = 5;
                        return;
                    }

                    speed = lastDist - lastDist / 159F;
                }

                speed = Math.max(speed, MovementUtils.getBaseMoveSpeed());

                if (!MovementUtils.isMoving()) {
                    e.setX(0.0);
                    e.setZ(0.0);
                } else {
                    MovementUtils.setSpeed(e, speed);
                }
                break;
        }

        getModule(TargetStrafe.class).strafe(e);
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (mode.is("redesky")) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook && shouldBoost) {
                e.cancel();
                shouldBoost = false;
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}