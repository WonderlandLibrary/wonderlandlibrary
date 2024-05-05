package club.async.module;

public enum Category {

    COMBAT("Combat"), MOVEMENT("Movement"), PLAYER("Player"), VISUAL("Visual"), HUD("Hud");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

}
