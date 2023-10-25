package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;

public class Scoreboard {
    private final Map<String, ScoreObjective> scoreObjectives = Maps.newHashMap();
    private final Map<IScoreObjectiveCriteria, List<ScoreObjective>> scoreObjectiveCriterias = Maps.newHashMap();
    private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives = Maps.newHashMap();
    private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
    private final Map<String, ScorePlayerTeam> teams = Maps.newHashMap();
    private final Map<String, ScorePlayerTeam> teamMemberships = Maps.newHashMap();
    private static String[] field_178823_g = null;

    public ScoreObjective getObjective(String name) {
        return (ScoreObjective)this.scoreObjectives.get((Object)name);
    }

    public ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria) {
        if (name.length() > 16) {
            throw new IllegalArgumentException("The objective name '" + name + "' is too long!");
        }
        ScoreObjective scoreobjective = this.getObjective(name);
        if (scoreobjective != null) {
            throw new IllegalArgumentException("An objective with the name '" + name + "' already exists!");
        }
        scoreobjective = new ScoreObjective(this, name, criteria);
        List list = (List)this.scoreObjectiveCriterias.get((Object)criteria);
        if (list == null) {
            list = Lists.newArrayList();
            this.scoreObjectiveCriterias.put((Object)criteria, (Object)list);
        }
        list.add((Object)scoreobjective);
        this.scoreObjectives.put((Object)name, (Object)scoreobjective);
        this.onScoreObjectiveAdded(scoreobjective);
        return scoreobjective;
    }

    public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreObjectiveCriteria criteria) {
        Collection collection = (Collection)this.scoreObjectiveCriterias.get((Object)criteria);
        return collection == null ? Lists.newArrayList() : Lists.newArrayList((Iterable)collection);
    }

    public boolean entityHasObjective(String name, ScoreObjective p_178819_2_) {
        Map map = (Map)this.entitiesScoreObjectives.get((Object)name);
        if (map == null) {
            return false;
        }
        Score score = (Score)map.get((Object)p_178819_2_);
        return score != null;
    }

    public Score getValueFromObjective(String name, ScoreObjective objective) {
        Score score;
        if (name.length() > 40) {
            throw new IllegalArgumentException("The player name '" + name + "' is too long!");
        }
        Map map = (Map)this.entitiesScoreObjectives.get((Object)name);
        if (map == null) {
            map = Maps.newHashMap();
            this.entitiesScoreObjectives.put((Object)name, (Object)map);
        }
        if ((score = (Score)map.get((Object)objective)) == null) {
            score = new Score(this, objective, name);
            map.put((Object)objective, (Object)score);
        }
        return score;
    }

    public Collection<Score> getSortedScores(ScoreObjective objective) {
        ArrayList list = Lists.newArrayList();
        for (Map map : this.entitiesScoreObjectives.values()) {
            Score score = (Score)map.get((Object)objective);
            if (score == null) continue;
            list.add((Object)score);
        }
        Collections.sort((List)list, (Comparator)Score.scoreComparator);
        return list;
    }

    public Collection<ScoreObjective> getScoreObjectives() {
        return this.scoreObjectives.values();
    }

    public Collection<String> getObjectiveNames() {
        return this.entitiesScoreObjectives.keySet();
    }

    public void removeObjectiveFromEntity(String name, ScoreObjective objective) {
        if (objective == null) {
            Map map = (Map)this.entitiesScoreObjectives.remove((Object)name);
            if (map != null) {
                this.func_96516_a(name);
            }
        } else {
            Map map2 = (Map)this.entitiesScoreObjectives.get((Object)name);
            if (map2 != null) {
                Score score = (Score)map2.remove((Object)objective);
                if (map2.size() < 1) {
                    Map map1 = (Map)this.entitiesScoreObjectives.remove((Object)name);
                    if (map1 != null) {
                        this.func_96516_a(name);
                    }
                } else if (score != null) {
                    this.func_178820_a(name, objective);
                }
            }
        }
    }

    public Collection<Score> getScores() {
        Collection collection = this.entitiesScoreObjectives.values();
        ArrayList list = Lists.newArrayList();
        for (Map map : collection) {
            list.addAll(map.values());
        }
        return list;
    }

    public Map<ScoreObjective, Score> getObjectivesForEntity(String name) {
        Map map = (Map)this.entitiesScoreObjectives.get((Object)name);
        if (map == null) {
            map = Maps.newHashMap();
        }
        return map;
    }

    public void removeObjective(ScoreObjective p_96519_1_) {
        this.scoreObjectives.remove((Object)p_96519_1_.getName());
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) != p_96519_1_) continue;
            this.setObjectiveInDisplaySlot(i, null);
        }
        List list = (List)this.scoreObjectiveCriterias.get((Object)p_96519_1_.getCriteria());
        if (list != null) {
            list.remove((Object)p_96519_1_);
        }
        for (Map map : this.entitiesScoreObjectives.values()) {
            map.remove((Object)p_96519_1_);
        }
        this.onScoreObjectiveRemoved(p_96519_1_);
    }

    public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_) {
        this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
    }

    public ScoreObjective getObjectiveInDisplaySlot(int p_96539_1_) {
        return this.objectiveDisplaySlots[p_96539_1_];
    }

    public ScorePlayerTeam getTeam(String p_96508_1_) {
        return (ScorePlayerTeam)this.teams.get((Object)p_96508_1_);
    }

    public ScorePlayerTeam createTeam(String name) {
        if (name.length() > 16) {
            throw new IllegalArgumentException("The team name '" + name + "' is too long!");
        }
        ScorePlayerTeam scoreplayerteam = this.getTeam(name);
        if (scoreplayerteam != null) {
            throw new IllegalArgumentException("A team with the name '" + name + "' already exists!");
        }
        scoreplayerteam = new ScorePlayerTeam(this, name);
        this.teams.put((Object)name, (Object)scoreplayerteam);
        this.broadcastTeamCreated(scoreplayerteam);
        return scoreplayerteam;
    }

    public void removeTeam(ScorePlayerTeam p_96511_1_) {
        this.teams.remove((Object)p_96511_1_.getRegisteredName());
        for (String s : p_96511_1_.getMembershipCollection()) {
            this.teamMemberships.remove((Object)s);
        }
        this.func_96513_c(p_96511_1_);
    }

    public boolean addPlayerToTeam(String player, String newTeam) {
        if (player.length() > 40) {
            throw new IllegalArgumentException("The player name '" + player + "' is too long!");
        }
        if (!this.teams.containsKey((Object)newTeam)) {
            return false;
        }
        ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);
        if (this.getPlayersTeam(player) != null) {
            this.removePlayerFromTeams(player);
        }
        this.teamMemberships.put((Object)player, (Object)scoreplayerteam);
        scoreplayerteam.getMembershipCollection().add((Object)player);
        return true;
    }

    public boolean removePlayerFromTeams(String p_96524_1_) {
        ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(p_96524_1_);
        if (scoreplayerteam != null) {
            this.removePlayerFromTeam(p_96524_1_, scoreplayerteam);
            return true;
        }
        return false;
    }

    public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_) {
        if (this.getPlayersTeam(p_96512_1_) != p_96512_2_) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + p_96512_2_.getRegisteredName() + "'.");
        }
        this.teamMemberships.remove((Object)p_96512_1_);
        p_96512_2_.getMembershipCollection().remove((Object)p_96512_1_);
    }

    public Collection<String> getTeamNames() {
        return this.teams.keySet();
    }

    public Collection<ScorePlayerTeam> getTeams() {
        return this.teams.values();
    }

    public ScorePlayerTeam getPlayersTeam(String p_96509_1_) {
        return (ScorePlayerTeam)this.teamMemberships.get((Object)p_96509_1_);
    }

    public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {
    }

    public void onObjectiveDisplayNameChanged(ScoreObjective p_96532_1_) {
    }

    public void onScoreObjectiveRemoved(ScoreObjective p_96533_1_) {
    }

    public void func_96536_a(Score p_96536_1_) {
    }

    public void func_96516_a(String p_96516_1_) {
    }

    public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_) {
    }

    public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {
    }

    public void sendTeamUpdate(ScorePlayerTeam playerTeam) {
    }

    public void func_96513_c(ScorePlayerTeam playerTeam) {
    }

    public static String getObjectiveDisplaySlot(int p_96517_0_) {
        EnumChatFormatting enumchatformatting;
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
        }
        if (p_96517_0_ >= 3 && p_96517_0_ <= 18 && (enumchatformatting = EnumChatFormatting.func_175744_a((int)(p_96517_0_ - 3))) != null && enumchatformatting != EnumChatFormatting.RESET) {
            return "sidebar.team." + enumchatformatting.getFriendlyName();
        }
        return null;
    }

    public static int getObjectiveDisplaySlotNumber(String p_96537_0_) {
        String s;
        EnumChatFormatting enumchatformatting;
        if (p_96537_0_.equalsIgnoreCase("list")) {
            return 0;
        }
        if (p_96537_0_.equalsIgnoreCase("sidebar")) {
            return 1;
        }
        if (p_96537_0_.equalsIgnoreCase("belowName")) {
            return 2;
        }
        if (p_96537_0_.startsWith("sidebar.team.") && (enumchatformatting = EnumChatFormatting.getValueByName((String)(s = p_96537_0_.substring("sidebar.team.".length())))) != null && enumchatformatting.getColorIndex() >= 0) {
            return enumchatformatting.getColorIndex() + 3;
        }
        return -1;
    }

    public static String[] getDisplaySlotStrings() {
        if (field_178823_g == null) {
            field_178823_g = new String[19];
            for (int i = 0; i < 19; ++i) {
                Scoreboard.field_178823_g[i] = Scoreboard.getObjectiveDisplaySlot(i);
            }
        }
        return field_178823_g;
    }

    public void func_181140_a(Entity p_181140_1_) {
        if (p_181140_1_ != null && !(p_181140_1_ instanceof EntityPlayer) && !p_181140_1_.isEntityAlive()) {
            String s = p_181140_1_.getUniqueID().toString();
            this.removeObjectiveFromEntity(s, null);
            this.removePlayerFromTeams(s);
        }
    }
}
