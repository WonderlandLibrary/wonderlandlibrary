// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.minecraft.WorldIdentifiers;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.storage.InventoryTracker1_16;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.MappingData;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.UUID;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16Types;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.metadata.MetadataRewriter1_16To1_15_2;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;

public class EntityPackets
{
    private static final PacketHandler DIMENSION_HANDLER;
    public static final CompoundTag DIMENSIONS_TAG;
    private static final String[] WORLD_NAMES;
    
    private static CompoundTag createOverworldEntry() {
        final CompoundTag tag = new CompoundTag();
        tag.put("name", new StringTag("minecraft:overworld"));
        tag.put("has_ceiling", new ByteTag((byte)0));
        addSharedOverwaldEntries(tag);
        return tag;
    }
    
    private static CompoundTag createOverworldCavesEntry() {
        final CompoundTag tag = new CompoundTag();
        tag.put("name", new StringTag("minecraft:overworld_caves"));
        tag.put("has_ceiling", new ByteTag((byte)1));
        addSharedOverwaldEntries(tag);
        return tag;
    }
    
    private static void addSharedOverwaldEntries(final CompoundTag tag) {
        tag.put("piglin_safe", new ByteTag((byte)0));
        tag.put("natural", new ByteTag((byte)1));
        tag.put("ambient_light", new FloatTag(0.0f));
        tag.put("infiniburn", new StringTag("minecraft:infiniburn_overworld"));
        tag.put("respawn_anchor_works", new ByteTag((byte)0));
        tag.put("has_skylight", new ByteTag((byte)1));
        tag.put("bed_works", new ByteTag((byte)1));
        tag.put("has_raids", new ByteTag((byte)1));
        tag.put("logical_height", new IntTag(256));
        tag.put("shrunk", new ByteTag((byte)0));
        tag.put("ultrawarm", new ByteTag((byte)0));
    }
    
    private static CompoundTag createNetherEntry() {
        final CompoundTag tag = new CompoundTag();
        tag.put("piglin_safe", new ByteTag((byte)1));
        tag.put("natural", new ByteTag((byte)0));
        tag.put("ambient_light", new FloatTag(0.1f));
        tag.put("infiniburn", new StringTag("minecraft:infiniburn_nether"));
        tag.put("respawn_anchor_works", new ByteTag((byte)1));
        tag.put("has_skylight", new ByteTag((byte)0));
        tag.put("bed_works", new ByteTag((byte)0));
        tag.put("fixed_time", new LongTag(18000L));
        tag.put("has_raids", new ByteTag((byte)0));
        tag.put("name", new StringTag("minecraft:the_nether"));
        tag.put("logical_height", new IntTag(128));
        tag.put("shrunk", new ByteTag((byte)1));
        tag.put("ultrawarm", new ByteTag((byte)1));
        tag.put("has_ceiling", new ByteTag((byte)1));
        return tag;
    }
    
    private static CompoundTag createEndEntry() {
        final CompoundTag tag = new CompoundTag();
        tag.put("piglin_safe", new ByteTag((byte)0));
        tag.put("natural", new ByteTag((byte)0));
        tag.put("ambient_light", new FloatTag(0.0f));
        tag.put("infiniburn", new StringTag("minecraft:infiniburn_end"));
        tag.put("respawn_anchor_works", new ByteTag((byte)0));
        tag.put("has_skylight", new ByteTag((byte)0));
        tag.put("bed_works", new ByteTag((byte)0));
        tag.put("fixed_time", new LongTag(6000L));
        tag.put("has_raids", new ByteTag((byte)1));
        tag.put("name", new StringTag("minecraft:the_end"));
        tag.put("logical_height", new IntTag(256));
        tag.put("shrunk", new ByteTag((byte)0));
        tag.put("ultrawarm", new ByteTag((byte)0));
        tag.put("has_ceiling", new ByteTag((byte)0));
        return tag;
    }
    
