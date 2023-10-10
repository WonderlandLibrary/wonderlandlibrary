// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets;

import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.type.types.Particle;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.rewriter.meta.MetaHandlerEvent;
import com.viaversion.viaversion.api.minecraft.VillagerData;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viabackwards.api.entities.storage.EntityData;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_13Types;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import java.util.function.Supplier;
import com.viaversion.viabackwards.api.entities.storage.EntityPositionStorage;
import com.viaversion.viabackwards.api.rewriters.EntityRewriterBase;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.types.version.Types1_13_2;
import com.viaversion.viabackwards.api.entities.storage.EntityPositionHandler;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import com.viaversion.viabackwards.api.rewriters.LegacyEntityRewriter;

public class EntityPackets1_14 extends LegacyEntityRewriter<Protocol1_13_2To1_14>
{
    private EntityPositionHandler positionHandler;
    
    public EntityPackets1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol, Types1_13_2.META_TYPES.optionalComponentType, Types1_13_2.META_TYPES.booleanType);
    }
    
    @Override
    protected void addTrackedEntity(final PacketWrapper wrapper, final int entityId, final EntityType type) throws Exception {
        super.addTrackedEntity(wrapper, entityId, type);
        if (type == Entity1_14Types.PAINTING) {
            final Position position = wrapper.get(Type.POSITION, 0);
            this.positionHandler.cacheEntityPosition(wrapper, position.getX(), position.getY(), position.getZ(), true, false);
        }
        else if (wrapper.getId() != ClientboundPackets1_14.JOIN_GAME.getId()) {
            this.positionHandler.cacheEntityPosition(wrapper, true, false);
        }
    }
    
    @Override
    protected void registerPackets() {
        this.positionHandler = new EntityPositionHandler(this, EntityPositionStorage1_14.class, EntityPositionStorage1_14::new);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_STATUS, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int entityId;
                final byte status;
                EntityTracker tracker;
                EntityType entityType;
                int i;
                PacketWrapper equipmentPacket;
                this.handler(wrapper -> {
                    entityId = wrapper.passthrough((Type<Integer>)Type.INT);
                    status = wrapper.passthrough((Type<Byte>)Type.BYTE);
                    if (status == 3) {
                        tracker = ((com.viaversion.viaversion.api.rewriter.EntityRewriter<Protocol>)EntityPackets1_14.this).tracker(wrapper.user());
                        entityType = tracker.entityType(entityId);
                        if (entityType == Entity1_14Types.PLAYER) {
                            for (i = 0; i <= 5; ++i) {
                                equipmentPacket = wrapper.create(ClientboundPackets1_13.ENTITY_EQUIPMENT);
                                equipmentPacket.write(Type.VAR_INT, entityId);
                                equipmentPacket.write(Type.VAR_INT, i);
                                equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, null);
                                equipmentPacket.send(Protocol1_13_2To1_14.class);
                            }
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_TELEPORT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, false, false));
            }
        });
        final PacketRemapper relativeMoveHandler = new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final double x = wrapper.get((Type<Short>)Type.SHORT, 0) / 4096.0;
                        final double y = wrapper.get((Type<Short>)Type.SHORT, 1) / 4096.0;
                        final double z = wrapper.get((Type<Short>)Type.SHORT, 2) / 4096.0;
                        EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, x, y, z, false, true);
                    }
                });
            }
        };
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_POSITION, relativeMoveHandler);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.ENTITY_POSITION_AND_ROTATION, relativeMoveHandler);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT, Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.INT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(LegacyEntityRewriter.this.getObjectTrackerHandler());
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int id = wrapper.get((Type<Byte>)Type.BYTE, 0);
                        final int mappedId = EntityPackets1_14.this.newEntityId(id);
                        final Entity1_13Types.EntityType entityType = Entity1_13Types.getTypeFromId(mappedId, false);
                        Entity1_13Types.ObjectType objectType;
                        if (entityType.isOrHasParent(Entity1_13Types.EntityType.MINECART_ABSTRACT)) {
                            objectType = Entity1_13Types.ObjectType.MINECART;
                            int data = 0;
                            switch (entityType) {
                                case CHEST_MINECART: {
                                    data = 1;
                                    break;
                                }
                                case FURNACE_MINECART: {
                                    data = 2;
                                    break;
                                }
                                case TNT_MINECART: {
                                    data = 3;
                                    break;
                                }
                                case SPAWNER_MINECART: {
                                    data = 4;
                                    break;
                                }
                                case HOPPER_MINECART: {
                                    data = 5;
                                    break;
                                }
                                case COMMAND_BLOCK_MINECART: {
                                    data = 6;
                                    break;
                                }
                            }
                            if (data != 0) {
                                wrapper.set(Type.INT, 0, data);
                            }
                        }
                        else {
                            objectType = Entity1_13Types.ObjectType.fromEntityType(entityType).orElse(null);
                        }
                        if (objectType == null) {
                            return;
                        }
                        wrapper.set(Type.BYTE, 0, (byte)objectType.getId());
                        int data = wrapper.get((Type<Integer>)Type.INT, 0);
                        if (objectType == Entity1_13Types.ObjectType.FALLING_BLOCK) {
                            final int blockState = wrapper.get((Type<Integer>)Type.INT, 0);
                            final int combined = ((Protocol1_13_2To1_14)EntityPackets1_14.this.protocol).getMappingData().getNewBlockStateId(blockState);
                            wrapper.set(Type.INT, 0, combined);
                        }
                        else if (entityType.isOrHasParent(Entity1_13Types.EntityType.ABSTRACT_ARROW)) {
                            wrapper.set(Type.INT, 0, data + 1);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_MOB, new PacketRemapper() {
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
                this.map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 1);
                        final EntityType entityType = Entity1_14Types.getTypeFromId(type);
                        EntityPackets1_14.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), entityType);
                        final int oldId = EntityPackets1_14.this.newEntityId(type);
                        if (oldId == -1) {
                            final EntityData entityData = EntityRewriterBase.this.entityDataForType(entityType);
                            if (entityData == null) {
                                ViaBackwards.getPlatform().getLogger().warning("Could not find 1.13.2 entity type for 1.14 entity type " + type + "/" + entityType);
                                wrapper.cancel();
                            }
                            else {
                                wrapper.set(Type.VAR_INT, 1, entityData.replacementId());
                            }
                        }
                        else {
                            wrapper.set(Type.VAR_INT, 1, oldId);
                        }
                    }
                });
                this.handler(LegacyEntityRewriter.this.getMobSpawnRewriter(Types1_13_2.METADATA_LIST));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_EXPERIENCE_ORB, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), Entity1_14Types.EXPERIENCE_ORB));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_GLOBAL_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), Entity1_14Types.LIGHTNING_BOLT));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_PAINTING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.VAR_INT);
                this.map(Type.POSITION1_14, Type.POSITION);
                this.map(Type.BYTE);
                this.handler(wrapper -> EntityPackets1_14.this.addTrackedEntity(wrapper, wrapper.get((Type<Integer>)Type.VAR_INT, 0), Entity1_14Types.PAINTING));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.UUID);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
                this.handler(LegacyEntityRewriter.this.getTrackerAndMetaHandler(Types1_13_2.METADATA_LIST, Entity1_14Types.PLAYER));
                this.handler(wrapper -> EntityPackets1_14.this.positionHandler.cacheEntityPosition(wrapper, true, false));
            }
        });
        this.registerRemoveEntities(ClientboundPackets1_14.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_14.ENTITY_METADATA, Types1_14.METADATA_LIST, Types1_13_2.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.INT);
                this.handler(EntityRewriterBase.this.getTrackerHandler(Entity1_14Types.PLAYER, Type.INT));
                this.handler(EntityRewriterBase.this.getDimensionHandler(1));
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
                        wrapper.passthrough((Type<Object>)Type.UNSIGNED_BYTE);
                        wrapper.passthrough(Type.STRING);
                        wrapper.read((Type<Object>)Type.VAR_INT);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final ClientWorld clientWorld = wrapper.user().get(ClientWorld.class);
                        final int dimensionId = wrapper.get((Type<Integer>)Type.INT, 0);
                        clientWorld.setEnvironment(dimensionId);
                        wrapper.write(Type.UNSIGNED_BYTE, (Short)0);
                        wrapper.user().get(ChunkLightStorage.class).clear();
                    }
                });
            }
        });
    }
    
    @Override
    protected void registerRewrites() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.values:()[Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //     4: ldc             Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_13Types$EntityType;.class
        //     6: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.mapTypes:([Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;Ljava/lang/Class;)V
        //     9: aload_0         /* this */
        //    10: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.CAT:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    13: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.OCELOT:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    16: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.mapEntityTypeWithData:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    19: invokevirtual   com/viaversion/viabackwards/api/entities/storage/EntityData.jsonName:()Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    22: pop            
        //    23: aload_0         /* this */
        //    24: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.TRADER_LLAMA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    27: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.LLAMA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    30: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.mapEntityTypeWithData:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    33: invokevirtual   com/viaversion/viabackwards/api/entities/storage/EntityData.jsonName:()Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    36: pop            
        //    37: aload_0         /* this */
        //    38: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.FOX:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    41: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.WOLF:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    44: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.mapEntityTypeWithData:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    47: invokevirtual   com/viaversion/viabackwards/api/entities/storage/EntityData.jsonName:()Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    50: pop            
        //    51: aload_0         /* this */
        //    52: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PANDA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    55: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.POLAR_BEAR:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    58: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.mapEntityTypeWithData:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    61: invokevirtual   com/viaversion/viabackwards/api/entities/storage/EntityData.jsonName:()Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    64: pop            
        //    65: aload_0         /* this */
        //    66: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PILLAGER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    69: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.VILLAGER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    72: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.mapEntityTypeWithData:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    75: invokevirtual   com/viaversion/viabackwards/api/entities/storage/EntityData.jsonName:()Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    78: pop            
        //    79: aload_0         /* this */
        //    80: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.WANDERING_TRADER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    83: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.VILLAGER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    86: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.mapEntityTypeWithData:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    89: invokevirtual   com/viaversion/viabackwards/api/entities/storage/EntityData.jsonName:()Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //    92: pop            
        //    93: aload_0         /* this */
        //    94: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.RAVAGER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //    97: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.COW:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   100: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.mapEntityTypeWithData:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //   103: invokevirtual   com/viaversion/viabackwards/api/entities/storage/EntityData.jsonName:()Lcom/viaversion/viabackwards/api/entities/storage/EntityData;
        //   106: pop            
        //   107: aload_0         /* this */
        //   108: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   111: aload_0         /* this */
        //   112: invokedynamic   BootstrapMethod #1, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14;)Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   117: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   120: aload_0         /* this */
        //   121: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   124: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PILLAGER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   127: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   130: bipush          15
        //   132: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   135: aload_0         /* this */
        //   136: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   139: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.FOX:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   142: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   145: bipush          15
        //   147: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   150: aload_0         /* this */
        //   151: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   154: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.FOX:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   157: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   160: bipush          16
        //   162: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   165: aload_0         /* this */
        //   166: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   169: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.FOX:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   172: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   175: bipush          17
        //   177: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   180: aload_0         /* this */
        //   181: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   184: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.FOX:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   187: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   190: bipush          18
        //   192: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   195: aload_0         /* this */
        //   196: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   199: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PANDA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   202: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   205: bipush          15
        //   207: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   210: aload_0         /* this */
        //   211: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   214: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PANDA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   217: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   220: bipush          16
        //   222: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   225: aload_0         /* this */
        //   226: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   229: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PANDA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   232: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   235: bipush          17
        //   237: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   240: aload_0         /* this */
        //   241: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   244: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PANDA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   247: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   250: bipush          18
        //   252: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   255: aload_0         /* this */
        //   256: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   259: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PANDA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   262: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   265: bipush          19
        //   267: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   270: aload_0         /* this */
        //   271: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   274: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.PANDA:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   277: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   280: bipush          20
        //   282: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   285: aload_0         /* this */
        //   286: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   289: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.CAT:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   292: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   295: bipush          18
        //   297: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   300: aload_0         /* this */
        //   301: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   304: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.CAT:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   307: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   310: bipush          19
        //   312: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   315: aload_0         /* this */
        //   316: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   319: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.CAT:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   322: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   325: bipush          20
        //   327: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   330: aload_0         /* this */
        //   331: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   334: invokedynamic   BootstrapMethod #2, handle:()Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   339: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   342: aload_0         /* this */
        //   343: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   346: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.AREA_EFFECT_CLOUD:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   349: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   352: bipush          10
        //   354: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.index:(I)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   357: aload_0         /* this */
        //   358: invokedynamic   BootstrapMethod #3, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14;)Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   363: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   366: aload_0         /* this */
        //   367: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   370: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.FIREWORK_ROCKET:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   373: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   376: bipush          8
        //   378: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.index:(I)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   381: invokedynamic   BootstrapMethod #4, handle:()Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   386: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   389: aload_0         /* this */
        //   390: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   393: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.ABSTRACT_ARROW:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   396: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.filterFamily:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   399: bipush          9
        //   401: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.removeIndex:(I)V
        //   404: aload_0         /* this */
        //   405: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   408: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.VILLAGER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   411: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   414: bipush          15
        //   416: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.cancel:(I)V
        //   419: aload_0         /* this */
        //   420: invokedynamic   BootstrapMethod #5, handle:(Lcom/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14;)Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   425: astore_1        /* villagerDataHandler */
        //   426: aload_0         /* this */
        //   427: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   430: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.ZOMBIE_VILLAGER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   433: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   436: bipush          18
        //   438: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.index:(I)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   441: aload_1         /* villagerDataHandler */
        //   442: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   445: aload_0         /* this */
        //   446: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   449: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.VILLAGER:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   452: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   455: bipush          16
        //   457: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.index:(I)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   460: aload_1         /* villagerDataHandler */
        //   461: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   464: aload_0         /* this */
        //   465: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   468: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.ABSTRACT_SKELETON:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   471: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.filterFamily:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   474: bipush          13
        //   476: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.index:(I)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   479: invokedynamic   BootstrapMethod #6, handle:()Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   484: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   487: aload_0         /* this */
        //   488: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   491: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.ZOMBIE:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   494: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.filterFamily:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   497: bipush          13
        //   499: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.index:(I)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   502: invokedynamic   BootstrapMethod #7, handle:()Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   507: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   510: aload_0         /* this */
        //   511: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   514: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.ZOMBIE:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   517: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.filterFamily:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   520: bipush          16
        //   522: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.addIndex:(I)V
        //   525: aload_0         /* this */
        //   526: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   529: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.LIVINGENTITY:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   532: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.filterFamily:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   535: invokedynamic   BootstrapMethod #8, handle:()Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   540: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   543: aload_0         /* this */
        //   544: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   547: bipush          6
        //   549: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.removeIndex:(I)V
        //   552: aload_0         /* this */
        //   553: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   556: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.OCELOT:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   559: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   562: bipush          13
        //   564: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.index:(I)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   567: invokedynamic   BootstrapMethod #9, handle:()Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   572: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   575: aload_0         /* this */
        //   576: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   579: getstatic       com/viaversion/viaversion/api/minecraft/entities/Entity1_14Types.CAT:Lcom/viaversion/viaversion/api/minecraft/entities/Entity1_14Types;
        //   582: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.type:(Lcom/viaversion/viaversion/api/minecraft/entities/EntityType;)Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   585: invokedynamic   BootstrapMethod #10, handle:()Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   590: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   593: aload_0         /* this */
        //   594: invokevirtual   com/viaversion/viabackwards/protocol/protocol1_13_2to1_14/packets/EntityPackets1_14.filter:()Lcom/viaversion/viaversion/rewriter/meta/MetaFilter$Builder;
        //   597: invokedynamic   BootstrapMethod #11, handle:()Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;
        //   602: invokevirtual   com/viaversion/viaversion/rewriter/meta/MetaFilter$Builder.handler:(Lcom/viaversion/viaversion/rewriter/meta/MetaHandler;)V
        //   605: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    public int villagerDataToProfession(final VillagerData data) {
        switch (data.getProfession()) {
            case 1:
            case 10:
            case 13:
            case 14: {
                return 3;
            }
            case 2:
            case 8: {
                return 4;
            }
            case 3:
            case 9: {
                return 1;
            }
            case 4: {
                return 2;
            }
            case 5:
            case 6:
            case 7:
            case 12: {
                return 0;
            }
            default: {
                return 5;
            }
        }
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_14Types.getTypeFromId(typeId);
    }
}
