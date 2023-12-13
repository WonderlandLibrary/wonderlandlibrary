package vestige.module.impl.player;

import vestige.event.Listener;
import vestige.event.impl.PostMotionEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.DoubleSetting;

public class Timer extends Module {

    private final DoubleSetting speed = new DoubleSetting("Speed", 1.1, 0.1, 5, 0.1);

    public Timer() {
        super("Timer", Category.PLAYER);
        this.addSettings(speed);
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        mc.timer.timerSpeed = (float) speed.getValue();
    }

}
