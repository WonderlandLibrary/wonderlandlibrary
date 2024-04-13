package dev.tenacity.module.impl.player;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;

public class
FastPlaceModule extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Standard", "Fast");

    public FastPlaceModule() {
        super("FastPlace", "Makes you place fast", ModuleCategory.PLAYER);
        this.initializeSettings(mode);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if(event.isPost()) return;

        switch (mode.getCurrentMode()) {
            case "Standard": {
                mc.rightClickDelayTimer = mc.thePlayer.ticksExisted % 3;
                break;
            }

            case "Fast": {
                mc.rightClickDelayTimer = 0;
                break;
            }
        }
    };

}
