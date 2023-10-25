package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.HashMap;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;

public class Windows extends StoredObject {
  private HashMap<Short, String> types = new HashMap<>();
  
  private HashMap<Short, Item[]> brewingItems = (HashMap)new HashMap<>();
  
  public Windows(UserConnection user) {
    super(user);
  }
  
  public String get(short windowId) {
    return this.types.get(Short.valueOf(windowId));
  }
  
  public void put(short windowId, String type) {
    this.types.put(Short.valueOf(windowId), type);
  }
  
  public void remove(short windowId) {
    this.types.remove(Short.valueOf(windowId));
    this.brewingItems.remove(Short.valueOf(windowId));
  }
  
  public Item[] getBrewingItems(short windowId) {
    return this.brewingItems.computeIfAbsent(Short.valueOf(windowId), key -> new Item[] { new Item(), new Item(), new Item(), new Item() });
  }
  
  public static void updateBrewingStand(UserConnection user, Item blazePowder, short windowId) {
    if (blazePowder != null && blazePowder.getIdentifier() != 377)
      return; 
    int amount = (blazePowder == null) ? 0 : blazePowder.getAmount();
    PacketWrapper openWindow = new PacketWrapper(45, null, user);
    openWindow.write(Type.UNSIGNED_BYTE, Short.valueOf(windowId));
    openWindow.write(Type.STRING, "minecraft:brewing_stand");
    openWindow.write(Type.STRING, "[{\"translate\":\"container.brewing\"},{\"text\":\": \",\"color\":\"dark_gray\"},{\"text\":\"§4" + amount + " \",\"color\":\"dark_red\"},{\"translate\":\"item.blazePowder.name\",\"color\":\"dark_red\"}]");
    openWindow.write(Type.UNSIGNED_BYTE, Short.valueOf((short)420));
    PacketUtil.sendPacket(openWindow, Protocol1_8TO1_9.class);
    Item[] items = ((Windows)user.get(Windows.class)).getBrewingItems(windowId);
    for (int i = 0; i < items.length; i++) {
      PacketWrapper setSlot = new PacketWrapper(47, null, user);
      setSlot.write(Type.BYTE, Byte.valueOf((byte)windowId));
      setSlot.write((Type)Type.SHORT, Short.valueOf((short)i));
      setSlot.write(Type.ITEM, items[i]);
      PacketUtil.sendPacket(setSlot, Protocol1_8TO1_9.class);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\storage\Windows.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */