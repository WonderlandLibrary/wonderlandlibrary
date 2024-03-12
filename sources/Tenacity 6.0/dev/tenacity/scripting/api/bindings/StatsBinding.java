// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.module.impl.render.Statistics;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class StatsBinding
{
    public int getKills() {
        return Statistics.killCount;
    }
    
    public int getDeaths() {
        return Statistics.deathCount;
    }
    
    public double getKD() {
        return (this.getDeaths() == 0) ? this.getKills() : MathUtils.round(this.getKills() / (double)this.getDeaths(), 2);
    }
    
    public int getGamesPlayed() {
        return Statistics.gamesPlayed;
    }
    
    public int[] getPlayTime() {
        return Statistics.getPlayTime();
    }
}
