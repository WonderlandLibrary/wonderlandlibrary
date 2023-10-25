/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.ui;

public final class Alt {
    private String mask = "";
    private String password;
    private final String username;

    public String getMask() {
        return this.mask;
    }

    public void setMask(String string) {
        this.mask = string;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String string) {
        this.password = string;
    }

    public Alt(String string, String string2) {
        this(string, string2, "");
    }

    public Alt(String string, String string2, String string3) {
        this.username = string;
        this.password = string2;
        this.mask = string3;
    }
}

