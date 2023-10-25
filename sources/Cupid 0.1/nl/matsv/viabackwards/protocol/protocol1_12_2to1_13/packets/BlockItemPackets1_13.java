package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.EnchantmentRewriter;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.FlowerPotHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.BlockIdData;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.SpawnEggRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.opennbt.conversion.ConverterRegistry;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class BlockItemPackets1_13 extends ItemRewriter<Protocol1_12_2To1_13> {
  private final Map<String, String> enchantmentMappings = new HashMap<>();
  
  private final String extraNbtTag;
  
  public BlockItemPackets1_13(Protocol1_12_2To1_13 protocol) {
    super((BackwardsProtocol)protocol, null);
    this.extraNbtTag = "VB|" + protocol.getClass().getSimpleName() + "|2";
  }
  
  public static boolean isDamageable(int id) {
    return ((id >= 256 && id <= 259) || id == 261 || (id >= 267 && id <= 279) || (id >= 283 && id <= 286) || (id >= 290 && id <= 294) || (id >= 298 && id <= 317) || id == 346 || id == 359 || id == 398 || id == 442 || id == 443);
  }
  
  protected void registerPackets() {
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.COOLDOWN, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int itemId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int oldId = ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getItemMappings().get(itemId);
                    if (oldId != -1) {
                      Optional<String> eggEntityId = SpawnEggRewriter.getEntityId(oldId);
                      if (eggEntityId.isPresent()) {
                        itemId = 25100288;
                      } else {
                        itemId = oldId >> 4 << 16 | oldId & 0xF;
                      } 
                    } 
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(itemId));
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.BLOCK_ACTION, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map(Type.UNSIGNED_BYTE);
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int blockId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    if (blockId == 73) {
                      blockId = 25;
                    } else if (blockId == 99) {
                      blockId = 33;
                    } else if (blockId == 92) {
                      blockId = 29;
                    } else if (blockId == 142) {
                      blockId = 54;
                    } else if (blockId == 305) {
                      blockId = 146;
                    } else if (blockId == 249) {
                      blockId = 130;
                    } else if (blockId == 257) {
                      blockId = 138;
                    } else if (blockId == 140) {
                      blockId = 52;
                    } else if (blockId == 472) {
                      blockId = 209;
                    } else if (blockId >= 483 && blockId <= 498) {
                      blockId = blockId - 483 + 219;
                    } 
                    wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(blockId));
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.BLOCK_ENTITY_DATA, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map(Type.UNSIGNED_BYTE);
            map(Type.NBT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    BackwardsBlockEntityProvider provider = (BackwardsBlockEntityProvider)Via.getManager().getProviders().get(BackwardsBlockEntityProvider.class);
                    if (((Short)wrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue() == 5)
                      wrapper.cancel(); 
                    wrapper.set(Type.NBT, 0, provider
                        .transform(wrapper
                          .user(), (Position)wrapper
                          .get(Type.POSITION, 0), (CompoundTag)wrapper
                          .get(Type.NBT, 0)));
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.UNLOAD_CHUNK, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int chunkMinX = ((Integer)wrapper.passthrough(Type.INT)).intValue() << 4;
                    int chunkMinZ = ((Integer)wrapper.passthrough(Type.INT)).intValue() << 4;
                    int chunkMaxX = chunkMinX + 15;
                    int chunkMaxZ = chunkMinZ + 15;
                    BackwardsBlockStorage blockStorage = (BackwardsBlockStorage)wrapper.user().get(BackwardsBlockStorage.class);
                    blockStorage.getBlocks().entrySet().removeIf(entry -> {
                          Position position = (Position)entry.getKey();
                          return (position.getX() >= chunkMinX && position.getZ() >= chunkMinZ && position.getX() <= chunkMaxX && position.getZ() <= chunkMaxZ);
                        });
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.BLOCK_CHANGE, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int blockState = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    Position position = (Position)wrapper.get(Type.POSITION, 0);
                    BackwardsBlockStorage storage = (BackwardsBlockStorage)wrapper.user().get(BackwardsBlockStorage.class);
                    storage.checkAndStore(position, blockState);
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(blockState)));
                    BlockItemPackets1_13.flowerPotSpecialTreatment(wrapper.user(), blockState, position);
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.MULTI_BLOCK_CHANGE, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.INT);
            map(Type.BLOCK_CHANGE_RECORD_ARRAY);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    BackwardsBlockStorage storage = (BackwardsBlockStorage)wrapper.user().get(BackwardsBlockStorage.class);
                    for (BlockChangeRecord record : (BlockChangeRecord[])wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0)) {
                      int chunkX = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                      int chunkZ = ((Integer)wrapper.get(Type.INT, 1)).intValue();
                      int block = record.getBlockId();
                      Position position = new Position(record.getSectionX() + chunkX * 16, record.getY(), record.getSectionZ() + chunkZ * 16);
                      storage.checkAndStore(position, block);
                      BlockItemPackets1_13.flowerPotSpecialTreatment(wrapper.user(), block, position);
                      record.setBlockId(((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(block));
                    } 
                  }
                });
          }
        });
    final ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.WINDOW_ITEMS, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map(Type.FLAT_ITEM_ARRAY, Type.ITEM_ARRAY);
            handler(itemRewriter.itemArrayHandler(Type.ITEM_ARRAY));
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SET_SLOT, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map(Type.FLAT_ITEM, Type.ITEM);
            handler(itemRewriter.itemToClientHandler(Type.ITEM));
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.CHUNK_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
                  Chunk1_9_3_4Type type_old = new Chunk1_9_3_4Type(clientWorld);
                  Chunk1_13Type type = new Chunk1_13Type(clientWorld);
                  Chunk chunk = (Chunk)wrapper.read((Type)type);
                  BackwardsBlockEntityProvider provider = (BackwardsBlockEntityProvider)Via.getManager().getProviders().get(BackwardsBlockEntityProvider.class);
                  BackwardsBlockStorage storage = (BackwardsBlockStorage)wrapper.user().get(BackwardsBlockStorage.class);
                  for (CompoundTag tag : chunk.getBlockEntities()) {
                    Tag idTag = tag.get("id");
                    if (idTag == null)
                      continue; 
                    String id = (String)idTag.getValue();
                    if (!provider.isHandled(id))
                      continue; 
                    int sectionIndex = ((Integer)tag.get("y").getValue()).intValue() >> 4;
                    ChunkSection section = chunk.getSections()[sectionIndex];
                    int x = ((Integer)tag.get("x").getValue()).intValue();
                    int y = ((Integer)tag.get("y").getValue()).intValue();
                    int z = ((Integer)tag.get("z").getValue()).intValue();
                    Position position = new Position(x, (short)y, z);
                    int block = section.getFlatBlock(x & 0xF, y & 0xF, z & 0xF);
                    storage.checkAndStore(position, block);
                    provider.transform(wrapper.user(), position, tag);
                  } 
                  int i;
                  for (i = 0; i < (chunk.getSections()).length; i++) {
                    ChunkSection section = chunk.getSections()[i];
                    if (section != null) {
                      for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                          for (int x = 0; x < 16; x++) {
                            int block = section.getFlatBlock(x, y, z);
                            if (FlowerPotHandler.isFlowah(block)) {
                              Position pos = new Position(x + (chunk.getX() << 4), (short)(y + (i << 4)), z + (chunk.getZ() << 4));
                              storage.checkAndStore(pos, block);
                              CompoundTag nbt = provider.transform(wrapper.user(), pos, "minecraft:flower_pot");
                              chunk.getBlockEntities().add(nbt);
                            } 
                          } 
                        } 
                      } 
                      for (int p = 0; p < section.getPaletteSize(); p++) {
                        int old = section.getPaletteEntry(p);
                        if (old != 0) {
                          int oldId = ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(old);
                          section.setPaletteEntry(p, oldId);
                        } 
                      } 
                    } 
                  } 
                  if (chunk.isBiomeData())
                    for (i = 0; i < 256; i++) {
                      int biome = chunk.getBiomeData()[i];
                      int newId = -1;
                      switch (biome) {
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                          newId = 9;
                          break;
                        case 47:
                        case 48:
                        case 49:
                          newId = 24;
                          break;
                        case 50:
                          newId = 10;
                          break;
                        case 44:
                        case 45:
                        case 46:
                          newId = 0;
                          break;
                      } 
                      if (newId != -1)
                        chunk.getBiomeData()[i] = newId; 
                    }  
                  wrapper.write((Type)type_old, chunk);
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.EFFECT, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.POSITION);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int id = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                    int data = ((Integer)wrapper.get(Type.INT, 1)).intValue();
                    if (id == 1010) {
                      wrapper.set(Type.INT, 1, Integer.valueOf(((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getItemMappings().get(data) >> 4));
                    } else if (id == 2001) {
                      data = ((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getNewBlockStateId(data);
                      int blockId = data >> 4;
                      int blockData = data & 0xF;
                      wrapper.set(Type.INT, 1, Integer.valueOf(blockId & 0xFFF | blockData << 12));
                    } 
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.MAP_DATA, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int iconCount = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    for (int i = 0; i < iconCount; i++) {
                      int type = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                      byte x = ((Byte)wrapper.read(Type.BYTE)).byteValue();
                      byte z = ((Byte)wrapper.read(Type.BYTE)).byteValue();
                      byte direction = ((Byte)wrapper.read(Type.BYTE)).byteValue();
                      if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                        wrapper.read(Type.COMPONENT); 
                      if (type > 9) {
                        wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue() - 1));
                      } else {
                        wrapper.write(Type.BYTE, Byte.valueOf((byte)(type << 4 | direction & 0xF)));
                        wrapper.write(Type.BYTE, Byte.valueOf(x));
                        wrapper.write(Type.BYTE, Byte.valueOf(z));
                      } 
                    } 
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.ENTITY_EQUIPMENT, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            map(Type.FLAT_ITEM, Type.ITEM);
            handler(itemRewriter.itemToClientHandler(Type.ITEM));
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.WINDOW_PROPERTY, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            handler(wrapper -> {
                  short property = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue();
                  if (property >= 4 && property <= 6) {
                    short oldId = ((Short)wrapper.get((Type)Type.SHORT, 1)).shortValue();
                    wrapper.set((Type)Type.SHORT, 1, Short.valueOf((short)((Protocol1_12_2To1_13)BlockItemPackets1_13.this.protocol).getMappingData().getEnchantmentMappings().getNewId(oldId)));
                  } 
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.CREATIVE_INVENTORY_ACTION, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.SHORT);
            map(Type.ITEM, Type.FLAT_ITEM);
            handler(itemRewriter.itemToServerHandler(Type.FLAT_ITEM));
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.CLICK_WINDOW, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.SHORT);
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map((Type)Type.VAR_INT);
            map(Type.ITEM, Type.FLAT_ITEM);
            handler(itemRewriter.itemToServerHandler(Type.FLAT_ITEM));
          }
        });
  }
  
  protected void registerRewrites() {
    this.enchantmentMappings.put("minecraft:loyalty", "§7Loyalty");
    this.enchantmentMappings.put("minecraft:impaling", "§7Impaling");
    this.enchantmentMappings.put("minecraft:riptide", "§7Riptide");
    this.enchantmentMappings.put("minecraft:channeling", "§7Channeling");
  }
  
  public Item handleItemToClient(Item item) {
    if (item == null)
      return null; 
    int originalId = item.getIdentifier();
    Integer rawId = null;
    boolean gotRawIdFromTag = false;
    CompoundTag tag = item.getTag();
    Tag originalIdTag;
    if (tag != null && (originalIdTag = tag.remove(this.extraNbtTag)) != null) {
      rawId = (Integer)originalIdTag.getValue();
      gotRawIdFromTag = true;
    } 
    if (rawId == null) {
      super.handleItemToClient(item);
      if (item.getIdentifier() == -1) {
        if (originalId == 362) {
          rawId = Integer.valueOf(15007744);
        } else {
          if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug())
            ViaBackwards.getPlatform().getLogger().warning("Failed to get 1.12 item for " + originalId); 
          rawId = Integer.valueOf(65536);
        } 
      } else {
        if (tag == null)
          tag = item.getTag(); 
        rawId = Integer.valueOf(itemIdToRaw(item.getIdentifier(), item, tag));
      } 
    } 
    item.setIdentifier(rawId.intValue() >> 16);
    item.setData((short)(rawId.intValue() & 0xFFFF));
    if (tag != null) {
      if (isDamageable(item.getIdentifier())) {
        Tag damageTag = tag.remove("Damage");
        if (!gotRawIdFromTag && damageTag instanceof IntTag)
          item.setData((short)((Integer)damageTag.getValue()).intValue()); 
      } 
      if (item.getIdentifier() == 358) {
        Tag mapTag = tag.remove("map");
        if (!gotRawIdFromTag && mapTag instanceof IntTag)
          item.setData((short)((Integer)mapTag.getValue()).intValue()); 
      } 
      invertShieldAndBannerId(item, tag);
      CompoundTag display = (CompoundTag)tag.get("display");
      if (display != null) {
        StringTag name = (StringTag)display.get("Name");
        if (name instanceof StringTag) {
          display.put((Tag)new StringTag(this.extraNbtTag + "|Name", name.getValue()));
          name.setValue(ChatRewriter.jsonTextToLegacy(name.getValue()));
        } 
      } 
      rewriteEnchantmentsToClient(tag, false);
      rewriteEnchantmentsToClient(tag, true);
      rewriteCanPlaceToClient(tag, "CanPlaceOn");
      rewriteCanPlaceToClient(tag, "CanDestroy");
    } 
    return item;
  }
  
  private int itemIdToRaw(int oldId, Item item, CompoundTag tag) {
    Optional<String> eggEntityId = SpawnEggRewriter.getEntityId(oldId);
    if (eggEntityId.isPresent()) {
      if (tag == null)
        item.setTag(tag = new CompoundTag("tag")); 
      if (!tag.contains("EntityTag")) {
        CompoundTag entityTag = new CompoundTag("EntityTag");
        entityTag.put((Tag)new StringTag("id", eggEntityId.get()));
        tag.put((Tag)entityTag);
      } 
      return 25100288;
    } 
    return oldId >> 4 << 16 | oldId & 0xF;
  }
  
  private void rewriteCanPlaceToClient(CompoundTag tag, String tagName) {
    if (!(tag.get(tagName) instanceof ListTag))
      return; 
    ListTag blockTag = (ListTag)tag.get(tagName);
    if (blockTag == null)
      return; 
    ListTag newCanPlaceOn = new ListTag(tagName, StringTag.class);
    tag.put(ConverterRegistry.convertToTag(this.extraNbtTag + "|" + tagName, ConverterRegistry.convertToValue((Tag)blockTag)));
    for (Tag oldTag : blockTag) {
      Object value = oldTag.getValue();
      String[] newValues = (value instanceof String) ? (String[])BlockIdData.fallbackReverseMapping.get(((String)value).replace("minecraft:", "")) : null;
      if (newValues != null) {
        for (String newValue : newValues)
          newCanPlaceOn.add((Tag)new StringTag("", newValue)); 
        continue;
      } 
      newCanPlaceOn.add(oldTag);
    } 
    tag.put((Tag)newCanPlaceOn);
  }
  
  private void rewriteEnchantmentsToClient(CompoundTag tag, boolean storedEnch) {
    String key = storedEnch ? "StoredEnchantments" : "Enchantments";
    ListTag enchantments = (ListTag)tag.get(key);
    if (enchantments == null)
      return; 
    ListTag noMapped = new ListTag(this.extraNbtTag + "|" + key, CompoundTag.class);
    ListTag newEnchantments = new ListTag(storedEnch ? key : "ench", CompoundTag.class);
    List<Tag> lore = new ArrayList<>();
    boolean hasValidEnchants = false;
    for (Tag enchantmentEntryTag : enchantments.clone()) {
      CompoundTag enchantmentEntry = (CompoundTag)enchantmentEntryTag;
      String newId = (String)enchantmentEntry.get("id").getValue();
      Number levelValue = (Number)enchantmentEntry.get("lvl").getValue();
      int intValue = levelValue.intValue();
      short level = (intValue < 32767) ? levelValue.shortValue() : Short.MAX_VALUE;
      String mappedEnchantmentId = this.enchantmentMappings.get(newId);
      if (mappedEnchantmentId != null) {
        lore.add(new StringTag("", mappedEnchantmentId + " " + EnchantmentRewriter.getRomanNumber(level)));
        noMapped.add((Tag)enchantmentEntry);
        continue;
      } 
      if (!newId.isEmpty()) {
        Short oldId = (Short)Protocol1_13To1_12_2.MAPPINGS.getOldEnchantmentsIds().inverse().get(newId);
        if (oldId == null) {
          if (!newId.startsWith("viaversion:legacy/")) {
            noMapped.add((Tag)enchantmentEntry);
            if (ViaBackwards.getConfig().addCustomEnchantsToLore()) {
              String name = newId;
              int index = name.indexOf(':') + 1;
              if (index != 0 && index != name.length())
                name = name.substring(index); 
              name = "§7" + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase(Locale.ENGLISH);
              lore.add(new StringTag("", name + " " + EnchantmentRewriter.getRomanNumber(level)));
            } 
            if (Via.getManager().isDebug())
              ViaBackwards.getPlatform().getLogger().warning("Found unknown enchant: " + newId); 
            continue;
          } 
          oldId = Short.valueOf(newId.substring(18));
        } 
        if (level != 0)
          hasValidEnchants = true; 
        CompoundTag newEntry = new CompoundTag("");
        newEntry.put((Tag)new ShortTag("id", oldId.shortValue()));
        newEntry.put((Tag)new ShortTag("lvl", level));
        newEnchantments.add((Tag)newEntry);
      } 
    } 
    if (!storedEnch && !hasValidEnchants) {
      IntTag hideFlags = (IntTag)tag.get("HideFlags");
      if (hideFlags == null) {
        hideFlags = new IntTag("HideFlags");
        tag.put((Tag)new ByteTag(this.extraNbtTag + "|DummyEnchant"));
      } else {
        tag.put((Tag)new IntTag(this.extraNbtTag + "|OldHideFlags", hideFlags.getValue().intValue()));
      } 
      if (newEnchantments.size() == 0) {
        CompoundTag enchEntry = new CompoundTag("");
        enchEntry.put((Tag)new ShortTag("id", (short)0));
        enchEntry.put((Tag)new ShortTag("lvl", (short)0));
        newEnchantments.add((Tag)enchEntry);
      } 
      int value = hideFlags.getValue().intValue() | 0x1;
      hideFlags.setValue(value);
      tag.put((Tag)hideFlags);
    } 
    if (noMapped.size() != 0) {
      tag.put((Tag)noMapped);
      if (!lore.isEmpty()) {
        CompoundTag display = (CompoundTag)tag.get("display");
        if (display == null)
          tag.put((Tag)(display = new CompoundTag("display"))); 
        ListTag loreTag = (ListTag)display.get("Lore");
        if (loreTag == null) {
          display.put((Tag)(loreTag = new ListTag("Lore", StringTag.class)));
          tag.put((Tag)new ByteTag(this.extraNbtTag + "|DummyLore"));
        } else if (loreTag.size() != 0) {
          ListTag oldLore = new ListTag(this.extraNbtTag + "|OldLore", StringTag.class);
          for (Tag value : loreTag)
            oldLore.add(value.clone()); 
          tag.put((Tag)oldLore);
          lore.addAll(loreTag.getValue());
        } 
        loreTag.setValue(lore);
      } 
    } 
    tag.remove("Enchantments");
    tag.put((Tag)newEnchantments);
  }
  
  public Item handleItemToServer(Item item) {
    if (item == null)
      return null; 
    CompoundTag tag = item.getTag();
    int originalId = item.getIdentifier() << 16 | item.getData() & 0xFFFF;
    int rawId = item.getIdentifier() << 4 | item.getData() & 0xF;
    if (isDamageable(item.getIdentifier())) {
      if (tag == null)
        item.setTag(tag = new CompoundTag("tag")); 
      tag.put((Tag)new IntTag("Damage", item.getData()));
    } 
    if (item.getIdentifier() == 358) {
      if (tag == null)
        item.setTag(tag = new CompoundTag("tag")); 
      tag.put((Tag)new IntTag("map", item.getData()));
    } 
    if (tag != null) {
      invertShieldAndBannerId(item, tag);
      Tag display = tag.get("display");
      if (display instanceof CompoundTag) {
        CompoundTag displayTag = (CompoundTag)display;
        StringTag name = (StringTag)displayTag.get("Name");
        if (name instanceof StringTag) {
          StringTag via = (StringTag)displayTag.remove(this.extraNbtTag + "|Name");
          name.setValue((via != null) ? via.getValue() : ChatRewriter.legacyTextToJson(name.getValue()).toString());
        } 
      } 
      rewriteEnchantmentsToServer(tag, false);
      rewriteEnchantmentsToServer(tag, true);
      rewriteCanPlaceToServer(tag, "CanPlaceOn");
      rewriteCanPlaceToServer(tag, "CanDestroy");
      if (item.getIdentifier() == 383) {
        CompoundTag entityTag = (CompoundTag)tag.get("EntityTag");
        StringTag stringTag;
        if (entityTag != null && (stringTag = (StringTag)entityTag.get("id")) != null) {
          rawId = SpawnEggRewriter.getSpawnEggId(stringTag.getValue());
          if (rawId == -1) {
            rawId = 25100288;
          } else {
            entityTag.remove("id");
            if (entityTag.isEmpty())
              tag.remove("EntityTag"); 
          } 
        } else {
          rawId = 25100288;
        } 
      } 
      if (tag.isEmpty())
        item.setTag(tag = null); 
    } 
    int identifier = item.getIdentifier();
    item.setIdentifier(rawId);
    super.handleItemToServer(item);
    if (item.getIdentifier() != rawId && item.getIdentifier() != -1)
      return item; 
    item.setIdentifier(identifier);
    int newId = -1;
    if (!((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().containsKey(rawId)) {
      if (!isDamageable(item.getIdentifier()) && item.getIdentifier() != 358) {
        if (tag == null)
          item.setTag(tag = new CompoundTag("tag")); 
        tag.put((Tag)new IntTag(this.extraNbtTag, originalId));
      } 
      if (item.getIdentifier() == 229) {
        newId = 362;
      } else if (item.getIdentifier() == 31 && item.getData() == 0) {
        rawId = 512;
      } else if (((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().containsKey(rawId & 0xFFFFFFF0)) {
        rawId &= 0xFFFFFFF0;
      } else {
        if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug())
          ViaBackwards.getPlatform().getLogger().warning("Failed to get 1.13 item for " + item.getIdentifier()); 
        rawId = 16;
      } 
    } 
    if (newId == -1)
      newId = ((Protocol1_12_2To1_13)this.protocol).getMappingData().getItemMappings().inverse().get(rawId); 
    item.setIdentifier(newId);
    item.setData((short)0);
    return item;
  }
  
  private void rewriteCanPlaceToServer(CompoundTag tag, String tagName) {
    if (!(tag.get(tagName) instanceof ListTag))
      return; 
    ListTag blockTag = (ListTag)tag.remove(this.extraNbtTag + "|" + tagName);
    if (blockTag != null) {
      tag.put(ConverterRegistry.convertToTag(tagName, ConverterRegistry.convertToValue((Tag)blockTag)));
    } else if ((blockTag = (ListTag)tag.get(tagName)) != null) {
      ListTag newCanPlaceOn = new ListTag(tagName, StringTag.class);
      for (Tag oldTag : blockTag) {
        Object value = oldTag.getValue();
        String oldId = value.toString().replace("minecraft:", "");
        String numberConverted = (String)BlockIdData.numberIdToString.get(Ints.tryParse(oldId));
        if (numberConverted != null)
          oldId = numberConverted; 
        String lowerCaseId = oldId.toLowerCase(Locale.ROOT);
        String[] newValues = (String[])BlockIdData.blockIdMapping.get(lowerCaseId);
        if (newValues != null) {
          for (String newValue : newValues)
            newCanPlaceOn.add((Tag)new StringTag("", newValue)); 
          continue;
        } 
        newCanPlaceOn.add((Tag)new StringTag("", lowerCaseId));
      } 
      tag.put((Tag)newCanPlaceOn);
    } 
  }
  
  private void rewriteEnchantmentsToServer(CompoundTag tag, boolean storedEnch) {
    String key = storedEnch ? "StoredEnchantments" : "Enchantments";
    ListTag enchantments = (ListTag)tag.get(storedEnch ? key : "ench");
    if (enchantments == null)
      return; 
    ListTag newEnchantments = new ListTag(key, CompoundTag.class);
    boolean dummyEnchant = false;
    if (!storedEnch) {
      IntTag hideFlags = (IntTag)tag.remove(this.extraNbtTag + "|OldHideFlags");
      if (hideFlags != null) {
        tag.put((Tag)new IntTag("HideFlags", hideFlags.getValue().intValue()));
        dummyEnchant = true;
      } else if (tag.remove(this.extraNbtTag + "|DummyEnchant") != null) {
        tag.remove("HideFlags");
        dummyEnchant = true;
      } 
    } 
    for (Tag enchEntry : enchantments) {
      CompoundTag enchantmentEntry = new CompoundTag("");
      short oldId = ((Number)((CompoundTag)enchEntry).get("id").getValue()).shortValue();
      short level = ((Number)((CompoundTag)enchEntry).get("lvl").getValue()).shortValue();
      if (dummyEnchant && oldId == 0 && level == 0)
        continue; 
      String newId = (String)Protocol1_13To1_12_2.MAPPINGS.getOldEnchantmentsIds().get(Short.valueOf(oldId));
      if (newId == null)
        newId = "viaversion:legacy/" + oldId; 
      enchantmentEntry.put((Tag)new StringTag("id", newId));
      enchantmentEntry.put((Tag)new ShortTag("lvl", level));
      newEnchantments.add((Tag)enchantmentEntry);
    } 
    ListTag noMapped = (ListTag)tag.remove(this.extraNbtTag + "|Enchantments");
    if (noMapped != null)
      for (Tag value : noMapped)
        newEnchantments.add(value);  
    CompoundTag display = (CompoundTag)tag.get("display");
    if (display == null)
      tag.put((Tag)(display = new CompoundTag("display"))); 
    ListTag oldLore = (ListTag)tag.remove(this.extraNbtTag + "|OldLore");
    if (oldLore != null) {
      ListTag lore = (ListTag)display.get("Lore");
      if (lore == null)
        tag.put((Tag)(lore = new ListTag("Lore"))); 
      lore.setValue(oldLore.getValue());
    } else if (tag.remove(this.extraNbtTag + "|DummyLore") != null) {
      display.remove("Lore");
      if (display.isEmpty())
        tag.remove("display"); 
    } 
    if (!storedEnch)
      tag.remove("ench"); 
    tag.put((Tag)newEnchantments);
  }
  
  private void invertShieldAndBannerId(Item item, CompoundTag tag) {
    if (item.getIdentifier() != 442 && item.getIdentifier() != 425)
      return; 
    Tag blockEntityTag = tag.get("BlockEntityTag");
    if (!(blockEntityTag instanceof CompoundTag))
      return; 
    CompoundTag blockEntityCompoundTag = (CompoundTag)blockEntityTag;
    Tag base = blockEntityCompoundTag.get("Base");
    if (base instanceof IntTag) {
      IntTag baseTag = (IntTag)base;
      baseTag.setValue(15 - baseTag.getValue().intValue());
    } 
    Tag patterns = blockEntityCompoundTag.get("Patterns");
    if (patterns instanceof ListTag) {
      ListTag patternsTag = (ListTag)patterns;
      for (Tag pattern : patternsTag) {
        if (!(pattern instanceof CompoundTag))
          continue; 
        IntTag colorTag = (IntTag)((CompoundTag)pattern).get("Color");
        colorTag.setValue(15 - colorTag.getValue().intValue());
      } 
    } 
  }
  
  private static void flowerPotSpecialTreatment(UserConnection user, int blockState, Position position) throws Exception {
    if (FlowerPotHandler.isFlowah(blockState)) {
      BackwardsBlockEntityProvider beProvider = (BackwardsBlockEntityProvider)Via.getManager().getProviders().get(BackwardsBlockEntityProvider.class);
      CompoundTag nbt = beProvider.transform(user, position, "minecraft:flower_pot");
      PacketWrapper blockUpdateRemove = new PacketWrapper(11, null, user);
      blockUpdateRemove.write(Type.POSITION, position);
      blockUpdateRemove.write((Type)Type.VAR_INT, Integer.valueOf(0));
      blockUpdateRemove.send(Protocol1_12_2To1_13.class, true);
      PacketWrapper blockCreate = new PacketWrapper(11, null, user);
      blockCreate.write(Type.POSITION, position);
      blockCreate.write((Type)Type.VAR_INT, Integer.valueOf(Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(blockState)));
      blockCreate.send(Protocol1_12_2To1_13.class, true);
      PacketWrapper wrapper = new PacketWrapper(9, null, user);
      wrapper.write(Type.POSITION, position);
      wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)5));
      wrapper.write(Type.NBT, nbt);
      wrapper.send(Protocol1_12_2To1_13.class, true);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\packets\BlockItemPackets1_13.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */