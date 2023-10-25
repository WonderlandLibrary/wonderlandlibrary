package tech.drainwalk.client.module.category;

import lombok.Getter;

public enum Type {
    MAIN("Main"), SECONDARY("Secondary");
    @Getter
    private final String name;

    Type(String name) {
        this.name = name;
    }
}
