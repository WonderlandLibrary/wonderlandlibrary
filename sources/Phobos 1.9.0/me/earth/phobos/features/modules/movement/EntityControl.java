package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.Module;

public class EntityControl
        extends Module {
    public static EntityControl INSTANCE;

    public EntityControl() {
        super("EntityControl", "Control entities with the force or some shit", Module.Category.MOVEMENT, false, false, false);
        INSTANCE = this;
    }
}

