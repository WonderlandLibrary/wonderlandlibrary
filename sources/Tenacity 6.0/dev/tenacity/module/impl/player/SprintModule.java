package dev.tenacity.module.impl.player;

import dev.tenacity.Tenacity;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;

public final class SprintModule extends Module {

    public SprintModule() {
        super("Sprint", "Automatically makes your players sprint state to true", ModuleCategory.PLAYER);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if(Tenacity.getInstance().getModuleRepository().getModule(ScaffoldModule.class).isEnabled()) return;

        mc.gameSettings.keyBindSprint.pressed = true;
    };

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindSprint.pressed = false;
        mc.thePlayer.setSprinting(false);
        super.onDisable();
    }
}
