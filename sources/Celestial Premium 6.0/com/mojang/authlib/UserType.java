/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.authlib;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
    LEGACY("legacy"),
    MOJANG("mojang");

    private static final Map<String, UserType> BY_NAME;
    private final String name;

    private UserType(String name) {
        this.name = name;
    }

    public static UserType byName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

    public String getName() {
        return this.name;
    }

    static {
        BY_NAME = new HashMap<String, UserType>();
        for (UserType type : UserType.values()) {
            BY_NAME.put(type.name, type);
        }
    }
}

