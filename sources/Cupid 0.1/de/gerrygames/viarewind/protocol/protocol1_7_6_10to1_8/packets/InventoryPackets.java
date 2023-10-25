package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.utils.ChatUtil;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;
import us.myles.viaversion.libs.gson.JsonElement;

public class InventoryPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 45, 45, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = ((Short)packetWrapper.passthrough(Type.UNSIGNED_BYTE)).shortValue();
                    String windowType = (String)packetWrapper.read(Type.STRING);
                    short windowtypeId = (short)Windows.getInventoryType(windowType);
                    ((Windows)packetWrapper.user().get(Windows.class)).types.put(Short.valueOf(windowId), Short.valueOf(windowtypeId));
                    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf(windowtypeId));
                    JsonElement titleComponent = (JsonElement)packetWrapper.read(Type.COMPONENT);
                    String title = ChatUtil.jsonToLegacy(titleComponent);
                    title = ChatUtil.removeUnusedColor(title, '8');
                    if (title.length() > 32)
                      title = title.substring(0, 32); 
                    packetWrapper.write(Type.STRING, title);
                    packetWrapper.passthrough(Type.UNSIGNED_BYTE);
                    packetWrapper.write(Type.BOOLEAN, Boolean.valueOf(true));
                    if (windowtypeId == 11)
                      packetWrapper.passthrough(Type.INT); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 46, 46, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowsId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    ((Windows)packetWrapper.user().get(Windows.class)).remove(windowsId);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 47, 47, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = (short)((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                    short windowType = ((Windows)packetWrapper.user().get(Windows.class)).get(windowId);
                    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)windowId));
                    short slot = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    if (windowType == 4) {
                      if (slot == 1) {
                        packetWrapper.cancel();
                        return;
                      } 
                      if (slot >= 2)
                        slot = (short)(slot - 1); 
                    } 
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf(slot));
                  }
                });
            map(Type.ITEM, Types1_7_6_10.COMPRESSED_NBT_ITEM);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Item item = (Item)packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                    ItemRewriter.toClient(item);
                    packetWrapper.set(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0, item);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = (short)((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    if (windowId != 0)
                      return; 
                    short slot = ((Short)packetWrapper.get((Type)Type.SHORT, 0)).shortValue();
                    if (slot < 5 || slot > 8)
                      return; 
                    Item item = (Item)packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM, 0);
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    UUID uuid = ((ProtocolInfo)packetWrapper.user().get(ProtocolInfo.class)).getUuid();
                    Item[] equipment = tracker.getPlayerEquipment(uuid);
                    if (equipment == null)
                      tracker.setPlayerEquipment(uuid, equipment = new Item[5]); 
                    equipment[9 - slot] = item;
                    if (tracker.getGamemode() == 3)
                      packetWrapper.cancel(); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 48, 48, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = ((Short)packetWrapper.read(Type.UNSIGNED_BYTE)).shortValue();
                    short windowType = ((Windows)packetWrapper.user().get(Windows.class)).get(windowId);
                    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf(windowId));
                    Item[] items = (Item[])packetWrapper.read(Type.ITEM_ARRAY);
                    if (windowType == 4) {
                      Item[] old = items;
                      items = new Item[old.length - 1];
                      items[0] = old[0];
                      System.arraycopy(old, 2, items, 1, old.length - 3);
                    } 
                    for (int i = 0; i < items.length; ) {
                      items[i] = ItemRewriter.toClient(items[i]);
                      i++;
                    } 
                    packetWrapper.write(Types1_7_6_10.COMPRESSED_NBT_ITEM_ARRAY, items);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    if (windowId != 0)
                      return; 
                    Item[] items = (Item[])packetWrapper.get(Types1_7_6_10.COMPRESSED_NBT_ITEM_ARRAY, 0);
                    EntityTracker tracker = (EntityTracker)packetWrapper.user().get(EntityTracker.class);
                    UUID uuid = ((ProtocolInfo)packetWrapper.user().get(ProtocolInfo.class)).getUuid();
                    Item[] equipment = tracker.getPlayerEquipment(uuid);
                    if (equipment == null)
                      tracker.setPlayerEquipment(uuid, equipment = new Item[5]); 
                    for (int i = 5; i < 9; i++) {
                      equipment[9 - i] = items[i];
                      if (tracker.getGamemode() == 3)
                        items[i] = null; 
                    } 
                    if (tracker.getGamemode() == 3) {
                      GameProfileStorage.GameProfile profile = ((GameProfileStorage)packetWrapper.user().get(GameProfileStorage.class)).get(uuid);
                      if (profile != null)
                        items[5] = profile.getSkull(); 
                    } 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 49, 49, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.SHORT);
            map((Type)Type.SHORT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    Windows windows = (Windows)packetWrapper.user().get(Windows.class);
                    short windowType = windows.get(windowId);
                    short property = ((Short)packetWrapper.get((Type)Type.SHORT, 0)).shortValue();
                    short value = ((Short)packetWrapper.get((Type)Type.SHORT, 1)).shortValue();
                    if (windowType == -1)
                      return; 
                    if (windowType == 2) {
                      Windows.Furnace furnace = windows.furnace.computeIfAbsent(Short.valueOf(windowId), x -> new Windows.Furnace());
                      if (property == 0 || property == 1) {
                        if (property == 0) {
                          furnace.setFuelLeft(value);
                        } else {
                          furnace.setMaxFuel(value);
                        } 
                        if (furnace.getMaxFuel() == 0) {
                          packetWrapper.cancel();
                          return;
                        } 
                        value = (short)(200 * furnace.getFuelLeft() / furnace.getMaxFuel());
                        packetWrapper.set((Type)Type.SHORT, 0, Short.valueOf((short)1));
                        packetWrapper.set((Type)Type.SHORT, 1, Short.valueOf(value));
                      } else if (property == 2 || property == 3) {
                        if (property == 2) {
                          furnace.setProgress(value);
                        } else {
                          furnace.setMaxProgress(value);
                        } 
                        if (furnace.getMaxProgress() == 0) {
                          packetWrapper.cancel();
                          return;
                        } 
                        value = (short)(200 * furnace.getProgress() / furnace.getMaxProgress());
                        packetWrapper.set((Type)Type.SHORT, 0, Short.valueOf((short)0));
                        packetWrapper.set((Type)Type.SHORT, 1, Short.valueOf(value));
                      } 
                    } else if (windowType == 4) {
                      if (property > 2) {
                        packetWrapper.cancel();
                        return;
                      } 
                    } else if (windowType == 8) {
                      windows.levelCost = value;
                      windows.anvilId = windowId;
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 13, 13, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowsId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    ((Windows)packetWrapper.user().get(Windows.class)).remove(windowsId);
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 14, 14, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = (short)((Byte)packetWrapper.read(Type.BYTE)).byteValue();
                    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf(windowId));
                    short windowType = ((Windows)packetWrapper.user().get(Windows.class)).get(windowId);
                    short slot = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    if (windowType == 4 && 
                      slot > 0)
                      slot = (short)(slot + 1); 
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf(slot));
                  }
                });
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map(Type.BYTE);
            map(Types1_7_6_10.COMPRESSED_NBT_ITEM, Type.ITEM);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Item item = (Item)packetWrapper.get(Type.ITEM, 0);
                    ItemRewriter.toServer(item);
                    packetWrapper.set(Type.ITEM, 0, item);
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 15, 15, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map(Type.BOOLEAN);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int action = ((Short)packetWrapper.get((Type)Type.SHORT, 0)).shortValue();
                    if (action == -89)
                      packetWrapper.cancel(); 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 16, 16, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.SHORT);
            map(Types1_7_6_10.COMPRESSED_NBT_ITEM, Type.ITEM);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    Item item = (Item)packetWrapper.get(Type.ITEM, 0);
                    ItemRewriter.toServer(item);
                    packetWrapper.set(Type.ITEM, 0, item);
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\packets\InventoryPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */