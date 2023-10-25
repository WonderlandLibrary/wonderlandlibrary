/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.stats;

import net.minecraft.scoreboard.ScoreDummyCriteria;
import net.minecraft.stats.StatBase;

public class ObjectiveStat
extends ScoreDummyCriteria {
    private final StatBase field_151459_g;

    public ObjectiveStat(StatBase statBase) {
        super(statBase.statId);
        this.field_151459_g = statBase;
    }
}

