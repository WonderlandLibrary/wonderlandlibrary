package intent.AquaDev.aqua.modules.combat;

import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.TimeUtil;

public class TickBase
extends Module {
    public static boolean sneaked;
    TimeUtil timeUtil = new TimeUtil();

    public TickBase() {
        super("TickBase", "TickBase", 0, Category.Combat);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            sneaked = TickBase.mc.gameSettings.keyBindSprint.pressed;
            if (sneaked) {
                TickBase.mc.timer.timerSpeed = 0.1f;
                this.timeUtil.reset();
            } else {
                TickBase.mc.timer.timerSpeed = 1.5f;
            }
        }
    }
}
