// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19Types;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.ProfileKey;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.time.Instant;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ChatDecorationResult;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.Protocol1_19_1To1_19;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.storage.DimensionRegistryStorage;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.data.CommandRewriter1_19;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.packets.BlockItemPackets1_19;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.packets.EntityPackets1_19;
import java.util.UUID;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ServerboundPackets1_19;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_18_2To1_19 extends BackwardsProtocol<ClientboundPackets1_19, ClientboundPackets1_18, ServerboundPackets1_19, ServerboundPackets1_17>
{
    public static final BackwardsMappings MAPPINGS;
    private static final UUID ZERO_UUID;
    private static final byte[] EMPTY_BYTES;
    private final EntityPackets1_19 entityRewriter;
    private final BlockItemPackets1_19 blockItemPackets;
    private final TranslatableRewriter translatableRewriter;
    
    public Protocol1_18_2To1_19() {
        super(ClientboundPackets1_19.class, ClientboundPackets1_18.class, ServerboundPackets1_19.class, ServerboundPackets1_17.class);
        this.entityRewriter = new EntityPackets1_19(this);
        this.blockItemPackets = new BlockItemPackets1_19(this);
        this.translatableRewriter = new TranslatableRewriter(this);
    }
    
    @Override
    protected void registerPackets() {
        this.executeAsyncAfterLoaded(Protocol1_19To1_18_2.class, () -> {
            Protocol1_18_2To1_19.MAPPINGS.load();
            this.entityRewriter.onMappingDataLoaded();
            return;
        });
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19.ACTIONBAR);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19.TITLE_TEXT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19.TITLE_SUBTITLE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_19.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_19.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_19.TAB_LIST);
        this.translatableRewriter.registerOpenWindow(ClientboundPackets1_19.OPEN_WINDOW);
        this.translatableRewriter.registerCombatKill(ClientboundPackets1_19.COMBAT_KILL);
        this.translatableRewriter.registerPing();
        this.blockItemPackets.register();
        this.entityRewriter.register();
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerStopSound(ClientboundPackets1_19.STOP_SOUND);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.read(Type.LONG);
                this.handler(soundRewriter.getSoundHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.ENTITY_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.read(Type.LONG);
                this.handler(soundRewriter.getSoundHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.read(Type.LONG);
                this.handler(soundRewriter.getNamedSoundHandler());
            }
        });
        final TagRewriter tagRewriter = new TagRewriter(this);
        tagRewriter.removeTags("minecraft:banner_pattern");
        tagRewriter.removeTags("minecraft:instrument");
        tagRewriter.removeTags("minecraft:cat_variant");
        tagRewriter.removeTags("minecraft:painting_variant");
        tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:polar_bears_spawnable_on_in_frozen_ocean");
        tagRewriter.renameTag(RegistryType.BLOCK, "minecraft:wool_carpets", "minecraft:carpets");
        tagRewriter.renameTag(RegistryType.ITEM, "minecraft:wool_carpets", "minecraft:carpets");
        tagRewriter.addEmptyTag(RegistryType.ITEM, "minecraft:occludes_vibration_signals");
        tagRewriter.registerGeneric(ClientboundPackets1_19.TAGS);
        new StatisticsRewriter(this).register(ClientboundPackets1_19.STATISTICS);
        final CommandRewriter commandRewriter = new CommandRewriter1_19(this);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.DECLARE_COMMANDS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$commandRewriter = commandRewriter;
                    for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        final byte flags = wrapper.passthrough((Type<Byte>)Type.BYTE);
                        wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                        if ((flags & 0x8) != 0x0) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        final int nodeType = flags & 0x3;
                        if (nodeType == 1 || nodeType == 2) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (nodeType == 2) {
                            final int argumentTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                            String argumentType = Protocol1_18_2To1_19.MAPPINGS.argumentType(argumentTypeId);
                            if (argumentType == null) {
                                ViaBackwards.getPlatform().getLogger().warning("Unknown command argument type id: " + argumentTypeId);
                                argumentType = "minecraft:no";
                            }
                            wrapper.write(Type.STRING, commandRewriter.handleArgumentType(argumentType));
                            commandRewriter.handleArgument(wrapper, argumentType);
                            if ((flags & 0x10) != 0x0) {
                                wrapper.passthrough(Type.STRING);
                            }
                        }
                    }
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19.SERVER_DATA);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19.CHAT_PREVIEW);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19.SET_DISPLAY_CHAT_PREVIEW);
        ((Protocol<ClientboundPackets1_19, ClientboundPackets1_18, S1, S2>)this).registerClientbound(ClientboundPackets1_19.PLAYER_CHAT, ClientboundPackets1_18.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final JsonElement signedContent = wrapper.read(Type.COMPONENT);
                    final JsonElement unsignedContent = wrapper.read(Type.OPTIONAL_COMPONENT);
                    final int chatTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final UUID sender = wrapper.read(Type.UUID);
                    final JsonElement senderName = wrapper.read(Type.COMPONENT);
                    final JsonElement teamName = wrapper.read(Type.OPTIONAL_COMPONENT);
                    final CompoundTag chatType = wrapper.user().get(DimensionRegistryStorage.class).chatType(chatTypeId);
                    final ChatDecorationResult decorationResult = Protocol1_19_1To1_19.decorateChatMessage(chatType, chatTypeId, senderName, teamName, (unsignedContent != null) ? unsignedContent : signedContent);
                    if (decorationResult == null) {
                        wrapper.cancel();
                        return;
                    }
                    else {
                        Protocol1_18_2To1_19.this.translatableRewriter.processText(decorationResult.content());
                        wrapper.write(Type.COMPONENT, decorationResult.content());
                        wrapper.write(Type.BYTE, (byte)(decorationResult.overlay() ? 2 : 1));
                        wrapper.write(Type.UUID, sender);
                        return;
                    }
                });
                this.read(Type.LONG);
                this.read(Type.LONG);
                this.read(Type.BYTE_ARRAY_PRIMITIVE);
            }
        });
        ((Protocol<ClientboundPackets1_19, ClientboundPackets1_18, S1, S2>)this).registerClientbound(ClientboundPackets1_19.SYSTEM_CHAT, ClientboundPackets1_18.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final JsonElement content = wrapper.passthrough(Type.COMPONENT);
                    Protocol1_18_2To1_19.this.translatableRewriter.processText(content);
                    final int typeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.BYTE, (byte)((typeId == 2) ? 2 : 0));
                    return;
                });
                this.create(Type.UUID, Protocol1_18_2To1_19.ZERO_UUID);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(wrapper -> wrapper.write(Type.LONG, Instant.now().toEpochMilli()));
                this.create(Type.LONG, 0L);
                this.handler(wrapper -> {
                    final String message = wrapper.get(Type.STRING, 0);
                    if (!message.isEmpty() && message.charAt(0) == '/') {
                        wrapper.setPacketType(ServerboundPackets1_19.CHAT_COMMAND);
                        wrapper.set(Type.STRING, 0, message.substring(1));
                        wrapper.write(Type.VAR_INT, 0);
                    }
                    else {
                        wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, Protocol1_18_2To1_19.EMPTY_BYTES);
                    }
                    wrapper.write(Type.BOOLEAN, false);
                });
            }
        });
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.GAME_PROFILE.getId(), ClientboundLoginPackets.GAME_PROFILE.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UUID);
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    for (int properties = wrapper.read((Type<Integer>)Type.VAR_INT), i = 0; i < properties; ++i) {
                        wrapper.read(Type.STRING);
                        wrapper.read(Type.STRING);
                        if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.read(Type.STRING);
                        }
                    }
                });
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.create(Type.OPTIONAL_PROFILE_KEY, null);
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.ENCRYPTION_KEY.getId(), ServerboundLoginPackets.ENCRYPTION_KEY.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE_ARRAY_PRIMITIVE);
                this.create(Type.BOOLEAN, true);
            }
        });
    }
    
    @Override
    public void init(final UserConnection user) {
        user.put(new DimensionRegistryStorage());
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_19Types.PLAYER, true));
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_18_2To1_19.MAPPINGS;
    }
    
    @Override
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public ItemRewriter getItemRewriter() {
        return this.blockItemPackets;
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
        ZERO_UUID = new UUID(0L, 0L);
        EMPTY_BYTES = new byte[0];
    }
}
