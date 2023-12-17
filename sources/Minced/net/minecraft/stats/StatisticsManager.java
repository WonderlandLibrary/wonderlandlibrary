// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Maps;
import net.minecraft.util.TupleIntJsonSerializable;
import java.util.Map;

public class StatisticsManager
{
    protected final Map<StatBase, TupleIntJsonSerializable> statsData;
    
    public StatisticsManager() {
        this.statsData = (Map<StatBase, TupleIntJsonSerializable>)Maps.newConcurrentMap();
    }
    
    public void increaseStat(final EntityPlayer player, final StatBase stat, final int amount) {
        this.unlockAchievement(player, stat, this.readStat(stat) + amount);
    }
    
    public void unlockAchievement(final EntityPlayer playerIn, final StatBase statIn, final int p_150873_3_) {
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(statIn);
        if (tupleintjsonserializable == null) {
            tupleintjsonserializable = new TupleIntJsonSerializable();
            this.statsData.put(statIn, tupleintjsonserializable);
        }
        tupleintjsonserializable.setIntegerValue(p_150873_3_);
    }
    
    public int readStat(final StatBase stat) {
        final TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(stat);
        return (tupleintjsonserializable == null) ? 0 : tupleintjsonserializable.getIntegerValue();
    }
}
