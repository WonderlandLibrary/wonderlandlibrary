package vestige.api.module;

import lombok.Getter;

@Getter
public enum Category {

    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    WORLD("World"),
    RENDER("Render"),
    EXPLOIT("Exploit"),
    GHOST("Ghost"),
    MISC("Misc");

    private String name;

    Category(String name) {
        this.name = name;
    }

}