    public static void register(final Protocol1_16To1_15_2 protocol) {
        final MetadataRewriter1_16To1_15_2 metadataRewriter = protocol.get(MetadataRewriter1_16To1_15_2.class);
        ((Protocol<ClientboundPackets1_15, ClientboundPackets1_16, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.SPAWN_GLOBAL_ENTITY, ClientboundPackets1_16.SPAWN_ENTITY, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int entityId;
                final byte type;
                this.handler(wrapper -> {
                    entityId = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    type = wrapper.read((Type<Byte>)Type.BYTE);
                    if (type != 1) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.user().getEntityTracker(Protocol1_16To1_15_2.class).addEntity(entityId, Entity1_16Types.LIGHTNING_BOLT);
                        wrapper.write(Type.UUID, UUID.randomUUID());
                        wrapper.write(Type.VAR_INT, Entity1_16Types.LIGHTNING_BOLT.getId());
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.passthrough((Type<Object>)Type.DOUBLE);
                        wrapper.write(Type.BYTE, (Byte)0);
                        wrapper.write(Type.BYTE, (Byte)0);
                        wrapper.write(Type.INT, 0);
                        wrapper.write(Type.SHORT, (Short)0);
                        wrapper.write(Type.SHORT, (Short)0);
                        wrapper.write(Type.SHORT, (Short)0);
                    }
                });
            }
        });
        metadataRewriter.registerTrackerWithData(ClientboundPackets1_15.SPAWN_ENTITY, Entity1_16Types.FALLING_BLOCK);
        metadataRewriter.registerTracker(ClientboundPackets1_15.SPAWN_MOB);
        metadataRewriter.registerTracker(ClientboundPackets1_15.SPAWN_PLAYER, Entity1_16Types.PLAYER);
        metadataRewriter.registerMetadataRewriter(ClientboundPackets1_15.ENTITY_METADATA, Types1_14.METADATA_LIST, Types1_16.METADATA_LIST);
        metadataRewriter.registerRemoveEntities(ClientboundPackets1_15.DESTROY_ENTITIES);
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(EntityPackets.DIMENSION_HANDLER);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                final String levelType;
                this.handler(wrapper -> {
                    wrapper.write(Type.BYTE, (Byte)(-1));
                    levelType = wrapper.read(Type.STRING);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, levelType.equals("flat"));
                    wrapper.write(Type.BOOLEAN, true);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.UNSIGNED_BYTE);
                this.handler(wrapper -> {
                    wrapper.write(Type.BYTE, (Byte)(-1));
                    wrapper.write(Type.STRING_ARRAY, EntityPackets.WORLD_NAMES);
                    wrapper.write(Type.NBT, EntityPackets.DIMENSIONS_TAG);
                    return;
                });
                this.handler(EntityPackets.DIMENSION_HANDLER);
                this.map(Type.LONG);
                this.map(Type.UNSIGNED_BYTE);
                final String type;
                this.handler(wrapper -> {
                    wrapper.user().getEntityTracker(Protocol1_16To1_15_2.class).addEntity(wrapper.get((Type<Integer>)Type.INT, 0), Entity1_16Types.PLAYER);
                    type = wrapper.read(Type.STRING);
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    wrapper.write(Type.BOOLEAN, false);
                    wrapper.write(Type.BOOLEAN, type.equals("flat"));
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_15, C2, S1, S2>)protocol).registerClientbound(ClientboundPackets1_15.ENTITY_PROPERTIES, new PacketRemapper() {
            @Override
            public void registerMap() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_0         /* this */
                //     2: getfield        com/viaversion/viaversion/protocols/protocol1_16to1_15_2/packets/EntityPackets$4.val$protocol:Lcom/viaversion/viaversion/protocols/protocol1_16to1_15_2/Protocol1_16To1_15_2;
                //     5: invokedynamic   BootstrapMethod #0, handle:(Lcom/viaversion/viaversion/protocols/protocol1_16to1_15_2/Protocol1_16To1_15_2;)Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;
                //    10: invokevirtual   com/viaversion/viaversion/protocols/protocol1_16to1_15_2/packets/EntityPackets$4.handler:(Lcom/viaversion/viaversion/api/protocol/remapper/PacketHandler;)V
                //    13: return         
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
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_16>)protocol).registerServerbound(ServerboundPackets1_16.ANIMATION, new PacketRemapper() {
            @Override
            public void registerMap() {
                final InventoryTracker1_16 inventoryTracker;
                this.handler(wrapper -> {
                    inventoryTracker = wrapper.user().get(InventoryTracker1_16.class);
                    if (inventoryTracker.getInventory() != -1) {
                        wrapper.cancel();
                    }
                });
            }
        });
    }
    
    static {
        WorldIdentifiers map;
        final WorldIdentifiers userMap;
        final int dimension;
        String dimensionName = null;
        String outputName = null;
        DIMENSION_HANDLER = (wrapper -> {
            map = Via.getConfig().get1_16WorldNamesMap();
            userMap = wrapper.user().get(WorldIdentifiers.class);
            if (userMap != null) {
                map = userMap;
            }
            dimension = wrapper.read((Type<Integer>)Type.INT);
            switch (dimension) {
                case -1: {
                    dimensionName = "minecraft:the_nether";
                    outputName = map.nether();
                    break;
                }
                case 0: {
                    dimensionName = "minecraft:overworld";
                    outputName = map.overworld();
                    break;
                }
                case 1: {
                    dimensionName = "minecraft:the_end";
                    outputName = map.end();
                    break;
                }
                default: {
                    Via.getPlatform().getLogger().warning("Invalid dimension id: " + dimension);
                    dimensionName = "minecraft:overworld";
                    outputName = map.overworld();
                    break;
                }
            }
            wrapper.write(Type.STRING, dimensionName);
            wrapper.write(Type.STRING, outputName);
            return;
        });
        DIMENSIONS_TAG = new CompoundTag();
        WORLD_NAMES = new String[] { "minecraft:overworld", "minecraft:the_nether", "minecraft:the_end" };
        final ListTag list = new ListTag(CompoundTag.class);
        list.add(createOverworldEntry());
        list.add(createOverworldCavesEntry());
        list.add(createNetherEntry());
        list.add(createEndEntry());
        EntityPackets.DIMENSIONS_TAG.put("dimension", list);
    }
}
