// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_17_1to1_18;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_17Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.Objects;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.Protocol1_18To1_17_1;
import com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.packets.BlockItemPackets1_18;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.packets.EntityPackets1_18;
import com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.ClientboundPackets1_17_1;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_17_1To1_18 extends BackwardsProtocol<ClientboundPackets1_18, ClientboundPackets1_17_1, ServerboundPackets1_17, ServerboundPackets1_17>
{
    private static final BackwardsMappings MAPPINGS;
    private final EntityPackets1_18 entityRewriter;
    private TranslatableRewriter translatableRewriter;
    private BlockItemPackets1_18 itemRewriter;
    
    public Protocol1_17_1To1_18() {
        super(ClientboundPackets1_18.class, ClientboundPackets1_17_1.class, ServerboundPackets1_17.class, ServerboundPackets1_17.class);
        this.entityRewriter = new EntityPackets1_18(this);
    }
    
    @Override
    protected void registerPackets() {
        final Class<Protocol1_18To1_17_1> protocolClass = Protocol1_18To1_17_1.class;
        final BackwardsMappings mappings = Protocol1_17_1To1_18.MAPPINGS;
        Objects.requireNonNull(mappings);
        this.executeAsyncAfterLoaded(protocolClass, mappings::load);
        (this.translatableRewriter = new TranslatableRewriter(this)).registerComponentPacket(ClientboundPackets1_18.CHAT_MESSAGE);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_18.ACTIONBAR);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_18.TITLE_TEXT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_18.TITLE_SUBTITLE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_18.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_18.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_18.TAB_LIST);
        this.translatableRewriter.registerOpenWindow(ClientboundPackets1_18.OPEN_WINDOW);
        this.translatableRewriter.registerCombatKill(ClientboundPackets1_18.COMBAT_KILL);
        this.translatableRewriter.registerPing();
        this.itemRewriter = new BlockItemPackets1_18(this, this.translatableRewriter);
        this.entityRewriter.register();
        this.itemRewriter.register();
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerSound(ClientboundPackets1_18.SOUND);
        soundRewriter.registerSound(ClientboundPackets1_18.ENTITY_SOUND);
        soundRewriter.registerStopSound(ClientboundPackets1_18.STOP_SOUND);
        soundRewriter.registerNamedSound(ClientboundPackets1_18.NAMED_SOUND);
        final TagRewriter tagRewriter = new TagRewriter(this);
        tagRewriter.addEmptyTag(RegistryType.BLOCK, "minecraft:lava_pool_stone_replaceables");
        tagRewriter.registerGeneric(ClientboundPackets1_18.TAGS);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CLIENT_SETTINGS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN);
                this.create(Type.BOOLEAN, true);
            }
        });
    }
    
    @Override
    public void init(final UserConnection connection) {
        this.addEntityTracker(connection, new EntityTrackerBase(connection, Entity1_17Types.PLAYER));
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_17_1To1_18.MAPPINGS;
    }
    
    @Override
    public EntityPackets1_18 getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public BlockItemPackets1_18 getItemRewriter() {
        return this.itemRewriter;
    }
    
    @Override
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
    }
}
