package io.github.raze.modules.collection.movement.step;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.Step;
import io.github.raze.modules.collection.movement.step.impl.*;
import io.github.raze.utilities.system.Methods;

import java.util.HashMap;

public class ModeStep implements Methods {

    public String name;
    protected Step parent;

    public ModeStep(String name) {
        this.name = name;
        this.parent = Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(Step.class);
    }

    public static void init() {
        modes.put(JumpStep.class, new JumpStep());
        modes.put(VanillaStep.class, new VanillaStep());
        modes.put(MotionStep.class, new MotionStep());
        modes.put(TimerStep.class, new TimerStep());
        modes.put(NCPStep.class, new NCPStep());
        modes.put(PacketStep.class, new PacketStep());
    }

    private static final HashMap<Class<? extends ModeStep>, ModeStep> modes = new HashMap<>();

    public static <T extends ModeStep> T getMode(Class<? extends ModeStep> query) {
        return (T) modes.values().stream().filter(output -> output.getClass().equals(query)).findFirst().orElse(null);
    }

    public static ModeStep getMode(String query) {
        return modes.values().stream().filter(output -> output.getName().equals(query)).findFirst().orElse(null);
    }

    public String getName() { return name; }

    public Step getParent() { return parent; }

    public void setParent(Step parent) { this.parent = parent; }

    public void onEnable() { /* */ }
    public void onDisable() { /* */ }
}
