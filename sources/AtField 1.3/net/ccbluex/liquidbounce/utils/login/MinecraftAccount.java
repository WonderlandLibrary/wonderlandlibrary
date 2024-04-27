/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.login;

public final class MinecraftAccount {
    private String password;
    private String inGameName;
    private final String username;

    public boolean isCracked() {
        return this.password == null || this.password.isEmpty();
    }

    public MinecraftAccount(String string, String string2, String string3) {
        this.username = string;
        this.password = string2;
        this.inGameName = string3;
    }

    public MinecraftAccount(String string) {
        this.username = string;
    }

    public void setAccountName(String string) {
        this.inGameName = string;
    }

    public String getName() {
        return this.username;
    }

    public MinecraftAccount(String string, String string2) {
        this.username = string;
        this.password = string2;
    }

    public String getAccountName() {
        return this.inGameName;
    }

    public String getPassword() {
        return this.password;
    }
}

