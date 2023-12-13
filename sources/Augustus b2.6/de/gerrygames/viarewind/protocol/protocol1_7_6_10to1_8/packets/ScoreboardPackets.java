// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import com.viaversion.viaversion.util.ChatColorUtil;
import java.util.Optional;
import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Scoreboard;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;

public class ScoreboardPackets
{
    public static void register(final Protocol1_7_6_10TO1_8 protocol) {
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.SCOREBOARD_OBJECTIVE, new PacketRemapper() {
            @Override
            public void registerMap() {
                String name;
                Type<String> string;
                final int n;
                final T t;
                final byte mode;
                final Scoreboard scoreboard;
                String username;
                Optional<Byte> color;
                String sidebar;
                PacketWrapper sidebarPacket;
                String displayName;
                this.handler(packetWrapper -> {
                    name = packetWrapper.passthrough(Type.STRING);
                    if (name.length() > 16) {
                        string = Type.STRING;
                        name = name.substring(0, 16);
                        packetWrapper.set(string, n, (String)t);
                    }
                    mode = packetWrapper.read((Type<Byte>)Type.BYTE);
                    scoreboard = packetWrapper.user().get(Scoreboard.class);
                    if (mode == 0) {
                        if (scoreboard.objectiveExists(name)) {
                            packetWrapper.cancel();
                            return;
                        }
                        else {
                            scoreboard.addObjective(name);
                        }
                    }
                    else if (mode == 1) {
                        if (!scoreboard.objectiveExists(name)) {
                            packetWrapper.cancel();
                            return;
                        }
                        else {
                            if (scoreboard.getColorIndependentSidebar() != null) {
                                username = packetWrapper.user().getProtocolInfo().getUsername();
                                color = scoreboard.getPlayerTeamColor(username);
                                if (color.isPresent()) {
                                    sidebar = scoreboard.getColorDependentSidebar().get(color.get());
                                    if (name.equals(sidebar)) {
                                        sidebarPacket = PacketWrapper.create(61, null, packetWrapper.user());
                                        sidebarPacket.write(Type.BYTE, (Byte)1);
                                        sidebarPacket.write(Type.STRING, scoreboard.getColorIndependentSidebar());
                                        PacketUtil.sendPacket(sidebarPacket, Protocol1_7_6_10TO1_8.class);
                                    }
                                }
                            }
                            scoreboard.removeObjective(name);
                        }
                    }
                    else if (mode == 2 && !scoreboard.objectiveExists(name)) {
                        packetWrapper.cancel();
                        return;
                    }
                    if (mode == 0 || mode == 2) {
                        displayName = packetWrapper.passthrough(Type.STRING);
                        if (displayName.length() > 32) {
                            packetWrapper.set(Type.STRING, 1, displayName.substring(0, 32));
                        }
                        packetWrapper.read(Type.STRING);
                    }
                    else {
                        packetWrapper.write(Type.STRING, "");
                    }
                    packetWrapper.write(Type.BYTE, mode);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.UPDATE_SCORE, new PacketRemapper() {
            @Override
            public void registerMap() {
                final Scoreboard scoreboard;
                final String name;
                final byte mode;
                String name2;
                String objective;
                int score;
                this.handler(packetWrapper -> {
                    scoreboard = packetWrapper.user().get(Scoreboard.class);
                    name = packetWrapper.passthrough(Type.STRING);
                    mode = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                    if (mode == 1) {
                        name2 = scoreboard.removeTeamForScore(name);
                    }
                    else {
                        name2 = scoreboard.sendTeamForScore(name);
                    }
                    if (name2.length() > 16) {
                        name2 = ChatColorUtil.stripColor(name2);
                        if (name2.length() > 16) {
                            name2 = name2.substring(0, 16);
                        }
                    }
                    packetWrapper.set(Type.STRING, 0, name2);
                    objective = packetWrapper.read(Type.STRING);
                    if (objective.length() > 16) {
                        objective = objective.substring(0, 16);
                    }
                    if (mode != 1) {
                        score = packetWrapper.read((Type<Integer>)Type.VAR_INT);
                        packetWrapper.write(Type.STRING, objective);
                        packetWrapper.write(Type.INT, score);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.DISPLAY_SCOREBOARD, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE);
                this.map(Type.STRING);
                byte position;
                final String name;
                final Scoreboard scoreboard;
                byte receiverTeamColor;
                String username;
                Optional<Byte> color;
                String username2;
                Optional<Byte> color2;
                this.handler(packetWrapper -> {
                    position = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    name = packetWrapper.get(Type.STRING, 0);
                    scoreboard = packetWrapper.user().get(Scoreboard.class);
                    if (position > 2) {
                        receiverTeamColor = (byte)(position - 3);
                        scoreboard.getColorDependentSidebar().put(receiverTeamColor, name);
                        username = packetWrapper.user().getProtocolInfo().getUsername();
                        color = scoreboard.getPlayerTeamColor(username);
                        if (color.isPresent() && color.get() == receiverTeamColor) {
                            position = 1;
                        }
                        else {
                            position = -1;
                        }
                    }
                    else if (position == 1) {
                        scoreboard.setColorIndependentSidebar(name);
                        username2 = packetWrapper.user().getProtocolInfo().getUsername();
                        color2 = scoreboard.getPlayerTeamColor(username2);
                        if (color2.isPresent() && scoreboard.getColorDependentSidebar().containsKey(color2.get())) {
                            position = -1;
                        }
                    }
                    if (position == -1) {
                        packetWrapper.cancel();
                    }
                    else {
                        packetWrapper.set(Type.BYTE, 0, position);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_8, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_8.TEAMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                final String team;
                byte mode;
                Scoreboard scoreboard;
                PacketWrapper remove;
                byte color;
                String username;
                String sidebar;
                PacketWrapper sidebarPacket;
                byte color2;
                String[] entries;
                ArrayList<String> entryList;
                int i;
                String entry;
                String username2;
                PacketWrapper sidebarPacket2;
                PacketWrapper displayObjective;
                final Iterator<String> iterator;
                String entry2;
                this.handler(packetWrapper -> {
                    team = packetWrapper.get(Type.STRING, 0);
                    if (team == null) {
                        packetWrapper.cancel();
                    }
                    else {
                        mode = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                        scoreboard = packetWrapper.user().get(Scoreboard.class);
                        if (mode != 0 && !scoreboard.teamExists(team)) {
                            packetWrapper.cancel();
                        }
                        else {
                            if (mode == 0 && scoreboard.teamExists(team)) {
                                scoreboard.removeTeam(team);
                                remove = PacketWrapper.create(62, null, packetWrapper.user());
                                remove.write(Type.STRING, team);
                                remove.write(Type.BYTE, (Byte)1);
                                PacketUtil.sendPacket(remove, Protocol1_7_6_10TO1_8.class, true, true);
                            }
                            if (mode == 0) {
                                scoreboard.addTeam(team);
                            }
                            else if (mode == 1) {
                                scoreboard.removeTeam(team);
                            }
                            if (mode == 0 || mode == 2) {
                                packetWrapper.passthrough(Type.STRING);
                                packetWrapper.passthrough(Type.STRING);
                                packetWrapper.passthrough(Type.STRING);
                                packetWrapper.passthrough((Type<Object>)Type.BYTE);
                                packetWrapper.read(Type.STRING);
                                color = packetWrapper.read((Type<Byte>)Type.BYTE);
                                if (mode == 2 && scoreboard.getTeamColor(team).get() != color) {
                                    username = packetWrapper.user().getProtocolInfo().getUsername();
                                    sidebar = scoreboard.getColorDependentSidebar().get(color);
                                    sidebarPacket = packetWrapper.create(61);
                                    sidebarPacket.write(Type.BYTE, (Byte)1);
                                    sidebarPacket.write(Type.STRING, (sidebar == null) ? "" : sidebar);
                                    PacketUtil.sendPacket(sidebarPacket, Protocol1_7_6_10TO1_8.class);
                                }
                                scoreboard.setTeamColor(team, color);
                            }
                            if (mode == 0 || mode == 3 || mode == 4) {
                                color2 = scoreboard.getTeamColor(team).get();
                                entries = packetWrapper.read(Type.STRING_ARRAY);
                                entryList = new ArrayList<String>();
                                for (i = 0; i < entries.length; ++i) {
                                    entry = entries[i];
                                    username2 = packetWrapper.user().getProtocolInfo().getUsername();
                                    if (mode == 4) {
                                        if (!scoreboard.isPlayerInTeam(entry, team)) {
                                            continue;
                                        }
                                        else {
                                            scoreboard.removePlayerFromTeam(entry, team);
                                            if (entry.equals(username2)) {
                                                sidebarPacket2 = packetWrapper.create(61);
                                                sidebarPacket2.write(Type.BYTE, (Byte)1);
                                                sidebarPacket2.write(Type.STRING, (scoreboard.getColorIndependentSidebar() == null) ? "" : scoreboard.getColorIndependentSidebar());
                                                PacketUtil.sendPacket(sidebarPacket2, Protocol1_7_6_10TO1_8.class);
                                            }
                                        }
                                    }
                                    else {
                                        scoreboard.addPlayerToTeam(entry, team);
                                        if (entry.equals(username2) && scoreboard.getColorDependentSidebar().containsKey(color2)) {
                                            displayObjective = packetWrapper.create(61);
                                            displayObjective.write(Type.BYTE, (Byte)1);
                                            displayObjective.write(Type.STRING, scoreboard.getColorDependentSidebar().get(color2));
                                            PacketUtil.sendPacket(displayObjective, Protocol1_7_6_10TO1_8.class);
                                        }
                                    }
                                    entryList.add(entry);
                                }
                                packetWrapper.write(Type.SHORT, (short)entryList.size());
                                entryList.iterator();
                                while (iterator.hasNext()) {
                                    entry2 = iterator.next();
                                    packetWrapper.write(Type.STRING, entry2);
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}
