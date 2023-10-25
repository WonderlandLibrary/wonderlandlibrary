/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ru.wendoxd.wclassguard.WXFuscator
 */
package org.celestial.client.feature.impl.movement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.motion.EventMove;
import org.celestial.client.event.events.impl.packet.EventAttackSilent;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import ru.wendoxd.wclassguard.WXFuscator;

public class Speed
extends Feature {
    public static int stage;
    public TimerHelper timerHelper = new TimerHelper();
    private final BooleanSetting strafing;
    private final BooleanSetting boost;
    private final List<Packet<?>> packets;
    private final LinkedList<double[]> positions;
    private final TimerHelper pulseTimer = new TimerHelper();
    private boolean disableLogger;
    private final BooleanSetting NCPLowHop;
    private final BooleanSetting potionCheck;
    private final BooleanSetting matrixDisabler;
    private final BooleanSetting sunriseDisabler;
    public double moveSpeed;
    public static ListSetting speedMode;
    public BooleanSetting autoHitDisable;
    public NumberSetting jumpMoveFactor = new NumberSetting("JumpMovementFactor", 0.15f, 0.01f, 0.5f, 0.001f, new Supplier<Boolean>(){

        @Override
        public Boolean get() {
            return Speed.speedMode.currentMode.equals("Custom");
        }
    });
    public BooleanSetting onGround = new BooleanSetting("Ground Only", false, new Supplier<Boolean>(){

        @Override
        public Boolean get() {
            return Speed.speedMode.currentMode.equals("Custom");
        }
    });
    public NumberSetting onGroundSpeed = new NumberSetting("Ground Speed", 0.5f, 0.001f, 10.0f, 0.01f, new Supplier<Boolean>(){

        @Override
        public Boolean get() {
            return Speed.speedMode.currentMode.equals("Custom") && Speed.this.onGround.getCurrentValue();
        }
    });
    public BooleanSetting blink = new BooleanSetting("Blink", false, new Supplier<Boolean>(){

        @Override
        public Boolean get() {
            return Speed.speedMode.currentMode.equals("Custom");
        }
    });
    public BooleanSetting timerExploit = new BooleanSetting("Timer Exploit", false, new Supplier<Boolean>(){

        @Override
        public Boolean get() {
            return !Speed.speedMode.currentMode.equals("Matrix Old") && !Speed.speedMode.currentMode.equals("Sunrise Boost");
        }
    });
    public NumberSetting timer = new NumberSetting("Timer", 1.0f, 0.1f, 10.0f, 0.1f, new Supplier<Boolean>(){

        @Override
        public Boolean get() {
            return Speed.speedMode.currentMode.equals("Custom") && !Speed.this.timerExploit.getCurrentValue();
        }
    });
    private int boostTick;
    public NumberSetting motionMultiplier;
    public NumberSetting motion;
    public NumberSetting motion1;
    private boolean slowDownHop;
    private double less;
    private double stair;
    public static boolean isDamagedByBow;
    public boolean isDamaged;
    private int moveTicks;
    private final TimerHelper sendTimer = new TimerHelper();

    public Speed() {
        super("Speed", "\u0423\u0432\u0435\u043b\u0438\u0447\u0438\u0432\u0430\u0435\u0442 \u0432\u0430\u0448\u0443 \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c", Type.Movement);
        speedMode = new ListSetting("Speed Mode", "Matrix", "Matrix", "Matrix Old", "Matrix 6.0.2", "Matrix Fast", "Really World", "NCP YPort", "Sunrise NoDamage", "Sunrise Damage", "Sunrise Disabler", "Sunrise Timer", "Sunrise Ground", "Sunrise YPort", "Sunrise Boost", "Old Sunrise", "NCP", "YPort", "Strafe", "Custom");
        this.strafing = new BooleanSetting("Strafing", false, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return !Speed.speedMode.currentMode.equals("Sunrise Boost") && !Speed.speedMode.currentMode.equals("Sunrise Damage");
            }
        });
        this.boost = new BooleanSetting("Boost", true, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Speed.speedMode.currentMode.equals("Custom") || Speed.speedMode.currentMode.equals("Matrix Old");
            }
        });
        this.potionCheck = new BooleanSetting("Speed Potion Check", false, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Speed.speedMode.currentMode.equals("Custom");
            }
        });
        this.NCPLowHop = new BooleanSetting("NCP Low Hop", false, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Speed.speedMode.currentMode.equals("NCP");
            }
        });
        this.autoHitDisable = new BooleanSetting("Auto Hit Disable", false);
        this.motionMultiplier = new NumberSetting("Motion Multiplier", 0.3f, 0.05f, 0.3f, 0.01f, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Speed.speedMode.currentMode.equals("Matrix") || Speed.speedMode.currentMode.equals("Matrix Fast") || Speed.speedMode.currentMode.equals("Really World");
            }
        });
        this.motion = new NumberSetting("Motion", 1.6f, 1.1f, 5.0f, 0.1f, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Speed.speedMode.currentMode.equals("Sunrise Disabler") || Speed.speedMode.currentMode.equals("Sunrise Boost") || Speed.speedMode.currentMode.equals("Sunrise NoDamage");
            }
        });
        this.motion1 = new NumberSetting("Sunrise Motion", 0.7f, 0.1f, 1.0f, 0.01f, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Speed.speedMode.currentMode.equals("Sunrise Damage");
            }
        });
        this.matrixDisabler = new BooleanSetting("Matrix Disabler", true, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Speed.speedMode.currentMode.equals("Custom");
            }
        });
        this.sunriseDisabler = new BooleanSetting("Sunrise Disabler", true, new Supplier<Boolean>(){

            @Override
            public Boolean get() {
                return Speed.speedMode.currentMode.equals("Custom");
            }
        });
        this.packets = new ArrayList();
        this.positions = new LinkedList();
        this.addSettings(speedMode, this.strafing, this.boost, this.motion, this.motion1, this.motionMultiplier, this.NCPLowHop, this.jumpMoveFactor, this.onGroundSpeed, this.onGround, this.sunriseDisabler, this.matrixDisabler, this.blink, this.timerExploit, this.timer, this.potionCheck, this.autoHitDisable);
    }

    @EventTarget
    public void onMove(EventMove eventMove) {
        if (Speed.speedMode.currentMode.equals("Sunrise Ground")) {
            MovementHelper.setSpeed(MovementHelper.getSpeed());
            if (!MovementHelper.isMoving() || Speed.mc.player.movementInput.jump || !Speed.mc.gameSettings.keyBindForward.isKeyDown()) {
                return;
            }
            if (Speed.mc.player.onGround) {
                float value = Speed.mc.player.rotationYaw * ((float)Math.PI / 180);
                Speed.mc.player.motionX -= Math.sin(value) * (double)0.2075f;
                Speed.mc.player.motionZ += Math.cos(value) * (double)0.2075f;
                eventMove.setX(Speed.mc.player.motionX);
                eventMove.setY(1.0E-9);
                eventMove.setZ(Speed.mc.player.motionZ);
            }
        } else if (Speed.speedMode.currentMode.equals("Sunrise NoDamage")) {
            Speed.mc.player.onGround = false;
            if (Speed.mc.player.isOnLadder() || Speed.mc.player.isInWater() || Speed.mc.player.isInLava() || Speed.mc.player.isInWeb || !MovementHelper.isMoving()) {
                return;
            }
            if (Speed.mc.player.movementInput.jump) {
                return;
            }
            if (!(Speed.mc.world.getBlockState(new BlockPos(Speed.mc.player.posX, Speed.mc.player.posY - 0.01, Speed.mc.player.posZ)).getBlock() instanceof BlockAir)) {
                MovementHelper.setSpeed(this.motion.getCurrentValue());
            }
        } else if (Speed.speedMode.currentMode.equals("Sunrise Damage")) {
            float value = Speed.mc.player.rotationYaw * ((float)Math.PI / 180);
            MovementHelper.strafe(MovementHelper.getSpeed());
            this.moveTicks = MovementHelper.isMoving() ? ++this.moveTicks : 0;
            if (!MovementHelper.isMoving()) {
                return;
            }
            if (Speed.mc.player.hurtTime > 0) {
                this.isDamaged = true;
            }
            if (!this.isDamaged) {
                return;
            }
            if (!(Speed.mc.player.isInLiquid() || !Speed.mc.player.movementInput.jump && Speed.mc.player.onGround)) {
                return;
            }
            if (Speed.mc.gameSettings.keyBindForward.isKeyDown() && Speed.mc.world.getBlockState(new BlockPos(Speed.mc.player.posX, Speed.mc.player.posY - 0.001, Speed.mc.player.posZ)).getBlock() != Blocks.AIR) {
                Speed.mc.player.motionX -= Math.sin(value) * (double)this.motion1.getCurrentValue();
                Speed.mc.player.motionZ += Math.cos(value) * (double)this.motion1.getCurrentValue();
            }
        } else if (Speed.speedMode.currentMode.equals("NCP")) {
            if (this.timerExploit.getCurrentValue()) {
                Speed.mc.timer.timerSpeed = Speed.mc.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
            }
            Speed.mc.player.jumpMovementFactor = (float)((double)Speed.mc.player.jumpMovementFactor * 1.04);
            boolean collided = Speed.mc.player.isCollidedHorizontally;
            if (collided) {
                stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.3;
            }
            this.less -= this.less > 1.0 ? 0.24 : 0.17;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!Speed.mc.player.isInWater() && Speed.mc.player.onGround && MovementHelper.isMoving()) {
                collided = Speed.mc.player.isCollidedHorizontally;
                if (stage >= 0 || collided) {
                    stage = 0;
                    float motY = this.NCPLowHop.getCurrentValue() ? 0.33f : 0.42f;
                    Speed.mc.player.motionY = motY;
                    if (this.stair == 0.0) {
                        eventMove.setY(motY);
                    }
                    this.less += 1.0;
                    boolean bl = this.slowDownHop = this.less > 1.0 && !this.slowDownHop;
                    if (this.less > 1.15) {
                        this.less = 1.15;
                    }
                }
            }
            this.moveSpeed = this.getCurrentSpeed(stage) + 0.0335;
            this.moveSpeed *= 0.85;
            if (this.stair > 0.0) {
                this.moveSpeed *= 1.0;
            }
            if (this.slowDownHop) {
                this.moveSpeed *= 0.8575;
            }
            if (Speed.mc.player.isInWater()) {
                this.moveSpeed = 0.351;
            }
            if (MovementHelper.isMoving()) {
                MovementHelper.setEventSpeed(eventMove, this.moveSpeed);
            }
            ++stage;
        }
    }

    @EventTarget
    public void onAttackSilent(EventAttackSilent event) {
        if (Speed.speedMode.currentMode.equals("Matrix Old")) {
            event.setCancelled(true);
            NotificationManager.publicity("Speed", "You can't attack entity with this speed mode!", 3, NotificationType.ERROR);
        }
        if (this.autoHitDisable.getCurrentValue()) {
            this.toggle();
            NotificationManager.publicity("Speed", "Speed automatically disabled because of hit!", 3, NotificationType.ERROR);
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (Speed.speedMode.currentMode.equals("Sunrise NoDamage")) {
                event.setCancelled(true);
            }
            if (Speed.speedMode.currentMode.equals("Sunrise Boost")) {
                event.setCancelled(true);
            }
            if (Speed.speedMode.currentMode.equals("Custom") && this.sunriseDisabler.getCurrentValue()) {
                event.setCancelled(true);
            }
        }
        if (Speed.speedMode.currentMode.equals("Matrix Old") && event.getPacket() instanceof SPacketUpdateHealth) {
            event.setCancelled(true);
        }
    }

    @WXFuscator
    @EventTarget
    public void onPre(EventPreMotion event) {
        if (Speed.speedMode.currentMode.equals("Sunrise Disabler")) {
            if (this.strafing.getCurrentValue()) {
                MovementHelper.setSpeed(MovementHelper.getSpeed());
            }
            if (Speed.mc.player.isOnLadder() || Speed.mc.player.isInWater() || Speed.mc.player.isInLava() || Speed.mc.player.isInWeb || !MovementHelper.isMoving()) {
                return;
            }
            Speed.mc.timer.timerSpeed = 1.2f;
            if (Speed.mc.player.onGround) {
                Speed.mc.player.posY += 2.542;
                Speed.mc.player.connection.sendPacket(new CPacketEntityAction(Speed.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                Speed.mc.player.jump();
            }
            if (Speed.mc.player.motionY == 0.0030162615090425808) {
                Speed.mc.player.jumpMovementFactor = this.motion.getCurrentValue();
                Speed.mc.player.posY -= 0.45;
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (this.getState()) {
            String mode = speedMode.getOptions();
            this.setSuffix(mode);
            if (mode.equalsIgnoreCase("YPort")) {
                if (MovementHelper.isMoving()) {
                    if (this.strafing.getCurrentValue()) {
                        MovementHelper.strafePlayer();
                    }
                    if (Speed.mc.player.onGround) {
                        MovementHelper.setSpeed(0.5f);
                        Speed.mc.player.motionY = 0.05;
                    }
                }
            } else if (mode.equalsIgnoreCase("Strafe")) {
                if (MovementHelper.isMoving()) {
                    if (Speed.mc.player.onGround) {
                        Speed.mc.player.jump();
                    }
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
            } else if (mode.equalsIgnoreCase("Custom")) {
                if (!Speed.mc.player.isPotionActive(MobEffects.SPEED) && this.potionCheck.getCurrentValue()) {
                    return;
                }
                if (this.blink.getCurrentValue() && this.pulseTimer.hasReached(600.0)) {
                    this.blink();
                    this.pulseTimer.reset();
                }
                if (this.matrixDisabler.getCurrentValue() && this.sendTimer.hasReached(300.0)) {
                    Speed.mc.player.connection.sendPacket(new CPacketEntityAction(Speed.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    this.sendTimer.reset();
                }
                if (Speed.mc.player.onGround && !this.onGround.getCurrentValue() && MovementHelper.isMoving()) {
                    if (this.boost.getCurrentValue()) {
                        Speed.mc.player.onGround = false;
                    }
                    Speed.mc.player.jump();
                }
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                Speed.mc.player.jumpMovementFactor = this.jumpMoveFactor.getCurrentValue();
                if (this.onGround.getCurrentValue()) {
                    MovementHelper.setSpeed(this.onGroundSpeed.getCurrentValue());
                }
                if (!this.timerExploit.getCurrentValue()) {
                    Speed.mc.timer.timerSpeed = this.timer.getCurrentValue();
                }
                if (this.timerExploit.getCurrentValue()) {
                    Speed.mc.timer.timerSpeed = Speed.mc.player.ticksExisted % 60 > 39 ? 1000.0f : 1.0f;
                }
            } else if (mode.equalsIgnoreCase("NCP YPort")) {
                if (MovementHelper.isMoving()) {
                    Speed.mc.timer.timerSpeed = 1.05f;
                    if (Speed.mc.player.onGround) {
                        Speed.mc.player.motionY = 0.4f;
                        if (!Speed.mc.player.isPotionActive(Potion.getPotionById(1))) {
                            MovementHelper.setSpeed(0.34f);
                        } else {
                            if (Speed.mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier() == 0) {
                                MovementHelper.setSpeed(0.49f);
                            }
                            if (Speed.mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier() == 1) {
                                MovementHelper.setSpeed(0.575f);
                            }
                            if (Speed.mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier() == 2) {
                                MovementHelper.setSpeed(0.73f);
                            }
                        }
                    } else {
                        Speed.mc.player.motionY = -100.0;
                    }
                }
            } else if (mode.equalsIgnoreCase("Matrix")) {
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                if (MovementHelper.isMoving()) {
                    if (Speed.mc.player.isInWeb || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid()) {
                        return;
                    }
                    if (this.timerExploit.getCurrentValue()) {
                        Speed.mc.timer.timerSpeed = Speed.mc.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
                    }
                    Speed.mc.player.jumpMovementFactor = (float)((double)Speed.mc.player.jumpMovementFactor * 1.04);
                    double x = Speed.mc.player.posX;
                    double y = Speed.mc.player.posY;
                    double z = Speed.mc.player.posZ;
                    double yaw = (double)Speed.mc.player.rotationYaw * 0.017453292;
                    if (Speed.mc.player.onGround) {
                        this.boostTick = 11;
                        Speed.mc.timer.timerSpeed = 1.05f;
                        MovementHelper.strafePlayer();
                        Speed.mc.player.jump();
                    } else if (this.boostTick < 11) {
                        ++this.boostTick;
                    } else {
                        if (this.timerHelper.hasReached(460.0)) {
                            Speed.mc.player.onGround = false;
                            Speed.mc.player.motionX *= (double)(1.0f + this.motionMultiplier.getCurrentValue()) + 0.4;
                            Speed.mc.player.motionZ *= (double)(1.0f + this.motionMultiplier.getCurrentValue()) + 0.4;
                            Speed.mc.player.setPosition(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
                            this.timerHelper.reset();
                        }
                        this.boostTick = 0;
                    }
                }
            } else if (mode.equalsIgnoreCase("Really World")) {
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                if (MovementHelper.isMoving()) {
                    if (Speed.mc.player.isInWeb || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid()) {
                        return;
                    }
                    if (this.timerExploit.getCurrentValue()) {
                        Speed.mc.timer.timerSpeed = Speed.mc.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
                    }
                    Speed.mc.player.jumpMovementFactor = (float)((double)Speed.mc.player.jumpMovementFactor * 1.04);
                    double x = Speed.mc.player.posX;
                    double y = Speed.mc.player.posY;
                    double z = Speed.mc.player.posZ;
                    double yaw = (double)Speed.mc.player.rotationYaw * 0.017453292;
                    if (Speed.mc.player.onGround) {
                        Speed.mc.timer.timerSpeed = 1.3f;
                        this.boostTick = 11;
                        MovementHelper.strafePlayer();
                        Speed.mc.player.jump();
                    } else if (this.boostTick < 11) {
                        ++this.boostTick;
                    } else {
                        if (this.timerHelper.hasReached(260.0)) {
                            Speed.mc.player.onGround = false;
                            Speed.mc.player.motionX *= (double)(1.0f + this.motionMultiplier.getCurrentValue()) + 0.4;
                            Speed.mc.player.motionZ *= (double)(1.0f + this.motionMultiplier.getCurrentValue()) + 0.4;
                            Speed.mc.player.setPosition(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
                            this.timerHelper.reset();
                        }
                        this.boostTick = 0;
                    }
                }
            } else if (mode.equalsIgnoreCase("Sunrise Timer")) {
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                if (!Speed.mc.player.isInLiquid() && MovementHelper.isMoving()) {
                    Speed.mc.player.jumpMovementFactor = 0.0311f;
                    if (Speed.mc.player.onGround) {
                        Speed.mc.player.jump();
                    }
                    Speed.mc.timer.timerSpeed = !Speed.mc.player.onGround && (double)Speed.mc.player.fallDistance <= 0.1 ? 1.1f : 1.0f;
                }
            } else if (mode.equalsIgnoreCase("Sunrise YPort")) {
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                if (Speed.mc.player.isOnLadder() || Speed.mc.player.isInWater() || Speed.mc.player.isInLava() || Speed.mc.player.isInWeb || !MovementHelper.isMoving()) {
                    return;
                }
                Speed.mc.player.speedInAir = 0.0235f;
                if (Speed.mc.player.onGround) {
                    Speed.mc.player.jump();
                } else {
                    Speed.mc.player.motionY = -1000.0;
                }
            } else if (mode.equalsIgnoreCase("Matrix 6.0.2")) {
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                Speed.mc.gameSettings.keyBindJump.pressed = false;
                double x = Speed.mc.player.posX;
                double y = Speed.mc.player.posY;
                double z = Speed.mc.player.posZ;
                double yaw = (double)Speed.mc.player.rotationYaw * 0.017453292;
                if (MovementHelper.isMoving() && (double)Speed.mc.player.fallDistance < 0.1) {
                    boolean isRuneAndSpeed;
                    ItemStack stack = Speed.mc.player.getHeldItemOffhand();
                    if (Speed.mc.player.isInWeb || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid() || Speed.mc.player.isCollidedHorizontally) {
                        return;
                    }
                    if (this.timerExploit.getCurrentValue()) {
                        Speed.mc.timer.timerSpeed = Speed.mc.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
                    }
                    boolean isSpeed = Speed.mc.player.isPotionActive(MobEffects.SPEED);
                    boolean isRune = stack.getItem() == Items.FIREWORK_CHARGE && stack.getDisplayName().contains("\u043d\u0435\u0431\u0435\u0441\u043d\u044b\u0445 \u0432\u0440\u0430\u0442");
                    boolean bl = isRuneAndSpeed = stack.getItem() == Items.FIREWORK_CHARGE && stack.getDisplayName().contains("\u043d\u0435\u0431\u0435\u0441\u043d\u044b\u0445 \u0432\u0440\u0430\u0442") && Speed.mc.player.isPotionActive(MobEffects.SPEED);
                    if (Speed.mc.player.onGround) {
                        this.boostTick = 8;
                        Speed.mc.player.jump();
                    } else if (this.boostTick < 8) {
                        Speed.mc.player.jumpMovementFactor *= 1.04f;
                        if (this.boostTick == 0) {
                            double motion = isRuneAndSpeed ? 1.67 : (isSpeed ? 1.7 : (isRune ? 1.7 : 1.7605));
                            Speed.mc.player.motionX *= motion;
                            Speed.mc.player.motionZ *= motion;
                            Speed.mc.player.setPosition(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
                        } else {
                            Speed.mc.player.motionY -= 0.0098;
                        }
                        ++this.boostTick;
                    } else {
                        this.boostTick = 0;
                    }
                }
            } else if (mode.equalsIgnoreCase("Matrix Fast")) {
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                if (MovementHelper.isMoving()) {
                    if (Speed.mc.player.isInWeb || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid()) {
                        return;
                    }
                    if (this.timerExploit.getCurrentValue()) {
                        Speed.mc.timer.timerSpeed = Speed.mc.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
                    }
                    Speed.mc.player.jumpMovementFactor = (float)((double)Speed.mc.player.jumpMovementFactor * 1.04);
                    if (Speed.mc.player.onGround) {
                        this.boostTick = 11;
                        Speed.mc.timer.timerSpeed = 1.05f;
                        Speed.mc.player.jump();
                    } else {
                        this.boostTick = this.boostTick < 11 ? ++this.boostTick : 0;
                    }
                    if (this.timerHelper.hasReached(450.0)) {
                        Speed.mc.player.onGround = false;
                        Speed.mc.player.motionX *= (double)(1.0f + this.motionMultiplier.getCurrentValue());
                        Speed.mc.player.motionZ *= (double)(1.0f + this.motionMultiplier.getCurrentValue());
                        this.timerHelper.reset();
                    }
                }
            } else if (mode.equalsIgnoreCase("Old Sunrise")) {
                Speed.mc.gameSettings.keyBindJump.pressed = false;
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                if (MovementHelper.isMoving() && (double)Speed.mc.player.fallDistance < 0.1 && !Speed.mc.player.isCollidedHorizontally) {
                    if (this.timerExploit.getCurrentValue()) {
                        float f = Speed.mc.timer.timerSpeed = Speed.mc.player.ticksExisted % 60 > 39 ? 100.0f : 1.0f;
                    }
                    if (Speed.mc.player.onGround) {
                        Speed.mc.player.jump();
                    } else {
                        if (Speed.mc.player.motionY == 0.33319999363422365) {
                            Speed.mc.player.motionX *= Speed.mc.player.isPotionActive(MobEffects.SPEED) ? 1.7 : 1.73;
                            Speed.mc.player.motionZ *= Speed.mc.player.isPotionActive(MobEffects.SPEED) ? 1.7 : 1.73;
                        }
                        if (Speed.mc.player.motionY == 0.24813599859094576) {
                            Speed.mc.player.motionX *= 1.19;
                            Speed.mc.player.motionZ *= 1.19;
                        }
                    }
                }
            } else if (mode.equalsIgnoreCase("Matrix Old")) {
                Speed.mc.gameSettings.keyBindJump.pressed = false;
                if (this.strafing.getCurrentValue()) {
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
                if (MovementHelper.isMoving()) {
                    if (Speed.mc.player.onGround) {
                        Speed.mc.player.jump();
                    }
                    if (!Speed.mc.player.onGround && Speed.mc.player.fallDistance <= 0.1f) {
                        Speed.mc.timer.timerSpeed = 1.21f;
                    }
                    if (Speed.mc.player.fallDistance > 0.1f && Speed.mc.player.fallDistance < 1.3f) {
                        Speed.mc.timer.timerSpeed = 1.0f;
                    }
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = speedMode.getOptions();
        if (Speed.mc.player.isRiding() || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid() || Speed.mc.player.isInWeb) {
            return;
        }
        if (mode.equalsIgnoreCase("Matrix Old")) {
            if (this.boost.getCurrentValue()) {
                Speed.mc.player.onGround = false;
            }
            Speed.mc.gameSettings.keyBindJump.pressed = false;
            Speed.mc.timer.timerSpeed = 6.78f;
            Speed.mc.player.jumpMovementFactor *= 1.04f;
            Speed.mc.player.motionY = Speed.mc.player.motionY > 0.0 && !Speed.mc.player.onGround ? (Speed.mc.player.motionY -= 0.00994) : (Speed.mc.player.motionY -= 0.00995);
        } else if (mode.equalsIgnoreCase("Matrix") || mode.equalsIgnoreCase("Really World") || mode.equalsIgnoreCase("Matrix Fast")) {
            Speed.mc.gameSettings.keyBindJump.pressed = false;
            Speed.mc.player.motionY = Speed.mc.player.motionY > 0.0 && !Speed.mc.player.onGround ? (Speed.mc.player.motionY -= 0.00994) : (Speed.mc.player.motionY -= 0.00995);
        } else if (mode.equalsIgnoreCase("NCP YPort")) {
            Speed.mc.gameSettings.keyBindJump.pressed = false;
        } else if (mode.equalsIgnoreCase("Sunrise Disabler")) {
            Speed.mc.gameSettings.keyBindJump.pressed = false;
        } else if (mode.equalsIgnoreCase("Sunrise Timer") || mode.equalsIgnoreCase("Sunrise YPort")) {
            Speed.mc.gameSettings.keyBindJump.pressed = false;
            Speed.mc.player.motionY = Speed.mc.player.motionY > 0.0 && !Speed.mc.player.onGround ? (Speed.mc.player.motionY -= 0.00994) : (Speed.mc.player.motionY -= 0.00995);
        } else if (mode.equalsIgnoreCase("Sunrise Boost")) {
            if (!Speed.mc.player.isPotionActive(MobEffects.SPEED) && this.potionCheck.getCurrentValue()) {
                return;
            }
            MovementHelper.setSpeed(MovementHelper.getSpeed());
            if (Speed.mc.player.onGround && MovementHelper.isMoving()) {
                Speed.mc.player.onGround = false;
                Speed.mc.player.motionX *= (double)(this.motion.getCurrentValue() * 2.0f);
                Speed.mc.player.motionZ *= (double)(this.motion.getCurrentValue() * 2.0f);
                Speed.mc.player.jump();
            }
        } else if (mode.equalsIgnoreCase("Strafe")) {
            Speed.mc.gameSettings.keyBindJump.pressed = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onUpdateTwo(EventUpdate event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Old") || mode.equalsIgnoreCase("Custom") && this.blink.getCurrentValue()) {
            LinkedList<double[]> linkedList = this.positions;
            synchronized (linkedList) {
                this.positions.add(new double[]{Speed.mc.player.posX, Speed.mc.player.getEntityBoundingBox().minY, Speed.mc.player.posZ});
            }
            if (this.pulseTimer.hasReached(600.0)) {
                this.blink();
                this.pulseTimer.reset();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onEnable() {
        if (Speed.mc.player == null) {
            return;
        }
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Old") || mode.equalsIgnoreCase("Custom") && this.blink.getCurrentValue()) {
            LinkedList<double[]> linkedList = this.positions;
            synchronized (linkedList) {
                this.positions.add(new double[]{Speed.mc.player.posX, Speed.mc.player.getEntityBoundingBox().minY + (double)(Speed.mc.player.getEyeHeight() / 2.0f), Speed.mc.player.posZ});
                this.positions.add(new double[]{Speed.mc.player.posX, Speed.mc.player.getEntityBoundingBox().minY, Speed.mc.player.posZ});
            }
        }
        super.onEnable();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix Old") || mode.equalsIgnoreCase("Custom") && this.blink.getCurrentValue()) {
            if (Speed.mc.player == null || !(event.getPacket() instanceof CPacketPlayer) || this.disableLogger) {
                return;
            }
            event.setCancelled(true);
            if (!(event.getPacket() instanceof CPacketPlayer.Position) && !(event.getPacket() instanceof CPacketPlayer.PositionRotation)) {
                return;
            }
            this.packets.add(event.getPacket());
        }
    }

    private double getCurrentSpeed(int stage) {
        double speed = MovementHelper.getBaseSpeed() + 0.028 * (double)MovementHelper.getSpeedEffect() + (double)MovementHelper.getSpeedEffect() / 15.0;
        double initSpeed = 0.4145 + (double)MovementHelper.getSpeedEffect() / 12.5;
        double decrease = (double)stage / 500.0 * 1.87;
        if (stage == 0) {
            speed = 0.64 + ((double)MovementHelper.getSpeedEffect() + 0.028 * (double)MovementHelper.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            speed = initSpeed;
        } else if (stage >= 2) {
            speed = initSpeed - decrease;
        }
        return Math.max(speed, this.slowDownHop ? speed : MovementHelper.getBaseSpeed() + 0.028 * (double)MovementHelper.getSpeedEffect());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void blink() {
        try {
            this.disableLogger = true;
            Iterator<Packet<?>> packetIterator = this.packets.iterator();
            while (packetIterator.hasNext()) {
                Speed.mc.player.connection.sendPacket(packetIterator.next());
                packetIterator.remove();
            }
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.clear();
        }
    }

    @Override
    public void onDisable() {
        this.moveTicks = 0;
        Speed.mc.timer.timerSpeed = 1.0f;
        Speed.mc.player.speedInAir = 0.02f;
        String mode = speedMode.getOptions();
        if (mode.equalsIgnoreCase("YPort")) {
            Speed.mc.player.motionX = 0.0;
            Speed.mc.player.motionZ = 0.0;
        }
        if (mode.equalsIgnoreCase("Matrix Old") || mode.equalsIgnoreCase("Custom") && this.blink.getCurrentValue()) {
            this.blink();
        }
        super.onDisable();
    }

    public static void init() {
    }
}

