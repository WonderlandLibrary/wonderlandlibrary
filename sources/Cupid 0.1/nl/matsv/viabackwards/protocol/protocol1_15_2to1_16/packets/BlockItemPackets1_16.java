package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.EnchantmentRewriter;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.data.MapColorRewriter;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.UUIDIntArrayType;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.packets.InventoryPackets;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import us.myles.ViaVersion.util.CompactArrayUtil;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class BlockItemPackets1_16 extends ItemRewriter<Protocol1_15_2To1_16> {
  private EnchantmentRewriter enchantmentRewriter;
  
  public BlockItemPackets1_16(Protocol1_15_2To1_16 protocol, TranslatableRewriter translatableRewriter) {
    super((BackwardsProtocol)protocol, translatableRewriter);
  }
  
  protected void registerPackets() {
    ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
    BlockRewriter blockRewriter = new BlockRewriter((Protocol)this.protocol, Type.POSITION1_14);
    final RecipeRewriter1_14 recipeRewriter = new RecipeRewriter1_14((Protocol)this.protocol, this::handleItemToClient);
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.DECLARE_RECIPES, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                  int newSize = size;
                  for (int i = 0; i < size; i++) {
                    String originalType = (String)wrapper.read(Type.STRING);
                    String type = originalType.replace("minecraft:", "");
                    if (type.equals("smithing")) {
                      newSize--;
                      wrapper.read(Type.STRING);
                      wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                      wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                      wrapper.read(Type.FLAT_VAR_INT_ITEM);
                    } else {
                      wrapper.write(Type.STRING, originalType);
                      String id = (String)wrapper.passthrough(Type.STRING);
                      recipeRewriter.handle(wrapper, type);
                    } 
                  } 
                  wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(newSize));
                });
          }
        });
    itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_16.COOLDOWN);
    itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_16.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
    itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_16.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerTradeList((ClientboundPacketType)ClientboundPackets1_16.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerAdvancements((ClientboundPacketType)ClientboundPackets1_16.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
    blockRewriter.registerAcknowledgePlayerDigging((ClientboundPacketType)ClientboundPackets1_16.ACKNOWLEDGE_PLAYER_DIGGING);
    blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_16.BLOCK_ACTION);
    blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_16.BLOCK_CHANGE);
    blockRewriter.registerMultiBlockChange((ClientboundPacketType)ClientboundPackets1_16.MULTI_BLOCK_CHANGE);
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.ENTITY_EQUIPMENT, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  byte slot;
                  int entityId = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                  List<BlockItemPackets1_16.EquipmentData> equipmentData = new ArrayList<>();
                  do {
                    slot = ((Byte)wrapper.read(Type.BYTE)).byteValue();
                    Item item = BlockItemPackets1_16.this.handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM));
                    int rawSlot = slot & Byte.MAX_VALUE;
                    equipmentData.add(new BlockItemPackets1_16.EquipmentData(rawSlot, item));
                  } while ((slot & Byte.MIN_VALUE) != 0);
                  BlockItemPackets1_16.EquipmentData firstData = equipmentData.get(0);
                  wrapper.write((Type)Type.VAR_INT, Integer.valueOf(firstData.slot));
                  wrapper.write(Type.FLAT_VAR_INT_ITEM, firstData.item);
                  for (int i = 1; i < equipmentData.size(); i++) {
                    PacketWrapper equipmentPacket = wrapper.create(ClientboundPackets1_15.ENTITY_EQUIPMENT.ordinal());
                    BlockItemPackets1_16.EquipmentData data = equipmentData.get(i);
                    equipmentPacket.write((Type)Type.VAR_INT, Integer.valueOf(entityId));
                    equipmentPacket.write((Type)Type.VAR_INT, Integer.valueOf(data.slot));
                    equipmentPacket.write(Type.FLAT_VAR_INT_ITEM, data.item);
                    equipmentPacket.send(Protocol1_15_2To1_16.class);
                  } 
                });
          }
        });
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.UPDATE_LIGHT, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            map(Type.BOOLEAN, Type.NOTHING);
          }
        });
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.CHUNK_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_16Type());
                  wrapper.write((Type)new Chunk1_15Type(), chunk);
                  for (int i = 0; i < (chunk.getSections()).length; i++) {
                    ChunkSection section = chunk.getSections()[i];
                    if (section != null)
                      for (int j = 0; j < section.getPaletteSize(); j++) {
                        int old = section.getPaletteEntry(j);
                        section.setPaletteEntry(j, ((Protocol1_15_2To1_16)BlockItemPackets1_16.this.protocol).getMappingData().getNewBlockStateId(old));
                      }  
                  } 
                  CompoundTag heightMaps = chunk.getHeightMap();
                  for (Tag heightMapTag : heightMaps) {
                    LongArrayTag heightMap = (LongArrayTag)heightMapTag;
                    int[] heightMapData = new int[256];
                    CompactArrayUtil.iterateCompactArrayWithPadding(9, heightMapData.length, heightMap.getValue(), ());
                    heightMap.setValue(CompactArrayUtil.createCompactArray(9, heightMapData.length, ()));
                  } 
                  if (chunk.isBiomeData())
                    for (int j = 0; j < 1024; j++) {
                      int biome = chunk.getBiomeData()[j];
                      switch (biome) {
                        case 170:
                        case 171:
                        case 172:
                        case 173:
                          chunk.getBiomeData()[j] = 8;
                          break;
                      } 
                    }  
                  if (chunk.getBlockEntities() == null)
                    return; 
                  for (CompoundTag blockEntity : chunk.getBlockEntities())
                    BlockItemPackets1_16.this.handleBlockEntity(blockEntity); 
                });
          }
        });
    blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_16.EFFECT, 1010, 2001);
    itemRewriter.registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_16.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.WINDOW_PROPERTY, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            handler(wrapper -> {
                  short property = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue();
                  if (property >= 4 && property <= 6) {
                    short enchantmentId = ((Short)wrapper.get((Type)Type.SHORT, 1)).shortValue();
                    if (enchantmentId > 11) {
                      enchantmentId = (short)(enchantmentId - 1);
                      wrapper.set((Type)Type.SHORT, 1, Short.valueOf(enchantmentId));
                    } else if (enchantmentId == 11) {
                      wrapper.set((Type)Type.SHORT, 1, Short.valueOf((short)9));
                    } 
                  } 
                });
          }
        });
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.MAP_DATA, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            map(Type.BOOLEAN);
            map(Type.BOOLEAN);
            handler(wrapper -> {
                  int iconCount = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                  for (int i = 0; i < iconCount; i++) {
                    wrapper.passthrough((Type)Type.VAR_INT);
                    wrapper.passthrough(Type.BYTE);
                    wrapper.passthrough(Type.BYTE);
                    wrapper.passthrough(Type.BYTE);
                    if (((Boolean)wrapper.passthrough(Type.BOOLEAN)).booleanValue())
                      wrapper.passthrough(Type.COMPONENT); 
                  } 
                  short columns = ((Short)wrapper.passthrough(Type.UNSIGNED_BYTE)).shortValue();
                  if (columns < 1)
                    return; 
                  wrapper.passthrough(Type.UNSIGNED_BYTE);
                  wrapper.passthrough(Type.UNSIGNED_BYTE);
                  wrapper.passthrough(Type.UNSIGNED_BYTE);
                  byte[] data = (byte[])wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                  for (int j = 0; j < data.length; j++) {
                    int color = data[j] & 0xFF;
                    int mappedColor = MapColorRewriter.getMappedColor(color);
                    if (mappedColor != -1)
                      data[j] = (byte)mappedColor; 
                  } 
                });
          }
        });
    ((Protocol1_15_2To1_16)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.BLOCK_ENTITY_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  Position position = (Position)wrapper.passthrough(Type.POSITION1_14);
                  short action = ((Short)wrapper.passthrough(Type.UNSIGNED_BYTE)).shortValue();
                  CompoundTag tag = (CompoundTag)wrapper.passthrough(Type.NBT);
                  BlockItemPackets1_16.this.handleBlockEntity(tag);
                });
          }
        });
    itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
    ((Protocol1_15_2To1_16)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_14.EDIT_BOOK, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> BlockItemPackets1_16.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
          }
        });
  }
  
  private void handleBlockEntity(CompoundTag tag) {
    StringTag idTag = (StringTag)tag.get("id");
    if (idTag == null)
      return; 
    String id = idTag.getValue();
    if (id.equals("minecraft:conduit")) {
      Tag targetUuidTag = tag.remove("Target");
      if (!(targetUuidTag instanceof IntArrayTag))
        return; 
      UUID targetUuid = UUIDIntArrayType.uuidFromIntArray((int[])targetUuidTag.getValue());
      tag.put((Tag)new StringTag("target_uuid", targetUuid.toString()));
    } else if (id.equals("minecraft:skull")) {
      Tag skullOwnerTag = tag.remove("SkullOwner");
      if (!(skullOwnerTag instanceof CompoundTag))
        return; 
      CompoundTag skullOwnerCompoundTag = (CompoundTag)skullOwnerTag;
      Tag ownerUuidTag = skullOwnerCompoundTag.remove("Id");
      if (ownerUuidTag instanceof IntArrayTag) {
        UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])ownerUuidTag.getValue());
        skullOwnerCompoundTag.put((Tag)new StringTag("Id", ownerUuid.toString()));
      } 
      CompoundTag ownerTag = new CompoundTag("Owner");
      for (Tag t : skullOwnerCompoundTag)
        ownerTag.put(t); 
      tag.put((Tag)ownerTag);
    } 
  }
  
  protected void registerRewrites() {
    this.enchantmentRewriter = new EnchantmentRewriter(this.nbtTagName);
    this.enchantmentRewriter.registerEnchantment("minecraft:soul_speed", "§7Soul Speed");
  }
  
  public Item handleItemToClient(Item item) {
    if (item == null)
      return null; 
    super.handleItemToClient(item);
    CompoundTag tag = item.getTag();
    if (item.getIdentifier() == 771 && tag != null) {
      Tag ownerTag = tag.get("SkullOwner");
      if (ownerTag instanceof CompoundTag) {
        CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
        Tag idTag = ownerCompundTag.get("Id");
        if (idTag instanceof IntArrayTag) {
          UUID ownerUuid = UUIDIntArrayType.uuidFromIntArray((int[])idTag.getValue());
          ownerCompundTag.put((Tag)new StringTag("Id", ownerUuid.toString()));
        } 
      } 
    } 
    InventoryPackets.newToOldAttributes(item);
    this.enchantmentRewriter.handleToClient(item);
    return item;
  }
  
  public Item handleItemToServer(Item item) {
    if (item == null)
      return null; 
    int identifier = item.getIdentifier();
    super.handleItemToServer(item);
    CompoundTag tag = item.getTag();
    if (identifier == 771 && tag != null) {
      Tag ownerTag = tag.get("SkullOwner");
      if (ownerTag instanceof CompoundTag) {
        CompoundTag ownerCompundTag = (CompoundTag)ownerTag;
        Tag idTag = ownerCompundTag.get("Id");
        if (idTag instanceof StringTag) {
          UUID ownerUuid = UUID.fromString((String)idTag.getValue());
          ownerCompundTag.put((Tag)new IntArrayTag("Id", UUIDIntArrayType.uuidToIntArray(ownerUuid)));
        } 
      } 
    } 
    InventoryPackets.oldToNewAttributes(item);
    this.enchantmentRewriter.handleToServer(item);
    return item;
  }
  
  private static final class EquipmentData {
    private final int slot;
    
    private final Item item;
    
    private EquipmentData(int slot, Item item) {
      this.slot = slot;
      this.item = item;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_15_2to1_16\packets\BlockItemPackets1_16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */