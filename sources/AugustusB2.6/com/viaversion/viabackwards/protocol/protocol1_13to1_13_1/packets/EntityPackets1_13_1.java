// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import com.viaversion.viaversion.api.type.types.version.Types1_13;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_13_1 extends LegacyEntityRewriter<Protocol1_13To1_13_1>
{
    public EntityPackets1_13_1(final Protocol1_13To1_13_1 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int entityId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        final byte type = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        final Entity1_13Types.EntityType entType = Entity1_13Types.getTypeFromId(type, true);
                        if (entType == null) {
                            ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13 entity type " + type);
                            return;
                        }
                        if (entType.is(Entity1_13Types.EntityType.FALLING_BLOCK)) {
                            final int data = wrapper.get((Type<Integer>)Type.INT, 0);
                            wrapper.set(Type.INT, 0, ((Protocol1_13To1_13_1)EntityPackets1_13_1.this.protocol).getMappingData().getNewBlockStateId(data));
                        }
                        EntityPackets1_13_1.this.tracker(wrapper.user()).addEntity(entityId, entType);
                    }
                });
            }
        });
        this.registerTracker(ClientboundPackets1_13.SPAWN_EXPERIENCE_ORB, Entity1_13Types.EntityType.EXPERIENCE_ORB);
        this.registerTracker(ClientboundPackets1_13.SPAWN_GLOBAL_ENTITY, Entity1_13Types.EntityType.LIGHTNING_BOLT);
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.SPAWN_MOB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Types1_13.METADATA_LIST);
                this.handler(EntityRewriterBase.this.getTrackerHandler());
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final List<Metadata> metadata = wrapper.get(Types1_13.METADATA_LIST, 0);
                        EntityPackets1_13_1.this.handleMetadata(wrapper.get((Type<Integer>)Type.VAR_INT, 0), metadata, wrapper.user());
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_13, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_13.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_13.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler(Types1_13.METADATA_LIST, Entity1_13Types.EntityType.PLAYER));
            }
        });
        this.registerTracker(ClientboundPackets1_13.SPAWN_PAINTING, Entity1_13Types.EntityType.PAINTING);
        this.registerJoinGame(ClientboundPackets1_13.JOIN_GAME, Entity1_13Types.EntityType.PLAYER);
        this.registerRespawn(ClientboundPackets1_13.RESPAWN);
        this.registerRemoveEntities(ClientboundPackets1_13.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_13.ENTITY_METADATA, Types1_13.METADATA_LIST);
    }
    
    @Override
    protected void registerRewrites() {
        int data;
        this.filter().handler((event, meta) -> {
            if (meta.metaType() == Types1_13.META_TYPES.itemType) {
                ((Protocol1_13To1_13_1)this.protocol).getItemRewriter().handleItemToClient((Item)meta.getValue());
            }
            else if (meta.metaType() == Types1_13.META_TYPES.blockStateType) {
                data = (int)meta.getValue();
                meta.setValue(((Protocol1_13To1_13_1)this.protocol).getMappingData().getNewBlockStateId(data));
            }
            else if (meta.metaType() == Types1_13.META_TYPES.particleType) {
                this.rewriteParticle((Particle)meta.getValue());
            }
            return;
        });
        this.filter().filterFamily(Entity1_13Types.EntityType.ABSTRACT_ARROW).cancel(7);
        this.filter().type(Entity1_13Types.EntityType.SPECTRAL_ARROW).index(8).toIndex(7);
        this.filter().type(Entity1_13Types.EntityType.TRIDENT).index(8).toIndex(7);
        final int data2;
        this.filter().filterFamily(Entity1_13Types.EntityType.MINECART_ABSTRACT).index(9).handler((event, meta) -> {
            data2 = (int)meta.getValue();
            meta.setValue(((Protocol1_13To1_13_1)this.protocol).getMappingData().getNewBlockStateId(data2));
        });
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_13Types.getTypeFromId(typeId, false);
    }
    
    @Override
    protected EntityType getObjectTypeFromId(final int typeId) {
        return Entity1_13Types.getTypeFromId(typeId, true);
    }
}
