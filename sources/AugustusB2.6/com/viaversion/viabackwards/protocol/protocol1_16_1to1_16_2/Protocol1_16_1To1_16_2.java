// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16_2Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.data.CommandRewriter1_16_2;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.Objects;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.packets.EntityPackets1_16_2;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.packets.BlockItemPackets1_16_2;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viabackwards.api.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public class Protocol1_16_1To1_16_2 extends BackwardsProtocol<ClientboundPackets1_16_2, ClientboundPackets1_16, ServerboundPackets1_16_2, ServerboundPackets1_16>
{
    public static final BackwardsMappings MAPPINGS;
    private final EntityRewriter entityRewriter;
    private BlockItemPackets1_16_2 blockItemPackets;
    private TranslatableRewriter translatableRewriter;
    
    public Protocol1_16_1To1_16_2() {
        super(ClientboundPackets1_16_2.class, ClientboundPackets1_16.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16.class);
        this.entityRewriter = new EntityPackets1_16_2(this);
    }
    
    @Override
    protected void registerPackets() {
        final Class<Protocol1_16_2To1_16_1> protocolClass = Protocol1_16_2To1_16_1.class;
        final BackwardsMappings mappings = Protocol1_16_1To1_16_2.MAPPINGS;
        Objects.requireNonNull(mappings);
        this.executeAsyncAfterLoaded(protocolClass, mappings::load);
        (this.translatableRewriter = new TranslatableRewriter(this)).registerBossBar(ClientboundPackets1_16_2.BOSSBAR);
        this.translatableRewriter.registerCombatEvent(ClientboundPackets1_16_2.COMBAT_EVENT);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_16_2.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_16_2.TAB_LIST);
        this.translatableRewriter.registerTitle(ClientboundPackets1_16_2.TITLE);
        this.translatableRewriter.registerOpenWindow(ClientboundPackets1_16_2.OPEN_WINDOW);
        this.translatableRewriter.registerPing();
        new CommandRewriter1_16_2(this).registerDeclareCommands(ClientboundPackets1_16_2.DECLARE_COMMANDS);
        (this.blockItemPackets = new BlockItemPackets1_16_2(this, this.translatableRewriter)).register();
        this.entityRewriter.register();
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_16_2.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_16_2.ENTITY_SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_16_2.NAMED_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_16_2.STOP_SOUND);
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_16_2.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                final JsonElement message;
                final byte position;
                this.handler(wrapper -> {
                    message = wrapper.passthrough(Type.COMPONENT);
                    Protocol1_16_1To1_16_2.this.translatableRewriter.processText(message);
                    position = wrapper.passthrough((Type<Byte>)Type.BYTE);
                    if (position == 2) {
                        wrapper.clearPacket();
                        wrapper.setId(ClientboundPackets1_16.TITLE.ordinal());
                        wrapper.write(Type.VAR_INT, 2);
                        wrapper.write(Type.COMPONENT, message);
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_16>)this).registerServerbound(ServerboundPackets1_16.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = wrapper.read((Type<Integer>)Type.VAR_INT);
                        if (type == 0) {
                            wrapper.passthrough(Type.STRING);
                            wrapper.setId(ServerboundPackets1_16_2.SEEN_RECIPE.ordinal());
                        }
                        else {
                            wrapper.cancel();
                            for (int i = 0; i < 3; ++i) {
                                this.sendSeenRecipePacket(i, wrapper);
                            }
                        }
                    }
                    
                    private void sendSeenRecipePacket(final int recipeType, final PacketWrapper wrapper) throws Exception {
                        final boolean open = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                        final boolean filter = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                        final PacketWrapper newPacket = wrapper.create(ServerboundPackets1_16_2.RECIPE_BOOK_DATA);
                        newPacket.write(Type.VAR_INT, recipeType);
                        newPacket.write(Type.BOOLEAN, open);
                        newPacket.write(Type.BOOLEAN, filter);
                        newPacket.sendToServer(Protocol1_16_1To1_16_2.class);
                    }
                });
            }
        });
        new TagRewriter(this).register(ClientboundPackets1_16_2.TAGS, RegistryType.ENTITY);
        new StatisticsRewriter(this).register(ClientboundPackets1_16_2.STATISTICS);
    }
    
    @Override
    public void init(final UserConnection user) {
        user.addEntityTracker(this.getClass(), new EntityTrackerBase(user, Entity1_16_2Types.PLAYER));
    }
    
    @Override
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_16_1To1_16_2.MAPPINGS;
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public BlockItemPackets1_16_2 getItemRewriter() {
        return this.blockItemPackets;
    }
    
    static {
        MAPPINGS = new BackwardsMappings("1.16.2", "1.16", Protocol1_16_2To1_16_1.class, true);
    }
}
