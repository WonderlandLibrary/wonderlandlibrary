package dev.tenacity.module.impl.player;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.TimerUtil;
import org.lwjgl.input.Keyboard;

public final class AntiVoid extends Module {

    private final TimerUtil timerUtil = new TimerUtil();
    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Negativity");

    public AntiVoid() {
        super("AntiVoid", "Attempts to save you from the void", ModuleCategory.PLAYER);
        initializeSettings(mode);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if (mc.thePlayer.fallDistance > 3) {
            switch (mode.getCurrentMode()) {
                case "Negativity": {
                    mc.thePlayer.motionY = 0;
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        mc.thePlayer.motionY = -100;
                    }
                    break;
                }
                case "Vanilla": {
                    event.cancel();
                    }
                }
            }
        };

    public AntiVoid(String name, String description, ModuleCategory category) {
        super(name, description, category);
    }
}
