/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.stats;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatFileWriter {
    protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newConcurrentMap();

    public <T extends IJsonSerializable> T func_150870_b(StatBase statBase) {
        TupleIntJsonSerializable tupleIntJsonSerializable = this.statsData.get(statBase);
        return tupleIntJsonSerializable != null ? (T)tupleIntJsonSerializable.getJsonSerializableValue() : null;
    }

    public void increaseStat(EntityPlayer entityPlayer, StatBase statBase, int n) {
        if (!statBase.isAchievement() || this.canUnlockAchievement((Achievement)statBase)) {
            this.unlockAchievement(entityPlayer, statBase, this.readStat(statBase) + n);
        }
    }

    public int func_150874_c(Achievement achievement) {
        if (this.hasAchievementUnlocked(achievement)) {
            return 0;
        }
        int n = 0;
        Achievement achievement2 = achievement.parentAchievement;
        while (achievement2 != null && !this.hasAchievementUnlocked(achievement2)) {
            achievement2 = achievement2.parentAchievement;
            ++n;
        }
        return n;
    }

    public boolean canUnlockAchievement(Achievement achievement) {
        return achievement.parentAchievement == null || this.hasAchievementUnlocked(achievement.parentAchievement);
    }

    public <T extends IJsonSerializable> T func_150872_a(StatBase statBase, T t) {
        TupleIntJsonSerializable tupleIntJsonSerializable = this.statsData.get(statBase);
        if (tupleIntJsonSerializable == null) {
            tupleIntJsonSerializable = new TupleIntJsonSerializable();
            this.statsData.put(statBase, tupleIntJsonSerializable);
        }
        tupleIntJsonSerializable.setJsonSerializableValue(t);
        return t;
    }

    public boolean hasAchievementUnlocked(Achievement achievement) {
        return this.readStat(achievement) > 0;
    }

    public int readStat(StatBase statBase) {
        TupleIntJsonSerializable tupleIntJsonSerializable = this.statsData.get(statBase);
        return tupleIntJsonSerializable == null ? 0 : tupleIntJsonSerializable.getIntegerValue();
    }

    public void unlockAchievement(EntityPlayer entityPlayer, StatBase statBase, int n) {
        TupleIntJsonSerializable tupleIntJsonSerializable = this.statsData.get(statBase);
        if (tupleIntJsonSerializable == null) {
            tupleIntJsonSerializable = new TupleIntJsonSerializable();
            this.statsData.put(statBase, tupleIntJsonSerializable);
        }
        tupleIntJsonSerializable.setIntegerValue(n);
    }
}

