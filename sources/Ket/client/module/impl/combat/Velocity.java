package client.module.impl.combat;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.combat.velocity.VanillaVelocity;
import client.module.impl.combat.velocity.WatchdogVelocity;
import client.value.impl.ModeValue;

@ModuleInfo(name = "Velocity", description = "Uses heavy dick and balls to drag across the floor to reduce velocity.", category = Category.COMBAT)
public class Velocity extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaVelocity("Vanilla", this))
            .add(new WatchdogVelocity("Watchdog", this))
            .setDefault("Cancel");
}
