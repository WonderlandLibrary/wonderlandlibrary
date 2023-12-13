/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.friendsystem;

public class Friend {
    private String aliasName;
    private String name;
    private final boolean hasCustomName;

    public Friend(String paramString1, String paramString2, boolean paramBoolean) {
        this.aliasName = paramString1;
        this.name = paramString2;
        this.hasCustomName = paramBoolean;
    }

    public String getAliasName() {
        return this.aliasName;
    }

    public void setAliasName(String paramString) {
        this.aliasName = paramString;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public boolean isHasCustomName() {
        return this.hasCustomName;
    }
}

