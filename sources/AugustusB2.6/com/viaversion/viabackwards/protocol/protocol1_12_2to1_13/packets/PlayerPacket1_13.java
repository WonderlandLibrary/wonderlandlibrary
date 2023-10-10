// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.Position;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viabackwards.utils.ChatUtil;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.gson.JsonElement;
import java.util.UUID;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.storage.TabCompleteStorage;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data.ParticleMapping;
import java.util.List;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import com.viaversion.viaversion.api.rewriter.RewriterBase;

public class PlayerPacket1_13 extends RewriterBase<Protocol1_12_2To1_13>
{
    private final CommandRewriter commandRewriter;
    
    public PlayerPacket1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol);
        this.commandRewriter = new CommandRewriter((Protocol)this.protocol) {};
    }
    
    @Override
    protected void registerPackets() {
        ((Protocol1_12_2To1_13)this.protocol).registerClientbound(State.LOGIN, 4, -1, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        packetWrapper.cancel();
                        packetWrapper.create(2, new PacketHandler() {
                            @Override
                            public void handle(final PacketWrapper newWrapper) throws Exception {
                                newWrapper.write(Type.VAR_INT, (Integer)packetWrapper.read((Type<T>)Type.VAR_INT));
                                newWrapper.write(Type.BOOLEAN, false);
                            }
                        }).sendToServer(Protocol1_12_2To1_13.class);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final String channel = wrapper.read(Type.STRING);
                        if (channel.equals("minecraft:trader_list")) {
                            wrapper.write(Type.STRING, "MC|TrList");
                            wrapper.passthrough((Type<Object>)Type.INT);
                            for (int size = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE), i = 0; i < size; ++i) {
                                final Item input = wrapper.read(Type.FLAT_ITEM);
                                wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getItemRewriter().handleItemToClient(input));
                                final Item output = wrapper.read(Type.FLAT_ITEM);
                                wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getItemRewriter().handleItemToClient(output));
                                final boolean secondItem = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                                if (secondItem) {
                                    final Item second = wrapper.read(Type.FLAT_ITEM);
                                    wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getItemRewriter().handleItemToClient(second));
                                }
                                wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                                wrapper.passthrough((Type<Object>)Type.INT);
                                wrapper.passthrough((Type<Object>)Type.INT);
                            }
                        }
                        else {
                            final String oldChannel = InventoryPackets.getOldPluginChannelId(channel);
                            if (oldChannel == null) {
                                if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                                    ViaBackwards.getPlatform().getLogger().warning("Ignoring outgoing plugin message with channel: " + channel);
                                }
                                wrapper.cancel();
                                return;
                            }
                            wrapper.write(Type.STRING, oldChannel);
                            if (oldChannel.equals("REGISTER") || oldChannel.equals("UNREGISTER")) {
                                final String[] channels = new String(wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8).split("\u0000");
                                final List<String> rewrittenChannels = new ArrayList<String>();
                                for (final String s : channels) {
                                    final String rewritten = InventoryPackets.getOldPluginChannelId(s);
                                    if (rewritten != null) {
                                        rewrittenChannels.add(rewritten);
                                    }
                                    else if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                                        ViaBackwards.getPlatform().getLogger().warning("Ignoring plugin channel in outgoing REGISTER: " + s);
                                    }
                                }
                                wrapper.write(Type.REMAINING_BYTES, Joiner.on('\0').join(rewrittenChannels).getBytes(StandardCharsets.UTF_8));
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ParticleMapping.ParticleData old = ParticleMapping.getMapping(wrapper.get((Type<Integer>)Type.INT, 0));
                        wrapper.set(Type.INT, 0, old.getHistoryId());
                        final int[] data = old.rewriteData((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol, wrapper);
                        if (data != null) {
                            if (old.getHandler().isBlockHandler() && data[0] == 0) {
                                wrapper.cancel();
                                return;
                            }
                            for (final int i : data) {
                                wrapper.write(Type.VAR_INT, i);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.PLAYER_INFO, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper packetWrapper) throws Exception {
                        final TabCompleteStorage storage = packetWrapper.user().get(TabCompleteStorage.class);
                        final int action = packetWrapper.passthrough((Type<Integer>)Type.VAR_INT);
                        for (int nPlayers = packetWrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < nPlayers; ++i) {
                            final UUID uuid = packetWrapper.passthrough(Type.UUID);
                            if (action == 0) {
                                final String name = packetWrapper.passthrough(Type.STRING);
                                storage.usernames().put(uuid, name);
                                for (int nProperties = packetWrapper.passthrough((Type<Integer>)Type.VAR_INT), j = 0; j < nProperties; ++j) {
                                    packetWrapper.passthrough(Type.STRING);
                                    packetWrapper.passthrough(Type.STRING);
                                    if (packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                        packetWrapper.passthrough(Type.STRING);
                                    }
                                }
                                packetWrapper.passthrough((Type<Object>)Type.VAR_INT);
                                packetWrapper.passthrough((Type<Object>)Type.VAR_INT);
                                if (packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                    packetWrapper.passthrough(Type.COMPONENT);
                                }
                            }
                            else if (action == 1) {
                                packetWrapper.passthrough((Type<Object>)Type.VAR_INT);
                            }
                            else if (action == 2) {
                                packetWrapper.passthrough((Type<Object>)Type.VAR_INT);
                            }
                            else if (action == 3) {
                                if (packetWrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                    packetWrapper.passthrough(Type.COMPONENT);
                                }
                            }
                            else if (action == 4) {
                                storage.usernames().remove(uuid);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.SCOREBOARD_OBJECTIVE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte mode = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        if (mode == 0 || mode == 2) {
                            String value = wrapper.read(Type.COMPONENT).toString();
                            value = ChatRewriter.jsonToLegacyText(value);
                            if (value.length() > 32) {
                                value = value.substring(0, 32);
                            }
                            wrapper.write(Type.STRING, value);
                            final int type = wrapper.read((Type<Integer>)Type.VAR_INT);
                            wrapper.write(Type.STRING, (type == 1) ? "hearts" : "integer");
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.TEAMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final byte action = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        if (action == 0 || action == 2) {
                            String displayName = wrapper.read(Type.STRING);
                            displayName = ChatRewriter.jsonToLegacyText(displayName);
                            displayName = ChatUtil.removeUnusedColor(displayName, 'f');
                            if (displayName.length() > 32) {
                                displayName = displayName.substring(0, 32);
                            }
                            wrapper.write(Type.STRING, displayName);
                            final byte flags = wrapper.read((Type<Byte>)Type.BYTE);
                            final String nameTagVisibility = wrapper.read(Type.STRING);
                            final String collisionRule = wrapper.read(Type.STRING);
                            int colour = wrapper.read((Type<Integer>)Type.VAR_INT);
                            if (colour == 21) {
                                colour = -1;
                            }
                            final JsonElement prefixComponent = wrapper.read(Type.COMPONENT);
                            final JsonElement suffixComponent = wrapper.read(Type.COMPONENT);
                            String prefix = (prefixComponent == null || prefixComponent.isJsonNull()) ? "" : ChatRewriter.jsonToLegacyText(prefixComponent.toString());
                            if (ViaBackwards.getConfig().addTeamColorTo1_13Prefix()) {
                                prefix = prefix + "§" + ((colour > -1 && colour <= 15) ? Integer.toHexString(colour) : "r");
                            }
                            prefix = ChatUtil.removeUnusedColor(prefix, 'f', true);
                            if (prefix.length() > 16) {
                                prefix = prefix.substring(0, 16);
                            }
                            if (prefix.endsWith("§")) {
                                prefix = prefix.substring(0, prefix.length() - 1);
                            }
                            String suffix = (suffixComponent == null || suffixComponent.isJsonNull()) ? "" : ChatRewriter.jsonToLegacyText(suffixComponent.toString());
                            suffix = ChatUtil.removeUnusedColor(suffix, '\0');
                            if (suffix.length() > 16) {
                                suffix = suffix.substring(0, 16);
                            }
                            if (suffix.endsWith("§")) {
                                suffix = suffix.substring(0, suffix.length() - 1);
                            }
                            wrapper.write(Type.STRING, prefix);
                            wrapper.write(Type.STRING, suffix);
                            wrapper.write(Type.BYTE, flags);
                            wrapper.write(Type.STRING, nameTagVisibility);
                            wrapper.write(Type.STRING, collisionRule);
                            wrapper.write(Type.BYTE, (byte)colour);
                        }
                        if (action == 0 || action == 3 || action == 4) {
                            wrapper.passthrough(Type.STRING_ARRAY);
                        }
                    }
                });
            }
        });
        ((Protocol<ClientboundPackets1_13, ClientboundPackets1_12_1, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.DECLARE_COMMANDS, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                final TabCompleteStorage storage;
                final int size;
                boolean initialNodes;
                int i;
                byte flags;
                byte nodeType;
                String name;
                this.handler(wrapper -> {
                    wrapper.cancel();
                    storage = wrapper.user().get(TabCompleteStorage.class);
                    if (!storage.commands().isEmpty()) {
                        storage.commands().clear();
                    }
                    size = wrapper.read((Type<Integer>)Type.VAR_INT);
                    initialNodes = true;
                    for (i = 0; i < size; ++i) {
                        flags = wrapper.read((Type<Byte>)Type.BYTE);
                        wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                        if ((flags & 0x8) != 0x0) {
                            wrapper.read((Type<Object>)Type.VAR_INT);
                        }
                        nodeType = (byte)(flags & 0x3);
                        if (initialNodes && nodeType == 2) {
                            initialNodes = false;
                        }
                        if (nodeType == 1 || nodeType == 2) {
                            name = wrapper.read(Type.STRING);
                            if (nodeType == 1 && initialNodes) {
                                storage.commands().add('/' + name);
                            }
                        }
                        if (nodeType == 2) {
                            PlayerPacket1_13.this.commandRewriter.handleArgument(wrapper, wrapper.read(Type.STRING));
                        }
                        if ((flags & 0x10) != 0x0) {
                            wrapper.read(Type.STRING);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final TabCompleteStorage storage = wrapper.user().get(TabCompleteStorage.class);
                        if (storage.lastRequest() == null) {
                            wrapper.cancel();
                            return;
                        }
                        if (storage.lastId() != wrapper.read((Type<Integer>)Type.VAR_INT)) {
                            wrapper.cancel();
                        }
                        final int start = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final int length = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final int lastRequestPartIndex = storage.lastRequest().lastIndexOf(32) + 1;
                        if (lastRequestPartIndex != start) {
                            wrapper.cancel();
                        }
                        if (length != storage.lastRequest().length() - lastRequestPartIndex) {
                            wrapper.cancel();
                        }
                        for (int count = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < count; ++i) {
                            final String match = wrapper.read(Type.STRING);
                            wrapper.write(Type.STRING, ((start == 0 && !storage.isLastAssumeCommand()) ? "/" : "") + match);
                            if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                                wrapper.read(Type.STRING);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_12_1>)this.protocol).registerServerbound(ServerboundPackets1_12_1.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                final TabCompleteStorage storage;
                final ArrayList<String> suggestions;
                String command;
                final boolean assumeCommand;
                String buffer;
                final Iterator<String> iterator;
                String value;
                final Iterator<String> iterator2;
                String value2;
                PacketWrapper response;
                final Iterator<String> iterator3;
                String value3;
                int id;
                this.handler(wrapper -> {
                    storage = wrapper.user().get(TabCompleteStorage.class);
                    suggestions = new ArrayList<String>();
                    command = wrapper.read(Type.STRING);
                    assumeCommand = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    wrapper.read(Type.OPTIONAL_POSITION);
                    if (!assumeCommand && !command.startsWith("/")) {
                        buffer = command.substring(command.lastIndexOf(32) + 1);
                        storage.usernames().values().iterator();
                        while (iterator.hasNext()) {
                            value = iterator.next();
                            if (startsWithIgnoreCase(value, buffer)) {
                                suggestions.add(value);
                            }
                        }
                    }
                    else if (!storage.commands().isEmpty() && !command.contains(" ")) {
                        storage.commands().iterator();
                        while (iterator2.hasNext()) {
                            value2 = iterator2.next();
                            if (startsWithIgnoreCase(value2, command)) {
                                suggestions.add(value2);
                            }
                        }
                    }
                    if (!suggestions.isEmpty()) {
                        wrapper.cancel();
                        response = wrapper.create(ClientboundPackets1_12_1.TAB_COMPLETE);
                        response.write(Type.VAR_INT, suggestions.size());
                        suggestions.iterator();
                        while (iterator3.hasNext()) {
                            value3 = iterator3.next();
                            response.write(Type.STRING, value3);
                        }
                        response.scheduleSend(Protocol1_12_2To1_13.class);
                        storage.setLastRequest(null);
                    }
                    else {
                        if (!assumeCommand && command.startsWith("/")) {
                            command = command.substring(1);
                        }
                        id = ThreadLocalRandom.current().nextInt();
                        wrapper.write(Type.VAR_INT, id);
                        wrapper.write(Type.STRING, command);
                        storage.setLastId(id);
                        storage.setLastAssumeCommand(assumeCommand);
                        storage.setLastRequest(command);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_12_1>)this.protocol).registerServerbound(ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_0         /* this */
                //     2: invokedynamic   BootstrapMethod #0, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$11;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
                //     7: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_12_2to1_13/packets/PlayerPacket1_13$11.handler:(Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
                //    10: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
                //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.STATISTICS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        int newSize;
                        final int size = newSize = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        for (int i = 0; i < size; ++i) {
                            final int categoryId = wrapper.read((Type<Integer>)Type.VAR_INT);
                            final int statisticId = wrapper.read((Type<Integer>)Type.VAR_INT);
                            String name = "";
                            switch (categoryId) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7: {
                                    wrapper.read((Type<Object>)Type.VAR_INT);
                                    --newSize;
                                    continue;
                                }
                                case 8: {
                                    name = ((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getMappingData().getStatisticMappings().get(statisticId);
                                    if (name == null) {
                                        wrapper.read((Type<Object>)Type.VAR_INT);
                                        --newSize;
                                        continue;
                                    }
                                    break;
                                }
                            }
                            wrapper.write(Type.STRING, name);
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        if (newSize != size) {
                            wrapper.set(Type.VAR_INT, 0, newSize);
                        }
                    }
                });
            }
        });
    }
    
    private static boolean startsWithIgnoreCase(final String string, final String prefix) {
        return string.length() >= prefix.length() && string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
