package club.async.module.impl.movement;

import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import org.lwjgl.input.Keyboard;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "Sprint", description = "Automatically sprint's", category = Category.MOVEMENT)
public class Sprint extends Module {

    @Handler
    public void update(EventUpdate eventUpdate) {
        mc.gameSettings.keyBindSprint.pressed = true;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.setSprinting(false);
        mc.gameSettings.keyBindSprint.pressed = false;
    }

}
