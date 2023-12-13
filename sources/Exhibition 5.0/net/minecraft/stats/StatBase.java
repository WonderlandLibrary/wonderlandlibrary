// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.stats;

import java.util.Locale;
import net.minecraft.util.ChatComponentText;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.EnumChatFormatting;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.util.IChatComponent;

public class StatBase
{
    public final String statId;
    private final IChatComponent statName;
    public boolean isIndependent;
    private final IStatType type;
    private final IScoreObjectiveCriteria field_150957_c;
    private Class field_150956_d;
    private static NumberFormat numberFormat;
    public static IStatType simpleStatType;
    private static DecimalFormat decimalFormat;
    public static IStatType timeStatType;
    public static IStatType distanceStatType;
    public static IStatType field_111202_k;
    private static final String __OBFID = "CL_00001472";
    
    public StatBase(final String p_i45307_1_, final IChatComponent p_i45307_2_, final IStatType p_i45307_3_) {
        this.statId = p_i45307_1_;
        this.statName = p_i45307_2_;
        this.type = p_i45307_3_;
        this.field_150957_c = new ObjectiveStat(this);
        IScoreObjectiveCriteria.INSTANCES.put(this.field_150957_c.getName(), this.field_150957_c);
    }
    
    public StatBase(final String p_i45308_1_, final IChatComponent p_i45308_2_) {
        this(p_i45308_1_, p_i45308_2_, StatBase.simpleStatType);
    }
    
    public StatBase initIndependentStat() {
        this.isIndependent = true;
        return this;
    }
    
    public StatBase registerStat() {
        if (StatList.oneShotStats.containsKey(this.statId)) {
            throw new RuntimeException("Duplicate stat id: \"" + StatList.oneShotStats.get(this.statId).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
        }
        StatList.allStats.add(this);
        StatList.oneShotStats.put(this.statId, this);
        return this;
    }
    
    public boolean isAchievement() {
        return false;
    }
    
    public String func_75968_a(final int p_75968_1_) {
        return this.type.format(p_75968_1_);
    }
    
    public IChatComponent getStatName() {
        final IChatComponent var1 = this.statName.createCopy();
        var1.getChatStyle().setColor(EnumChatFormatting.GRAY);
        var1.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ChatComponentText(this.statId)));
        return var1;
    }
    
    public IChatComponent func_150955_j() {
        final IChatComponent var1 = this.getStatName();
        final IChatComponent var2 = new ChatComponentText("[").appendSibling(var1).appendText("]");
        var2.setChatStyle(var1.getChatStyle());
        return var2;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            final StatBase var2 = (StatBase)p_equals_1_;
            return this.statId.equals(var2.statId);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.statId.hashCode();
    }
    
    @Override
    public String toString() {
        return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.type + ", objectiveCriteria=" + this.field_150957_c + '}';
    }
    
    public IScoreObjectiveCriteria func_150952_k() {
        return this.field_150957_c;
    }
    
    public Class func_150954_l() {
        return this.field_150956_d;
    }
    
    public StatBase func_150953_b(final Class p_150953_1_) {
        this.field_150956_d = p_150953_1_;
        return this;
    }
    
    static {
        StatBase.numberFormat = NumberFormat.getIntegerInstance(Locale.US);
        StatBase.simpleStatType = new IStatType() {
            private static final String __OBFID = "CL_00001473";
            
            @Override
            public String format(final int p_75843_1_) {
                return StatBase.numberFormat.format(p_75843_1_);
            }
        };
        StatBase.decimalFormat = new DecimalFormat("########0.00");
        StatBase.timeStatType = new IStatType() {
            private static final String __OBFID = "CL_00001474";
            
            @Override
            public String format(final int p_75843_1_) {
                final double var2 = p_75843_1_ / 20.0;
                final double var3 = var2 / 60.0;
                final double var4 = var3 / 60.0;
                final double var5 = var4 / 24.0;
                final double var6 = var5 / 365.0;
                return (var6 > 0.5) ? (StatBase.decimalFormat.format(var6) + " y") : ((var5 > 0.5) ? (StatBase.decimalFormat.format(var5) + " d") : ((var4 > 0.5) ? (StatBase.decimalFormat.format(var4) + " h") : ((var3 > 0.5) ? (StatBase.decimalFormat.format(var3) + " m") : (var2 + " s"))));
            }
        };
        StatBase.distanceStatType = new IStatType() {
            private static final String __OBFID = "CL_00001475";
            
            @Override
            public String format(final int p_75843_1_) {
                final double var2 = p_75843_1_ / 100.0;
                final double var3 = var2 / 1000.0;
                return (var3 > 0.5) ? (StatBase.decimalFormat.format(var3) + " km") : ((var2 > 0.5) ? (StatBase.decimalFormat.format(var2) + " m") : (p_75843_1_ + " cm"));
            }
        };
        StatBase.field_111202_k = new IStatType() {
            private static final String __OBFID = "CL_00001476";
            
            @Override
            public String format(final int p_75843_1_) {
                return StatBase.decimalFormat.format(p_75843_1_ * 0.1);
            }
        };
    }
}
