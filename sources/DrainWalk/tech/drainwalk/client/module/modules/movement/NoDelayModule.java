package tech.drainwalk.client.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.events.UpdateEvent;

public class NoDelayModule extends Module {
    private final BooleanOption noJumpDelay = new BooleanOption("Jump", true);
    private final BooleanOption noClickDelay = new BooleanOption("Click", true);

    public NoDelayModule() {
        super("NoDelay", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(UpdateEvent eventUpdate) {
        mc.player.jumpTicks = 0;
    }

    @Override
    public void onDisable() {
        mc.player.jumpTicks = 10;
    }
}
