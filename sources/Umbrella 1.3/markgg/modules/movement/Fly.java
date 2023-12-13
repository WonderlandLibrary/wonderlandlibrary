/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.movement;

import markgg.Client;
import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.utilities.TimerUtil;
import markgg.utilities.movement.MovementUtil;
import net.minecraft.client.settings.GameSettings;

public class Fly
extends Module {
    TimerUtil timer = new TimerUtil();
    TimerUtil blinkTimer = new TimerUtil();
    private float hSpeed;
    private float ySpeed;
    public NumberSetting speed = new NumberSetting("Speed", 0.3, 0.1, 5.0, 0.1);
    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Verus", "Brlns", "Jetpack");

    public Fly() {
        super("Fly", 0, Module.Category.MOVEMENT);
        this.addSettings(this.speed, this.mode);
    }

    @Override
    public void onEnable() {
        if (this.mode.getMode() == "Brlns") {
            this.mc.thePlayer.jump();
        }
        if (this.mode.getMode() == "Vanilla") {
            this.mc.thePlayer.capabilities.allowFlying = true;
            this.mc.thePlayer.jump();
            this.mc.thePlayer.capabilities.isFlying = true;
        }
        if (this.mode.getMode() == "Verus") {
            if (this.speed.getValue() != 1.0) {
                Client.addChatMessage("Verus Fly - Recommended speed is 1!");
            }
            this.mc.thePlayer.onGround = true;
            this.mc.thePlayer.motionY = 0.0;
            this.mc.timer.timerSpeed = (float)this.speed.getValue();
        }
    }

    @Override
    public void onDisable() {
        MovementUtil.setSpeed(0.15f);
        this.mc.thePlayer.capabilities.allowFlying = false;
        this.mc.thePlayer.capabilities.isFlying = false;
        this.mc.timer.timerSpeed = 1.0f;
        this.mc.thePlayer.capabilities.setFlySpeed(0.045f);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            EventMotion event = (EventMotion)e;
            if (this.mode.getMode() == "Brlns") {
                event.onGround = true;
                MovementUtil.setSpeed(0.5);
                this.mc.thePlayer.motionY = GameSettings.isKeyDown(this.mc.gameSettings.keyBindJump) ? 0.5 : (GameSettings.isKeyDown(this.mc.gameSettings.keyBindSneak) ? -0.5 : 0.0);
                this.mc.timer.timerSpeed = 1.0f;
            }
            if (e.isPre()) {
                boolean towerDown;
                boolean tower;
                this.mc.thePlayer.capabilities.isFlying = true;
                boolean bl = tower = this.mc.thePlayer.movementInput.jump && !this.mc.thePlayer.movementInput.sneak && this.mode.is("Airwalk");
                if (tower) {
                    this.mc.thePlayer.motionY = 0.72f;
                }
                boolean bl2 = towerDown = this.mc.thePlayer.movementInput.sneak && !this.mc.thePlayer.movementInput.jump && this.mode.is("Airwalk");
                if (towerDown) {
                    this.mc.thePlayer.motionY = -0.72f;
                }
            }
        }
        if (e instanceof EventUpdate) {
            if (this.mode.is("Vanilla")) {
                this.mc.thePlayer.capabilities.isFlying = true;
                this.mc.timer.timerSpeed = 1.0f;
                this.mc.thePlayer.capabilities.setFlySpeed((float)this.speed.getValue());
            }
            if (this.mode.is("Jetpack") && this.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                this.mc.thePlayer.motionY += (double)0.2f;
            }
        }
    }
}

