/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.TextFormatting;

public class Scoreboard {
    private final Map<String, ScoreObjective> scoreObjectives = Maps.newHashMap();
    private final Map<IScoreCriteria, List<ScoreObjective>> scoreObjectiveCriterias = Maps.newHashMap();
    private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives = Maps.newHashMap();
    private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
    private final Map<String, ScorePlayerTeam> teams = Maps.newHashMap();
    private final Map<String, ScorePlayerTeam> teamMemberships = Maps.newHashMap();
    private static String[] displaySlots;

    @Nullable
    public ScoreObjective getObjective(String name) {
        return this.scoreObjectives.get(name);
    }

    public ScoreObjective addScoreObjective(String name, IScoreCriteria criteria) {
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

    public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreCriteria criteria) {
        Collection collection = this.scoreObjectiveCriterias.get(criteria);
        return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
    }

    public boolean entityHasObjective(String name, ScoreObjective objective) {
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            return false;
        }
        Score score = map.get(objective);
        return score != null;
    }

    public Score getOrCreateScore(String username, ScoreObjective objective) {
        Score score;
        if (username.length() > 40) {
            throw new IllegalArgumentException("The player name '" + username + "' is too long!");
        }
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(username);
        if (map == null) {
            map = Maps.newHashMap();
            this.entitiesScoreObjectives.put(username, map);
        }
        if ((score = map.get(objective)) == null) {
            score = new Score(this, objective, username);
            map.put(objective, score);
        }
        return score;
    }

    public Collection<Score> getSortedScores(ScoreObjective objective) {
        ArrayList<Score> list = Lists.newArrayList();
        for (Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values()) {
            Score score = map.get(objective);
            if (score == null) continue;
            list.add(score);
        }
        Collections.sort(list, Score.SCORE_COMPARATOR);
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
            Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.remove(name);
            if (map != null) {
                this.broadcastScoreUpdate(name);
            }
        } else {
            Map<ScoreObjective, Score> map2 = this.entitiesScoreObjectives.get(name);
            if (map2 != null) {
                Score score = map2.remove(objective);
                if (map2.size() < 1) {
                    Map<ScoreObjective, Score> map1 = this.entitiesScoreObjectives.remove(name);
                    if (map1 != null) {
                        this.broadcastScoreUpdate(name);
                    }
                } else if (score != null) {
                    this.broadcastScoreUpdate(name, objective);
                }
            }
        }
    }

    public Collection<Score> getScores() {
        Collection<Map<ScoreObjective, Score>> collection = this.entitiesScoreObjectives.values();
        ArrayList<Score> list = Lists.newArrayList();
        for (Map<ScoreObjective, Score> map : collection) {
            list.addAll(map.values());
        }
        return list;
    }

    public Map<ScoreObjective, Score> getObjectivesForEntity(String name) {
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            map = Maps.newHashMap();
        }
        return map;
    }

    public void removeObjective(ScoreObjective objective) {
        this.scoreObjectives.remove(objective.getName());
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) != objective) continue;
            this.setObjectiveInDisplaySlot(i, null);
        }
        List<ScoreObjective> list = this.scoreObjectiveCriterias.get(objective.getCriteria());
        if (list != null) {
            list.remove(objective);
        }
        for (Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values()) {
            map.remove(objective);
        }
        this.onScoreObjectiveRemoved(objective);
    }

    public void setObjectiveInDisplaySlot(int objectiveSlot, ScoreObjective objective) {
        this.objectiveDisplaySlots[objectiveSlot] = objective;
    }

    @Nullable
    public ScoreObjective getObjectiveInDisplaySlot(int slotIn) {
        return this.objectiveDisplaySlots[slotIn];
    }

    public ScorePlayerTeam getTeam(String teamName) {
        return this.teams.get(teamName);
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
        this.teams.put(name, scoreplayerteam);
        this.broadcastTeamCreated(scoreplayerteam);
        return scoreplayerteam;
    }

    public void removeTeam(ScorePlayerTeam playerTeam) {
        this.teams.remove(playerTeam.getRegisteredName());
        for (String s : playerTeam.getMembershipCollection()) {
            this.teamMemberships.remove(s);
        }
        this.broadcastTeamRemove(playerTeam);
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

    public boolean removePlayerFromTeams(String playerName) {
        ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(playerName);
        if (scoreplayerteam != null) {
            this.removePlayerFromTeam(playerName, scoreplayerteam);
            return true;
        }
        return false;
    }

    public void removePlayerFromTeam(String username, ScorePlayerTeam playerTeam) {
        if (this.getPlayersTeam(username) != playerTeam) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + playerTeam.getRegisteredName() + "'.");
        }
        this.teamMemberships.remove(username);
        playerTeam.getMembershipCollection().remove(username);
    }

    public Collection<String> getTeamNames() {
        return this.teams.keySet();
    }

    public Collection<ScorePlayerTeam> getTeams() {
        return this.teams.values();
    }

    @Nullable
    public ScorePlayerTeam getPlayersTeam(String username) {
        return this.teamMemberships.get(username);
    }

    public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {
    }

    public void onObjectiveDisplayNameChanged(ScoreObjective objective) {
    }

    public void onScoreObjectiveRemoved(ScoreObjective objective) {
    }

    public void onScoreUpdated(Score scoreIn) {
    }

    public void broadcastScoreUpdate(String scoreName) {
    }

    public void broadcastScoreUpdate(String scoreName, ScoreObjective objective) {
    }

    public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {
    }

    public void broadcastTeamInfoUpdate(ScorePlayerTeam playerTeam) {
    }

    public void broadcastTeamRemove(ScorePlayerTeam playerTeam) {
    }

    public static String getObjectiveDisplaySlot(int id) {
        TextFormatting textformatting;
        switch (id) {
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
        if (id >= 3 && id <= 18 && (textformatting = TextFormatting.fromColorIndex(id - 3)) != null && textformatting != TextFormatting.RESET) {
            return "sidebar.team." + textformatting.getFriendlyName();
        }
        return null;
    }

    public static int getObjectiveDisplaySlotNumber(String name) {
        String s;
        TextFormatting textformatting;
        if ("list".equalsIgnoreCase(name)) {
            return 0;
        }
        if ("sidebar".equalsIgnoreCase(name)) {
            return 1;
        }
        if ("belowName".equalsIgnoreCase(name)) {
            return 2;
        }
        if (name.startsWith("sidebar.team.") && (textformatting = TextFormatting.getValueByName(s = name.substring("sidebar.team.".length()))) != null && textformatting.getColorIndex() >= 0) {
            return textformatting.getColorIndex() + 3;
        }
        return -1;
    }

    public static String[] getDisplaySlotStrings() {
        if (displaySlots == null) {
            displaySlots = new String[19];
            for (int i = 0; i < 19; ++i) {
                Scoreboard.displaySlots[i] = Scoreboard.getObjectiveDisplaySlot(i);
            }
        }
        return displaySlots;
    }

    public void removeEntity(Entity entityIn) {
        if (entityIn != null && !(entityIn instanceof EntityPlayer) && !entityIn.isEntityAlive()) {
            String s = entityIn.getCachedUniqueIdString();
            this.removeObjectiveFromEntity(s, null);
            this.removePlayerFromTeams(s);
        }
    }
}

