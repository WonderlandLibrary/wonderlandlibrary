// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.packets.InventoryPackets;
import java.util.UUID;
import java.util.Map;
import com.viaversion.viaversion.api.type.types.UUIDIntArrayType;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viabackwards.api.rewriters.MapColorRewriter;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.data.MapColorRewrites;
import com.viaversion.viaversion.api.type.types.ShortType;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.storage.BiomeStorage;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.util.CompactArrayUtil;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import java.util.List;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.BlockRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viabackwards.api.rewriters.EnchantmentRewriter;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import com.viaversion.viabackwards.api.rewriters.ItemRewriter;

public class BlockItemPackets1_16 extends ItemRewriter<Protocol1_15_2To1_16>
{
    private EnchantmentRewriter enchantmentRewriter;
    
    public BlockItemPackets1_16(final Protocol1_15_2To1_16 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        final BlockRewriter blockRewriter = new BlockRewriter(this.protocol, Type.POSITION1_14);
        final RecipeRewriter1_14 recipeRewriter = new RecipeRewriter1_14(this.protocol);
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.DECLARE_RECIPES, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$recipeRewriter = recipeRewriter;
                    int newSize;
                    for (int size = newSize = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        final String originalType = wrapper.read(Type.STRING);
                        final String type = originalType.replace("minecraft:", "");
                        if (type.equals("smithing")) {
                            --newSize;
                            wrapper.read(Type.STRING);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM);
                        }
                        else {
                            wrapper.write(Type.STRING, originalType);
                            final String id = wrapper.passthrough(Type.STRING);
                            recipeRewriter.handle(wrapper, type);
                        }
                    }
                    wrapper.set(Type.VAR_INT, 0, newSize);
                });
            }
        });
        this.registerSetCooldown(ClientboundPackets1_16.COOLDOWN);
        this.registerWindowItems(ClientboundPackets1_16.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
        this.registerSetSlot(ClientboundPackets1_16.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
        this.registerTradeList(ClientboundPackets1_16.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
        this.registerAdvancements(ClientboundPackets1_16.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
        blockRewriter.registerAcknowledgePlayerDigging(ClientboundPackets1_16.ACKNOWLEDGE_PLAYER_DIGGING);
        blockRewriter.registerBlockAction(ClientboundPackets1_16.BLOCK_ACTION);
        blockRewriter.registerBlockChange(ClientboundPackets1_16.BLOCK_CHANGE);
        blockRewriter.registerMultiBlockChange(ClientboundPackets1_16.MULTI_BLOCK_CHANGE);
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.ENTITY_EQUIPMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final int entityId = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    final ArrayList equipmentData = new ArrayList<EquipmentData>();
                    byte slot;
                    do {
                        slot = wrapper.read((Type<Byte>)Type.BYTE);
                        final Item item = BlockItemPackets1_16.this.handleItemToClient(wrapper.read(Type.FLAT_VAR_INT_ITEM));
                        final int rawSlot = slot & 0x7F;
                        equipmentData.add(new EquipmentData(rawSlot, item));
                    } while ((slot & 0xFFFFFF80) != 0x0);
                    final EquipmentData firstData = (EquipmentData)equipmentData.get(0);
                    wrapper.write(Type.VAR_INT, firstData.slot);
                    wrapper.write(Type.FLAT_VAR_INT_ITEM, firstData.item);
                    for (int i = 1; i < equipmentData.size(); ++i) {
                        final PacketWrapper equipmentPacket = wrapper.create(ClientboundPackets1_15.ENTITY_EQUIPMENT);
                        final EquipmentData data = (EquipmentData)equipmentData.get(i);
                        equipmentPacket.write(Type.VAR_INT, entityId);
                        equipmentPacket.write(Type.VAR_INT, data.slot);
                        equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, data.item);
                        equipmentPacket.send(Protocol1_15_2To1_16.class);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.UPDATE_LIGHT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.CHUNK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Chunk chunk = wrapper.read((Type<Chunk>)new Chunk1_16Type());
                    wrapper.write(new Chunk1_15Type(), chunk);
                    int i;
                    for (i = 0; i < chunk.getSections().length; ++i) {
                        final ChunkSection section = chunk.getSections()[i];
                        if (section != null) {
                            for (int j = 0; j < section.getPaletteSize(); ++j) {
                                final int old = section.getPaletteEntry(j);
                                section.setPaletteEntry(j, ((Protocol1_15_2To1_16)BlockItemPackets1_16.this.protocol).getMappingData().getNewBlockStateId(old));
                            }
                        }
                    }
                    final CompoundTag heightMaps = chunk.getHeightMap();
                    heightMaps.values().iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag heightMapTag = iterator.next();
                        final LongArrayTag heightMap = (LongArrayTag)heightMapTag;
                        final int[] heightMapData = new int[256];
                        CompactArrayUtil.iterateCompactArrayWithPadding(9, heightMapData.length, heightMap.getValue(), (i, v) -> heightMapData[i] = v);
                        heightMap.setValue(CompactArrayUtil.createCompactArray(9, heightMapData.length, i -> heightMapData[i]));
                    }
                    if (chunk.isBiomeData()) {
                        if (wrapper.user().getProtocolInfo().getServerProtocolVersion() >= ProtocolVersion.v1_16_2.getVersion()) {
                            final BiomeStorage biomeStorage = wrapper.user().get(BiomeStorage.class);
                            for (int k = 0; k < 1024; ++k) {
                                final int biome = chunk.getBiomeData()[k];
                                int legacyBiome = biomeStorage.legacyBiome(biome);
                                if (legacyBiome == -1) {
                                    ViaBackwards.getPlatform().getLogger().warning("Biome sent that does not exist in the biome registry: " + biome);
                                    legacyBiome = 1;
                                }
                                chunk.getBiomeData()[k] = legacyBiome;
                            }
                        }
                        else {
                            for (int l = 0; l < 1024; ++l) {
                                final int biome2 = chunk.getBiomeData()[l];
                                switch (biome2) {
                                    case 170:
                                    case 171:
                                    case 172:
                                    case 173: {
                                        chunk.getBiomeData()[l] = 8;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (chunk.getBlockEntities() != null) {
                        chunk.getBlockEntities().iterator();
                        final Iterator iterator2;
                        while (iterator2.hasNext()) {
                            final CompoundTag blockEntity = iterator2.next();
                            BlockItemPackets1_16.this.handleBlockEntity(blockEntity);
                        }
                    }
                });
            }
        });
        blockRewriter.registerEffect(ClientboundPackets1_16.EFFECT, 1010, 2001);
        this.registerSpawnParticle(ClientboundPackets1_16.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.WINDOW_PROPERTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.SHORT);
                this.map(Type.SHORT);
                this.handler(wrapper -> {
                    final short property = wrapper.get((Type<Short>)Type.SHORT, 0);
                    if (property >= 4 && property <= 6) {
                        final short enchantmentId = wrapper.get((Type<Short>)Type.SHORT, 1);
                        if (enchantmentId > 11) {
                            final ShortType short1 = Type.SHORT;
                            final short enchantmentId2 = (short)(enchantmentId - 1);
                            final int n;
                            wrapper.set(short1, n, enchantmentId2);
                        }
                        else if (enchantmentId == 11) {
                            wrapper.set(Type.SHORT, 1, (Short)9);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.MAP_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.BYTE);
                this.map(Type.BOOLEAN);
                this.map(Type.BOOLEAN);
                this.handler(MapColorRewriter.getRewriteHandler(MapColorRewrites::getMappedColor));
            }
        });
        ((AbstractProtocol<ClientboundPackets1_16, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_16.BLOCK_ENTITY_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Position position = wrapper.passthrough(Type.POSITION1_14);
                    final short action = wrapper.passthrough((Type<Short>)Type.UNSIGNED_BYTE);
                    final CompoundTag tag = wrapper.passthrough(Type.NBT);
                    BlockItemPackets1_16.this.handleBlockEntity(tag);
                });
            }
        });
        this.registerClickWindow(ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
        this.registerCreativeInvAction(ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_14>)this.protocol).registerServerbound(ServerboundPackets1_14.EDIT_BOOK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> BlockItemPackets1_16.this.handleItemToServer(wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
            }
        });
    }
    
    private void handleBlockEntity(final CompoundTag tag) {
        final StringTag idTag = tag.get("id");
        if (idTag == null) {
            return;
        }
        final String id = idTag.getValue();
        if (id.equals("minecraft:conduit")) {
            final Tag targetUuidTag = tag.remove("Target");
            if (!(targetUuidTag instanceof IntArrayTag)) {
                return;
            }
            final UUID targetUuid = UUIDIntArrayType.uuidFromIntArray((int[])targetUuidTag.getValue());
            tag.put("target_uuid", new StringTag(targetUuid.toString()));
        }
        else if (id.equals("minecraft:skull")) {
            final Tag skullOwnerTag = tag.remove("SkullOwner");
            if (!(skullOwnerTag instanceof CompoundTag)) {
                return;
            }
            final CompoundTag skullOwnerCompoundTag = (CompoundTag)skullOwnerTag;
            final Tag ownerUuidTag = skullOwnerCompoundTag.remove("Id");
            if (ownerUuidTag instanceof IntArrayTag) {
                final UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])ownerUuidTag.getValue());
                skullOwnerCompoundTag.put("Id", new StringTag(ownerUuid.toString()));
            }
            final CompoundTag ownerTag = new CompoundTag();
            for (final Map.Entry<String, Tag> entry : skullOwnerCompoundTag) {
                ownerTag.put(entry.getKey(), entry.getValue());
            }
            tag.put("Owner", ownerTag);
        }
    }
    
    @Override
    protected void registerRewrites() {
        (this.enchantmentRewriter = new EnchantmentRewriter(this)).registerEnchantment("minecraft:soul_speed", "§7Soul Speed");
    }
    
    @Override
    public Item handleItemToClient(final Item item) {
        if (item == null) {
            return null;
        }
        super.handleItemToClient(item);
        final CompoundTag tag = item.tag();
        if (item.identifier() == 771 && tag != null) {
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof IntArrayTag) {
                    final UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])idTag.getValue());
                    ownerCompundTag.put("Id", new StringTag(ownerUuid.toString()));
                }
            }
        }
        InventoryPackets.newToOldAttributes(item);
        this.enchantmentRewriter.handleToClient(item);
        return item;
    }
    
    @Override
    public Item handleItemToServer(final Item item) {
        if (item == null) {
            return null;
        }
        final int identifier = item.identifier();
        super.handleItemToServer(item);
        final CompoundTag tag = item.tag();
        if (identifier == 771 && tag != null) {
            final Tag ownerTag = tag.get("SkullOwner");
            if (ownerTag instanceof CompoundTag) {
                final CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
                final Tag idTag = ownerCompundTag.get("Id");
                if (idTag instanceof StringTag) {
                    final UUID ownerUuid = UUID.fromString((String)idTag.getValue());
                    ownerCompundTag.put("Id", new IntArrayTag(UUIDIntArrayType.uuidToIntArray(ownerUuid)));
                }
            }
        }
        InventoryPackets.oldToNewAttributes(item);
        this.enchantmentRewriter.handleToServer(item);
        return item;
    }
    
    private static final class EquipmentData
    {
        private final int slot;
        private final Item item;
        
        private EquipmentData(final int slot, final Item item) {
            this.slot = slot;
            this.item = item;
        }
    }
}
