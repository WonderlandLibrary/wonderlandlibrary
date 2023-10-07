// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import com.viaversion.viaversion.api.protocol.Protocol;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.NamedTextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.HashMap;
import com.viaversion.viaversion.api.connection.StoredObject;

public class Windows extends StoredObject
{
    private final HashMap<Short, String> types;
    private final HashMap<Short, Item[]> brewingItems;
    
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
        final PacketWrapper openWindow = PacketWrapper.create(ClientboundPackets1_8.OPEN_WINDOW, user);
        openWindow.write(Type.UNSIGNED_BYTE, windowId);
        openWindow.write(Type.STRING, "minecraft:brewing_stand");
        final Component title = Component.empty().append(Component.translatable("container.brewing")).append(Component.text(": ", NamedTextColor.DARK_GRAY)).append(Component.text(amount + " ", NamedTextColor.DARK_RED)).append(Component.translatable("item.blazePowder.name", NamedTextColor.DARK_RED));
        openWindow.write(Type.COMPONENT, GsonComponentSerializer.colorDownsamplingGson().serializeToTree(title));
        openWindow.write(Type.UNSIGNED_BYTE, (Short)420);
        PacketUtil.sendPacket(openWindow, Protocol1_8TO1_9.class);
        final Item[] items = user.get(Windows.class).getBrewingItems(windowId);
        for (int i = 0; i < items.length; ++i) {
            final PacketWrapper setSlot = PacketWrapper.create(ClientboundPackets1_8.SET_SLOT, user);
            setSlot.write(Type.UNSIGNED_BYTE, windowId);
            setSlot.write(Type.SHORT, (short)i);
            setSlot.write(Type.ITEM, items[i]);
            PacketUtil.sendPacket(setSlot, Protocol1_8TO1_9.class);
        }
    }
}
