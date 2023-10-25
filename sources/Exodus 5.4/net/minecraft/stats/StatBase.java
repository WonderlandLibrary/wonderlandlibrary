/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.stats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.event.HoverEvent;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.stats.IStatType;
import net.minecraft.stats.ObjectiveStat;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IJsonSerializable;

public class StatBase {
    public static IStatType simpleStatType;
    public static IStatType field_111202_k;
    public static IStatType timeStatType;
    private final IChatComponent statName;
    public final String statId;
    public boolean isIndependent;
    private static DecimalFormat decimalFormat;
    private Class<? extends IJsonSerializable> field_150956_d;
    private final IScoreObjectiveCriteria field_150957_c;
    private final IStatType type;
    public static IStatType distanceStatType;
    private static NumberFormat numberFormat;

    public StatBase(String string, IChatComponent iChatComponent, IStatType iStatType) {
        this.statId = string;
        this.statName = iChatComponent;
        this.type = iStatType;
        this.field_150957_c = new ObjectiveStat(this);
        IScoreObjectiveCriteria.INSTANCES.put(this.field_150957_c.getName(), this.field_150957_c);
    }

    public Class<? extends IJsonSerializable> func_150954_l() {
        return this.field_150956_d;
    }

    public IScoreObjectiveCriteria func_150952_k() {
        return this.field_150957_c;
    }

    public String format(int n) {
        return this.type.format(n);
    }

    public StatBase registerStat() {
        if (StatList.oneShotStats.containsKey(this.statId)) {
            throw new RuntimeException("Duplicate stat id: \"" + StatList.oneShotStats.get((Object)this.statId).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
        }
        StatList.allStats.add(this);
        StatList.oneShotStats.put(this.statId, this);
        return this;
    }

    public StatBase initIndependentStat() {
        this.isIndependent = true;
        return this;
    }

    public boolean isAchievement() {
        return false;
    }

    public String toString() {
        return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.type + ", objectiveCriteria=" + this.field_150957_c + '}';
    }

    static {
        numberFormat = NumberFormat.getIntegerInstance(Locale.US);
        simpleStatType = new IStatType(){

            @Override
            public String format(int n) {
                return numberFormat.format(n);
            }
        };
        decimalFormat = new DecimalFormat("########0.00");
        timeStatType = new IStatType(){

            @Override
            public String format(int n) {
                double d = (double)n / 20.0;
                double d2 = d / 60.0;
                double d3 = d2 / 60.0;
                double d4 = d3 / 24.0;
                double d5 = d4 / 365.0;
                return d5 > 0.5 ? String.valueOf(decimalFormat.format(d5)) + " y" : (d4 > 0.5 ? String.valueOf(decimalFormat.format(d4)) + " d" : (d3 > 0.5 ? String.valueOf(decimalFormat.format(d3)) + " h" : (d2 > 0.5 ? String.valueOf(decimalFormat.format(d2)) + " m" : String.valueOf(d) + " s")));
            }
        };
        distanceStatType = new IStatType(){

            @Override
            public String format(int n) {
                double d = (double)n / 100.0;
                double d2 = d / 1000.0;
                return d2 > 0.5 ? String.valueOf(decimalFormat.format(d2)) + " km" : (d > 0.5 ? String.valueOf(decimalFormat.format(d)) + " m" : String.valueOf(n) + " cm");
            }
        };
        field_111202_k = new IStatType(){

            @Override
            public String format(int n) {
                return decimalFormat.format((double)n * 0.1);
            }
        };
    }

    public StatBase func_150953_b(Class<? extends IJsonSerializable> clazz) {
        this.field_150956_d = clazz;
        return this;
    }

    public IChatComponent getStatName() {
        IChatComponent iChatComponent = this.statName.createCopy();
        iChatComponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
        iChatComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ChatComponentText(this.statId)));
        return iChatComponent;
    }

    public int hashCode() {
        return this.statId.hashCode();
    }

    public StatBase(String string, IChatComponent iChatComponent) {
        this(string, iChatComponent, simpleStatType);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            StatBase statBase = (StatBase)object;
            return this.statId.equals(statBase.statId);
        }
        return false;
    }

    public IChatComponent func_150955_j() {
        IChatComponent iChatComponent = this.getStatName();
        IChatComponent iChatComponent2 = new ChatComponentText("[").appendSibling(iChatComponent).appendText("]");
        iChatComponent2.setChatStyle(iChatComponent.getChatStyle());
        return iChatComponent2;
    }
}

