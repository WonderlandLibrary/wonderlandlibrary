package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.util.impl.TimerUtil;

public class AutoClicker extends Module {

    public ModeSetting mode;
    public IntSetting delay;
    public TimerUtil timer;

    public AutoClicker() {
        super("Auto Clicker", Category.Misc, "Automatically clicks your mouse");
        mode = this.setting("Mode", "Left", new String[]{"Left", "Right"});
        delay = this.setting("Interval", 100, 0, 10000);
        timer = new TimerUtil();
    }

    @EventListener
    public void event(TickEvent event) {
        if (getMc().currentScreen != null || Prestige.Companion.getClickManager().click() || !timer.delay(this.delay.getObject())) {
            return;
        }
        Prestige.Companion.getClickManager().setClick(mode.getObject().equals("Left") ? 0 : 1, 0);
    }
}
