package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.type.Type;

public class Scoreboard extends StoredObject {
  private HashMap<String, List<String>> teams = new HashMap<>();
  
  private HashSet<String> objectives = new HashSet<>();
  
  private HashMap<String, ScoreTeam> scoreTeams = new HashMap<>();
  
  private HashMap<String, Byte> teamColors = new HashMap<>();
  
  private HashSet<String> scoreTeamNames = new HashSet<>();
  
  private String colorIndependentSidebar;
  
  public String getColorIndependentSidebar() {
    return this.colorIndependentSidebar;
  }
  
  public void setColorIndependentSidebar(String colorIndependentSidebar) {
    this.colorIndependentSidebar = colorIndependentSidebar;
  }
  
  private HashMap<Byte, String> colorDependentSidebar = new HashMap<>();
  
  public HashMap<Byte, String> getColorDependentSidebar() {
    return this.colorDependentSidebar;
  }
  
  public Scoreboard(UserConnection user) {
    super(user);
  }
  
  public void addPlayerToTeam(String player, String team) {
    ((List<String>)this.teams.computeIfAbsent(team, key -> new ArrayList())).add(player);
  }
  
  public void setTeamColor(String team, Byte color) {
    this.teamColors.put(team, color);
  }
  
  public Optional<Byte> getTeamColor(String team) {
    return Optional.ofNullable(this.teamColors.get(team));
  }
  
  public void addTeam(String team) {
    this.teams.computeIfAbsent(team, key -> new ArrayList());
  }
  
  public void removeTeam(String team) {
    this.teams.remove(team);
    this.scoreTeams.remove(team);
    this.teamColors.remove(team);
  }
  
  public boolean teamExists(String team) {
    return this.teams.containsKey(team);
  }
  
  public void removePlayerFromTeam(String player, String team) {
    List<String> teamPlayers = this.teams.get(team);
    if (teamPlayers != null)
      teamPlayers.remove(player); 
  }
  
  public boolean isPlayerInTeam(String player, String team) {
    List<String> teamPlayers = this.teams.get(team);
    return (teamPlayers != null && teamPlayers.contains(player));
  }
  
  public boolean isPlayerInTeam(String player) {
    for (List<String> teamPlayers : this.teams.values()) {
      if (teamPlayers.contains(player))
        return true; 
    } 
    return false;
  }
  
  public Optional<Byte> getPlayerTeamColor(String player) {
    Optional<String> team = getTeam(player);
    return team.isPresent() ? getTeamColor(team.get()) : Optional.<Byte>empty();
  }
  
  public Optional<String> getTeam(String player) {
    for (Map.Entry<String, List<String>> entry : this.teams.entrySet()) {
      if (((List)entry.getValue()).contains(player))
        return Optional.of(entry.getKey()); 
    } 
    return Optional.empty();
  }
  
  public void addObjective(String name) {
    this.objectives.add(name);
  }
  
  public void removeObjective(String name) {
    this.objectives.remove(name);
    this.colorDependentSidebar.values().remove(name);
    if (name.equals(this.colorIndependentSidebar))
      this.colorIndependentSidebar = null; 
  }
  
  public boolean objectiveExists(String name) {
    return this.objectives.contains(name);
  }
  
  public String sendTeamForScore(String score) {
    if (score.length() <= 16)
      return score; 
    if (this.scoreTeams.containsKey(score))
      return (this.scoreTeams.get(score)).name; 
    int l = 16;
    int i = Math.min(16, score.length() - 16);
    String name = score.substring(i, i + l);
    while (this.scoreTeamNames.contains(name) || this.teams.containsKey(name)) {
      i--;
      while (score.length() - l - i > 16) {
        l--;
        if (l < 1)
          return score; 
        i = Math.min(16, score.length() - l);
      } 
      name = score.substring(i, i + l);
    } 
    String prefix = score.substring(0, i);
    String suffix = (i + l >= score.length()) ? "" : score.substring(i + l, score.length());
    ScoreTeam scoreTeam = new ScoreTeam(name, prefix, suffix);
    this.scoreTeams.put(score, scoreTeam);
    this.scoreTeamNames.add(name);
    PacketWrapper teamPacket = new PacketWrapper(62, null, getUser());
    teamPacket.write(Type.STRING, name);
    teamPacket.write(Type.BYTE, Byte.valueOf((byte)0));
    teamPacket.write(Type.STRING, "ViaRewind");
    teamPacket.write(Type.STRING, prefix);
    teamPacket.write(Type.STRING, suffix);
    teamPacket.write(Type.BYTE, Byte.valueOf((byte)0));
    teamPacket.write((Type)Type.SHORT, Short.valueOf((short)1));
    teamPacket.write(Type.STRING, name);
    PacketUtil.sendPacket(teamPacket, Protocol1_7_6_10TO1_8.class, true, true);
    return name;
  }
  
  public String removeTeamForScore(String score) {
    ScoreTeam scoreTeam = this.scoreTeams.remove(score);
    if (scoreTeam == null)
      return score; 
    this.scoreTeamNames.remove(scoreTeam.name);
    PacketWrapper teamPacket = new PacketWrapper(62, null, getUser());
    teamPacket.write(Type.STRING, scoreTeam.name);
    teamPacket.write(Type.BYTE, Byte.valueOf((byte)1));
    PacketUtil.sendPacket(teamPacket, Protocol1_7_6_10TO1_8.class, true, true);
    return scoreTeam.name;
  }
  
  private class ScoreTeam {
    private String prefix;
    
    private String suffix;
    
    private String name;
    
    public ScoreTeam(String name, String prefix, String suffix) {
      this.prefix = prefix;
      this.suffix = suffix;
      this.name = name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\storage\Scoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */