package wtf.monsoon.impl.modules.combat;



import org.lwjgl.input.Keyboard;
import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.misc.Timer;


public class AutoClicker extends Module {
    public Timer timer = new Timer();
    NumberSetting cps = new NumberSetting("CPS",  10, 1, 50, 1,this);

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks for you", Keyboard.KEY_NONE, Category.COMBAT);
        addSettings(cps);
    }


    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.gameSettings.keyBindAttack.isKeyDown() && timer.hasTimeElapsed((long) (1000 / cps.getValue()), true)) {
            mc.leftClickCounter = 0;
            mc.clickMouse();
        }
    }
}