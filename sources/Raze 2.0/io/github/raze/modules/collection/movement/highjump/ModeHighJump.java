package io.github.raze.modules.collection.movement.highjump;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.HighJump;
import io.github.raze.modules.collection.movement.highjump.impl.*;
import io.github.raze.utilities.system.Methods;

import java.util.HashMap;

public class ModeHighJump implements Methods {

    public String name;
    protected HighJump parent;

    public ModeHighJump(String name) {
        this.name = name;
        this.parent = Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(HighJump.class);
    }

    public static void init() {
        modes.put(VulcanHighJump.class, new VulcanHighJump());
        modes.put(VulcanDamageHighJump.class, new VulcanDamageHighJump());
    }

    private static final HashMap<Class<? extends ModeHighJump>, ModeHighJump> modes = new HashMap<>();

    public static <T extends ModeHighJump> T getMode(Class<? extends ModeHighJump> query) {
        return (T) modes.values().stream().filter(output -> output.getClass().equals(query)).findFirst().orElse(null);
    }

    public static ModeHighJump getMode(String query) {
        return modes.values().stream().filter(output -> output.getName().equals(query)).findFirst().orElse(null);
    }

    public String getName() { return name; }

    public HighJump getParent() { return parent; }

    public void setParent(HighJump parent) { this.parent = parent; }

    public void onEnable() { /* */ }
    public void onDisable() { /* */ }
}
