package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Scoreboard;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;

public class ScoreboardPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 59, 59, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String name = (String)packetWrapper.passthrough(Type.STRING);
                    if (name.length() > 16)
                      packetWrapper.set(Type.STRING, 0, name = name.substring(0, 16)); 
                    byte mode = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                    Scoreboard scoreboard = (Scoreboard)packetWrapper.user().get(Scoreboard.class);
                    if (mode == 0) {
                      if (scoreboard.objectiveExists(name)) {
                        packetWrapper.cancel();
                        return;
                      } 
                      scoreboard.addObjective(name);
                    } else if (mode == 1) {
                      if (!scoreboard.objectiveExists(name)) {
                        packetWrapper.cancel();
                        return;
                      } 
                      if (scoreboard.getColorIndependentSidebar() != null) {
                        String username = ((ProtocolInfo)packetWrapper.user().get(ProtocolInfo.class)).getUsername();
                        Optional<Byte> color = scoreboard.getPlayerTeamColor(username);
                        if (color.isPresent()) {
                          String sidebar = (String)scoreboard.getColorDependentSidebar().get(color.get());
                          if (name.equals(sidebar)) {
                            PacketWrapper sidebarPacket = new PacketWrapper(61, null, packetWrapper.user());
                            sidebarPacket.write(Type.BYTE, Byte.valueOf((byte)1));
                            sidebarPacket.write(Type.STRING, scoreboard.getColorIndependentSidebar());
                            PacketUtil.sendPacket(sidebarPacket, Protocol1_7_6_10TO1_8.class);
                          } 
                        } 
                      } 
                      scoreboard.removeObjective(name);
                    } else if (mode == 2 && 
                      !scoreboard.objectiveExists(name)) {
                      packetWrapper.cancel();
                      return;
                    } 
                    if (mode == 0 || mode == 2) {
                      String displayName = (String)packetWrapper.passthrough(Type.STRING);
                      if (displayName.length() > 32)
                        packetWrapper.set(Type.STRING, 1, displayName.substring(0, 32)); 
                      packetWrapper.read(Type.STRING);
                    } else {
                      packetWrapper.write(Type.STRING, "");
                    } 
                    packetWrapper.write(Type.BYTE, Byte.valueOf(mode));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 60, 60, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Scoreboard scoreboard = (Scoreboard)packetWrapper.user().get(Scoreboard.class);
                    String name = (String)packetWrapper.passthrough(Type.STRING);
                    byte mode = ((Byte)packetWrapper.passthrough(Type.BYTE)).byteValue();
                    if (mode == 1) {
                      name = scoreboard.removeTeamForScore(name);
                    } else {
                      name = scoreboard.sendTeamForScore(name);
                    } 
                    if (name.length() > 16) {
                      name = ChatColor.stripColor(name);
                      if (name.length() > 16)
                        name = name.substring(0, 16); 
                    } 
                    packetWrapper.set(Type.STRING, 0, name);
                    String objective = (String)packetWrapper.read(Type.STRING);
                    if (objective.length() > 16)
                      objective = objective.substring(0, 16); 
                    if (mode != 1) {
                      int score = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                      packetWrapper.write(Type.STRING, objective);
                      packetWrapper.write(Type.INT, Integer.valueOf(score));
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 61, 61, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE);
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    byte position = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    String name = (String)packetWrapper.get(Type.STRING, 0);
                    Scoreboard scoreboard = (Scoreboard)packetWrapper.user().get(Scoreboard.class);
                    if (position > 2) {
                      byte receiverTeamColor = (byte)(position - 3);
                      scoreboard.getColorDependentSidebar().put(Byte.valueOf(receiverTeamColor), name);
                      String username = ((ProtocolInfo)packetWrapper.user().get(ProtocolInfo.class)).getUsername();
                      Optional<Byte> color = scoreboard.getPlayerTeamColor(username);
                      if (color.isPresent() && ((Byte)color.get()).byteValue() == receiverTeamColor) {
                        position = 1;
                      } else {
                        position = -1;
                      } 
                    } else if (position == 1) {
                      scoreboard.setColorIndependentSidebar(name);
                      String username = ((ProtocolInfo)packetWrapper.user().get(ProtocolInfo.class)).getUsername();
                      Optional<Byte> color = scoreboard.getPlayerTeamColor(username);
                      if (color.isPresent() && scoreboard.getColorDependentSidebar().containsKey(color.get()))
                        position = -1; 
                    } 
                    if (position == -1) {
                      packetWrapper.cancel();
                      return;
                    } 
                    packetWrapper.set(Type.BYTE, 0, Byte.valueOf(position));
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 62, 62, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String team = (String)packetWrapper.get(Type.STRING, 0);
                    if (team == null) {
                      packetWrapper.cancel();
                      return;
                    } 
                    byte mode = ((Byte)packetWrapper.passthrough(Type.BYTE)).byteValue();
                    Scoreboard scoreboard = (Scoreboard)packetWrapper.user().get(Scoreboard.class);
                    if (mode != 0 && !scoreboard.teamExists(team)) {
                      packetWrapper.cancel();
                      return;
                    } 
                    if (mode == 0 && scoreboard.teamExists(team)) {
                      scoreboard.removeTeam(team);
                      PacketWrapper remove = new PacketWrapper(62, null, packetWrapper.user());
                      remove.write(Type.STRING, team);
                      remove.write(Type.BYTE, Byte.valueOf((byte)1));
                      PacketUtil.sendPacket(remove, Protocol1_7_6_10TO1_8.class, true, true);
                    } 
                    if (mode == 0) {
                      scoreboard.addTeam(team);
                    } else if (mode == 1) {
                      scoreboard.removeTeam(team);
                    } 
                    if (mode == 0 || mode == 2) {
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.STRING);
                      packetWrapper.passthrough(Type.BYTE);
                      packetWrapper.read(Type.STRING);
                      byte color = ((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                      if (mode == 2 && ((Byte)scoreboard.getTeamColor(team).get()).byteValue() != color) {
                        String username = ((ProtocolInfo)packetWrapper.user().get(ProtocolInfo.class)).getUsername();
                        String sidebar = (String)scoreboard.getColorDependentSidebar().get(Byte.valueOf(color));
                        PacketWrapper sidebarPacket = packetWrapper.create(61);
                        sidebarPacket.write(Type.BYTE, Byte.valueOf((byte)1));
                        sidebarPacket.write(Type.STRING, (sidebar == null) ? "" : sidebar);
                        PacketUtil.sendPacket(sidebarPacket, Protocol1_7_6_10TO1_8.class);
                      } 
                      scoreboard.setTeamColor(team, Byte.valueOf(color));
                    } 
                    if (mode == 0 || mode == 3 || mode == 4) {
                      byte color = ((Byte)scoreboard.getTeamColor(team).get()).byteValue();
                      String[] entries = (String[])packetWrapper.read(Type.STRING_ARRAY);
                      List<String> entryList = new ArrayList<>();
                      for (int i = 0; i < entries.length; i++) {
                        String entry = entries[i];
                        String username = ((ProtocolInfo)packetWrapper.user().get(ProtocolInfo.class)).getUsername();
                        if (mode == 4) {
                          if (!scoreboard.isPlayerInTeam(entry, team))
                            continue; 
                          scoreboard.removePlayerFromTeam(entry, team);
                          if (entry.equals(username)) {
                            PacketWrapper sidebarPacket = packetWrapper.create(61);
                            sidebarPacket.write(Type.BYTE, Byte.valueOf((byte)1));
                            sidebarPacket.write(Type.STRING, (scoreboard.getColorIndependentSidebar() == null) ? "" : scoreboard.getColorIndependentSidebar());
                            PacketUtil.sendPacket(sidebarPacket, Protocol1_7_6_10TO1_8.class);
                          } 
                        } else {
                          scoreboard.addPlayerToTeam(entry, team);
                          if (entry.equals(username) && scoreboard.getColorDependentSidebar().containsKey(Byte.valueOf(color))) {
                            PacketWrapper displayObjective = packetWrapper.create(61);
                            displayObjective.write(Type.BYTE, Byte.valueOf((byte)1));
                            displayObjective.write(Type.STRING, scoreboard.getColorDependentSidebar().get(Byte.valueOf(color)));
                            PacketUtil.sendPacket(displayObjective, Protocol1_7_6_10TO1_8.class);
                          } 
                        } 
                        entryList.add(entry);
                        continue;
                      } 
                      packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)entryList.size()));
                      for (String entry : entryList)
                        packetWrapper.write(Type.STRING, entry); 
                    } 
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\packets\ScoreboardPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */