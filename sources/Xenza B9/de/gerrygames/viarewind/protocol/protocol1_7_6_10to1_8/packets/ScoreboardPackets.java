// 
// Decompiled by Procyon v0.6.0
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
                this.handler(packetWrapper -> {
                    String name = packetWrapper.passthrough(Type.STRING);
                    if (name.length() > 16) {
                        final Type<String> string = Type.STRING;
                        name = name.substring(0, 16);
                        final int n;
                        final T t;
                        packetWrapper.set(string, n, (String)t);
                    }
                    final byte mode = packetWrapper.read((Type<Byte>)Type.BYTE);
                    final Scoreboard scoreboard = packetWrapper.user().get(Scoreboard.class);
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
                                final String username = packetWrapper.user().getProtocolInfo().getUsername();
                                final Optional<Byte> color = scoreboard.getPlayerTeamColor(username);
                                if (color.isPresent()) {
                                    final String sidebar = scoreboard.getColorDependentSidebar().get(color.get());
                                    if (name.equals(sidebar)) {
                                        final PacketWrapper sidebarPacket = PacketWrapper.create(61, null, packetWrapper.user());
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
                        final String displayName = packetWrapper.passthrough(Type.STRING);
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
                this.map(Type.STRING);
                this.map(Type.VAR_INT, Type.BYTE);
                this.handler(packetWrapper -> {
                    final Scoreboard scoreboard = packetWrapper.user().get(Scoreboard.class);
                    final String name = packetWrapper.get(Type.STRING, 0);
                    final byte mode = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    String name2;
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
                    String objective = packetWrapper.read(Type.STRING);
                    if (objective.length() > 16) {
                        objective = objective.substring(0, 16);
                    }
                    if (mode != 1) {
                        final int score = packetWrapper.read((Type<Integer>)Type.VAR_INT);
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
                this.handler(packetWrapper -> {
                    byte position = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    final String name = packetWrapper.get(Type.STRING, 0);
                    final Scoreboard scoreboard = packetWrapper.user().get(Scoreboard.class);
                    if (position > 2) {
                        final byte receiverTeamColor = (byte)(position - 3);
                        scoreboard.getColorDependentSidebar().put(receiverTeamColor, name);
                        final String username = packetWrapper.user().getProtocolInfo().getUsername();
                        final Optional<Byte> color = scoreboard.getPlayerTeamColor(username);
                        if (color.isPresent() && color.get() == receiverTeamColor) {
                            position = 1;
                        }
                        else {
                            position = -1;
                        }
                    }
                    else if (position == 1) {
                        scoreboard.setColorIndependentSidebar(name);
                        final String username2 = packetWrapper.user().getProtocolInfo().getUsername();
                        final Optional<Byte> color2 = scoreboard.getPlayerTeamColor(username2);
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
                this.handler(packetWrapper -> {
                    final String team = packetWrapper.get(Type.STRING, 0);
                    if (team == null) {
                        packetWrapper.cancel();
                    }
                    else {
                        final byte mode = packetWrapper.passthrough((Type<Byte>)Type.BYTE);
                        final Scoreboard scoreboard = packetWrapper.user().get(Scoreboard.class);
                        if (mode != 0 && !scoreboard.teamExists(team)) {
                            packetWrapper.cancel();
                        }
                        else {
                            if (mode == 0 && scoreboard.teamExists(team)) {
                                scoreboard.removeTeam(team);
                                final PacketWrapper remove = PacketWrapper.create(62, null, packetWrapper.user());
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
                                final byte color = packetWrapper.read((Type<Byte>)Type.BYTE);
                                if (mode == 2 && scoreboard.getTeamColor(team).get() != color) {
                                    final String username = packetWrapper.user().getProtocolInfo().getUsername();
                                    final String sidebar = scoreboard.getColorDependentSidebar().get(color);
                                    final PacketWrapper sidebarPacket = packetWrapper.create(61);
                                    sidebarPacket.write(Type.BYTE, (Byte)1);
                                    sidebarPacket.write(Type.STRING, (sidebar == null) ? "" : sidebar);
                                    PacketUtil.sendPacket(sidebarPacket, Protocol1_7_6_10TO1_8.class);
                                }
                                scoreboard.setTeamColor(team, color);
                            }
                            if (mode == 0 || mode == 3 || mode == 4) {
                                final byte color2 = scoreboard.getTeamColor(team).get();
                                final String[] entries = packetWrapper.read(Type.STRING_ARRAY);
                                final ArrayList entryList = new ArrayList<String>();
                                for (int i = 0; i < entries.length; ++i) {
                                    final String entry = entries[i];
                                    final String username2 = packetWrapper.user().getProtocolInfo().getUsername();
                                    if (mode == 4) {
                                        if (!scoreboard.isPlayerInTeam(entry, team)) {
                                            continue;
                                        }
                                        else {
                                            scoreboard.removePlayerFromTeam(entry, team);
                                            if (entry.equals(username2)) {
                                                final PacketWrapper sidebarPacket2 = packetWrapper.create(61);
                                                sidebarPacket2.write(Type.BYTE, (Byte)1);
                                                sidebarPacket2.write(Type.STRING, (scoreboard.getColorIndependentSidebar() == null) ? "" : scoreboard.getColorIndependentSidebar());
                                                PacketUtil.sendPacket(sidebarPacket2, Protocol1_7_6_10TO1_8.class);
                                            }
                                        }
                                    }
                                    else {
                                        scoreboard.addPlayerToTeam(entry, team);
                                        if (entry.equals(username2) && scoreboard.getColorDependentSidebar().containsKey(color2)) {
                                            final PacketWrapper displayObjective = packetWrapper.create(61);
                                            displayObjective.write(Type.BYTE, (Byte)1);
                                            displayObjective.write(Type.STRING, scoreboard.getColorDependentSidebar().get(color2));
                                            PacketUtil.sendPacket(displayObjective, Protocol1_7_6_10TO1_8.class);
                                        }
                                    }
                                    entryList.add(entry);
                                }
                                packetWrapper.write(Type.SHORT, (short)entryList.size());
                                entryList.iterator();
                                final Iterator iterator;
                                while (iterator.hasNext()) {
                                    final String entry2 = iterator.next();
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
