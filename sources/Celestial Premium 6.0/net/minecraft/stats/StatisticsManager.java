/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.stats;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatBase;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatisticsManager {
    protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newConcurrentMap();

    public void increaseStat(EntityPlayer player, StatBase stat, int amount) {
        this.unlockAchievement(player, stat, this.readStat(stat) + amount);
    }

    public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(statIn);
        if (tupleintjsonserializable == null) {
            tupleintjsonserializable = new TupleIntJsonSerializable();
            this.statsData.put(statIn, tupleintjsonserializable);
        }
        tupleintjsonserializable.setIntegerValue(p_150873_3_);
    }

    public int readStat(StatBase stat) {
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(stat);
        return tupleintjsonserializable == null ? 0 : tupleintjsonserializable.getIntegerValue();
    }
}

