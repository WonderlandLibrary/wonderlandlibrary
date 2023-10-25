package tech.drainwalk.client.module.modules.movement;

import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;

public class FlyModule extends Module {
    public FlyModule(){
        super("FlyModule", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        mc.player.capabilities.isFlying = true;
        mc.player.capabilities.allowFlying = true;
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.allowFlying = false;
    }
}
