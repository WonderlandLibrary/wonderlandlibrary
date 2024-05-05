/*
 * Decompiled with CFR 0.152.
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

    public boolean hasAchievementUnlocked(Achievement achievementIn) {
        if (this.readStat(achievementIn) <= 0) return false;
        return true;
    }

    public boolean canUnlockAchievement(Achievement achievementIn) {
        if (achievementIn.parentAchievement == null) return true;
        if (this.hasAchievementUnlocked(achievementIn.parentAchievement)) return true;
        return false;
    }

    public int func_150874_c(Achievement p_150874_1_) {
        if (this.hasAchievementUnlocked(p_150874_1_)) {
            return 0;
        }
        int i = 0;
        Achievement achievement = p_150874_1_.parentAchievement;
        while (achievement != null) {
            if (this.hasAchievementUnlocked(achievement)) return i;
            achievement = achievement.parentAchievement;
            ++i;
        }
        return i;
    }

    public void increaseStat(EntityPlayer player, StatBase stat, int amount) {
        if (stat.isAchievement()) {
            if (!this.canUnlockAchievement((Achievement)stat)) return;
        }
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
        if (tupleintjsonserializable == null) {
            return 0;
        }
        int n = tupleintjsonserializable.getIntegerValue();
        return n;
    }

    public <T extends IJsonSerializable> T func_150870_b(StatBase p_150870_1_) {
        T t;
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(p_150870_1_);
        if (tupleintjsonserializable != null) {
            t = tupleintjsonserializable.getJsonSerializableValue();
            return t;
        }
        t = null;
        return t;
    }

    public <T extends IJsonSerializable> T func_150872_a(StatBase p_150872_1_, T p_150872_2_) {
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(p_150872_1_);
        if (tupleintjsonserializable == null) {
            tupleintjsonserializable = new TupleIntJsonSerializable();
            this.statsData.put(p_150872_1_, tupleintjsonserializable);
        }
        tupleintjsonserializable.setJsonSerializableValue(p_150872_2_);
        return p_150872_2_;
    }
}

