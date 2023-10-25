/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

public class RealmsWorldResetDto {
    private final String seed;
    private final long worldTemplateId;
    private final int levelType;
    private final boolean generateStructures;

    public RealmsWorldResetDto(String seed, long worldTemplateId, int levelType, boolean generateStructures) {
        this.seed = seed;
        this.worldTemplateId = worldTemplateId;
        this.levelType = levelType;
        this.generateStructures = generateStructures;
    }
}

