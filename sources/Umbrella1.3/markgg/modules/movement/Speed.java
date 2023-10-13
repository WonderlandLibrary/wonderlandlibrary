/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.movement;

import markgg.Client;
import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.utilities.TimerUtil;
import markgg.utilities.movement.MovementUtil;
import net.minecraft.init.Blocks;

public class Speed
extends Module {
    TimerUtil timer = new TimerUtil();
    boolean isWalking = false;
    public NumberSetting speed = new NumberSetting("Speed", 1.0, 0.1, 3.0, 0.1);
    public ModeSetting speedMode = new ModeSetting("Mode", "Legit BHop", "Legit BHop", "Lowhop", "Verus", "Mineplex", "OldHypixel", "Ice Speed");

    public Speed() {
        super("Speed", 0, Module.Category.MOVEMENT);
        this.addSettings(this.speed, this.speedMode);
    }

    @Override
    public void onEnable() {
        if (this.speedMode.getMode() == "Ice Speed") {
            Client.addChatMessage("This speed mode can only be used on ice!");
            Blocks.ice.slipperiness = 0.4f;
            Blocks.packed_ice.slipperiness = 0.4f;
        }
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        Blocks.ice.slipperiness = 0.89f;
        Blocks.packed_ice.slipperiness = 0.89f;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            this.isWalking = this.mc.thePlayer.motionX > 0.14 || this.mc.thePlayer.motionX < -0.14 || this.mc.thePlayer.motionZ > 0.14 || this.mc.thePlayer.motionZ < -0.14;
            if (this.speedMode.getMode() == "Mineplex" && (this.mc.thePlayer.motionX != 0.0 || this.mc.thePlayer.motionZ != 0.0)) {
                this.mc.thePlayer.setSprinting(true);
                if (this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.motionY = MovementUtil.jumpHeight() + 0.1;
                } else {
                    if (this.mc.thePlayer.fallDistance == 0.0f) {
                        this.mc.thePlayer.motionY += 0.013;
                    }
                    MovementUtil.setSpeed(0.45f);
                }
            }
            if (this.speedMode.is("Lowhop")) {
                if (!this.mc.thePlayer.isUsingItem()) {
                    this.mc.timer.timerSpeed = 1.3f;
                }
                if (this.mc.thePlayer.onGround && MovementUtil.isMoving() && !this.mc.thePlayer.isCollidedHorizontally) {
                    this.mc.thePlayer.motionY = 0.12f;
                }
                if ((this.mc.thePlayer.isCollidedHorizontally || this.mc.gameSettings.keyBindJump.isPressed()) && this.mc.thePlayer.onGround && MovementUtil.isMoving()) {
                    this.mc.thePlayer.jump();
                }
                MovementUtil.setSpeed((float)this.speed.getValue());
            }
            if (this.speedMode.getMode() == "OldHypixel" && (this.mc.thePlayer.motionX != 0.0 || this.mc.thePlayer.motionZ != 0.0)) {
                this.mc.thePlayer.setSprinting(true);
                if (this.mc.timer.timerSpeed != 1.5f) {
                    this.mc.timer.timerSpeed = 1.5f;
                }
                if (this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.motionY = MovementUtil.jumpHeight();
                } else {
                    MovementUtil.strafe(0.13f);
                }
            }
            if (this.speedMode.is("Legit BHop")) {
                this.mc.timer.timerSpeed = 1.0f;
                if (this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.jump();
                }
                if (this.mc.thePlayer.isDead || this.mc.thePlayer.getHealth() <= 0.0f) {
                    this.toggle();
                    this.mc.timer.timerSpeed = 1.0f;
                }
            }
            if (this.speedMode.is("Verus")) {
                this.mc.timer.timerSpeed = 1.0f;
                if (this.isWalking && this.mc.thePlayer.onGround && this.timer.hasTimeElapsed(100L, true)) {
                    this.mc.thePlayer.jump();
                }
                if (this.mc.thePlayer.isSprinting()) {
                    if (this.mc.thePlayer.onGround) {
                        if (this.mc.thePlayer.moveForward > 0.0f) {
                            MovementUtil.setSpeed(0.19f);
                        } else {
                            MovementUtil.setSpeed(0.14f);
                        }
                    } else if (this.mc.thePlayer.moveForward > 0.0f) {
                        MovementUtil.setSpeed(0.295f);
                    } else {
                        MovementUtil.setSpeed(0.29f);
                    }
                } else if (this.mc.thePlayer.onGround) {
                    if (this.mc.thePlayer.moveForward > 0.0f) {
                        MovementUtil.setSpeed(0.16f);
                    } else {
                        MovementUtil.setSpeed(0.14f);
                    }
                } else if (this.mc.thePlayer.moveForward > 0.0f) {
                    MovementUtil.setSpeed(0.25);
                } else {
                    MovementUtil.setSpeed(0.2f);
                }
            }
        }
    }
}

