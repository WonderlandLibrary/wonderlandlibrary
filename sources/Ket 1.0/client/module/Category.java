package client.module;

import lombok.Getter;

@Getter
public enum Category {

    COMBAT("Combat"),
    EXPLOIT("Exploit"),
    MOVEMENT("Movement"),
    OTHER("Other"),
    PLAYER("Player"),
    RENDER("Render");

    private final String name;

    Category(final String name) {
        this.name = name;
    }
}
