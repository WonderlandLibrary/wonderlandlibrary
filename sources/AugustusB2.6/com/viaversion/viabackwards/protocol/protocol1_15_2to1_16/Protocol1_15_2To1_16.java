// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16Types;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.WorldNameTracker;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.storage.PlayerSneakStorage;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.rewriter.TagRewriter;
import java.util.UUID;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.CommandRewriter1_16;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.chat.TranslatableRewriter1_16;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.Objects;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.packets.EntityPackets1_16;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.packets.BlockItemPackets1_16;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_15_2To1_16 extends BackwardsProtocol<ClientboundPackets1_16, ClientboundPackets1_15, ServerboundPackets1_16, ServerboundPackets1_14>
{
    public static final BackwardsMappings MAPPINGS;
    private final EntityRewriter entityRewriter;
    private BlockItemPackets1_16 blockItemPackets;
    private TranslatableRewriter translatableRewriter;
    
    public Protocol1_15_2To1_16() {
        super(ClientboundPackets1_16.class, ClientboundPackets1_15.class, ServerboundPackets1_16.class, ServerboundPackets1_14.class);
        this.entityRewriter = new EntityPackets1_16(this);
    }
    
    @Override
    protected void registerPackets() {
        final Class<Protocol1_16To1_15_2> protocolClass = Protocol1_16To1_15_2.class;
        final BackwardsMappings mappings = Protocol1_15_2To1_16.MAPPINGS;
        Objects.requireNonNull(mappings);
        this.executeAsyncAfterLoaded(protocolClass, mappings::load);
        (this.translatableRewriter = new TranslatableRewriter1_16(this)).registerBossBar(ClientboundPackets1_16.BOSSBAR);
        this.translatableRewriter.registerCombatEvent(ClientboundPackets1_16.COMBAT_EVENT);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_16.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_16.TAB_LIST);
        this.translatableRewriter.registerTitle(ClientboundPackets1_16.TITLE);
        this.translatableRewriter.registerPing();
        new CommandRewriter1_16(this).registerDeclareCommands(ClientboundPackets1_16.DECLARE_COMMANDS);
        (this.blockItemPackets = new BlockItemPackets1_16(this, this.translatableRewriter)).register();
        this.entityRewriter.register();
        this.registerClientbound(State.STATUS, 0, 0, new PacketRemapper() {
            @Override
            public void registerMap() {
                final String original;
                final JsonObject object;
                final JsonElement description;
                this.handler(wrapper -> {
                    original = wrapper.passthrough(Type.STRING);
                    object = GsonUtil.getGson().fromJson(original, JsonObject.class);
                    description = object.get("description");
                    if (description != null) {
                        Protocol1_15_2To1_16.this.translatableRewriter.processText(description);
                        wrapper.set(Type.STRING, 0, object.toString());
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_16.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT)));
                this.map(Type.BYTE);
                this.map(Type.UUID, Type.NOTHING);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_16.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT)));
                int windowType;
                this.handler(wrapper -> {
                    windowType = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                    if (windowType == 20) {
                        wrapper.set(Type.VAR_INT, 1, 7);
                    }
                    else if (windowType > 20) {
                        wrapper.set(Type.VAR_INT, 1, --windowType);
                    }
                });
            }
        });
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_16.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_16.ENTITY_SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_16.NAMED_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_16.STOP_SOUND);
        this.registerClientbound(State.LOGIN, 2, 2, new PacketRemapper() {
            @Override
            public void registerMap() {
                final UUID uuid;
                this.handler(wrapper -> {
                    uuid = wrapper.read(Type.UUID_INT_ARRAY);
                    wrapper.write(Type.STRING, uuid.toString());
                });
            }
        });
        new TagRewriter(this).register(ClientboundPackets1_16.TAGS, RegistryType.ENTITY);
        new StatisticsRewriter(this).register(ClientboundPackets1_16.STATISTICS);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_14>)this).registerServerbound(ServerboundPackets1_14.ENTITY_ACTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int action;
                this.handler(wrapper -> {
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    action = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    if (action == 0) {
                        wrapper.user().get(PlayerSneakStorage.class).setSneaking(true);
                    }
                    else if (action == 1) {
                        wrapper.user().get(PlayerSneakStorage.class).setSneaking(false);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_14>)this).registerServerbound(ServerboundPackets1_14.INTERACT_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int action;
                this.handler(wrapper -> {
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    action = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    if (action == 0 || action == 2) {
                        if (action == 2) {
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                            wrapper.passthrough((Type<Object>)Type.FLOAT);
                        }
                        wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    }
                    wrapper.write(Type.BOOLEAN, wrapper.user().get(PlayerSneakStorage.class).isSneaking());
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_14>)this).registerServerbound(ServerboundPackets1_14.PLAYER_ABILITIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                final byte flags;
                final byte flags2;
                this.handler(wrapper -> {
                    flags = wrapper.read((Type<Byte>)Type.BYTE);
                    flags2 = (byte)(flags & 0x2);
                    wrapper.write(Type.BYTE, flags2);
                    wrapper.read((Type<Object>)Type.FLOAT);
                    wrapper.read((Type<Object>)Type.FLOAT);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_14>)this).cancelServerbound(ServerboundPackets1_14.UPDATE_JIGSAW_BLOCK);
    }
    
    @Override
    public void init(final UserConnection user) {
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
        user.put(new PlayerSneakStorage());
        user.put(new WorldNameTracker());
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_16Types.PLAYER, true));
    }
    
    @Override
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_15_2To1_16.MAPPINGS;
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public BlockItemPackets1_16 getItemRewriter() {
        return this.blockItemPackets;
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
    }
}
