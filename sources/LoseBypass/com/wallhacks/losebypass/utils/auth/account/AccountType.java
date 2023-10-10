/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth.account;

public enum AccountType {
    MICROSOFT("MIRCOSOFT"),
    TOKEN("TOKEN"),
    CRACKED("CRACKED");

    private final String name;

    private AccountType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

