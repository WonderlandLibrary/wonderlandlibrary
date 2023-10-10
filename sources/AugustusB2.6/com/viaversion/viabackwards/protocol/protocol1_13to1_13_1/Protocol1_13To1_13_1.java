// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13to1_13_1;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.rewriter.ComponentRewriter;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.data.CommandRewriter1_13_1;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets.WorldPackets1_13_1;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.Objects;
import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets.InventoryPackets1_13_1;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets.EntityPackets1_13_1;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_13To1_13_1 extends BackwardsProtocol<ClientboundPackets1_13, ClientboundPackets1_13, ServerboundPackets1_13, ServerboundPackets1_13>
{
    public static final BackwardsMappings MAPPINGS;
    private final EntityRewriter entityRewriter;
    private final ItemRewriter itemRewriter;
    
    public Protocol1_13To1_13_1() {
        super(ClientboundPackets1_13.class, ClientboundPackets1_13.class, ServerboundPackets1_13.class, ServerboundPackets1_13.class);
        this.entityRewriter = new EntityPackets1_13_1(this);
        this.itemRewriter = new InventoryPackets1_13_1(this);
    }
    
    @Override
    protected void registerPackets() {
        final Class<Protocol1_13_1To1_13> protocolClass = Protocol1_13_1To1_13.class;
        final BackwardsMappings mappings = Protocol1_13To1_13_1.MAPPINGS;
        Objects.requireNonNull(mappings);
        this.executeAsyncAfterLoaded(protocolClass, mappings::load);
        this.entityRewriter.register();
        this.itemRewriter.register();
        WorldPackets1_13_1.register(this);
        final TranslatableRewriter translatableRewriter = new TranslatableRewriter(this);
        translatableRewriter.registerChatMessage(ClientboundPackets1_13.CHAT_MESSAGE);
        translatableRewriter.registerCombatEvent(ClientboundPackets1_13.COMBAT_EVENT);
        translatableRewriter.registerDisconnect(ClientboundPackets1_13.DISCONNECT);
        translatableRewriter.registerTabList(ClientboundPackets1_13.TAB_LIST);
        translatableRewriter.registerTitle(ClientboundPackets1_13.TITLE);
        translatableRewriter.registerPing();
        new CommandRewriter1_13_1(this).registerDeclareCommands(ClientboundPackets1_13.DECLARE_COMMANDS);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this).registerServerbound(ServerboundPackets1_13.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.STRING, (ValueTransformer<String, Object>)new ValueTransformer<String, String>(Type.STRING) {
                    @Override
                    public String transform(final PacketWrapper wrapper, final String inputValue) {
                        return inputValue.startsWith("/") ? inputValue : ("/" + inputValue);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this).registerServerbound(ServerboundPackets1_13.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.FLAT_ITEM);
                this.map(Type.BOOLEAN);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        Protocol1_13To1_13_1.this.itemRewriter.handleItemToServer(wrapper.get(Type.FLAT_ITEM, 0));
                        wrapper.write(Type.VAR_INT, 0);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_13.OPEN_WINDOW, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                final ComponentRewriter val$translatableRewriter;
                final JsonElement title;
                JsonObject legacyComponent;
                this.handler(wrapper -> {
                    val$translatableRewriter = translatableRewriter;
                    title = wrapper.passthrough(Type.COMPONENT);
                    val$translatableRewriter.processText(title);
                    if (ViaBackwards.getConfig().fix1_13FormattedInventoryTitle()) {
                        if (!title.isJsonObject() || title.getAsJsonObject().size() != 1 || !title.getAsJsonObject().has("translate")) {
                            legacyComponent = new JsonObject();
                            legacyComponent.addProperty("text", ChatRewriter.jsonToLegacyText(title.toString()));
                            wrapper.set(Type.COMPONENT, 0, legacyComponent);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_13.TAB_COMPLETE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int start = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                        wrapper.set(Type.VAR_INT, 1, start - 1);
                        for (int count = wrapper.get((Type<Integer>)Type.VAR_INT, 3), i = 0; i < count; ++i) {
                            wrapper.passthrough(Type.STRING);
                            final boolean hasTooltip = wrapper.passthrough((Type<Boolean>)Type.BOOLEAN);
                            if (hasTooltip) {
                                wrapper.passthrough(Type.STRING);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_13.BOSSBAR, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int action = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (action == 0 || action == 3) {
                            translatableRewriter.processText(wrapper.passthrough(Type.COMPONENT));
                            if (action == 0) {
                                wrapper.passthrough((Type<Object>)Type.FLOAT);
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                                short flags = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                                if ((flags & 0x4) != 0x0) {
                                    flags |= 0x2;
                                }
                                wrapper.write(Type.UNSIGNED_BYTE, flags);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_13.ADVANCEMENTS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                        for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                            wrapper.passthrough(Type.STRING);
                            if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                wrapper.passthrough(Type.STRING);
                            }
                            if (wrapper.passthrough((Type<Boolean>)Type.BOOLEAN)) {
                                wrapper.passthrough(Type.COMPONENT);
                                wrapper.passthrough(Type.COMPONENT);
                                final Item icon = wrapper.passthrough(Type.FLAT_ITEM);
                                Protocol1_13To1_13_1.this.itemRewriter.handleItemToClient(icon);
                                wrapper.passthrough((Type<Object>)Type.VAR_INT);
                                final int flags = wrapper.passthrough((Type<Integer>)Type.INT);
                                if ((flags & 0x1) != 0x0) {
                                    wrapper.passthrough(Type.STRING);
                                }
                                wrapper.passthrough((Type<Object>)Type.FLOAT);
                                wrapper.passthrough((Type<Object>)Type.FLOAT);
                            }
                            wrapper.passthrough(Type.STRING_ARRAY);
                            for (int arrayLength = wrapper.passthrough((Type<Integer>)Type.VAR_INT), array = 0; array < arrayLength; ++array) {
                                wrapper.passthrough(Type.STRING_ARRAY);
                            }
                        }
                    }
                });
            }
        });
        new TagRewriter(this).register(ClientboundPackets1_13.TAGS, RegistryType.ITEM);
        new StatisticsRewriter(this).register(ClientboundPackets1_13.STATISTICS);
    }
    
    @Override
    public void init(final UserConnection user) {
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_13Types.EntityType.PLAYER));
        if (!user.has(ClientWorld.class)) {
            user.put(new ClientWorld(user));
        }
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_13To1_13_1.MAPPINGS;
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public ItemRewriter getItemRewriter() {
        return this.itemRewriter;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.13.2", "1.13", Protocol1_13_1To1_13.class, true);
    }
}
