package io.github.raze.modules.system.information;

public enum ModuleCategory {

    COMBAT ("Combat"),MOVEMENT ("Movement"), PLAYER ("Player"),
    VISUAL ("Visual"), MISC("Misc"), CLIENT ("Client");

    public final String name;

    ModuleCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
