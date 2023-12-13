// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.HashMap;
import com.viaversion.viaversion.api.connection.StoredObject;

public class Windows extends StoredObject
{
    private HashMap<Short, String> types;
    private HashMap<Short, Item[]> brewingItems;
    
    public Windows(final UserConnection user) {
        super(user);
        this.types = new HashMap<Short, String>();
        this.brewingItems = new HashMap<Short, Item[]>();
    }
    
    public String get(final short windowId) {
        return this.types.get(windowId);
    }
    
    public void put(final short windowId, final String type) {
        this.types.put(windowId, type);
    }
    
    public void remove(final short windowId) {
        this.types.remove(windowId);
        this.brewingItems.remove(windowId);
    }
    
    public Item[] getBrewingItems(final short windowId) {
        return this.brewingItems.computeIfAbsent(windowId, key -> new Item[] { new DataItem(), new DataItem(), new DataItem(), new DataItem() });
    }
    
    public static void updateBrewingStand(final UserConnection user, final Item blazePowder, final short windowId) {
        if (blazePowder != null && blazePowder.identifier() != 377) {
            return;
        }
        final int amount = (blazePowder == null) ? 0 : blazePowder.amount();
        final PacketWrapper openWindow = PacketWrapper.create(45, null, user);
        openWindow.write(Type.UNSIGNED_BYTE, windowId);
        openWindow.write(Type.STRING, "minecraft:brewing_stand");
        openWindow.write(Type.STRING, "[{\"translate\":\"container.brewing\"},{\"text\":\": \",\"color\":\"dark_gray\"},{\"text\":\"§4" + amount + " \",\"color\":\"dark_red\"},{\"translate\":\"item.blazePowder.name\",\"color\":\"dark_red\"}]");
        openWindow.write(Type.UNSIGNED_BYTE, (Short)420);
        PacketUtil.sendPacket(openWindow, Protocol1_8TO1_9.class);
        final Item[] items = user.get(Windows.class).getBrewingItems(windowId);
        for (int i = 0; i < items.length; ++i) {
            final PacketWrapper setSlot = PacketWrapper.create(47, null, user);
            setSlot.write(Type.BYTE, (byte)windowId);
            setSlot.write(Type.SHORT, (short)i);
            setSlot.write(Type.ITEM, items[i]);
            PacketUtil.sendPacket(setSlot, Protocol1_8TO1_9.class);
        }
    }
}
