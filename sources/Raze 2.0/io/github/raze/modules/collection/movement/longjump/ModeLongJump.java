package io.github.raze.modules.collection.movement.longjump;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.LongJump;
import io.github.raze.modules.collection.movement.longjump.impl.*;
import io.github.raze.utilities.system.Methods;

import java.util.HashMap;

public class ModeLongJump implements Methods {

    public String name;
    protected LongJump parent;

    public ModeLongJump(String name) {
        this.name = name;
        this.parent = Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(LongJump.class);
    }

    public static void init() {
        modes.put(VulcanLongJump.class, new VulcanLongJump());
        modes.put(VerusLongJump.class, new VerusLongJump());
        modes.put(VanillaLongJump.class, new VanillaLongJump());
    }

    private static final HashMap<Class<? extends ModeLongJump>, ModeLongJump> modes = new HashMap<>();

    public static <T extends ModeLongJump> T getMode(Class<? extends ModeLongJump> query) {
        return (T) modes.values().stream().filter(output -> output.getClass().equals(query)).findFirst().orElse(null);
    }

    public static ModeLongJump getMode(String query) {
        return modes.values().stream().filter(output -> output.getName().equals(query)).findFirst().orElse(null);
    }

    public String getName() { return name; }

    public LongJump getParent() { return parent; }

    public void setParent(LongJump parent) { this.parent = parent; }

    public void onEnable() { /* */ }
    public void onDisable() { /* */ }
}
