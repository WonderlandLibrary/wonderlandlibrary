/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.login;

public final class MinecraftAccount {
    private final String username;
    private String password;
    private String inGameName;

    public MinecraftAccount(String username) {
        this.username = username;
    }

    public MinecraftAccount(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public MinecraftAccount(String name, String password, String inGameName) {
        this.username = name;
        this.password = password;
        this.inGameName = inGameName;
    }

    public boolean isCracked() {
        return this.password == null || this.password.isEmpty();
    }

    public String getName() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getAccountName() {
        return this.inGameName;
    }

    public void setAccountName(String accountName) {
        this.inGameName = accountName;
    }
}

