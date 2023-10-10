package vestige.module.impl.combat;

import org.lwjgl.input.Mouse;
import vestige.event.Listener;
import vestige.event.impl.RenderEvent;
import vestige.event.impl.TickEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.IntegerSetting;
import vestige.util.misc.TimerUtil;

import java.util.concurrent.ThreadLocalRandom;

public class Autoclicker extends Module {

    private boolean wasHoldingMouse;
    private boolean clickingTick;

    private final TimerUtil timer = new TimerUtil();

    private final IntegerSetting minCPS = new IntegerSetting("Min CPS", 8, 1, 20, 1);
    private final IntegerSetting maxCPS = new IntegerSetting("Max CPS", 15, 1, 20, 1);

    public Autoclicker() {
        super("Autoclicker", Category.COMBAT);
        this.addSettings(minCPS, maxCPS);
    }

    @Override
    public void onEnable() {
        wasHoldingMouse = false;
    }

    @Listener
    public void onRender(RenderEvent event) {
        if(wasHoldingMouse) {
            long maxDelay = (long) (1000.0 / minCPS.getValue());
            long minDelay = (long) (1000.0 / maxCPS.getValue());

            long delay = maxDelay > minDelay ? ThreadLocalRandom.current().nextLong(minDelay, maxDelay) : minDelay;

            if(timer.getTimeElapsed() >= delay) {
                clickingTick = true;
                timer.reset();
            }
        }
    }

    @Listener
    public void onTick(TickEvent event) {
        if(Mouse.isButtonDown(0)) {
            if(wasHoldingMouse && clickingTick) {
                mc.leftClickCounter = 0;
                mc.clickMouse();

                clickingTick = false;
            }

            wasHoldingMouse = true;
        } else {
            wasHoldingMouse = false;
        }
    }

}
