package koks.api.manager.cl;

import lombok.Getter;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Getter
public enum Role {
    ADMIN(10, "Dealer", "§c"), DEVELOPER(2, "Dev", "§b"), FRIEND(3, "Friend", "§b"), SUPPORTER(1, "Supporter", "§d"), USER(0, "User", "§a");

    private final int id;
    private final String name, color;

    Role(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
