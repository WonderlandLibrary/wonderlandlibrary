package net.minecraft.stats;

import net.minecraft.scoreboard.ScoreDummyCriteria;
import net.minecraft.stats.StatBase;

public class ObjectiveStat
extends ScoreDummyCriteria {
    private final StatBase stat;

    public ObjectiveStat(StatBase statIn) {
        super(statIn.statId);
        this.stat = statIn;
    }
}
