/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.stats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreCriteriaStat;
import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatList;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class StatBase {
    public final String statId;
    private final ITextComponent statName;
    public boolean isIndependent;
    private final IStatType formatter;
    private final IScoreCriteria objectiveCriteria;
    private Class<? extends IJsonSerializable> serializableClazz;
    private static final NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
    public static IStatType simpleStatType = new IStatType(){

        @Override
        public String format(int number) {
            return numberFormat.format(number);
        }
    };
    private static final DecimalFormat decimalFormat = new DecimalFormat("########0.00");
    public static IStatType timeStatType = new IStatType(){

        @Override
        public String format(int number) {
            double d0 = (double)number / 20.0;
            double d1 = d0 / 60.0;
            double d2 = d1 / 60.0;
            double d3 = d2 / 24.0;
            double d4 = d3 / 365.0;
            if (d4 > 0.5) {
                return decimalFormat.format(d4) + " y";
            }
            if (d3 > 0.5) {
                return decimalFormat.format(d3) + " d";
            }
            if (d2 > 0.5) {
                return decimalFormat.format(d2) + " h";
            }
            return d1 > 0.5 ? decimalFormat.format(d1) + " m" : d0 + " s";
        }
    };
    public static IStatType distanceStatType = new IStatType(){

        @Override
        public String format(int number) {
            double d0 = (double)number / 100.0;
            double d1 = d0 / 1000.0;
            if (d1 > 0.5) {
                return decimalFormat.format(d1) + " km";
            }
            return d0 > 0.5 ? decimalFormat.format(d0) + " m" : number + " cm";
        }
    };
    public static IStatType divideByTen = new IStatType(){

        @Override
        public String format(int number) {
            return decimalFormat.format((double)number * 0.1);
        }
    };

    public StatBase(String statIdIn, ITextComponent statNameIn, IStatType formatterIn) {
        this.statId = statIdIn;
        this.statName = statNameIn;
        this.formatter = formatterIn;
        this.objectiveCriteria = new ScoreCriteriaStat(this);
        IScoreCriteria.INSTANCES.put(this.objectiveCriteria.getName(), this.objectiveCriteria);
    }

    public StatBase(String statIdIn, ITextComponent statNameIn) {
        this(statIdIn, statNameIn, simpleStatType);
    }

    public StatBase initIndependentStat() {
        this.isIndependent = true;
        return this;
    }

    public StatBase registerStat() {
        if (StatList.ID_TO_STAT_MAP.containsKey(this.statId)) {
            throw new RuntimeException("Duplicate stat id: \"" + StatList.ID_TO_STAT_MAP.get((Object)this.statId).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
        }
        StatList.ALL_STATS.add(this);
        StatList.ID_TO_STAT_MAP.put(this.statId, this);
        return this;
    }

    public String format(int number) {
        return this.formatter.format(number);
    }

    public ITextComponent getStatName() {
        ITextComponent itextcomponent = this.statName.createCopy();
        itextcomponent.getStyle().setColor(TextFormatting.GRAY);
        return itextcomponent;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            StatBase statbase = (StatBase)p_equals_1_;
            return this.statId.equals(statbase.statId);
        }
        return false;
    }

    public int hashCode() {
        return this.statId.hashCode();
    }

    public String toString() {
        return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.formatter + ", objectiveCriteria=" + this.objectiveCriteria + '}';
    }

    public IScoreCriteria getCriteria() {
        return this.objectiveCriteria;
    }

    public Class<? extends IJsonSerializable> getSerializableClazz() {
        return this.serializableClazz;
    }
}

