// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.stats;

import net.minecraft.scoreboard.ScoreDummyCriteria;

public class ObjectiveStat extends ScoreDummyCriteria
{
    private final StatBase stat;
    
    public ObjectiveStat(final StatBase statIn) {
        super(statIn.statId);
        this.stat = statIn;
    }
}
