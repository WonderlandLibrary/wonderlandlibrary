/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.stream;

import net.minecraft.client.stream.Metadata;
import net.minecraft.stats.Achievement;

public class MetadataAchievement
extends Metadata {
    public MetadataAchievement(Achievement achievement) {
        super("achievement");
        this.func_152808_a("achievement_id", achievement.statId);
        this.func_152808_a("achievement_name", achievement.getStatName().getUnformattedText());
        this.func_152808_a("achievement_description", achievement.getDescription());
        this.func_152807_a("Achievement '" + achievement.getStatName().getUnformattedText() + "' obtained!");
    }
}

