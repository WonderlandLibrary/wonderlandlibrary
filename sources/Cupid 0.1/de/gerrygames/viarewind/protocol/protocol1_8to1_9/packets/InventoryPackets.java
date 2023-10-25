package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.items.ItemRewriter;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Windows;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonElement;

public class InventoryPackets {
  public static void register(Protocol protocol) {
    protocol.registerOutgoing(State.PLAY, 17, 50);
    protocol.registerOutgoing(State.PLAY, 18, 46, new PacketRemapper() {
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
    protocol.registerOutgoing(State.PLAY, 19, 45, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map(Type.STRING);
            map(Type.COMPONENT);
            map(Type.UNSIGNED_BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String type = (String)packetWrapper.get(Type.STRING, 0);
                    if (type.equals("EntityHorse"))
                      packetWrapper.passthrough(Type.INT); 
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    String windowType = (String)packetWrapper.get(Type.STRING, 0);
                    ((Windows)packetWrapper.user().get(Windows.class)).put(windowId, windowType);
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    String type = (String)packetWrapper.get(Type.STRING, 0);
                    if (type.equalsIgnoreCase("minecraft:shulker_box"))
                      packetWrapper.set(Type.STRING, 0, type = "minecraft:container"); 
                    String name = ((JsonElement)packetWrapper.get(Type.COMPONENT, 0)).toString();
                    if (name.equalsIgnoreCase("{\"translate\":\"container.shulkerBox\"}"))
                      packetWrapper.set(Type.COMPONENT, 0, GsonUtil.getJsonParser().parse("{\"text\":\"Shulker Box\"}")); 
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 20, 48, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    Item[] items = (Item[])packetWrapper.read(Type.ITEM_ARRAY);
                    for (int i = 0; i < items.length; i++)
                      items[i] = ItemRewriter.toClient(items[i]); 
                    if (windowId == 0 && items.length == 46) {
                      Item[] old = items;
                      items = new Item[45];
                      System.arraycopy(old, 0, items, 0, 45);
                    } else {
                      String type = ((Windows)packetWrapper.user().get(Windows.class)).get(windowId);
                      if (type != null && type.equalsIgnoreCase("minecraft:brewing_stand")) {
                        System.arraycopy(items, 0, ((Windows)packetWrapper.user().get(Windows.class)).getBrewingItems(windowId), 0, 4);
                        Windows.updateBrewingStand(packetWrapper.user(), items[4], windowId);
                        Item[] old = items;
                        items = new Item[old.length - 1];
                        System.arraycopy(old, 0, items, 0, 4);
                        System.arraycopy(old, 5, items, 4, old.length - 5);
                      } 
                    } 
                    packetWrapper.write(Type.ITEM_ARRAY, items);
                  }
                });
          }
        });
    protocol.registerOutgoing(State.PLAY, 21, 49);
    protocol.registerOutgoing(State.PLAY, 22, 47, new PacketRemapper() {
          public void registerMap() {
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map(Type.ITEM);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.set(Type.ITEM, 0, ItemRewriter.toClient((Item)packetWrapper.get(Type.ITEM, 0)));
                    byte windowId = ((Byte)packetWrapper.get(Type.BYTE, 0)).byteValue();
                    short slot = ((Short)packetWrapper.get((Type)Type.SHORT, 0)).shortValue();
                    if (windowId == 0 && slot == 45) {
                      packetWrapper.cancel();
                      return;
                    } 
                    String type = ((Windows)packetWrapper.user().get(Windows.class)).get((short)windowId);
                    if (type == null)
                      return; 
                    if (type.equalsIgnoreCase("minecraft:brewing_stand"))
                      if (slot > 4) {
                        slot = (short)(slot - 1);
                        packetWrapper.set((Type)Type.SHORT, 0, Short.valueOf(slot));
                      } else {
                        if (slot == 4) {
                          packetWrapper.cancel();
                          Windows.updateBrewingStand(packetWrapper.user(), (Item)packetWrapper.get(Type.ITEM, 0), (short)windowId);
                          return;
                        } 
                        ((Windows)packetWrapper.user().get(Windows.class)).getBrewingItems((short)windowId)[slot] = (Item)packetWrapper.get(Type.ITEM, 0);
                      }  
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 8, 13, new PacketRemapper() {
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
    protocol.registerIncoming(State.PLAY, 7, 14, new PacketRemapper() {
          public void registerMap() {
            map(Type.UNSIGNED_BYTE);
            map((Type)Type.SHORT);
            map(Type.BYTE);
            map((Type)Type.SHORT);
            map(Type.BYTE, (Type)Type.VAR_INT);
            map(Type.ITEM);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.set(Type.ITEM, 0, ItemRewriter.toServer((Item)packetWrapper.get(Type.ITEM, 0)));
                  }
                });
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    short windowId = ((Short)packetWrapper.get(Type.UNSIGNED_BYTE, 0)).shortValue();
                    Windows windows = (Windows)packetWrapper.user().get(Windows.class);
                    String type = windows.get(windowId);
                    if (type == null)
                      return; 
                    if (type.equalsIgnoreCase("minecraft:brewing_stand")) {
                      short slot = ((Short)packetWrapper.get((Type)Type.SHORT, 0)).shortValue();
                      if (slot > 3) {
                        slot = (short)(slot + 1);
                        packetWrapper.set((Type)Type.SHORT, 0, Short.valueOf(slot));
                      } 
                    } 
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 24, 16, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.SHORT);
            map(Type.ITEM);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.set(Type.ITEM, 0, ItemRewriter.toServer((Item)packetWrapper.get(Type.ITEM, 0)));
                  }
                });
          }
        });
    protocol.registerIncoming(State.PLAY, 6, 17);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\packets\InventoryPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */