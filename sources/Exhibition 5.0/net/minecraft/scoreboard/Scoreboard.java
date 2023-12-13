// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.scoreboard;

import net.minecraft.util.EnumChatFormatting;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Map;

public class Scoreboard
{
    private final Map scoreObjectives;
    private final Map scoreObjectiveCriterias;
    private final Map field_96544_c;
    private final ScoreObjective[] objectiveDisplaySlots;
    private final Map teams;
    private final Map teamMemberships;
    private static String[] field_178823_g;
    private static final String __OBFID = "CL_00000619";
    
    public Scoreboard() {
        this.scoreObjectives = Maps.newHashMap();
        this.scoreObjectiveCriterias = Maps.newHashMap();
        this.field_96544_c = Maps.newHashMap();
        this.objectiveDisplaySlots = new ScoreObjective[19];
        this.teams = Maps.newHashMap();
        this.teamMemberships = Maps.newHashMap();
    }
    
    public ScoreObjective getObjective(final String p_96518_1_) {
        return this.scoreObjectives.get(p_96518_1_);
    }
    
    public ScoreObjective addScoreObjective(final String p_96535_1_, final IScoreObjectiveCriteria p_96535_2_) {
        ScoreObjective var3 = this.getObjective(p_96535_1_);
        if (var3 != null) {
            throw new IllegalArgumentException("An objective with the name '" + p_96535_1_ + "' already exists!");
        }
        var3 = new ScoreObjective(this, p_96535_1_, p_96535_2_);
        Object var4 = this.scoreObjectiveCriterias.get(p_96535_2_);
        if (var4 == null) {
            var4 = Lists.newArrayList();
            this.scoreObjectiveCriterias.put(p_96535_2_, var4);
        }
        ((List)var4).add(var3);
        this.scoreObjectives.put(p_96535_1_, var3);
        this.func_96522_a(var3);
        return var3;
    }
    
    public Collection func_96520_a(final IScoreObjectiveCriteria p_96520_1_) {
        final Collection var2 = this.scoreObjectiveCriterias.get(p_96520_1_);
        return (var2 == null) ? Lists.newArrayList() : Lists.newArrayList((Iterable)var2);
    }
    
    public boolean func_178819_b(final String p_178819_1_, final ScoreObjective p_178819_2_) {
        final Map var3 = this.field_96544_c.get(p_178819_1_);
        if (var3 == null) {
            return false;
        }
        final Score var4 = var3.get(p_178819_2_);
        return var4 != null;
    }
    
    public Score getValueFromObjective(final String p_96529_1_, final ScoreObjective p_96529_2_) {
        Object var3 = this.field_96544_c.get(p_96529_1_);
        if (var3 == null) {
            var3 = Maps.newHashMap();
            this.field_96544_c.put(p_96529_1_, var3);
        }
        Score var4 = ((Map)var3).get(p_96529_2_);
        if (var4 == null) {
            var4 = new Score(this, p_96529_2_, p_96529_1_);
            ((Map)var3).put(p_96529_2_, var4);
        }
        return var4;
    }
    
    public Collection getSortedScores(final ScoreObjective p_96534_1_) {
        final ArrayList var2 = Lists.newArrayList();
        for (final Map var4 : this.field_96544_c.values()) {
            final Score var5 = var4.get(p_96534_1_);
            if (var5 != null) {
                var2.add(var5);
            }
        }
        Collections.sort((List<Object>)var2, Score.scoreComparator);
        return var2;
    }
    
    public Collection getScoreObjectives() {
        return this.scoreObjectives.values();
    }
    
    public Collection getObjectiveNames() {
        return this.field_96544_c.keySet();
    }
    
    public void func_178822_d(final String p_178822_1_, final ScoreObjective p_178822_2_) {
        if (p_178822_2_ == null) {
            final Map var3 = this.field_96544_c.remove(p_178822_1_);
            if (var3 != null) {
                this.func_96516_a(p_178822_1_);
            }
        }
        else {
            final Map var3 = this.field_96544_c.get(p_178822_1_);
            if (var3 != null) {
                final Score var4 = var3.remove(p_178822_2_);
                if (var3.size() < 1) {
                    final Map var5 = this.field_96544_c.remove(p_178822_1_);
                    if (var5 != null) {
                        this.func_96516_a(p_178822_1_);
                    }
                }
                else if (var4 != null) {
                    this.func_178820_a(p_178822_1_, p_178822_2_);
                }
            }
        }
    }
    
    public Collection func_96528_e() {
        final Collection var1 = this.field_96544_c.values();
        final ArrayList var2 = Lists.newArrayList();
        for (final Map var4 : var1) {
            var2.addAll(var4.values());
        }
        return var2;
    }
    
    public Map func_96510_d(final String p_96510_1_) {
        Object var2 = this.field_96544_c.get(p_96510_1_);
        if (var2 == null) {
            var2 = Maps.newHashMap();
        }
        return (Map)var2;
    }
    
    public void func_96519_k(final ScoreObjective p_96519_1_) {
        this.scoreObjectives.remove(p_96519_1_.getName());
        for (int var2 = 0; var2 < 19; ++var2) {
            if (this.getObjectiveInDisplaySlot(var2) == p_96519_1_) {
                this.setObjectiveInDisplaySlot(var2, null);
            }
        }
        final List var3 = this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());
        if (var3 != null) {
            var3.remove(p_96519_1_);
        }
        for (final Map var5 : this.field_96544_c.values()) {
            var5.remove(p_96519_1_);
        }
        this.func_96533_c(p_96519_1_);
    }
    
    public void setObjectiveInDisplaySlot(final int p_96530_1_, final ScoreObjective p_96530_2_) {
        this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
    }
    
    public ScoreObjective getObjectiveInDisplaySlot(final int p_96539_1_) {
        return this.objectiveDisplaySlots[p_96539_1_];
    }
    
    public ScorePlayerTeam getTeam(final String p_96508_1_) {
        return this.teams.get(p_96508_1_);
    }
    
    public ScorePlayerTeam createTeam(final String p_96527_1_) {
        ScorePlayerTeam var2 = this.getTeam(p_96527_1_);
        if (var2 != null) {
            throw new IllegalArgumentException("A team with the name '" + p_96527_1_ + "' already exists!");
        }
        var2 = new ScorePlayerTeam(this, p_96527_1_);
        this.teams.put(p_96527_1_, var2);
        this.broadcastTeamCreated(var2);
        return var2;
    }
    
    public void removeTeam(final ScorePlayerTeam p_96511_1_) {
        this.teams.remove(p_96511_1_.getRegisteredName());
        for (final String var3 : p_96511_1_.getMembershipCollection()) {
            this.teamMemberships.remove(var3);
        }
        this.func_96513_c(p_96511_1_);
    }
    
    public boolean func_151392_a(final String p_151392_1_, final String p_151392_2_) {
        if (!this.teams.containsKey(p_151392_2_)) {
            return false;
        }
        final ScorePlayerTeam var3 = this.getTeam(p_151392_2_);
        if (this.getPlayersTeam(p_151392_1_) != null) {
            this.removePlayerFromTeams(p_151392_1_);
        }
        this.teamMemberships.put(p_151392_1_, var3);
        var3.getMembershipCollection().add(p_151392_1_);
        return true;
    }
    
    public boolean removePlayerFromTeams(final String p_96524_1_) {
        final ScorePlayerTeam var2 = this.getPlayersTeam(p_96524_1_);
        if (var2 != null) {
            this.removePlayerFromTeam(p_96524_1_, var2);
            return true;
        }
        return false;
    }
    
    public void removePlayerFromTeam(final String p_96512_1_, final ScorePlayerTeam p_96512_2_) {
        if (this.getPlayersTeam(p_96512_1_) != p_96512_2_) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + p_96512_2_.getRegisteredName() + "'.");
        }
        this.teamMemberships.remove(p_96512_1_);
        p_96512_2_.getMembershipCollection().remove(p_96512_1_);
    }
    
    public Collection getTeamNames() {
        return this.teams.keySet();
    }
    
    public Collection getTeams() {
        return this.teams.values();
    }
    
    public ScorePlayerTeam getPlayersTeam(final String p_96509_1_) {
        return this.teamMemberships.get(p_96509_1_);
    }
    
    public void func_96522_a(final ScoreObjective p_96522_1_) {
    }
    
    public void func_96532_b(final ScoreObjective p_96532_1_) {
    }
    
    public void func_96533_c(final ScoreObjective p_96533_1_) {
    }
    
    public void func_96536_a(final Score p_96536_1_) {
    }
    
    public void func_96516_a(final String p_96516_1_) {
    }
    
    public void func_178820_a(final String p_178820_1_, final ScoreObjective p_178820_2_) {
    }
    
    public void broadcastTeamCreated(final ScorePlayerTeam p_96523_1_) {
    }
    
    public void broadcastTeamRemoved(final ScorePlayerTeam p_96538_1_) {
    }
    
    public void func_96513_c(final ScorePlayerTeam p_96513_1_) {
    }
    
    public static String getObjectiveDisplaySlot(final int p_96517_0_) {
        switch (p_96517_0_) {
            case 0: {
                return "list";
            }
            case 1: {
                return "sidebar";
            }
            case 2: {
                return "belowName";
            }
            default: {
                if (p_96517_0_ >= 3 && p_96517_0_ <= 18) {
                    final EnumChatFormatting var1 = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);
                    if (var1 != null && var1 != EnumChatFormatting.RESET) {
                        return "sidebar.team." + var1.getFriendlyName();
                    }
                }
                return null;
            }
        }
    }
    
    public static int getObjectiveDisplaySlotNumber(final String p_96537_0_) {
        if (p_96537_0_.equalsIgnoreCase("list")) {
            return 0;
        }
        if (p_96537_0_.equalsIgnoreCase("sidebar")) {
            return 1;
        }
        if (p_96537_0_.equalsIgnoreCase("belowName")) {
            return 2;
        }
        if (p_96537_0_.startsWith("sidebar.team.")) {
            final String var1 = p_96537_0_.substring("sidebar.team.".length());
            final EnumChatFormatting var2 = EnumChatFormatting.getValueByName(var1);
            if (var2 != null && var2.func_175746_b() >= 0) {
                return var2.func_175746_b() + 3;
            }
        }
        return -1;
    }
    
    public static String[] func_178821_h() {
        if (Scoreboard.field_178823_g == null) {
            Scoreboard.field_178823_g = new String[19];
            for (int var0 = 0; var0 < 19; ++var0) {
                Scoreboard.field_178823_g[var0] = getObjectiveDisplaySlot(var0);
            }
        }
        return Scoreboard.field_178823_g;
    }
    
    static {
        Scoreboard.field_178823_g = null;
    }
}
