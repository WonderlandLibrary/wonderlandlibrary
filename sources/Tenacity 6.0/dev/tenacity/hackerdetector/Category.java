package dev.tenacity.hackerdetector;

public enum Category {

    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    MISC("Misc"),
    EXPLOIT("Exploit");

    private String name;

    Category(String name) {
        this.name = name;
    }

}
