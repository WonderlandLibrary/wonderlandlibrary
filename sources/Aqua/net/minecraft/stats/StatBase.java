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
    public final String statId;
    private final IChatComponent statName;
    public boolean isIndependent;
    private final IStatType type;
    private final IScoreObjectiveCriteria objectiveCriteria;
    private Class<? extends IJsonSerializable> field_150956_d;
    private static NumberFormat numberFormat = NumberFormat.getIntegerInstance((Locale)Locale.US);
    public static IStatType simpleStatType = new /* Unavailable Anonymous Inner Class!! */;
    private static DecimalFormat decimalFormat = new DecimalFormat("########0.00");
    public static IStatType timeStatType = new /* Unavailable Anonymous Inner Class!! */;
    public static IStatType distanceStatType = new /* Unavailable Anonymous Inner Class!! */;
    public static IStatType field_111202_k = new /* Unavailable Anonymous Inner Class!! */;

    public StatBase(String statIdIn, IChatComponent statNameIn, IStatType typeIn) {
        this.statId = statIdIn;
        this.statName = statNameIn;
        this.type = typeIn;
        this.objectiveCriteria = new ObjectiveStat(this);
        IScoreObjectiveCriteria.INSTANCES.put((Object)this.objectiveCriteria.getName(), (Object)this.objectiveCriteria);
    }

    public StatBase(String statIdIn, IChatComponent statNameIn) {
        this(statIdIn, statNameIn, simpleStatType);
    }

    public StatBase initIndependentStat() {
        this.isIndependent = true;
        return this;
    }

    public StatBase registerStat() {
        if (StatList.oneShotStats.containsKey((Object)this.statId)) {
            throw new RuntimeException("Duplicate stat id: \"" + ((StatBase)StatList.oneShotStats.get((Object)this.statId)).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
        }
        StatList.allStats.add((Object)this);
        StatList.oneShotStats.put((Object)this.statId, (Object)this);
        return this;
    }

    public boolean isAchievement() {
        return false;
    }

    public String format(int p_75968_1_) {
        return this.type.format(p_75968_1_);
    }

    public IChatComponent getStatName() {
        IChatComponent ichatcomponent = this.statName.createCopy();
        ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
        ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, (IChatComponent)new ChatComponentText(this.statId)));
        return ichatcomponent;
    }

    public IChatComponent createChatComponent() {
        IChatComponent ichatcomponent = this.getStatName();
        IChatComponent ichatcomponent1 = new ChatComponentText("[").appendSibling(ichatcomponent).appendText("]");
        ichatcomponent1.setChatStyle(ichatcomponent.getChatStyle());
        return ichatcomponent1;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            StatBase statbase = (StatBase)p_equals_1_;
            return this.statId.equals((Object)statbase.statId);
        }
        return false;
    }

    public int hashCode() {
        return this.statId.hashCode();
    }

    public String toString() {
        return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.type + ", objectiveCriteria=" + this.objectiveCriteria + '}';
    }

    public IScoreObjectiveCriteria getCriteria() {
        return this.objectiveCriteria;
    }

    public Class<? extends IJsonSerializable> func_150954_l() {
        return this.field_150956_d;
    }

    public StatBase func_150953_b(Class<? extends IJsonSerializable> p_150953_1_) {
        this.field_150956_d = p_150953_1_;
        return this;
    }

    static /* synthetic */ NumberFormat access$000() {
        return numberFormat;
    }

    static /* synthetic */ DecimalFormat access$100() {
        return decimalFormat;
    }
}
