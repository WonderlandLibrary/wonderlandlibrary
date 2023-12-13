// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16Types;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets.EntityPackets;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.storage.BiomeStorage;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.type.types.version.Types1_16;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_16_2Types;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.google.common.collect.Sets;
import java.util.Set;
import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.Protocol1_16_1To1_16_2;
import com.viaversion.viabackwards.api.rewriters.EntityRewriter;

public class EntityPackets1_16_2 extends EntityRewriter<Protocol1_16_1To1_16_2>
{
    private final Set<String> oldDimensions;
    private boolean warned;
    
    public EntityPackets1_16_2(final Protocol1_16_1To1_16_2 protocol) {
        super(protocol);
        this.oldDimensions = Sets.newHashSet("minecraft:overworld", "minecraft:the_nether", "minecraft:the_end");
    }
    
    @Override
    protected void registerPackets() {
        this.registerTrackerWithData(ClientboundPackets1_16_2.SPAWN_ENTITY, Entity1_16_2Types.FALLING_BLOCK);
        this.registerSpawnTracker(ClientboundPackets1_16_2.SPAWN_MOB);
        this.registerTracker(ClientboundPackets1_16_2.SPAWN_EXPERIENCE_ORB, Entity1_16_2Types.EXPERIENCE_ORB);
        this.registerTracker(ClientboundPackets1_16_2.SPAWN_PAINTING, Entity1_16_2Types.PAINTING);
        this.registerTracker(ClientboundPackets1_16_2.SPAWN_PLAYER, Entity1_16_2Types.PLAYER);
        this.registerRemoveEntities(ClientboundPackets1_16_2.DESTROY_ENTITIES);
        this.registerMetadataRewriter(ClientboundPackets1_16_2.ENTITY_METADATA, Types1_16.METADATA_LIST);
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16_2.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final boolean hardcore = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    short gamemode = wrapper.read((Type<Short>)Type.UNSIGNED_BYTE);
                    if (hardcore) {
                        gamemode |= 0x8;
                    }
                    wrapper.write(Type.UNSIGNED_BYTE, gamemode);
                    return;
                });
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.handler(wrapper -> {
                    final CompoundTag registry = wrapper.read(Type.NBT);
                    if (wrapper.user().getProtocolInfo().getProtocolVersion() <= ProtocolVersion.v1_15_2.getVersion()) {
                        final CompoundTag biomeRegistry = registry.get("minecraft:worldgen/biome");
                        final ListTag biomes = biomeRegistry.get("value");
                        final BiomeStorage biomeStorage = wrapper.user().get(BiomeStorage.class);
                        biomeStorage.clear();
                        biomes.iterator();
                        final Iterator iterator;
                        while (iterator.hasNext()) {
                            final Tag biome = iterator.next();
                            final CompoundTag biomeCompound = (CompoundTag)biome;
                            final StringTag name = biomeCompound.get("name");
                            final NumberTag id = biomeCompound.get("id");
                            biomeStorage.addBiome(name.getValue(), id.asInt());
                        }
                    }
                    else if (!EntityPackets1_16_2.this.warned) {
                        EntityPackets1_16_2.this.warned = true;
                        ViaBackwards.getPlatform().getLogger().warning("1.16 and 1.16.1 clients are only partially supported and may have wrong biomes displayed.");
                    }
                    wrapper.write(Type.NBT, EntityPackets.DIMENSIONS_TAG);
                    final CompoundTag dimensionData = wrapper.read(Type.NBT);
                    wrapper.write(Type.STRING, EntityPackets1_16_2.this.getDimensionFromData(dimensionData));
                    return;
                });
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.handler(wrapper -> {
                    final int maxPlayers = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.write(Type.UNSIGNED_BYTE, (short)Math.max(maxPlayers, 255));
                    return;
                });
                this.handler(EntityRewriterBase.this.getTrackerHandler(Entity1_16_2Types.PLAYER, Type.INT));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16_2, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16_2.RESPAWN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final CompoundTag dimensionData = wrapper.read(Type.NBT);
                    wrapper.write(Type.STRING, EntityPackets1_16_2.this.getDimensionFromData(dimensionData));
                });
            }
        });
    }
    
    private String getDimensionFromData(final CompoundTag dimensionData) {
        final StringTag effectsLocation = dimensionData.get("effects");
        return (effectsLocation != null && this.oldDimensions.contains(effectsLocation.getValue())) ? effectsLocation.getValue() : "minecraft:overworld";
    }
    
    @Override
    protected void registerRewrites() {
        this.registerMetaTypeHandler(Types1_16.META_TYPES.itemType, Types1_16.META_TYPES.blockStateType, Types1_16.META_TYPES.particleType, Types1_16.META_TYPES.optionalComponentType);
        this.mapTypes(Entity1_16_2Types.values(), Entity1_16Types.class);
        this.mapEntityTypeWithData(Entity1_16_2Types.PIGLIN_BRUTE, Entity1_16_2Types.PIGLIN).jsonName();
        this.filter().filterFamily(Entity1_16_2Types.ABSTRACT_PIGLIN).index(15).toIndex(16);
        this.filter().filterFamily(Entity1_16_2Types.ABSTRACT_PIGLIN).index(16).toIndex(15);
    }
    
    @Override
    public EntityType typeFromId(final int typeId) {
        return Entity1_16_2Types.getTypeFromId(typeId);
    }
}
