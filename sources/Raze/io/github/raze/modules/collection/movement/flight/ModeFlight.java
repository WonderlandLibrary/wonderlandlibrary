package io.github.raze.modules.collection.movement.flight;

import io.github.raze.modules.collection.movement.Flight;
import io.github.raze.modules.collection.movement.flight.impl.*;
import io.github.raze.utilities.system.Methods;

import java.util.HashMap;

public class ModeFlight implements Methods {

    private static final HashMap<Class<? extends ModeFlight>, ModeFlight> modes = new HashMap<>();

    public String name;
    protected Flight parent;

    public ModeFlight(String name) {
        this.name = name;
    }

    public static void init() {
        modes.put(VanillaFlight.class, new VanillaFlight());
        modes.put(VulcanAirJumpFlight.class, new VulcanAirJumpFlight());
        modes.put(VulcanGlideFlight.class, new VulcanGlideFlight());
        modes.put(VerusFlight.class, new VerusFlight());
        modes.put(PacketFlight.class, new PacketFlight());
        modes.put(BlinkFlight.class, new BlinkFlight());
        modes.put(VerusFastFlight.class, new VerusFastFlight());
    }

    public static <T extends ModeFlight> T getMode(Class<? extends ModeFlight> query) {
        return (T) modes.values().stream().filter(output -> output.getClass().equals(query)).findFirst().orElse(null);
    }

    public static ModeFlight getMode(String query) {
        return modes.values().stream().filter(output -> output.getName().equals(query)).findFirst().orElse(null);
    }

    public String getName() { return name; }

    public Flight getParent() { return parent; }

    public void setParent(Flight parent) { this.parent = parent; }

    public void onEnable() { /* */ }
    public void onDisable() { /* */ }
}