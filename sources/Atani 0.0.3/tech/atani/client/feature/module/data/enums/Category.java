package tech.atani.client.feature.module.data.enums;

public enum Category {
    SERVER("Server"),
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    MISCELLANEOUS("Others"),
    RENDER("Render"),
    HUD("Design"),
    CHAT("Chat"),
    OPTIONS("Options");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
