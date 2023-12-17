// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import java.util.Locale;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.scoreboard.ScoreCriteriaStat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.util.text.ITextComponent;

public class StatBase
{
    public final String statId;
    private final ITextComponent statName;
    public boolean isIndependent;
    private final IStatType formatter;
    private final IScoreCriteria objectiveCriteria;
    private Class<? extends IJsonSerializable> serializableClazz;
    private static final NumberFormat numberFormat;
    public static IStatType simpleStatType;
    private static final DecimalFormat decimalFormat;
    public static IStatType timeStatType;
    public static IStatType distanceStatType;
    public static IStatType divideByTen;
    
    public StatBase(final String statIdIn, final ITextComponent statNameIn, final IStatType formatterIn) {
        this.statId = statIdIn;
        this.statName = statNameIn;
        this.formatter = formatterIn;
        this.objectiveCriteria = new ScoreCriteriaStat(this);
        IScoreCriteria.INSTANCES.put(this.objectiveCriteria.getName(), this.objectiveCriteria);
    }
    
    public StatBase(final String statIdIn, final ITextComponent statNameIn) {
        this(statIdIn, statNameIn, StatBase.simpleStatType);
    }
    
    public StatBase initIndependentStat() {
        this.isIndependent = true;
        return this;
    }
    
    public StatBase registerStat() {
        if (StatList.ID_TO_STAT_MAP.containsKey(this.statId)) {
            throw new RuntimeException("Duplicate stat id: \"" + StatList.ID_TO_STAT_MAP.get(this.statId).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
        }
        StatList.ALL_STATS.add(this);
        StatList.ID_TO_STAT_MAP.put(this.statId, this);
        return this;
    }
    
    public String format(final int number) {
        return this.formatter.format(number);
    }
    
    public ITextComponent getStatName() {
        final ITextComponent itextcomponent = this.statName.createCopy();
        itextcomponent.getStyle().setColor(TextFormatting.GRAY);
        return itextcomponent;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            final StatBase statbase = (StatBase)p_equals_1_;
            return this.statId.equals(statbase.statId);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.statId.hashCode();
    }
    
    @Override
    public String toString() {
        return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.formatter + ", objectiveCriteria=" + this.objectiveCriteria + '}';
    }
    
    public IScoreCriteria getCriteria() {
        return this.objectiveCriteria;
    }
    
    public Class<? extends IJsonSerializable> getSerializableClazz() {
        return this.serializableClazz;
    }
    
    static {
        numberFormat = NumberFormat.getIntegerInstance(Locale.US);
        StatBase.simpleStatType = new IStatType() {
            @Override
            public String format(final int number) {
                return StatBase.numberFormat.format(number);
            }
        };
        decimalFormat = new DecimalFormat("########0.00");
        StatBase.timeStatType = new IStatType() {
            @Override
            public String format(final int number) {
                final double d0 = number / 20.0;
                final double d2 = d0 / 60.0;
                final double d3 = d2 / 60.0;
                final double d4 = d3 / 24.0;
                final double d5 = d4 / 365.0;
                if (d5 > 0.5) {
                    return StatBase.decimalFormat.format(d5) + " y";
                }
                if (d4 > 0.5) {
                    return StatBase.decimalFormat.format(d4) + " d";
                }
                if (d3 > 0.5) {
                    return StatBase.decimalFormat.format(d3) + " h";
                }
                return (d2 > 0.5) ? (StatBase.decimalFormat.format(d2) + " m") : (d0 + " s");
            }
        };
        StatBase.distanceStatType = new IStatType() {
            @Override
            public String format(final int number) {
                final double d0 = number / 100.0;
                final double d2 = d0 / 1000.0;
                if (d2 > 0.5) {
                    return StatBase.decimalFormat.format(d2) + " km";
                }
                return (d0 > 0.5) ? (StatBase.decimalFormat.format(d0) + " m") : (number + " cm");
            }
        };
        StatBase.divideByTen = new IStatType() {
            @Override
            public String format(final int number) {
                return StatBase.decimalFormat.format(number * 0.1);
            }
        };
    }
}
