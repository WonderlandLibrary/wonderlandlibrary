// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;
import java.util.Collection;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class Scoreboard
{
    private final Map<String, ScoreObjective> scoreObjectives;
    private final Map<IScoreCriteria, List<ScoreObjective>> scoreObjectiveCriterias;
    private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives;
    private final ScoreObjective[] objectiveDisplaySlots;
    private final Map<String, ScorePlayerTeam> teams;
    private final Map<String, ScorePlayerTeam> teamMemberships;
    private static String[] displaySlots;
    
    public Scoreboard() {
        this.scoreObjectives = (Map<String, ScoreObjective>)Maps.newHashMap();
        this.scoreObjectiveCriterias = (Map<IScoreCriteria, List<ScoreObjective>>)Maps.newHashMap();
        this.entitiesScoreObjectives = (Map<String, Map<ScoreObjective, Score>>)Maps.newHashMap();
        this.objectiveDisplaySlots = new ScoreObjective[19];
        this.teams = (Map<String, ScorePlayerTeam>)Maps.newHashMap();
        this.teamMemberships = (Map<String, ScorePlayerTeam>)Maps.newHashMap();
    }
    
    @Nullable
    public ScoreObjective getObjective(final String name) {
        return this.scoreObjectives.get(name);
    }
    
    public ScoreObjective addScoreObjective(final String name, final IScoreCriteria criteria) {
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
            list = (List<ScoreObjective>)Lists.newArrayList();
            this.scoreObjectiveCriterias.put(criteria, list);
        }
        list.add(scoreobjective);
        this.scoreObjectives.put(name, scoreobjective);
        this.onScoreObjectiveAdded(scoreobjective);
        return scoreobjective;
    }
    
    public Collection<ScoreObjective> getObjectivesFromCriteria(final IScoreCriteria criteria) {
        final Collection<ScoreObjective> collection = this.scoreObjectiveCriterias.get(criteria);
        return (Collection<ScoreObjective>)((collection == null) ? Lists.newArrayList() : Lists.newArrayList((Iterable)collection));
    }
    
    public boolean entityHasObjective(final String name, final ScoreObjective objective) {
        final Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            return false;
        }
        final Score score = map.get(objective);
        return score != null;
    }
    
    public Score getOrCreateScore(final String username, final ScoreObjective objective) {
        if (username.length() > 40) {
            throw new IllegalArgumentException("The player name '" + username + "' is too long!");
        }
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(username);
        if (map == null) {
            map = (Map<ScoreObjective, Score>)Maps.newHashMap();
            this.entitiesScoreObjectives.put(username, map);
        }
        Score score = map.get(objective);
        if (score == null) {
            score = new Score(this, objective, username);
            map.put(objective, score);
        }
        return score;
    }
    
    public Collection<Score> getSortedScores(final ScoreObjective objective) {
        final List<Score> list = (List<Score>)Lists.newArrayList();
        for (final Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values()) {
            final Score score = map.get(objective);
            if (score != null) {
                list.add(score);
            }
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
    
    public void removeObjectiveFromEntity(final String name, final ScoreObjective objective) {
        if (objective == null) {
            final Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.remove(name);
            if (map != null) {
                this.broadcastScoreUpdate(name);
            }
        }
        else {
            final Map<ScoreObjective, Score> map2 = this.entitiesScoreObjectives.get(name);
            if (map2 != null) {
                final Score score = map2.remove(objective);
                if (map2.size() < 1) {
                    final Map<ScoreObjective, Score> map3 = this.entitiesScoreObjectives.remove(name);
                    if (map3 != null) {
                        this.broadcastScoreUpdate(name);
                    }
                }
                else if (score != null) {
                    this.broadcastScoreUpdate(name, objective);
                }
            }
        }
    }
    
    public Collection<Score> getScores() {
        final Collection<Map<ScoreObjective, Score>> collection = this.entitiesScoreObjectives.values();
        final List<Score> list = (List<Score>)Lists.newArrayList();
        for (final Map<ScoreObjective, Score> map : collection) {
            list.addAll(map.values());
        }
        return list;
    }
    
    public Map<ScoreObjective, Score> getObjectivesForEntity(final String name) {
        Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
        if (map == null) {
            map = (Map<ScoreObjective, Score>)Maps.newHashMap();
        }
        return map;
    }
    
    public void removeObjective(final ScoreObjective objective) {
        this.scoreObjectives.remove(objective.getName());
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) == objective) {
                this.setObjectiveInDisplaySlot(i, null);
            }
        }
        final List<ScoreObjective> list = this.scoreObjectiveCriterias.get(objective.getCriteria());
        if (list != null) {
            list.remove(objective);
        }
        for (final Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values()) {
            map.remove(objective);
        }
        this.onScoreObjectiveRemoved(objective);
    }
    
    public void setObjectiveInDisplaySlot(final int objectiveSlot, final ScoreObjective objective) {
        this.objectiveDisplaySlots[objectiveSlot] = objective;
    }
    
    @Nullable
    public ScoreObjective getObjectiveInDisplaySlot(final int slotIn) {
        return this.objectiveDisplaySlots[slotIn];
    }
    
    public ScorePlayerTeam getTeam(final String teamName) {
        return this.teams.get(teamName);
    }
    
    public ScorePlayerTeam createTeam(final String name) {
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
    
    public void removeTeam(final ScorePlayerTeam playerTeam) {
        this.teams.remove(playerTeam.getName());
        for (final String s : playerTeam.getMembershipCollection()) {
            this.teamMemberships.remove(s);
        }
        this.broadcastTeamRemove(playerTeam);
    }
    
    public boolean addPlayerToTeam(final String player, final String newTeam) {
        if (player.length() > 40) {
            throw new IllegalArgumentException("The player name '" + player + "' is too long!");
        }
        if (!this.teams.containsKey(newTeam)) {
            return false;
        }
        final ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);
        if (this.getPlayersTeam(player) != null) {
            this.removePlayerFromTeams(player);
        }
        this.teamMemberships.put(player, scoreplayerteam);
        scoreplayerteam.getMembershipCollection().add(player);
        return true;
    }
    
    public boolean removePlayerFromTeams(final String playerName) {
        final ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(playerName);
        if (scoreplayerteam != null) {
            this.removePlayerFromTeam(playerName, scoreplayerteam);
            return true;
        }
        return false;
    }
    
    public void removePlayerFromTeam(final String username, final ScorePlayerTeam playerTeam) {
        if (this.getPlayersTeam(username) == playerTeam) {
            this.teamMemberships.remove(username);
            playerTeam.getMembershipCollection().remove(username);
        }
    }
    
    public Collection<String> getTeamNames() {
        return this.teams.keySet();
    }
    
    public Collection<ScorePlayerTeam> getTeams() {
        return this.teams.values();
    }
    
    @Nullable
    public ScorePlayerTeam getPlayersTeam(final String username) {
        return this.teamMemberships.get(username);
    }
    
    public void onScoreObjectiveAdded(final ScoreObjective scoreObjectiveIn) {
    }
    
    public void onObjectiveDisplayNameChanged(final ScoreObjective objective) {
    }
    
    public void onScoreObjectiveRemoved(final ScoreObjective objective) {
    }
    
    public void onScoreUpdated(final Score scoreIn) {
    }
    
    public void broadcastScoreUpdate(final String scoreName) {
    }
    
    public void broadcastScoreUpdate(final String scoreName, final ScoreObjective objective) {
    }
    
    public void broadcastTeamCreated(final ScorePlayerTeam playerTeam) {
    }
    
    public void broadcastTeamInfoUpdate(final ScorePlayerTeam playerTeam) {
    }
    
    public void broadcastTeamRemove(final ScorePlayerTeam playerTeam) {
    }
    
    public static String getObjectiveDisplaySlot(final int id) {
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
            default: {
                if (id >= 3 && id <= 18) {
                    final TextFormatting textformatting = TextFormatting.fromColorIndex(id - 3);
                    if (textformatting != null && textformatting != TextFormatting.RESET) {
                        return "sidebar.team." + textformatting.getFriendlyName();
                    }
                }
                return null;
            }
        }
    }
    
    public static int getObjectiveDisplaySlotNumber(final String name) {
        if ("list".equalsIgnoreCase(name)) {
            return 0;
        }
        if ("sidebar".equalsIgnoreCase(name)) {
            return 1;
        }
        if ("belowName".equalsIgnoreCase(name)) {
            return 2;
        }
        if (name.startsWith("sidebar.team.")) {
            final String s = name.substring("sidebar.team.".length());
            final TextFormatting textformatting = TextFormatting.getValueByName(s);
            if (textformatting != null && textformatting.getColorIndex() >= 0) {
                return textformatting.getColorIndex() + 3;
            }
        }
        return -1;
    }
    
    public static String[] getDisplaySlotStrings() {
        if (Scoreboard.displaySlots == null) {
            Scoreboard.displaySlots = new String[19];
            for (int i = 0; i < 19; ++i) {
                Scoreboard.displaySlots[i] = getObjectiveDisplaySlot(i);
            }
        }
        return Scoreboard.displaySlots;
    }
    
    public void removeEntity(final Entity entityIn) {
        if (entityIn != null && !(entityIn instanceof EntityPlayer) && !entityIn.isEntityAlive()) {
            final String s = entityIn.getCachedUniqueIdString();
            this.removeObjectiveFromEntity(s, null);
            this.removePlayerFromTeams(s);
        }
    }
}
