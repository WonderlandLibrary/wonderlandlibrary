// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.ClientboundPackets1_17_1;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.types.Chunk1_17Type;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.chunks.BaseChunk;
import com.viaversion.viaversion.api.minecraft.blockentity.BlockEntity;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import java.util.BitSet;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.types.Chunk1_18Type;
import com.viaversion.viaversion.util.MathUtil;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.data.BlockEntityIds;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.data.ParticleMappings;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.data.RecipeRewriter1_16;
import com.viaversion.viabackwards.protocol.protocol1_17_1to1_18.Protocol1_17_1To1_18;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public final class BlockItemPackets1_18 extends ItemRewriter<Protocol1_17_1To1_18>
{
    public BlockItemPackets1_18(final Protocol1_17_1To1_18 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        new RecipeRewriter1_16(this.protocol).registerDefaultHandler(ClientboundPackets1_18.DECLARE_RECIPES);
        this.registerSetCooldown(ClientboundPackets1_18.COOLDOWN);
        this.registerWindowItems1_17_1(ClientboundPackets1_18.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT, Type.FLAT_VAR_INT_ITEM);
        this.registerSetSlot1_17_1(ClientboundPackets1_18.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        this.registerEntityEquipmentArray(ClientboundPackets1_18.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
        this.registerTradeList(ClientboundPackets1_18.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        this.registerAdvancements(ClientboundPackets1_18.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        this.registerClickWindow1_17_1(ServerboundPackets1_17.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.EFFECT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.POSITION1_14);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    final int data = wrapper.get((Type<Integer>)Type.INT, 1);
                    if (id == 1010) {
                        wrapper.set(Type.INT, 1, ((Protocol1_17_1To1_18)BlockItemPackets1_18.this.protocol).getMappingData().getNewItemId(data));
                    }
                });
            }
        });
        this.registerCreativeInvAction(ServerboundPackets1_17.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.SPAWN_PARTICLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.DOUBLE);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.map(Type.INT);
                this.handler(wrapper -> {
                    final int id = wrapper.get((Type<Integer>)Type.INT, 0);
                    if (id == 3) {
                        final int blockState = wrapper.read((Type<Integer>)Type.VAR_INT);
                        if (blockState == 7786) {
                            wrapper.set(Type.INT, 0, 3);
                        }
                        else {
                            wrapper.set(Type.INT, 0, 2);
                        }
                    }
                    else {
                        final ParticleMappings mappings = ((Protocol1_17_1To1_18)BlockItemPackets1_18.this.protocol).getMappingData().getParticleMappings();
                        if (mappings.isBlockParticle(id)) {
                            final int data = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                            wrapper.set(Type.VAR_INT, 0, ((Protocol1_17_1To1_18)BlockItemPackets1_18.this.protocol).getMappingData().getNewBlockStateId(data));
                        }
                        else if (mappings.isItemParticle(id)) {
                            BlockItemPackets1_18.this.handleItemToClient(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM));
                        }
                        final int newId = ((Protocol1_17_1To1_18)BlockItemPackets1_18.this.protocol).getMappingData().getNewParticleId(id);
                        if (newId != id) {
                            wrapper.set(Type.INT, 0, newId);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14);
                this.handler(wrapper -> {
                    final int id = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final CompoundTag tag = wrapper.read(Type.NBT);
                    final int mappedId = BlockEntityIds.mappedId(id);
                    if (mappedId == -1) {
                        wrapper.cancel();
                    }
                    else {
                        final String identifier = ((Protocol1_17_1To1_18)BlockItemPackets1_18.this.protocol).getMappingData().blockEntities().get(id);
                        if (identifier == null) {
                            wrapper.cancel();
                        }
                        else {
                            CompoundTag compoundTag = null;
                            if (tag == null) {
                                new(com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag.class)();
                                new CompoundTag();
                            }
                            else {
                                compoundTag = tag;
                            }
                            final CompoundTag newTag = compoundTag;
                            final Position pos = wrapper.get(Type.POSITION1_14, 0);
                            new StringTag("minecraft:" + identifier);
                            final StringTag tag2;
                            final Object o2;
                            final String tagName;
                            ((CompoundTag)o2).put(tagName, tag2);
                            newTag.put("x", new IntTag(pos.x()));
                            newTag.put("y", new IntTag(pos.y()));
                            newTag.put("z", new IntTag(pos.z()));
                            BlockItemPackets1_18.this.handleSpawner(id, newTag);
                            wrapper.write(Type.UNSIGNED_BYTE, (short)mappedId);
                            wrapper.write(Type.NBT, newTag);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_18.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final EntityTracker tracker = ((Protocol1_17_1To1_18)BlockItemPackets1_18.this.protocol).getEntityRewriter().tracker(wrapper.user());
                    final Chunk1_18Type chunkType = new Chunk1_18Type(tracker.currentWorldSectionHeight(), MathUtil.ceilLog2(((Protocol1_17_1To1_18)BlockItemPackets1_18.this.protocol).getMappingData().getBlockStateMappings().mappedSize()), MathUtil.ceilLog2(tracker.biomesSent()));
                    final Chunk oldChunk = wrapper.read((Type<Chunk>)chunkType);
                    final ChunkSection[] sections = oldChunk.getSections();
                    final BitSet mask = new BitSet(oldChunk.getSections().length);
                    final int[] biomeData = new int[sections.length * 64];
                    int biomeIndex = 0;
                    for (int j = 0; j < sections.length; ++j) {
                        final ChunkSection section = sections[j];
                        final DataPalette biomePalette = section.palette(PaletteType.BIOMES);
                        for (int i = 0; i < 64; ++i) {
                            biomeIndex++;
                            final Object o;
                            final int n;
                            o[n] = biomePalette.idAt(i);
                        }
                        if (section.getNonAirBlocksCount() == 0) {
                            sections[j] = null;
                        }
                        else {
                            mask.set(j);
                        }
                    }
                    final ArrayList<CompoundTag> blockEntityTags = new ArrayList<CompoundTag>(oldChunk.blockEntities().size());
                    oldChunk.blockEntities().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final BlockEntity blockEntity = iterator.next();
                        final String id = ((Protocol1_17_1To1_18)BlockItemPackets1_18.this.protocol).getMappingData().blockEntities().get(blockEntity.typeId());
                        if (id == null) {
                            continue;
                        }
                        else {
                            CompoundTag tag;
                            if (blockEntity.tag() != null) {
                                tag = blockEntity.tag();
                                BlockItemPackets1_18.this.handleSpawner(blockEntity.typeId(), tag);
                            }
                            else {
                                tag = new CompoundTag();
                            }
                            blockEntityTags.add(tag);
                            tag.put("x", new IntTag((oldChunk.getX() << 4) + blockEntity.sectionX()));
                            tag.put("y", new IntTag(blockEntity.y()));
                            tag.put("z", new IntTag((oldChunk.getZ() << 4) + blockEntity.sectionZ()));
                            new StringTag("minecraft:" + id);
                            final StringTag tag2;
                            final Object o2;
                            final String tagName;
                            ((CompoundTag)o2).put(tagName, tag2);
                        }
                    }
                    final Chunk chunk = new BaseChunk(oldChunk.getX(), oldChunk.getZ(), true, false, mask, oldChunk.getSections(), biomeData, oldChunk.getHeightMap(), blockEntityTags);
                    wrapper.write(new Chunk1_17Type(tracker.currentWorldSectionHeight()), chunk);
                    final PacketWrapper lightPacket = wrapper.create(ClientboundPackets1_17_1.UPDATE_LIGHT);
                    lightPacket.write(Type.VAR_INT, chunk.getX());
                    lightPacket.write(Type.VAR_INT, chunk.getZ());
                    lightPacket.write((Type<Object>)Type.BOOLEAN, wrapper.read((Type<T>)Type.BOOLEAN));
                    lightPacket.write(Type.LONG_ARRAY_PRIMITIVE, (long[])(Object)wrapper.read((Type<T>)Type.LONG_ARRAY_PRIMITIVE));
                    lightPacket.write(Type.LONG_ARRAY_PRIMITIVE, (long[])(Object)wrapper.read((Type<T>)Type.LONG_ARRAY_PRIMITIVE));
                    lightPacket.write(Type.LONG_ARRAY_PRIMITIVE, (long[])(Object)wrapper.read((Type<T>)Type.LONG_ARRAY_PRIMITIVE));
                    lightPacket.write(Type.LONG_ARRAY_PRIMITIVE, (long[])(Object)wrapper.read((Type<T>)Type.LONG_ARRAY_PRIMITIVE));
                    final int skyLightLength = wrapper.read((Type<Integer>)Type.VAR_INT);
                    lightPacket.write(Type.VAR_INT, skyLightLength);
                    for (int k = 0; k < skyLightLength; ++k) {
                        lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, (byte[])(Object)wrapper.read((Type<T>)Type.BYTE_ARRAY_PRIMITIVE));
                    }
                    final int blockLightLength = wrapper.read((Type<Integer>)Type.VAR_INT);
                    lightPacket.write(Type.VAR_INT, blockLightLength);
                    for (int l = 0; l < blockLightLength; ++l) {
                        lightPacket.write(Type.BYTE_ARRAY_PRIMITIVE, (byte[])(Object)wrapper.read((Type<T>)Type.BYTE_ARRAY_PRIMITIVE));
                    }
                    lightPacket.send(Protocol1_17_1To1_18.class);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_18, C2, S1, S2>)this.protocol).cancelClientbound(ClientboundPackets1_18.SET_SIMULATION_DISTANCE);
    }
    
    private void handleSpawner(final int typeId, final CompoundTag tag) {
        if (typeId == 8) {
            final CompoundTag spawnData = tag.get("SpawnData");
            final CompoundTag entity;
            if (spawnData != null && (entity = spawnData.get("entity")) != null) {
                tag.put("SpawnData", entity);
            }
        }
    }
}
