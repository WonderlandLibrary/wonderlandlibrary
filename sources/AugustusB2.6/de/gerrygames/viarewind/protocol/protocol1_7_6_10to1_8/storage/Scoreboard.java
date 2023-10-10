// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Map;
import java.util.Iterator;
import java.util.Optional;
import java.util.ArrayList;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import com.viaversion.viaversion.api.connection.StoredObject;

public class Scoreboard extends StoredObject
{
    private HashMap<String, List<String>> teams;
    private HashSet<String> objectives;
    private HashMap<String, ScoreTeam> scoreTeams;
    private HashMap<String, Byte> teamColors;
    private HashSet<String> scoreTeamNames;
    private String colorIndependentSidebar;
    private HashMap<Byte, String> colorDependentSidebar;
    
    public Scoreboard(final UserConnection user) {
        super(user);
        this.teams = new HashMap<String, List<String>>();
        this.objectives = new HashSet<String>();
        this.scoreTeams = new HashMap<String, ScoreTeam>();
        this.teamColors = new HashMap<String, Byte>();
        this.scoreTeamNames = new HashSet<String>();
        this.colorDependentSidebar = new HashMap<Byte, String>();
    }
    
    public void addPlayerToTeam(final String player, final String team) {
        this.teams.computeIfAbsent(team, key -> new ArrayList()).add(player);
    }
    
    public void setTeamColor(final String team, final Byte color) {
        this.teamColors.put(team, color);
    }
    
    public Optional<Byte> getTeamColor(final String team) {
        return Optional.ofNullable(this.teamColors.get(team));
    }
    
    public void addTeam(final String team) {
        this.teams.computeIfAbsent(team, key -> new ArrayList());
    }
    
    public void removeTeam(final String team) {
        this.teams.remove(team);
        this.scoreTeams.remove(team);
        this.teamColors.remove(team);
    }
    
    public boolean teamExists(final String team) {
        return this.teams.containsKey(team);
    }
    
    public void removePlayerFromTeam(final String player, final String team) {
        final List<String> teamPlayers = this.teams.get(team);
        if (teamPlayers != null) {
            teamPlayers.remove(player);
        }
    }
    
    public boolean isPlayerInTeam(final String player, final String team) {
        final List<String> teamPlayers = this.teams.get(team);
        return teamPlayers != null && teamPlayers.contains(player);
    }
    
    public boolean isPlayerInTeam(final String player) {
        for (final List<String> teamPlayers : this.teams.values()) {
            if (teamPlayers.contains(player)) {
                return true;
            }
        }
        return false;
    }
    
    public Optional<Byte> getPlayerTeamColor(final String player) {
        final Optional<String> team = this.getTeam(player);
        return team.isPresent() ? this.getTeamColor(team.get()) : Optional.empty();
    }
    
    public Optional<String> getTeam(final String player) {
        for (final Map.Entry<String, List<String>> entry : this.teams.entrySet()) {
            if (entry.getValue().contains(player)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
    
    public void addObjective(final String name) {
        this.objectives.add(name);
    }
    
    public void removeObjective(final String name) {
        this.objectives.remove(name);
        this.colorDependentSidebar.values().remove(name);
        if (name.equals(this.colorIndependentSidebar)) {
            this.colorIndependentSidebar = null;
        }
    }
    
    public boolean objectiveExists(final String name) {
        return this.objectives.contains(name);
    }
    
    public String sendTeamForScore(final String score) {
        if (score.length() <= 16) {
            return score;
        }
        if (this.scoreTeams.containsKey(score)) {
            return this.scoreTeams.get(score).name;
        }
        int l;
        int i;
        String name;
        for (l = 16, i = Math.min(16, score.length() - 16), name = score.substring(i, i + l); this.scoreTeamNames.contains(name) || this.teams.containsKey(name); name = score.substring(i, i + l)) {
            --i;
            while (score.length() - l - i > 16) {
                if (--l < 1) {
                    return score;
                }
                i = Math.min(16, score.length() - l);
            }
        }
        final String prefix = score.substring(0, i);
        final String suffix = (i + l >= score.length()) ? "" : score.substring(i + l, score.length());
        final ScoreTeam scoreTeam = new ScoreTeam(name, prefix, suffix);
        this.scoreTeams.put(score, scoreTeam);
        this.scoreTeamNames.add(name);
        final PacketWrapper teamPacket = PacketWrapper.create(62, null, this.getUser());
        teamPacket.write(Type.STRING, name);
        teamPacket.write(Type.BYTE, (Byte)0);
        teamPacket.write(Type.STRING, "ViaRewind");
        teamPacket.write(Type.STRING, prefix);
        teamPacket.write(Type.STRING, suffix);
        teamPacket.write(Type.BYTE, (Byte)0);
        teamPacket.write(Type.SHORT, (Short)1);
        teamPacket.write(Type.STRING, name);
        PacketUtil.sendPacket(teamPacket, Protocol1_7_6_10TO1_8.class, true, true);
        return name;
    }
    
    public String removeTeamForScore(final String score) {
        final ScoreTeam scoreTeam = this.scoreTeams.remove(score);
        if (scoreTeam == null) {
            return score;
        }
        this.scoreTeamNames.remove(scoreTeam.name);
        final PacketWrapper teamPacket = PacketWrapper.create(62, null, this.getUser());
        teamPacket.write(Type.STRING, scoreTeam.name);
        teamPacket.write(Type.BYTE, (Byte)1);
        PacketUtil.sendPacket(teamPacket, Protocol1_7_6_10TO1_8.class, true, true);
        return scoreTeam.name;
    }
    
    public String getColorIndependentSidebar() {
        return this.colorIndependentSidebar;
    }
    
    public HashMap<Byte, String> getColorDependentSidebar() {
        return this.colorDependentSidebar;
    }
    
    public void setColorIndependentSidebar(final String colorIndependentSidebar) {
        this.colorIndependentSidebar = colorIndependentSidebar;
    }
    
    private class ScoreTeam
    {
        private String prefix;
        private String suffix;
        private String name;
        
        public ScoreTeam(final String name, final String prefix, final String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
            this.name = name;
        }
    }
}
