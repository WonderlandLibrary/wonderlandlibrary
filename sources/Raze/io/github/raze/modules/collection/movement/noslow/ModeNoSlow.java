package io.github.raze.modules.collection.movement.noslow;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.NoSlow;
import io.github.raze.modules.collection.movement.noslow.impl.*;
import io.github.raze.utilities.system.Methods;

import java.util.HashMap;

public class ModeNoSlow implements Methods {

    public String name;
    protected NoSlow parent;

    public ModeNoSlow(String name) {
        this.name = name;
        this.parent = Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(NoSlow.class);
    }

    public static void init() {
        modes.put(VanillaNoSlow.class, new VanillaNoSlow());
        modes.put(NCPNoSlow.class, new NCPNoSlow());
        modes.put(OldIntaveNoSlow.class, new OldIntaveNoSlow());
        modes.put(SpoofNoSlow.class, new SpoofNoSlow());
    }

    private static final HashMap<Class<? extends ModeNoSlow>, ModeNoSlow> modes = new HashMap<>();

    public static <T extends ModeNoSlow> T getMode(Class<? extends ModeNoSlow> query) {
        return (T) modes.values().stream().filter(output -> output.getClass().equals(query)).findFirst().orElse(null);
    }

    public static ModeNoSlow getMode(String query) {
        return modes.values().stream().filter(output -> output.getName().equals(query)).findFirst().orElse(null);
    }

    public String getName() { return name; }

    public NoSlow getParent() { return parent; }

    public void setParent(NoSlow parent) { this.parent = parent; }

    public void onEnable() { /* */ }
    public void onDisable() { /* */ }
}
