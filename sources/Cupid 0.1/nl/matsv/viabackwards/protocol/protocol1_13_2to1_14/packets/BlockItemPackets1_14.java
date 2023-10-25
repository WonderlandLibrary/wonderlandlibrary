package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.EnchantmentRewriter;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.entities.Entity1_14Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.Environment;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_13_2;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.rewriters.RecipeRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.RecipeRewriter1_13_2;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.types.Chunk1_13Type;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.opennbt.conversion.ConverterRegistry;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class BlockItemPackets1_14 extends ItemRewriter<Protocol1_13_2To1_14> {
  private EnchantmentRewriter enchantmentRewriter;
  
  public BlockItemPackets1_14(Protocol1_13_2To1_14 protocol, TranslatableRewriter translatableRewriter) {
    super((BackwardsProtocol)protocol, translatableRewriter);
  }
  
  protected void registerPackets() {
    ((Protocol1_13_2To1_14)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_13.EDIT_BOOK, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> BlockItemPackets1_14.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.OPEN_WINDOW, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int windowId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)windowId));
                    int type = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    String stringType = null;
                    String containerTitle = null;
                    int slotSize = 0;
                    if (type < 6) {
                      if (type == 2)
                        containerTitle = "Barrel"; 
                      stringType = "minecraft:container";
                      slotSize = (type + 1) * 9;
                    } else {
                      switch (type) {
                        case 11:
                          stringType = "minecraft:crafting_table";
                          break;
                        case 9:
                        case 13:
                        case 14:
                        case 20:
                          if (type == 9) {
                            containerTitle = "Blast Furnace";
                          } else if (type == 20) {
                            containerTitle = "Smoker";
                          } else if (type == 14) {
                            containerTitle = "Grindstone";
                          } 
                          stringType = "minecraft:furnace";
                          slotSize = 3;
                          break;
                        case 6:
                          stringType = "minecraft:dropper";
                          slotSize = 9;
                          break;
                        case 12:
                          stringType = "minecraft:enchanting_table";
                          break;
                        case 10:
                          stringType = "minecraft:brewing_stand";
                          slotSize = 5;
                          break;
                        case 18:
                          stringType = "minecraft:villager";
                          break;
                        case 8:
                          stringType = "minecraft:beacon";
                          slotSize = 1;
                          break;
                        case 7:
                        case 21:
                          if (type == 21)
                            containerTitle = "Cartography Table"; 
                          stringType = "minecraft:anvil";
                          break;
                        case 15:
                          stringType = "minecraft:hopper";
                          slotSize = 5;
                          break;
                        case 19:
                          stringType = "minecraft:shulker_box";
                          slotSize = 27;
                          break;
                      } 
                    } 
                    if (stringType == null) {
                      ViaBackwards.getPlatform().getLogger().warning("Can't open inventory for 1.13 player! Type: " + type);
                      wrapper.cancel();
                      return;
                    } 
                    wrapper.write(Type.STRING, stringType);
                    JsonElement title = (JsonElement)wrapper.read(Type.COMPONENT);
                    if (containerTitle != null) {
                      JsonObject object;
                      if (title.isJsonObject() && (object = title.getAsJsonObject()).has("translate"))
                        if (type != 2 || object.getAsJsonPrimitive("translate").getAsString().equals("container.barrel"))
                          title = ChatRewriter.legacyTextToJson(containerTitle);  
                    } 
                    wrapper.write(Type.COMPONENT, title);
                    wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)slotSize));
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.OPEN_HORSE_WINDOW, (ClientboundPacketType)ClientboundPackets1_13.OPEN_WINDOW, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    wrapper.passthrough(Type.UNSIGNED_BYTE);
                    wrapper.write(Type.STRING, "EntityHorse");
                    JsonObject object = new JsonObject();
                    object.addProperty("translate", "minecraft.horse");
                    wrapper.write(Type.COMPONENT, object);
                    wrapper.write(Type.UNSIGNED_BYTE, Short.valueOf(((Integer)wrapper.read((Type)Type.VAR_INT)).shortValue()));
                    wrapper.passthrough(Type.INT);
                  }
                });
          }
        });
    final ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
    BlockRewriter blockRewriter = new BlockRewriter((Protocol)this.protocol, Type.POSITION);
    itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_14.COOLDOWN);
    itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_14.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
    itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_14.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerAdvancements((ClientboundPacketType)ClientboundPackets1_14.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.TRADE_LIST, (ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    wrapper.write(Type.STRING, "minecraft:trader_list");
                    int windowId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    wrapper.write(Type.INT, Integer.valueOf(windowId));
                    int size = ((Short)wrapper.passthrough(Type.UNSIGNED_BYTE)).shortValue();
                    for (int i = 0; i < size; i++) {
                      Item input = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                      input = BlockItemPackets1_14.this.handleItemToClient(input);
                      wrapper.write(Type.FLAT_VAR_INT_ITEM, input);
                      Item output = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                      output = BlockItemPackets1_14.this.handleItemToClient(output);
                      wrapper.write(Type.FLAT_VAR_INT_ITEM, output);
                      boolean secondItem = ((Boolean)wrapper.passthrough(Type.BOOLEAN)).booleanValue();
                      if (secondItem) {
                        Item second = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                        second = BlockItemPackets1_14.this.handleItemToClient(second);
                        wrapper.write(Type.FLAT_VAR_INT_ITEM, second);
                      } 
                      wrapper.passthrough(Type.BOOLEAN);
                      wrapper.passthrough(Type.INT);
                      wrapper.passthrough(Type.INT);
                      wrapper.read(Type.INT);
                      wrapper.read(Type.INT);
                      wrapper.read((Type)Type.FLOAT);
                    } 
                    wrapper.read((Type)Type.VAR_INT);
                    wrapper.read((Type)Type.VAR_INT);
                    wrapper.read(Type.BOOLEAN);
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.OPEN_BOOK, (ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    wrapper.write(Type.STRING, "minecraft:book_open");
                    wrapper.passthrough((Type)Type.VAR_INT);
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_EQUIPMENT, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            map(Type.FLAT_VAR_INT_ITEM);
            handler(itemRewriter.itemToClientHandler(Type.FLAT_VAR_INT_ITEM));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int entityId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    EntityType entityType = ((EntityTracker)wrapper.user().get(EntityTracker.class)).get(BlockItemPackets1_14.this.getProtocol()).getEntityType(entityId);
                    if (entityType == null)
                      return; 
                    if (entityType.isOrHasParent((EntityType)Entity1_14Types.EntityType.ABSTRACT_HORSE)) {
                      wrapper.setId(63);
                      wrapper.resetReader();
                      wrapper.passthrough((Type)Type.VAR_INT);
                      wrapper.read((Type)Type.VAR_INT);
                      Item item = (Item)wrapper.read(Type.FLAT_VAR_INT_ITEM);
                      int armorType = (item == null || item.getIdentifier() == 0) ? 0 : (item.getIdentifier() - 726);
                      if (armorType < 0 || armorType > 3) {
                        ViaBackwards.getPlatform().getLogger().warning("Received invalid horse armor: " + item);
                        wrapper.cancel();
                        return;
                      } 
                      List<Metadata> metadataList = new ArrayList<>();
                      metadataList.add(new Metadata(16, (MetaType)MetaType1_13_2.VarInt, Integer.valueOf(armorType)));
                      wrapper.write(Types1_13.METADATA_LIST, metadataList);
                    } 
                  }
                });
          }
        });
    final RecipeRewriter1_13_2 recipeHandler = new RecipeRewriter1_13_2((Protocol)this.protocol, this::handleItemToClient);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.DECLARE_RECIPES, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  private final Set<String> removedTypes = (Set<String>)ImmutableSet.of("crafting_special_suspiciousstew", "blasting", "smoking", "campfire_cooking", "stonecutting");
                  
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int size = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    int deleted = 0;
                    for (int i = 0; i < size; i++) {
                      String type = (String)wrapper.read(Type.STRING);
                      String id = (String)wrapper.read(Type.STRING);
                      type = type.replace("minecraft:", "");
                      if (this.removedTypes.contains(type)) {
                        switch (type) {
                          case "blasting":
                          case "smoking":
                          case "campfire_cooking":
                            wrapper.read(Type.STRING);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM);
                            wrapper.read((Type)Type.FLOAT);
                            wrapper.read((Type)Type.VAR_INT);
                            break;
                          case "stonecutting":
                            wrapper.read(Type.STRING);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM_ARRAY_VAR_INT);
                            wrapper.read(Type.FLAT_VAR_INT_ITEM);
                            break;
                        } 
                        deleted++;
                      } else {
                        wrapper.write(Type.STRING, id);
                        wrapper.write(Type.STRING, type);
                        recipeHandler.handle(wrapper, type);
                      } 
                    } 
                    wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(size - deleted));
                  }
                });
          }
        });
    itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_13.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_13.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.BLOCK_BREAK_ANIMATION, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.POSITION1_14, Type.POSITION);
            map(Type.BYTE);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.BLOCK_ENTITY_DATA, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION1_14, Type.POSITION);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.BLOCK_ACTION, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION1_14, Type.POSITION);
            map(Type.UNSIGNED_BYTE);
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.VAR_INT);
            handler(wrapper -> {
                  int mappedId = ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockId(((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue());
                  if (mappedId == -1) {
                    wrapper.cancel();
                    return;
                  } 
                  wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(mappedId));
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.BLOCK_CHANGE, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION1_14, Type.POSITION);
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int id = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(id)));
                  }
                });
          }
        });
    blockRewriter.registerMultiBlockChange((ClientboundPacketType)ClientboundPackets1_14.MULTI_BLOCK_CHANGE);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.EXPLOSION, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    for (int i = 0; i < 3; i++) {
                      float coord = ((Float)wrapper.get((Type)Type.FLOAT, i)).floatValue();
                      if (coord < 0.0F) {
                        coord = (float)Math.floor(coord);
                        wrapper.set((Type)Type.FLOAT, i, Float.valueOf(coord));
                      } 
                    } 
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.CHUNK_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
                    Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_14Type());
                    wrapper.write((Type)new Chunk1_13Type(clientWorld), chunk);
                    ChunkLightStorage.ChunkLight chunkLight = ((ChunkLightStorage)wrapper.user().get(ChunkLightStorage.class)).getStoredLight(chunk.getX(), chunk.getZ());
                    for (int i = 0; i < (chunk.getSections()).length; i++) {
                      ChunkSection section = chunk.getSections()[i];
                      if (section != null) {
                        if (chunkLight == null) {
                          section.setBlockLight(ChunkLightStorage.FULL_LIGHT);
                          if (clientWorld.getEnvironment() == Environment.NORMAL)
                            section.setSkyLight(ChunkLightStorage.FULL_LIGHT); 
                        } else {
                          byte[] blockLight = chunkLight.getBlockLight()[i];
                          section.setBlockLight((blockLight != null) ? blockLight : ChunkLightStorage.FULL_LIGHT);
                          if (clientWorld.getEnvironment() == Environment.NORMAL) {
                            byte[] skyLight = chunkLight.getSkyLight()[i];
                            section.setSkyLight((skyLight != null) ? skyLight : ChunkLightStorage.FULL_LIGHT);
                          } 
                        } 
                        if (Via.getConfig().isNonFullBlockLightFix() && section.getNonAirBlocksCount() != 0 && section.hasBlockLight())
                          for (int x = 0; x < 16; x++) {
                            for (int y = 0; y < 16; y++) {
                              for (int z = 0; z < 16; z++) {
                                int id = section.getFlatBlock(x, y, z);
                                if (Protocol1_14To1_13_2.MAPPINGS.getNonFullBlocks().contains(id))
                                  section.getBlockLightNibbleArray().set(x, y, z, 0); 
                              } 
                            } 
                          }  
                        for (int j = 0; j < section.getPaletteSize(); j++) {
                          int old = section.getPaletteEntry(j);
                          int newId = ((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(old);
                          section.setPaletteEntry(j, newId);
                        } 
                      } 
                    } 
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.UNLOAD_CHUNK, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int x = ((Integer)wrapper.passthrough(Type.INT)).intValue();
                    int z = ((Integer)wrapper.passthrough(Type.INT)).intValue();
                    ((ChunkLightStorage)wrapper.user().get(ChunkLightStorage.class)).unloadChunk(x, z);
                  }
                });
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.EFFECT, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.POSITION1_14, Type.POSITION);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int id = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                    int data = ((Integer)wrapper.get(Type.INT, 1)).intValue();
                    if (id == 1010) {
                      wrapper.set(Type.INT, 1, Integer.valueOf(((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewItemId(data)));
                    } else if (id == 2001) {
                      wrapper.set(Type.INT, 1, Integer.valueOf(((Protocol1_13_2To1_14)BlockItemPackets1_14.this.protocol).getMappingData().getNewBlockStateId(data)));
                    } 
                  }
                });
          }
        });
    itemRewriter.registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_14.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, (Type)Type.FLOAT);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.MAP_DATA, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map(Type.BYTE);
            map(Type.BOOLEAN);
            map(Type.BOOLEAN, Type.NOTHING);
          }
        });
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.SPAWN_POSITION, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION1_14, Type.POSITION);
          }
        });
  }
  
  protected void registerRewrites() {
    this.enchantmentRewriter = new EnchantmentRewriter(this.nbtTagName, false);
    this.enchantmentRewriter.registerEnchantment("minecraft:multishot", "§7Multishot");
    this.enchantmentRewriter.registerEnchantment("minecraft:quick_charge", "§7Quick Charge");
    this.enchantmentRewriter.registerEnchantment("minecraft:piercing", "§7Piercing");
  }
  
  public Item handleItemToClient(Item item) {
    if (item == null)
      return null; 
    super.handleItemToClient(item);
    CompoundTag tag = item.getTag();
    if (tag != null) {
      if (tag.get("display") instanceof CompoundTag) {
        CompoundTag display = (CompoundTag)tag.get("display");
        if (((CompoundTag)tag.get("display")).get("Lore") instanceof ListTag) {
          ListTag lore = (ListTag)display.get("Lore");
          ListTag via = (ListTag)display.remove(this.nbtTagName + "|Lore");
          if (via != null) {
            display.put(ConverterRegistry.convertToTag("Lore", ConverterRegistry.convertToValue((Tag)via)));
          } else {
            for (Tag loreEntry : lore) {
              if (!(loreEntry instanceof StringTag))
                continue; 
              String value = ((StringTag)loreEntry).getValue();
              if (value != null && !value.isEmpty())
                ((StringTag)loreEntry).setValue(ChatRewriter.jsonTextToLegacy(value)); 
            } 
          } 
        } 
      } 
      this.enchantmentRewriter.handleToClient(item);
    } 
    return item;
  }
  
  public Item handleItemToServer(Item item) {
    if (item == null)
      return null; 
    super.handleItemToServer(item);
    CompoundTag tag = item.getTag();
    if (tag != null) {
      if (tag.get("display") instanceof CompoundTag) {
        CompoundTag display = (CompoundTag)tag.get("display");
        if (display.get("Lore") instanceof ListTag) {
          ListTag lore = (ListTag)display.get("Lore");
          display.put(ConverterRegistry.convertToTag(this.nbtTagName + "|Lore", ConverterRegistry.convertToValue((Tag)lore)));
          for (Tag loreEntry : lore) {
            if (loreEntry instanceof StringTag)
              ((StringTag)loreEntry).setValue(ChatRewriter.legacyTextToJson(((StringTag)loreEntry).getValue()).toString()); 
          } 
        } 
      } 
      this.enchantmentRewriter.handleToServer(item);
    } 
    return item;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_13_2to1_14\packets\BlockItemPackets1_14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */