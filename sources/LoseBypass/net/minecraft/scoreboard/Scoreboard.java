/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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
        return this.scoreObjectives.get(name);
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
        List<ScoreObjective> list = this.scoreObjectiveCriterias.get(criteria);
        if (list == null) {
            list = Lists.newArrayList();
            this.scoreObjectiveCriterias.put(criteria, list);
        }
        list.add(scoreobjective);
        this.scoreObjectives.put(name, scoreobjective);
        this.onScoreObjectiveAdded(scoreobjective);
        return scoreobjective;
    }

    public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreObjectiveCriteria criteria) {
        ArrayList<ScoreObjective> arrayList;
        Collection collection = this.scoreObjectiveCriterias.get(criteria);
        if (collection == null) {
            arrayList = Lists.newArrayList();
            return arrayList;
        }
        arrayList = Lists.newArrayList(collection);
        return arrayList;
    }

    public boolean entityHasObjective(String name, ScoreObjective p_178819_2_) {
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            return false;
        }
        Score score = map.get(p_178819_2_);
        if (score == null) return false;
        return true;
    }

    public Score getValueFromObjective(String name, ScoreObjective objective) {
        Score score;
        if (name.length() > 40) {
            throw new IllegalArgumentException("The player name '" + name + "' is too long!");
        }
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            map = Maps.newHashMap();
            this.entitiesScoreObjectives.put(name, map);
        }
        if ((score = map.get(objective)) != null) return score;
        score = new Score(this, objective, name);
        map.put(objective, score);
        return score;
    }

    public Collection<Score> getSortedScores(ScoreObjective objective) {
        ArrayList<Score> list = Lists.newArrayList();
        Iterator<Map<ScoreObjective, Score>> iterator = this.entitiesScoreObjectives.values().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                Collections.sort(list, Score.scoreComparator);
                return list;
            }
            Map<ScoreObjective, Score> map = iterator.next();
            Score score = map.get(objective);
            if (score == null) continue;
            list.add(score);
        }
    }

    public Collection<ScoreObjective> getScoreObjectives() {
        return this.scoreObjectives.values();
    }

    public Collection<String> getObjectiveNames() {
        return this.entitiesScoreObjectives.keySet();
    }

    public void removeObjectiveFromEntity(String name, ScoreObjective objective) {
        if (objective == null) {
            Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.remove(name);
            if (map == null) return;
            this.func_96516_a(name);
            return;
        }
        Map<ScoreObjective, Score> map2 = this.entitiesScoreObjectives.get(name);
        if (map2 == null) return;
        Score score = map2.remove(objective);
        if (map2.size() < 1) {
            Map<ScoreObjective, Score> map1 = this.entitiesScoreObjectives.remove(name);
            if (map1 == null) return;
            this.func_96516_a(name);
            return;
        }
        if (score == null) return;
        this.func_178820_a(name, objective);
    }

    public Collection<Score> getScores() {
        Collection<Map<ScoreObjective, Score>> collection = this.entitiesScoreObjectives.values();
        ArrayList<Score> list = Lists.newArrayList();
        Iterator<Map<ScoreObjective, Score>> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Map<ScoreObjective, Score> map = iterator.next();
            list.addAll(map.values());
        }
        return list;
    }

    public Map<ScoreObjective, Score> getObjectivesForEntity(String name) {
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map != null) return map;
        return Maps.newHashMap();
    }

    public void removeObjective(ScoreObjective p_96519_1_) {
        this.scoreObjectives.remove(p_96519_1_.getName());
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) != p_96519_1_) continue;
            this.setObjectiveInDisplaySlot(i, null);
        }
        List<ScoreObjective> list = this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());
        if (list != null) {
            list.remove(p_96519_1_);
        }
        Iterator<Map<ScoreObjective, Score>> iterator = this.entitiesScoreObjectives.values().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.func_96533_c(p_96519_1_);
                return;
            }
            Map<ScoreObjective, Score> map = iterator.next();
            map.remove(p_96519_1_);
        }
    }

    public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_) {
        this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
    }

    public ScoreObjective getObjectiveInDisplaySlot(int p_96539_1_) {
        return this.objectiveDisplaySlots[p_96539_1_];
    }

    public ScorePlayerTeam getTeam(String p_96508_1_) {
        return this.teams.get(p_96508_1_);
    }

    public ScorePlayerTeam createTeam(String p_96527_1_) {
        if (p_96527_1_.length() > 16) {
            throw new IllegalArgumentException("The team name '" + p_96527_1_ + "' is too long!");
        }
        ScorePlayerTeam scoreplayerteam = this.getTeam(p_96527_1_);
        if (scoreplayerteam != null) {
            throw new IllegalArgumentException("A team with the name '" + p_96527_1_ + "' already exists!");
        }
        scoreplayerteam = new ScorePlayerTeam(this, p_96527_1_);
        this.teams.put(p_96527_1_, scoreplayerteam);
        this.broadcastTeamCreated(scoreplayerteam);
        return scoreplayerteam;
    }

    public void removeTeam(ScorePlayerTeam p_96511_1_) {
        this.teams.remove(p_96511_1_.getRegisteredName());
        Iterator<String> iterator = p_96511_1_.getMembershipCollection().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.func_96513_c(p_96511_1_);
                return;
            }
            String s = iterator.next();
            this.teamMemberships.remove(s);
        }
    }

    public boolean addPlayerToTeam(String player, String newTeam) {
        if (player.length() > 40) {
            throw new IllegalArgumentException("The player name '" + player + "' is too long!");
        }
        if (!this.teams.containsKey(newTeam)) {
            return false;
        }
        ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);
        if (this.getPlayersTeam(player) != null) {
            this.removePlayerFromTeams(player);
        }
        this.teamMemberships.put(player, scoreplayerteam);
        scoreplayerteam.getMembershipCollection().add(player);
        return true;
    }

    public boolean removePlayerFromTeams(String p_96524_1_) {
        ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(p_96524_1_);
        if (scoreplayerteam == null) return false;
        this.removePlayerFromTeam(p_96524_1_, scoreplayerteam);
        return true;
    }

    public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_) {
        if (this.getPlayersTeam(p_96512_1_) != p_96512_2_) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + p_96512_2_.getRegisteredName() + "'.");
        }
        this.teamMemberships.remove(p_96512_1_);
        p_96512_2_.getMembershipCollection().remove(p_96512_1_);
    }

    public Collection<String> getTeamNames() {
        return this.teams.keySet();
    }

    public Collection<ScorePlayerTeam> getTeams() {
        return this.teams.values();
    }

    public ScorePlayerTeam getPlayersTeam(String p_96509_1_) {
        return this.teamMemberships.get(p_96509_1_);
    }

    public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {
    }

    public void func_96532_b(ScoreObjective p_96532_1_) {
    }

    public void func_96533_c(ScoreObjective p_96533_1_) {
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
        if (p_96517_0_ < 3) return null;
        if (p_96517_0_ > 18) return null;
        EnumChatFormatting enumchatformatting = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);
        if (enumchatformatting == null) return null;
        if (enumchatformatting == EnumChatFormatting.RESET) return null;
        return "sidebar.team." + enumchatformatting.getFriendlyName();
    }

    public static int getObjectiveDisplaySlotNumber(String p_96537_0_) {
        if (p_96537_0_.equalsIgnoreCase("list")) {
            return 0;
        }
        if (p_96537_0_.equalsIgnoreCase("sidebar")) {
            return 1;
        }
        if (p_96537_0_.equalsIgnoreCase("belowName")) {
            return 2;
        }
        if (!p_96537_0_.startsWith("sidebar.team.")) return -1;
        String s = p_96537_0_.substring("sidebar.team.".length());
        EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s);
        if (enumchatformatting == null) return -1;
        if (enumchatformatting.getColorIndex() < 0) return -1;
        return enumchatformatting.getColorIndex() + 3;
    }

    public static String[] getDisplaySlotStrings() {
        if (field_178823_g != null) return field_178823_g;
        field_178823_g = new String[19];
        int i = 0;
        while (i < 19) {
            Scoreboard.field_178823_g[i] = Scoreboard.getObjectiveDisplaySlot(i);
            ++i;
        }
        return field_178823_g;
    }

    public void func_181140_a(Entity p_181140_1_) {
        if (p_181140_1_ == null) return;
        if (p_181140_1_ instanceof EntityPlayer) return;
        if (p_181140_1_.isEntityAlive()) return;
        String s = p_181140_1_.getUniqueID().toString();
        this.removeObjectiveFromEntity(s, null);
        this.removePlayerFromTeams(s);
    }
}

