/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.motion.EventMove;
import org.celestial.client.event.events.impl.player.EventPostMotion;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.Criticals;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.feature.impl.combat.TargetStrafe;
import org.celestial.client.feature.impl.movement.Flight;
import org.celestial.client.feature.impl.movement.Speed;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class NoSlowDown
extends Feature {
    private boolean isNotJump;
    public static NumberSetting percentage;
    public static BooleanSetting soulSand;
    public static BooleanSetting slimeBlock;
    public static BooleanSetting autoJump;
    public static BooleanSetting jumpBoost;
    public static BooleanSetting bowing;
    public static BooleanSetting eating;
    public static BooleanSetting drinking;
    public static BooleanSetting blocking;
    public static ListSetting noSlowMode;
    private final TimerHelper sendTimer = new TimerHelper();
    public static int usingTicks;

    public NoSlowDown() {
        super("NoSlowDown", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0437\u0430\u043c\u0435\u0434\u043b\u0435\u043d\u0438\u0435 \u043f\u0440\u0438 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0438 \u0435\u0434\u044b \u0438 \u0434\u0440\u0443\u0433\u0438\u0445 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432", Type.Movement);
        percentage = new NumberSetting("Percentage", 100.0f, 0.0f, 100.0f, 1.0f, () -> true);
        noSlowMode = new ListSetting("NoSlow Mode", "Default", () -> true, "Default", "AAC5", "Sunrise New", "Sunrise Safe", "Matrix New", "Sunrise Old", "Sunrise Fast", "Old Matrix", "Matrix Jump", "Matrix");
        eating = new BooleanSetting("Eating", true, () -> true);
        bowing = new BooleanSetting("Bowing", true, () -> true);
        drinking = new BooleanSetting("Drinking", true, () -> true);
        blocking = new BooleanSetting("Blocking", true, () -> true);
        autoJump = new BooleanSetting("Auto Jump", false, () -> !NoSlowDown.noSlowMode.currentMode.equals("Sunrise"));
        jumpBoost = new BooleanSetting("Jump Boost", false, () -> !NoSlowDown.noSlowMode.currentMode.equals("Sunrise") && autoJump.getCurrentValue());
        soulSand = new BooleanSetting("Soul Sand", false, () -> true);
        slimeBlock = new BooleanSetting("Slime", true, () -> true);
        this.addSettings(noSlowMode, eating, blocking, bowing, drinking, autoJump, jumpBoost, soulSand, slimeBlock, percentage);
    }

    public static boolean canNoSLow() {
        if (NoSlowDown.mc.player.isEating() && !eating.getCurrentValue()) {
            return false;
        }
        if (NoSlowDown.mc.player.isBowing() && !bowing.getCurrentValue()) {
            return false;
        }
        if (NoSlowDown.mc.player.isDrinking() && !drinking.getCurrentValue()) {
            return false;
        }
        if (NoSlowDown.mc.player.isBlocking() && !blocking.getCurrentValue()) {
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(Speed.class).getState() && Speed.speedMode.currentMode.equals("Sunrise YPort")) {
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(Criticals.class).getState() && Criticals.critMode.currentMode.equals("Sunrise Air") && KillAura.target != null) {
            return false;
        }
        return !NoSlowDown.noSlowMode.currentMode.equals("Sunrise Safe") || usingTicks >= 3;
    }

    @Override
    public void onDisable() {
        NoSlowDown.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventTarget
    public void onMove(EventMove eventMove) {
        if (!NoSlowDown.canNoSLow()) {
            return;
        }
        if (NoSlowDown.noSlowMode.currentMode.equals("Sunrise Fast")) {
            if (Celestial.instance.featureManager.getFeatureByClass(Speed.class).getState() && Speed.speedMode.currentMode.equals("Sunrise Ground")) {
                return;
            }
            if (NoSlowDown.mc.player.isUsingItem() && NoSlowDown.mc.gameSettings.keyBindForward.isKeyDown()) {
                MovementHelper.setSpeed(MovementHelper.getSpeed());
                if (!MovementHelper.isMoving() || NoSlowDown.mc.player.movementInput.jump) {
                    return;
                }
                if (NoSlowDown.mc.player.onGround) {
                    float value = NoSlowDown.mc.player.rotationYaw * ((float)Math.PI / 180);
                    NoSlowDown.mc.player.motionX -= Math.sin(value) * (double)0.2075f;
                    NoSlowDown.mc.player.motionZ += Math.cos(value) * (double)0.2075f;
                    eventMove.setX(NoSlowDown.mc.player.motionX);
                    eventMove.setY(1.0E-9);
                    eventMove.setZ(NoSlowDown.mc.player.motionZ);
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(noSlowMode.getCurrentMode());
        usingTicks = NoSlowDown.mc.player.isUsingItem() ? ++usingTicks : 0;
        boolean bl = this.isNotJump = !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown();
        if (!this.getState() || !NoSlowDown.mc.player.isUsingItem()) {
            return;
        }
        if (!NoSlowDown.canNoSLow()) {
            return;
        }
        if (autoJump.getCurrentValue() && NoSlowDown.mc.player.isUsingItem()) {
            if (jumpBoost.getCurrentValue()) {
                NoSlowDown.mc.player.jumpMovementFactor *= 1.04f;
                NoSlowDown.mc.player.motionY = NoSlowDown.mc.player.motionY > 0.0 && !NoSlowDown.mc.player.onGround ? (NoSlowDown.mc.player.motionY -= 0.00994) : (NoSlowDown.mc.player.motionY -= 0.00995);
            }
            if (NoSlowDown.mc.player.onGround) {
                NoSlowDown.mc.gameSettings.keyBindJump.pressed = false;
                NoSlowDown.mc.player.jump();
                if (jumpBoost.getCurrentValue()) {
                    NoSlowDown.mc.player.motionX *= 1.005;
                    NoSlowDown.mc.player.motionZ *= 1.005;
                }
            }
        }
        if (NoSlowDown.noSlowMode.currentMode.equals("Matrix Jump")) {
            if (NoSlowDown.mc.player.isUsingItem() && MovementHelper.isMoving() && (double)NoSlowDown.mc.player.fallDistance > 0.7) {
                NoSlowDown.mc.player.motionX *= (double)0.97f;
                NoSlowDown.mc.player.motionZ *= (double)0.97f;
            }
        } else if (NoSlowDown.noSlowMode.currentMode.equals("Matrix New")) {
            if (NoSlowDown.mc.player.isUsingItem()) {
                if (NoSlowDown.mc.player.onGround && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (NoSlowDown.mc.player.ticksExisted % 2 == 0) {
                        NoSlowDown.mc.player.motionX *= 0.48;
                        NoSlowDown.mc.player.motionZ *= 0.48;
                    }
                } else if ((double)NoSlowDown.mc.player.fallDistance > 0.7) {
                    NoSlowDown.mc.player.motionX *= (double)0.97f;
                    NoSlowDown.mc.player.motionZ *= (double)0.97f;
                }
            }
        } else if (NoSlowDown.noSlowMode.currentMode.equals("Sunrise New")) {
            if (NoSlowDown.mc.player.isUsingItem()) {
                if (NoSlowDown.mc.player.onGround && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (NoSlowDown.mc.player.ticksExisted % 2 == 0) {
                        NoSlowDown.mc.player.motionX *= 0.47;
                        NoSlowDown.mc.player.motionZ *= 0.47;
                    }
                } else if ((double)NoSlowDown.mc.player.fallDistance > 0.2) {
                    NoSlowDown.mc.player.motionX *= (double)0.93f;
                    NoSlowDown.mc.player.motionZ *= (double)0.93f;
                }
            }
        } else if (NoSlowDown.noSlowMode.currentMode.equals("Sunrise Safe")) {
            if (NoSlowDown.mc.player.isUsingItem()) {
                if (NoSlowDown.mc.player.onGround && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (NoSlowDown.mc.player.ticksExisted % 2 == 0) {
                        NoSlowDown.mc.player.motionX *= 0.47;
                        NoSlowDown.mc.player.motionZ *= 0.47;
                    }
                } else if ((double)NoSlowDown.mc.player.fallDistance > 0.2) {
                    NoSlowDown.mc.player.motionX *= (double)0.91f;
                    NoSlowDown.mc.player.motionZ *= (double)0.91f;
                }
            }
        } else if (NoSlowDown.noSlowMode.currentMode.equals("Matrix")) {
            if (NoSlowDown.mc.player.isUsingItem() && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown()) {
                if (!MovementHelper.isBlockUnder(0.2f)) {
                    NoSlowDown.mc.player.motionY = 0.0;
                    MovementHelper.setSpeed(MovementHelper.getSpeed());
                }
            } else if ((double)NoSlowDown.mc.player.fallDistance > 0.7) {
                NoSlowDown.mc.player.motionX *= (double)0.97f;
                NoSlowDown.mc.player.motionZ *= (double)0.97f;
            }
        }
    }

    @EventTarget
    public void onPlayerState(EventPostMotion event) {
        if (!this.getState() || !NoSlowDown.mc.player.isUsingItem()) {
            return;
        }
        if (!NoSlowDown.canNoSLow()) {
            return;
        }
        if (NoSlowDown.noSlowMode.currentMode.equals("AAC5")) {
            NoSlowDown.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }

    @EventTarget
    public void onSendPacket(EventPreMotion event) {
        if (!this.getState() || !NoSlowDown.mc.player.isUsingItem()) {
            return;
        }
        if (!NoSlowDown.canNoSLow()) {
            return;
        }
        if (NoSlowDown.noSlowMode.currentMode.equals("Matrix")) {
            if (NoSlowDown.mc.player.isUsingItem() && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown() && !MovementHelper.isBlockUnder(0.2f)) {
                event.setPosY(NoSlowDown.mc.player.ticksExisted % 2 == 0 ? event.getPosY() + 5.0E-4 : event.getPosY() + 3.0E-4);
                event.setOnGround(!NoSlowDown.mc.player.onGround);
            }
        } else if (NoSlowDown.noSlowMode.currentMode.equals("Sunrise Old")) {
            if (NoSlowDown.mc.player.isUsingItem()) {
                if (!NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (!Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() || KillAura.target == null || !Criticals.critMode.currentMode.equals("Sunrise")) {
                        NoSlowDown.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        event.setOnGround(false);
                        event.setPosY(NoSlowDown.mc.player.ticksExisted % 2 != 1 ? event.getPosY() + (double)0.14f : event.getPosY() + (double)0.09f);
                    }
                } else if (NoSlowDown.mc.player.ticksExisted % 2 == 0) {
                    NoSlowDown.mc.player.motionX *= 0.97;
                    NoSlowDown.mc.player.motionZ *= 0.97;
                }
            }
        } else if (NoSlowDown.noSlowMode.currentMode.equals("Old Matrix") && NoSlowDown.mc.player.isUsingItem()) {
            if (this.isNotJump) {
                if (Celestial.instance.featureManager.getFeatureByClass(Flight.class).getState() || Celestial.instance.featureManager.getFeatureByClass(Criticals.class).getState() && Criticals.critMode.currentMode.equals("Old Matrix") && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target != null) {
                    return;
                }
                if (this.sendTimer.hasReached(300.0)) {
                    NoSlowDown.mc.player.connection.sendPacket(new CPacketEntityAction(NoSlowDown.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    this.sendTimer.reset();
                }
                event.setOnGround(false);
                event.setPosY(NoSlowDown.mc.player.ticksExisted % 2 != 1 ? event.getPosY() + 0.08 : event.getPosY() + 0.05);
            } else if (!Celestial.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && !Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState() && KillAura.target == null && (double)NoSlowDown.mc.player.fallDistance > 0.7) {
                NoSlowDown.mc.player.motionX *= (double)0.97f;
                NoSlowDown.mc.player.motionZ *= (double)0.97f;
            }
        }
    }
}

