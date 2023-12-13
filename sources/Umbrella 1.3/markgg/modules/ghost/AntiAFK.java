/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.ghost;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.BooleanSetting;
import markgg.settings.NumberSetting;
import markgg.utilities.TimerUtil;

public class AntiAFK
extends Module {
    public BooleanSetting hit = new BooleanSetting("Punch", false);
    public NumberSetting delay = new NumberSetting("Delay", 5.0, 2.5, 10.0, 0.5);
    public TimerUtil timer = new TimerUtil();

    public AntiAFK() {
        super("AntiAFK", 0, Module.Category.GHOST);
        this.addSettings(this.hit, this.delay);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            this.mc.gameSettings.keyBindForward.pressed = true;
            if (this.timer.hasTimeElapsed((long)this.delay.getValue() * 100L, true)) {
                if (this.hit.isEnabled()) {
                    this.mc.clickMouse();
                }
                this.mc.thePlayer.rotationYaw = this.mc.thePlayer.prevRotationYaw -= 90.0f;
                this.mc.gameSettings.keyBindForward.pressed = false;
            }
        }
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.keyBindForward.pressed = false;
    }
}

