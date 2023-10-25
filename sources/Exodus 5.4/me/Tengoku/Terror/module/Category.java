/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    MISC("Misc"),
    SKYBLOCK("Skyblock"),
    WORLD("World");

    public String name;
    public int moduleIndex;

    private Category(String string2) {
        this.name = string2;
    }
}

