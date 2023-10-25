package nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets;

import java.util.Arrays;
import java.util.Optional;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.MappedLegacyBlockItem;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.LegacyBlockItemRewriter;
import nl.matsv.viabackwards.api.rewriters.LegacyEnchantmentRewriter;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage.ChestedHorseStorage;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage.WindowTracker;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_11Types;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_11to1_10.EntityIdRewriter;
import us.myles.ViaVersion.protocols.protocol1_9_1_2to1_9_3_4.types.Chunk1_9_3_4Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class BlockItemPackets1_11 extends LegacyBlockItemRewriter<Protocol1_10To1_11> {
  private LegacyEnchantmentRewriter enchantmentRewriter;
  
  public BlockItemPackets1_11(Protocol1_10To1_11 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    final ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SET_SLOT, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map(Type.ITEM);
            handler(itemRewriter.itemToClientHandler(Type.ITEM));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                      Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                      if (!horse.isPresent())
                        return; 
                      ChestedHorseStorage storage = horse.get();
                      int currentSlot = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue();
                      wrapper.set((Type)Type.SHORT, 0, Short.valueOf(Integer.valueOf(currentSlot = BlockItemPackets1_11.this.getNewSlotId(storage, currentSlot)).shortValue()));
                      wrapper.set(Type.ITEM, 0, BlockItemPackets1_11.this.getNewItem(storage, currentSlot, (Item)wrapper.get(Type.ITEM, 0)));
                    } 
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.WINDOW_ITEMS, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map(Type.ITEM_ARRAY);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    Item[] stacks = (Item[])wrapper.get(Type.ITEM_ARRAY, 0);
                    for (int i = 0; i < stacks.length; i++)
                      stacks[i] = BlockItemPackets1_11.this.handleItemToClient(stacks[i]); 
                    if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                      Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                      if (!horse.isPresent())
                        return; 
                      ChestedHorseStorage storage = horse.get();
                      stacks = Arrays.<Item>copyOf(stacks, !storage.isChested() ? 38 : 53);
                      for (int j = stacks.length - 1; j >= 0; j--) {
                        stacks[BlockItemPackets1_11.this.getNewSlotId(storage, j)] = stacks[j];
                        stacks[j] = BlockItemPackets1_11.this.getNewItem(storage, j, stacks[j]);
                      } 
                      wrapper.set(Type.ITEM_ARRAY, 0, stacks);
                    } 
                  }
                });
          }
        });
    itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.PLUGIN_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    if (((String)wrapper.get(Type.STRING, 0)).equalsIgnoreCase("MC|TrList")) {
                      wrapper.passthrough(Type.INT);
                      int size = ((Short)wrapper.passthrough(Type.UNSIGNED_BYTE)).shortValue();
                      for (int i = 0; i < size; i++) {
                        wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                        wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                        boolean secondItem = ((Boolean)wrapper.passthrough(Type.BOOLEAN)).booleanValue();
                        if (secondItem)
                          wrapper.write(Type.ITEM, BlockItemPackets1_11.this.handleItemToClient((Item)wrapper.read(Type.ITEM))); 
                        wrapper.passthrough(Type.BOOLEAN);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                      } 
                    } 
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_9_3.CLICK_WINDOW, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.SHORT);
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map((Type)Type.VAR_INT);
            map(Type.ITEM);
            handler(itemRewriter.itemToServerHandler(Type.ITEM));
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    if (BlockItemPackets1_11.this.isLlama(wrapper.user())) {
                      Optional<ChestedHorseStorage> horse = BlockItemPackets1_11.this.getChestedHorse(wrapper.user());
                      if (!horse.isPresent())
                        return; 
                      ChestedHorseStorage storage = horse.get();
                      int clickSlot = ((Short)wrapper.get((Type)Type.SHORT, 0)).shortValue();
                      int correctSlot = BlockItemPackets1_11.this.getOldSlotId(storage, clickSlot);
                      wrapper.set((Type)Type.SHORT, 0, Short.valueOf(Integer.valueOf(correctSlot).shortValue()));
                    } 
                  }
                });
          }
        });
    itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.CHUNK_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    ClientWorld clientWorld = (ClientWorld)wrapper.user().get(ClientWorld.class);
                    Chunk1_9_3_4Type type = new Chunk1_9_3_4Type(clientWorld);
                    Chunk chunk = (Chunk)wrapper.passthrough((Type)type);
                    BlockItemPackets1_11.this.handleChunk(chunk);
                    for (CompoundTag tag : chunk.getBlockEntities()) {
                      Tag idTag = tag.get("id");
                      if (!(idTag instanceof StringTag))
                        continue; 
                      String id = (String)idTag.getValue();
                      if (id.equals("minecraft:sign"))
                        ((StringTag)idTag).setValue("Sign"); 
                    } 
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.BLOCK_CHANGE, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int idx = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(BlockItemPackets1_11.this.handleBlockID(idx)));
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.MULTI_BLOCK_CHANGE, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.INT);
            map(Type.BLOCK_CHANGE_RECORD_ARRAY);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    for (BlockChangeRecord record : (BlockChangeRecord[])wrapper.get(Type.BLOCK_CHANGE_RECORD_ARRAY, 0))
                      record.setBlockId(BlockItemPackets1_11.this.handleBlockID(record.getBlockId())); 
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.BLOCK_ENTITY_DATA, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map(Type.UNSIGNED_BYTE);
            map(Type.NBT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    if (((Short)wrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue() == 10)
                      wrapper.cancel(); 
                    if (((Short)wrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue() == 1) {
                      CompoundTag tag = (CompoundTag)wrapper.get(Type.NBT, 0);
                      EntityIdRewriter.toClientSpawner(tag, true);
                    } 
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.OPEN_WINDOW, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map(Type.STRING);
            map(Type.COMPONENT);
            map(Type.UNSIGNED_BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int entityId = -1;
                    if (((String)wrapper.get(Type.STRING, 0)).equals("EntityHorse"))
                      entityId = ((Integer)wrapper.passthrough(Type.INT)).intValue(); 
                    String inventory = (String)wrapper.get(Type.STRING, 0);
                    WindowTracker windowTracker = (WindowTracker)wrapper.user().get(WindowTracker.class);
                    windowTracker.setInventory(inventory);
                    windowTracker.setEntityId(entityId);
                    if (BlockItemPackets1_11.this.isLlama(wrapper.user()))
                      wrapper.set(Type.UNSIGNED_BYTE, 1, Short.valueOf((short)17)); 
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.CLOSE_WINDOW, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    WindowTracker windowTracker = (WindowTracker)wrapper.user().get(WindowTracker.class);
                    windowTracker.setInventory(null);
                    windowTracker.setEntityId(-1);
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_9_3.CLOSE_WINDOW, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    WindowTracker windowTracker = (WindowTracker)wrapper.user().get(WindowTracker.class);
                    windowTracker.setInventory(null);
                    windowTracker.setEntityId(-1);
                  }
                });
          }
        });
    ((Protocol1_10To1_11)this.protocol).getEntityPackets().registerMetaHandler().handle(e -> {
          Metadata data = e.getData();
          if (data.getMetaType().getType().equals(Type.ITEM))
            data.setValue(handleItemToClient((Item)data.getValue())); 
          return data;
        });
  }
  
  protected void registerRewrites() {
    MappedLegacyBlockItem data = (MappedLegacyBlockItem)this.replacementData.computeIfAbsent(52, s -> new MappedLegacyBlockItem(52, (short)-1, null, false));
    data.setBlockEntityHandler((b, tag) -> {
          EntityIdRewriter.toClientSpawner(tag, true);
          return tag;
        });
    this.enchantmentRewriter = new LegacyEnchantmentRewriter(this.nbtTagName);
    this.enchantmentRewriter.registerEnchantment(71, "§cCurse of Vanishing");
    this.enchantmentRewriter.registerEnchantment(10, "§cCurse of Binding");
    this.enchantmentRewriter.setHideLevelForEnchants(new int[] { 71, 10 });
  }
  
  public Item handleItemToClient(Item item) {
    if (item == null)
      return null; 
    super.handleItemToClient(item);
    CompoundTag tag = item.getTag();
    if (tag == null)
      return item; 
    EntityIdRewriter.toClientItem(item, true);
    if (tag.get("ench") instanceof us.myles.viaversion.libs.opennbt.tag.builtin.ListTag)
      this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, false); 
    if (tag.get("StoredEnchantments") instanceof us.myles.viaversion.libs.opennbt.tag.builtin.ListTag)
      this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, true); 
    return item;
  }
  
  public Item handleItemToServer(Item item) {
    if (item == null)
      return null; 
    super.handleItemToServer(item);
    CompoundTag tag = item.getTag();
    if (tag == null)
      return item; 
    EntityIdRewriter.toServerItem(item, true);
    if (tag.contains(this.nbtTagName + "|ench"))
      this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, false); 
    if (tag.contains(this.nbtTagName + "|StoredEnchantments"))
      this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, true); 
    return item;
  }
  
  private boolean isLlama(UserConnection user) {
    WindowTracker tracker = (WindowTracker)user.get(WindowTracker.class);
    if (tracker.getInventory() != null && tracker.getInventory().equals("EntityHorse")) {
      EntityTracker.ProtocolEntityTracker entTracker = ((EntityTracker)user.get(EntityTracker.class)).get(getProtocol());
      EntityTracker.StoredEntity storedEntity = entTracker.getEntity(tracker.getEntityId());
      return (storedEntity != null && storedEntity.getType().is((EntityType)Entity1_11Types.EntityType.LIAMA));
    } 
    return false;
  }
  
  private Optional<ChestedHorseStorage> getChestedHorse(UserConnection user) {
    WindowTracker tracker = (WindowTracker)user.get(WindowTracker.class);
    if (tracker.getInventory() != null && tracker.getInventory().equals("EntityHorse")) {
      EntityTracker.ProtocolEntityTracker entTracker = ((EntityTracker)user.get(EntityTracker.class)).get(getProtocol());
      EntityTracker.StoredEntity storedEntity = entTracker.getEntity(tracker.getEntityId());
      if (storedEntity != null)
        return (Optional)Optional.of(storedEntity.get(ChestedHorseStorage.class)); 
    } 
    return Optional.empty();
  }
  
  private int getNewSlotId(ChestedHorseStorage storage, int slotId) {
    int totalSlots = !storage.isChested() ? 38 : 53;
    int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
    int startNonExistingFormula = 2 + 3 * strength;
    int offsetForm = 15 - 3 * strength;
    if (slotId >= startNonExistingFormula && totalSlots > slotId + offsetForm)
      return offsetForm + slotId; 
    if (slotId == 1)
      return 0; 
    return slotId;
  }
  
  private int getOldSlotId(ChestedHorseStorage storage, int slotId) {
    int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
    int startNonExistingFormula = 2 + 3 * strength;
    int endNonExistingFormula = 2 + 3 * (storage.isChested() ? 5 : 0);
    int offsetForm = endNonExistingFormula - startNonExistingFormula;
    if (slotId == 1 || (slotId >= startNonExistingFormula && slotId < endNonExistingFormula))
      return 0; 
    if (slotId >= endNonExistingFormula)
      return slotId - offsetForm; 
    if (slotId == 0)
      return 1; 
    return slotId;
  }
  
  private Item getNewItem(ChestedHorseStorage storage, int slotId, Item current) {
    int strength = storage.isChested() ? storage.getLiamaStrength() : 0;
    int startNonExistingFormula = 2 + 3 * strength;
    int endNonExistingFormula = 2 + 3 * (storage.isChested() ? 5 : 0);
    if (slotId >= startNonExistingFormula && slotId < endNonExistingFormula)
      return new Item(166, (byte)1, (short)0, getNamedTag(ChatColor.RED + "SLOT DISABLED")); 
    if (slotId == 1)
      return null; 
    return current;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_10to1_11\packets\BlockItemPackets1_11.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */