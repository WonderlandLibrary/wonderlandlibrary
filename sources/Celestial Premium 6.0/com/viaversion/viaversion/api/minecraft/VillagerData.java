/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.minecraft;

public class VillagerData {
    private final int type;
    private final int profession;
    private final int level;

    public VillagerData(int type, int profession, int level) {
        this.type = type;
        this.profession = profession;
        this.level = level;
    }

    public int getType() {
        return this.type;
    }

    public int getProfession() {
        return this.profession;
    }

    public int getLevel() {
        return this.level;
    }
}

