/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.MoveButtonEvent;
import dev.rise.event.impl.other.MoveEvent;
import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.font.CustomFont;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.player.Scaffold;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Gives you a boost of speed or an improved strafing.
 */
@ModuleInfo(name = "Speed", description = "Lets you move faster", category = Category.MOVEMENT)
public final class Speed extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Custom", "Custom", "Mineplex", "Strafe Hop", "Vulcan", "Mineplex Low", "Mineplex Smooth", "Hypixel", "Hypixel2", "Cubecraft", "NCP", "Timer Bypass", "Taka", "Verus", "Spartan", "Redesky", "Zonecraft", "Exempted Value", "NCP Latest", "Convert Ground", "AAC 4 Simple", "Invadedlands", "Minemenclub Hop", "Intave", "Teleport", "Predictive", "Vulcan2");
    private final ModeSetting exemptedValueType = new ModeSetting("Value", this, "1", "1", "2", "3");
    private final ModeSetting verusMode = new ModeSetting("Verus Mode", this, "Hop", "Hop", "Float", "YPort");

    private final ModeSetting groundSpoofMode = new ModeSetting("GroundSpoof", this, "Off", "Off", "On", "Air", "Tick");
    private final NumberSetting customSpeed = new NumberSetting("Custom Speed", this, 0.35, 0.0, 4.0, 0.01);
    private final NumberSetting customMotionY = new NumberSetting("Custom MotionY", this, 0.42F, 0.0, 1.0, 0.01);
    private final NumberSetting customFallMotion = new NumberSetting("Custom Fall Motion", this, 0.4, 0.0, 1.0, 0.01);
    private final NumberSetting fallTick = new NumberSetting("Custom Fall Tick", this, 2, 0.0, 12, 1);
    private final NumberSetting randomizeSpeed = new NumberSetting("Randomize Speed", this, 0.0, 0.0, 10, 0.1);
    private final NumberSetting voidTpTicks = new NumberSetting("Void TP Ticks", this, 0.0, 0.0, 50, 1);
    private final NumberSetting timer = new NumberSetting("Timer", this, 1.0, 0.1, 10, 0.1);
    private final NumberSetting timerRandom = new NumberSetting("Timer Random", this, 0.0, 0.0, 10, 0.1);
    private final NumberSetting groundSpoofTick = new NumberSetting("GroundSpoof Ticks", this, 0.0, 0.0, 10, 1);

    private final NumberSetting teleportDistance = new NumberSetting("Teleport Distance", this, 0.03, 0.0, 1, 0.01);
    private final BooleanSetting strafe = new BooleanSetting("Strafe", this, false);
    private final BooleanSetting mmcBalanceTimer = new BooleanSetting("MinemenClub Balance Timer", this, false);

    private final BooleanSetting friction = new BooleanSetting("Abide Friciton", this, false);
    private final BooleanSetting strafeOnGround = new BooleanSetting("Only Strafe On Ground", this, false);
    private final BooleanSetting groundDisabler = new BooleanSetting("Ground Disabler", this, false);
    private final BooleanSetting fastFall = new BooleanSetting("FastFall", this, false);
    private final BooleanSetting voidTP = new BooleanSetting("VoidTP", this, false);
    private final BooleanSetting voidTPMove = new BooleanSetting("VoidTP only Move", this, false);
    private final BooleanSetting cancelC0F = new BooleanSetting("Cancel C0F", this, false);
    private final BooleanSetting cancelC00 = new BooleanSetting("Cancel C00", this, false);

    private final NumberSetting speedTimer = new NumberSetting("Speed", this, 2, 0.1, 6.0, 0.1);
    private final NumberSetting pulse = new NumberSetting("Pulse", this, 20, 4, 100, 0.1);

    private final BooleanSetting damageBoost = new BooleanSetting("Damage Boost", this, false);

    private final NumberSetting speedMulti = new NumberSetting("Speed Multiplier", this, 1.1, 1, 2, 0.1);
    private final NumberSetting timerBoost = new NumberSetting("Timer Boost", this, 1.1, 1, 4, 0.1);

    private final NumberSetting tick = new NumberSetting("Tick", this, 1, 0.0, 12, 0.1);
    private final BooleanSetting groundSpoof = new BooleanSetting("GroundSpoof", this, false);

    private final BooleanSetting smoothCamera = new BooleanSetting("Hide Jumps", this, false);

    private final NumberSetting speedMultiplierNCP = new NumberSetting("Speed Multiplier No Speed", this, 1.05, 1, 1.35, 0.01);
    private final NumberSetting speedMultiplierNCPSpeed1 = new NumberSetting("Speed Multiplier Speed 1", this, 1.05, 1, 1.35, 0.01);
    private final NumberSetting speedMultiplierNCPSpeed2 = new NumberSetting("Speed Multiplier Speed 2", this, 1.05, 1, 1.35, 0.01);
    private final NumberSetting timerNcp = new NumberSetting("General Timer", this, 1, 1, 1.4, 0.1);
    private final NumberSetting speedInAirNcp = new NumberSetting("SpeedInAir No Speed", this, 0.02, 0.02, 0.06, 0.01);
    private final NumberSetting speedInAirNcpSpeed1 = new NumberSetting("SpeedInAir Speed 1", this, 0.02, 0.02, 0.06, 0.01);
    private final NumberSetting speedInAirNcpSpeed2 = new NumberSetting("SpeedInAir Speed 2", this, 0.02, 0.02, 0.06, 0.01);
    private final BooleanSetting smartTimer = new BooleanSetting("Smart Timer", this, false);
    private final BooleanSetting revertNcpSpeed = new BooleanSetting("Revert to fastest on default config", this, false);

    private final NumberSetting howOften = new NumberSetting("How Often", this, 1, 1, 50, 0.01);
    private final NumberSetting distance = new NumberSetting("Distance", this, 2, 2, 200, 0.01);

    private final BooleanSetting disableOnFlag = new BooleanSetting("Disable on flag", this, true);
    private final BooleanSetting stopOnDisable = new BooleanSetting("Stop on Disable", this, true);

    private final NumberSetting hypixelTimer = new NumberSetting("Hypixel Timer", this, 1, 1, 2, 0.1);

    private final NumberSetting mmc = new NumberSetting("Mmc Speed", this, 1.0, 1.1, 5.0, 0.1);

    private long balance, lastPreMotion;
    private int ticks, offGroundTicks, onGroundTicks, stage, ticksDisable;
    private double startY, speed;
    private boolean jumped, bool, verusEpicBypassBooleanTM, touchedGround;
    private final Deque<Packet<?>> packets = new ArrayDeque<>();

    private float targetYaw;
    private float lastYaw;
    private float yaw;

    @Override
    protected void onEnable() {
        ticks = 0;
        offGroundTicks = 0;
        speed = 0;
        onGroundTicks = 0;
        startY = mc.thePlayer.posY;
        packets.clear();

        stage = 0;
        verusEpicBypassBooleanTM = false;
        touchedGround = false;
        bool = false;
        jumped = false;
        ticksDisable = 21;

//        switch (mode.getMode()) {
//            case "Hypixel":
//                targetYaw = mc.thePlayer.rotationYaw;
//                break;
//        }
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.speedInAir = 0.02f;
        mc.thePlayer.jumpMovementFactor = 0.02f;

        mc.timer.timerSpeed = 1;

        EntityPlayer.enableCameraYOffset = false;
//        EntityPlayer.movementYaw = null;

        if (stopOnDisable.isEnabled())
            MoveUtil.stop();

        if (packets.isEmpty()) return;

        packets.forEach(PacketUtil::sendPacketWithoutEvent);
        packets.clear();
    }

    @Override
    public void onUpdateAlwaysInGui() {
        fallTick.hidden = customFallMotion.hidden = !fastFall.isEnabled() || !mode.is("Custom");

        voidTPMove.hidden = voidTpTicks.hidden = !voidTP.isEnabled() || !mode.is("Custom");

        strafeOnGround.hidden = voidTP.hidden = groundSpoofTick.hidden = groundSpoofMode.hidden = cancelC00.hidden = cancelC0F.hidden = timer.hidden = timerRandom.hidden = randomizeSpeed.hidden = customSpeed.hidden = customMotionY.hidden = friction.hidden = groundDisabler.hidden = fastFall.hidden = customFallMotion.hidden = fallTick.hidden = !mode.is("Custom");

        groundSpoofTick.hidden = !groundSpoofMode.is("Tick") && mode.is("Custom") || !mode.is("Custom");

        speedTimer.hidden = pulse.hidden = !mode.is("Timer Bypass");

        damageBoost.hidden = !mode.is("Verus") || !verusMode.is("Hop");

        speedMulti.hidden = timerBoost.hidden = !mode.is("NCP");

        tick.hidden = strafe.hidden = groundSpoof.hidden = !mode.is("Exempted Value");

        strafe.hidden = !(mode.is("Strafe Hop") || mode.is("Exempted Value") || mode.is("Teleport"));

        verusMode.hidden = !(mode.is("Verus"));

        speedMultiplierNCP.hidden = speedMultiplierNCPSpeed1.hidden = speedMultiplierNCPSpeed2.hidden = speedInAirNcp.hidden = speedInAirNcpSpeed1.hidden = speedInAirNcpSpeed2.hidden = revertNcpSpeed.hidden = !mode.is("NCP Latest");

        timerNcp.hidden = !(mode.is("NCP Latest") && !smartTimer.isEnabled());

        smartTimer.hidden = !mode.is("NCP Latest");

        hypixelTimer.hidden = !mode.is("Hypixel") && !mode.is("Hypixel2");

        howOften.hidden = distance.hidden = !mode.is("Convert Ground");

        mmcBalanceTimer.hidden = mmc.hidden = !mode.is("Minemenclub Hop");

        exemptedValueType.hidden = !mode.is("Exempted Value");

        teleportDistance.hidden = !mode.is("Teleport");

        if (revertNcpSpeed.isEnabled()) {
            revertNcpSpeed.setEnabled(false);

            speedMultiplierNCP.setValue(1.07);
            speedMultiplierNCPSpeed2.setValue(1.1);
            speedMultiplierNCPSpeed1.setValue(1.1);
            timerNcp.setValue(1.07);
            speedInAirNcp.setValue(0.02);
            speedInAirNcpSpeed1.setValue(0.025);
            speedInAirNcpSpeed2.setValue(0.03);
            smartTimer.setEnabled(true);
        }
    }

    @Override
    public void onUpdateAlways() {
        if (mode.is("Minemenclub Hop") && mmcBalanceTimer.isEnabled()) {
            if (mc.thePlayer.ticksExisted < 5) balance = 0;

            final long lastPreMotion = this.lastPreMotion;
            this.lastPreMotion = System.currentTimeMillis();

            if (lastPreMotion != 0) {
                final long difference = System.currentTimeMillis() - lastPreMotion;

                balance += 50;
                balance -= difference;
            }

            if (!MoveUtil.isMoving()) {
                mc.timer.timerSpeed = 0.7f;
            } else {
                if (mc.timer.timerSpeed == 0.7f) mc.timer.timerSpeed = 1;
            }
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        ++ticks;
        ticksDisable++;
        if (ticksDisable < 20) {
            return;
        }

        if (ticksDisable == 20) onEnable();

        if (mc.thePlayer.ticksExisted == 1) toggleModule();
        if (Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Fly")).isEnabled()) return;

        if (mc.thePlayer.onGround) {
            offGroundTicks = 0;
            onGroundTicks += 1;
            if (!verusMode.is("Float"))
                startY = mc.thePlayer.posY - 0.01;
        } else {
            offGroundTicks += 1;
            onGroundTicks = 0;
        }

        EntityPlayer.enableCameraYOffset = false;

        if (mc.thePlayer.posY > startY && smoothCamera.isEnabled()) {
            EntityPlayer.enableCameraYOffset = true;
            EntityPlayer.cameraYPosition = startY;
        }

        switch (mode.getMode()) {
            case "Custom":
                if (MoveUtil.isMoving()) {
                    final double randomConst = randomizeSpeed.getValue() < 0.1 ? 0.0 : randomDouble(0.0, randomizeSpeed.getValue() / 100);
                    final double randomize = (random() < 0.5 ? -randomConst : randomConst);

                    if (mc.thePlayer.onGround) {
                        final double jumpValue = customMotionY.getValue();

                        mc.thePlayer.motionY = (Math.abs(jumpValue - 0.42F) < 1.0E-3) ? 0.42f : jumpValue;

                        if (friction.isEnabled()) {
                            MoveUtil.strafe(customSpeed.getValue() + randomize);
                        }

                        if (groundDisabler.isEnabled()) {
                            event.setGround(ticks % 2 == 0);
                        }
                    }

                    if (!friction.isEnabled()) {
                        if (strafeOnGround.isEnabled()) {
                            if (mc.thePlayer.onGround) {
                                MoveUtil.strafe(customSpeed.getValue() + randomize);
                            }
                        } else {
                            MoveUtil.strafe(customSpeed.getValue() + randomize);
                        }
                    } else {
                        if (strafeOnGround.isEnabled()) {
                            if (mc.thePlayer.onGround) {
                                MoveUtil.strafe();
                            }
                        } else {
                            MoveUtil.strafe();
                        }
                    }

                    if (fastFall.isEnabled()) {
                        if (offGroundTicks == Math.round(fallTick.getValue())) {
                            mc.thePlayer.motionY = -customFallMotion.getValue();
                        }
                    }
                } else {
                    mc.thePlayer.motionZ = mc.thePlayer.motionX = 0;
                }

                if (voidTP.isEnabled()) {
                    if (voidTpTicks.getValue() >= 0.5 && ticks % Math.round(voidTpTicks.getValue()) == 0) {
                        if (voidTPMove.isEnabled()) {
                            if (MoveUtil.isMoving()) {
                                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 16 - Math.random(), mc.thePlayer.posZ, event.isGround()));
                            }
                        } else {
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 16 - Math.random(), mc.thePlayer.posZ, event.isGround()));
                        }
                    }
                }

                final float timerRandomConst = (float) (timerRandom.getValue() < 0.1 ? 0.0 : randomDouble(0.0, timerRandom.getValue() / 100));
                final float timerRandomize = (random() < 0.5 ? -timerRandomConst : timerRandomConst);

                final boolean timerReset = timer.getValue() - 1.0 < 1E-3 && timer.getValue() - 1.0 > -1E-3;

                mc.timer.timerSpeed = timerReset ? 1.0F : (float) timer.getValue();
                mc.timer.timerSpeed += timerRandomize;

                switch (groundSpoofMode.getMode().toLowerCase()) {
                    case "off":
                        break;

                    case "on": {
                        event.setGround(true);
                        break;
                    }

                    case "air": {
                        event.setGround(false);
                        break;
                    }

                    case "tick": {
                        if (groundSpoofTick.getValue() > 0.5 && ticks % Math.round(groundSpoofTick.getValue()) == 0) {
                            event.setGround(true);
                        }

                        break;
                    }
                }
                break;

            case "Vulcan2":
                if (mc.thePlayer.onGround && !this.getModule(Scaffold.class).isEnabled() && mc.thePlayer.motionY > -.2) {
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition((mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2, ((mc.thePlayer.posY + mc.thePlayer.lastTickPosY) / 2) - 0.0784000015258789, (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2, false));
                    PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook((mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2, (mc.thePlayer.posY + mc.thePlayer.lastTickPosY) / 2, (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));

                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0784000015258789, mc.thePlayer.posZ, false));
                    PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));

                    MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() * 1.25 * 2);

                } else if (offGroundTicks == 1) {
                    MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() * 0.91f);
                }
                break;

            case "Hypixel":
                final double xDist = mc.thePlayer.lastTickPosX - mc.thePlayer.posX;
                final double zDist = mc.thePlayer.lastTickPosZ - mc.thePlayer.posZ;
                final double lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (MoveUtil.isMoving()) {
                    final double baseMoveSpeed = MoveUtil.getBaseMoveSpeed();
                    //strafe check dont remove
                    event.setYaw(getMovementDirection(mc.thePlayer.moveForward, mc.thePlayer.moveStrafing, mc.thePlayer.rotationYaw));

                    if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance < 0.54) {
                        mc.thePlayer.motionY = this.lowHopYModification(mc.thePlayer.motionY, round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3, 0.001)) + Math.random() / 100000f;
                    }

                    if (mc.thePlayer.onGround) {
                        boolean doInitialLowHop = !mc.thePlayer.isPotionActive(Potion.jump) && !mc.thePlayer.isCollidedHorizontally;

                        mc.thePlayer.motionY = doInitialLowHop ? 0.4f : 0.42f;

                        MoveUtil.strafe(baseMoveSpeed * 1.6);
                        bool = true;
                    } else if (bool) {
                        bool = false;

                        final double bunny = 0.85 * (lastDist - baseMoveSpeed);

                        MoveUtil.strafe(lastDist - bunny);

                    } else {
                        MoveUtil.strafe(MoveUtil.getSpeed() * 0.99);
                    }
                }

                mc.timer.timerSpeed = (float) (hypixelTimer.getValue() - Math.random() / 50);

                if (mc.thePlayer.isCollidedHorizontally || mc.thePlayer.fallDistance > 0.8)
                    MoveUtil.strafe(MoveUtil.getSpeed() * 0.9);

//                speed = MoveUtil.moveSpeed() * 2 - Math.random() / 100;
//
//                if (mc.thePlayer.hurtTime == 9) ticks = 0;
//
//                if (mc.thePlayer.onGround && !this.getModule(Scaffold.class).isEnabled() && mc.thePlayer.posY == mc.thePlayer.lastTickPosY) {
//                    if (MoveUtil.getSpeed() > 0.06)
//                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition((mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2, ((mc.thePlayer.posY + mc.thePlayer.lastTickPosY) / 2), (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2, false));
//                    MoveUtil.strafe(speed);
//
//                } else if (offGroundTicks == 1) {
//                    MoveUtil.strafe(MoveUtil.moveSpeed() - Math.random() / 100);
//                }
//
//                Rise.addChatMessage(speed);
//
//                mc.timer.timerSpeed = (float) (0.9f - Math.random() / 10f);

                break;

            case "Predictive":
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }

                if (offGroundTicks == 2) {
                    event.setGround(true);
                    mc.thePlayer.motionY = -0.0784000015258789;
                    mc.thePlayer.onGround = true;

                    speed = mc.thePlayer.motionY;
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = speed;
                }
                break;

            case "Strafe Hop":
                if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                    mc.thePlayer.jump();
                }

                if (strafe.isEnabled())
                    MoveUtil.strafe();
                break;

            case "Vulcan":
                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {

                        double speed = MoveUtil.getBaseMoveSpeed() - 0.01;
                        MoveUtil.strafe(speed - (Math.random() / 2000));

                        mc.thePlayer.jump();

                        bool = true;
                    } else {
                        if (bool) {
                            if (offGroundTicks > 3)
                                mc.thePlayer.motionY = MoveUtil.getPredictedMotionY(mc.thePlayer.motionY);

                            if (!(PlayerUtil.getBlockRelativeToPlayer(0, 2, 0) instanceof BlockAir))
                                MoveUtil.strafe(MoveUtil.getSpeed() * (1.1 - (Math.random() / 500)));
                        }

                        if (mc.thePlayer.isInLiquid() || mc.thePlayer.hurtTime == 9)
                            MoveUtil.strafe();
                    }
                } else
                    MoveUtil.stop();
                break;

            case "Mineplex Low":
                ticks--;

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    ticks = 0;
                    return;
                }

                if (!jumped && mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = 0.21546;
                    MoveUtil.strafe(0.1);
                    jumped = true;
                    ticks++;
                }

                if (!mc.thePlayer.onGround) {
                    speed = (float) MoveUtil.getSpeed();
                }

                if (offGroundTicks == 1 && jumped) {
                    MoveUtil.strafe(0.6);
                }

                if (jumped && mc.thePlayer.onGround) {
                    MoveUtil.strafe(speed);
                }

                if (jumped)
                    speed -= 0.009;

                if (onGroundTicks > 2) {
                    offGroundTicks = 0;
                    jumped = false;
                    speed = 0;
                    onGroundTicks = 0;
                    startY = mc.thePlayer.posY;
                    packets.clear();
                }

                if (!MoveUtil.isMoving())
                    ticks = 0;

                MoveUtil.strafe();

                break;

            case "Teleport": {
                if (!MoveUtil.isMoving()) return;

                speed = teleportDistance.getValue();
                final float yaw = (float) MoveUtil.getDirection();
                final double posX = -MathHelper.sin(yaw) * speed + mc.thePlayer.posX;
                final double posZ = MathHelper.cos(yaw) * speed + mc.thePlayer.posZ;

                if (offGroundTicks > 1 && offGroundTicks < 11) mc.thePlayer.setPosition(posX, mc.thePlayer.posY, posZ);

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                break;
            }

            case "NCP": {
                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        MoveUtil.strafe(MoveUtil.getSpeed() * speedMulti.getValue() + Math.random() / 100);
                    } else {
                        MoveUtil.strafe(MoveUtil.getSpeed() * 1.0035);
                    }

                    mc.thePlayer.jumpMovementFactor = 0.02725F;
                }

                if (ticks < 20) {
                    mc.timer.timerSpeed = 0.98f;
                }

                if (ticks == 20)
                    ticks = 0;

                if (ticks < 9) {
                    mc.timer.timerSpeed = (float) (timerBoost.getValue() + Math.random() / 100);
                }
                break;
            }

            case "Taka": {
                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {

                        mc.thePlayer.speedInAir = (float) (0.02f + Math.random() / 300);
                        if (mc.thePlayer.fallDistance > 0) {
                            mc.thePlayer.motionY += 0.04;
                        }

                    }

                    mc.timer.timerSpeed = (float) (1 + Math.random() / 3);
                } else {
                    mc.timer.timerSpeed = 1;
                }
                break;
            }

            case "Spartan": {
                if (MoveUtil.isMoving()) {
                    MoveUtil.strafe();

                    mc.thePlayer.speedInAir = (float) (0.04 + Math.random() / 5000);

                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        ticks++;

                        speed = 0.5 + Math.random() / 500;

                        if (MoveUtil.getSpeed() < speed)
                            MoveUtil.strafe(speed);
                    }

                    mc.thePlayer.setSprinting(false);
                    mc.gameSettings.keyBindSprint.setKeyPressed(false);
                }
                break;
            }

            case "Redesky": {

                if (mc.thePlayer.onGround) {
                    MoveUtil.strafe(0.5);
                }

                if (mc.thePlayer.ticksExisted % 40 == 0) {
                    event.setY(event.getY() - 0.5);
                    event.setGround(false);
                    MoveUtil.strafe(0);
                }

                break;
            }

            case "Mineplex Smooth": {

                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();

                        if (speed < 0.5)
                            speed = 0.1f;
                    } else {
                        if (offGroundTicks == 1 && speed < 0.3) {
                            speed = 0.55f;
                        }
                    }
                }

                if (mc.thePlayer.hurtTime == 9) {
                    speed = 0.7f;
                }

                speed -= speed / 70;


                MoveUtil.strafe(speed);


                break;
            }
            case "Zonecraft": {

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    MoveUtil.strafe(1);
                }
                mc.timer.timerSpeed = 0.9f;

                if (offGroundTicks == 1) {
                    mc.thePlayer.motionY = -0.0784000015258789;
                }

                MoveUtil.strafe();

                /*
                if (mc.thePlayer.onGround) {
                    speed = (float) MoveUtil.getBaseMoveSpeed() + 0.1f;
                    mc.thePlayer.jump();
                }

                speed -= speed / 79;
                MoveUtil.strafe(speed);*/

                break;
            }
            case "Exempted Value": {

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                } else {
                    if (offGroundTicks == (int) tick.getValue()) {
                        double v = 0;
                        switch (exemptedValueType.getMode()) {
                            case "1":
                                v = -0.0784000015258789;
                                break;
                            case "2":
                                v = -0.09800000190734864;
                                break;
                            case "3":
                                v = -0.09800000190735147;
                                break;
                        }

                        mc.thePlayer.motionY = v;
                        if (groundSpoof.isEnabled()) {
                            event.setGround(true);
                        }
                    }
                }

                if (strafe.isEnabled()) {
                    MoveUtil.strafe();
                }

                break;
            }

            case "Convert Ground": {

                if (mc.thePlayer.ticksExisted % (int) howOften.getValue() == 0) {
                    speed = (float) MoveUtil.getSpeed();

                    final double direction2 = MoveUtil.getDirection();
                    double posX2 = -Math.sin(direction2) * speed;
                    double posZ2 = Math.cos(direction2) * speed;

                    if (!MoveUtil.isMoving())
                        posX2 = posZ2 = 0;

                    if (MoveUtil.isMoving())
                        for (int i = 0; i <= (int) distance.getValue(); ++i) {

                            MoveUtil.strafe(speed * (i + 1));
                            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + posX2 * i, mc.thePlayer.posY, mc.thePlayer.posZ + posZ2 * i, (float) (mc.thePlayer.rotationYaw + Math.random()), (float) (mc.thePlayer.rotationPitch + Math.random()), mc.thePlayer.onGround));

                        }


                } else {
                    MoveUtil.stop();
                    MoveUtil.strafe(0.14);
                    mc.timer.timerSpeed = 0.6f;
                }

                break;
            }

//            case "Hypixel": {
//                event.setYaw(yaw);
//                mc.thePlayer.renderYawOffset = yaw;
//                mc.thePlayer.rotationYawHead = yaw;
//
//                EntityPlayer.movementYaw = yaw;
//
//                lastYaw = yaw;
//                break;
//            }

            case "AAC 4 Simple": {

                mc.timer.timerSpeed = 1.02f;
                if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionZ *= 1.05;
                    mc.thePlayer.motionX *= 1.05;
                    mc.thePlayer.motionY = MoveUtil.getJumpMotion(0.4F);
                }


                break;
            }
            case "Invadedlands": {

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = 0.1;
                    MoveUtil.strafe(5);
                } else {
                    MoveUtil.stop();
                    MoveUtil.strafe(MoveUtil.getBaseMoveSpeedOther() * 0.4);
                }

                break;
            }

            case "Intave":
                if (mc.thePlayer.onGround) {
                    MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() * 1.2);
                    mc.thePlayer.jump();
                } else if (offGroundTicks == 4) {
                    mc.thePlayer.motionY = -0.0784000015258789;
                }

                MoveUtil.strafe();
                break;
            case "Mineplex":
                if (mc.thePlayer.onGround) {
                    MoveUtil.strafe(0);
                }
                if (ticks < 0) {
                    Rise.addChatMessage("gliding");
//                    mc.thePlayer.motionY += 0.0355f;
                }
                break;
            case "Minemenclub Hop":

                if (mmcBalanceTimer.isEnabled()) {
                    if (balance < -(mc.timer.timerSpeed * 20) - 20) {
                        //Allow timer :)
                        mc.timer.timerSpeed = (float) mmc.getValue() + (float) (Math.random() / 10);
                        if (mc.thePlayer.ticksExisted % 30 == 0) mc.timer.timerSpeed = 0.9f;
                    } else {
                        mc.timer.timerSpeed = 1;
                    }
                }

                break;
        }

        if (mode.is("Verus"))
            switch (verusMode.getMode()) {
                case "Hop":
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        MoveUtil.strafe(0.55);
                    } else {
                        MoveUtil.strafe(0.349);
                    }

                    if (mc.thePlayer.fallDistance > 2) ticks = 0;

                    if (damageBoost.isEnabled() && mc.thePlayer.hurtTime > 0 && ticks > 15) {
                        MoveUtil.strafe(1);
                    }
                    break;

                case "Float": {
                    if (!mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (mc.thePlayer.onGround) {
                            event.setGround(true);
                            ticks = 0;
                            MoveUtil.strafe(0.44f);
                            mc.thePlayer.motionY = 0.42F;
                            mc.timer.timerSpeed = 2.1f;

                        } else {

                            if (ticks >= 10) {
                                verusEpicBypassBooleanTM = true;
                                MoveUtil.strafe(0.2865f);
                                return;
                            }

                            if (verusEpicBypassBooleanTM) {
                                if (ticks <= 1) {
                                    MoveUtil.strafe(0.45f);
                                }

                                if (ticks >= 2) {
                                    MoveUtil.strafe(0.69f - (ticks - 2) * 0.019f);
                                }
                            }

                            mc.thePlayer.motionY = 0;
                            mc.timer.timerSpeed = 0.9f;

                            event.setGround(true);
                            mc.thePlayer.onGround = true;
                        }
                    }

                    break;
                }
            }
    }

//    @Override
//    public void onRender3DEvent(final Render3DEvent event) {
//        if (mode.is("Hypixel")) {
//            targetYaw = (float) (MoveUtil.getDirection() * (180 / Math.PI));
//            this.calculateHypixelYaw();
//        }
//    }
//
//    private void calculateHypixelYaw() {
//        final int fps = (int) (Minecraft.getDebugFPS() / 20.0F);
//
//        final float rotationSpeed = 15 * 6F / fps;
//
//        final float deltaYaw = (((targetYaw - lastYaw) + 540) % 360) - 180;
//
//        final float distanceYaw = MathHelper.clamp_float(deltaYaw, -rotationSpeed, rotationSpeed);
//
//        yaw = lastYaw + distanceYaw;
//
//        final float[] currentRotations = new float[]{yaw, mc.thePlayer.rotationPitch};
//        final float[] lastRotations = new float[]{lastYaw, mc.thePlayer.rotationPitch};
//
//        final float[] fixedRotations = RotationUtil.getFixedRotation(currentRotations, lastRotations);
//
//        yaw = fixedRotations[0];
//    }

    public static double round(final double value, final int scale, final double inc) {
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;

        if (value >= floored + halfOfInc)
            return new BigDecimal(Math.ceil(value / inc) * inc)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        else return new BigDecimal(floored)
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public void onMoveButton(final MoveButtonEvent event) {
        if (ticksDisable <= 20) {
            return;
        }

        switch (mode.getMode()) {
            case "Hypixel":
            case "Vulcan":
            case "Minemenclub Hop":
                event.setJump(false);
                event.setSneak(false);
                break;
        }
    }

    @Override
    public void onMove(final MoveEvent event) {

        if (ticksDisable <= 20) {
            return;
        }

        if (mode.is("Verus"))
            switch (verusMode.getMode()) {
                case "YPort":
                    if (MoveUtil.isMoving()) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            MoveUtil.strafe(0.69);
                            mc.thePlayer.motionY = 0;
                            event.setY(0.42F);
                        }
                        MoveUtil.strafe(0.41);
                    }
                    break;
            }

        switch (mode.getMode()) {
            case "Mineplex":

                if (!MoveUtil.isMoving()) {
                    if (mc.thePlayer.onGround) stage = 0;
                    return;
                }

                if (mc.thePlayer.isCollidedHorizontally) {
                    stage = 0;
                }

                if (mc.thePlayer.onGround) {
                    event.setY(mc.thePlayer.motionY = 0.42f);
                    stage++;
                }

                if (offGroundTicks == 1) {
                    final float nextSpeed = 0.2f;
                    speed += MoveUtil.getBaseMoveSpeed() / 2;
                    if (speed > 1) speed = 1;
                }

                speed *= 0.985;

//                if (offGroundTicks > 1 && offGroundTicks <= 15) mc.thePlayer.motionY += 0.02999;
//
//                if (offGroundTicks == 1) mc.thePlayer.motionY += 0.04f;
//
//                if (offGroundTicks == 17) mc.thePlayer.motionY -= 0.04f;

                if (stage >= 1 && !mc.thePlayer.onGround) {
                    final double baseSpeed = Math.max(MoveUtil.getBaseMoveSpeedOther(), speed);
                    MoveUtil.setMoveEventSpeed(event, (Math.max(speed, baseSpeed)));
                }

                break;
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (ticksDisable <= 20) {
            return;
        }

        final Packet<?> p = event.getPacket();

        switch (mode.getMode()) {
            case "Custom":
                if (cancelC0F.isEnabled()) {
                    if (p instanceof C0FPacketConfirmTransaction) {
                        event.setCancelled(true);
                    }
                }

                if (cancelC00.isEnabled()) {
                    if (p instanceof C00PacketKeepAlive) {
                        event.setCancelled(true);
                    }
                }
                break;
        }

        if (!mode.is("Timer Bypass") || mc.isIntegratedServerRunning() || mc.thePlayer == null || mc.thePlayer.isDead)
            return;

        if (mc.thePlayer.ticksExisted < 10) {
            packets.clear();
            mc.timer.timerSpeed = 1;
            return;
        }

        mc.timer.timerSpeed = (float) speedTimer.getValue();

        packets.add(p);

        event.setCancelled(true);

        if (!packets.isEmpty() && mc.thePlayer.ticksExisted % (int) pulse.getValue() != 0) return;

        packets.forEach(PacketUtil::sendPacketWithoutEvent);
        packets.clear();

    }

    @Override
    public void onStrafe(final StrafeEvent event) {
        if (ticksDisable <= 20) {
            return;
        }

        switch (mode.getMode()) {
            case "Minemenclub Hop":
                if (MoveUtil.isMoving() && mc.thePlayer.hurtTime < 7) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }

                    MoveUtil.strafe();
                }
                break;

            case "Vulcan":
                if (offGroundTicks <= 2) MoveUtil.strafe();
                break;

            case "Cubecraft":
                if (MoveUtil.isMoving()) {
                    final double baseSpeed = MoveUtil.getBaseMoveSpeed();
                    switch (offGroundTicks) {
                        case 0:
                            if (mc.thePlayer.onGround) {
                                /*
                                 * Sets speed on ground
                                 */
                                speed = baseSpeed * (1.9 - (Math.random() / 500));

                                /*
                                 * Automatically jumps
                                 */
                                mc.thePlayer.motionY = MoveUtil.getJumpMotion(0.42F);
                            }
                            break;

                        case 5:
                            /*
                             * Makes you go down faster
                             */
                            mc.thePlayer.motionY = MoveUtil.getPredictedMotionY(mc.thePlayer.motionY);
                            break;
                    }

                    /*
                     * Sets the timer speed
                     */
                    mc.timer.timerSpeed = (float) (1.2 - Math.random() / 100);

                    /*
                     * Sets the players speed
                     */
                    if (mc.thePlayer.onGround) {
                        event.setFriction((float) Math.max(baseSpeed, event.getForward() != 0 && event.getStrafe() != 0 ? speed * 0.91F : speed));
                        MoveUtil.stop();
                    } else
                        MoveUtil.strafe();
                } else if (mc.currentScreen == null) {
                    MoveUtil.stop();
                    mc.timer.timerSpeed = 1;
                }
                break;

            case "Hypixel2":
                mc.timer.timerSpeed = (float) (hypixelTimer.getValue() - Math.random() / 50);

                mc.thePlayer.speedInAir = 0.02f;

                if (mc.thePlayer.onGround) {
                    MoveUtil.strafe(MoveUtil.getBaseMoveSpeed() - Math.random() / 100f);
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        MoveUtil.strafe(MoveUtil.getSpeed() * 1.34 - Math.random() / 400f);
                    }
                    mc.thePlayer.jump();
                } else if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    mc.thePlayer.speedInAir = (float) (0.03f - Math.random() / 400f);
                }

                double motionZ = mc.thePlayer.motionZ;
                double motionX = mc.thePlayer.motionX;

                MoveUtil.strafe();

                if (mc.thePlayer.speedInAir > 0.02) {
                    mc.thePlayer.motionZ = (motionZ + mc.thePlayer.motionZ * 5) / 6;
                    mc.thePlayer.motionX = (motionX + mc.thePlayer.motionX * 5) / 6;
                } else {
                    mc.thePlayer.motionZ = (motionZ + mc.thePlayer.motionZ * 7) / 8;
                    mc.thePlayer.motionX = (motionX + mc.thePlayer.motionX * 7) / 8;
                }

                if (mc.thePlayer.fallDistance > 0) mc.thePlayer.motionY += 0.014f - Math.random() / 100f;
                break;

            case "Teleport":
                if (strafe.isEnabled()) MoveUtil.strafe();
                break;

            case "NCP Latest":
                mc.timer.timerSpeed = (float) timerNcp.getValue();

                if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = MoveUtil.getJumpMotion((float) (0.4 + Math.random() / 500));

                    mc.thePlayer.speedInAir = (float) speedInAirNcp.getValue();
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 2) {
                        mc.thePlayer.motionX *= speedMultiplierNCPSpeed2.getValue();
                        mc.thePlayer.motionZ *= speedMultiplierNCPSpeed2.getValue();
                        mc.thePlayer.speedInAir = (float) speedInAirNcpSpeed2.getValue();
                    } else if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) == 1) {
                        mc.thePlayer.motionX *= speedMultiplierNCPSpeed1.getValue();
                        mc.thePlayer.motionZ *= speedMultiplierNCPSpeed1.getValue();
                        mc.thePlayer.speedInAir = (float) speedInAirNcpSpeed1.getValue();
                    } else {
                        mc.thePlayer.motionX *= speedMultiplierNCP.getValue();
                        mc.thePlayer.motionZ *= speedMultiplierNCP.getValue();
                    }
                }

                if (MoveUtil.isMoving() && smartTimer.isEnabled()) {
                    float timerSpeed = (float) (1.1 + 0.5 - MoveUtil.getSpeed());

                    if (!jumped) {
                        timerSpeed = (float) (1.337 - MoveUtil.getSpeed());
                    }

                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        timerSpeed += (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) / 12f;
                    }

                    if (timerSpeed > 1.5) timerSpeed = 1.5f;
                    if (timerSpeed < 0.6) timerSpeed = 0.6f;
                    mc.timer.timerSpeed = timerSpeed;
                }

                MoveUtil.strafe();

                break;

            case "Hypixel":
                MoveUtil.strafe();
                break;
        }
    }

    public float getMovementDirection(float forward, float strafing, float yaw) {
        if (forward == 0.0F && strafing == 0.0F) return yaw;
        boolean reversed = (forward < 0.0F);
        float strafingYaw = 90.0F * ((forward > 0.0F) ? 0.5F : (reversed ? -0.5F : 1.0F));
        if (reversed) yaw += 180.0F;
        if (strafing > 0.0F) {
            yaw -= strafingYaw;
        } else if (strafing < 0.0F) {
            yaw += strafingYaw;
        }
        return yaw;
    }

    private double lowHopYModification(final double baseMotionY, final double yDistFromGround) {
        if (yDistFromGround == lowhopy[0]) {
            return 0.31;
        } else if (yDistFromGround == lowhopy[1]) {
            return 0.04;
        } else if (yDistFromGround == lowhopy[2]) {
            return -0.2;
        } else if (yDistFromGround == lowhopy[3]) {
            return -0.14;
        } else if (yDistFromGround == lowhopy[4]) {
            return -0.2;
        }
        return baseMotionY;
    }

    public static final double[] lowhopy = {
            round(0.4, 3, 0.001),
            round(0.71, 3, 0.001),
            round(0.75, 3, 0.001),
            round(0.55, 3, 0.001),
            round(0.41, 3, 0.001),
    };


    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        switch (mode.getMode()) {
            case "Minemenclub Hop": {
                if (mmcBalanceTimer.isEnabled()) {
                    final ScaledResolution sr = new ScaledResolution(mc);
                    CustomFont.drawCenteredString("Balance: " + balance + ((balance > (-mc.timer.timerSpeed * 20) - 20) ? " get this number below -10 by using timer or modules with timer" : ""), sr.getScaledWidth() / 2d, sr.getScaledHeight() - 60, Color.WHITE.getRGB());
                }
                break;
            }
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (ticksDisable <= 20) {
            return;
        }

        final Packet<?> p = event.getPacket();
        if (mc.thePlayer.ticksExisted <= 1)
            return;

        switch (mode.getMode()) {
            case "Zonecraft":
                if (mc.thePlayer.ticksExisted > 20) {
                    if (p instanceof S08PacketPlayerPosLook) {
                        event.setCancelled(true);
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((S08PacketPlayerPosLook) p).x, ((S08PacketPlayerPosLook) p).y, ((S08PacketPlayerPosLook) p).z, true));
                    }
                }
                break;

            case "Minemenclub Hop":
                if (p instanceof S08PacketPlayerPosLook) {
                    balance = 0;
                }
                break;

            case "Vulcan2":
                if (p instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 20) {
                    S08PacketPlayerPosLook s08 = ((S08PacketPlayerPosLook) p);
                    if (mc.thePlayer.getDistanceSq(s08.getX(), s08.getY(), s08.getZ()) < 25 * 4) {
                        event.setCancelled(true);
                    }
                }
                break;
        }

        if (p instanceof S08PacketPlayerPosLook && disableOnFlag.isEnabled()) {
            ticksDisable = 0;

            onDisable();
            this.registerNotification("Temporarily disabled speed due to a teleport.");
        }

        if (event.getPacket() instanceof S00PacketDisconnect) {
            balance = 0;
        }
    }
}