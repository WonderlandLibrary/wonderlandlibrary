package dev.monsoon.module.implementation.combat;


import dev.monsoon.event.Event;
import dev.monsoon.module.enums.Category;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import org.lwjgl.input.Keyboard;


public class AutoClicker extends Module {
    public Timer timer = new Timer();
    NumberSetting cps = new NumberSetting("CPS",  10, 1, 10, 1,this);

    public AutoClicker() {
        super("AutoClicker", Keyboard.KEY_NONE, Category.COMBAT);
        addSettings(cps);
    }

    public void onEvent(Event e) {
        if (e instanceof EventUpdate && e.isPre()) {
            if (mc.gameSettings.keyBindAttack.isKeyDown() && timer.hasTimeElapsed((long) (1000 / cps.getValue()), true)) {
                mc.leftClickCounter = 0;
                mc.clickMouse();
            }
        }
    }
}