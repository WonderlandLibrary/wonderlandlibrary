package io.github.raze.modules.collection.movement.speed;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.Speed;
import io.github.raze.modules.collection.movement.speed.impl.*;
import io.github.raze.utilities.system.Methods;

import java.util.HashMap;

public class ModeSpeed implements Methods {

    public String name;
    protected Speed parent;

    public ModeSpeed(String name) {
        this.name = name;
        this.parent = Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(Speed.class);
    }

    private static final HashMap<Class<? extends ModeSpeed>, ModeSpeed> modes = new HashMap<>();

    public static void init() {
        modes.put(LegitSpeed.class, new LegitSpeed());
        modes.put(CustomSpeed.class, new CustomSpeed());
        modes.put(StrafeSpeed.class, new StrafeSpeed());
        modes.put(VulcanSpeed.class, new VulcanSpeed());
        modes.put(MatrixSpeed.class, new MatrixSpeed());
        modes.put(SpoofSpeed.class, new SpoofSpeed());
        modes.put(NCPSpeed.class, new NCPSpeed());
        modes.put(OldNCPSpeed.class, new OldNCPSpeed());
        modes.put(MushMCSpeed.class, new MushMCSpeed());
        modes.put(VerusSpeed.class, new VerusSpeed());
        modes.put(SpartanSpeed.class, new SpartanSpeed());
        modes.put(IncognitoSpeed.class, new IncognitoSpeed());
        modes.put(KarhuSpeed.class, new KarhuSpeed());
        modes.put(MineMenClubSpeed.class, new MineMenClubSpeed());
    }

    public static <T extends ModeSpeed> T getMode(Class<? extends ModeSpeed> query) {
        return (T) modes.values().stream().filter(output -> output.getClass().equals(query)).findFirst().orElse(null);
    }

    public static ModeSpeed getMode(String query) {
        return modes.values().stream().filter(output -> output.getName().equals(query)).findFirst().orElse(null);
    }

    public String getName() { return name; }

    public Speed getParent() { return parent; }

    public void setParent(Speed parent) { this.parent = parent; }

    public void onEnable() { /* */ }
    public void onDisable() { /* */ }
}