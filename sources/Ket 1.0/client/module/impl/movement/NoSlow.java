package client.module.impl.movement;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.movement.noslow.VanillaNoSlow;
import client.value.impl.ModeValue;

@ModuleInfo(name = "No Slow", description = "Allows you to move at full speed whilst using items", category = Category.MOVEMENT)
public class NoSlow extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaNoSlow("Vanilla", this))
            .setDefault("Vanilla");
}
